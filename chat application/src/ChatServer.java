import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class ChatServer extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatServer() {
        setTitle("Chat Server");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        messageField = new JTextField();
        messageField.addActionListener(e -> sendMessage());
        add(messageField, BorderLayout.SOUTH);

        setVisible(true);

        new Thread(this::startServer).start();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(5000);
            appendMessage("Server started. Waiting for client...");
            clientSocket = serverSocket.accept();
            appendMessage("Client connected.");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                appendMessage("Client: " + inputLine);
            }
        } catch (IOException e) {
            appendMessage("Error: " + e.getMessage());
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            appendMessage("Server: " + message);
            messageField.setText("");
        }
    }

    private void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatServer::new);
    }
}