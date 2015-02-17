package iShamrock.Postal.entity;

/**
 * Created by lifengshuang on 2/16/15.
 */
public class User {

    private String name;
    private String phone;
    private String photoURI;
    private String coverURI;

    public User(String name, String phone, String photoURI, String coverURI) {
        this.name = name;
        this.phone = phone;
        this.photoURI = photoURI;
        this.coverURI = coverURI;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public String getCoverURI() {
        return coverURI;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setCoverURI(String coverURI) {
        this.coverURI = coverURI;
    }
}
