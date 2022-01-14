package options;

import domain.Table;
import function.Check;
import function.Read;

import java.util.*;

public class Delete {

    /*  DELETE
    FROM Person WHERE Gender='Male', No='10'  */
    public static void delete(String command){

        String[] commands = command.split(" ",3);
        String tableName = commands[1];

        commands = commands[2].split(" ", 2);
        String body = commands[1];

//        System.out.println("tableName = "+tableName);
//        System.out.println("body : "+body);

        Table table = Read.readTable(tableName);

        List<String> whereList = new LinkedList<>();
        List<String> whereMembersList = new LinkedList<>();
        List<String> whereValuesList = new LinkedList<>();
        List<String> whereConnectList = new ArrayList<>();

        String[] wheres = body.split("AND|OR");
        Collections.addAll(whereList, wheres);
        for(String str : wheres){
            String[] s = str.trim().split("=|>|<|<>");
            whereMembersList.add(s[0]);
            if(s[1].contains("'")){
                whereValuesList.add(s[1].trim().substring(1, s[1].length()-1));
            }else {
                whereValuesList.add(s[1].trim());
            }

        }
        String[] whereConnect = body.trim().split(" ");
        for(int i=0; i<whereConnect.length; ++i) {
            if(whereConnect[i].equals("AND")){
                whereConnectList.add("AND");
            }else if(whereConnect[i].equals("OR")){
                whereConnectList.add("OR");
            }
        }

        //检查表是否存在
        String[] tables = {tableName};
        String[] whereMembers = new String[whereMembersList.size()];
        whereMembersList.toArray(whereMembers);
        if(!Check.tableNameCheck(tables, whereMembers)){
            return;
        }


        List<String> vname = table.getVName();
        Iterator<List<String>> it = table.getRow().iterator();
        while(it.hasNext()){
            List<String> list = it.next();
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
                    it.remove();
                    System.out.println("Remove Person'name is " + list.get(1));
                    table.writeInFile();
                }
            }

        }
        table.show();


    }

}
