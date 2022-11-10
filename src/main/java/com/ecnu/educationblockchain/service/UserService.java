package com.ecnu.educationblockchain.service;

import com.citahub.cita.protocol.core.methods.response.TransactionReceipt;
import com.ecnu.educationblockchain.model.User;

import java.math.BigInteger;

public interface UserService {
    //用于测试是否能够连接到链
    BigInteger testRpc() throws Exception;
    BigInteger getPoint(String userAddress) throws Exception;

    TransactionReceipt addUser(User user) throws Exception;
    String setPoint(String userAddress,BigInteger point) throws Exception;

    String addPoint(String userAddress,BigInteger addPoint) throws Exception;

    String subPoint(String userAddress,BigInteger subPoint) throws Exception;
}
