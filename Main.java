package zip_unzip;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
        ArrayList<String> filesList = new ArrayList<>();

        // Initial input of file paths
        System.out.println("Enter file paths (type 'done' to finish):");
        while (true) {
            System.out.print("Enter file path: ");
            String filePathraw = sc.nextLine();
            String filePath = filePathraw.replace("\\", "/");
            if (filePath.equalsIgnoreCase("done")) {
                break;
            }
            filesList.add(filePath);
        }

        // Convert list to array for the rest of the operations
        String[] files = filesList.toArray(new String[0]);

        FileReaderArray filesData = new FileReaderArray(files.length);
        Zipper zipper = new Zipper();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Read Files size");
            System.out.println("2. Zip Files");
            System.out.println("3. Unzip Files");
            System.out.println("4. Add More Files");
            System.out.println("5. Exit");
            System.out.print("Please choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    // Read files
                    System.out.println("Reading files...");
                    for (String path : files) {
                        byte[] fileData = FileReader.readFile(path);
                        if (fileData != null) {
                            filesData.add(fileData);
                        } else {
                            System.out.println("Skipping file due to read error: " + path);
                        }
                    }

                    // Print out details of the files read
                    for (int i = 0; i < filesData.getSize(); i++) {
                        System.out.println("File " + (i + 1) + " read with " + filesData.get(i).length + " bytes.");
                    }
                    break;

                case 2:
                    // Zip files
                    System.out.print("Enter the output zip file name (e.g., output.zip): ");
                    String outputZip = sc.nextLine();
                    zipper.zipFiles(files, outputZip);
                    File zipFile = new File(outputZip);
                    //System.out.println("Files compressed and zipped successfully!");
                    //System.out.println("Size of the zipped file: " + zipFile.length() + " bytes");
                    break;

                case 3:
                    // Unzip files
                    System.out.print("Enter the zip file name to unzip (e.g., output.zip): ");
                    String zipFileName = sc.nextLine();
                    System.out.print("Enter the destination folder to unzip: ");
                    String destinationFolder = sc.nextLine();
                    zipper.unzipFiles(zipFileName, destinationFolder);
                    break;

                case 4:
                    // Add more files
                    System.out.println("Enter additional file paths (type 'done' to finish):");
                    while (true) {
                        System.out.print("Enter file path: ");
                        String additionalFilePathRaw = sc.nextLine();
                        String additionalFilePath = additionalFilePathRaw.replace("\\", "/");
                        if (additionalFilePath.equalsIgnoreCase("done")) {
                            break;
                        }
                        filesList.add(additionalFilePath);
                    }

                    // Update the array of files
                    files = filesList.toArray(new String[0]);
                    System.out.println("Files have been added.");
                    break;

                case 5:
                    // Exit the program
                    System.out.println("Exiting program.");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}



class FileReader {
	
    public static byte[] readFile(String path) {
        byte[] data = null;
        FileInputStream fis = null;
        
        try {
        	
        	// Create a File object for the given path
        	File file = new File(path);

            // Check if the file exists
            if (!file.exists()) {
                throw new IOException("Invalid file path: File does not exist.");
            }
            
            // Open a FileInputStream for the file
            fis = new FileInputStream(path);
            // Get the size of the file
            int fileSize = fis.available();
            // Initialize the byte array to the file size
            data = new byte[fileSize];
            // Read the file data into the byte array
            int bytesRead = fis.read(data);
            
            // Check if the entire file was read
            if (bytesRead != fileSize) {
                throw new IOException("Could not read the entire file");
            }
            
        } 
        catch (IOException e) {
        	
            System.err.println("Error reading file: " + e.getMessage());
        } finally {
        	
            // Ensure the FileInputStream is closed
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println("Error closing file: " + e.getMessage());
                }
            }
        }
        
        // Return the read data
        return data;
    }
}

class FileReaderArray {
    private byte[][] filesData;
    private int size;
    
    // Constructor to initialize the array with a given capacity
    public FileReaderArray(int capacity) {
        filesData = new byte[capacity][];
        size = 0;
    }

    // Add a new byte array to the array
    public void add(byte[] fileData) {
        if (size == filesData.length) {
            // Resize the array if needed
            byte[][] newFilesData = new byte[filesData.length * 2][];
            for (int i = 0; i < size; i++) {
                newFilesData[i] = filesData[i];
            }
            filesData = newFilesData;
        }
        filesData[size++] = fileData;
    }

    // Get the byte array at a specific index
    public byte[] get(int i) {
        if (i >= size || i < 0) {
            throw new IndexOutOfBoundsException("Index out of range");
        }
        return filesData[i];
    }

    // Get the current size of the array
    public int getSize() {
        return size;
    }
}

class Node {
    int frequency;
    String data;
    char c;
    Node left, right;

    // Constructor for creating a node with frequency and data
    public Node(int frequency, String data) {
        this.frequency = frequency;
        this.data = data;
    }
    
    // Constructor for creating a node with a character and frequency
    public Node(char data, int frequency) {
        this.c = data;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }
    
    // Constructor for creating a node with frequency, left and right children
    public Node(int frequency, Node left, Node right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}

class MinHeap {
    private Node[] heap;
    int size;

    // Constructor to initialize the heap with a given capacity
    public MinHeap(int capacity) {
        heap = new Node[capacity];
        size = 0;
    }

    // Insert a node into the heap
    public void insert(Node node) {
        if (size == heap.length) {
            throw new IllegalStateException("Heap is full");
        }
        heap[size] = node;
        size++;
        heapifyUp(size - 1);
    }

    // Extract the minimum node from the heap
    public Node extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        Node min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        return min;
    }

    // Check if the heap is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Heapify up to maintain heap property
    private void heapifyUp(int index) {
        while (index > 0 && heap[parent(index)].frequency > heap[index].frequency) {
        	
            swap(index, parent(index));
            index = parent(index);
        }
    }

    // Heapify down to maintain heap property
    private void heapifyDown(int index) {
        int smallest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && heap[left].frequency < heap[smallest].frequency) {
            smallest = left;
        }
        if (right < size && heap[right].frequency < heap[smallest].frequency) {
            smallest = right;
        }
        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    // Swap two nodes in the heap
    private void swap(int i, int j) {
        Node temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Get the parent index of a given index
    private int parent(int index) {
        return (index - 1) / 2;
    }

    // Get the left child index of a given index
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    // Get the right child index of a given index
    private int rightChild(int index) {
        return 2 * index + 2;
    }
}

class HuffmanCompression {
    private MinHeap priorityQueue;
    private String[] huffmanCodes;
    private Node root;

    // Constructor to initialize the HuffmanCompression class
    public HuffmanCompression() {
        // Initialize the MinHeap with a capacity for 256 characters (assuming ASCII)
        priorityQueue = new MinHeap(256);
        // Array to store Huffman codes for each character
        huffmanCodes = new String[256];
    }

    // Method to build the frequency table from the input data
    public void buildFrequencyTable(byte[] data) {
        int[] frequencyTable = new int[256];
        // Count the frequency of each byte in the input data
        for (byte b : data) {
            frequencyTable[b & 0xFF]++;
        }

        // Insert characters with non-zero frequency into the priority queue
        for (int i = 0; i < frequencyTable.length; i++) {
            if (frequencyTable[i] > 0) {
                priorityQueue.insert(new Node((char) i, frequencyTable[i]));
            }
        }
    }

    // Method to build the Huffman Tree
    public void buildHuffmanTree() {
        // Combine nodes until only one node is left in the priority queue
        while (priorityQueue.size > 1) {
            Node left = priorityQueue.extractMin();
            Node right = priorityQueue.extractMin();
            Node combined = new Node(left.frequency + right.frequency, left, right);
            priorityQueue.insert(combined);
        }
        // The remaining node is the root of the Huffman Tree
        root = priorityQueue.extractMin();
        // Generate Huffman codes for each character
        generateCodes(root, "");
    }

    // Recursive method to generate Huffman codes from the Huffman Tree
    private void generateCodes(Node node, String code) {
        // If the node is a leaf, assign the code to the character
        if (node.left == null && node.right == null) {
            huffmanCodes[node.c] = code;
            return;
        }
        // Traverse the left subtree with code "0"
        generateCodes(node.left, code + "0");
        // Traverse the right subtree with code "1"
        generateCodes(node.right, code + "1");
    }

    // Method to compress the input data using the generated Huffman codes
    public byte[] compress(byte[] data) {
        StringBuilder compressedData = new StringBuilder();
        // Append the Huffman code for each byte in the input data
        for (byte b : data) {
            compressedData.append(huffmanCodes[b & 0xFF]);
        }

        // Convert the binary string to a byte array
        int length = (compressedData.length() + 7) / 8;
        byte[] compressedBytes = new byte[length];
        for (int i = 0; i < compressedData.length(); i++) {
            if (compressedData.charAt(i) == '1') {
                compressedBytes[i / 8] |= (128 >> (i % 8));
            }
        }
        return compressedBytes;
    }

    // Method to decompress the compressed data back to the original data
    public byte[] decompress(byte[] compressedData) {
        StringBuilder bits = new StringBuilder();
        // Convert the compressed byte array to a binary string
        for (byte b : compressedData) {
            for (int i = 0; i < 8; i++) {
                bits.append((b & (128 >> i)) == 0 ? '0' : '1');
            }
        }

        Node current = root;
        ByteArrayOutputStream decompressedData = new ByteArrayOutputStream();
        // Traverse the binary string to decode it using the Huffman Tree
        for (int i = 0; i < bits.length(); i++) {
            current = bits.charAt(i) == '0' ? current.left : current.right;
            // If a leaf node is reached, write the character to the output
            if (current.left == null && current.right == null) {
                decompressedData.write((byte) current.c);
                current = root;
            }
        }
        return decompressedData.toByteArray();
    }
}

class Zipper {
    // private HuffmanCompression compressor = new HuffmanCompression();

    // Read file and return byte array
    private byte[] readFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return fis.readAllBytes();  // Read all bytes from the file
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath + " (" + e.getMessage() + ")");
        }
        return null; // Return null if there was an error
    }

    // Method to write the zip header information
    private void writeZipHeader(FileOutputStream fos, String filePath, int compressedSize, int originalSize) throws IOException {
        DataOutputStream dos = new DataOutputStream(fos);
        dos.writeUTF(filePath);  // Write the file name
        dos.writeInt(originalSize);  // Write the original file size
        dos.writeInt(compressedSize);  // Write the compressed size
    }

    // Method to zip and compress files
    public void zipFiles(String[] inputFiles, String outputZipFile) {
        boolean flag = false;
        File fileToZip = null;

        try (FileOutputStream fos = new FileOutputStream(outputZipFile);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            for (String filePath : inputFiles) {
                fileToZip = new File(filePath);

                if (!fileToZip.exists()) {
                    System.err.println("Error: File not found - " + fileToZip.getAbsolutePath());
                    continue; // Skip this file and continue with others
                }

                try (FileInputStream fis = new FileInputStream(fileToZip);
                     BufferedInputStream bis = new BufferedInputStream(fis)) {

                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = bis.read(buffer)) >= 0) {
                        zipOut.write(buffer, 0, length);
                    }

                    zipOut.closeEntry();
                    flag = true;
                }
            }
            if (flag) {
                System.out.println("Files compressed and zipped successfully!");
                File zipFile = new File(outputZipFile);
                System.out.println("Compressed Size of the zipped file: " + zipFile.length() + " bytes");
            } else {
                System.out.println("No files were compressed.");
            }
        } catch (IOException e) {
            System.err.println("Error while zipping files: " + e.getMessage());
        }
    }

    public void unzipFiles(String zipFile, String destFolder) {
        boolean flag = false;
        File destDir = new File(destFolder);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];

            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destFolder + File.separator + entry.getName());

                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int length;
                        while ((length = zis.read(buffer)) >= 0) {
                            fos.write(buffer, 0, length);
                        }
                        flag = true; // Set flag to true after successfully writing a file
                    }
                }
                zis.closeEntry();
            }

            if (flag) {
                System.out.println("Files unzipped successfully!");
            } else {
                System.out.println("No files were unzipped.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: Zip file not found - " + zipFile);
            
        } catch (IOException e) {
            System.err.println("Error while unzipping files: " + e.getMessage());
        }
    }

}

