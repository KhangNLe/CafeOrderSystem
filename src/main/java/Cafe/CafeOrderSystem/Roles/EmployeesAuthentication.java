package Cafe.CafeOrderSystem.Roles;

import java.util.*;

public class EmployeesAuthentication {
    private final RolesList baristaAccounts;
    private final RolesList managerAccounts;

    public EmployeesAuthentication(RolesList baristaAccounts, RolesList managerAccounts) {
        this.baristaAccounts = baristaAccounts;
        this.managerAccounts = managerAccounts;
    }

    public boolean baristaLogin(String username, String password) {
        return verifyLogin(username, password, baristaAccounts.getCollection());
    }

    public boolean managerLogin(String username, String password) {
        return verifyLogin(username, password, managerAccounts.getCollection());
    }

    private boolean verifyLogin(String username, String password, List<Role> role) {
        for (Role acc : role) {
            if (acc.validateCredentials(username, password)) {
                return true;
            }
        }
        return false;
    }
}
