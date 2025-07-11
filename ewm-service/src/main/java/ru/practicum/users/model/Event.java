package ru.practicum.users.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.admin.model.Category;
import ru.practicum.admin.model.Location;
import ru.practicum.admin.model.User;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;

    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;

    private String state;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long views;
    private String publishedOn;
}
