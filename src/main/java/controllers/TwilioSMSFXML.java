package controllers;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioSMSFXML {
    // Vos identifiants Twilio
    public static final String ACCOUNT_SID = "ACc978b03fb24b1c53243c4ac9e321c9e4";
    public static final String AUTH_TOKEN = "cc4cd33feb540b804fed388d0c2b60b9";

    // Le numéro de téléphone Twilio
    public static final String TWILIO_NUMBER = "+18155678037";

    // Méthode pour envoyer un SMS
    public static void sendSMS(String toPhoneNumber, String message) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message twilioMessage = Message.creator(
                        new PhoneNumber(toPhoneNumber), // Le numéro de téléphone du destinataire (avec le code du pays)
                        new PhoneNumber(TWILIO_NUMBER), // Votre numéro Twilio
                        message) // Le message à envoyer
                .create();

        System.out.println("SMS envoyé avec SID : " + twilioMessage.getSid());
    }
}

