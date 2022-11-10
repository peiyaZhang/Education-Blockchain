package com.ecnu.educationblockchain.service.impl;

import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.account.Account;
import com.citahub.cita.protocol.core.methods.response.AppBlockNumber;
import com.citahub.cita.protocol.core.methods.response.AppSendTransaction;
import com.citahub.cita.protocol.core.methods.response.Log;
import com.citahub.cita.protocol.core.methods.response.TransactionReceipt;
import com.citahub.cita.tx.response.PollingTransactionReceiptProcessor;
import com.ecnu.educationblockchain.config.CITAConfig;
import com.ecnu.educationblockchain.dao.UserRepository;
import com.ecnu.educationblockchain.model.User;
import com.ecnu.educationblockchain.service.UserService;
import com.ecnu.educationblockchain.util.CITAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * @program: EducationBlockchain
 * @description: Implemention of user service
 * @author: Pei-ya
 * @create: 2022-10-26 19:37
 **/
@Service
public class UserServiceImpl implements UserService {

    private static CITAj service;

    private static PollingTransactionReceiptProcessor txProcessor;

    private static CITAConfig config;

    private static long quota;

    private static BigInteger chainId;

    private static String value;

    private static int version;

    private static String contractAddr;

    private static Account account;

    static{
        config = new CITAConfig();
        service = config.service;
        txProcessor = config.txProcessor;
        quota = Long.parseLong(config.defaultQuotaDeployment);
        version = config.version;
        chainId = config.chainId;
        contractAddr = config.pointContractAddr;
        value = "0";
        account = new Account(config.adminPrivateKey,service);
    }

    @Autowired
    private UserRepository userRepository;

    @Override
    public BigInteger testRpc() throws Exception {
        AppBlockNumber appBlockNumber = service.appBlockNumber().send();
        return appBlockNumber.getBlockNumber();
    }

    @Override
    public BigInteger getPoint(String userAddress) throws Exception {
        Object object = account.callContract(contractAddr,"GetRes", CITAUtil.getNonce(),quota,version,
                chainId,value,userAddress);
        AppSendTransaction transaction = (AppSendTransaction) object;
        String txHash = transaction.getSendTransactionResult().getHash();
        TransactionReceipt receipt = txProcessor.waitForTransactionReceipt(txHash);
        List<Log> logs =  receipt.getLogs();
        String data = logs.get(0).getData();
        BigInteger point = CITAUtil.convertHexToBytes(data);
        return point;
    }

    @Override
    public TransactionReceipt addUser(User user) throws Exception {
        user = userRepository.saveAndFlush(user);
        String userAddress = user.getUserAddress();
        BigInteger id = BigInteger.valueOf(Long.parseLong(user.getId()));
        Object object = account.callContract(contractAddr,"AddUser",CITAUtil.getNonce(),quota,version,
                chainId,value,userAddress,id);
        AppSendTransaction transaction = (AppSendTransaction) object;
        String txHash = transaction.getSendTransactionResult().getHash();
        System.out.println("FirstHash："+txHash);
        TransactionReceipt receipt =
                txProcessor.waitForTransactionReceipt(txHash);
        System.out.println("SecondHash："+receipt.getTransactionHash());
        return receipt;
    }

    @Override
    public String setPoint(String userAddress, BigInteger point) throws Exception {
        Object object = account.callContract(contractAddr,"SetRes",CITAUtil.getNonce(),quota,version,
                chainId,value,userAddress,point);
        AppSendTransaction transaction = (AppSendTransaction) object;
        String txHash = transaction.getSendTransactionResult().getHash();
        TransactionReceipt receipt = txProcessor.waitForTransactionReceipt(txHash);
        return txHash;
    }

    @Override
    public String addPoint(String userAddress, BigInteger addPoint) throws Exception {
        Object object = account.callContract(contractAddr,"AddRes",CITAUtil.getNonce(),quota,version,
                chainId,value,userAddress,addPoint);
        AppSendTransaction transaction = (AppSendTransaction) object;
        String txHash = transaction.getSendTransactionResult().getHash();
        //TransactionReceipt receipt = txProcessor.waitForTransactionReceipt(txHash);
        return txHash;
    }

    @Override
    public String subPoint(String userAddress, BigInteger subPoint) throws Exception {
        Object object = account.callContract(contractAddr,"SubRes",CITAUtil.getNonce(),quota,version,
                chainId,value,userAddress,subPoint);
        AppSendTransaction transaction = (AppSendTransaction) object;
        String txHash = transaction.getSendTransactionResult().getHash();
        //TransactionReceipt receipt = txProcessor.waitForTransactionReceipt(txHash);
        return txHash;
    }
}
