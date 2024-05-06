package controllers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.Event;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import services.ServiceEvent;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TextField titreField;
    @FXML
    private TextField localisationField;
    @FXML
    private TextField dateField;
    @FXML
    private Button addButton;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ImageView qrCodeView;

    private ServiceEvent ServiceEvent = new ServiceEvent();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEvents();
        setupTableSelection();
        afficherRepartitionParDate();
    }

    private void setupTableSelection() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                try {
                    populateFieldsWithEvent(newSelection); // Populate fields with the selected event
                    Image qrCode = generateQRCodeImage(newSelection.getTitre() + " - " + newSelection.getLocalisation() + " - " + newSelection.getDate(), 250, 250);
                    qrCodeView.setImage(qrCode);
                } catch (WriterException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void populateFieldsWithEvent(Event event) {
        titreField.setText(event.getTitre());
        localisationField.setText(event.getLocalisation());
        LocalDate date = LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        datePicker.setValue(date);
    }

    @FXML
    private void handleAddEvent() {
        if (validateInput()) {
            try {
                String titre = titreField.getText();
                String localisation = localisationField.getText();
                String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                Event event = new Event(titre, localisation, date);
                ServiceEvent.addEvent(event);

                // Regenerate QR code with new event details
                Image qrCode = generateQRCodeImage(event.getTitre() + " - " + event.getLocalisation() + " - " + event.getDate(), 250, 250);
                qrCodeView.setImage(qrCode);

                // Load HTML template
                String htmlContent = loadHtmlTemplate();

                // Replace placeholders with actual values
                htmlContent = htmlContent.replace("{{eventName}}", titre)
                        .replace("{{eventLocation}}", localisation)
                        .replace("{{eventDate}}", date);

                // Compose invitation email with HTML content
                String inviteSubject = "Invitation to Event: " + titre;

                // Send invitation email
                sendEmail("malekbenslamacontact@gmail.com", inviteSubject, htmlContent);

                loadEvents();
                clearFields();
                showSuccessAlert();
            } catch (SQLException | IOException | WriterException e) {
                e.printStackTrace();
                showErrorAlert(e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdateEvent() {
        if (validateInput()) {
            try {
                Event event = eventTable.getSelectionModel().getSelectedItem();
                event.setTitre(titreField.getText());
                event.setLocalisation(localisationField.getText());
                event.setDate(datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                ServiceEvent.updateEvent(event);
                loadEvents();
                showSuccessAlert("Event updated successfully!");
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Failed to update event: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteEvent() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                ServiceEvent.deleteEvent(selectedEvent.getId());
                loadEvents();
                clearFields();
                showSuccessAlert("Event deleted successfully!");
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorAlert("Failed to delete event: " + e.getMessage());
            }
        } else {
            showErrorAlert("No event selected for deletion!");
        }
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Added");
        alert.setHeaderText(null);
        alert.setContentText("The event has been successfully added.");
        alert.showAndWait();
    }

    private void loadEvents() {
        try {
            List<Event> events = ServiceEvent.getAllEvents();
            eventTable.getItems().setAll(events);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error retrieving data from database: " + e.getMessage());
        }
    }

    private void clearFields() {
        titreField.clear();
        localisationField.clear();
        datePicker.setValue(null);
    }


    private boolean validateInput() {
        StringBuilder errorMessage = new StringBuilder();
        String titre = titreField.getText();
        String localisation = localisationField.getText();
        LocalDate date = datePicker.getValue();
        String datePattern = "dd/MM/yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

        if (titre.isEmpty() || titre.length() < 6 || !titre.matches("^[A-Z].*")) {
            errorMessage.append("Title must start with an uppercase letter and be at least 6 characters long.\n");
        }
        if (localisation.isEmpty() || localisation.length() < 3 || !localisation.matches("^[a-zA-Z0-9]{3,}$")) {
            errorMessage.append("Location must be at least 3 characters long and contain only letters and numbers.\n");
        }
        if (date == null || date.isBefore(LocalDate.now())) {
            errorMessage.append("Date cannot be in the past and must be in the format: ").append(datePattern).append(".\n");
        }

        if (errorMessage.length() > 0) {
            showErrorAlert(errorMessage.toString());
            return false;
        }
        return true;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        final String username = "malek.benslama8b3@gmail.com";
        final String password = "hsqjqokkhahloxmb";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    @FXML
    private void handleSendSms() {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        String phoneNumber = phoneNumberField.getText();

        if (selectedEvent != null && phoneNumber != null && !phoneNumber.isEmpty()) {
            String message = String.format("Event: %s at %s on %s",
                    selectedEvent.getTitre(), selectedEvent.getLocalisation(), selectedEvent.getDate());
            SmsSender.sendSms(phoneNumber, message);
            showSuccessAlert("SMS sent to " + phoneNumber);
        } else {
            showErrorAlert("Select an event and enter a phone number.");
        }
    }

    public class SmsSender {

        public static final String ACCOUNT_SID = "AC99dc6c7cd0949b302b1e14812ff4e247";
        public static final String AUTH_TOKEN = "13c31ebfba7732aebf0d41fa0dedffde";
        public static final String FROM_NUMBER = "+12562864029";

        static {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        }

        public static void sendSms(String to, String message) {
            com.twilio.rest.api.v2010.account.Message sms = com.twilio.rest.api.v2010.account.Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(FROM_NUMBER),
                    message
            ).create();

            System.out.println("SMS sent successfully to " + to + " with SID: " + sms.getSid());
        }
    }

    @FXML
    private void handleGenerateStyledPdf() {
        String filePath = "StyledEventsReport.pdf";
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setLeading(14.5f);
            float yStart = 700; // Starting y position from top of the page

            // Title with custom color similar to iText example
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.setNonStrokingColor(new PDColor(new float[]{76 / 255f, 175 / 255f, 80 / 255f}, PDDeviceRGB.INSTANCE));
            contentStream.newLineAtOffset(100, yStart);
            contentStream.showText("Rapport des Événements");
            contentStream.endText();
            yStart -= 50; // Space after title

            // Fetch events
            List<Event> events = ServiceEvent.getAllEvents();
            for (Event event : events) {
                if (yStart < 100) { // Check if new page is needed
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setLeading(14.5f);
                    yStart = 750; // Reset yStart for new page
                }

                // Section Title for each event with underline
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(new PDColor(new float[]{0 / 255f, 77 / 255f, 64 / 255f}, PDDeviceRGB.INSTANCE));
                contentStream.newLineAtOffset(100, yStart);
                contentStream.showText(event.getTitre());
                contentStream.endText();
                contentStream.moveTo(100, yStart - 5);
                contentStream.lineTo(300, yStart - 5);
                contentStream.setLineWidth(0.5f);
                contentStream.stroke();

                // Details - Localisation
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, yStart - 20);
                contentStream.showText("Localisation: " + event.getLocalisation());
                contentStream.endText();

                // Details - Date
                contentStream.beginText();
                contentStream.newLineAtOffset(100, yStart - 35);
                contentStream.showText("Date: " + event.getDate());
                contentStream.endText();

                // Divider line similar to iText LineSeparator
                yStart -= 55; // Adjust y position
                contentStream.moveTo(100, yStart - 45);
                contentStream.lineTo(500, yStart - 45);
                contentStream.setStrokingColor(new PDColor(new float[]{204 / 255f, 204 / 255f, 204 / 255f}, PDDeviceRGB.INSTANCE));
                contentStream.stroke();

                yStart -= 10; // Additional space after line
            }
            contentStream.close();

            // Save the document
            document.save(filePath);
            showSuccessAlert("PDF stylisé généré avec succès. Enregistré sous: " + filePath);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur lors de la génération du PDF: " + e.getMessage());
        }
    }


    @FXML
    private PieChart pieChart;

    private void afficherRepartitionParDate() {
        ObservableList<Event> events = eventTable.getItems();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Using a Map to count events by date
        Map<String, Integer> dateCount = new HashMap<>();

        for (Event event : events) {
            dateCount.merge(event.getDate(), 1, Integer::sum);
        }

        // Convert counts to PieChart data
        dateCount.forEach((date, count) -> {
            pieChartData.add(new PieChart.Data(date, count));
        });

        // Update the pie chart
        pieChart.setData(pieChartData);

        // Apply CSS styling
        pieChart.setStyle("-fx-background-color: white;");

        // Additional styling (optional)
        for (PieChart.Data data : pieChartData) {
            Node node = data.getNode();
        }
    }

    private Image generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        Image image = new Image(new ByteArrayInputStream(pngData));
        System.out.println("QR Code Generated: " + (image != null));
        return image;
    }
    private String loadHtmlTemplate() {
        return  """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Email Invitation</title>
            <style>
                body {
                    font-family: 'Arial', sans-serif;
                    color: #333;
                    background-color: #f4f4f4;
                    margin: 0;
                    padding: 20px;
                }
                .container {
                    max-width: 600px;
                    margin: auto;
                    background: white;
                    border-radius: 8px;
                    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                    overflow: hidden;
                }
                .header {
                    background-color: #007bff;
                    color: #ffffff;
                    padding: 20px;
                    text-align: center;
                    border-bottom: 1px solid #0056b3;
                }
                .header h1 {
                    margin: 0;
                    font-size: 24px;
                }
                .content {
                    padding: 20px;
                    line-height: 1.6;
                }
                .details {
                    background-color: #f9f9f9;
                    padding: 15px;
                    margin: 20px 0;
                    border-left: 4px solid #007bff;
                }
                .details p, .details ul {
                    margin: 0;
                }
                .details ul {
                    padding-left: 20px;
                }
                .details li {
                    margin-bottom: 10px;
                }
                .footer {
                    text-align: center;
                    padding: 20px;
                    background-color: #f0f0f0;
                    color: #333;
                }
                .btn {
                    display: inline-block;
                    background-color: #ffffff;
                    color: #ffffff;
                    padding: 10px 20px;
                    text-decoration: none;
                    border-radius: 5px;
                    font-weight: bold;
                    text-align: center;
                    margin: 10px 0;
                }
                .btn:hover {
                    background-color: #0056b3;
                }
            </style>
        </head>
        <body>
        <div class="container">
            <div class="header">
                <h1>Invitation to Event: {{eventName}}</h1>
            </div>
            <div class="content">
                <p>Dear User,</p>
                <p>You are invited to the event:</p>
                <p><strong>Event Name:</strong> {{eventName}}</p>
                <p><strong>Location:</strong> {{eventLocation}}</p>
                <p><strong>Date:</strong> {{eventDate}}</p>
            </div>
            <div class="footer">
                <p>We hope to see you there!</p>
            </div>
            <a href="URL_DE_VOTRE_EVENEMENT" class="btn">Join Now</a>
        </div>
        </body>
        </html>
        """;
    }

}