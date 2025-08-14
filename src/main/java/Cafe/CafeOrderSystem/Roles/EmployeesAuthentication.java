package Cafe.CafeOrderSystem.Roles;

import Cafe.CafeOrderSystem.JsonParser.Authentication.AuthenticationParser;

import java.util.*;

/**
 * Provides authentication functionality for employees based on their assigned roles.
 * <p>
 * This class uses an {@link AuthenticationParser} to retrieve a collection of
 * {@link CafeRole} objects, and checks if the provided credentials match any existing
 * employee record.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * AuthenticationParser parser = new JsonAuthenticationParser("roles.json");
 * EmployeesAuthentication authService = new EmployeesAuthentication(parser);
 *
 * boolean isValid = authService.validateCredentials("Manager", "alice", "secret123");
 * }</pre>
 * </p>
 */
public class EmployeesAuthentication {
    private final AuthenticationParser  authenticationParser;

    /**
     * Creates a new {@code EmployeesAuthentication} service.
     *
     * @param authenticationParser the parser responsible for retrieving employee roles and credentials;
     *                              must not be {@code null}
     * @throws NullPointerException if {@code authenticationParser} is {@code null}
     */
    public EmployeesAuthentication(AuthenticationParser authenticationParser) {
        this.authenticationParser = authenticationParser;
    }

    /**
     * Validates whether the provided role type, username, and password match
     * any employee's stored credentials.
     *
     * @param roleType the role type to check (e.g., "Manager", "Cashier"); must not be {@code null}
     * @param userName the username to check; must not be {@code null}
     * @param password the password to check; must not be {@code null}
     * @return {@code true} if the credentials match an existing employee, {@code false} otherwise
     */
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