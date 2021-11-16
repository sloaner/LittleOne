package com.jsloane.littleone.domain.model

import androidx.annotation.DrawableRes
import com.jsloane.littleone.R

enum class ActivityType(val title: String, val category: Category, @DrawableRes val icon: Int) {
    LEFT_BREAST("Left Breast", Category.FEEDING, R.drawable.ic_breast_left),
    RIGHT_BREAST("Right Breast", Category.FEEDING, R.drawable.ic_breast_right),
    BOTTLE("Bottle", Category.FEEDING, R.drawable.ic_bottle),
    MEAL("Meal", Category.FEEDING, R.drawable.ic_meal),
    PEE("Pee", Category.DIAPER, R.drawable.ic_pee),
    POOP("Poop", Category.DIAPER, R.drawable.ic_poop),
    TUMMY_TIME("Tummy Time", Category.LEISURE, R.drawable.ic_tummy_time),
    PLAY("Play", Category.LEISURE, R.drawable.ic_play),
    OUTDOORS("Outdoors", Category.LEISURE, R.drawable.ic_outdoors),
    BATH("Bath", Category.LEISURE, R.drawable.ic_bath),
    TV("TV", Category.LEISURE, R.drawable.ic_tv),
    SLEEP("Sleep", Category.SLEEP, R.drawable.ic_sleep);

    enum class Category {FEEDING, DIAPER, LEISURE, SLEEP}
}