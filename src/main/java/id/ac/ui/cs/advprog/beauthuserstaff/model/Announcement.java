package id.ac.ui.cs.advprog.beauthuserstaff.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity(name = "announcement")
public class Announcement {
    @Id
    private String id;
    private String content;
    private String tag;
    private String title;

    @Column(name = "creation_timestamp", nullable = false, updatable = false)
    private Instant creationTimestamp;

    @PrePersist
    protected void onCreate() {
        this.creationTimestamp = Instant.now();
    }

    public Announcement(String id, String content, String tag, String title) {
        if (id == null) {
            UUID uuid = UUID.randomUUID();
            this.id = uuid.toString();
        } else {
            this.id = id;
        }
        this.tag = tag;
        this.content = content;
        this.title = title;
        this.creationTimestamp = Instant.now();
    }
}
