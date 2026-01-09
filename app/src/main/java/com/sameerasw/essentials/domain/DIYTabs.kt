package com.sameerasw.essentials.domain

import androidx.annotation.StringRes
import com.sameerasw.essentials.R

enum class DIYTabs(@StringRes val title: Int, @StringRes val subtitle: Int, val iconRes: Int) {
    ESSENTIALS(R.string.tab_essentials, R.string.tab_essentials_subtitle, R.drawable.ic_stat_name),
    FREEZE(R.string.tab_freeze, R.string.tab_freeze_subtitle, R.drawable.rounded_mode_cool_24),
    DIY(R.string.tab_diy, R.string.tab_diy_subtitle, R.drawable.rounded_experiment_24)
}