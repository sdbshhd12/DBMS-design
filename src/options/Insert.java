package options;

import domain.ForeignKey;
import domain.Table;
import function.Check;
import function.Read;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Insert {

    static public void insert(String command){

        /*
        * INSERT
        * INTO Persons (No, Name, Gender) VALUES (100, Alex, Male)
        */

        String[] commands = command.split(" ", 3);
        if(!commands[0].equalsIgnoreCase("INTO")){
            System.out.println("ERROR: 语句错误");
            return;
        }
        String tableName = commands[1];    //寻找表名
        Table table = Read.readTable(tableName);
        table.setName(tableName);



        //找到两个括号中的内容，加入到一个长度为1或为2的列表中
        Pattern pattern = Pattern.compile("\\(.*?\\)");
        Matcher matcher = pattern.matcher(command);
        List<String> list = new LinkedList<>();
        while(matcher.find()){
            list.add(matcher.group());
        }

        if(list.size() == 2){    //如果有两个括号， 需要对象和值对应
            String member = list.get(0).substring(1,list.get(0).length()-1).trim();
            String value = list.get(1).substring(1,list.get(1).length()-1).trim();

            //获取每个括号中的每一项
            String[] members = member.split(",");
            String[] values = value.split(",");
            if(members.length != values.length){
                System.out.println("数据输入有误，请检查");
                return;
            }
            List<String> membersList = new LinkedList<>();
            List<String> valuesList = new LinkedList<>();
            for(int i=0; i<members.length; ++i){
                membersList.add(members[i].trim());
            }
            for(int i=0; i<members.length; ++i){
                valuesList.add(values[i].trim());
            }

            //检查主键是否唯一
            if(table.getPrimaryKey()!=null){
                int insertPrimaryIndex = membersList.indexOf(table.getPrimaryKey());
                int tablePrimaryIndex = table.getVName().indexOf(table.getPrimaryKey());
                for(int i=0; i<table.getRow().size(); ++i){
                    if(table.getRow().get(i).get(tablePrimaryIndex).equalsIgnoreCase(valuesList.get(insertPrimaryIndex))){
                        System.out.println("ERROR: 主键重复。");
                        return;
                    }
                }
            }

            // 检查外键
            if(!Check.foreignCheck(table, membersList, valuesList, "=")){
                return;
            }
            table.addRow(membersList, valuesList);
            table.writeInFile();
        }

        else if(list.size() == 1){    //如果有一个括号，则按表中顺序
            String value = list.get(0).substring(1,list.get(0).length()-1).trim();
            String[] values = value.split(",");

            List<String> valuesList = Arrays.asList(values);
            List<String> membersList = table.getVName();

            //检查主键是否唯一
            if(table.getPrimaryKey()!=null){
                int insertPrimaryIndex = membersList.indexOf(table.getPrimaryKey());
                int tablePrimaryIndex = table.getVName().indexOf(table.getPrimaryKey());
                for(int i=0; i<table.getRow().size(); ++i){
                    if(table.getRow().get(i).get(tablePrimaryIndex).equalsIgnoreCase(valuesList.get(insertPrimaryIndex))){
                        System.out.println("ERROR: 主键重复。");
                        return;
                    }
                }
            }

            // 检查外键
            if(! Check.foreignCheck(table, membersList, valuesList, "=")){
                return;
            }

            table.addRow(values);
            table.writeInFile();
        }


    }

}
