package Roles;

public abstract class Role {

    protected String userName;
    protected String password;

    public Role(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean validatecredentials(String inputuserName, String inputpassword) {
        return userName.equals(inputuserName) && password.equals(inputpassword);
    }

    public String getDisplayname() {
        return userName;
    }
    public abstract void goToPage();

    @Override
    public String toString() {
        return getDisplayname();
    }

}
