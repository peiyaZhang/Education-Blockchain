package com.ecnu.educationblockchain;

import com.ecnu.educationblockchain.config.CITAConfig;
import com.ecnu.educationblockchain.util.ContractUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.MergedAnnotationPredicates;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: EducationBlockchain
 * @description: To first deploy contract
 * @author: Pei-ya
 * @create: 2022-10-20 16:54
 **/
@SpringBootTest
public class ContractAccountTest {
    private static CITAConfig conf;
    static {
        conf = new CITAConfig();
    }
    @Test
    void firstDeployAndRun() throws Exception{
        Map<String,String> map =new HashMap<>();
        String pointContractAddress = ContractUtil.deployContract(conf.pointSolidity);

        ContractUtil.storeAbiToBlockchain(pointContractAddress,conf.pointSolidity);

        map.put("PointContractAddr",pointContractAddress);

        ContractUtil.updateConfig(map,"deploy contract");
    }
}
