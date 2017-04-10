package co.abowes.silverbars;

import co.abowes.silverbars.exception.UnknownOrderException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static co.abowes.silverbars.OrderType.*;

public class LiveOrderBoardTest {

    private LiveOrderBoard orderBoard;

    @Before
    public void setup(){
        orderBoard = new LiveOrderBoard();
    }

    @Test
    public void testRegisterOrder(){
        assertThat(orderBoard.size(), equalTo(0));
        orderBoard.registerOrder(new Order("user1", 3.5, 303, BUY));
        assertThat(orderBoard.size(), equalTo(1));
    }

    @Test
    public void testRegisterMultipleOrders(){
        assertThat(orderBoard.size(), equalTo(0));
        orderBoard.registerOrder(new Order("user1", 3.5, 303, BUY));
        orderBoard.registerOrder(new Order("user2", 1.5, 310, SELL));
        orderBoard.registerOrder(new Order("user3", 2.0, 307, BUY));
        assertThat(orderBoard.size(), equalTo(3));
    }

    @Test
    public void testCancelOrder(){
        final Order order = new Order("user1", 3.5, 303, BUY);
        assertThat(orderBoard.size(), equalTo(0));
        orderBoard.registerOrder(order);
        assertThat(orderBoard.size(), equalTo(1));
        orderBoard.cancelOrder(order);
        assertThat(orderBoard.size(), equalTo(0));
    }

    @Test
    public void testCancelMultipleOrders(){
        final Order order1 = new Order("user1", 3.5, 303, BUY);
        final Order order2 = new Order("user2", 1.5, 310, SELL);
        final Order order3 = new Order("user3", 2.0, 307, BUY);
        assertThat(orderBoard.size(), equalTo(0));
        orderBoard.registerOrder(order1);
        orderBoard.registerOrder(order2);
        orderBoard.registerOrder(order3);
        assertThat(orderBoard.size(), equalTo(3));
        orderBoard.cancelOrder(order2);
        assertThat(orderBoard.size(), equalTo(2));
        orderBoard.cancelOrder(order1);
        assertThat(orderBoard.size(), equalTo(1));
        orderBoard.cancelOrder(order3);
        assertThat(orderBoard.size(), equalTo(0));
    }

    @Test(expected = UnknownOrderException.class)
    public void testCancelUnknownOrder(){
        final Order order = new Order("user1", 3.5, 303, BUY);
        final Order order2 = new Order("user2", 3.5, 303, BUY);
        orderBoard.registerOrder(order);
        orderBoard.cancelOrder(order2);
    }

    @Test
    public void testFetchLiveSummary_EmptyBoard(){
        Collection liveSummary = orderBoard.fetchLiveSummary();
        assertThat(liveSummary.size(), equalTo(0));
    }

    @Test
    public void testFetchLiveSummary_OneOrder(){
        final Order order = new Order("user1", 3.5, 303, BUY);
        orderBoard.registerOrder(order);
        List<LiveSummaryRow> liveSummary = orderBoard.fetchLiveSummary();
        assertThat(liveSummary.size(), equalTo(1));
        LiveSummaryRow firstRow = liveSummary.get(0);
        assertThat(firstRow.getOrderType(), equalTo(BUY));
        assertThat(firstRow.getPrice(), equalTo(303));
        assertThat(firstRow.getTotalWeight(), equalTo(3.5));
    }

    @Test
    public void testFetchLiveSummary_TwoOrdersSamePrice(){
        final Order order = new Order("user1", 3.5, 303, BUY);
        final Order order2 = new Order("user2", 2.0, 303, BUY);
        orderBoard.registerOrder(order);
        orderBoard.registerOrder(order2);
        List<LiveSummaryRow> liveSummary = orderBoard.fetchLiveSummary();
        // Check that Orders have been grouped and weights have been summed
        assertThat(liveSummary.size(), equalTo(1));
        LiveSummaryRow firstRow = liveSummary.get(0);
        assertThat(firstRow.getOrderType(), equalTo(BUY));
        assertThat(firstRow.getPrice(), equalTo(303));
        assertThat(firstRow.getTotalWeight(), equalTo(5.5));
    }

    @Test
    public void testFetchLiveSummary_SellOrdersSortedByAscendingPrice(){

        final Order order = new Order("user1", 3.5, 306, SELL);
        final Order order2 = new Order("user2", 1.2, 310, SELL);
        final Order order3 = new Order("user3", 1.5, 307, SELL);
        final Order order4 = new Order("user4", 2.0, 306, SELL);

        orderBoard.registerOrder(order);
        orderBoard.registerOrder(order2);
        orderBoard.registerOrder(order3);
        orderBoard.registerOrder(order4);

        List<LiveSummaryRow> liveSummary = orderBoard.fetchLiveSummary();
        // Check that Orders have been grouped and weights have been summed
        // Check that records appear in ascending price order
        assertThat(liveSummary.size(), equalTo(3));
        LiveSummaryRow summaryRow = liveSummary.get(0);
        assertThat(summaryRow.getOrderType(), equalTo(SELL));
        assertThat(summaryRow.getPrice(), equalTo(306));
        assertThat(summaryRow.getTotalWeight(), equalTo(5.5));

        summaryRow = liveSummary.get(1);
        assertThat(summaryRow.getOrderType(), equalTo(SELL));
        assertThat(summaryRow.getPrice(), equalTo(307));
        assertThat(summaryRow.getTotalWeight(), equalTo(1.5));

        summaryRow = liveSummary.get(2);
        assertThat(summaryRow.getOrderType(), equalTo(SELL));
        assertThat(summaryRow.getPrice(), equalTo(310));
        assertThat(summaryRow.getTotalWeight(), equalTo(1.2));
    }

    @Test
    public void testFetchLiveSummary_BuyOrdersSortedByDescendingPrice(){

        final Order order = new Order("user1", 3.5, 306, BUY);
        final Order order2 = new Order("user2", 1.2, 310, BUY);
        final Order order3 = new Order("user3", 1.5, 307, BUY);
        final Order order4 = new Order("user4", 2.0, 306, BUY);

        orderBoard.registerOrder(order);
        orderBoard.registerOrder(order2);
        orderBoard.registerOrder(order3);
        orderBoard.registerOrder(order4);

        List<LiveSummaryRow> liveSummary = orderBoard.fetchLiveSummary();
        // Check that Orders have been grouped and weights have been summed
        // Check that records appear in descending price order
        assertThat(liveSummary.size(), equalTo(3));
        LiveSummaryRow summaryRow = liveSummary.get(0);
        assertThat(summaryRow.getOrderType(), equalTo(BUY));
        assertThat(summaryRow.getPrice(), equalTo(310));
        assertThat(summaryRow.getTotalWeight(), equalTo(1.2));

        summaryRow = liveSummary.get(1);
        assertThat(summaryRow.getOrderType(), equalTo(BUY));
        assertThat(summaryRow.getPrice(), equalTo(307));
        assertThat(summaryRow.getTotalWeight(), equalTo(1.5));

        summaryRow = liveSummary.get(2);
        assertThat(summaryRow.getOrderType(), equalTo(BUY));
        assertThat(summaryRow.getPrice(), equalTo(306));
        assertThat(summaryRow.getTotalWeight(), equalTo(5.5));
    }

}
