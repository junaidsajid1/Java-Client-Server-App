package cilentserverapp; // Or clientserverapp

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {

    @Override
    public void run() {
        try {
            // Step 1: Connect to the server on port 1527
            try (Socket socket = new Socket("localhost", 4000)) {
                Scanner scanner = new Scanner(System.in);

                // Step 2: Take user input for Customer ID and Purchase Amount
                System.out.print("Enter Customer ID (e.g., C001): ");
                String customerId = scanner.nextLine(); // Customer ID input
                
                System.out.print("Enter Purchase Amount (OMR): ");
                double amountSpent = scanner.nextDouble(); // Amount spent input

                // Consume the newline left by nextDouble
                scanner.nextLine();

                // Validate the amount
                if (amountSpent <= 0) {
                    System.out.println("❌ Invalid amount. Please enter a positive number.");
                    return; // Don't proceed if the amount is invalid
                }

                // Step 3: Send data to server (Customer ID and Amount)
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(customerId); // Send Customer ID
                dos.writeDouble(amountSpent); // Send Purchase Amount
                System.out.println("Sent Customer ID and Amount to server");

                // Step 4: Receive response from the server
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String status = dis.readUTF(); // Read status from the server
                System.out.println("Received status from server: " + status);

                // Handle different responses based on the server's status
                if (status.equals("SUCCESS")) {
                    int points = dis.readInt();
                    double discount = dis.readDouble();

                    // Display the reward details
                    System.out.println("\n✅ Reward Calculation:");
                    System.out.println("Customer ID: " + customerId);
                    System.out.println("Amount Spent: " + amountSpent + " OMR");
                    System.out.println("Reward Points: " + points);
                    System.out.println("Discount: " + discount + " OMR");
                } else if (status.equals("INSUFFICIENT")) {
                    System.out.println("\n⚠ You did not meet the minimum purchase requirement.");
                } else {
                    System.out.println("\n❌ Customer ID not found.");
                }

                // Step 5: Close the connections (handled automatically by try-with-resources)
                dis.close();
                dos.close();
            }

        } catch (IOException e) {
            System.out.println("⚠ Error: " + e.getMessage());
            // This will print the full stack trace for better debugging
        }
    }

    public static void main(String[] args) {
        // Create a new thread to run the client logic
        Thread clientThread = new Thread(new Client());
        clientThread.start();  // This will execute the run() method in a separate thread
    }
}



