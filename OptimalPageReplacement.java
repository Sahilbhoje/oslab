import java.util.ArrayList;
import java.util.Scanner;

public class OptimalPageReplacement {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Input number of frames
            System.out.print("Enter the number of frames: ");
            int numOfFrames = scanner.nextInt();
            
            // Input reference string
            System.out.print("Enter the reference string (comma-separated values): ");
            String refStringInput = scanner.next();
            String[] refStringArray = refStringInput.split(",");
            int[] referenceString = new int[refStringArray.length];
            for (int i = 0; i < refStringArray.length; i++) {
                referenceString[i] = Integer.parseInt(refStringArray[i]);
            }
            
            // Optimal Page Replacement Algorithm
            ArrayList<Integer> frames = new ArrayList<>(numOfFrames);
            int pageFaults = 0;
            int hits = 0;

            for (int i = 0; i < referenceString.length; i++) {
                int page = referenceString[i];
                if (frames.contains(page)) {
                    // Page hit
                    hits++;
                } else {
                    // Page fault
                    if (frames.size() == numOfFrames) {
                        // Find the page to replace using the optimal strategy
                        int farthest = i + 1;
                        int pageToReplace = -1;
                        for (int framePage : frames) {
                            int j;
                            for (j = i + 1; j < referenceString.length; j++) {
                                if (referenceString[j] == framePage) {
                                    if (j > farthest) {
                                        farthest = j;
                                        pageToReplace = framePage;
                                    }
                                    break;
                                }
                            }
                            if (j == referenceString.length) {
                                pageToReplace = framePage;
                                break;
                            }
                        }
                        frames.remove((Integer) pageToReplace);
                    }
                    frames.add(page);
                    pageFaults++;
                }
            }
            
            // Calculate hit ratio and miss ratio
            int totalRequests = referenceString.length;
            double hitRatio = (double) hits / totalRequests;
            double missRatio = (double) pageFaults / totalRequests;
            
            // Output number of page faults, hits, hit ratio, and miss ratio
            System.out.println("Total number of page faults: " + pageFaults);
            System.out.println("Total number of hits: " + hits);
            System.out.println("Hit ratio: " + hitRatio);
            System.out.println("Miss ratio: " + missRatio);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
