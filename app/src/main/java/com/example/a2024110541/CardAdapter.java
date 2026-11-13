package com.example.a2024110541;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 翻牌游戏适配器（Adapter 层）。
 *
 * <p>继承 {@link RecyclerView.Adapter}，重写 {@code onCreateViewHolder}、
 * {@code onBindViewHolder}、{@code getItemCount}，并支持局部刷新（payload）触发
 * 翻牌动画。点击事件通过 {@link OnCardClickListener} 回传给控制器。</p>
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    /** 局部刷新负载：只播放翻牌动画，不重建整行。 */
    public static final Object PAYLOAD_FLIP = new Object();

    /** 卡片点击回调。 */
    public interface OnCardClickListener {

        /**
         * 卡片被点击。
         *
         * @param position 卡片在网格中的位置
         */
        void onCardClick(int position);
    }

    private final CardMatchingGame game;
    private final OnCardClickListener listener;

    /**
     * 创建适配器。
     *
     * @param game     游戏模型
     * @param listener 卡片点击回调
     */
    public CardAdapter(CardMatchingGame game, OnCardClickListener listener) {
        this.game = game;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        CardViewHolder holder = new CardViewHolder(itemView);
        itemView.setOnClickListener(view -> {
            int position = holder.getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                listener.onCardClick(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        bindCard(holder, position);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position,
                                 @NonNull java.util.List<Object> payloads) {
        boolean flip = false;
        for (Object payload : payloads) {
            if (PAYLOAD_FLIP.equals(payload)) {
                flip = true;
                break;
            }
        }
        if (flip) {
            playFlipAnimation(holder, position);
        } else {
            bindCard(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return game.getCardCount();
    }

    /** 根据卡片状态刷新正面／背面内容与背景。 */
    private void bindCard(CardViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Card card = game.cardAtIndex(position);

        String[] ranks = context.getResources().getStringArray(R.array.card_ranks);
        String[] suits = context.getResources().getStringArray(R.array.card_suits);
        holder.rankText.setText(ranks[card.getRank() - 1]);
        holder.suitText.setText(suits[card.getSuit()]);

        if (card.isMatched()) {
            holder.cardRoot.setBackgroundResource(R.drawable.bg_card_matched);
            holder.backLayer.setVisibility(View.GONE);
            holder.faceLayer.setVisibility(View.VISIBLE);
            holder.rankText.setAlpha(0.55f);
            holder.suitText.setAlpha(0.55f);
        } else if (card.isFaceUp()) {
            holder.cardRoot.setBackgroundResource(card.isChosen()
                    ? R.drawable.bg_card_chosen
                    : R.drawable.bg_card_front);
            holder.backLayer.setVisibility(View.GONE);
            holder.faceLayer.setVisibility(View.VISIBLE);
            holder.rankText.setAlpha(1f);
            holder.suitText.setAlpha(1f);
        } else {
            holder.cardRoot.setBackgroundResource(R.drawable.bg_card_back);
            holder.backLayer.setVisibility(View.VISIBLE);
            holder.faceLayer.setVisibility(View.GONE);
        }
    }

    /** 翻牌动画：先转到侧面，切换正反面后再转回来。 */
    private void playFlipAnimation(CardViewHolder holder, int position) {
        holder.cardRoot.animate().cancel();
        holder.cardRoot.setRotationY(0f);
        holder.cardRoot.animate()
                .rotationY(90f)
                .setDuration(140)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(android.animation.Animator animation) {
                        bindCard(holder, position);
                        holder.cardRoot.setRotationY(-90f);
                        holder.cardRoot.animate()
                                .rotationY(0f)
                                .setDuration(140)
                                .setListener(null)
                                .start();
                    }
                })
                .start();
    }

    /** 缓存卡片条目中的控件引用。 */
    static final class CardViewHolder extends RecyclerView.ViewHolder {

        private final View cardRoot;
        private final View backLayer;
        private final View faceLayer;
        private final TextView rankText;
        private final TextView suitText;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoot = itemView.findViewById(R.id.card_root);
            backLayer = itemView.findViewById(R.id.card_back_layer);
            faceLayer = itemView.findViewById(R.id.card_face_layer);
            rankText = itemView.findViewById(R.id.text_card_rank);
            suitText = itemView.findViewById(R.id.text_card_suit);
        }
    }
}
