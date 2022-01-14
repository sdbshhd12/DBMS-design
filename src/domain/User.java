package domain;


import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {

    private String hostname;
    private String password;
    private List<Root> rootList;

    public User(){

    }

    public User(String hostname, String password) {
        this.hostname = hostname;
        this.password = password;
        rootList = new LinkedList<>();
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Root> getRootList() {
        return rootList;
    }

    public void setRootList(List<Root> rootList) {
        this.rootList = rootList;
    }

    public void addRoot(String tableName, String rootName){
        for(Root root : rootList){
            if(root.getTableName().equalsIgnoreCase(tableName)){
                if(rootName.equalsIgnoreCase("select")){
                    root.setSelect(true);
                }
                else if(rootName.equalsIgnoreCase("delete")){
                    root.setDelete(true);
                }
                else if(rootName.equalsIgnoreCase("update")){
                    root.setUpdate(true);
                }
                else if(rootName.equalsIgnoreCase("insert")){
                    root.setInsert(true);
                }
            }
        }
    }

    public void removeRoot(String tableName, String rootName){
        for(Root root : rootList){
            if(root.getTableName().equalsIgnoreCase(tableName)){
                if(rootName.equalsIgnoreCase("select")){
                    root.setSelect(false);
                }
                else if(rootName.equalsIgnoreCase("delete")){
                    root.setDelete(false);
                }
                else if(rootName.equalsIgnoreCase("update")){
                    root.setUpdate(false);
                }
                else if(rootName.equalsIgnoreCase("insert")){
                    root.setInsert(false);
                }
            }
        }
    }

    public void writeInFile(){
        String path = "D:\\MyDatabases\\User\\" + hostname  + ".dbuser";
        File file = new File(path);
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(this);
            oos.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }


}
