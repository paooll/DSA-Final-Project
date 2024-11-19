package hotel;

import java.util.Date;

public class Booking {
    private User user;
    private String roomNumber; 
    private Date checkInDate;
    private Date checkOutDate;
    private double totalPrice;
    private boolean isConfirmed;


    public Booking(User user, String roomNumber, Date checkInDate, Date checkOutDate, double totalPrice) {
        this.user = user;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.isConfirmed = false;
    }

    public void confirmBooking() {
        isConfirmed = true;
        System.out.println("Booking confirmed for user: " + user.getName() + " in room: " + roomNumber);
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        if (user == null) {
            return "No user information available for this booking";
        }

        return "Reservation for " + user.getName() + ": Room " + roomNumber + ", Check-in: " + checkInDate + ", Check-out: " + checkOutDate;
    }
}
