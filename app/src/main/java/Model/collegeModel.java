package Model;

import com.google.firebase.database.DataSnapshot;

public class collegeModel {
    private String name;
    private String img;
    private DataSnapshot shopsSnapshot = null;

    public collegeModel() {
    }

    public collegeModel(String name, String img, String key, DataSnapshot shopsSnapshot) {
        this.name = name;
        this.img = img;
        this.key = key;
        this.shopsSnapshot = shopsSnapshot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataSnapshot getShopsSnapshot() {
        return shopsSnapshot;
    }

    public void setShopsSnapshot(DataSnapshot shopsSnapshot) {
        this.shopsSnapshot = shopsSnapshot;
    }

    private String key;
}
