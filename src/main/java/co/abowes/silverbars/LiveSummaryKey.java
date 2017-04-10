package co.abowes.silverbars;

public class LiveSummaryKey {
    private final OrderType orderType;
    private final int price;

    public LiveSummaryKey(OrderType orderType, int price) {
        this.orderType = orderType;
        this.price = price;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LiveSummaryKey that = (LiveSummaryKey) o;

        if (price != that.price) return false;
        return orderType == that.orderType;
    }

    @Override
    public int hashCode() {
        int result = orderType.hashCode();
        result = 31 * result + price;
        return result;
    }
}
