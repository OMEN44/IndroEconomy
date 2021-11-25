package omen44.omens_economy.discordLink;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import omen44.omens_economy.datamanager.MySQL;

import javax.security.auth.login.LoginException;
import java.sql.Connection;

public class Bot extends ListenerAdapter {
    Connection connection;
    MySQL mySQL = new MySQL();
    static JDA jda;
    public static void main(String[] args) throws LoginException
    {
        if (args.length < 1) {
            System.out.println("You have to provide a token as first argument!");
            System.exit(1);
        }
        // args[0] should be the token
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        jda = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(new Bot())
                .setActivity(Activity.playing("Type !register"))
                .build();

        jda.addEventListener(new MessageRecievedEvent());
    }

    public void shutdownBot() {
        System.out.println("Disconnecting the Discord Bot...");
        jda.shutdownNow();
    }
}

