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
package me.hsgamer.gamesinthebox;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * The permissions of the plugin
 */
public class Permissions {
    public static final Permission RELOAD = new Permission("gamesinthebox.reload", PermissionDefault.OP);
    public static final Permission SET_GAME = new Permission("gamesinthebox.setgame", PermissionDefault.OP);
    public static final Permission ACTION = new Permission("gamesinthebox.action", PermissionDefault.OP);
    public static final Permission FORCE_PICK = new Permission("gamesinthebox.forcepick", PermissionDefault.OP);
    public static final Permission END = new Permission("gamesinthebox.end", PermissionDefault.OP);
    public static final Permission EXPANSION = new Permission("gamesinthebox.expansion", PermissionDefault.OP);
    public static final Permission EDITOR = new Permission("gamesinthebox.editor", PermissionDefault.OP);

    private Permissions() {
        // EMPTY
    }
}
