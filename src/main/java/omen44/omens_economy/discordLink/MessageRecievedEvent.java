package omen44.omens_economy.discordLink;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import omen44.omens_economy.events.WhitelistRegister;

import java.util.Arrays;

public class MessageRecievedEvent extends ListenerAdapter {
    WhitelistRegister wr = new WhitelistRegister();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String[] message = msg.getContentDisplay().split(" ");

        if (msg.getContentRaw().equals("!ping")) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
        }

        System.out.println(Arrays.toString(message));
        if (message[0].equals("!register") && message.length == 2) {
            String discordIGN = event.getAuthor().toString();
            String minecraftIGN = message[1];
            MessageChannel channel = event.getChannel();

            channel.sendMessage(minecraftIGN).queue();
            int result = wr.register(discordIGN, minecraftIGN);
            channel.sendMessage("initialising...").queue();
            switch (result) {
                case 0 -> {
                    int accountID = wr.getPlayerID();
                    channel.sendMessage("Successfully registered your name, your account id is " + accountID).queue();
                }
                case 1 -> channel.sendMessage("Error: discord name already registered").queue();
                case 2 -> channel.sendMessage("Error: minecraft verification servers down").queue();
                case 3 -> channel.sendMessage("Error; minecraft username not paid for").queue();
                case 4 -> channel.sendMessage("Error: minecraft name already registered").queue();
                case 5 -> channel.sendMessage("Error: SQL not connected").queue();
                default -> throw new IllegalStateException("Unexpected value: " + result);
            }
        }
    }
}
