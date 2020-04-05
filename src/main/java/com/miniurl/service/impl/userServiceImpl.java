package com.miniurl.service.impl;

import com.miniurl.entity.User;
import com.miniurl.mapper.UserMapper;
import com.miniurl.service.userService;
import com.miniurl.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
@Service
public class userServiceImpl implements userService {
    private final static int SUCCESS = 1;
    @Autowired
    private UserMapper userMapper;

    @Override
    public HashMap<String,String> add(User user) {
        // 创建Example
        Example example = new Example(User.class);
        // 创建Criteria
        Example.Criteria criteria = example.createCriteria();
        // 添加条件
        criteria.andEqualTo("userEmail", user.getUserEmail());
        if(!userMapper.selectByExample(example).isEmpty()){
            return new HashMap<>() {{
                put("msg", "该邮箱已注册");
                put("code", "201");
            }};
        }
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        user.setHashPass(encoder.encode(user.getHashPass()));
        user.setUserEmailVerify(encoder.encode(user.getUserEmail()).substring(32));//后32位
        user.setCreatedTime(new Date());
        user.setUrlNum(0);
        user.setUserClass("1");
        if(userMapper.insert(user) == SUCCESS) {
            User finaluser = userMapper.selectByPrimaryKey(user);
            return new HashMap<>() {{
                put("msg", "创建成功");
                put("userid", finaluser.getUserId().toString());
                put("code", finaluser.getUserEmailVerify());
            }};
        }else return new HashMap<>() {{
            put("msg", "创建失败");
            put("code", "0");
        }};

    }

    @Override
    public boolean confirm(User user) {
        User temp=userMapper.selectByPrimaryKey(user);
        if(temp==null) return false;
        temp.setUserEmailVerify("1");
        userMapper.updateByPrimaryKey(temp);
        return true;
    }

    @Override
    public String verify(User user) {
        User buffer=userMapper.selectByPrimaryKey(user);
        if(buffer==null) return "该用户未注册";
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        if(encoder.matches(user.getHashPass(),buffer.getHashPass())){
            return "注册成功";
        }
        return "存在用户但验证失败";
    }
    @Override
    public boolean comparePassword(User user,User userInDB){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String hashPassInDB="", passInput ="";
        try{
            hashPassInDB = userInDB.getHashPass();
            passInput = user.getHashPass();
            return encoder.matches(passInput, hashPassInDB);
        } catch (Exception e){}
        return false;
    }
    @Override
    public boolean delete(User user) {
        return userMapper.deleteByPrimaryKey(user) == SUCCESS;
    }

    @Override
    public boolean update(User user) {
        return userMapper.updateByPrimaryKey(user) == SUCCESS;
    }

    @Override
    public User getById(String user_id) {
        return userMapper.selectByPrimaryKey(user_id);
    }

    @Override
    public User getByEmail(String email) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userEmail", email);
        List<User> buffer=userMapper.selectByExample(example);
        if(buffer.isEmpty()){
            return null;
        }
        return buffer.get(0);
    }

    @Override
    public List<User> getAllByPage(Integer currentPage, Integer pageSize) {
        List<User> users = userMapper.selectAll();
        Integer countNums = userMapper.selectCount(new User());
        PageBean<User> page = new PageBean<>(currentPage,pageSize,countNums);
        page.setItems(users);
        return page.getItems();
    }

    @Override
    public void updateLoginTime(User user){
        User userInDB=userMapper.selectByPrimaryKey(user);
        userInDB.setLastLoginTime(new Date());
        userMapper.updateByPrimaryKey(userInDB);
    }
}
