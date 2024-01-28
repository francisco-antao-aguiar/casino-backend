package com.casino.model;

import lombok.Data;

import java.util.List;

@Data
public class RouletteInput {
    private String credentials;
    private List<Integer> selectedNumbers;
    private Long pointsBet;
}
