package controllers;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

public class NotificationApp {

    public static void showNotification(String title, String message, NotificationType notificationType) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(notificationType);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(Duration.millis(2000));
    }
    public static void showNotification(String title, String message, NotificationType notificationType, Duration duration) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(notificationType);
        tray.setAnimationType(AnimationType.POPUP);
        tray.showAndDismiss(duration);
    }
    public static void showNotification(String title, String message, NotificationType notificationType, AnimationType animationType, Duration duration) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(notificationType);
        tray.setAnimationType(animationType);
        tray.showAndDismiss(duration);
    }
}
