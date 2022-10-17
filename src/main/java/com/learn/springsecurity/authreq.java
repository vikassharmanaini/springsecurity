package com.learn.springsecurity;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class authreq {
    private String Username;
    private String Password;
}
