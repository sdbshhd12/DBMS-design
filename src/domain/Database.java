package domain;

import java.util.Set;

public class Database {

    private String databaseName;
    private Set<Table> tableSet;

    static public String nowDataBaseName;
    static public String path = "D:\\MyDatabases\\";

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Set<Table> getTableSet() {
        return tableSet;
    }

    public void setTableSet(Set<Table> tableSet) {
        this.tableSet = tableSet;
    }


    static public String getNowDatabaseName(){
        return nowDataBaseName;
    }

    static public void setNowDataBaseName(String name){
        nowDataBaseName = name;
    }
}
