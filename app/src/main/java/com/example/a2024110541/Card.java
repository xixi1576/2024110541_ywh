package com.example.a2024110541;

/**
 * 一张翻牌游戏卡片（Model 层实体）。
 *
 * <p>卡片由点数 {@code rank} 与花色 {@code suit} 唯一确定，内部保存“选中／已配对／正面朝上”三种状态。
 * 本类不依赖任何 Android API，便于用纯 Java 进行逻辑自检。</p>
 */
public class Card {

    /** 两张点数相同的牌配对成功时的得分。 */
    public static final int MATCH_SCORE = 4;

    /** 两张点数不同的牌配对失败时的得分（负分）。 */
    public static final int MISMATCH_SCORE = -1;

    /** 点数，取值 1~8。 */
    private final int rank;

    /** 花色，取值 0~2（三张同点数的牌由三种花色区分）。 */
    private final int suit;

    /** 是否处于选中（待配对）状态。 */
    private boolean chosen;

    /** 是否已经配对成功。 */
    private boolean matched;

    /** 是否正面朝上。 */
    private boolean faceUp;

    /**
     * 创建一张卡片。
     *
     * @param rank 点数，取值 1~8
     * @param suit 花色，取值 0~2
     */
    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    /**
     * 判断本卡片与另一张卡片是否配对。
     *
     * <p>本实验的三人配对玩法约定：<b>点数相同的三张牌</b>构成一组。
     * 因此两张牌的点数相同即视为“这一对匹配”，三张牌两两匹配即为整组配对成功。</p>
     *
     * @param other 另一张卡片
     * @return 匹配返回 {@link #MATCH_SCORE}，不匹配返回 {@link #MISMATCH_SCORE}
     */
    public int match(Card other) {
        if (other == null) {
            return MISMATCH_SCORE;
        }
        return rank == other.rank ? MATCH_SCORE : MISMATCH_SCORE;
    }
}
