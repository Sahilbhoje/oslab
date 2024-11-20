import java.util.*;

class Process {
    int pid;
    int burstTime;
    int arrivalTime;
    int waitingTime;
    int turnaroundTime;
    int completionTime;

    public Process(int pid, int burstTime, int arrivalTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
    }
}

public class FCFS_Scheduling {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();
        int n, pid, burstTime, arrivalTime;

        System.out.print("Enter number of processes: ");
        n = scanner.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.print("\nEnter process ID: ");
            pid = scanner.nextInt();
            System.out.print("Enter burst time: ");
            burstTime = scanner.nextInt();
            System.out.print("Enter arrival time: ");
            arrivalTime = scanner.nextInt();
            processes.add(new Process(pid, burstTime, arrivalTime));
        }

        fcfs(new ArrayList<>(processes));

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
        System.out.println("\n\nPID\tArrival\tBurst\tWaiting\tTurnaround\tCompletion");
        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
            System.out.println("P" + process.pid + "\t" + process.arrivalTime + "\t" + process.burstTime + "\t"
                    + process.waitingTime + "\t" + process.turnaroundTime + "\t\t" + process.completionTime);
        }
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    private static void fcfs(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<int[]> ganttChart = new ArrayList<>();

        for (Process process : processes) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            process.completionTime = currentTime + process.burstTime;
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            currentTime = process.completionTime;
            ganttChart.add(new int[]{process.pid, currentTime});
        }
        displayGanttChart(ganttChart);
        displayResults(processes);
    }
}
