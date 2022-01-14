package function;

import domain.Database;
import domain.Table;
import domain.User;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class Read {

    static public String readCommand(){
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine().trim();
        String str = "";
        while (true){
            if(command.endsWith(";")){
                break;
            }
            command += " ";
            str = sc.nextLine().trim();
            command += str;
        }
        command = command.substring(0, command.length()-1).replaceAll(" {2,}", " ").trim();

        return command;
    }

    static public Table readTable(String tableName){
        Table table = new Table();
        if(Database.nowDataBaseName == null){
            System.out.println("ERROR: 请先指定数据库。");
            return null;
        }
        String tablePath = Database.path + Database.nowDataBaseName + "\\Table\\" + tableName + ".dbf";
        File file = new File(tablePath);
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            table = (Table)ois.readObject();
            ois.close();
        }catch (Exception e){
            System.out.println("ERROR: 表不存在。");
        }
        return table;
    }
    static public Table readTable(String tableName, String databaseName){
        Table table = new Table();
        String tablePath = Database.path + databaseName + "\\Table\\" + tableName + ".dbf";
        File file = new File(tablePath);
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            table = (Table)ois.readObject();
            ois.close();
        }catch (Exception e){
            System.out.println("ERROR: 表不存在。");
        }
        return table;
    }

    static public User readUser(String hostname){
        User user = new User();
        String tablePath = "D:\\Mydatabases\\User\\" + hostname + ".dbuser";
        File file = new File(tablePath);
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            user = (User) ois.readObject();
            ois.close();
        }catch (Exception e){
            System.out.println("ERROR: 用户不存在");
        }

        return user;
    }

}
