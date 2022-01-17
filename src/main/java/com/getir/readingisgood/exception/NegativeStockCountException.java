package com.getir.readingisgood.exception;

public class NegativeStockCountException extends RuntimeException {

    public NegativeStockCountException() {
        super("Stock count cannot be lesser than 0!");
    }

}
