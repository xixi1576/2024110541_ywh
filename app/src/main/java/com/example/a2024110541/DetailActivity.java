package com.example.a2024110541;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 第二个 Activity：显示被选中书籍的完整信息。
 */
public class DetailActivity extends Activity {

    /** Intent 中传递书籍对象的键。 */
    public static final String EXTRA_BOOK = "extra_book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Book book = readBook(getIntent());
        if (book == null) {
            // 没有收到书籍数据时直接退出，避免显示空白页面
            finish();
            return;
        }

        TextView title = findViewById(R.id.text_detail_title);
        title.setText(book.getTitle());

        setText(R.id.text_detail_name, book.getTitle());
        setText(R.id.text_detail_author, book.getAuthor());
        setText(R.id.text_detail_publisher, book.getPublisher());
        setText(R.id.text_detail_price, book.getPriceText());
        setText(R.id.text_detail_date, book.getPublishDate());
        setText(R.id.text_detail_isbn, book.getIsbn());
        setText(R.id.text_detail_summary, book.getSummary());
    }

    /** 把文本填入指定 id 的 TextView。 */
    private void setText(int viewId, String text) {
        TextView textView = findViewById(viewId);
        textView.setText(text);
    }

    /**
     * 从 Intent 中取出书籍对象。
     *
     * <p>API 33 起官方推荐带 Class 参数的重载，低版本只能使用已标记弃用的旧方法。</p>
     */
    @SuppressWarnings("deprecation")
    private static Book readBook(Intent intent) {
        if (intent == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getSerializableExtra(EXTRA_BOOK, Book.class);
        }
        Object extra = intent.getSerializableExtra(EXTRA_BOOK);
        return extra instanceof Book ? (Book) extra : null;
    }
}
