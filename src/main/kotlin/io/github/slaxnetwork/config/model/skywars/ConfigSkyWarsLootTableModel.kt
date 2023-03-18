package io.github.slaxnetwork.config.model.skywars

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigSkyWarsLootTableModel(
    val spawn: DropTable,
    @SerialName("spawn_other")
    val spawnOther: DropTable,
    val middle: DropTable,
    val center: DropTable
) {
    init {

    }

    @Serializable
    data class DropTable(
        val sorted: Boolean,
        @SerialName("drop_amount")
        val amount: DropAmount,
        val drops: List<Drop>
    )

    @Serializable
    data class DropAmount(
        val min: Int,
        val max: Int
    )

    @Serializable
    data class Drop(
        @SerialName("material")
        val materialName: String,
        val amount: Int,
        val chance: Double
    )
}
