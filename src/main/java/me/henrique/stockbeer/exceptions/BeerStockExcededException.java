package me.henrique.stockbeer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExcededException extends Exception {

    public BeerStockExcededException(Long id, int quantityToIncrement) {
        super(String.format("Beers with ID %s to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }
}
