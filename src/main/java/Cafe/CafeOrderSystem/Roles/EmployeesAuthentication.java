package Cafe.CafeOrderSystem.Roles;

import java.util.*;

public class EmployeesAuthentication {
    private final RolesList<BaristaRole> baristaAccounts;
    private final RolesList<ManagerRole> managerAccounts;

    public EmployeesAuthentication(RolesList<BaristaRole> baristaAccounts,
                                   RolesList<ManagerRole> managerAccounts) {
        this.baristaAccounts = baristaAccounts;
        this.managerAccounts = managerAccounts;
    }

    public boolean baristaLogin(String username, String password) {
        return verifyLogin(username, password, baristaAccounts.getCollection());
    }

    public boolean managerLogin(String username, String password) {
        return verifyLogin(username, password, managerAccounts.getCollection());
    }

    private <T extends Role> boolean verifyLogin(String username, String password,
                                       List<T> role) {
        for (T acc : role) {
            if (acc.validateCredentials(username, password)) {
                return true;
            }
        }
        return false;
    }
}
