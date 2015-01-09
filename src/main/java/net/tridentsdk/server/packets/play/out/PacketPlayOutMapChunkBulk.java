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

package net.tridentsdk.server.packets.play.out;

import io.netty.buffer.ByteBuf;
import net.tridentsdk.factory.Factories;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.packet.OutPacket;
import net.tridentsdk.world.ChunkLocation;

import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

public class PacketPlayOutMapChunkBulk extends OutPacket {

    protected boolean lightSent = true;
    protected final Queue<PacketPlayOutChunkData> entries = new PriorityBlockingQueue<>(1024, new Comparator<PacketPlayOutChunkData>() {
        @Override
        public int compare(PacketPlayOutChunkData o1, PacketPlayOutChunkData o2) {
            ChunkLocation c = o1.getChunkLocation();
            ChunkLocation c0 = o2.getChunkLocation();

            int cx = c.getX();
            int cz = c.getZ();

            int c0x = c0.getX();
            int c0z = c0.getZ();

            return (Math.abs(cx) + Math.abs(cz)) - (Math.abs(c0x) + Math.abs(c0z));
        }
    });

    @Override
    public int getId() {
        return 0x26;
    }

    public void addEntry(PacketPlayOutChunkData entry) {
        entries.add(entry);
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeBoolean(this.lightSent);

        Codec.writeVarInt32(buf, entries.size());

        for(PacketPlayOutChunkData packet : entries) {
            ChunkLocation location = packet.getChunkLocation();

            buf.writeInt(location.getX());
            buf.writeInt(location.getZ());
            buf.writeShort(packet.getBitmask());
        }

        for(PacketPlayOutChunkData packet : entries) {
            buf.writeBytes(packet.getData());
        }
    }
}
