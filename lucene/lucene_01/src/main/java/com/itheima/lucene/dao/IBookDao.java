package com.itheima.lucene.dao;

import com.ithaima.lucene.pojo.Book;

import java.util.List;

/**
 * @author QiuDeSheng
 * @date 2019/3/22  20:07
 */
public interface IBookDao {
    /**
     * 查询所有书籍
     * @return
     */
    List<Book> findAll();
}
