package Roles;

public class BaristaRole extends Role{

    public BaristaRole(String userName, String password) {
        super(userName, password);
    }
    @Override
    public void goToPage() {
        System.out.println("Welcome Barista" + userName +"!");
    }
}


