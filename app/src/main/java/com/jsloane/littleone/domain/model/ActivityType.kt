package com.jsloane.littleone.domain.model

import androidx.annotation.DrawableRes
import com.jsloane.littleone.R

enum class ActivityType(
    val title: String,
    val category: Category,
    val code: String,
    val order: Int,
    @DrawableRes val icon: Int
) {
    LEFT_BREAST("Left Breast", Category.FEEDING, "BOOBL", 1, R.drawable.ic_breast_left),
    RIGHT_BREAST("Right Breast", Category.FEEDING, "BOOBR", 2, R.drawable.ic_breast_right),
    BOTTLE("Bottle", Category.FEEDING, "BOTTLE", 3, R.drawable.ic_bottle),
    MEAL("Meal", Category.FEEDING, "MEAL", 4, R.drawable.ic_meal),
    PEE("Pee", Category.DIAPER, "PEE", 0, R.drawable.ic_pee),
    POOP("Poop", Category.DIAPER, "POOP", 0, R.drawable.ic_poop),
    TUMMY_TIME("Tummy Time", Category.LEISURE, "TUMMY", 1, R.drawable.ic_tummy_time),
    PLAY("Play", Category.LEISURE, "PLAY", 2, R.drawable.ic_play),
    OUTDOORS("Outdoors", Category.LEISURE, "OUT", 3, R.drawable.ic_outdoors),
    BATH("Bath", Category.LEISURE, "BATH", 4, R.drawable.ic_bath),
    TV("TV", Category.LEISURE, "TV", 4, R.drawable.ic_tv),
    SLEEP("Sleep", Category.SLEEP, "SLEEP", 0, R.drawable.ic_sleep);

    enum class Category(val order: Int = 0) {
        FEEDING(1),
        DIAPER(2),
        LEISURE(3),
        SLEEP(4)
    }
}
