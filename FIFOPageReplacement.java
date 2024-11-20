import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class FIFOPageReplacement {
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
            
            // FIFO Page Replacement Algorithm
            Queue<Integer> frames = new LinkedList<>();
            int pageFaults = 0;
            int hits = 0;

            for (int page : referenceString) {
                if (!frames.contains(page)) {
                    if (frames.size() == numOfFrames) {
                        frames.poll();  // Remove the oldest page from the frames
                    }
                    frames.add(page);
                    pageFaults++;
                } else {
                    hits++;
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
