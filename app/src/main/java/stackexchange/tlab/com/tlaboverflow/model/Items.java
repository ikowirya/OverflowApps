package stackexchange.tlab.com.tlaboverflow.model;

import com.google.gson.annotations.SerializedName;

public class Items {

    @SerializedName("creation_date")
    private String creation_date;
    @SerializedName("title")
    private String title;
    @SerializedName("owner")
    private Owner owner;
    @SerializedName("link")
    private String link;

    public Items() {
    }

    public Items(String creation_date, String title, Owner owner, String link) {
        this.creation_date = creation_date;
        this.title = title;
        this.owner = owner;
        this.link = link;
    }

    public String getCreation_date() {

        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
