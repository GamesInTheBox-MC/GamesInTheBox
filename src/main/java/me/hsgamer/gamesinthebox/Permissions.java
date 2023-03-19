package me.hsgamer.gamesinthebox;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Permissions {
    public static final Permission RELOAD = new Permission("gamesinthebox.reload", PermissionDefault.OP);
    public static final Permission SET_GAME = new Permission("gamesinthebox.setgame", PermissionDefault.OP);
    public static final Permission ACTION = new Permission("gamesinthebox.action", PermissionDefault.OP);

    private Permissions() {
        // EMPTY
    }
}
