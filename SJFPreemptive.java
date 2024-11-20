import java.util.*;

class Process {
    int pid;
    int burstTime;
    int arrivalTime;
    int waitingTime;
    int turnaroundTime;
    int completionTime;
    int remainingTime;

    public Process(int pid, int burstTime, int arrivalTime) {
        this.pid = pid;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.waitingTime = 0;
        this.turnaroundTime = 0;
        this.completionTime = 0;
        this.remainingTime = burstTime;
    }
}

public class SJFPreemptive {
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

        sjfPreemptive(new ArrayList<>(processes));
        scanner.close();
    }

    private static void displayGanttChart(List<int[]> ganttChart) {
        System.out.println("\nGantt Chart:");
        for (int[] entry : ganttChart) {
            System.out.print("| P" + entry[0] + " ");
        }
        System.out.println("|");
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
                    + process.waitingTime + "\t" + process.turnaroundTime + "\t\t"
                    + process.completionTime);
        }
        double avgWaitingTime = (double) totalWaitingTime / processes.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.println("\nAverage Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
    }

    private static void sjfPreemptive(List<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        List<int[]> ganttChart = new ArrayList<>();
        List<Process> completed = new ArrayList<>();
        Process lastProcess = null;

        while (completed.size() < processes.size()) {
            List<Process> availableProcesses = new ArrayList<>();
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && !completed.contains(process)) {
                    availableProcesses.add(process);
                }
            }

            if (!availableProcesses.isEmpty()) {
                Process currentProcess = availableProcesses.stream()
                        .min(Comparator.comparingInt(p -> p.remainingTime))
                        .orElseThrow(NoSuchElementException::new);

                if (lastProcess == null || currentProcess.pid != lastProcess.pid) {
                    ganttChart.add(new int[]{currentProcess.pid, currentTime});
                }

                currentProcess.remainingTime--;
                currentTime++;

                if (currentProcess.remainingTime == 0) {
                    currentProcess.completionTime = currentTime;
                    currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
                    currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                    completed.add(currentProcess);
                }

                lastProcess = currentProcess;
            } else {
                // CPU is idle, increment time and record idle period in Gantt chart
                currentTime++;
                if (lastProcess == null || lastProcess.pid != -1) {
                    ganttChart.add(new int[]{-1, currentTime});
                }
                lastProcess = new Process(-1, 0, 0); // Representing idle process
            }
        }

        // Add end times to the Gantt chart
        for (int i = 1; i < ganttChart.size(); i++) {
            ganttChart.get(i - 1)[1] = ganttChart.get(i)[1];
        }
        ganttChart.get(ganttChart.size() - 1)[1] = currentTime;

        // Display results
        displayGanttChart(ganttChart);
        displayResults(processes);
    }
}

