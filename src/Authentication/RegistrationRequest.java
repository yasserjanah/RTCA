package Authentication;

import java.io.Serializable;

public class RegistrationRequest implements Serializable {

    private String username;
    private String password;

    public RegistrationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
