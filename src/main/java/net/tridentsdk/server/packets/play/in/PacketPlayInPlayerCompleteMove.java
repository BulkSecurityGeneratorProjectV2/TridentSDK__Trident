/*
 * Trident - A Multithreaded Server Alternative
 * Copyright 2014 The TridentSDK Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.tridentsdk.server.packets.play.in;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.Coordinates;
import net.tridentsdk.Trident;
import net.tridentsdk.base.Substance;
import net.tridentsdk.event.Cancellable;
import net.tridentsdk.event.Event;
import net.tridentsdk.event.player.PlayerMoveEvent;
import net.tridentsdk.server.data.Slot;
import net.tridentsdk.server.netty.ClientConnection;
import net.tridentsdk.server.netty.packet.Packet;
import net.tridentsdk.server.packets.play.out.PacketPlayOutEntityCompleteMove;
import net.tridentsdk.server.packets.play.out.PacketPlayOutEntityTeleport;
import net.tridentsdk.server.packets.play.out.PacketPlayOutWindowItems;
import net.tridentsdk.server.player.PlayerConnection;
import net.tridentsdk.server.player.TridentPlayer;
import net.tridentsdk.window.inventory.Item;

/**
 * Packet sent when player moved both x, y, z and yaw, and pitch.
 */
public class PacketPlayInPlayerCompleteMove extends PacketPlayInPlayerMove {
    /**
     * New yaw of the client
     */
    protected float newYaw;
    /**
     * New pitch of the client
     */
    protected float newPitch;

    @Override
    public int getId() {
        return 0x06;
    }

    @Override
    public Packet decode(ByteBuf buf) {
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();

        super.location = Coordinates.create(null, x, y, z);

        this.newYaw = buf.readFloat();
        this.newPitch = buf.readFloat();

        super.onGround = buf.readBoolean();
        return this;
    }

    @Override
    public void handleReceived(ClientConnection connection) {
        TridentPlayer player = ((PlayerConnection) connection).getPlayer();
        super.location.setWorld(player.getWorld());

        Event event = new PlayerMoveEvent(player, player.getLocation(), super.location);
        Trident.getEventHandler().call(event);

        if (((Cancellable) event).isIgnored()) {
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();

            packet.set("entityId", player.getId());
            packet.set("location", player.getLocation());
            packet.set("onGround", player.isOnGround());

            connection.sendPacket(packet);
            return;
        }

        // process move

        if (player.isLoggingIn())
            player.resumeLogin();
    }
}
