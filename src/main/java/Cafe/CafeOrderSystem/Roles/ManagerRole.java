package Cafe.CafeOrderSystem.Roles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ManagerRole extends Role {

    @JsonCreator
    public ManagerRole(
            @JsonProperty("userName") String userName,
            @JsonProperty("password") String password) {
        super(userName, password);
    }
    @Override
    public void goToPage() {
            System.out.println("Welcome Manager" + userName +"!");
        }
}


