package Cafe.CafeOrderSystem;

import Roles.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {
    private record UserTest(String name, String pass){}

    private boolean isExist(List<Role> roles, UserTest user){
        for (Role role : roles){
            if (role.validateCredentials(user.name(), user.pass())){
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("Test for role")
    void testForRoleAuthentication(){
        List<Role> roles = new ArrayList<>();
        roles.add(new BaristaRole("user1", "password1"));
        roles.add(new ManagerRole("user2", "password2"));


        UserTest user1 = new UserTest("user1", "password1");
        UserTest user2 = new UserTest("user4", "password5");

        assertTrue(isExist(roles, user1));
        assertFalse(isExist(roles, user2));

    }
}
