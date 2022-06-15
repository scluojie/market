package com.hxcy.market;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author kevin
 * @date 2022/6/9
 * @desc
 */
public class Test {
    String filePath = "C:\\Users\\13090\\Downloads\\card_configs";

    String driverClassName = "com.mysql.cj.jdbc.Driver";	//启动驱动
    String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";	//设置连接路径
    String username = "root";	//数据库用户名
    String password = "123456";	//数据库连接密码
    Connection con = null;		//连接
    PreparedStatement pstmt = null;	//使用预编译语句
    ResultSet rs = null;	//获取的结果集

    public static void main(String[] args) {
        new Test().read();

    }

    public void read(){
        try {
            FileReader fileReader = new FileReader(new File(filePath));
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
                String[] split = line.split("\t");
                Card card = new Card(split[0], split[1], split[13]);
                insert(card);
            }
            System.out.println(sb);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void insert(Card card){
        try {
            Class.forName(driverClassName); //执行驱动
            con = DriverManager.getConnection(url, username, password); //获取连接


            String sql = "INSERT INTO card VALUES(?,?,?)"; //设置的预编译语句格式
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, card.getCardId());
            pstmt.setString(2, card.getFullName());
            pstmt.setString(3, card.getSkill());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //关闭资源,倒关
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                if(con != null) con.close();  //必须要关
            } catch (Exception e) {
            }
        }

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Card{
        String cardId;
        String fullName;
        String skill;

    }
}
