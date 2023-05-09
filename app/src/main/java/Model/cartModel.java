package Model;

public class cartModel {
    String name;
    String image;

    public cartModel() {
    }

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public cartModel(String name, String image, Long quantity, Long price) {
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    Long quantity;
    Long price;
}
