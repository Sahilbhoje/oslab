import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileIOExample {
    public static void main(String[] args) {
        String fileName = "example.txt";
        String dataToWrite = "Hello, System Calls!";
        byte[] buffer = new byte[100];

        // Demonstrate 5 system calls: open, write, read, stat, and close

        // Writing data to the file (open, write, close)
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            byte[] data = dataToWrite.getBytes();
            fileOutputStream.write(data);  // write system call
            System.out.println("Wrote " + data.length + " bytes to the file.");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            System.exit(1);
        }

        // Reading data from the file (open, read, close)
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            int bytesRead = fileInputStream.read(buffer);  // read system call
            if (bytesRead == -1) {
                System.err.println("Error reading from file");
                System.exit(1);
            }
            String fileContent = new String(buffer, 0, bytesRead);
            System.out.println("Read from file: " + fileContent);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            System.exit(1);
        }

        // Demonstrate the stat system call (checking file properties)
        Path filePath = Paths.get(fileName);
        try {
            File file = new File(fileName);
            if (file.exists()) {
                System.out.println("File exists: " + fileName);
                System.out.println("File size: " + Files.size(filePath) + " bytes");  // stat system call
                System.out.println("Is file readable: " + Files.isReadable(filePath));  // stat system call
                System.out.println("Is file writable: " + Files.isWritable(filePath));  // stat system call
            } else {
                System.err.println("File does not exist: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("Error accessing file properties: " + e.getMessage());
        }
    }
}
