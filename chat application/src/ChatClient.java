import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

public class ChatClient extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient() {
        setTitle("Chat Client");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        messageField = new JTextField();
        messageField.addActionListener(e -> sendMessage());
        add(messageField, BorderLayout.SOUTH);

        setVisible(true);

        new Thread(this::connectToServer).start();
    }

    private void connectToServer() {
        try {
            appendMessage("Connecting to server...");
            socket = new Socket("localhost", 5000);
            appendMessage("Connected to server.");

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                appendMessage("Server: " + inputLine);
            }
        } catch (IOException e) {
            appendMessage("Error: " + e.getMessage());
        }
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            appendMessage("Client: " + message);
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
        SwingUtilities.invokeLater(ChatClient::new);
    }
}