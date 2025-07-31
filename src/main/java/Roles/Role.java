package Roles;

public class Role {
     private String roleName;
     private String loginType;
     private String username;
     private String password;
     private String redirectPage;

     public Role(String roleName, String loginType, String username, String password, String redirectPage) {
         this.roleName = roleName;
         this.loginType = loginType;
         this.username = username;
         this.password = password;
         this.redirectPage = redirectPage;
     }

     public boolean validateCredentials(String inputUsername, String inputPassword) {
         return this.username.equals(inputUsername) && this.password.equals(inputPassword);
     }

     // Backend only: return FXML path instead of loading it
     public String getRedirectPage() {
         return redirectPage;
     }

     public String getUsername() {
         return username;
     }

     public String getRoleName() {
         return roleName;
     }

     public String getLoginType() {
         return loginType;
     }

     @Override
     public String toString() {
         return roleName + " (" + username + ")";
     }
 }