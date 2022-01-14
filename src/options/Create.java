package options;

import domain.Database;
import domain.Root;
import domain.Table;
import domain.User;
import function.Check;
import function.Read;

import java.io.*;
import java.util.*;

public class Create {

    static public void create(String command){
        String[] commands = command.split(" ");
        if(commands[0].equalsIgnoreCase("TABLE")){
            if(Database.nowDataBaseName == null){
                System.out.println("ERROR: 请先指定数据库");
            }else{
                createTable(command);
            }
        }else if(commands[0].equalsIgnoreCase("DATABASE")){
            createDatabase(command);
        }else if(commands[0].equalsIgnoreCase("VIEW")){
            createView(command);
        }
        else if(commands[0].equalsIgnoreCase("INDEX")){
            createIndex(command);
        }
        else if(commands[0].equalsIgnoreCase("USER")){
            createUser(command);
        }
    }

    /* 创建数据库 */
    static private void createDatabase(String command){
        String[] commands = command.split(" ");
        String databaseName = commands[1];
        //创建数据库
        String databasePath = "D:\\MyDatabases\\" + databaseName;
        File dir = new File(databasePath);
        if(dir.exists()){
            System.out.println("ERROR: 数据库已存在。");
        } else {
            if(!dir.mkdir()){
                System.out.println("ERROR: 创建数据库文件夹出错~");
            }
        }
    }

    /* 创建表 */
    static private void createTable(String command){
        Table table = new Table();
        String[] commands = command.split(" ");

        //------------- 获取表名 -------------------
        table.setName(commands[1]);

        //----------- 获取括号之间的内容 ---------------
        int index = command.indexOf("(");
        int lastIndex = command.lastIndexOf(")");
        String body = command.substring(index+1, lastIndex);

        String[] members = body.split(",");
//        Map<String, String> memberMap = new TreeMap<>();

        List<String> memberList = new LinkedList<>();

        List<Boolean> canBeNullList = new LinkedList<>();

        //-----------唯一性验证---------------
        List<String> checkList = new LinkedList<>();
        Collections.addAll(checkList, members);
        Set<String> checkSet = new TreeSet<>(checkList);
        if(checkList.size() != checkSet.size()){
            System.out.println("ERROR: 存在重复成员，请检查。");
            return;
        }

        for(String m:members){
            String[] value = m.trim().split(" ");
            if(m.contains("PRIMARY KEY")){
                table.setPrimaryKey(value[1]);
            }
            if(m.contains("FOREIGN KEY")){
                /*  xxx FOREIGN KEY Person(No) xxx */
                String[] str = m.split("FOREIGN KEY ", 2);
                String foreignKey = str[1].trim().split(" ")[0].trim();
                int fIndex = foreignKey.indexOf("(");
                int lIndex = foreignKey.lastIndexOf(")");
                String relatedTable = foreignKey.substring(0, fIndex);
                String relatedKey = foreignKey.substring(fIndex+1, lIndex);
                table.addForeignKey(relatedTable, relatedKey, value[1]);

                Table pTable = Read.readTable(relatedTable);
                pTable.addRelatedKeyList(relatedKey,table.getName(),value[1]);    //次序值得注意
                pTable.writeInFile();
            }
            if(m.contains("NOT NULL")){
                canBeNullList.add(false);
            }else {
                canBeNullList.add(true);
            }
            memberList.add(value[1]);
        }

        table.setVName(memberList);
        table.setCanBeNull(canBeNullList);

        /*
        使用Map，效果不好，因为输入的顺序会乱
        List<String> vNameList = new LinkedList<>();
        for(Map.Entry<String,String> entry : memberMap.entrySet()){
            vNameList.add(entry.getKey());
        }
        table.setVName(vNameList);
        */

        //---------------- 测试 -------------------
        table.show();

        //------------ 写入文件 -----------------
        System.out.println("A new table named " + table.getName() + " has been created!");
        String tablePath = "D:\\MyDatabases\\" + Database.nowDataBaseName + "\\Table\\" + commands[1] + ".dbf";
        File file = new File(tablePath);
        // 序列化写入
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(table);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 创建视图 */
    static public void createView(String command){

        // CREATE
        // VIEW XXX AS STR

        int asIndex = command.indexOf("AS");
        int nameIndex = 5;
        String viewName = command.substring(nameIndex, asIndex).trim();
        String body = command.substring(asIndex+3);

        //安全性检测
        int selectIndex = command.indexOf("SELECT");
        int fromIndex = command.indexOf("FROM");
        int whereIndex = command.indexOf("WHERE");
        String[] selects = command.substring(selectIndex+6, fromIndex).trim().split(",");
        String[] froms;
        String[] wheres;
        List<String> whereMembersList = new ArrayList<>();

        if(whereIndex != -1){
            froms = command.substring(fromIndex+4, whereIndex).trim().split(",");
            wheres = command.substring(whereIndex+5).trim().split("AND|OR|LIKE");
            for(String s : wheres){
                whereMembersList.add(s.split("=|>|<|<>")[0]);
            }
        }else {
            froms = command.substring(fromIndex+4).trim().split(",");
        }

        String[] whereMembers = new String[whereMembersList.size()];
        whereMembersList.toArray(whereMembers);

        if(!selects[0].equals("*")){
            if(!Check.tableNameCheck(froms, selects)){
                return;
            }
        }if(whereIndex != -1){
            if(!Check.tableNameCheck(froms, whereMembers)){
                return;
            }
        }

        System.out.println(body);
        String viewPath = "D:\\MyDatabases\\" + Database.nowDataBaseName + "\\View\\" + viewName + ".view";
        File file = new File(viewPath);
        try{
            BufferedWriter bfw = new BufferedWriter(new FileWriter(file));
            bfw.write(body);
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static public void createIndex(String command){
        /* CREATE
        *  INDEX PersonIndex ON Person(Name) */

        int onIndex = command.indexOf("ON");
        String indexName = command.substring(6,onIndex-1);
        int colIndex1 = command.indexOf("(");
        int colIndex2 = command.indexOf(")");
        String tableName = command.substring(onIndex+3, colIndex1);
        String col = command.substring(colIndex1+1, colIndex2);

        System.out.println(indexName);
        System.out.println(tableName);
        System.out.println(col);

        String[] tables = {tableName};
        String[] cols = col.split(",");
        if(!Check.tableNameCheck(tables, cols)){
            return;
        }
    }

    static public void createUser(String command){
        /*  CREATE USER 'hostname' IDENTIFED BY 'password'  */

        int indentifedIndex = command.indexOf("IDENTIFED");
        int byIndex = indentifedIndex+12;
        String hostname = command.substring(6, indentifedIndex-2);
        String password = command.substring(byIndex+2,command.length()-1);

        User user = new User(hostname, password);
        user.writeInFile();

    }

}
