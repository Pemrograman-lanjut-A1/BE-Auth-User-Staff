package id.ac.ui.cs.advprog.beauthuserstaff.model;



import lombok.Getter;

import java.util.UUID;

@Getter
public class Announcement {
    String id;
    String content;

    public Announcement(String id, String content){
        if(id == null){
            UUID uuid = UUID.randomUUID();
            this.id = (uuid.toString());
        }
        else{
            this.id = id;
        }

        this.content = content;
    }
}


