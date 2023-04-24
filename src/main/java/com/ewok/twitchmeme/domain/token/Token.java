package com.ewok.twitchmeme.domain.token;

import com.nimbusds.oauth2.sdk.token.Tokens;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String token;

    public void update(String newToken) {
        this.token = newToken;
    }
}
