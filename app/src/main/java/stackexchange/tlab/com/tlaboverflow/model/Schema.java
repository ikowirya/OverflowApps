package stackexchange.tlab.com.tlaboverflow.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schema {
    @SerializedName("items")
    private List<Items> itemsList;

    public Schema() {
    }

    public Schema(List<Items> itemsList) {

        this.itemsList = itemsList;
    }

    public List<Items> getItemsList() {

        return itemsList;
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
    }
}
