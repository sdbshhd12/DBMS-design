package options;

import domain.Root;
import domain.User;
import function.Read;

public class RootOptions {


    static public void grant(String command){

        /*
        * GRANT SELECT, UPDATE ON tableName TO username
        *  */

        int onIndex = command.indexOf("ON");
        int toIndex = command.indexOf("TO");

        String rootStr = command.substring(0, onIndex-1);
        String tableStr = command.substring(onIndex+3, toIndex-1);
        String userStr = command.substring(toIndex+3);


        String[] roots = rootStr.split(",");

        User user = Read.readUser(userStr);
        for(String s : roots){
            user.addRoot(tableStr,s);
        }

    }

    static public void revoke(String command){

        /*
         * REVOKE SELECT, UPDATE ON tableName TO username
         *  */

        int onIndex = command.indexOf("ON");
        int toIndex = command.indexOf("TO");

        String rootStr = command.substring(0, onIndex-1);
        String tableStr = command.substring(onIndex+3, toIndex-1);
        String userStr = command.substring(toIndex+3);


        String[] roots = rootStr.split(",");

        User user = Read.readUser(userStr);
        for(String s : roots){
            user.removeRoot(tableStr,s);
        }

    }

}
