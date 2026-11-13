package com.example.a2024110541;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 翻牌游戏模型（Model 层）。
 *
 * <p>牌组由“点数 × 花色”生成：每种点数固定 {@link #SUIT_COUNT} 张牌，
 * 因此 8 种点数共 24 张牌。玩家每次最多翻开 {@link #CARDS_TO_MATCH} 张牌，
 * 若这 {@link #CARDS_TO_MATCH} 张牌点数全部相同则配对成功。</p>
 *
 * <p>本类不依赖任何 Android API，可直接用纯 Java 编译运行进行逻辑自检。</p>
 */
public class CardMatchingGame {

    /** 翻开一张牌消耗的分数。 */
    public static final int FLIP_COST = 1;

    /** 三张牌配对失败时扣除的分数。 */
    public static final int MISMATCH_PENALTY = 2;

    /** 每种点数的牌数，也即一组需要配对的牌数。 */
    public static final int SUIT_COUNT = 3;

    /** 参与配对的牌数：三张牌凑成一组。 */
    public static final int CARDS_TO_MATCH = 3;

    /** 一种点数最多可用的花色数量。 */
    private static final int MAX_SUIT_COUNT = 3;

    /** 全部卡片。 */
    private final List<Card> cards = new ArrayList<>();

    /** 当前分数。 */
    private int score;

    /** 累计翻牌次数。 */
    private int flipCount;

    /**
     * 创建 24 张牌的默认牌组（8 种点数 × 3 种花色）。
     */
    public CardMatchingGame() {
        this(CARDS_TO_MATCH * 8);
    }

    /**
     * 按指定牌数创建牌组，牌数必须是 {@link #CARDS_TO_MATCH} 的整数倍。
     *
     * <p>竖屏、横屏可以使用不同的牌数，模型本身不关心屏幕方向。</p>
     *
     * @param cardCount 牌数，取值范围 3~24，且为 3 的整数倍
     */
    public CardMatchingGame(int cardCount) {
        int normalized = Math.max(CARDS_TO_MATCH, Math.min(cardCount, CARDS_TO_MATCH * 8));
        int rankCount = normalized / CARDS_TO_MATCH;
        for (int rank = 1; rank <= rankCount; rank++) {
            for (int suit = 0; suit < MAX_SUIT_COUNT; suit++) {
                cards.add(new Card(rank, suit));
            }
        }
        shuffle();
    }

    /**
     * 重置游戏：清空所有卡片状态、分数与翻牌次数，并重新洗牌。
     *
     * <p>这是实验五对实验四模型新增的方法。</p>
     */
    public void reset() {
        for (Card card : cards) {
            card.setChosen(false);
            card.setMatched(false);
            card.setFaceUp(false);
        }
        score = 0;
        flipCount = 0;
        shuffle();
    }

    /** 洗牌：打乱卡片在网格中的位置。 */
    private void shuffle() {
        Random random = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            Collections.swap(cards, i, random.nextInt(i + 1));
        }
    }

    /**
     * 取得指定位置的卡片。
     *
     * @param index 位置索引
     * @return 对应卡片
     */
    public Card cardAtIndex(int index) {
        return cards.get(index);
    }

    /** 返回牌组中的牌数。 */
    public int getCardCount() {
        return cards.size();
    }

    public int getScore() {
        return score;
    }

    public int getFlipCount() {
        return flipCount;
    }

    /**
     * 翻开／翻回指定位置的卡片，并处理配对逻辑。
     *
     * <p>规则：</p>
     * <ol>
     *   <li>已配对成功的牌不再响应点击；</li>
     *   <li>点击已翻开的牌会把它翻回背面，且不扣分；</li>
     *   <li>每翻开一张牌扣 {@link #FLIP_COST} 分；</li>
     *   <li>当翻开的牌达到 {@link #CARDS_TO_MATCH} 张时，三张牌两两比对：
     *       全部匹配则三张牌标记为已配对并累加得分，否则全部翻回背面并扣 {@link #MISMATCH_PENALTY} 分。</li>
     * </ol>
     *
     * @param index 被点击的卡片位置
     */
    public void chooseCardAtIndex(int index) {
        Card card = cardAtIndex(index);
        if (card.isMatched()) {
            return;
        }
        if (card.isChosen()) {
            card.setChosen(false);
            card.setFaceUp(false);
            return;
        }

        card.setChosen(true);
        card.setFaceUp(true);
        flipCount++;
        score -= FLIP_COST;

        Card first = null;
        Card second = null;
        for (int i = 0; i < cards.size(); i++) {
            Card other = cards.get(i);
            if (other == card || !other.isChosen() || other.isMatched()) {
                continue;
            }
            if (first == null) {
                first = other;
            } else if (second == null) {
                second = other;
                break;
            }
        }

        if (second == null) {
            return;
        }

        // 三张牌两两比对，任何一对不匹配即视为整组失败
        boolean allMatched = first.match(second) > 0
                && first.match(card) > 0
                && second.match(card) > 0;
        int pairScore = first.match(second) + first.match(card) + second.match(card);
        if (allMatched) {
            for (Card matchedCard : new Card[]{first, second, card}) {
                matchedCard.setChosen(false);
                matchedCard.setMatched(true);
                matchedCard.setFaceUp(true);
            }
            score += pairScore;
        } else {
            for (Card unmatchedCard : new Card[]{first, second, card}) {
                unmatchedCard.setChosen(false);
                unmatchedCard.setFaceUp(false);
            }
            score -= MISMATCH_PENALTY;
        }
    }
}
