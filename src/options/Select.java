package options;

import domain.Table;
import function.Check;
import function.Read;

import java.util.*;

public class Select {

    /*
     * SELECT
     *     NO, Name
     * FROM
     *     Person, table2
     * WHERE
     *     Gender='Male'
     * */

    static public List<List<String>> select(String command) {

        List<List<String>> tempLists = new LinkedList<>();

        //--------------------解析字符串------------------
        /*  SELECT
         *   No, Name FROM Person WHERE Gender='male' AND No='12'  */
        int fromIndex = command.indexOf("FROM");
        int whereIndex = command.indexOf("WHERE");
        String[] selects = command.substring(0, fromIndex).trim().split(",");
        String[] froms;
        String[] wheres;

        List<String> whereMembersList = new ArrayList<>();
        List<String> whereValuesList = new ArrayList<>();
        List<String> whereList = new ArrayList<>();
        List<String> whereConnectList = new ArrayList<>();

        // 安全性检测
        if (whereIndex != -1) {
            froms = command.substring(fromIndex + 4, whereIndex).trim().split(",");
            wheres = command.substring(whereIndex + 5).trim().split("AND|OR|LIKE");
            for (String s : wheres) {
                String[] str = s.split(">=|<=|=|>|<|<>");
                whereMembersList.add(str[0].trim());
                if (str[1].contains("'")) {
//                    System.out.println(str[1]);
                    whereValuesList.add(str[1].trim().substring(1, str[1].trim().length() - 1));
                } else {
                    whereValuesList.add(str[1].trim());
                }

            }
            Collections.addAll(whereList, wheres);
            String[] whereConnect = command.substring(whereIndex + 5).trim().split(" ");
            for (int i = 0; i < whereConnect.length; ++i) {
                if (whereConnect[i].equals("AND")) {
                    whereConnectList.add("AND");
                } else if (whereConnect[i].equals("OR")) {
                    whereConnectList.add("OR");
                }
            }
        } else {
            froms = command.substring(fromIndex + 4).trim().split(",");
        }

        String[] whereMembers = new String[whereMembersList.size()];
        whereMembersList.toArray(whereMembers);

        if (!selects[0].equals("*")) {
            if (!Check.tableNameCheck(froms, selects)) {
                return null;
            }
        }
        if (whereIndex != -1) {
            if (!Check.tableNameCheck(froms, whereMembers)) {
                return null;
            }
        }

        //单表查询
        if (froms.length == 1) {
            // 显示表头
            String tableName = froms[0];
            Table table = Read.readTable(tableName);
            List<String> vname = table.getVName();
            List<Integer> vnameIndex = new LinkedList<>();
            for (int i = 0; i < selects.length; ++i) {    //获取要显示的列
                if (vname.get(i).contains(selects[i].trim())) {
                    vnameIndex.add(i);
                }
            }
//            System.out.println("------------------------------");
//            for(int i=0; i<vnameIndex.size(); ++i){
//                System.out.print(vname.get(vnameIndex.get(i))+"\t");
//            }
//            System.out.println();
//            System.out.println("------------------------------");

            if (whereIndex == -1) {    //如果没有选择约束条件
                if (selects[0].equals("*")) {    //所有列全部输出
                    System.out.println("------------------------------");
                    for (String s : vname) {
                        System.out.print(s + "\t");
                    }
                    System.out.println();
                    System.out.println("------------------------------");
                    Iterator<List<String>> it = table.getRow().iterator();
                    while (it.hasNext()) {
                        List<String> list = it.next();
                        for (String s : list) {
                            System.out.print(s + "\t");
                        }
                        System.out.println();
                    }
                } else {    //选择selects中出现的列
                    for (List<String> list : table.getRow()) {
                        for (int i = 0; i < vnameIndex.size(); ++i) {
                            System.out.print(list.get(vnameIndex.get(i)) + "\t");
                        }
                        System.out.println();
                    }
                }
            } else {    //where中有选择约束条件
                int connectIndex = 0;
                for (List<String> list : table.getRow()) {
                    boolean flag = true;
                    String operator = "";
                    List<Boolean> whereVerifyList = new ArrayList<>();
                    // 查找要修改的元组
                    for (int i = 0; i < whereMembersList.size(); ++i) {

                        int index = table.getVName().indexOf(whereMembersList.get(i));
                        if (index >= 0) {
                            if (whereList.get(i).contains("=")) {  //判断相等条件
                                operator = "=";
                                // 如果此元组的index项与这一判断条件不相等
                                if (!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)) {
                                    whereVerifyList.add(false);
                                } else {
                                    whereVerifyList.add(true);
                                }
                            }
                            if (whereList.get(i).contains("<")) {
                                operator = "<";
                                if (!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)) {
                                    whereVerifyList.add(false);
                                } else {
                                    whereVerifyList.add(true);
                                }
                            }
                            if (whereList.get(i).contains(">")) {
                                operator = ">";
                                if (!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)) {
                                    whereVerifyList.add(false);
                                } else {
                                    whereVerifyList.add(true);
                                }
                            }
                            if (whereList.get(i).contains("<=")) {
                                operator = "<=";
                                if (!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)) {
                                    whereVerifyList.add(false);
                                } else {
                                    whereVerifyList.add(true);
                                }
                            }
                            if (whereList.get(i).contains(">=")) {
                                operator = ">=";
                                if (!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)) {
                                    whereVerifyList.add(false);
                                } else {
                                    whereVerifyList.add(true);
                                }
                            }
                            if (whereList.get(i).contains("<>")) {
                                operator = "<>";
                                if (!Check.whereCheck(list.get(index), whereValuesList.get(i), operator)) {
                                    whereVerifyList.add(false);
                                } else {
                                    whereVerifyList.add(true);
                                }
                            }
                        } else {
                            System.out.println("ERROR: 查询条件错误。");
                        }
                    }
                    flag = Check.verifyCheck(whereConnectList, whereVerifyList);
                    if (flag) {
                        tempLists.add(list);
                        for (int i = 0; i < vnameIndex.size(); ++i) {
                            System.out.print(list.get(vnameIndex.get(i)) + "\t");
                        }
                        System.out.println();

                    }
                }
            }
        }
        return tempLists;
    }
}


