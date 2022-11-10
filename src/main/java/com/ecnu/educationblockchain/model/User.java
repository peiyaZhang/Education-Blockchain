package com.ecnu.educationblockchain.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
@Getter
@Setter
@ToString
public class User {
    @Id
    private String Id;
    @Column
    private String userNumber;
    @Column
    private String password;
    @Column(unique = true)
    private String userAddress;
    @Column(unique = true)
    private String privateKey;
    @Column(unique = true)
    private String publicKey;

    public User(String id,String userNumber,String password,String userAddress, String privateKey,String publicKey){
        this.Id = id;
        this.userNumber = userNumber;
        this.password = password;
        this.userAddress = userAddress;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public User(){
    }

}

