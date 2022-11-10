package com.ecnu.educationblockchain.controller;

import com.citahub.cita.crypto.ECKeyPair;
import com.citahub.cita.crypto.Keys;
import com.citahub.cita.protocol.core.methods.response.TransactionReceipt;
import com.ecnu.educationblockchain.model.User;
import com.ecnu.educationblockchain.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;

/**
 * @program: EducationBlockchain
 * @description: The controller of user
 * @author: Pei-ya
 * @create: 2022-10-31 12:42
 **/
@Slf4j
@Controller
@RequestMapping("/user")
@Api(tags="用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseBody
    @ApiOperation(value = "用户注册")
    public User register(User user, HttpServletResponse response) throws Exception{
        response.setContentType("text/html;charset=utf-8");
        try{
            //生成公私钥和用户地址
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            BigInteger privateKey = ecKeyPair.getPrivateKey();
            BigInteger publicKey = ecKeyPair.getPublicKey();
            String userAddress = Keys.getAddress(publicKey);

            user.setUserAddress("0x"+userAddress);
            user.setPrivateKey("0x"+privateKey.toString(16));
            user.setPublicKey("0x"+publicKey.toString(16));

            //将这个用户存放到合约中
            TransactionReceipt receipt = userService.addUser(user);
            if(receipt.getErrorMessage()==null){
                response.setHeader("msg", URLEncoder.encode("用户注册成功！","utf-8"));
                log.info("注册用户交易哈希："+receipt.getTransactionHash());
            }else{
                response.setHeader("msg",URLEncoder.encode("注册失败，用户已经存在。","utf-8"));
                log.info("注册用户errorMessage："+receipt.getErrorMessage());
            }
        } catch(SocketTimeoutException e1){
            response.setHeader("msg",URLEncoder.encode("网络超时，请尝试重新注册。","utf-8"));
            e1.printStackTrace();
            return  null;
        } catch (Exception e2){
            response.setHeader("msg",URLEncoder.encode("系统内部错误。","utf-8"));
            e2.printStackTrace();
            return null;
        }
        log.info(user.toString());
        return user;
    }

    @GetMapping("/getUserAddress")
    @ResponseBody
    @ApiOperation(value = "获取当前用户在链上的地址")
    public String getUserAddress(HttpServletRequest request) throws Exception{
        String token = request.getHeader("token");
        String userAddress ="";
        return userAddress;
    }

    @GetMapping("/getPoint")
    @ResponseBody
    @ApiOperation(value = "从链上获取用户积分")
    public BigInteger getPoint(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        String userAddress ="0xbc02b3c23b92460644e1387c0caf37f4485a212c";
        BigInteger point = userService.getPoint(userAddress);
        System.out.println("用户"+userAddress+"目前的积分是："+point);
        return point;
    }

    @PostMapping("/setPoint")
    @ResponseBody
    @ApiOperation(value = "设定积分")
    public String setPoint(String userAddress,int point,HttpServletResponse response)throws Exception{
        response.setContentType("text/html;charset=utf-8");
        String txHash = null;
        try{
            txHash = userService.setPoint(userAddress,BigInteger.valueOf(point));
            if(txHash!=null){
                response.setHeader("msg", URLEncoder.encode("积分设置成功！", "utf-8"));
            } else {
                response.setHeader("msg", URLEncoder.encode("积分设置失败，请重新尝试。", "utf-8"));
            }
        } catch (SocketTimeoutException e1){
            response.setHeader("msg",URLEncoder.encode("网络超时，请重新尝试。","utf-8"));
            e1.printStackTrace();
            return null;
        } catch (Exception e2){
            response.setHeader("msg", URLEncoder.encode("系统内部错误。", "utf-8"));
            e2.printStackTrace();
            return null;
        }
        return txHash;
    }

    @PostMapping("/addPoint")
    @ResponseBody
    @ApiOperation(value = "增加积分")
    public String addPoint(String userAddress,int addPoint,HttpServletResponse response)throws Exception{
        response.setContentType("text/html;charset=utf-8");
        String txHash = null;
        try{
            txHash = userService.addPoint(userAddress,BigInteger.valueOf(addPoint));
            if(txHash!=null){
                response.setHeader("msg", URLEncoder.encode("积分增加成功！", "utf-8"));
            } else {
                response.setHeader("msg", URLEncoder.encode("积分增加失败，请重新尝试。", "utf-8"));
            }
        } catch (SocketTimeoutException e1){
            response.setHeader("msg",URLEncoder.encode("网络超时，请重新尝试。","utf-8"));
            e1.printStackTrace();
            return null;
        }catch (Exception e2){
            response.setHeader("msg", URLEncoder.encode("系统内部错误。", "utf-8"));
            e2.printStackTrace();
            return null;
        }
        return txHash;
    }

    @PostMapping("/subPoint")
    @ResponseBody
    @ApiOperation(value = "减少积分")
    public String subPoint(String userAddress,int subPoint,HttpServletResponse response)throws Exception{
        response.setContentType("text/html;charset=utf-8");
        String txHash = null;
        try{
            txHash = userService.subPoint(userAddress,BigInteger.valueOf(subPoint));
            if(txHash!=null){
                response.setHeader("msg", URLEncoder.encode("积分已减少", "utf-8"));
            } else {
                response.setHeader("msg", URLEncoder.encode("积分未减少，请重新尝试。", "utf-8"));
            }
        } catch (SocketTimeoutException e1){
            response.setHeader("msg",URLEncoder.encode("网络超时，请重新尝试。","utf-8"));
            e1.printStackTrace();
            return null;
        }catch (Exception e2){
            response.setHeader("msg", URLEncoder.encode("系统内部错误。", "utf-8"));
            e2.printStackTrace();
            return null;
        }
        return txHash;
    }
}
