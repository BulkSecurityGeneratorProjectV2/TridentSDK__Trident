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

package net.tridentsdk.server.netty.protocol;

import com.google.common.collect.Maps;
import net.tridentsdk.reflect.FastClass;
import net.tridentsdk.server.netty.packet.Packet;
import net.tridentsdk.server.netty.packet.PacketDirection;
import net.tridentsdk.server.netty.packet.UnknownPacket;
import net.tridentsdk.util.TridentLogger;

import java.util.Map;

abstract class ProtocolHandler {
    protected final Map<Integer, Class<? extends Packet>> inPackets = Maps.newHashMap();
    protected final Map<Integer, Class<? extends Packet>> outPackets = Maps.newHashMap();

    ProtocolHandler() {
        this.inPackets.put(-1, UnknownPacket.class);
        this.outPackets.put(-1, UnknownPacket.class);
    }

    public Packet packet(int id, PacketDirection type) {
        try {
            Map<Integer, Class<? extends Packet>> applicableMap;

            switch (type) {
                case IN:
                    applicableMap = this.inPackets;
                    break;

                case OUT:
                    applicableMap = this.outPackets;
                    break;

                default:
                    return null;
            }

            Class<?> cls = applicableMap.get(id);

            if (cls == null)
                cls = applicableMap.get(-1);

            FastClass fastClass = FastClass.get(cls);

            return fastClass.constructor().newInstance();
        } catch (Exception ex) {
            TridentLogger.get().error(new RuntimeException(ex.getMessage()));
            return null;
        }
    }
}