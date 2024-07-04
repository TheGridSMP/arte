package io.shardmc.arte.fabric.platform;

import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.UUID;

public class Platform1205 extends FabricPlatform {

    @Override
    protected void applyPack(ServerPlayerEntity player, UUID id, String address, String hash, boolean force, Text text) {
        player.networkHandler.sendPacket(new ResourcePackSendS2CPacket(id, address, hash, force, Optional.ofNullable(text)));
    }
}
