package com.example;

import com.example.entity.Book;
import com.example.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchDemoApplicationTests {

    @Autowired
    private BookRepository repository;

    @Test
    public void contextLoads() {

    }

    /**
     * 设置索引
     */
    @Test
    public void index() {
        Book book = new Book(2, "西游记", "吴承恩");
        Book index = repository.save(book);
        System.out.println(index);
    }

    /**
     * 查询
     */
    @Test
    public void search() {
        Book book = repository.findById(1).get();
        System.out.println(book);
    }

    /**
     * 模糊查询
     */
    @Test
    public void fuzzyQuery() {
        List<Book> list = repository.findBookByBookName("三");
        for (Book book : list) {
            System.out.println(book);
        }
    }

    @Test
    public void delete() {
        repository.deleteById(1);
    }
}
