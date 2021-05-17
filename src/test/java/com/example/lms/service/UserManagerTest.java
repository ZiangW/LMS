package com.example.lms.service;

import com.example.lms.model.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserManagerTest {

    @Autowired
    private UserManager userManager;


    @Test
    public void addUserWithExistedInfo() {
        User user = new User();
//        user.setUserId(1);
        user.setUserStatus(1);
        user.setUserType("Bronze");
        user.setUserPwd("123");
        user.setUserEmail("bba@ke.com");
        user.setAdminId(1);
        user.setUserName("a");

        User res = userManager.addUser(user);

        Assertions.assertNull(res.getUserId(), "id存在");
        Assertions.assertNull(res.getUserName(), "名字存在");
        Assertions.assertNull(res.getAdminId(), "管理员存在");
        Assertions.assertNull(res.getUserType(), "分类存在");
        Assertions.assertNull(res.getUserPwd(), "密码存在");
        Assertions.assertNull(res.getUserEmail(), "邮箱存在");
        Assertions.assertNull(res.getUserStatus(), "状态存在");
    }

    @Test
    public void addUserWithNewInfo() {
        User user = new User();
//        user.setUserId(1);
        user.setUserStatus(1);
        user.setUserType("Bronze");
        user.setUserPwd("123");
        user.setUserEmail("ddd@ke.com");
        user.setAdminId(1);
        user.setUserName("addd");

        User res = userManager.addUser(user);

        Assertions.assertNotNull(res.getUserId(), "id存在");
        Assertions.assertEquals(user.getUserName(), res.getUserName(), "名字匹配");
        Assertions.assertEquals(user.getAdminId(), res.getAdminId(), "管理员匹配");
        Assertions.assertEquals(user.getUserType(), res.getUserType(), "分类匹配");
        Assertions.assertEquals(user.getUserPwd(), res.getUserPwd(), "密码匹配");
        Assertions.assertEquals(user.getUserEmail(), res.getUserEmail(), "邮箱匹配");
        Assertions.assertEquals(user.getUserStatus(), res.getUserStatus(), "状态匹配");
    }

    @Test
    public void addUserWithNullInfo() {
        User user = new User();

        User res = userManager.addUser(user);

        Assertions.assertNull(res.getUserId(), "id存在");
        Assertions.assertNull(res.getUserName(), "名字存在");
        Assertions.assertNull(res.getAdminId(), "管理员存在");
        Assertions.assertNull(res.getUserType(), "分类存在");
        Assertions.assertNull(res.getUserPwd(), "密码存在");
        Assertions.assertNull(res.getUserEmail(), "邮箱存在");
        Assertions.assertNull(res.getUserStatus(), "状态存在");
    }

    @Test
    public void deleteUsersWithValidInfo() {
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            User user = new User();
            user.setUserId(i);
            list.add(user);
        }

        List<Integer> res = userManager.deleteUsers(list);

        for (int code : res) {
            Assertions.assertEquals(code, 1, "返回码错误");
        }
    }

    @Test
    public void deleteUsersWithSomeInvalidInfo() {
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            User user = new User();
            user.setUserId(i);
            list.add(user);
        }
        User user = new User();
        user.setUserName("asdf");
        list.add(user);

        List<Integer> res = userManager.deleteUsers(list);

        Assertions.assertEquals(res.get(2), 0, "返回码错误");
    }

    @Test
    public void updateUserWithValidInfo() {
        User user = new User();
        user.setUserId(1);
        user.setUserStatus(1);
        user.setUserType("Bronze");
        user.setUserPwd("123");
        user.setUserEmail("ddd@ke.com");
        user.setAdminId(1);

        User res = userManager.updateUser(user);

        Assertions.assertEquals(user.getUserId(), res.getUserId(), "id匹配");
        Assertions.assertNotNull(res.getUserName(), "名字为空");
        Assertions.assertEquals(user.getAdminId(), res.getAdminId(), "管理员匹配");
        Assertions.assertEquals(user.getUserType(), res.getUserType(), "分类匹配");
        Assertions.assertEquals(user.getUserPwd(), res.getUserPwd(), "密码匹配");
        Assertions.assertEquals(user.getUserEmail(), res.getUserEmail(), "邮箱匹配");
        Assertions.assertEquals(user.getUserStatus(), res.getUserStatus(), "状态匹配");
    }

    @Test
    public void updateUserWithInValidInfo() {
        User user = new User();
        user.setUserId(131);

        User res = userManager.updateUser(user);

        Assertions.assertNull(res.getUserId(), "id存在");
        Assertions.assertNull(res.getUserName(), "名字存在");
        Assertions.assertNull(res.getAdminId(), "管理员存在");
        Assertions.assertNull(res.getUserType(), "分类存在");
        Assertions.assertNull(res.getUserPwd(), "密码存在");
        Assertions.assertNull(res.getUserEmail(), "邮箱存在");
        Assertions.assertNull(res.getUserStatus(), "状态存在");
    }

    @Test
    public void getUsersWithValidInfo() {
        User user = new User();
        user.setUserId(1);

        List<User> res = userManager.getUsers(user);

        for (User u : res) {
            Assertions.assertNotNull(u.getUserName(), "名字为空");
        }
    }

    @Test
    public void getUsersWithInValidInfo() {
        User user = new User();
        user.setUserId(122);

        List<User> res = userManager.getUsers(user);

        for (User u : res) {
            Assertions.assertNull(u.getUserName(), "名字不为空");
        }
    }


    @Test
    public void getAllUsers() {
        List<User> res = userManager.getAllUsers();

        for (User u : res) {
            Assertions.assertNotNull(u.getUserId(), "id为空");
        }
    }
}
