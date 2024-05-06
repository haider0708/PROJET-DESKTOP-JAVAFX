package models;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.util.Random;
public class SmsSender {
    public static final String ACCOUNT_SID = "AC2d7b19af7491710356c1d35b9a3a3c1a";
    public static final String AUTH_TOKEN = "6dee3daabd0df68e4be99efd2a11672d";
    public static String verificationCode; //besh nanesthakesh instance juste naayet lel classe.sendverif

    public static void sendVerificationCode(String toPhoneNumber,String Lieu) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String twilioPhoneNumber = "+12514187130"; // Remplacez par votre numéro de téléphone Twilio réel
        // Générer un code aléatoire
        Random random = new Random();
        verificationCode = String.format("%04d", random.nextInt(10000));
        String messageBody = "Bonjour, votre rendez-vous est confirmé avec le rendezvous."+" "+Lieu+" Gardez le code de confirmation.: " + verificationCode;
        Message message = Message.creator(
                        new PhoneNumber(toPhoneNumber),
                        new PhoneNumber(twilioPhoneNumber),
                        messageBody)
                .setFrom(new PhoneNumber(twilioPhoneNumber)) // Utilisez votre numéro de téléphone Twilio réel ici
                .create();
        System.out.println("Message SID: " + message.getSid());
    }

}