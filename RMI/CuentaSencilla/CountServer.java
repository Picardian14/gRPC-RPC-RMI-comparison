import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class CountServer extends UnicastRemoteObject implements IfaceRemoteClass{

    protected CountServer() throws RemoteException{
        super();
    }
    public int add(OperandClass operands) {
        int x = operands.getX();
        int y = operands.getY();

        return x+y;
    }

    public int sub(OperandClass operands){
        int x = operands.getX();
        int y = operands.getY();
        return x-y;
    }
    
}
