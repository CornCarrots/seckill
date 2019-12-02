package com.lh.seckill;

import cn.hutool.core.util.NetUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lh.seckill.controller.vo.LoginVo;
import com.lh.seckill.dao.po.User;
import com.lh.seckill.util.DBUtil;
import com.lh.seckill.util.MD5Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.Cookie;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={TestSeckill.class})
public class TestSeckill {
    static String PASSWORD = "000000";
    static String PID = "1";
    static int SUM = 1;
    public static void createPath(int count)throws Exception{
        List<User> users = new ArrayList<>(count);
        // 生成用户信息
//        generateMiaoshaUserList(count, users);
        List<String> tokens = createUser(count,users);
        System.out.println("start to seckill...");
        String url = "http://localhost:8041/seckill/path";
        File file = new File("C:\\Users\\zeroCoder\\Desktop\\paths.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
        file.createNewFile();
        accessFile.seek(0);

        for (int i = 0; i < users.size(); i++) {
            HttpRequest.getCookieManager().getCookieStore().removeAll();
            User user = users.get(i);
            String token = tokens.get(i);
            Map<String, Object> map = new HashMap<>();//存放参数
//            map.put("uid", user.getId());
            map.put("pid", PID);
            HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
            HttpCookie cookie = new HttpCookie("token",token);
//            headers.put("cookie", cookie.toString());
            //发送post请求并接收响应数据
//            String result= HttpUtil.createGet(url).addHeaders(headers).form(map).execute().body();
            String result= HttpUtil.createGet(url).cookie(cookie).form(map).execute().body();
            JSONObject jo = JSONUtil.parseObj(result);
            System.out.println(jo);
            String path = jo.get("data").toString();// data为edu.uestc.controller.result.Result中的字段
            System.out.println("create path : " + user.getPhone()+" "+tokens.get(i));
            // 将token写入文件中，用于压测时模拟客户端登录时发送的token
            String row = tokens.get(i) + "," + path +"," + user.getId() + "," + PID + "," + SUM;
            accessFile.seek(accessFile.length());
            accessFile.write(row.getBytes());
            accessFile.write("\r\n".getBytes());
            System.out.println("write to file : " + user.getPhone());
        }
        accessFile.close();
        System.out.println("write path to file done!");
    }
    public static List<String> createUser(int count,List<User> users) throws IOException, SQLException, ClassNotFoundException {

//        List<User> users = new ArrayList<>(count);

        // 生成用户信息
        generateMiaoshaUserList(count, users);

        // 将用户信息插入数据库，以便在后面模拟用户登录时可以找到该用户，从而可以生成token返会给客户端，然后保存到文件中用于压测
        // 首次生成数据库信息的时候需要调用这个方法，非首次需要注释掉
//         insertSeckillUserToDB(users);
        // 模拟用户登录，生成token
        System.out.println("start to login...");
        List<String> tokens = new ArrayList<>();
        String url = "http://localhost:8031/user/login";
//        File file = new File("C:\\Users\\zeroCoder\\Desktop\\tokens.txt");
//        if (file.exists()) {
//            file.delete();
//        }
//        RandomAccessFile accessFile = new RandomAccessFile(file, "rw");
//        file.createNewFile();
//        accessFile.seek(0);

        for (int i = 0; i < users.size(); i++) {
            // 模拟用户登录
            User user = users.get(i);
//            Map<String, Object> map = new HashMap<>();//存放参数
//            map.put("phone", user.getPhone());
//            map.put("pass", PASSWORD);
            LoginVo loginVo = new LoginVo();
            loginVo.setPhone(user.getPhone());
            loginVo.setPassword(PASSWORD);
            JSON json = JSONUtil.parse(loginVo);
            HashMap<String, String> headers = new HashMap<>();//存放请求头，可以存放多个请求头
//            headers.put("xxx", xxx);
            //发送post请求并接收响应数据
            String result= HttpUtil.createPost(url).addHeaders(headers).body(json).execute().body();
            // 生成token
            JSONObject jo = JSONUtil.parseObj(result);
            System.out.println(jo.toString());
            String token = jo.get("data").toString();// data为edu.uestc.controller.result.Result中的字段
            System.out.println("create token : " + user.getPhone());
            tokens.add(token);
            // 将token写入文件中，用于压测时模拟客户端登录时发送的token
//            String row = user.getId() + "," + token;
//            accessFile.seek(accessFile.length());
//            accessFile.write(row.getBytes());
//            accessFile.write("\r\n".getBytes());
//            System.out.println("write to file : " + user.getPhone());
        }
//        accessFile.close();
//        System.out.println("write token to file done!");
        return tokens;
    }

    /**
     * 生成用户信息
     *
     * @param count 生成的用户数量
     * @param users 用于存储用户的list
     */
    private static void generateMiaoshaUserList(int count, List<User> users) {
        for (int i = 1; i <= count; i++) {
            User user = new User();
            user.setId(i + 3);
            user.setPhone("1321234123" + i);// 模拟11位的手机号码
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.inputPassToDbPass(PASSWORD, user.getSalt()));
            user.setRegisterDate(new Date());
            user.setAddress("测试"+i);
            users.add(user);
        }
    }

    /**
     * 将用户信息插入数据库中
     *
     * @param users 待插入的用户信息
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static void insertSeckillUserToDB(List<User> users) throws ClassNotFoundException, SQLException {
        System.out.println("start create user...");
        Connection conn = DBUtil.getConn();
        String sql = "INSERT INTO `user`(phone, password, salt, address, register_date, login_date)VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            pstmt.setString(1, user.getPhone());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getSalt());
            pstmt.setString(4, user.getAddress());
            pstmt.setTimestamp(5, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.setTimestamp(6, new Timestamp(user.getRegisterDate().getTime()));
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        pstmt.close();
        conn.close();
        System.out.println("insert to db done!");
    }



    @Before
    public void testBefore(){
        System.out.println("Test before");
    }

    @After
    public void testAfter(){
        System.out.println("Test after");
    }
    @Test
    public void test() throws Exception {
//        createUser(10);
        createPath(9);
    }
}
