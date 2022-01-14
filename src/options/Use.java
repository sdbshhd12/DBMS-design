package options;

import domain.Database;

public class Use {

    static public void use(String command){

        String[] str = command.split(" ");
        if(str.length > 1){
            System.out.println("ERROR: 语法错误。");
            return;
        }
        Database.setNowDataBaseName(command);
        System.out.println("Database '" +Database.nowDataBaseName + "' is used.");
    }

}
