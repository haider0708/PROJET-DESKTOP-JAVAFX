package models;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class ChatBot extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Create UI elements
        TextArea chatArea = new TextArea();
        TextField inputField = new TextField();
        Button sendButton = new Button("Send");

        // Set action for the send button
        sendButton.setOnAction(e -> {
            String userInput = inputField.getText();
            String response = getResponse(userInput);
            chatArea.appendText("You: " + userInput + "\n");
            chatArea.appendText("ChatBot: " + response + "\n");
            inputField.clear();
        });

        // Set up the layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(chatArea, inputField, sendButton);

        // Set up the scene and stage
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("JavaFX ChatBot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to generate response based on user input
    private String getResponse(String input) {
        input = input.toLowerCase();

        switch (input) {
            case "hello":
            case "hi":
                return "Hello there!";
            case "how are you?":
                return "I'm just a chatbot, but thanks for asking!";


            case "comment se déroule la consultation en ligne ?":
                return "The online consultation is conducted via secure video conference.";

            case "quels sont les médecins disponibles pour une consultation en ligne ?":
                return "Currently, our available doctors for online consultations are Dr. Smith and Dr. Dupont.";

            case "comment puis-je payer pour la consultation en ligne ?":
                return "Once your appointment is confirmed, you will receive a payment link.";

            case "comment puis-je annuler ou reporter mon rendez-vous en ligne ?":
                return "You can cancel or reschedule your online appointment by contacting us directly by phone or email.";

            case "mes informations médicales seront-elles sécurisées ?":
                return "Absolutely. We use advanced technologies to protect your confidentiality.";
            case "bye":
                return "Goodbye!";

            default:
                return "I'm sorry, I didn't understand that.";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

