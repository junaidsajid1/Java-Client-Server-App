package cilentserverapp; // Or clientserverapp

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Server implements Runnable {

    @Override
    public void run() {
        Connection con = null;
        ServerSocket serverSocket = null;

        try {
            // Step 1: Connect to Derby database
            String dbURL ="jdbc:derby://localhost:1527/Reward_point"; // Correct database URL
            String user = "FARAH1";
            String password = "FARAH1";

            // Establish DB connection
            System.out.println(" Connecting to the database...");
            con = DriverManager.getConnection(dbURL, user, password);
            System.out.println(" Database connected.");

            // Step 2: Start the server socket
            serverSocket = new ServerSocket(4000);
            System.out.println(" Reward Server is running... Waiting for clients...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println(" Client connected.");
                    
                    // Step 3: Set up I/O streams
                    DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream dos;
                    dos = new DataOutputStream(clientSocket.getOutputStream());

                    // Step 4: Read data from client
                    String customerId = dis.readUTF();
                    double amountSpent = dis.readDouble();
                    System.out.println(" Received - Customer ID: " + customerId + ", Amount: " + amountSpent);

                    // Step 5: Query the database to fetch rewards configuration
                    String query = "SELECT MINIMUM_PURCHASE, POINT_RATE, BAISA_PER_POINT " +
                            "FROM FARAH1.REWARDSCONFIG WHERE CUSTOMER_ID = ?";
                    
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setString(1, customerId);
                    ResultSet rs = pstmt.executeQuery();

                    // Step 6: Check if the customer exists and process rewards
                    if (rs.next()) {
                        double minPurchase = rs.getDouble("MINIMUM_PURCHASE");
                        int pointRate = rs.getInt("POINT_RATE");
                        double baisaPerPoint = rs.getDouble("BAISA_PER_POINT");

                        System.out.println(" Database record: Min Purchase = " + minPurchase +
                                           ", Point Rate = " + pointRate + ", Baisa per Point = " + baisaPerPoint);

                        if (amountSpent >= minPurchase) {
                            int rewardPoints = (int)(amountSpent * pointRate);
                            double discount = rewardPoints * baisaPerPoint / 1000.0; // baisa to OMR
                            
                            // Step 7: Send success response
                            dos.writeUTF("SUCCESS");
                            dos.writeInt(rewardPoints);
                            dos.writeDouble(discount);
                            System.out.println(" Points: " + rewardPoints + " | Discount: " + discount + " OMR");
                        } else {
                            // Insufficient purchase
                            dos.writeUTF("INSUFFICIENT");
                            dos.writeInt(0);
                            dos.writeDouble(0.0);
                            System.out.println("Not enough purchase for rewards.");
                        }
                    } else {
                        // Customer ID not found
                        dos.writeUTF("NOT_FOUND");
                        dos.writeInt(0);
                        dos.writeDouble(0.0);
                        System.out.println("Customer ID not found.");
                    }

                    // Step 8: Flush the data to ensure it's sent
                    dos.flush();
                } catch (IOException e) {
                    System.out.println(" Error in client communication: " + e.getMessage());
                } catch (SQLException e) {
                    System.out.println(" Database error: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.out.println(" Error setting up server socket: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        } finally {
            // Clean up resources
            try {
                if (con != null) con.close();
                if (serverSocket != null) serverSocket.close();
                System.out.println("Server shutdown. Resources closed.");
            } catch (IOException | SQLException e) {
                System.out.println(" Error during resource cleanup: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}

