package com.example.lms.controller;

import com.example.lms.model.Admin;
import com.example.lms.model.Book;
import com.example.lms.model.BookCategory;
import com.example.lms.model.User;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AdminControllerTest {
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
    public void testLoginWithUnMatchedCondition() throws Exception {
        Admin admin = new Admin();
        admin.setAdminName("aaa");
        admin.setAdminPwd("1234");
        String userJson = mapper.writeValueAsString(admin);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        Admin res = mapper.readValue(content, Admin.class);

        Assertions.assertNull(res.getAdminId(), "?????????id?????????");
        Assertions.assertNull(res.getAdminName(), "??????????????????");
        Assertions.assertNull(res.getAdminPwd(), "????????????????????????");
        Assertions.assertNull(res.getAdminEmail(), "????????????????????????");
        Assertions.assertNull(res.getAdminStatus(), "????????????????????????");
    }

    @Test
    public void testLoginWithMatchedCondition() throws Exception {
        Admin admin = new Admin();
        admin.setAdminName("wza");
        admin.setAdminPwd("1234");
        String userJson = mapper.writeValueAsString(admin);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        Admin res = mapper.readValue(content, Admin.class);

        Assertions.assertNotNull(res.getAdminId(), "?????????id??????");
        Assertions.assertEquals(admin.getAdminName(), res.getAdminName(), "??????????????????");
        Assertions.assertEquals(admin.getAdminPwd(), res.getAdminPwd(), "?????????????????????");
        Assertions.assertNotNull(res.getAdminEmail(), "?????????????????????");
        Assertions.assertNotNull(res.getAdminStatus(), "?????????????????????");
    }

    @Test
    public void testAddAdmin() throws Exception {
        Admin admin = new Admin();
        admin.setAdminName("abc");
        admin.setAdminPwd("1234");
        admin.setAdminEmail("gg@ke.com");
        admin.setAdminStatus(1);
        String userJson = mapper.writeValueAsString(admin);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/addAdmins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        Admin res = mapper.readValue(content, Admin.class);

        Assertions.assertNotNull(res.getAdminId(), "?????????id?????????");
    }

    @Test
    public void testUpdateAdmin() throws Exception {
        Admin admin = new Admin();
        admin.setAdminId(1);
//        admin.setAdminName("wza");
        admin.setAdminPwd("1111");
        admin.setAdminEmail("gg@ke.com");
//        admin.setAdminStatus(1);
        String userJson = mapper.writeValueAsString(admin);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/updateAdmins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();

        Admin res = mapper.readValue(content, Admin.class);

        Assertions.assertEquals(admin.getAdminId(), res.getAdminId(), "id?????????");
        Assertions.assertEquals(admin.getAdminPwd(), res.getAdminPwd(), "???????????????");
        Assertions.assertEquals(admin.getAdminEmail(), res.getAdminEmail(), "???????????????");

    }


    @Test
    public void testDeleteAdmins() throws Exception {
        Admin admin = new Admin();
        admin.setAdminId(2);
        String userJson = mapper.writeValueAsString(admin);

        mockMvc.perform(MockMvcRequestBuilders.post("/Admins/deleteAdmins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetAdmins() throws Exception {
        Admin admin = new Admin();
        admin.setAdminId(2);
        String userJson = mapper.writeValueAsString(admin);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/getAdmins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())             //?????????Assert.assertEquals(200,status);
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Admin.class);
        List<Admin> admins = mapper.readValue(content, javaType);

        for (Admin admin1 : admins) {
            Assertions.assertNotNull(admin1.getAdminId(), "id?????????");
            Assertions.assertNotNull(admin1.getAdminName(), "??????????????????");
            Assertions.assertNotNull(admin1.getAdminPwd(), "???????????????");
            Assertions.assertNotNull(admin1.getAdminEmail(), "???????????????");
            Assertions.assertNotNull(admin1.getAdminStatus(), "???????????????");
        }
    }

    @Test
    public void testGetAllAdmins() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/Admins/getAllAdmins")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
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

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/addUsers")
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

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/updateUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        User res = mapper.readValue(content, User.class);

        Assertions.assertEquals(res.getUserId(), res.getUserId(), "??????id?????????");
//        Assertions.assertEquals(user.getUserName(), res.getUserName(), "??????????????????");
        Assertions.assertEquals(user.getUserPwd(), res.getUserPwd(), "???????????????");
        Assertions.assertEquals(user.getUserType(), res.getUserType(), "?????????????????????");
//        Assertions.assertEquals(user.getUserEmail(), res.getUserEmail(), "?????????????????????");
//        Assertions.assertEquals(user.getAdminId(), res.getAdminId(), "?????????id?????????");
//        Assertions.assertEquals(user.getUserStatus(), res.getUserStatus(), "?????????????????????");
    }

    @Test
    public void testDeleteUsers() throws Exception {
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            User user = new User();
            user.setUserId(i);
            list.add(user);
        }
        String userJson = mapper.writeValueAsString(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/Admins/deleteUsers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetUsers() throws Exception {
        User user = new User();
        user.setUserId(1);
        String userJson = mapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/getUsers")
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
    public void testAddBook() throws Exception {
        Book book = new Book();
//        book.setBookId(1);
        book.setBookName("asdf");
        book.setBookAuthor("1234");
        book.setBookCount(10);
        book.setBookCategory(2);
        book.setBookStatus(1);
        String userJson = mapper.writeValueAsString(book);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/addBooks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        Book res = mapper.readValue(content, Book.class);

        Assertions.assertNotNull(res.getBookId(), "id?????????");
    }

    @Test
    public void testDeleteBook() throws Exception {
        List<Book> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Book book = new Book();
            book.setBookId(i);
            list.add(book);
        }
        String userJson = mapper.writeValueAsString(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/Admins/deleteBooks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book();
        book.setBookId(1);
//        book.setBookName("asdf");
//        book.setBookAuthor("1234");
        book.setBookCount(29);
//        book.setBookCategory(2);
//        book.setBookStatus(1);
        String userJson = mapper.writeValueAsString(book);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/updateBooks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        Book res = mapper.readValue(content, Book.class);

        Assertions.assertEquals(book.getBookId(), res.getBookId(), "id?????????");
//        Assertions.assertEquals(user.getUserName(), res.getUserName(), "??????????????????");
        Assertions.assertEquals(book.getBookCount(), res.getBookCount(), "???????????????");
//        Assertions.assertEquals(user.getUserEmail(), res.getUserEmail(), "?????????????????????");
//        Assertions.assertEquals(user.getAdminId(), res.getAdminId(), "?????????id?????????");
//        Assertions.assertEquals(user.getUserStatus(), res.getUserStatus(), "?????????????????????");
    }

    @Test
    public void testGetBooks() throws Exception {
        Book book = new Book();
        book.setBookId(1);
//        book.setBookName("asdf");
//        book.setBookAuthor("1234");
//        book.setBookCount(29);
//        book.setBookCategory(2);
//        book.setBookStatus(1);
        String userJson = mapper.writeValueAsString(book);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/getBooks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())             //?????????Assert.assertEquals(200,status);
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Book.class);
        List<Book> books = mapper.readValue(content, javaType);

        for (Book book1 : books) {
            Assertions.assertNotNull(book1.getBookId(), "id?????????");
            Assertions.assertNotNull(book1.getBookName(), "???????????????");
            Assertions.assertNotNull(book1.getBookAuthor(), "???????????????");
            Assertions.assertNotNull(book1.getBookCategory(), "???????????????");
            Assertions.assertNotNull(book1.getBookCount(), "???????????????");
            Assertions.assertNotNull(book1.getBookStatus(), "???????????????");
        }
    }

    @Test
    public void testGetBookCategory() throws Exception {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryId(1);
//        bookCategory.setCategoryName("asdf");
//        bookCategory.setCategoryStatus(1);

        String userJson = mapper.writeValueAsString(bookCategory);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/getBookCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, BookCategory.class);
        List<BookCategory> bookCategories = mapper.readValue(content, javaType);

        for (BookCategory bookCategory1 : bookCategories) {
            Assertions.assertNotNull(bookCategory1.getCategoryId(), "id?????????");
            Assertions.assertNotNull(bookCategory1.getCategoryName(), "???????????????");
            Assertions.assertNotNull(bookCategory1.getCategoryStatus(), "???????????????");
        }

    }

    @Test
    public void testAddBookCategory() throws Exception {
        BookCategory bookCategory = new BookCategory();
//        bookCategory.setCategoryId(1);
        bookCategory.setCategoryName("asdf");
        bookCategory.setCategoryStatus(1);

        String userJson = mapper.writeValueAsString(bookCategory);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/addBookCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();    //??????????????????

        BookCategory res = mapper.readValue(content, BookCategory.class);

        Assertions.assertNotNull(res.getCategoryId(), "id?????????");
    }

    @Test
    public void testDeleteBookCategory() throws Exception {
        List<BookCategory> list = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            BookCategory bookCategory = new BookCategory();
            bookCategory.setCategoryId(i);
            list.add(bookCategory);
        }
        String userJson = mapper.writeValueAsString(list);

        mockMvc.perform(MockMvcRequestBuilders.post("/Admins/deleteBookCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testUpdateBookCategory() throws Exception {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setCategoryId(1);
        bookCategory.setCategoryName("asdff");
        bookCategory.setCategoryStatus(1);

        String userJson = mapper.writeValueAsString(bookCategory);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/Admins/updateBookCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson.getBytes()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        //???JSON???????????????
        String content = mvcResult.getResponse().getContentAsString();
        BookCategory res = mapper.readValue(content, BookCategory.class);

        Assertions.assertEquals(bookCategory.getCategoryId(), res.getCategoryId(), "id?????????");
        Assertions.assertEquals(bookCategory.getCategoryName(), res.getCategoryName(), "???????????????");
        Assertions.assertEquals(bookCategory.getCategoryStatus(), res.getCategoryStatus(), "???????????????");
    }
}
