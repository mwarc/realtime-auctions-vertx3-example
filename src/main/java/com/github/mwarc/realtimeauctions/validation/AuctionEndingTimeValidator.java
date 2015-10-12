package com.github.mwarc.realtimeauctions.validation;

import com.github.mwarc.realtimeauctions.exception.AuctionNotFoundException;
import com.github.mwarc.realtimeauctions.model.Auction;
import com.github.mwarc.realtimeauctions.repository.AuctionRepository;

import javax.inject.Inject;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AuctionEndingTimeValidator implements Validator {

    private final AuctionRepository repository;

    @Inject
    public AuctionEndingTimeValidator(AuctionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean validate(Auction auction) {
        Auction auctionDatabase = repository.getById(auction.getId())
            .orElseThrow(() -> new AuctionNotFoundException(auction.getId()));

        return auctionDatabase.getEndingTime().isAfter(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
