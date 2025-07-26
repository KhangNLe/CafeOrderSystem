package Roles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public abstract class Role {

    protected String userName;
    protected String password;

    @JsonCreator
    public Role(
            @JsonProperty("userName") String userName,
            @JsonProperty("password") String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean validateCredentials(String inputUserName, String inputPassword) {
        return userName.equals(inputUserName) && password.equals(inputPassword);
    }

    public String getDisplayName() {
        return userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public abstract void goToPage();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Role role)){ return false; }

        return validateCredentials(role.getUserName(), role.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

}
