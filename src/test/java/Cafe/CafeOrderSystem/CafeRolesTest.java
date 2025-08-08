package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.Roles.CafeRole;
import Cafe.CafeOrderSystem.Roles.EmployeesAuthentication;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CafeRolesTest {
    private record UserTest(String roleType, String name, String pass){}

    private boolean isExist(List<CafeRole> roles, UserTest user){
        for (CafeRole role : roles){
            if (role.validateCredentials(user.roleType(), user.name(), user.pass())){
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("Test for role")
    void testForRoleAuthentication(){
        List<CafeRole> roles = new ArrayList<>();
        roles.add(new CafeRole("Barista", "user1", "password1"));

        roles.add(new CafeRole("Barista", "user2", "password2"));


        UserTest user1 = new UserTest("Barista", "user1", "password1");
        UserTest user2 = new UserTest("Barista", "user4", "password5");

        assertTrue(isExist(roles, user1));
        assertFalse(isExist(roles, user2));

    }

    @Test
    @DisplayName("Test for Employee Authentification")
    void testForEmployeeAuthentication(){
        Cafe cafeShop = new Cafe();
        cafeShop.startShop();

        EmployeesAuthentication auth = cafeShop.getEmployeesAuthentication();

        UserTest barista1 = new UserTest("Barista", "barista1", "barista1");
        UserTest barista2 = new UserTest("", "barista", "barista");
        UserTest manager1 = new UserTest("Manager","manager1", "manager1");
        UserTest manager2 = new UserTest("", "manager", "manager");

        assertTrue(auth.validateCredentials(barista1.roleType(), barista1.name(), barista1.pass()));
        assertTrue(auth.validateCredentials(manager1.roleType(), manager1.name(), manager1.pass()));

        assertFalse(auth.validateCredentials(manager2.roleType(), manager2.name(),
                manager2.pass()));
        assertFalse(auth.validateCredentials(barista2.roleType(), barista2.name(), barista2.pass()));
    }
}
