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
import net.tridentsdk.base.Position;
import net.tridentsdk.server.data.PositionWritable;
import net.tridentsdk.server.netty.Codec;
import net.tridentsdk.server.netty.packet.OutPacket;

public class PacketPlayOutSpawnPainting extends OutPacket {
    protected int entityId;
    protected String title;
    protected Position location;
    protected short direction;

    @Override
    public int id() {
        return 0x10;
    }

    public int entityId() {
        return this.entityId;
    }

    public String title() {
        return this.title;
    }

    public Position location() {
        return this.location;
    }

    public short paintingDirection() {
        return this.direction;
    }

    @Override
    public void encode(ByteBuf buf) {
        Codec.writeVarInt32(buf, this.entityId);
        Codec.writeString(buf, this.title);

        new PositionWritable(this.location).write(buf);

        buf.writeByte((int) this.direction);
    }
}
