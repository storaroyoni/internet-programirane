import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("you connected with the server");
            String command;

            while (true) {
                System.out.println("input command(ADD, VIEW, AVERAGE, EXIT):");
                command = userInput.readLine();

                if ("EXIT".equalsIgnoreCase(command)) {
                    break;
                }

                switch (command.toUpperCase()) {
                    case "ADD":
                        System.out.println("input student name:");
                        String name = userInput.readLine();
                        System.out.println("input student grade:");
                        String grade = userInput.readLine();
                        out.println("ADD," + name + "," + grade);
                        break;
                    case "VIEW":
                        out.println("VIEW");
                        break;
                    case "AVERAGE":
                        out.println("AVERAGE");
                        break;
                    default:
                        System.out.println("invalid command");
                        continue;
                }

                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                    if (response.isEmpty()) break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
