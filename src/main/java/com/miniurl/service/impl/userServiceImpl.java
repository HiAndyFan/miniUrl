package com.miniurl.service.impl;

import com.miniurl.entity.User;
import com.miniurl.mapper.UserMapper;
import com.miniurl.service.userService;
import com.miniurl.utils.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class userServiceImpl implements userService {
    private final static int SUCCESS = 1;
    @Autowired
    private UserMapper userMapper;

    @Override
    public String add(User user) {
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        user.setHashPass(encoder.encode(user.getHashPass()));
        user.setUserEmailVerify(encoder.encode(user.getUserEmail()));
        if(userMapper.insert(user) == SUCCESS)return user.getUserEmailVerify();
        else return "0";
        //这里发送邮件
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
    public boolean delete(User user) {
        if(userMapper.deleteByPrimaryKey(user) == SUCCESS)return true;
        else return false;
        //待完善
    }

    @Override
    public boolean update(User user) {
        if(userMapper.updateByPrimaryKey(user) == SUCCESS)return true;
        else return false;
        //待完善
    }

    @Override
    public User getById(String user_id) {
        return userMapper.selectByPrimaryKey(user_id);
    }

    @Override
    public List<User> getAllByPage(Integer currentPage, Integer pageSize) {
        List<User> users = userMapper.selectAll();
        Integer countNums = userMapper.selectCount(new User());
        PageBean<User> page = new PageBean<>(currentPage,pageSize,countNums);
        page.setItems(users);
        return page.getItems();
    }
}
