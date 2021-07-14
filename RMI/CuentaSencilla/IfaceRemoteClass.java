import java.rmi.Remote;
import java.rmi.RemoteException;



public interface IfaceRemoteClass extends Remote {
    public int add(OperandClass operands) throws RemoteException;
    public int sub(OperandClass operands) throws RemoteException;
}