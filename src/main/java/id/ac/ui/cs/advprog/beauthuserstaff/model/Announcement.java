package id.ac.ui.cs.advprog.beauthuserstaff.model;



import lombok.Getter;

@Getter
public class Announcement {
    String id;
    String content;

    public Announcement(String id, String content){
        this.id = id;
        this.content = content;
    }
}


