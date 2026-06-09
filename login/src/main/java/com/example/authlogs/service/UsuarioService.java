package com.example.authlogs.service;

import com.example.authlogs.model.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private static final String ARCHIVO =
            "src/main/resources/usuarios.json";

    private final ObjectMapper mapper =
            new ObjectMapper();

    // =========================
    // GUARDAR USUARIO
    // =========================
    public void guardarUsuario(Usuario usuario) {

        List<Usuario> usuarios =
                obtenerUsuarios();

        usuarios.add(usuario);

        guardarTodos(usuarios);
    }

    // =========================
    // VALIDAR LOGIN
    // =========================
    public Usuario validarUsuario(
            String username,
            String password) {

        List<Usuario> usuarios =
                obtenerUsuarios();

        for (Usuario u : usuarios) {

            if (u.getUsername().equals(username)
                    && u.getPassword().equals(password)) {

                return u;
            }
        }

        return null;
    }

    // =========================
    // LEER USUARIOS
    // =========================
    private List<Usuario> obtenerUsuarios() {

        File file = new File(ARCHIVO);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {

            return mapper.readValue(
                    file,
                    new TypeReference<List<Usuario>>() {}
            );

        } catch (IOException e) {

            return new ArrayList<>();
        }
    }

    // =========================
    // GUARDAR JSON
    // =========================
    private void guardarTodos(
            List<Usuario> usuarios) {

        try {

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            new File(ARCHIVO),
                            usuarios
                    );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}