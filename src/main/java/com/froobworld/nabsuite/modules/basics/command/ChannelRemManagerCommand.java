package com.froobworld.nabsuite.modules.basics.command;

import cloud.commandframework.Command;
import cloud.commandframework.context.CommandContext;
import com.froobworld.nabsuite.command.NabCommand;
import com.froobworld.nabsuite.command.argument.predicate.ArgumentPredicate;
import com.froobworld.nabsuite.modules.basics.BasicsModule;
import com.froobworld.nabsuite.modules.basics.channel.ChatChannel;
import com.froobworld.nabsuite.modules.basics.channel.ChatChannelManager;
import com.froobworld.nabsuite.modules.basics.command.argument.ChatChannelArgument;
import com.froobworld.nabsuite.modules.protect.command.argument.UserArgument;
import com.froobworld.nabsuite.user.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChannelRemManagerCommand extends NabCommand {
    private final BasicsModule basicsModule;

    public ChannelRemManagerCommand(BasicsModule basicsModule) {
        super(
                "remmanager",
                "Remove a manager from a channel.",
                "nabsuite.command.channel.remmanager",
                CommandSender.class
        );
        this.basicsModule = basicsModule;
    }

    @Override
    public void execute(CommandContext<CommandSender> context) {
        User user = context.get("user");
        ChatChannel channel = context.get("channel");
        channel.removeManager(user);
        context.getSender().sendMessage(Component.text(
                "Removed manager from channel.", NamedTextColor.YELLOW
        ));
    }

    @Override
    public Command.Builder<CommandSender> populateBuilder(Command.Builder<CommandSender> builder) {
        return builder
                .argument(new ChatChannelArgument<>(
                        true,
                        "channel",
                        basicsModule.getChatChannelManager(),
                        new ArgumentPredicate<>(
                                false,
                                (context, channel) -> {
                                    if (context.getSender().hasPermission(ChatChannelManager.EDIT_ALL_CHANNELS_PERMISSION)) {
                                        return true;
                                    } else if (context.getSender() instanceof Player) {
                                        return channel.isOwner((Player) context.getSender());
                                    }
                                    return true;
                                },
                                "You don't have permission to remove managers from this channel."
                        )
                ))
                .argument(new UserArgument<>(
                        true,
                        "user",
                        basicsModule.getPlugin(),
                        true,
                        new ArgumentPredicate<>(
                                false,
                                (context, user) -> {
                                    ChatChannel channel = context.get("channel");
                                    return channel.isManager(user);
                                },
                                "That user is not a manger of this channel."
                        )
                ));
    }

    @Override
    public String getUsage() {
        return "/channel remmanager <channel> <user>";
    }
}