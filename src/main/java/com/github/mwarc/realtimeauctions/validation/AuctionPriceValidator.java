package com.github.mwarc.realtimeauctions.validation;

import com.github.mwarc.realtimeauctions.exception.AuctionNotFoundException;
import com.github.mwarc.realtimeauctions.model.Auction;
import com.github.mwarc.realtimeauctions.repository.AuctionRepository;

import javax.inject.Inject;

public class AuctionPriceValidator implements Validator {

    private final AuctionRepository repository;

    @Inject
    public AuctionPriceValidator(AuctionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean validate(Auction auction) {
        Auction auctionDatabase = repository.getById(auction.getId())
            .orElseThrow(() -> new AuctionNotFoundException(auction.getId()));

        return auctionDatabase.getPrice().compareTo(auction.getPrice()) == -1;
    }
}
