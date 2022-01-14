package options;

import domain.Table;
import function.Read;

public class Show {

    static public void show(String str){
        if(str.split(" ")[0].equalsIgnoreCase("user"));
        Table table = Read.readTable(str);
        table.show();
    }

}
