package com.github.mwarc.realtimeauctions.validation;

import com.github.mwarc.realtimeauctions.model.Auction;
import com.github.mwarc.realtimeauctions.repository.AuctionRepository;

import javax.inject.Inject;

public class AuctionValidator implements Validator {

    private final AuctionPriceValidator priceValidator;
    private final AuctionEndingTimeValidator endingTimeValidator;

    @Inject
    public AuctionValidator(
        AuctionPriceValidator priceValidator,
        AuctionEndingTimeValidator endingTimeValidator
    ) {
        this.priceValidator = priceValidator;
        this.endingTimeValidator = endingTimeValidator;
    }

    public boolean validate(Auction auction) {
        return priceValidator.validate(auction) && endingTimeValidator.validate(auction);
    }
}
