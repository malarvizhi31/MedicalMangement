package example.medicalmangement;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "staffs")
public class Staff {
    @Id
    private String id;
    private String username;
    private String password;

    public Staff() {}
    public Staff(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

