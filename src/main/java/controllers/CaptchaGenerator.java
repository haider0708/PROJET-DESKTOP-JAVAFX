package controllers;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class CaptchaGenerator {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 50;

    public Canvas generateCaptcha() {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Fill the background
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Generate a random number
        Random random = new Random();
        int number = random.nextInt(9999 - 1000 + 1) + 1000; // generates a random number between 1000 and 9999

        // Draw the number on the canvas
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Verdana", 24)); // Adjust the font here
        gc.setTextAlign(TextAlignment.CENTER); // Center the text

        // Calculate the text dimensions
        double textWidth = gc.getFont().getSize() * String.valueOf(number).length();
        double textHeight = gc.getFont().getSize();

        // Calculate the position to center the text vertically and horizontally
        double textX = (WIDTH - textWidth) / 2.0;
        double textY = (HEIGHT - textHeight) / 2.0 + textHeight / 2.0;

        gc.fillText(String.valueOf(number), textX, textY); // Adjust the vertical position here

        return canvas;
    }
}
