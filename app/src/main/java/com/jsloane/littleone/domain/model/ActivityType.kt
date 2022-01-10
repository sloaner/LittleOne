package com.jsloane.littleone.domain.model

import androidx.annotation.DrawableRes
import com.jsloane.littleone.R
import java.util.BitSet

enum class ActivityType(
    val title: String,
    val category: Category,
    val code: String,
    val features: BitSet,
    @DrawableRes val icon: Int
) {
    LEFT_BREAST(
        "Left Breast",
        Category.FEEDING,
        "BOOBL",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_breast_left
    ),
    RIGHT_BREAST(
        "Right Breast",
        Category.FEEDING,
        "BOOBR",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_breast_right
    ),
    BOTTLE(
        "Bottle",
        Category.FEEDING,
        "BOTTLE",
        BitSet.valueOf(longArrayOf(0b1101)),
        R.drawable.ic_activity_bottle
    ),
    MEAL(
        "Meal",
        Category.FEEDING,
        "MEAL",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_meal
    ),
    PEE(
        "Pee",
        Category.DIAPER,
        "PEE",
        BitSet.valueOf(longArrayOf(0b1100)),
        R.drawable.ic_activity_pee
    ),
    POOP(
        "Poop",
        Category.DIAPER,
        "POOP",
        BitSet.valueOf(longArrayOf(0b1100)),
        R.drawable.ic_activity_poop
    ),
    BOTH(
        "Both",
        Category.DIAPER,
        "BOTH",
        BitSet.valueOf(longArrayOf(0b1100)),
        R.drawable.ic_activity_both
    ),
    TUMMY_TIME(
        "Tummy Time",
        Category.PLAY,
        "TUMMY",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_tummy_time
    ),
    PLAY(
        "Play",
        Category.PLAY,
        "PLAY",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_play
    ),
    OUTDOORS(
        "Outdoors",
        Category.PLAY,
        "OUT",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_outdoors
    ),
    BATH(
        "Bath",
        Category.LEISURE,
        "BATH",
        BitSet.valueOf(longArrayOf(0b1100)),
        R.drawable.ic_activity_bath
    ),
    TV(
        "TV",
        Category.LEISURE,
        "TV",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_tv
    ),
    SLEEP(
        "Sleep",
        Category.SLEEP,
        "SLEEP",
        BitSet.valueOf(longArrayOf(0b1110)),
        R.drawable.ic_activity_sleep
    );

    enum class Category(
        val title: String,
        @DrawableRes val icon: Int,
        val multilineSummary: Boolean
    ) {
        FEEDING("Feeding", R.drawable.ic_activity_bottle, true),
        DIAPER("Diapering", R.drawable.ic_diaper, true),
        LEISURE("Misc", R.drawable.ic_activity_tv, false),
        PLAY("Play", R.drawable.ic_activity_tummy_time, false),
        SLEEP("Sleep", R.drawable.ic_activity_sleep, false)
    }

    companion object {
        // set using longArrayOf(0b3210)
        val FEATURE_DATE = 3 // Has date
        val FEATURE_START = 2 // Has start time
        val FEATURE_END = 1 // Has an end time (if false duration is always 0)
        val FEATURE_QUANTITY = 0 // Has a quantity
    }
}
