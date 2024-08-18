package com.ufpb.meuguia.utils

import com.ufpb.meuguia.R
import com.ufpb.meuguia.ui.theme.components.Destinations
import com.ufpb.meuguia.ui.theme.components.MenuItem
import java.util.LinkedList

fun getMainItems(): List<MenuItem> {

    val items = LinkedList<MenuItem>()

    items.add(
        MenuItem(
            1,
            R.string.find_attraction_by_name,
            Destinations.FIND.name
        )
    )

    items.add(
        MenuItem(
            2,
            R.string.find_attraction_by_seg,
            Destinations.FINDBYSEG.name
        )
    )

    items.add(
        MenuItem(
            3,
            R.string.find_attraction_by_city,
            Destinations.FINDBYCITY.name
        )
    )

    items.add(
        MenuItem(
            4,
            R.string.find_attraction_by_type,
            Destinations.FINDBYTYPE.name
        )
    )

    return items

}