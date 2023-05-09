package Model;

public class FoodModel {
    String name,image;
    Long price,quatntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Long getQuatntity() {
        return quatntity;
    }

    public void setQuatntity(Long quatntity) {
        this.quatntity = quatntity;
    }

    public FoodModel() {
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public FoodModel(String name, String image, Long price, Long quatntity) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.quatntity = quatntity;
    }
}
