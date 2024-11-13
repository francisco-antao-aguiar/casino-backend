package com.casino.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserInfo {
    @Id
    private String email;
    private String name;
    private String picture;
    private long points;
}
