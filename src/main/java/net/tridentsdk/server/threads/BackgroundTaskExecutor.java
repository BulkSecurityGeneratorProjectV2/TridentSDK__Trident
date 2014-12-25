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

package net.tridentsdk.server.threads;

import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executes background tasks that don't matter too much to the actual server
 *
 * @author The TridentSDK Team
 */
@ThreadSafe public final class BackgroundTaskExecutor {
    static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    private BackgroundTaskExecutor() {
    }

    /**
     * Execute the task in the internal thread pool <p/> <p>Synchronization is a requirement</p>
     *
     * @param runnable the task to reflect
     */
    public static void execute(Runnable runnable) {
        SERVICE.execute(runnable);
    }
}
