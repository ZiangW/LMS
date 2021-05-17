package com.example.lms.controller;

import com.example.lms.model.BorrowedBook;
import com.example.lms.model.ReturnedBook;
import com.example.lms.model.User;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setupMockMvc(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/Users/getAllUsers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testGetUsers() throws Exception {
        User user = new User();
        user.setUserId(1);
        String userJson = mapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Users/getUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())             //等同于Assert.assertEquals(200,status);
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();    //得到返回结果

        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, User.class);
        List<User> users = mapper.readValue(content, javaType);

        for (User user1 : users) {
            Assertions.assertNotNull(user1.getUserId(), "用户id不为空");
            Assertions.assertNotNull(user1.getUserName(), "用户名不为空");
            Assertions.assertNotNull(user1.getUserPwd(), "用户密码不为空");
            Assertions.assertNotNull(user1.getUserEmail(), "用户邮箱不为空");
            Assertions.assertNotNull(user1.getUserType(), "用户类型不为空");
            Assertions.assertNotNull(user1.getAdminId(), "管理员id不为空");
            Assertions.assertNotNull(user1.getUserStatus(), "用户状态不为空");
        }
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
//        user.setUserId(1);
        user.setUserName("g");
        user.setUserPwd("1234");
        user.setUserEmail("gg@ke.com");
        user.setUserType("Bronze");
        user.setAdminId(1);
        user.setUserStatus(1);
        String userJson = mapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Users/addUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())             //等同于Assert.assertEquals(200,status);
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();    //得到返回结果

        User res = mapper.readValue(content, User.class);

        Assertions.assertNotNull(res.getUserId(), "用户id不为空");
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setUserId(1);
        user.setUserPwd("111");
        user.setUserType("Silver");
        String userJson = mapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Users/updateUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //将JSON转换为对象
        String content = mvcResult.getResponse().getContentAsString();
        User res = mapper.readValue(content, User.class);

        Assertions.assertEquals(user.getUserId(), res.getUserId(), "用户id不匹配");
//        Assertions.assertEquals(user.getUserName(), res.getUserName(), "用户名不匹配");
        Assertions.assertEquals(user.getUserPwd(), res.getUserPwd(), "密码不匹配");
        Assertions.assertEquals(user.getUserType(), res.getUserType(), "用户分类不匹配");
//        Assertions.assertEquals(user.getUserEmail(), res.getUserEmail(), "用户邮箱不匹配");
//        Assertions.assertEquals(user.getAdminId(), res.getAdminId(), "管理员id不匹配");
//        Assertions.assertEquals(user.getUserStatus(), res.getUserStatus(), "用户状态不匹配");
    }

    @Test
    public void testDeleteUser() throws Exception {
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            User user = new User();
            user.setUserId(i);
            list.add(user);
        }
        String userJson = mapper.writeValueAsString(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/Users/deleteUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testLoginWithUnMatchedCondition() throws Exception {
        User user = new User();
        user.setUserName("a");
        user.setUserPwd("1234");
        String userJson = mapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //将JSON转换为对象
        String content = mvcResult.getResponse().getContentAsString();
        User res = mapper.readValue(content, User.class);

        Assertions.assertNull(res.getUserId(), "用户id不为空");
        Assertions.assertNull(res.getUserName(), "用户名不为空");
        Assertions.assertNull(res.getUserPwd(), "用户密码不为空");
        Assertions.assertNull(res.getUserEmail(), "用户邮箱不为空");
        Assertions.assertNull(res.getUserType(), "用户类型不为空");
        Assertions.assertNull(res.getAdminId(), "管理员id不为空");
        Assertions.assertNull(res.getUserStatus(), "用户状态不为空");
    }

    @Test
    public void testLoginWithMatchedCondition() throws Exception {
        User user = new User();
        user.setUserName("a");
        user.setUserPwd("123");
        String userJson = mapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //将JSON转换为对象
        String content = mvcResult.getResponse().getContentAsString();
        User res = mapper.readValue(content, User.class);

        Assertions.assertNotNull(res.getUserId(), "用户id为空");
        Assertions.assertEquals(user.getUserName(), res.getUserName(), "用户名匹配");
        Assertions.assertEquals(user.getUserPwd(), res.getUserPwd(), "密码匹配");
        Assertions.assertNotNull(res.getUserEmail(), "用户邮箱为空");
        Assertions.assertNotNull(res.getUserType(), "用户类型为空");
        Assertions.assertNotNull(res.getAdminId(), "管理员id为空");
        Assertions.assertNotNull(res.getUserStatus(), "用户状态为空");

    }


    @Test
    public void testBorrowBook() throws Exception {
        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setLendBookId(1);
        borrowedBook.setLendUserId(1);
        borrowedBook.setLendDate(new Date());
        borrowedBook.setLendStatus(1);
        String userJson = mapper.writeValueAsString(borrowedBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/Users/borrowBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void testReturnBook() throws Exception{
        ReturnedBook returnedBook = new ReturnedBook();
        returnedBook.setReturnBookId(1);
        returnedBook.setReturnUserId(1);
        returnedBook.setReturnDate(new Date());
        returnedBook.setReturnStatus(1);
        String userJson = mapper.writeValueAsString(returnedBook);

        mockMvc.perform(MockMvcRequestBuilders.post("/Users/returnBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
