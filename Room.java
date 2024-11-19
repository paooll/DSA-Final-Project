package hotel;

public class Room {
    private String type;
    private double price;
    private boolean isAvailable;
    private String roomNumber;
    private int id;

    public Room(int id, String type, double price, boolean isAvailable, String roomNumber) {
        this.id = id;
        this.type = type; 
        this.price = price;
        this.isAvailable = isAvailable;
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return type + " - " + roomNumber;  
    }
}
