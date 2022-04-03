package com.hakan.dropremover;

import com.hakan.dropremover.hooks.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class DropRemoverPlugin extends JavaPlugin {

    private static DropRemoverPlugin INSTANCE;

    public static DropRemoverPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        Metrics.initialize(this);
        DropRemoverHandler.initialize(this);
    }
}