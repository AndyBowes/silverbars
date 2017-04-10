package co.abowes.silverbars;

public class Order {

    private final String userId;
    private final double weight;
    private final int price;
    private final OrderType orderType;

    public Order(String userId, double weight, int price, OrderType orderType) {
        this.userId = userId;
        this.weight = weight;
        this.price = price;
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public double getWeight() {
        return weight;
    }

    public int getPrice() {
        return price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public LiveSummaryKey getLiveSummaryKey(){
        return new LiveSummaryKey(orderType,price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (Double.compare(order.weight, weight) != 0) return false;
        if (price != order.price) return false;
        if (!userId.equals(order.userId)) return false;
        return orderType.equals(order.orderType);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = userId.hashCode();
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + price;
        result = 31 * result + orderType.name().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "userId='" + userId + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", orderType='" + orderType.name() + '\'' +
                '}';
    }

}
