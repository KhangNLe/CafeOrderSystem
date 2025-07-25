package Roles;

public class ManagerRole extends Role {


        public ManagerRole(String userName, String password) {
            super(userName, password);
        }
        @Override
        public void goToPage() {
            System.out.println("Welcome Manager" + userName +"!");
        }
    }


