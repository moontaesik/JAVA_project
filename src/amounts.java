import java.sql.Timestamp;

public class amounts {
    private Integer phone;
    private String second_choice;
    private String add_choice;
    private Integer amount;
    private Timestamp times;

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public String getSecond_choice() {
        return second_choice;
    }

    public void setSecond_choice(String second_choice) {
        this.second_choice = second_choice;
    }

    public String getAdd_choice() {
        return add_choice;
    }

    public void setAdd_choice(String add_choice) {
        this.add_choice = add_choice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Timestamp getTimes() {
        return times;
    }

    public void setTimes(Timestamp times) {
        this.times = times;
    }
}
