package com.ecnu.educationblockchain.util;

import com.citahub.cita.protobuf.ConvertStrByte;
import com.citahub.cita.protocol.CITAj;
import com.citahub.cita.protocol.core.DefaultBlockParameterName;
import com.citahub.cita.protocol.core.methods.response.AppMetaData;
import com.citahub.cita.utils.Numeric;
import java.math.BigInteger;
import java.util.Random;

/**
 * @program: EducationBlockchain
 * @description: basic functions of CITA
 * @author: Pei-ya
 * @create: 2022-10-16 20:27
 **/

public class CITAUtil {
    public static BigInteger convertHexToBytes(String hex){
        String clearedStr = Numeric.cleanHexPrefix(hex);
        //String returnSuccess = clearedStr.substring(0,64);
        String point = clearedStr.substring(64,128);
        BigInteger userPoint = new BigInteger(point,16);
        return userPoint;
    }

    public static int getVersion(CITAj service){
        AppMetaData appMetaData = null;
        try{
            appMetaData = service.appMetaData(DefaultBlockParameterName.PENDING).send();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println(appMetaData.getAppMetaDataResult().getVersion());
        return appMetaData.getAppMetaDataResult().getVersion();
    }

    public static BigInteger getChainId(CITAj service){
        AppMetaData appMetaData =  null;
        try {
            appMetaData = service.appMetaData(DefaultBlockParameterName.PENDING).send();
        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println(appMetaData.getAppMetaDataResult().getChainId());
        return appMetaData.getAppMetaDataResult().getChainId();
    }

    public static String getNonce(){
        Random random = new Random(System.currentTimeMillis());
        return String.valueOf(Math.abs(random.nextLong()));
    }

    static BigInteger getCurrentHeight(CITAj service){
        return getCurrentHeight(service,3);
    }

    private static BigInteger getCurrentHeight(CITAj service,int retry){
        int count = 0 ;
        long height = -1;
        while(count<retry){
            try{
                height = service.appBlockNumber().send().getBlockNumber().longValue();
            }
            catch(Exception e){
                height = -1;
                System.out.println("getBlockNumber failed retry ..");
                try{
                    Thread.sleep(2000);
                }
                catch (Exception e1){
                    System.out.println("failed to get block number,Exception: "+e1);
                }
            }
            count++;
        }
        if (height ==-1){
            System.out.println("Failed to get block number after "+ count +" times.");
        }
        return BigInteger.valueOf(height);
    }

    static BigInteger getValidUtilBlock(CITAj service,int validUtilBlock){
        return getCurrentHeight(service).add(BigInteger.valueOf(validUtilBlock));
    }

    static BigInteger getValidUtilBlock(CITAj service){
        return getCurrentHeight(service).add(BigInteger.valueOf(88));
    }
}
