package domain;

import function.Check;
import function.Read;

import java.io.Serializable;
import java.util.List;

public class ForeignKey implements Serializable {

    private final String relatedTable;    //外键对应的表
    private final String key;    //外键的名称
    private final String relatedKey;    //对应的名称


    public String getKey() {
        return key;
    }

    public String getRelatedTable() {
        return relatedTable;
    }

    public String getRelatedKey() {
        return relatedKey;
    }

    public ForeignKey(String relatedTable, String relatedKey, String key){
        this.relatedTable = relatedTable;
        this.relatedKey = relatedKey;
        this.key = key;
    }


    /*
    * when(update / insert)
    *   if(cols(value) == key)
    * */

    // 检查新的外键值在对应表中是否存在
    // ture为可执行，false为不可执行
    public boolean check(String value, String operator){
        boolean isExist = false;
        Table pTable = Read.readTable(relatedTable);    //对应表
        int index = pTable.getVName().indexOf(relatedKey);    //对应表中的的键值
        for(List<String> list : pTable.getRow()){
            if(Check.whereCheck(list.get(index), value, operator)){
                isExist = true;
                break;
            }
        }
        return isExist;
    }


}
