package com.example.a2024110541;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 书籍列表适配器：把 {@link Book} 数组渲染成列表条目。
 *
 * <p>使用 ViewHolder 复用条目视图，避免重复查找控件。</p>
 */
public class BookAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final Book[] books;

    public BookAdapter(Context context, Book[] books) {
        this.inflater = LayoutInflater.from(context);
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.length;
    }

    @Override
    public Book getItem(int position) {
        return books[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_book, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = books[position];
        holder.index.setText(String.valueOf(position + 1));
        holder.title.setText(book.getTitle());
        holder.price.setText(book.getPriceText());
        holder.meta.setText(holder.meta.getContext().getString(
                R.string.item_meta_format, book.getAuthor(), book.getPublisher()));
        return convertView;
    }

    /** 缓存条目中的控件引用。 */
    private static final class ViewHolder {

        private final TextView index;
        private final TextView title;
        private final TextView price;
        private final TextView meta;

        ViewHolder(View itemView) {
            index = itemView.findViewById(R.id.text_index);
            title = itemView.findViewById(R.id.text_title);
            price = itemView.findViewById(R.id.text_price);
            meta = itemView.findViewById(R.id.text_meta);
        }
    }
}
