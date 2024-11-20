import java.util.*;

class Process {
    int pid;
    int burstTime;
    int waitingTime;
    int turnaroundTime;
    int completionTime;

    public Process(int pid, int burstTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
    }
}

public class SJF_NonPreemptive_Scheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();
        int n, pid, burstTime;

        System.out.print("Enter number of processes: ");
        n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("\nEnter process ID: ");
            pid = scanner.nextInt();
            System.out.print("Enter burst time: ");
            burstTime = scanner.nextInt();
            processes.add(new Process(pid, burstTime));
        }

        sjfNonPreemptive(new ArrayList<>(processes));

        scanner.close();
    }

    private static void displayGanttChart(List<int[]> ganttChart) {
        System.out.println("\nGantt Chart:");
        for (int[] entry : ganttChart) {
            System.out.print(" | P" + entry[0]);
        }
        System.out.println(" |");
        System.out.print("0");
        for (int[] entry : ganttChart) {
            System.out.print(" -> " + entry[1]);
        }
        System.out.println();
    }

    private static void displayResults(List<Process> processes) {
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        System.out.println("\n\nPID\tBurst\tWaiting\tTurnaround\tCompletion");
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
            System.out.println("P" + process.pid + "\t" + process.burstTime + "\t"
                    + process.waitingTime + "\t" + process.turnaroundTime + "\t\t" + process.completionTime);
        }
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    private static void sjfNonPreemptive(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.burstTime));
        int currentTime = 0;
        List<int[]> ganttChart = new ArrayList<>();
        List<Process> completed = new ArrayList<>();

        for (Process process : processes) {
            currentTime += process.burstTime;
            process.completionTime = currentTime;
            process.turnaroundTime = process.completionTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            completed.add(process);
            ganttChart.add(new int[]{process.pid, currentTime});
        }
        displayGanttChart(ganttChart);
        displayResults(processes);
    }
}
