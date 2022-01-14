import domain.*;
import function.Check;
import function.Read;
import options.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to the newborn DBMS!");

        Database.nowDataBaseName = null;
        String hostName = "root";
        String password = "123";
        if(!Check.userCheck(hostName, password)){
            System.out.println("ERROR: 用户名错误");
            return;
        }
        System.out.println("登陆成功。");
        String str = "";
        while (true){
            System.out.print("root>");

            User user = Read.readUser(hostName);


            str = Read.readCommand();
            String[] command = str.split(" ", 2);

            //------------------- 命令 ----------------------
            if(command[0].equalsIgnoreCase("USE")) {
                Use.use(command[1]);
            }
            if(command[0].equalsIgnoreCase("CREATE")) {
                Create.create(command[1]);
            }
            if(command[0].equalsIgnoreCase("SELECT")) {
                Select.select(command[1]);
            }
            if(command[0].equalsIgnoreCase("INSERT")){
                Insert.insert(command[1]);
            }
            if(command[0].equalsIgnoreCase("UPDATE")){
                Update.update(command[1]);
            }
            if(command[0].equalsIgnoreCase("DELETE")){
                Delete.delete(command[1]);
            }
            if(command[0].equalsIgnoreCase("SHOW")) {
                Show.show(command[1]);
            }
            if(command[0].equalsIgnoreCase("GRANT")){
                RootOptions.grant(command[1]);
            }
            if(command[0].equalsIgnoreCase("REVOKE")){
                RootOptions.revoke(command[1]);
            }
            if(command[0].equalsIgnoreCase("HELP")){
                Help.help(command[1]);
            }
            if(command[0].equalsIgnoreCase("EXIT")) {
                break;
            }
        }
        System.out.println("See you again~");
    }

}
