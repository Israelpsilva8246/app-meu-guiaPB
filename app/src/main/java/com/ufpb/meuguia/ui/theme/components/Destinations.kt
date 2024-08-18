package com.ufpb.meuguia.ui.theme.components

import androidx.annotation.StringRes
import com.ufpb.meuguia.R

enum class Destinations(@StringRes val title: Int) {
    HOME(title = R.string.homeView),
    LIST(title = R.string.listAttractiveView),
    FINDOPTIONS(title = R.string.find_options_view),
    FIND(title = R.string.find_attraction),
    FINDBYSEG(title = R.string.find_attraction_by_seg),
    FINDBYTYPE(title = R.string.find_attraction_by_type),
    FINDBYCITY(title = R.string.find_attraction_by_city),
    ABOUT(title = R.string.sobre)
}