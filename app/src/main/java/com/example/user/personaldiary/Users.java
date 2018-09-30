package com.example.user.personaldiary;

/**
 * Created by User on 30-Jul-17.
 */

public class Users {

    private String Name, image, Status, thumb_image;

    public Users(){

    }

    public Users(String Name, String image, String Status, String thumb_image){
        this.Name = Name;
        this.image = image;
        this.Status = Status;
        this.thumb_image = thumb_image;
    }
    public String getName() {

        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}
