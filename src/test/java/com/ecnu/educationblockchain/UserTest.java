package com.ecnu.educationblockchain;

import com.ecnu.educationblockchain.controller.UserController;
import com.ecnu.educationblockchain.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: EducationBlockchain
 * @description: Test of Users
 * @author: Pei-ya
 * @create: 2022-11-03 12:57
 **/


@SpringBootTest
public class UserTest {
    @Autowired
    private UserController userController;
    private static MockHttpServletResponse response;
    private static MockHttpServletRequest request;
    static {
        response = new MockHttpServletResponse();
        request =  new MockHttpServletRequest();
    }

    @Test
    void RunAddUser()throws Exception{
        User testUser = new User("12","51255903074","a123456",null,null,null);
        User result = userController.register(testUser, response);
    }
    @Test
    void RunGet() throws Exception{
        //System.out.println("Hi,it's me! I am testing token!");
        //System.out.println(request.getHeader("token"));
        BigInteger point = userController.getPoint(request);
        System.out.println(point);
    }
    @Test
    void RunSet() throws Exception{
        String txHash = userController.setPoint("0xbc02b3c23b92460644e1387c0caf37f4485a212c", 60,response);
        BigInteger point = userController.getPoint(request);
    }

    @Test
    void RunAddPoint() throws Exception{
        String txHash = userController.addPoint("0xace73fa1fe5c0a5c1410c891a2288af60e47fed6", 40,response);
        BigInteger point = userController.getPoint(request);
    }

    @Test
    void RunSubPoint() throws Exception{
        String txHash = userController.subPoint("0xace73fa1fe5c0a5c1410c891a2288af60e47fed6", 100,response);
        System.out.println("本次交易的哈希值为："+txHash);
        BigInteger point = userController.getPoint(request);
    }
}
