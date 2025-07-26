package  Roles;
import java.util.*;
public class RoleTest {
    public static void main(String[] args) {
        List<Role> roles;
        roles = new ArrayList<>();
        roles.add(new BaristaRole("Admin","password"));
        roles.add(new ManagerRole("Manager","password"));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Role Name: ");
        String roleName = scanner.nextLine();
        System.out.println("Enter password");
        String passwordinput = scanner.nextLine();

        Role loggedinrole = null;

        for(Role role : roles) {
            if (role.validateCredentials(roleName, passwordinput)) {
                loggedinrole = role;
                break;
            }
        }
        if(loggedinrole != null) System.out.println("Valid Credentials");
        else{
            System.out.println("Invalid Credentials");
        }
    }
}
