package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private String username;
    private String action;
    private String requestParams;

    public AuditLog(LocalDateTime timestamp, String username, String action, String requestParams) {
        this.timestamp = timestamp;
        this.username = username;
        this.action = action;
        this.requestParams = requestParams;
    }

}
