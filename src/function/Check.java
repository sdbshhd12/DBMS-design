package function;

import domain.ForeignKey;
import domain.RelatedKey;
import domain.Table;
import domain.User;

import java.util.LinkedList;
import java.util.List;

public class Check {


    /**
     *
     * @param tables 所选所有表
     * @param members 所选所有属性
     * @return 检查通过返回true，有错误返回false
     */
    static public boolean tableNameCheck(String[] tables, String[] members){
        List<String> vNameList = new LinkedList<>();
        String errorTable = "";
        try{
            for(String tableName : tables){
                Table table = Read.readTable(tableName.trim());
                vNameList.addAll(table.getVname());
            }
        }catch (Exception e){
            System.out.println("ERROR: 所选择的表有缺失项。");
            return false;
        }
        for(String member : members){
            if(!vNameList.contains(member.trim())){
                System.out.println("ERROR: 所选成员 '" + member + "' 不存在。");
            }
        }

        return true;
    }



    /**
     *
     * @param table
     * @param membersList
     * @param valuesList
     * @return
     *
     * 外键检测，供Insert和Update使用
     */
    static public boolean foreignCheck(Table table, List<String> membersList, List<String> valuesList, String operator){
        if(table.getForeignKeyList() != null){
            for(ForeignKey foreignKey : table.getForeignKeyList()){
                for(int i=0; i< membersList.size(); ++i){
                    if(membersList.get(i).equalsIgnoreCase(foreignKey.getKey())){
                        if(!foreignKey.check(valuesList.get(i).trim(), operator)){    //如果结果为false，即不存在对应键
                            System.out.println("ERROR: 参照性约束不匹配。");
                            System.out.println("发生错误的键为 " + membersList.get(i) + "，值为 " + valuesList.get(i) + "，参照表为" + foreignKey.getRelatedTable());

                            Table pTable = Read.readTable(foreignKey.getRelatedTable());
                            int index = pTable.getVName().indexOf(membersList.get(i));
                            for(List<String> list : pTable.getRow()){
                                System.out.println(list.get(index));
                            }
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /* 关联检测，供Delete和Update使用 */
    static public boolean relationCheck(Table table, List<String> membersList, List<String> valuesList, String operator){
        if(table.getRelatedKeyList() != null){
            for(RelatedKey relatedKey : table.getRelatedKeyList()){
                for(int i=0; i < membersList.size(); ++i){
                    if(!relatedKey.check(valuesList.get(i), operator)){
                        System.out.println("ERROR: 参照性约束不匹配。");
                        System.out.println("发生错误的键为 " + membersList.get(i) + "，值为 " + valuesList.get(i) + "，参照表为" + relatedKey.getRelatedTable());

                        Table pTable = Read.readTable(relatedKey.getRelatedTable());
                        int index = pTable.getVName().indexOf(membersList.get(i));
                        for(List<String> list : pTable.getRow()){
                            System.out.println(list.get(index));
                        }
                        return false;
                    }
                }
            }
        }

        return true;
    }

    static public boolean whereCheck(String tableValue, String newValue, String operator){
        if(operator.equals("=")){
            return tableValue.equalsIgnoreCase(newValue);
        }
        else if(operator.equals(">")){
            return Integer.parseInt(tableValue) > Integer.parseInt(newValue);
        }
        else if(operator.equals("<")){
            return Integer.parseInt(tableValue) < Integer.parseInt(newValue);
        }
        else if(operator.equals(">=")){
            return Integer.parseInt(tableValue) >= Integer.parseInt(newValue);
        }
        else if(operator.equals("<=")){
            return Integer.parseInt(tableValue) > Integer.parseInt(newValue);
        }
        else if(operator.equals("<>")){
            return Integer.parseInt(tableValue) != Integer.parseInt(newValue);
        }
        return false;
    }

    static public boolean verifyCheck(List<String> connectList, List<Boolean> verifyList){
        boolean flag = verifyList.get(0);
        for(int i=0; i<connectList.size(); ++i){
            if(connectList.get(i).equalsIgnoreCase("AND")){
                flag = flag && verifyList.get(i+1);
            }else if(connectList.get(i).equalsIgnoreCase("OR")){
                flag = flag || verifyList.get(i+1);
            }
        }

        return flag;
    }

    static public boolean userCheck(String hostname, String password){
        User user = Read.readUser(hostname);
        if(user.getPassword().equals(password)){
            return true;
        }
        return false;
    }


}
