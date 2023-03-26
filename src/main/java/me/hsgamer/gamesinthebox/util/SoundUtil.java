/*
   Copyright 2023-2023 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.gamesinthebox.util;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * The sound utility
 */
public final class SoundUtil {
    private static final Map<String, XSound.Record> recordMap = new ConcurrentHashMap<>();

    private SoundUtil() {
        // EMPTY
    }

    /**
     * Do the sound action
     *
     * @param sound               the sound
     * @param soundRecordConsumer the sound record consumer
     */
    public static void doSoundAction(String sound, Consumer<XSound.Record> soundRecordConsumer) {
        if (sound == null || sound.isEmpty()) {
            return;
        }

        XSound.Record soundRecord = recordMap.computeIfAbsent(sound, XSound::parse);
        if (soundRecord == null) {
            return;
        }

        XSound.Record clonedSoundRecord = soundRecord.clone();
        soundRecordConsumer.accept(clonedSoundRecord);
    }

    /**
     * Play the sound
     *
     * @param sound  the sound
     * @param player the player
     */
    public static void playSound(String sound, Player player) {
        doSoundAction(sound, soundRecord -> {
            soundRecord.forPlayer(player);
            soundRecord.play();
        });
    }

    /**
     * Play the sound
     *
     * @param sound    the sound
     * @param location the location
     */
    public static void playSound(String sound, Location location) {
        doSoundAction(sound, soundRecord -> {
            soundRecord.atLocation(location);
            soundRecord.play();
        });
    }

    /**
     * Play the sound
     *
     * @param sound    the sound
     * @param player   the player
     * @param location the location
     */
    public static void playSound(String sound, Player player, Location location) {
        doSoundAction(sound, soundRecord -> {
            soundRecord.forPlayerAtLocation(player, location);
            soundRecord.play();
        });
    }
}
