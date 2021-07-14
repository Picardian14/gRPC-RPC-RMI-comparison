import java.rmi.Naming;
import java.rmi.registry.Registry;


public class CountClient {
    public static void main(String[] args)    {
        if (args.length != 3) {
            System.out.println("1 argument needed: (remote) hostname");
            System.exit(1);
        }
    
    try {
        String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
        IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);
        OperandClass operands = new OperandClass(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        int result = remote.add(operands);
        System.out.println("Servidor devuelve: "+result);


    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}