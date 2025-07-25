package Roles;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;

public class RolesList extends JsonCollection<Role> {
    /**
     * This class is responsible for creating and managing a collection of Role objects
     * @param fileParser The relevant parser for the object
     * @param folderPath The folder from which the collection fills and writes to
     */
    protected RolesList(ItemsParser fileParser, String folderPath) {
        super(fileParser, folderPath, Role.class);
    }

    /**
     * Creates and prepares a roleList for the class
     * @return
     */
    public static RolesList newRoleList(){
        ItemsParser roleParser = new ItemsParser();
        String rolesFolder = "";
        RolesList startingRolesList = new RolesList(roleParser, rolesFolder);
        startingRolesList.startCollection();
        return startingRolesList;
    }



}
