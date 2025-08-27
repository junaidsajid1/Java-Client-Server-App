package cilentserverapp;

public class ServerMain {
    public static void main(String[] args) {
        // Create a new thread for the Server
        Thread serverThread = new Thread(new Server());
        serverThread.start(); // Start the server in its own thread

        System.out.println(" ServerMain: Server thread started.");
    }
}

