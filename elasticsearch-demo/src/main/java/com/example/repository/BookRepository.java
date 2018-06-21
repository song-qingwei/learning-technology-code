package com.example.repository;

import com.example.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author SongQingWei
 * @date 2018年05月15 15:05
 */
public interface BookRepository extends ElasticsearchRepository<Book, Integer> {

    /**
     * 通过书名查找书
     * @param bookName 书名
     * @return book
     */
    List<Book> findBookByBookName(String bookName);
}
