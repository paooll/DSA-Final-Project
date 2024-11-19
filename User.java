package hotel;

public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String personalInfo;
    private String email;
    private String phone;
    private boolean isAdmin;  
    public int id;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.firstName = "";
        this.lastName = "";
        this.personalInfo = "";
        this.email = "";
        this.phone = "";
        this.isAdmin = false; 
    }


    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.personalInfo = "";
        this.email = "";
        this.phone = "";
        this.isAdmin = false; 
    }


    public User(String firstName, String lastName, String username, String password, String personalInfo, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.personalInfo = personalInfo;
        this.email = email;
        this.phone = phone;
        this.isAdmin = false; 
    }


    public User(String firstName, String lastName, String username, String password, String personalInfo, String email, String phone, boolean isAdmin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.personalInfo = personalInfo;
        this.email = email;
        this.phone = phone;
        this.isAdmin = isAdmin; 
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

 
    public boolean isAdmin() {
        return isAdmin;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

 
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
