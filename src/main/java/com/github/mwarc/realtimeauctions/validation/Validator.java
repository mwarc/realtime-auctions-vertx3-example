package com.github.mwarc.realtimeauctions.validation;


import com.github.mwarc.realtimeauctions.model.Auction;

public interface Validator {

    boolean validate(Auction value);
}
