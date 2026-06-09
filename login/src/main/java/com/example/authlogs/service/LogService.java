package com.example.authlogs.service;

import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class LogService {

    private static final Path LOG_FILE_PATH = Path.of("src", "main", "resources", "logs.txt");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    
    public void init() {
        try {
            if (Files.notExists(LOG_FILE_PATH)) {
                Files.createDirectories(LOG_FILE_PATH.getParent());
                Files.createFile(LOG_FILE_PATH);
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo inicializar el archivo de logs", e);
        }
    }

    public void registrarEvento(String evento) {
        String linea = String.format("%s - %s", LocalDateTime.now().format(FORMATTER), evento);
        try {
            Files.writeString(LOG_FILE_PATH, linea + System.lineSeparator(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo en logs.txt", e);
        }
    }

    public List<String> obtenerEventos() {
        try {
            if (Files.notExists(LOG_FILE_PATH)) {
                return Collections.emptyList();
            }
            return Files.readAllLines(LOG_FILE_PATH, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo logs.txt", e);
        }
    }
}
