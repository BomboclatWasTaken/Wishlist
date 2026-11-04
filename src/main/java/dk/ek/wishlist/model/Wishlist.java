package dk.ek.wishlist.model;

public class Wishlist {

    /// Attributter
    private int id;
    private String title;
    private String shareCode;
    private int userId;

    /// Konstruktører
    public Wishlist(int id, String title, String shareCode, int userId){
        this.id = id;
        this.title = title;
        this.shareCode = shareCode;
        this.userId = userId;
    }

    public Wishlist(){

    }

    /// Getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
