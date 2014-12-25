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

package net.tridentsdk.server.window;

import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import net.tridentsdk.window.Window;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collection;
import java.util.Map;

/**
 * Manages the inventory windows on the server, whether being viewed or not
 *
 * @author The TridentSDK Team
 */
@ThreadSafe public class WindowManager {
    private static final Map<Integer, TridentWindow> windows = new ConcurrentHashMapV8<>();

    /**
     * Gets a window by its ID
     *
     * @param id the ID of a window
     * @return the window with the ID, or {@code null} if it doesn't exist
     */
    public Window getWindow(int id) {
        return windows.get(id);
    }

    /**
     * Registers the window with the manager
     *
     * @param window the window to be registered
     */
    public void registerWindow(TridentWindow window) {
        windows.put(window.getId(), window);
    }

    /**
     * Gets all registered windows with the manager
     *
     * @return the windows registered
     */
    public Collection<TridentWindow> getWindows() {
        return windows.values();
    }
}
