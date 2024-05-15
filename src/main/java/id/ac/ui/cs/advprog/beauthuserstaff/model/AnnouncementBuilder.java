package id.ac.ui.cs.advprog.beauthuserstaff.model;

public class AnnouncementBuilder {
    private String id;
    private String content;
    private String tag;

    public AnnouncementBuilder id(String id){
        this.id = id;
        return this;
    }

    public AnnouncementBuilder content(String content){
        this.content = content;
        return this;
    }

    public AnnouncementBuilder tag(String tag){
        this.tag = tag;
        return this;
    }

    public Announcement build(){
        if (content == null){
            throw new IllegalArgumentException("Content cannot be null");
        }
        return new Announcement(id, content, tag);
    }
}
