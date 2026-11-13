package com.example.a2024110541;

import java.io.Serializable;
import java.util.Locale;

/**
 * 书籍实体类。
 *
 * <p>属性包含书名、作者、出版社、价格，以及出版日期、ISBN 和内容简介。
 * 实现 {@link Serializable}，便于通过 Intent 在 Activity 之间传递对象。</p>
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 书名。 */
    private final String title;

    /** 作者。 */
    private final String author;

    /** 出版社。 */
    private final String publisher;

    /** 价格，单位：元。 */
    private final double price;

    /** 出版日期，格式 yyyy-MM。 */
    private final String publishDate;

    /** 国际标准书号。 */
    private final String isbn;

    /** 内容简介。 */
    private final String summary;

    /**
     * 创建一个书籍对象。
     *
     * @param title       书名
     * @param author      作者
     * @param publisher   出版社
     * @param price       价格（元）
     * @param publishDate 出版日期
     * @param isbn        国际标准书号
     * @param summary     内容简介
     */
    public Book(String title, String author, String publisher, double price,
                String publishDate, String isbn, String summary) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.publishDate = publishDate;
        this.isbn = isbn;
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public double getPrice() {
        return price;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getSummary() {
        return summary;
    }

    /**
     * 返回带货币符号、保留两位小数的价格文本，例如 {@code ￥108.00}。
     */
    public String getPriceText() {
        return String.format(Locale.CHINA, "￥%.2f", price);
    }
}
