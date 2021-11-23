package omen44.omens_economy.discordLink;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import omen44.omens_economy.Main;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {
    private static Main plugin;
    public Bot(Main plugin) {
        this.plugin = plugin;
    }
    public static void main(String[] args) throws LoginException
    {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }
        // args[0] should be the token
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        EventListener register = new CommandRegister(plugin);
        JDA jda = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Bot(null))
                .setActivity(Activity.playing("Type !register"))
                .build();

        jda.addEventListener(new CommandPing());
        jda.addEventListener();
    }
}

