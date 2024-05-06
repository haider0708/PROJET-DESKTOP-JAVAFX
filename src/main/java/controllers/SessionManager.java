package controllers;

import models.Patient;

public class SessionManager {
    private static Patient currentSession;
    private static int code;

    private static String codeS;



    public static void saveCode(int newCode) {
        code = newCode;
    }

    public static int getCode() {
        return code;
    }


    public static void saveCodeS(String newCode) {
        codeS = newCode;
    }

    public static String getCodeS() {
        return codeS;
    }

    public static void endCode() {code = 0;}

    public static void startSession(Patient patient) {
        currentSession = patient;
    }

    public static void endSession() {
        currentSession = null;
    }

    public static Patient getCurrentSession() {
        return currentSession;
    }
}
