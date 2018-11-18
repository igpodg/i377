package test.model;

public class OrderReport {
    private Integer count;
    private Integer averageOrderAmount;
    private Integer turnoverWithoutVAT;
    private Integer turnoverVAT;
    private Integer turnoverWithVAT;

    public OrderReport() {
        this.count = 0;
        this.averageOrderAmount = 0;
        this.turnoverWithoutVAT = 0;
        this.turnoverVAT = 0;
        this.turnoverWithVAT = 0;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getAverageOrderAmount() {
        return this.averageOrderAmount;
    }

    public void setAverageOrderAmount(Integer averageOrderAmount) {
        this.averageOrderAmount = averageOrderAmount;
    }

    public Integer getTurnoverWithoutVAT() {
        return this.turnoverWithoutVAT;
    }

    public void setTurnoverWithoutVAT(Integer turnoverWithoutVAT) {
        this.turnoverWithoutVAT = turnoverWithoutVAT;
    }

    public Integer getTurnoverVAT() {
        return this.turnoverVAT;
    }

    public void setTurnoverVAT(Integer turnoverVAT) {
        this.turnoverVAT = turnoverVAT;
    }

    public Integer getTurnoverWithVAT() {
        return this.turnoverWithVAT;
    }

    public void setTurnoverWithVAT(Integer turnoverWithVAT) {
        this.turnoverWithVAT = turnoverWithVAT;
    }

    @Override
    public String toString() {
        return "OrderReport{" +
                "count=" + this.count +
                ", averageOrderAmount=" + this.averageOrderAmount +
                ", turnoverWithoutVAT=" + this.turnoverWithoutVAT +
                ", turnoverVAT=" + this.turnoverVAT +
                ", turnoverWithVAT=" + this.turnoverWithVAT +
                '}';
    }
}
