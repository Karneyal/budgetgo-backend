package com.budgetgo.backend.controller;

import com.budgetgo.backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dev/emails")
public class DevEmailController {

    @Autowired
    private EmailService emailService;

    @Value("${app.email.mock:true}")
    private boolean mockEmail;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestEmail() {
        if (!mockEmail) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Dev email preview is disabled. Set app.email.mock=true in configuration."));
        }

        return emailService.getLastSentEmail()
                .map(email -> ResponseEntity.ok(Map.of(
                        "to", email.to(),
                        "subject", email.subject(),
                        "body", email.body())))
                .orElse(ResponseEntity.ok(Map.of("message", "No emails have been generated yet.")));
    }
}
