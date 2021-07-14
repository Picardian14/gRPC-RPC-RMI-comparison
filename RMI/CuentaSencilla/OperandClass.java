import java.io.Serializable;
public class OperandClass implements Serializable {

    private int x;
    private int y;

    public OperandClass(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
