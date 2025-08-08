package Cafe.CafeOrderSystem.JsonParser.Authentication;

import Cafe.CafeOrderSystem.JsonParser.ItemsParser;
import Cafe.CafeOrderSystem.JsonParser.JsonCollection;
import Cafe.CafeOrderSystem.Roles.CafeRole;

public class AuthenticationParser extends JsonCollection<CafeRole> {
    public AuthenticationParser(String dirPath) {
        super(new ItemsParser(), dirPath, CafeRole.class);
    }

}
