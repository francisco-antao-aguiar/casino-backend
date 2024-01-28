package com.casino.service;

import com.casino.entity.UserInfo;
import com.casino.model.*;
import com.casino.repository.UserInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;
import java.util.Random;

@Service
public class CasinoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    private Random random = new Random();

    public UserInfo login(String credentials) throws JsonProcessingException {
        UserInfo userInfo = tokenCredentialsDecoder(credentials);
        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(userInfo.getEmail());
        if(optionalUserInfo.isPresent())
            return optionalUserInfo.get();

        return userInfoRepository.save(userInfo);
    }

    public UserInfo addFunds(FundsInput fundsInput) throws Exception {
        UserInfo userInfo = tokenCredentialsDecoder(fundsInput.getCredentials());

        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(userInfo.getEmail());
        if(optionalUserInfo.isEmpty())
            throw new Exception("user not found");

        userInfo = optionalUserInfo.get();
        userInfo.setPoints(userInfo.getPoints() + fundsInput.getPoints());
        userInfoRepository.save(userInfo);
        return userInfo;
    }

    public UserInfo withdrawFunds(FundsInput fundsInput) throws Exception {
        UserInfo userInfo = getUserInfo(fundsInput.getCredentials(), fundsInput.getPoints());
        userInfo.setPoints(userInfo.getPoints() - fundsInput.getPoints());
        userInfoRepository.save(userInfo);
        return userInfo;
    }

    public RouletteOutput roulette(RouletteInput rouletteInput) throws Exception {
        UserInfo userInfo = getUserInfo(rouletteInput.getCredentials(), rouletteInput.getPointsBet());

        int numberRolled = random.nextInt(15);
        if (rouletteInput.getSelectedNumbers().contains(numberRolled)) {
            userInfo.setPoints(userInfo.getPoints() - rouletteInput.getPointsBet()
                    + (rouletteInput.getPointsBet() * 14 / rouletteInput.getSelectedNumbers().size()));
        }
        else {
            userInfo.setPoints(userInfo.getPoints() - rouletteInput.getPointsBet());
        }
        userInfoRepository.save(userInfo);
        return RouletteOutput.builder()
                .userInfo(userInfo)
                .numberRolled(numberRolled)
                .build();
    }

    public CoinFlipOutput coinFlip(CoinFlipInput coinFlipInput) throws Exception {
        UserInfo userInfo = getUserInfo(coinFlipInput.getCredentials(), coinFlipInput.getPointsBet());

        int numberRolled = random.nextInt(2);
        if (numberRolled == 1) {
            userInfo.setPoints(userInfo.getPoints() + coinFlipInput.getPointsBet());
        }
        else {
            userInfo.setPoints(userInfo.getPoints() - coinFlipInput.getPointsBet());
        }
        userInfoRepository.save(userInfo);
        return CoinFlipOutput.builder()
                .userInfo(userInfo)
                .numberRolled(numberRolled)
                .build();
    }

    private UserInfo getUserInfo(String coinFlipInput, Long pointsBet) throws Exception {
        UserInfo userInfo = tokenCredentialsDecoder(coinFlipInput);
        userInfo = userInfoRepository.findById(userInfo.getEmail()).orElseThrow();
        if(pointsBet > userInfo.getPoints()){
            throw new Exception("not enough points");
        }
        return userInfo;
    }

    private UserInfo tokenCredentialsDecoder(String token) throws JsonProcessingException {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        ObjectMapper objectMapper = new ObjectMapper();
        TokenInfo tokenInfo = objectMapper.readValue(payload, TokenInfo.class);

        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(tokenInfo, userInfo);

        return userInfo;
    }
}
