package Cafe.CafeOrderSystem;

import Cafe.CafeOrderSystem.Roles.BaristaRole;
import Cafe.CafeOrderSystem.Roles.EmployeesAuthentication;
import Cafe.CafeOrderSystem.Roles.ManagerRole;
import Cafe.CafeOrderSystem.Roles.CafeRoles;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CafeRolesTest {
    private record UserTest(String name, String pass){}

    private boolean isExist(List<CafeRoles> roles, UserTest user){
        for (CafeRoles role : roles){
            if (role.validateCredentials(user.name(), user.pass())){
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("Test for role")
    void testForRoleAuthentication(){
        List<CafeRoles> roles = new ArrayList<>();
        roles.add(new BaristaRole("user1", "password1"));
        roles.add(new ManagerRole("user2", "password2"));


        UserTest user1 = new UserTest("user1", "password1");
        UserTest user2 = new UserTest("user4", "password5");

        assertTrue(isExist(roles, user1));
        assertFalse(isExist(roles, user2));

    }

    @Test
    @DisplayName("Test for Employee Authentification")
    void testForEmployeeAuthentication(){
        Cafe cafeShop = new Cafe();
        cafeShop.startShop();

        EmployeesAuthentication auth = cafeShop.getEmployeesAuthentication();

        UserTest barista1 = new UserTest("barista1", "barista1");
        UserTest barista2 = new UserTest("barista", "barista");
        UserTest manager1 = new UserTest("manager1", "manager1");
        UserTest manager2 = new UserTest("manager", "manager");

        assertTrue(auth.baristaLogin(barista1.name(), barista1.pass()));
        assertFalse(auth.baristaLogin(barista2.name(), barista2.pass()));

        assertTrue(auth.managerLogin(manager1.name(), manager1.pass()));
        assertFalse(auth.managerLogin(manager2.name(), manager2.pass()));
    }
}
