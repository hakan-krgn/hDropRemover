package com.hakan.dropremover;

import com.hakan.dropremover.hooks.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class DropRemoverPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Metrics.initialize(this);
        DropRemoverHandler.initialize(this);
    }
}