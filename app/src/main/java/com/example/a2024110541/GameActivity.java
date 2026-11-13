package com.example.a2024110541;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 翻牌游戏界面（Controller 层）。
 *
 * <p>把 {@link CardMatchingGame} 模型通过 {@link CardAdapter} 显示在 {@link RecyclerView} 中，
 * 负责处理点击、刷新分数、重新开始，以及按屏幕方向切换列数与牌数：
 * 竖屏 4 列 12 张，横屏 6 列 24 张（列数与牌数来自资源文件）。</p>
 */
public class GameActivity extends Activity implements CardAdapter.OnCardClickListener {

    /** 游戏模型。 */
    private CardMatchingGame game;

    /** 列表适配器。 */
    private CardAdapter adapter;

    /** 卡片网格。 */
    private RecyclerView cardsView;

    private TextView scoreText;

    private TextView flipCountText;

    /** 是否已经弹出过关对话框，避免重复弹出。 */
    private boolean winDialogShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        TextView menuButton = findViewById(R.id.button_menu);
        menuButton.setOnClickListener(this::showMenu);

        cardsView = findViewById(R.id.list_cards);
        scoreText = findViewById(R.id.text_score);
        flipCountText = findViewById(R.id.text_flip_count);

        int columns = getResources().getInteger(R.integer.game_columns);
        cardsView.setLayoutManager(new GridLayoutManager(this, columns));

        startNewGame();
    }

    /** 按当前屏幕方向重新开局。 */
    private void startNewGame() {
        game = new CardMatchingGame(getResources().getInteger(R.integer.game_card_count));
        adapter = new CardAdapter(game, this);
        cardsView.setAdapter(adapter);
        winDialogShown = false;
        updateHeader();
    }

    @Override
    public void onCardClick(int position) {
        int cardCount = game.getCardCount();
        boolean[] faceUpBefore = new boolean[cardCount];
        boolean[] matchedBefore = new boolean[cardCount];
        for (int i = 0; i < cardCount; i++) {
            faceUpBefore[i] = game.cardAtIndex(i).isFaceUp();
            matchedBefore[i] = game.cardAtIndex(i).isMatched();
        }

        game.chooseCardAtIndex(position);

        // 只刷新发生变化的卡片，并触发翻牌动画
        for (int i = 0; i < cardCount; i++) {
            Card card = game.cardAtIndex(i);
            if (card.isFaceUp() != faceUpBefore[i] || card.isMatched() != matchedBefore[i]) {
                adapter.notifyItemChanged(i, CardAdapter.PAYLOAD_FLIP);
            }
        }

        updateHeader();
        showWinDialogIfFinished();
    }

    /** 刷新分数与翻牌次数。 */
    private void updateHeader() {
        scoreText.setText(getString(R.string.score_format, game.getScore()));
        flipCountText.setText(getString(R.string.flip_count_format, game.getFlipCount()));
    }

    /** 全部卡片配对完成后弹出提示。 */
    private void showWinDialogIfFinished() {
        if (winDialogShown) {
            return;
        }
        int matchedCount = 0;
        for (int i = 0; i < game.getCardCount(); i++) {
            if (game.cardAtIndex(i).isMatched()) {
                matchedCount++;
            }
        }
        if (matchedCount != game.getCardCount()) {
            return;
        }
        winDialogShown = true;
        new AlertDialog.Builder(this)
                .setTitle(R.string.win_title)
                .setMessage(getString(R.string.win_message_format,
                        game.getCardCount() / CardMatchingGame.CARDS_TO_MATCH,
                        game.getFlipCount(),
                        game.getScore()))
                .setPositiveButton(R.string.win_positive, (dialog, which) -> startNewGame())
                .setCancelable(false)
                .show();
    }

    /**
     * 显示标题栏菜单。菜单内容来自 {@code res/menu/menu_game.xml}。
     *
     * @param anchor 菜单的锚点视图（标题栏右侧按钮）
     */
    private void showMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_game, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this::onOptionsItemSelected);
        popupMenu.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_reset) {
            startNewGame();
            return true;
        }
        if (itemId == R.id.action_about) {
            AboutDialog.show(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
