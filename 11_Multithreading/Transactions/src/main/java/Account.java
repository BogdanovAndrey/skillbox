import lombok.Data;

@Data
public class Account {
    private long money;
    private String accNumber;
    private boolean status;

    public void addMoney(long modMoney) {
        money += modMoney;
    }
    public void decMoney (long modMoney){
        money -= modMoney;
    }
    public boolean isBlocked(){
        return status;
    }
    public void block(){
        status = true;
    }
    public void unblock(){
        status = false;
    }
}
