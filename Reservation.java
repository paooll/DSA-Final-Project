package hotel;

public class Reservation {
    private User user;
    private String date;
    private String roomType;
    private String personalInfo;

    public Reservation(User user, String date, String roomType, String personalInfo) {
        this.user = user;
        this.date = date;
        this.roomType = roomType;
        this.personalInfo = personalInfo;
    }

    public User getUser() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }
}
