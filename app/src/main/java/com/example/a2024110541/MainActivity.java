package com.example.a2024110541;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 第一个 Activity。
 *
 * <p>在代码中创建多本书籍对象放入数组，按价格从低到高排序后用列表展示；
 * 单击某一书籍条目时启动 {@link DetailActivity} 显示其完整信息。</p>
 */
public class MainActivity extends Activity {

    /** 书籍数组：数据直接在代码中创建。 */
    private final Book[] books = createBooks();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ① 按价格从低到高排序
        Arrays.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book first, Book second) {
                return Double.compare(first.getPrice(), second.getPrice());
            }
        });

        // ② 用列表显示书籍
        ListView bookList = findViewById(R.id.list_books);
        bookList.setAdapter(new BookAdapter(this, books));

        // ③ 单击条目，把选中的书籍对象传给第二个 Activity
        bookList.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_BOOK, books[position]);
            startActivity(intent);
        });

        TextView header = findViewById(R.id.text_header);
        header.setText(getString(R.string.header_format, books.length));
    }

    /**
     * 在代码中创建多本书籍对象，并组成数组返回。
     */
    private static Book[] createBooks() {
        return new Book[]{
                new Book("Java编程思想（第4版）", "Bruce Eckel", "机械工业出版社", 108.00,
                        "2007-06", "9787111213826",
                        "Java 领域的经典著作，系统讲解了面向对象、泛型、集合与并发等核心知识。"),
                new Book("Android第一行代码（第3版）", "郭霖", "人民邮电出版社", 89.00,
                        "2020-04", "9787115529648",
                        "从零开始讲解 Android 应用开发，涵盖四大组件、UI、网络与数据存储。"),
                new Book("算法导论（原书第3版）", "Thomas H. Cormen", "机械工业出版社", 128.00,
                        "2013-01", "9787111407010",
                        "算法领域的权威教材，深入介绍了排序、图论、动态规划等经典算法。"),
                new Book("数据库系统概念（第6版）", "Abraham Silberschatz", "机械工业出版社", 99.00,
                        "2012-03", "9787111375296",
                        "数据库课程的经典教材，内容覆盖关系模型、SQL、事务与并发控制。"),
                new Book("计算机网络（第7版）", "谢希仁", "电子工业出版社", 49.00,
                        "2017-01", "9787121302954",
                        "国内广泛采用的计算机网络教材，按协议分层讲解网络原理。"),
                new Book("深入理解计算机系统（第3版）", "Randal E. Bryant", "机械工业出版社", 139.00,
                        "2016-11", "9787111544937",
                        "从程序员视角讲解计算机系统，串联汇编、内存层次、链接与并发。")
        };
    }
}
