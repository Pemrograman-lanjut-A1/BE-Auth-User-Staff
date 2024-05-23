package id.ac.ui.cs.advprog.beauthuserstaff.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity(name = "announcement")
public class Announcement {
    @Id
    String id;
    String content;
    String tag;
    String title;

    public Announcement(String id, String content, String tag, String title){
        if(id == null){
            UUID uuid = UUID.randomUUID();
            this.id = (uuid.toString());
        }
        else{
            this.id = id;
        }
        this.tag = tag;
        this.content = content;
        this.title = title;
    }
}