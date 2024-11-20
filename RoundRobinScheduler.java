import java.util.*;
import java.util.stream.Collectors;

class Process {
    String processId;
    int at;  // Arrival time
    int bt;  // Burst time
    int ct;  // Completion time
    int tat; // Turnaround time
    int wt;  // Waiting time
    int originalBt; // Original burst time

    public Process(String processId, int at, int bt) {
        this.processId = processId;
        this.at = at;
        this.bt = bt;
        this.originalBt = bt;
        this.ct = -1;
        this.tat = -1;
        this.wt = -1;
    }
}

public class RoundRobinScheduler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcess = scanner.nextInt();
        int timeQuantum = 2;

        List<Process> procArray = new ArrayList<>();
        
        // User input
        for (int i = 0; i < numProcess; i++) {
            System.out.print("Enter the process-id, arrival time, burst time all separated by spaces: ");
            String processId = scanner.next();
            int at = scanner.nextInt();
            int bt = scanner.nextInt();
            procArray.add(new Process(processId, at, bt));
            System.out.println();
        }

        // Sort on the basis of arrival time
        procArray.sort(Comparator.comparingInt(a -> a.at));

        int curTime = 0;
        int completedProcess = 0;
        Queue<Integer> readyQueue = new LinkedList<>();
        Set<Integer> inQueue = new HashSet<>(); // Track indices of processes in the queue
        List<String> ganttChartProcesses = new ArrayList<>();
        List<Integer> ganttChartTimes = new ArrayList<>();
        ganttChartTimes.add(curTime);

        // Initialize the ready queue with processes that have arrived at the initial time
        for (int i = 0; i < numProcess; i++) {
            if (procArray.get(i).at <= curTime) {
                readyQueue.add(i);
                inQueue.add(i);
            }
        }

        while (completedProcess < numProcess) {
            if (readyQueue.isEmpty()) {
                curTime++;
                // Add processes to the ready queue that have arrived by current time
                for (int i = 0; i < numProcess; i++) {
                    if (procArray.get(i).at == curTime && !inQueue.contains(i)) {
                        readyQueue.add(i);
                        inQueue.add(i);
                    }
                }
            } else {
                int idx = readyQueue.poll();
                inQueue.remove(idx);
                Process curProcess = procArray.get(idx);

                ganttChartProcesses.add(curProcess.processId);

                if (curProcess.bt <= timeQuantum) {
                    curTime += curProcess.bt;
                    curProcess.bt = 0;
                    curProcess.ct = curTime;
                    completedProcess++;
                } else {
                    curProcess.bt -= timeQuantum;
                    curTime += timeQuantum;
                }

                ganttChartTimes.add(curTime);

                // Add newly arrived processes to the ready queue
                for (int i = 0; i < numProcess; i++) {
                    Process p = procArray.get(i);
                    if (p.at > curProcess.at && p.at <= curTime && !inQueue.contains(i) && p.bt > 0) {
                        readyQueue.add(i);
                        inQueue.add(i);
                    }
                }

                // If the process is not yet completed, re-add it to the ready queue
                if (curProcess.bt > 0) {
                    readyQueue.add(idx);
                    inQueue.add(idx);
                }
            }
        }

        for (Process process : procArray) {
            process.tat = process.ct - process.at;
            process.wt = process.tat - process.originalBt;
        }

        // Printing the output
        System.out.println("P-id\tAT\tBT\tCT\tTAT\tWT");
        for (Process process : procArray) {
            System.out.println(process.processId + "\t" + process.at + "\t" + process.originalBt + "\t" + process.ct + "\t" + process.tat + "\t" + process.wt);
        }

        // Calculate average Waiting Time and Completion Time
        double averageWT = procArray.stream().collect(Collectors.averagingDouble(p -> p.wt));
        double averageCT = procArray.stream().collect(Collectors.averagingDouble(p -> p.ct));

        System.out.println("Average Waiting Time (WT): " + averageWT);
        System.out.println("Average Completion Time (CT): " + averageCT);

        // Display Gantt Chart
        displayGanttChart(ganttChartProcesses, ganttChartTimes);

        scanner.close();
    }

    private static void displayGanttChart(List<String> ganttChartProcesses, List<Integer> ganttChartTimes) {
        System.out.println("\nGantt Chart:");
        StringBuilder processBar = new StringBuilder();
        StringBuilder timeBar = new StringBuilder();

        for (int i = 0; i < ganttChartProcesses.size(); i++) {
            processBar.append(" | P").append(ganttChartProcesses.get(i));
            timeBar.append(ganttChartTimes.get(i)).append(" -> ");
        }
        timeBar.append(ganttChartTimes.get(ganttChartTimes.size() - 1));

        processBar.append(" |");

        System.out.println(processBar);
        System.out.println(timeBar);
    }
}
