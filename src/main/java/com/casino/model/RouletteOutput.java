package com.casino.model;

import com.casino.entity.UserInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RouletteOutput {
    UserInfo userInfo;
    Integer numberRolled;
}
