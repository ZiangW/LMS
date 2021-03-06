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
                .andExpect(MockMvcResultMatchers.status().isOk())             //?????????Assert.assertEquals(200,status);
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, User.class);
        List<User> users = mapper.readValue(content, javaType);

        for (User user1 : users) {
            Assertions.assertNotNull(user1.getUserId(), "??????id?????????");
            Assertions.assertNotNull(user1.getUserName(), "??????????????????");
            Assertions.assertNotNull(user1.getUserPwd(), "?????????????????????");
            Assertions.assertNotNull(user1.getUserEmail(), "?????????????????????");
            Assertions.assertNotNull(user1.getUserType(), "?????????????????????");
            Assertions.assertNotNull(user1.getAdminId(), "?????????id?????????");
            Assertions.assertNotNull(user1.getUserStatus(), "?????????????????????");
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
                .andExpect(MockMvcResultMatchers.status().isOk())             //?????????Assert.assertEquals(200,status);
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        User res = mapper.readValue(content, User.class);

        Assertions.assertNotNull(res.getUserId(), "??????id?????????");
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

        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        User res = mapper.readValue(content, User.class);

        Assertions.assertEquals(user.getUserId(), res.getUserId(), "??????id?????????");
//        Assertions.assertEquals(user.getUserName(), res.getUserName(), "??????????????????");
        Assertions.assertEquals(user.getUserPwd(), res.getUserPwd(), "???????????????");
        Assertions.assertEquals(user.getUserType(), res.getUserType(), "?????????????????????");
//        Assertions.assertEquals(user.getUserEmail(), res.getUserEmail(), "?????????????????????");
//        Assertions.assertEquals(user.getAdminId(), res.getAdminId(), "?????????id?????????");
//        Assertions.assertEquals(user.getUserStatus(), res.getUserStatus(), "?????????????????????");
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
        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        User res = mapper.readValue(content, User.class);

        Assertions.assertNull(res.getUserId(), "??????id?????????");
        Assertions.assertNull(res.getUserName(), "??????????????????");
        Assertions.assertNull(res.getUserPwd(), "?????????????????????");
        Assertions.assertNull(res.getUserEmail(), "?????????????????????");
        Assertions.assertNull(res.getUserType(), "?????????????????????");
        Assertions.assertNull(res.getAdminId(), "?????????id?????????");
        Assertions.assertNull(res.getUserStatus(), "?????????????????????");
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
        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        User res = mapper.readValue(content, User.class);

        Assertions.assertNotNull(res.getUserId(), "??????id??????");
        Assertions.assertEquals(user.getUserName(), res.getUserName(), "???????????????");
        Assertions.assertEquals(user.getUserPwd(), res.getUserPwd(), "????????????");
        Assertions.assertNotNull(res.getUserEmail(), "??????????????????");
        Assertions.assertNotNull(res.getUserType(), "??????????????????");
        Assertions.assertNotNull(res.getAdminId(), "?????????id??????");
        Assertions.assertNotNull(res.getUserStatus(), "??????????????????");

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
