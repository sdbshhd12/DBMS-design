package domain;

import java.io.*;
import java.util.*;

/**
 * 表的实类
 * */
public class Table implements Serializable {

    private String name;   //表名
    final private List<List<String>> row;    //表内容
    private List<String> vname;    //成员名称
    private String primaryKey;    //主键
    private List<ForeignKey> foreignKeyList;    //外键链表
    private List<RelatedKey> relatedKeyList;    //与其相关的表（被外键连接）
    private List<Boolean> canBeNull;    //可否为空

    public Table() {
        name = null;
        primaryKey = null;
        vname = new LinkedList<>();
        row = new LinkedList<>();
        canBeNull = new LinkedList<>();
        foreignKeyList = new LinkedList<>();
        relatedKeyList = new LinkedList<>();
    }

    public List<String> getVname() {
        return vname;
    }

    public String  getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setVname(List<String> vname) {
        this.vname = vname;
    }

    public List<ForeignKey> getForeignKeyList() {
        return foreignKeyList;
    }

    public void setForeignKeyList(List<ForeignKey> foreignKeyList) {
        this.foreignKeyList = foreignKeyList;
    }

    public List<Boolean> getCanBeNull() {
        return canBeNull;
    }

    public void setCanBeNull(List<Boolean> canBeNull) {
        this.canBeNull = canBeNull;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setVName(List<String> nameList){
        vname = nameList;
    }
    public List<String> getVName(){
        return vname;
    }

    //-------------- 从文件中读取 -----------------
    public void readRow(String[] values){
        List<String> list = new LinkedList<>(Arrays.asList(values));
        row.add(list);
    }

    //---------------- 添加数据项 ------------------
    /*INSERT中只有一个括号，按顺序插入*/
    public void addRow(String[] values){
        List<String> list = new LinkedList<>();
        for (String value : values) {
            list.add(value.trim());
        }
        row.add(list);
    }
    /*INSERT中有两个括号，对应插入*/
    public void addRow(List<String> members, List<String> values){
        List<String> list = new LinkedList<>();
        for(String s : vname){
            int i = members.indexOf(s);
            list.add(values.get(i));
        }
        row.add(list);
    }

    public List<RelatedKey> getRelatedKeyList() {
        return relatedKeyList;
    }

    public void setRelatedKeyList(List<RelatedKey> relatedKeyList) {
        this.relatedKeyList = relatedKeyList;
    }

    public List<List<String>> getRow(){
        return row;
    }

    public void removeRow(int index){
        row.remove(index);
    }

    //---------------- 写入文件 --------------------
    public void writeInFile(){
        String path = "D:\\MyDatabases\\" + Database.nowDataBaseName + "\\Table\\" + name + ".dbf";
        File file = new File(path);
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(this);
            oos.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    // 添加相关表项
    public void addRelatedKeyList(String key, String relatedTable, String relatedKey){
        relatedKeyList.add(new RelatedKey(key, relatedTable, relatedKey));
    }
    
    // 添加外键
    public void addForeignKey(String relatedTable, String relatedKey, String key){
        foreignKeyList.add(new ForeignKey(relatedTable, relatedKey, key));
    }

    public void show(){
        System.out.println("------------------------------");
        for (String s : vname){
            System.out.print(s + "\t");
        }
        System.out.println();
        System.out.println("------------------------------");
        Iterator<List<String>> it = row.iterator();
        while(it.hasNext()){
            List<String> list = it.next();
            for(String s : list){
                System.out.print(s + "\t");
            }
            System.out.println();
        }
        if(row.size() != 0)
            System.out.println("------------------------------");

        if(primaryKey != null){
            System.out.println("主键值为：" + primaryKey);
        }else {
            System.out.println("主键值为：null");
        }
        System.out.println("---------");

        if(relatedKeyList!=null){
            System.out.println("与此表相关联的表有：");
            for(RelatedKey relatedKey : relatedKeyList){
                System.out.println("表中键值为：" + relatedKey.getKey());
                System.out.println("对应表为：" + relatedKey.getRelatedTable());
                System.out.println("对应表中键为：" + relatedKey.getRelatedKey());
                System.out.println("---");
            }

        }
        System.out.println("---------");
        if(foreignKeyList!=null){
            for(ForeignKey foreignKey : foreignKeyList){
                System.out.println("外键值为：" + foreignKey.getKey());
                System.out.println("关联的主表为：" + foreignKey.getRelatedTable());
                System.out.println("关联的主表中的值为：" + foreignKey.getRelatedKey());
                System.out.println("---");
            }
        }else {
            System.out.println("外键值为：null");
        }

    }

}
