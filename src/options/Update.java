package options;

import domain.Table;
import function.Check;
import function.Read;

import java.util.*;

public class Update {

    /*  UPDATE
        Person SET Name='aaa', Gender='bbb' WHERE No='xxx' */

    static public void update(String command){
        String[] commands = command.split(" ", 3);
        String tableName = commands[0];
        commands = commands[2].split("WHERE");
        System.out.println("---------");
        String[] cols = commands[0].split(",");
        String[] where = commands[1].split("AND|OR");

        Table table = Read.readTable(tableName);

        //数据存放链表
        List<String> updateMembersList = new LinkedList<>();
        List<String> updateValuesList = new LinkedList<>();
        List<String> whereList = new LinkedList<>();
        List<String> whereMembersList = new LinkedList<>();
        List<String> whereValuesList = new LinkedList<>();
        List<String> whereConnectList = new ArrayList<>();

        Collections.addAll(whereList, where);

        for(String str : cols){
            String[] s = str.trim().split("=");
            String member = s[0];
            String value = s[1];
            if(s[1].contains("'")){
                value = s[1].trim().substring(1,s[1].length()-1);
            }
            updateMembersList.add(member);
            updateValuesList.add(value);
        }

        //添加时的外键检测
        if(!Check.foreignCheck(table, updateMembersList, updateValuesList, "=")){
            return;
        }

        //检查主键是否唯一
        if(table.getPrimaryKey()!=null){
            int insertPrimaryIndex = updateMembersList.indexOf(table.getPrimaryKey());
            if(insertPrimaryIndex != -1){
                int tablePrimaryIndex = table.getVName().indexOf(table.getPrimaryKey());
                for(int i=0; i<table.getRow().size(); ++i){
                    if(table.getRow().get(i).get(tablePrimaryIndex).equalsIgnoreCase(updateValuesList.get(insertPrimaryIndex))){
                        System.out.println("ERROR: 主键重复。");
                        return;
                    }
                }
            }
        }

        //获得表中所有数据
        List<List<String>> tableList = table.getRow();
        List<String> vname = table.getVName();

        //对表中数据进行筛选
        for(String str : where){
            String[] s = str.trim().split("=|>|<|<>");
            whereMembersList.add(s[0]);
            if(s[1].contains("'")){
                whereValuesList.add(s[1].trim().substring(1, s[1].length()-1));
            }else {
                whereValuesList.add(s[1].trim());
            }
        }

        String[] whereConnect = commands[1].trim().split(" ");
        for(int i=0; i<whereConnect.length; ++i) {
            if(whereConnect[i].equals("AND")){
                whereConnectList.add("AND");
            }else if(whereConnect[i].equals("OR")){
                whereConnectList.add("OR");
            }
        }

        for(List<String> list : tableList){
            boolean flag = true;
            String operator = "";
            List<Boolean> whereVerifyList = new ArrayList<>();
            // 查找要修改的元组
            for(int i=0; i<whereMembersList.size(); ++i){
                int index = vname.indexOf(whereMembersList.get(i));
//                System.out.println(whereMembersList.get(i));
                if(index >= 0){
                    if(whereList.get(i).contains("=")){  //判断相等条件
                        operator = "=";
                        // 如果此元组的index项与这一判断条件不相等
                        if(!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)){
                            whereVerifyList.add(false);
                        }
                        else {
                            whereVerifyList.add(true);
                        }
                    }
                    if(whereList.get(i).contains("<")){
                        operator = "<";
                        if(!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)){
                            whereVerifyList.add(false);
                        }
                        else {
                            whereVerifyList.add(true);
                        }
                    }
                    if(whereList.get(i).contains(">")){
                        operator = ">";
                        if(!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)){
                            whereVerifyList.add(false);
                        }
                        else {
                            whereVerifyList.add(true);
                        }
                    }
                    if(whereList.get(i).contains("<=")){
                        operator = "<=";
                        if(!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)){
                            whereVerifyList.add(false);
                        }
                        else {
                            whereVerifyList.add(true);
                        }
                    }
                    if(whereList.get(i).contains(">=")){
                        operator = ">=";
                        if(!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)){
                            whereVerifyList.add(false);
                        }
                        else {
                            whereVerifyList.add(true);
                        }
                    }
                    if(whereList.get(i).contains("<>")){
                        operator = "<>";
                        if(!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)){
                            whereVerifyList.add(false);
                        }
                        else {
                            whereVerifyList.add(true);
                        }
                    }
                }
                else {
                    System.out.println("ERROR: 查询条件错误。");
                }
            }
            flag = Check.verifyCheck(whereConnectList, whereVerifyList);
            if(flag){
                if(Check.relationCheck(table, whereMembersList, whereValuesList, operator)){    //删除时的外键检测
                    for(int i=0; i<updateMembersList.size(); ++i){
                        int index = vname.indexOf(updateMembersList.get(i));
                        if(index >= 0){
                            list.set(index, updateValuesList.get(i));
//                            table.writeInFile();
                        }
                        else {
                            System.out.println("ERROR: 更新条件错误。");
                        }
                    }
                }else if(!Check.relationCheck(table, updateMembersList, updateValuesList, operator)){
                    for(int i=0; i<updateMembersList.size(); ++i){
                        int index = vname.indexOf(updateMembersList.get(i));
                        if(index >= 0){
                            list.set(index, updateValuesList.get(i));
//                            table.writeInFile();
                        }
                        else {
                            System.out.println("ERROR: 更新条件错误。");
                        }
                    }
                }
//                table.show();

            }

        }
        table.show();
//        table.writeInFile();

//        Map<Integer, String> nameIndex = new HashMap<>();
//        for(Map.Entry<String,String> entry : whereMap.entrySet()){
//            if(vname.contains(entry.getKey())){
//                nameIndex.put(vname.indexOf(entry.getKey()), entry.getValue());
//            }
//        }
//        for(List<String> list : tableList){
//            boolean flag = true;
//            for(Map.Entry<Integer,String> entry : nameIndex.entrySet()){
//                if (!list.get(entry.getKey()).equalsIgnoreCase(entry.getValue())) {
//                    flag = false;
//                    break;
//                }
//            }
//            if(flag){
//                for(Map.Entry<String,String> entry : colsMap.entrySet()){
//                    try{
//                        list.set(vname.indexOf(entry.getKey()), entry.getValue());
//                    }
//                    catch(Exception e) {
//                        System.out.println("ERROR: 数据输入有误，列表项不存在。");
//                    }
//                }
//            }
//        }
//        System.out.println("After update: ");
//        table.show();


    }

}

