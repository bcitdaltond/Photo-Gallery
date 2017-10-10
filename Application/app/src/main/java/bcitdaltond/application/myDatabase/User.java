package bcitdaltond.application.myDatabase;

import android.content.ContentValues;

/**
 * Created by runej on 2017-09-19.
 */

public class User implements IDBClass {
    private int id;
    private String username; //email
    private String password;
    private String lastname;
    private String firstname;

    /**
     * The current user of the app. A driver.
     * @param username The email used by the user
     * @param password The password used by the user
     * @param firstname User's first name
     * @param lastname User's last name
     */
    public User(int id,
                String username,
                String password,
                String firstname,
                String lastname
    ) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public ContentValues getContent() {
        return null;
    }
}