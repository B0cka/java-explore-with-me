package ru.practicum.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Builder
@Data
public class Hit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String app;

    private String uri;

    private String ip;

    private LocalDateTime timeStamp;
}