package co.abowes.silverbars;

public class LiveSummaryRow {

    private final OrderType orderType;
    private final int price;
    private final double totalWeight;

    public LiveSummaryRow(OrderType orderType, int price, double totalWeight) {
        this.orderType = orderType;
        this.price = price;
        this.totalWeight = totalWeight;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public int getPrice() {
        return price;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public Integer getSortOrder() {
        return orderType.equals(OrderType.SELL) ? price : -1 * price ;
    }

}
