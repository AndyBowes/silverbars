package co.abowes.silverbars;

import co.abowes.silverbars.exception.UnknownOrderException;

import java.util.*;
import java.util.stream.Collectors;

public class LiveOrderBoard {

    List<Order> orders = new ArrayList<>();

    protected int size() { return orders.size(); }

    public void registerOrder(Order order) {
        orders.add(order);
    }

    public void cancelOrder(Order order) {
        if (!orders.contains(order)){
            throw new UnknownOrderException(order);
        }
        orders.remove(order);
    }

    /**
     * Generate the Live Summary by preforming the followings steps:
     * 1. Group the orders by Order Type & Price
     * 2. Sort by the combination of Order Type & Price so that Buys are sorted in descending order & Sells are sorted in ascending price
     *
     * @return A List of {@link LiveSummaryRow} which contains the total weight ordered at a particular price.
     */
    public List<LiveSummaryRow> fetchLiveSummary() {

        final Map<LiveSummaryKey, DoubleSummaryStatistics> summaryMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getLiveSummaryKey,
                        Collectors.summarizingDouble(Order::getWeight)));

        final List<LiveSummaryRow> summaryInfo = summaryMap.entrySet().stream()
                .map(e -> new LiveSummaryRow(e.getKey().getOrderType(), e.getKey().getPrice(), e.getValue().getSum()))
                .sorted(Comparator.comparingInt(LiveSummaryRow::getSortOrder))
                .collect(Collectors.toList());

        return summaryInfo;
    }

}
