package Cafe.CafeOrderSystem.Roles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class CafeRole {
     private String roleType;
     private String userName;
     private String password;

     @JsonCreator
     public CafeRole(
             @JsonProperty("roleType") String roleType,
             @JsonProperty("userName") String userName,
             @JsonProperty("password") String password) {
         this.roleType = roleType;
         this.userName = userName;
         this.password = password;
     }

     public boolean validateCredentials(String roleType, String inputUsername,
                                        String inputPassword) {
         return roleType.equals(this.roleType)
                 && userName.equals(inputUsername)
                 && password.equals(inputPassword);
     }

     public String getRoleType() {
         return roleType;
     }

     public String getUserName() {
         return userName;
     }

     public String getPassword() {
         return password;
     }

     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (!(o instanceof CafeRole role)) return false;
         return role.getRoleType().equals(roleType)
                 && role.getUserName().equals(userName)
                 && role.getPassword().equals(password);
     }

     @Override
     public int hashCode() {
         return Objects.hash(roleType, userName, password);
     }

     @Override
     public String toString() {
         return roleType + " (" + userName + ")";
     }
 }
