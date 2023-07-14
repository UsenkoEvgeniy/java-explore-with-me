package ru.practicum.explore_with_me.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "compilations")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Boolean pinned;
    private String title;
    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "compilation_id"), name = "compilation_event", inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    private List<Event> events;
}
