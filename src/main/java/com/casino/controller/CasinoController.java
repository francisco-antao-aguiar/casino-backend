package com.casino.controller;

import com.casino.entity.UserInfo;
import com.casino.model.*;
import com.casino.service.CasinoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class CasinoController {

    @Autowired
    private CasinoService casinoService;

    @PostMapping("/login")
    public UserInfo login(@RequestBody String credentials) throws JsonProcessingException {
        return casinoService.login(credentials);
    }
    @PostMapping("/add-funds")
    public UserInfo addFunds(@RequestBody FundsInput fundsInput) throws Exception {
        return casinoService.addFunds(fundsInput);
    }
    @PostMapping("/withdraw-funds")
    public UserInfo withdrawFunds(@RequestBody FundsInput fundsInput) throws Exception {
        return casinoService.withdrawFunds(fundsInput);
    }

    @PostMapping("/roulette")
    public RouletteOutput roulette(@RequestBody RouletteInput rouletteInput) throws Exception {
        return casinoService.roulette(rouletteInput);
    }

    @PostMapping("/coin-flip")
    public CoinFlipOutput coinFlip(@RequestBody CoinFlipInput coinFlipInput) throws Exception {
        return casinoService.coinFlip(coinFlipInput);
    }
}
