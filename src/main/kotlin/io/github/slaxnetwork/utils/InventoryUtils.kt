package io.github.slaxnetwork.utils

import org.bukkit.inventory.Inventory

fun Inventory.getRandomEmptySlot(): Int {
    // giveup
    if(this.firstEmpty() == -1) {
        return 0
    }

    val index = (0 until size).random()
    if(this.getItem(index) != null) {
        return getRandomEmptySlot()
    }
    return index
}