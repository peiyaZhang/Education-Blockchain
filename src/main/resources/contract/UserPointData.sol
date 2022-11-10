pragma solidity ^0.4.24;

contract UserPointData{
    
    struct userInfo{
        address user_address;
        uint user_id;
        uint res_point;
    }
    
    mapping(address => userInfo) users;
    mapping(address => bool) user_map;
    uint init_res = 0;
    address admin;
    
    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private FIND_USER_FAILED = 500101;
    uint constant private PERMISSION_ERROR = 500102;
    
    event add_user(uint);
    event set_res(uint);
    event get_res(uint, uint);
    event add_res(uint, uint);
    event sub_res(uint, uint);
    
    constructor(){
        admin = tx.origin;
    }
    
    function AddUser(address user, uint id) public{
        if(tx.origin == admin){
            if(user_map[user] == true){
                emit add_user(FIND_USER_FAILED);
            }else{
                user_map[user] = true;
                users[user] = userInfo(
                    user,
                    id,
                    init_res
                    );
                emit add_user(RETURN_CODE_SUCCESS);
            }
        }else{
            emit add_user(PERMISSION_ERROR);
        }
    }
    
    function SetRes(address user, uint res) public{
        if(tx.origin == admin){
            if(user_map[user] == true){
                userInfo user_info = users[user];
                
                user_info.res_point = res;
                users[user] = user_info;
                
                emit set_res(RETURN_CODE_SUCCESS);
            }else{
                emit set_res(FIND_USER_FAILED);   
            }
        }
        else{
            emit set_res(PERMISSION_ERROR);
        }
    }
   
    function GetRes(address user) public{
        if(user_map[user] == true){
            emit get_res(RETURN_CODE_SUCCESS, users[user].res_point);
        }else{
            emit get_res(FIND_USER_FAILED, 0);
        }
    }
    
    function AddRes(address user, uint num) public{
        if(tx.origin == admin){
            if(user_map[user] == true){
                userInfo user_info = users[user];
                
                user_info.res_point += num;
                users[user] = user_info;
                
                emit add_res(RETURN_CODE_SUCCESS, user_info.res_point);
            }else{
                emit add_res(FIND_USER_FAILED, 0);   
            }
        }
        else{
            emit add_res(PERMISSION_ERROR, 0);
        }
        
    }
    
    function SubRes(address user, uint num) public{
        if(tx.origin == admin){
            if(user_map[user] == true){
                userInfo user_info = users[user];
                
                user_info.res_point -= num;
                users[user] = user_info;
                
                emit sub_res(RETURN_CODE_SUCCESS, user_info.res_point);
            }else{
                emit sub_res(FIND_USER_FAILED, 0);   
            }
        }
        else{
            emit sub_res(PERMISSION_ERROR, 0);
        }
    }

}

