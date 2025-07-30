package Cafe.CafeOrderSystem.Roles;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;

public class RolesList<T extends Role> extends JsonCollection<T> {
    /**
     * This class is responsible for creating and managing a collection of Role objects
     * @param fileParser The relevant parser for the object
     * @param folderPath The folder from which the collection fills and writes to
     */
    private RolesList(ItemsParser fileParser, String folderPath, Class<T> type) {
        super(fileParser, folderPath, type);
    }


    public static RolesList<ManagerRole> newManagerRoleList(String path){
        return new RolesList<>(new ItemsParser(), path, ManagerRole.class);
    }

    public static RolesList<BaristaRole> newBaristaRoleList(String path){
        return new RolesList<>(new ItemsParser(), path, BaristaRole.class);
    }
}
