import java.util.Scanner;

public class BankersAlgorithm {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the number of processes: ");
            int n = scanner.nextInt();
            System.out.print("Enter the number of resource types: ");
            int m = scanner.nextInt();

            int[][] max = new int[n][m];
            int[][] allocation = new int[n][m];
            int[][] need = new int[n][m];
            int[] available = new int[m];
            boolean[] finish = new boolean[n];
            int[] safeSeq = new int[n];

            // Input maximum resource instances for each resource type:
            System.out.println("Enter the maximum resource instances for each resource type:");
            for (int i = 0; i < m; i++) {
                System.out.print("Resource type " + i + ": ");
                available[i] = scanner.nextInt();
            }

            // Input maximum resource allocation for each process:
            System.out.println("Enter the maximum resource allocation for each process:");
            for (int i = 0; i < n; i++) {
                System.out.print("Process " + i + ": ");
                for (int j = 0; j < m; j++) {
                    max[i][j] = scanner.nextInt();
                }
                finish[i] = false; // Initialize all processes as unfinished
            }

            // Input the current resource allocation for each process:
            System.out.println("Enter the current resource allocation for each process:");
            for (int i = 0; i < n; i++) {
                System.out.print("Process " + i + ": ");
                for (int j = 0; j < m; j++) {
                    allocation[i][j] = scanner.nextInt();
                    available[j] -= allocation[i][j]; // Update available resources
                }
            }

            // Calculate the need matrix:
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    need[i][j] = max[i][j] - allocation[i][j];
                }
            }

            // Banker's Algorithm (finding the safe sequence):
            int count = 0;
            while (count < n) {
                boolean found = false;
                for (int i = 0; i < n; i++) {
                    if (!finish[i]) {
                        int j;
                        for (j = 0; j < m; j++) {
                            if (need[i][j] > available[j]) {
                                break;
                            }
                        }
                        if (j == m) {
                            for (int k = 0; k < m; k++) {
                                available[k] += allocation[i][k];
                            }
                            safeSeq[count++] = i;
                            finish[i] = true;
                            found = true;
                        }
                    }
                }
                if (!found) {
                    System.out.println("System is NOT in a safe state.");
                    return; // Exiting the program
                }
            }

            // If all processes are in the safe sequence, print the output:
            System.out.println("System is in a safe state.");
            System.out.print("Safe Sequence: ");
            for (int i = 0; i < n - 1; i++) {
                System.out.print("P" + safeSeq[i] + " -> ");
            }
            System.out.println("P" + safeSeq[n - 1]);
        }
    }
}
