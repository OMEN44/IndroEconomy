package omen44.omens_economy.discordLink;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import omen44.omens_economy.Main;
import omen44.omens_economy.events.WhitelistRegister;

import java.util.Locale;

public class CommandRegister extends ListenerAdapter {
    public Main main;
    public CommandRegister(Main main) {
        this.main = main;
    }
    WhitelistRegister wr = new WhitelistRegister(main);
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" ");

        String discordIGN = event.getAuthor().toString();
        String minecraftIGN = message[1];
        String result = wr.register(discordIGN, minecraftIGN);
        switch (result.toLowerCase(Locale.ROOT)) {
            case "valid": {
                int accountID = wr.getPlayerID();
                event.getChannel().sendMessage("Successfully registered your name, your account id is " + accountID);
            }
            case "E-DNAR": {
                event.getChannel().sendMessage("Error: discord name already registered");

            }
            case "E-MCSD": {
                event.getChannel().sendMessage("Error: minecraft verification servers down");
            }
            case "E-MCNP": {
                event.getChannel().sendMessage("Error; minecraft username not paid for");
            }
            case "E-MCAR": {
                event.getChannel().sendMessage("Error: minecraft name already registered");
            }
            default:
                throw new IllegalStateException("Unexpected value: " + result.toLowerCase(Locale.ROOT));
        }
    }
}
