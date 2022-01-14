package options;

import domain.Database;
import domain.Table;
import function.Read;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class Help {

    static public void help(String command){
        String[] commands = command.split(" ");
        if(commands[0].equalsIgnoreCase("table")){
            showTable(commands[1].trim());
        }
        else if(commands[0].equalsIgnoreCase("database")){
            showDatabase(commands[1].trim());
        }else if(commands[0].equalsIgnoreCase("view")){
            showView(commands[1].trim());
        }


    }

    static private void showTable(String tableName){
        Table table = Read.readTable(tableName);
        table.show();
    }

    static private void showView(String viewName){
        String viewPath = "D:\\MyDatabases\\" + Database.nowDataBaseName + "\\View\\" + viewName + ".view";
        File file = new File(viewPath);
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            String str = bfr.readLine();
            System.out.println(str);
        }catch (Exception e){
            System.out.println("ERROR: 视图不存在。");
        }
    }

    static private void showDatabase(String databaseName){
        String tablePath = "D:\\MyDatabases\\" + databaseName + "\\Table\\";
        File file = new File(tablePath);
        File[] tempList = file.listFiles();
        List<String> fileNameList = new LinkedList<>();
        for (int i = 0; i < tempList.length; i++) {
            fileNameList.add(tempList[i].getName());
        }
        System.out.println("-----------------------");
        System.out.println("         Table         ");
        System.out.println("-----------------------");
        for(String tableName : fileNameList){
            Table table = Read.readTable(tableName.split("\\.")[0], databaseName);
            System.out.println("  "+table.getName());
            System.out.println("--------------");
            List<String> nameList = table.getVName();
            for(String name : nameList){
                System.out.println(name);
            }
            System.out.println("-----------------------");
        }
        System.out.println("         View         ");
        System.out.println("-----------------------");

        tablePath = "D:\\MyDatabases\\" + databaseName + "\\View\\";
        file = new File(tablePath);
        tempList = file.listFiles();
        fileNameList = new LinkedList<>();
        for (int i = 0; i < tempList.length; i++) {
            fileNameList.add(tempList[i].getName());
        }
        for(String view : fileNameList){
            String viewName = view.split("\\.")[0];
            System.out.println(viewName);
        }

    }


}
