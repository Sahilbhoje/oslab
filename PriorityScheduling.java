import java.util.Scanner;

class Process {
    int pid; // Process ID
    int burstTime; // CPU Burst Time
    int arrivalTime; // Arrival Time
    int priority; // Priority
    int waitingTime; // Waiting Time
    int turnaroundTime; // Turnaround Time
    int remainingTime; // Remaining Time (for Preemptive Scheduling)

    Process(int pid, int burstTime, int arrivalTime, int priority) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }
}

public class PriorityScheduling {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter the number of processes: ");
            int n = sc.nextInt();

            Process[] processes = new Process[n];

            for (int i = 0; i < n; i++) {
                System.out.print("Enter burst time for process " + (i + 1) + ": ");
                int burstTime = sc.nextInt();
                System.out.print("Enter arrival time for process " + (i + 1) + ": ");
                int arrivalTime = sc.nextInt();
                System.out.print("Enter priority for process " + (i + 1) + ": ");
                int priority = sc.nextInt();
                processes[i] = new Process(i + 1, burstTime, arrivalTime, priority);
            }

            System.out.println("\nChoose Scheduling Type:");
            System.out.println("1. Preemptive Priority Scheduling");
            System.out.println("2. Non-Preemptive Priority Scheduling");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    preemptivePriorityScheduling(processes);
                    break;
                case 2:
                    nonPreemptivePriorityScheduling(processes);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    static void preemptivePriorityScheduling(Process[] processes) {
        int n = processes.length;
        int currentTime = 0;
        int completed = 0;

        while (completed != n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime && processes[i].remainingTime > 0 && processes[i].priority < highestPriority) {
                    highestPriority = processes[i].priority;
                    idx = i;
                }
            }

            if (idx != -1) {
                processes[idx].remainingTime--;
                currentTime++;

                if (processes[idx].remainingTime == 0) {
                    completed++;
                    processes[idx].turnaroundTime = currentTime - processes[idx].arrivalTime;
                    processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burstTime;
                }
            } else {
                currentTime++;
            }
        }

        printProcessDetails(processes);
    }

    static void nonPreemptivePriorityScheduling(Process[] processes) {
        int n = processes.length;
        int currentTime = 0;
        int completed = 0;
        boolean[] isCompleted = new boolean[n];

        while (completed != n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (processes[i].arrivalTime <= currentTime && !isCompleted[i] && processes[i].priority < highestPriority) {
                    highestPriority = processes[i].priority;
                    idx = i;
                }
            }

            if (idx != -1) {
                currentTime += processes[idx].burstTime;
                processes[idx].turnaroundTime = currentTime - processes[idx].arrivalTime;
                processes[idx].waitingTime = processes[idx].turnaroundTime - processes[idx].burstTime;
                isCompleted[idx] = true;
                completed++;
            } else {
                currentTime++;
            }
        }

        printProcessDetails(processes);
    }

    static void printProcessDetails(Process[] processes) {
        System.out.println("\nProcess Details:");
        System.out.println("PID\tBurst Time\tArrival Time\tPriority\tWaiting Time\tTurnaround Time");
        for (Process p : processes) {
            System.out.println(p.pid + "\t" + p.burstTime + "\t\t" + p.arrivalTime + "\t\t" + p.priority + "\t\t" + p.waitingTime + "\t\t" + p.turnaroundTime);
        }

        double totalWT = 0, totalTAT = 0;
        for (Process p : processes) {
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }

        System.out.printf("\nAverage Waiting Time: %.2f", (totalWT / processes.length));
        System.out.printf("\nAverage Turnaround Time: %.2f\n", (totalTAT / processes.length));
    }
}