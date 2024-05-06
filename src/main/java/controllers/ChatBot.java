package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatBot {

    @FXML
    private TextField messageField;

    @FXML
    private Label userText;

    @FXML
    private Label chatbotText;

    @FXML
    private HBox userMessageContainer;

    @FXML
    private HBox chatbotMessageContainer;

    @FXML
    private Button sendButton;

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        if (userMessageContainer != null) {
            userMessageContainer.setVisible(false);
        }
        if (chatbotMessageContainer != null) {
            chatbotMessageContainer.setVisible(false);
        }
        if (sendButton != null) {
            sendButton.setOnAction(this::sendMessage);
        }
    }


    @FXML
    private void sendMessage(ActionEvent event) {
        try {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                userText.setText(message);
                userMessageContainer.setVisible(true);

                String response = generateResponse(message);

                chatbotText.setText(response);
                chatbotMessageContainer.setVisible(true);

                messageField.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
            userText.setText("Error: An error occurred while processing your message.");
        }
    }

    private String generateResponse(String message) {
        String lowercaseMessage = message.toLowerCase();
        if (lowercaseMessage.contains("hello") || lowercaseMessage.contains("hi")) {
            return "Hello! How can I assist you with our events today?";
        } else if (lowercaseMessage.contains("event info")) {
            return "Please specify the event you're interested in, and I'll provide the details!";
        } else if (lowercaseMessage.contains("activity")) {
            return "Our events include a variety of activities. Are you looking for something specific?";
        } else {
            return "I'm not sure how to help with that. Can you try asking something else about our events or activities?";
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Front.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

