package com.example.lms.service;

import com.example.lms.model.Admin;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AdminManagerTest {

    @Autowired
    private AdminManager adminManager;

    @Test
    public void addAdminsWithExistedInfo() {
        Admin admin = new Admin();
        admin.setAdminName("wza");
        admin.setAdminPwd("12345678");
        admin.setAdminEmail("wza@ke.com");

        Admin res = adminManager.addAdmin(admin);

        Assertions.assertNull(res.getAdminId(), "id存在");
        Assertions.assertNull(res.getAdminName(), "名字存在");
        Assertions.assertNull(res.getAdminEmail(), "邮箱存在");
        Assertions.assertNull(res.getAdminPwd(), "密码存在");
        Assertions.assertNull(res.getAdminStatus(), "状态存在");
    }

    @Test
    public void addAdminsWithNewInfo() {
        Admin admin = new Admin();
        admin.setAdminName("asdf");
        admin.setAdminPwd("12345678");
        admin.setAdminEmail("wasdf@ke.com");

        Admin res = adminManager.addAdmin(admin);

        Assertions.assertNotNull(res.getAdminId(), "id不存在");
        Assertions.assertEquals(admin.getAdminName(), res.getAdminName(), "名字不匹配");
        Assertions.assertEquals(admin.getAdminEmail(), res.getAdminEmail(), "邮箱不匹配");
        Assertions.assertEquals(admin.getAdminPwd(), res.getAdminPwd(), "密码不匹配");
        Assertions.assertEquals(admin.getAdminStatus(), res.getAdminStatus(), "状态不匹配");
    }

    @Test
    public void addAdminWithNullInfo() {
        Admin admin = new Admin();

        Admin res = adminManager.addAdmin(admin);

        Assertions.assertNull(res.getAdminId(), "id存在");
        Assertions.assertNull(res.getAdminName(), "名字存在");
        Assertions.assertNull(res.getAdminEmail(), "邮箱存在");
        Assertions.assertNull(res.getAdminPwd(), "密码存在");
        Assertions.assertNull(res.getAdminStatus(), "状态存在");
    }

    @Test
    public void deleteAdminWithValidInfo() {
        Admin admin = new Admin();
        admin.setAdminId(1);

        int res = adminManager.deleteAdmin(admin);

        Assertions.assertEquals(res, 1, "返回码错误");

    }

    @Test
    public void deleteAdminWithInValidInfo() {
        Admin admin = new Admin();
        admin.setAdminName("wza");

        int res = adminManager.deleteAdmin(admin);

        Assertions.assertEquals(res, 0, "返回码错误");

    }

    @Test
    public void updateAdminWithValidInfo() {
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setAdminPwd("12345");

        Admin res = adminManager.updateAdmin(admin);

        Assertions.assertEquals(admin.getAdminId(), res.getAdminId(), "id不匹配");
        Assertions.assertNotNull(res.getAdminName(), "名字为空");
        Assertions.assertNotNull(res.getAdminEmail(), "邮箱不为空");
        Assertions.assertEquals(admin.getAdminPwd(), res.getAdminPwd(), "密码不匹配");
        Assertions.assertNotNull(res.getAdminStatus(), "状态不为空");
    }

    @Test
    public void updateAdminWithInValidInfo() {
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setAdminStatus(1);
        admin.setAdminPwd("12345");
        admin.setAdminEmail("zzz@ke.com");
        admin.setAdminName("www");

        Admin res = adminManager.updateAdmin(admin);

        Assertions.assertNull(res.getAdminId(), "id存在");
        Assertions.assertNull(res.getAdminName(), "名字存在");
        Assertions.assertNull(res.getAdminEmail(), "邮箱存在");
        Assertions.assertNull(res.getAdminPwd(), "密码存在");
        Assertions.assertNull(res.getAdminStatus(), "状态存在");
    }

    @Test
    public void getAdminsWithValidInfo() {
        Admin admin = new Admin();
        admin.setAdminId(1);

        List<Admin> res = adminManager.getAdmins(admin);
        for (Admin ad : res) {
            Assertions.assertNotNull(ad.getAdminId(), "名字为空");
        }
    }

    @Test
    public void getAdminsWithInValidInfo() {
        Admin admin = new Admin();
        admin.setAdminId(1333);

        List<Admin> res = adminManager.getAdmins(admin);
        for (Admin ad : res) {
            Assertions.assertNull(ad.getAdminId(), "名字不为空");
        }
    }

    @Test
    public void getAllAdmins() {
        List<Admin> res = adminManager.getAllAdmins();

        for (Admin admin : res) {
            Assertions.assertNotNull(admin.getAdminId(), "id为空");
        }
    }
}
