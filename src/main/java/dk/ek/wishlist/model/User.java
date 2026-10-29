package dk.ek.wishlist.model;

public class User {

    private int id;
    private String name;
    private String description;
    private double price;
    private String linkUrl;
    private String pictureUrl;
    private int wishlistId;

    /// Konstruktører

    public User(int id, String name, String description, double price, String linkUrl, String pictureUrl, int wishlistId){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.linkUrl = linkUrl;
        this.pictureUrl = pictureUrl;
        this.wishlistId = wishlistId;
    }

    //Note to self - måske skal denne have wishlistId, men er her som en start lavet uden.
    public User (String name, String description, double price, String linkUrl, String pictureUrl){
        this.name = name;
        this.description = description;
        this.price = price;
        this.linkUrl = linkUrl;
        this.pictureUrl = pictureUrl;
    }

    public User(){
    }


    /// Getters og setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }
}
