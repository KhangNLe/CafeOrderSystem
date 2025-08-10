package Cafe.CafeOrderSystem.Roles;

import Cafe.CafeOrderSystem.JsonParser.Authentication.AuthenticationParser;

import java.util.*;

public class EmployeesAuthentication {
    private final AuthenticationParser  authenticationParser;

    public EmployeesAuthentication(AuthenticationParser authenticationParser) {
        this.authenticationParser = authenticationParser;
    }

    public boolean validateCredentials(String roleType, String userName, String password){
        List<CafeRole> employees = authenticationParser.getCollection();

        for (CafeRole cafeRole : employees) {
            if (cafeRole.validateCredentials(roleType, userName, password)) {
                return true;
            }
        }
        return false;
    }
}
