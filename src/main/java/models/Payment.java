package models;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodCreateParams;

public class Payment {

    // Set your secret key here
    private static final String SECRET_KEY = "sk_test_51OpBupJnXkKseWKeuRiGZ1arXNJRnS7p2fg7qsP3QQInyiREqDf4lbyiP8LC8RuZgc7PqdrYR2maQqei6NsDWLm600b9HkTe2m";

    public static void main(String[] args) {
        Stripe.apiKey = SECRET_KEY;

        try {
            // Retrieve your account information
            Account account = Account.retrieve();
            System.out.println("Account ID: " + account.getId());
            // Print other account information as needed
        } catch (StripeException e) {
            e.printStackTrace();
        }

        // Process payment
        processPayment();
    }

    private static void processPayment() {
        try {
            // Set your secret key here
            Stripe.apiKey = SECRET_KEY;

            // Create a PaymentIntent with other payment details
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(1000L) // Amount in cents (e.g., $10.00)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // If the payment was successful, display a success message
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
        } catch (StripeException e) {
            // If there was an error processing the payment, display the error message
            System.out.println("Payment failed. Error: " + e.getMessage());
        }
    }
}