package com.ecnu.educationblockchain.util;

import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.account.Account;
import com.citahub.cita.protocol.account.CompiledContract;
import com.citahub.cita.protocol.core.methods.response.AppSendTransaction;
import com.citahub.cita.protocol.core.methods.response.TransactionReceipt;
import com.citahub.cita.tx.response.PollingTransactionReceiptProcessor;
import com.ecnu.educationblockchain.config.CITAConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @program: EducationBlockchain
 * @description: operation to contracts
 * @author: Pei-ya
 * @create: 2022-10-20 20:36
 **/
@Slf4j
public class ContractUtil {
    private static CITAConfig config;
    private static CITAj service;
    private static Account adminAccount;
    private static PollingTransactionReceiptProcessor txProcessor;

    static{
        config = new CITAConfig();
        service = config.service;
        txProcessor = config.txProcessor;
        adminAccount  = new Account(config.adminPrivateKey,service);
    }

    public static String deployContract(String path)throws Exception{
        AppSendTransaction ethSendTransaction = adminAccount.deploy(
                new File(path),CITAUtil.getNonce(),Long.parseLong(config.defaultQuotaDeployment),config.version,config.chainId,"0"
        );
        TransactionReceipt receipt =
                txProcessor.waitForTransactionReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if(receipt.getErrorMessage() !=null){
            System.out.println("deploy contract failed because of "+receipt.getErrorMessage());
        }
        String contractAddress = receipt.getContractAddress();
        System.out.println("deploy contract successfully and the contract address is "+receipt.getContractAddress());
        return contractAddress;
    }

    public static void storeAbiToBlockchain(String contractAddress,String path) throws Exception {
        CompiledContract compiledContract = new CompiledContract(new File(path));
        System.out.println("get abi from " + path + " : " + compiledContract.getAbi());
        AppSendTransaction ethSendTransaction =
                (AppSendTransaction) adminAccount.uploadAbi(
                        contractAddress, compiledContract.getAbi(),
                        CITAUtil.getNonce(), Long.parseLong(config.defaultQuotaDeployment), config.version,
                        config.chainId, "0");
        TransactionReceipt receipt =
                txProcessor.waitForTransactionReceipt(ethSendTransaction.getSendTransactionResult().getHash());
        if (receipt.getErrorMessage()!=null){
            System.out.println("call upload abi method failed because of "
                    +receipt.getErrorMessage());
        }
        else{
            System.out.println("call upload abi method successfully. Receipt: "+receipt);
        }
        System.out.println("call upload abi method successfully and receipt is "
                + receipt.getTransactionHash());
    }

    public static void updateConfig(Map<String,String> map,String info){
        Properties properties = new Properties();
        Properties devProp = new Properties();
        try{
            properties.load(CITAConfig.configPath.openStream());
            devProp.load(new FileInputStream("src/main/resources/cita_config.properties"));
            FileOutputStream fos = new FileOutputStream(CITAConfig.configPath.getFile());
            FileOutputStream devFos = new FileOutputStream("src/main/resources/cita_config.properties");

            //通过set方法写入属性
            for(Map.Entry<String,String> entry : map.entrySet()){
                properties.setProperty(entry.getKey(),entry.getValue());
                devProp.setProperty(entry.getKey(),entry.getValue());
            }
            //调用properties的存储方法
            properties.store(fos,info);
            devProp.store(devFos,info);
            log.info(info + "is completed");
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

