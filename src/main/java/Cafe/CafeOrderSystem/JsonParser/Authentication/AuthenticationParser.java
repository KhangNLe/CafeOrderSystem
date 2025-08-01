package Cafe.CafeOrderSystem.JsonParser.Authentication;

import Cafe.CafeOrderSystem.JsonParser.Parsers;
import Cafe.CafeOrderSystem.Roles.BaristaRole;
import Cafe.CafeOrderSystem.Roles.ManagerRole;
import Cafe.CafeOrderSystem.Roles.RolesList;

public class AuthenticationParser implements Parsers {
    private RolesList<ManagerRole> managerAccounts;
    private RolesList<BaristaRole> baristaAccounts;

    public AuthenticationParser(RolesList<ManagerRole> managerAccounts,
                                RolesList<BaristaRole> baristaAccounts) {
        this.managerAccounts = managerAccounts;
        this.baristaAccounts = baristaAccounts;
    }

    public RolesList<ManagerRole> getManagerAcc(){
        return managerAccounts;
    }

    public RolesList<BaristaRole> getBaristaAcc(){
        return baristaAccounts;
    }

    @Override
    public void startCollection(){
        managerAccounts.startCollection();
        baristaAccounts.startCollection();
    }

    @Override
    public void endCollection(){}
}
