package com.FishingAddon.module

import java.awt.Color
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import org.cobalt.api.util.render.Render3D
import org.cobalt.api.module.Module
import org.cobalt.api.module.setting.impl.CheckboxSetting
import org.cobalt.api.event.impl.render.WorldRenderContext
import org.cobalt.api.event.impl.render.WorldRenderEvent
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import org.cobalt.api.event.Event
import org.cobalt.api.event.annotation.SubscribeEvent


object WormFishing : Module("WormFishing Settings") {
    var mc = Minecraft.getInstance()
    val level = mc.level
    private val highlightWormfishSpot by CheckboxSetting(
        name = "Highlight Wormfish Spots",
        description = "Highlights potential wormfish fishing spots in the Crystal Hollows.",
        defaultValue = false
    )

    fun detectWormfishSpot(
        level: Level,
        center: BlockPos,
        radius: Int,
    ): BlockPos? {

        val minX = center.x - radius
        val maxX = center.x + radius
        val minZ = center.z - radius
        val maxZ = center.z + radius

        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                for (y in 65..320) {
                    val pos = BlockPos(x, y, z)
                    val blockState = level.getBlockState(pos)

                    if (blockState.`is`(Blocks.LAVA)) {
                        return pos
                    }
                }
            }
        }

        return null
    }

    @SubscribeEvent
    fun onWorldRender(event: WorldRenderEvent.Start) {
        if (highlightWormfishSpot) return

        val player = mc.player ?: return
        val level = mc.level ?: return
        val playerPos = player.blockPosition()
        val lavapos = detectWormfishSpot(level, playerPos, 10) ?: return

        val blockBox = AABB.ofSize(
            net.minecraft.world.phys.Vec3.atCenterOf(lavapos),
            1.0, 1.0, 1.0
        )

        Render3D.drawBox(event.context, blockBox, Color(0, 150, 255), esp = true)

    }

}

