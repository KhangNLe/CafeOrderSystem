package Cafe.CafeOrderSystem.Authentication;

import java.util.*;

public class EmployeesAuthentication {
    private final Map<String, String> baristaAccounts;
    private final Map<String, String> managerAccounts;
    private static EmployeesAuthentication instance;

    private EmployeesAuthentication() {
        this.baristaAccounts = new HashMap<>();
        this.managerAccounts = new HashMap<>();
        instance = null;
    }

    public static EmployeesAuthentication getInstance() {
        if (instance == null) {
            instance = new EmployeesAuthentication();
        }
        return instance;
    }

    //this will be removed during deployment
    public static void destroyInstance() {
        instance = null;
    }

    public void addBaristaAccount(String username, String password) {
        verifyAccount(username, baristaAccounts, "barista accounts");
        baristaAccounts.put(username, password);
    }

    public void addManagerAccount(String username, String password) {
        verifyAccount(username, managerAccounts, "manager accounts");
        managerAccounts.put(username, password);
    }

    public boolean baristaLogin(String username, String password) {
        if (baristaAccounts.containsKey(username)) {
            return baristaAccounts.get(username).equals(password);
        }
        return false;
    }

    public boolean managerLogin(String username, String password) {
        if (managerAccounts.containsKey(username)) {
            return managerAccounts.get(username).equals(password);
        }
        return false;
    }

    private void verifyAccount(String username, Map<String, String> accounts, String fileName) {
        if (accounts.containsKey(username)) {
            throw new IllegalArgumentException(
                    String.format("Username %s is already in use for %s",
                            username, fileName
            ));
        }
    }
}
