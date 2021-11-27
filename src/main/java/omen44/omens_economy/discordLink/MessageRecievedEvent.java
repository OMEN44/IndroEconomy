package omen44.omens_economy.discordLink;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.events.WhitelistUtils;
import omen44.omens_economy.utils.SQLUtils;

import java.awt.*;
import java.sql.Connection;
import java.time.Duration;
import java.util.HashMap;
import java.time.LocalTime;

public class MessageRecievedEvent extends ListenerAdapter {
    WhitelistUtils wr = new WhitelistUtils();
    HashMap<String, LocalTime> cooldown = new HashMap<String, LocalTime>();

    MySQL mySQL = new MySQL();
    Connection conn = mySQL.getConnection();
    SQLUtils utils = new SQLUtils(conn);


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LocalTime localTime = LocalTime.now(); // Create a date object
        Message msg = event.getMessage();
        String[] message = msg.getContentDisplay().split(" ");
        MessageChannel channel = event.getChannel();
        String discordIGN = event.getAuthor().toString();
        EmbedBuilder eb = new EmbedBuilder();


        if (msg.getContentRaw().equals("!ping")) {
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
            return;
        }

        if (message[0].equals("!register")) {
            if (message.length == 2) {
                String minecraftIGN = message[1];

                channel.sendMessage(minecraftIGN).queue();
                int result = wr.register(discordIGN, minecraftIGN);
                channel.sendMessage("initialising...").queue();
                switch (result) {
                    case 1 -> channel.sendMessage("Error: SQL not connected").queue();
                    case 2 -> channel.sendMessage("Error: discord name already registered").queue();
                    case 3 -> channel.sendMessage("Error: minecraft name already registered").queue();
                    case 4 -> channel.sendMessage("Successfully registered your username").queue();
                    default -> throw new IllegalStateException("Unexpected value: " + result);
                }
                LocalTime cooldownTime = localTime.plusHours(12L);
                cooldown.put(discordIGN, cooldownTime);
                return;
            } else {

                eb.setTitle("Registration Bot");
                eb.setDescription("Registers your name to join the Server");
                eb.setColor(Color.blue);

                eb.addField("Syntax for !register", "!register <minecraftUsername>", false);

                channel.sendMessage(eb.build()).queue();
            }
        }

        if (message[0].equals("!unregister")) {
            if (message.length == 1) {

                int result = wr.unregister(discordIGN);
                channel.sendMessage("Initialising...").queue();
                switch (result) {
                    case 0 -> channel.sendMessage("Unregister successful!").queue();
                    case 1 -> channel.sendMessage("Error: SQL not connected").queue();
                    case 2 -> channel.sendMessage("Error: Discord User not registered").queue();
                    case 3 -> channel.sendMessage("Error: Minecraft Username not registered").queue();
                    default -> throw new IllegalStateException("Unexpected value: " + result);
                }
                LocalTime cooldownTime = localTime.plusHours(12L);
                cooldown.put(discordIGN, cooldownTime);
            } else {

                eb.setTitle("Registration Bot");
                eb.setDescription("Registers your name to join the Server");
                eb.setColor(Color.blue);

                eb.addField("Syntax for !unregister", "!unregister", false);

                channel.sendMessage(eb.build()).queue();
            }
        }

        if (message[0].equals("!status")) { // should display the
            if (message.length == 1) {
                channel.sendMessage("Initialising...").queue();

                //create an embed
                eb.setTitle("Registration Bot");
                eb.setDescription("Registers your name to join the Server");
                eb.setColor(Color.blue);

                String dbDiscord = utils.getDBString("discordIGN", "discordIGN", discordIGN, "accounts");
                if (dbDiscord.equals("")) {
                    dbDiscord = "unregistered";
                }

                String dbMinecraft = utils.getDBString("minecraftIGN", "discordIGN", discordIGN, "accounts");
                if (dbMinecraft.equals("")) {
                    dbMinecraft = "unregistered";
                }

                eb.addField("Discord username", dbDiscord, false);
                eb.addField("Minecraft username", dbMinecraft, false);

                channel.sendMessage(eb.build()).queue();
            } else if (message.length == 2) {
                String mcCalled = message[1];
                channel.sendMessage("Initialising...").queue();

                //create an embed
                eb.setTitle("Registration Bot");
                eb.setDescription("Registers your name to join the Server");
                eb.setColor(Color.blue);

                String dbDiscord = utils.getDBString("discordIGN", "discordIGN", mcCalled, "accounts");
                String dbMinecraft = utils.getDBString("minecraftIGN", "discordIGN", mcCalled, "accounts");
                eb.addField("Discord username", dbDiscord, false);
                eb.addField("Minecraft username", dbMinecraft, false);

                channel.sendMessage(eb.build()).queue();
            } else {
                eb.setTitle("Registration Bot");
                eb.setDescription("Registers your name to join the Server");
                eb.setColor(Color.blue);

                eb.addField("Syntax for !status", "!status (username)", false);

                channel.sendMessage(eb.build()).queue();
            }
         }
    }
}
        /*
        else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Registration Bot");
            eb.setDescription("Registers your name to join the Server");
            eb.setColor(Color.blue);

            Duration duration = Duration.between(localTime, cooldown.get(discordIGN)).truncatedTo(ChronoUnit.SECONDS);

            int time = duration.toSecondsPart();

            eb.addField("On Cooldown!", Integer.toString(time), false);

            channel.sendMessage(eb.build()).queue();
        }
    }
    public boolean checkCooldown(String user) { // return true if cooldown is passed, false if not
        LocalTime timeNow = LocalTime.now(); // Create a date object
        LocalTime timeCalled = cooldown.get(user);

        if (timeCalled == null) {
            return true; // they aren't on the hashmap. that is, they haven't called the command at all
        }
        if (Duration.between(timeNow, timeCalled).isNegative() || Duration.between(timeNow, timeCalled).isZero()) {
            cooldown.remove(user, timeCalled);
            return true;
        } else {
            return false; // the current time is after the cooldown time, means the cooldown is over
        }
    }
    */
