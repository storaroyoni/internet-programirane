import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("server has started and awaits clients");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                String[] parts = request.split(",");
                String command = parts[0];

                switch (command) {
                    case "ADD":
                        String name = parts[1];
                        String grade = parts[2];
                        addStudent(name, grade);
                        out.println("student was added successfully");
                        break;
                    case "VIEW":
                        List<String> students = viewStudents();
                        for (String student : students) {
                            out.println(student);
                        }
                        break;
                    case "AVERAGE":
                        double average = calculateAverage();
                        out.println("the average grade is: " + average);
                        break;
                    default:
                        out.println("invalid command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addStudent(String name, String grade) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("students.txt", true))) {
            writer.write(name + "," + grade);
            writer.newLine();
        }
    }

    private List<String> viewStudents() throws IOException {
        List<String> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                students.add(line);
            }
        }
        return students;
    }

    private double calculateAverage() throws IOException {
        List<String> students = viewStudents();
        int sum = 0;
        int count = 0;

        for (String student : students) {
            String[] parts = student.split(",");
            int grade = Integer.parseInt(parts[1]);
            sum += grade;
            count++;
        }
        return count == 0 ? 0 : (double) sum / count;
    }
}
