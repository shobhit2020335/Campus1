package Model;

import com.google.firebase.database.DataSnapshot;

public class ShopModel {
    private String key;
    private String name;
    private String img = "";
    private String campusKey = "";
    private DataSnapshot menuSnapshot = null;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null) this.name = "";
        else this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        if(img == null) this.img = "";
        else this.img = img;
    }

    public String getCampusKey() {
        return campusKey;
    }

    public void setCampusKey(String campusKey) {
        this.campusKey = campusKey;
    }

    public DataSnapshot getMenuSnapshot() {
        return menuSnapshot;
    }

    public void setMenuSnapshot(DataSnapshot menuSnapshot) {
        this.menuSnapshot = menuSnapshot;
    }
}
