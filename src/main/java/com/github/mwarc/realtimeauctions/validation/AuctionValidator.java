package com.github.mwarc.realtimeauctions.validation;

import com.github.mwarc.realtimeauctions.model.Auction;
import com.github.mwarc.realtimeauctions.repository.AuctionRepository;

public class AuctionValidator implements Validator {

    private final AuctionRepository repository;

    public AuctionValidator(AuctionRepository repository) {
        this.repository = repository;
    }

    public boolean validate(Auction auction) {
        AuctionPriceValidator priceValidator = new AuctionPriceValidator(this.repository);
        AuctionEndingTimeValidator endingTimeValidator = new AuctionEndingTimeValidator(this.repository);

        return priceValidator.validate(auction) && endingTimeValidator.validate(auction);
    }
}
