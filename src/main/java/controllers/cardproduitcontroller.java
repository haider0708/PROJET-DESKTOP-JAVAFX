package controllers;



import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Panier;
import models.Produit;
import javafx.scene.control.Alert;
public class cardproduitcontroller {

    private Stage stage;

    private Produit produit;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label nom;

    @FXML
    private Label prix;
    @FXML
    private ImageView produit_img;

    @FXML
    private Label description;
    @FXML
   private Button btnAjouterAuPanier;


    public void getData(Produit produit) {
        this.produit = produit;
        nom.setText(produit.getNom());
        description.setText(produit.getDescription());
        prix.setText(String.valueOf(produit.getPrix()));
        Image imageprofile = new Image(produit.getImg());
        produit_img.setImage(imageprofile);
        //System.out.printf("ahuaaaa"+produit.getImg());

    }

    @FXML
    void twilliobtn(ActionEvent event) {
        String destinataire = "+21629310624"; // Mettez ici le numéro de téléphone du destinataire
        String message = "Bonjour, Votre commande : " + produit.getNom() + ", est bien traiter. Le paiement sera effectué à la livraison."; // Message avec le nom du produit

        TwilioSMSFXML.sendSMS(destinataire, message);

        showAlert(Alert.AlertType.INFORMATION, "SMS envoyé", "Votre COMMANDE a été envoyé avec succès !");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
    @FXML
    private void ajouterAuPanier(ActionEvent event) {
        Panier.getInstance().ajouterProduit(produit);
        showAlert(Alert.AlertType.INFORMATION, "Produit ajouté", "Le produit a été ajouté au panier !");
    }
    @FXML
    private void initiateStripePayment(ActionEvent event) {
        // Set your Stripe secret key
        Stripe.apiKey = "sk_test_51OpBupJnXkKseWKeuRiGZ1arXNJRnS7p2fg7qsP3QQInyiREqDf4lbyiP8LC8RuZgc7PqdrYR2maQqei6NsDWLm600b9HkTe2m"; // Replace with your Stripe secret key

        // Create a new Payment Session with Stripe Checkout
        try {
            Session session = Session.create(createCheckoutSession());

            // Redirect the user to the Stripe Checkout page
            String checkoutUrl = session.getUrl();
            // Open checkoutUrl in a browser or WebView
            System.out.println("Redirect user to: " + checkoutUrl);
        } catch (StripeException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Stripe Error", "Failed to create checkout session. Please try again later.");
        }
    }

    private SessionCreateParams createCheckoutSession() {
        // Set up the parameters for creating a Stripe Checkout session
        return SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://your-website.com/success") // Replace with your success URL
                .setCancelUrl("https://your-website.com/cancel") // Replace with your cancel URL
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd") // Replace with your currency
                                                .setUnitAmount(1000L) // Replace with your amount in cents
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Your Product Name")
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();
    }



}
