package com.hakan.dropremover.commands;

import com.hakan.core.command.HCommandExecutor;
import com.hakan.dropremover.DropRemoverHandler;
import com.hakan.dropremover.configuration.DropRemoverConfiguration;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

public class DropCommand extends HCommandExecutor {

    public DropCommand(@Nonnull String command, @Nonnull String... aliases) {
        super(command, "hdropremover.reload", aliases);
        super.subCommand("reload");
        super.subCommand("yenile");
    }

    @Override
    public void onCommand(@Nonnull CommandSender sender, @Nonnull String[] args) {
        if (args.length == 1) {
            if (args[0].equals("reload") || args[0].equals("yenile")) {
                DropRemoverConfiguration.CONFIG.reload();
                DropRemoverHandler.loadValues();
                sender.sendMessage("has been reloaded.");
            }
        }
    }
}