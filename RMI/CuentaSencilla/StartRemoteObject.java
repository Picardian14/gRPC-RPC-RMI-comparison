import java.rmi.registry.Registry;
import java.rmi.Naming;

public class StartRemoteObject {
    public static void main(String args[]){
        try {
            CountServer robject = new CountServer();
            String rname = "//localhost:" + Registry.REGISTRY_PORT + "/remote";
            Naming.rebind(rname, robject);
        }
        catch (Exception e) {
            System.out.println("Fallo Naming.rebind");
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
