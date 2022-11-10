package com.ecnu.educationblockchain.config;


import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.Properties;
import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.http.HttpService;
import com.citahub.cita.tx.response.PollingTransactionReceiptProcessor;
import com.ecnu.educationblockchain.util.CITAUtil;

/**
 * @program: EducationBlockchain
 * @description: Configuration class of CITA
 * @author: Pei-ya
 * @create: 2022-10-12 20:02
 **/

public class CITAConfig {
    public static final URL configPath =  CITAConfig.class.getClassLoader().getResource("cita_config.properties");
    private static final String CHAIN_ID = "ChainId";
    private static final String VERSION = "Version";
    private static final String TEST_NET_ADDR = "TestNetIpAddr";
    private static final String POINT_SOLIDITY = "PointSolidity";
    private static final String DEFAULT_QUOTA_Transfer = "QuotaForTransfer";
    private static final String DEFAULT_QUOTA_Deployment = "QuotaForDeployment";
    private static final String CRYPTO_TYPE = "CryptoType";
    private static final String ADMIN_PRIVATE_KEY = "AdminPrivateKey";
    private static final String ADMIN_ADDRESS = "AdminAddress";
    private static final String POINT_CONTRACT_ADDR = "PointContractAddr";
    private Properties props;
    public BigInteger chainId;
    public int version;
    public String ipAddr;
    public String pointSolidity;
    public String defaultQuotaTransfer;
    public String defaultQuotaDeployment;
    public CITAj service;
    public PollingTransactionReceiptProcessor txProcessor;
    public String cryptoTx;
    public String adminPrivateKey;
    public String adminAddress;
    public String pointContractAddr;

    public CITAConfig(){
        props = load();
        loadPropsToAttr(props);
    }

    public CITAConfig(String configFilePath){
        props = load(configFilePath);
        loadPropsToAttr(props);
    }

    public void buildService(boolean debugMode){
        HttpService.setDebug(debugMode);
        this.service = CITAj.build(new HttpService(this.ipAddr));
    }

    public static Properties load(String path) {
        Properties props = new Properties();
        try{
            props.load(new FileInputStream(path));
        }
        catch(Exception e){
            System.out.println("Failed to read config at path" + path);
            e.printStackTrace();
        }
        return props;
    }

    public static Properties load(){
        Properties props = new Properties();
        try{
            props.load(configPath.openStream());
        }
        catch (Exception e){
            System.out.println("Failed to read config file.Error: "+e);
            e.printStackTrace();
        }
        return  props;
    }

    private void loadPropsToAttr(Properties props){
        ipAddr = props.getProperty(TEST_NET_ADDR);
        service = CITAj.build(new HttpService(this.ipAddr));
        txProcessor = new PollingTransactionReceiptProcessor(service,1000,20); //一秒轮询一次，获取交易回执
        chainId = CITAUtil.getChainId(service);
        version = CITAUtil.getVersion(service);
        pointSolidity = props.getProperty(POINT_SOLIDITY);
        defaultQuotaDeployment = props.getProperty(DEFAULT_QUOTA_Deployment);
        defaultQuotaTransfer = props.getProperty(DEFAULT_QUOTA_Transfer);
        cryptoTx = props.getProperty(CRYPTO_TYPE);
        adminPrivateKey = props.getProperty(ADMIN_PRIVATE_KEY);
        adminAddress = props.getProperty(ADMIN_ADDRESS);
        pointContractAddr = props.getProperty(POINT_CONTRACT_ADDR);
    }
}
