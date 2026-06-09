package com.example.authlogs.utils;

public final class Validador {

    private Validador() {
        // Utility class
    }

    // Validar username
    public static boolean esNombreValido(String username) {

        return username != null
                && username.matches("^[a-zA-Z0-9_.-]{3,20}$");
    }

    // Validar contraseña
    public static boolean esPasswordValida(String password) {

        return password != null
                && password.length() >= 6
                && password.length() <= 32;
    }

    // Validar correo
    public static boolean esCorreoValido(String email) {

        return email != null
                && email.contains("@")
                && email.contains(".");
    }
}