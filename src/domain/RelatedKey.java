package domain;

import function.Check;
import function.Read;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class RelatedKey implements Serializable {

    private String key;    //此表中键的名字
    private String relatedTable;    //关联表的名字
    private String relatedKey;    //关联表中键的名字

    public RelatedKey(String key, String relatedTable, String relatedKey){
        this.key = key;
        this.relatedTable = relatedTable;
        this.relatedKey = relatedKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRelatedTable() {
        return relatedTable;
    }

    public void setRelatedTable(String relatedTable) {
        this.relatedTable = relatedTable;
    }

    public String getRelatedKey() {
        return relatedKey;
    }

    public void setRelatedKey(String relatedKey) {
        this.relatedKey = relatedKey;
    }

    // 检查此键值是否对其他表中的外键有影响
    // 可以删除返回true，不能删除返回false
    public boolean check(String value, String operator){
        Table pTable = Read.readTable(relatedTable);
        int index = pTable.getVName().indexOf(relatedKey);
        for(List<String> list : pTable.getRow()){
            if(Check.whereCheck(list.get(index), value, operator)){
//            if(list.get(index).equalsIgnoreCase(value)){
                // （要删除的）这个值 存在于 某一个外键内
                System.out.println("ERROR: 在表 " + pTable.getName() + "中，存在外键值 " + list.get(index));
                return false;
            }
        }
        return true;
    }

}
