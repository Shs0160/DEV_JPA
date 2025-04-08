package io.shs0160.springjpa.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {


    private int memberId;
    public String username;
    public String password;

}
