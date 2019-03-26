package com.itheima.lucene.dao.impl;

import com.ithaima.lucene.pojo.Book;
import com.itheima.lucene.dao.IBookDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author QiuDeSheng
 * @date 2019/3/22  20:09
 */
public class BookDaoImpl implements IBookDao {
    public List<Book> findAll() {


        List<Book> bookList = new ArrayList<Book>();
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet rs=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene_db","root","root");
            String sql = "select * from book";
            statement = connection.prepareStatement(sql);
            rs = statement.executeQuery();

            while (rs.next()){
                Book book = new Book();
                book.setId(rs.getInt(1));
                book.setBookName(rs.getString(2));
                book.setPrice(rs.getFloat(3));
                book.setPic(rs.getString(4));
                book.setBookDesc(rs.getString(5));

                bookList.add(book);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (rs!=null) rs.close();
                if (statement!=null) statement.close();
                if (connection!=null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bookList;
    }
}
