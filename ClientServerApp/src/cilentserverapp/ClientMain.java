package cilentserverapp;

public class ClientMain {
    public static void main(String[] args) {
        // Create a new thread with the correct Client class
        Thread clientThread = new Thread(new Client()); // Make sure this is cilentserverapp.Client
        clientThread.start();
    }
}


