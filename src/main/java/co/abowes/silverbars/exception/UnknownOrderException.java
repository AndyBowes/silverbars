package co.abowes.silverbars.exception;

import co.abowes.silverbars.Order;

public class UnknownOrderException extends RuntimeException {

    public UnknownOrderException(Order order) {
        super("Unknown Order: " + order.toString());
    }
}
