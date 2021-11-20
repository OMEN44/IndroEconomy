package omen44.omens_economy.discordLink;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import omen44.omens_economy.events.WhitelistRegister;

import java.util.Locale;

public class CommandRegister extends ListenerAdapter {
    WhitelistRegister wr = new WhitelistRegister();
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");

        String discordIGN = event.getAuthor().toString();
        String minecraftIGN = message[1];
        String result = wr.register(discordIGN, minecraftIGN);
        switch (result.toLowerCase(Locale.ROOT)) {
            case "valid": {
                event.getChannel().sendMessage("Successfully registered your name, your account id is " + );
            }
            case "" {

            }
            case "" {

            }
            case "" {

            }
            case ""

        }
    }
}
