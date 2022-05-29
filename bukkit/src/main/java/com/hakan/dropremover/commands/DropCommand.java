package com.hakan.dropremover.commands;

import com.hakan.core.command.HCommandAdapter;
import com.hakan.core.command.executors.base.BaseCommand;
import com.hakan.core.command.executors.sub.SubCommand;
import com.hakan.dropremover.DropHandler;
import com.hakan.dropremover.configuration.DropRemoverConfiguration;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;

@BaseCommand(
        name = "hdropremover",
        aliases = {"dropremover", "hdrops", "drops"},
        usage = "/hdropremover <reload>"
)
public class DropCommand implements HCommandAdapter {

    @SubCommand(
            args = "reload",
            permission = "hdropremover.reload",
            permissionMessage = "§cYou don't have permission to reload the configuration!"
    )
    public void execute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        DropRemoverConfiguration.getConfigurations().values()
                .forEach(DropRemoverConfiguration::reload);
        DropHandler.loadValues();
        DropHandler.loadItems();

        sender.sendMessage("has been reloaded.");
    }
}