import java.util.*;

class Process {
    int pid;
    int burstTime;
    int priority;
    int waitingTime;
    int turnaroundTime;
    int completionTime;

    public Process(int pid, int burstTime, int priority) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.priority = priority;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
    }
}

public class PrioritySchedulingNonPreemptive {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();
        int n, pid, burstTime, priority;

        System.out.print("Enter number of processes: ");
        n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("\nEnter process ID: ");
            pid = scanner.nextInt();
            System.out.print("Enter burst time: ");
            burstTime = scanner.nextInt();
            System.out.print("Enter priority (lower number = higher priority): ");
            priority = scanner.nextInt();
            processes.add(new Process(pid, burstTime, priority));
        }

        priorityNonPreemptive(new ArrayList<>(processes));
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
        System.out.println("\n\nPID\tBurst\tPriority\tWaiting\tTurnaround\tCompletion");
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
            System.out.println("P" + process.pid + "\t" + process.burstTime + "\t"
                    + process.priority + "\t\t" + process.waitingTime + "\t" + process.turnaroundTime + "\t\t"
                    + process.completionTime);
        }
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    private static void priorityNonPreemptive(List<Process> processes) {
        int currentTime = 0;
        List<int[]> ganttChart = new ArrayList<>();
        List<Process> completed = new ArrayList<>();

        while (completed.size() < processes.size()) {
            List<Process> availableProcesses = new ArrayList<>();
            for (Process process : processes) {
                if (!completed.contains(process)) {
                    availableProcesses.add(process);
                }
            }

            if (!availableProcesses.isEmpty()) {
                Process currentProcess = availableProcesses.stream()
                        .min(Comparator.comparingInt(p -> p.priority))
                        .orElseThrow(NoSuchElementException::new);

                currentTime += currentProcess.burstTime;
                currentProcess.completionTime = currentTime;
                currentProcess.turnaroundTime = currentProcess.completionTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                completed.add(currentProcess);
                ganttChart.add(new int[]{currentProcess.pid, currentTime});
            } else {
                currentTime++;
            }
        }
        displayGanttChart(ganttChart);
        displayResults(processes);
    }
}
