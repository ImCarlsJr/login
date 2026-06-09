package com.example.authlogs.controller;

import com.example.authlogs.model.Usuario;
import com.example.authlogs.service.LogService;
import com.example.authlogs.service.UsuarioService;
import com.example.authlogs.utils.Validador;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final LogService logService;

    public AuthController(UsuarioService usuarioService, LogService logService) {
        this.usuarioService = usuarioService;
        this.logService = logService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        if (!Validador.esNombreValido(username) || !Validador.esPasswordValida(password)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Credenciales inválidas"));
        }

        Usuario usuario = usuarioService.validarUsuario(username, password);
        if (usuario == null) {
            logService.registrarEvento("Login fallido para usuario: " + username);
            return ResponseEntity.status(401).body(Map.of("error", "Usuario o contraseña incorrectos"));
        }

        logService.registrarEvento("Login exitoso para usuario: " + username);
        return ResponseEntity.ok(Map.of(
        "message", "Autenticación exitosa",
        "username", usuario.getUsername()
));
    }

    @PostMapping("/registro")
public ResponseEntity<?> registro(@RequestBody Usuario usuario) {

    // Validar username
    if (!Validador.esNombreValido(usuario.getUsername())) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Nombre inválido"));
    }

    // Validar correo
    if (!Validador.esCorreoValido(usuario.getEmail())) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", "Correo inválido"));
    }

    // Validar contraseña
    if (!Validador.esPasswordValida(usuario.getPassword())) {
        return ResponseEntity.badRequest()
                .body(Map.of(
                        "error",
                        "La contraseña debe tener mínimo 6 caracteres"
                ));
    }

    // Guardar usuario
    usuarioService.guardarUsuario(usuario);

    // Registrar log
    logService.registrarEvento(
            "REGISTRO EXITOSO | Usuario: "
                    + usuario.getUsername()
    );

    return ResponseEntity.ok(
            Map.of(
                    "message",
                    "Usuario registrado correctamente"
            )
    );
}

    @GetMapping("/logs")
    public ResponseEntity<List<String>> obtenerLogs() {
        List<String> logs = logService.obtenerEventos();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/")
public String inicio() {
    return "API funcionando correctamente";
}
}