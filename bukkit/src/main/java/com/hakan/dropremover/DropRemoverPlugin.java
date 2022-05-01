package com.hakan.dropremover;

import com.hakan.core.HCore;
import com.hakan.dropremover.hooks.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class DropRemoverPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        HCore.initialize(this);
        Metrics.initialize(this);
        DropRemoverHandler.initialize(this);
    }
}