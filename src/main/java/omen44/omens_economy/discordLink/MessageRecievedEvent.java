package omen44.omens_economy.discordLink;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import omen44.omens_economy.datamanager.ConfigTools;
import omen44.omens_economy.datamanager.MySQL;
import omen44.omens_economy.events.WhitelistUtils;
import omen44.omens_economy.utils.EconomyUtils;
import omen44.omens_economy.utils.SQLUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.HashMap;
import java.time.LocalTime;
import java.util.Objects;

public final class MessageRecievedEvent extends ListenerAdapter{
    WhitelistUtils wr = new WhitelistUtils();
    HashMap<String, LocalTime> unrCooldown = new HashMap<String, LocalTime>();
    HashMap<String, LocalTime> regCooldown = new HashMap<String, LocalTime>();
    HashMap<String, LocalTime> dayCooldown = new HashMap<String, LocalTime>();

    MySQL mySQL = new MySQL();
    Connection conn = mySQL.getConnection();
    SQLUtils utils = new SQLUtils(conn);
    EconomyUtils eco = new EconomyUtils();
    EmbedBuilder eb = new EmbedBuilder();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LocalTime localTime = LocalTime.now(); // Create a date object
        Message msg = event.getMessage();
        String[] message = msg.getContentDisplay().split(" ");
        MessageChannel channel = event.getChannel();
        String discordIGN = event.getAuthor().getName();
        FileConfiguration config = ConfigTools.getFileConfig("config.yml");

        eb.setTitle("Registration Bot");
        eb.setDescription("Registers your name to join the Server");
        eb.setColor(Color.blue);

        if (msg.getContentRaw().equals("!ping")) {
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                    .queue(response /* => Message */ -> {
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                    });
            return;
        }

        if (message[0].equals("!register")) {
            if (checkCooldown(discordIGN, "register")) {
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
                    regCooldown.put(discordIGN, cooldownTime);
                    return;
                } else {
                    eb.addField("Syntax for !register", "!register <minecraftUsername>", false);

                    channel.sendMessage(eb.build()).queue();
                }
            } else {
                eb.setTitle("Registration Bot");
                eb.setDescription("Registers your name to join the Server");
                eb.setColor(Color.blue);

                long delay = Duration.between(localTime, regCooldown.get(discordIGN)).toSeconds();

                int delayHours = (int) delay / 3600;
                int delayMinutes = (int) (delay % 3600) / 60;
                int delaySeconds = (int) delay % 60;

                eb.addField("On Cooldown!", String.format("%s:%s:%s", delayHours,delayMinutes,delaySeconds), false);

                channel.sendMessage(eb.build()).queue();
            }
        }

        if (message[0].equals("!unregister")) {
            if (checkCooldown(discordIGN, "unregister")) {
                if (message.length == 1) {
                    int result = wr.unregister(discordIGN);
                    channel.sendMessage("Initialising...").queue();

                    switch (result) {
                        case 1 -> channel.sendMessage("Error: SQL not connected").queue();
                        case 2 -> channel.sendMessage("Error: This account is not registered").queue();
                        case 3 -> channel.sendMessage("Unregister successful!").queue();
                        default -> throw new IllegalStateException("Unexpected value: " + result);
                    }
                    LocalTime cooldownTime = localTime.plusHours(12L);
                    unrCooldown.put(discordIGN, cooldownTime);
                } else {

                    

                    eb.addField("Syntax for !unregister", "!unregister", false);

                    channel.sendMessage(eb.build()).queue();
                }
            } else {
                long delay = Duration.between(localTime, unrCooldown.get(discordIGN)).toSeconds();

                int delayHours = (int) delay / 3600;
                int delayMinutes = (int) (delay % 3600) / 60;
                int delaySeconds = (int) delay % 60;

                eb.addField("On Cooldown!", String.format("%s:%s:%s", delayHours,delayMinutes,delaySeconds), false);

                channel.sendMessage(eb.build()).queue();
            }
        }

        if (message[0].equals("!status")) { // should display the registration data
            if (message.length == 1) {
                channel.sendMessage("Initialising...").queue();

                String dbDiscord = utils.getDBString("discordIGN", "discordIGN", discordIGN, "accounts");
                if (dbDiscord.equals("")) {
                    dbDiscord = "unregistered";
                }

                String dbMinecraft = utils.getDBString("minecraftIGN", "discordIGN", discordIGN, "accounts");
                if (dbMinecraft.equals("")) {
                    dbMinecraft = "unregistered";
                }

                // create embeds
                eb.addField("Discord username", dbDiscord, false);
                eb.addField("Minecraft username", dbMinecraft, false);

                channel.sendMessage(eb.build()).queue();
            } else if (message.length == 2) {
                String mcCalled = message[1];
                channel.sendMessage("Initialising...").queue();

                //create an embed
                String dbDiscord = utils.getDBString("discordIGN", "discordIGN", mcCalled, "accounts");
                String dbMinecraft = utils.getDBString("minecraftIGN", "discordIGN", mcCalled, "accounts");
                eb.addField("Discord username", dbDiscord, false);
                eb.addField("Minecraft username", dbMinecraft, false);

                channel.sendMessage(eb.build()).queue();
            } else {
                // create embed
                eb.addField("Syntax for !status", "!status (username)", false);

                channel.sendMessage(eb.build()).queue();
            }
        }

        if (message[0].equals("!daily")) { // allows a daily command for the usernames
            if (checkCooldown(discordIGN, "daily")) {
                String dbMinecraft = utils.getDBString("minecraftIGN", "discordIGN", discordIGN, "accounts");
                Player player = Bukkit.getPlayer(dbMinecraft);
                int wallet;

                if (player != null) {
                    wallet = eco.getMoney(player, "wallet");
                    wallet += 3285;
                    eco.setWallet(player, wallet);

                    eb.addField("Total wallet", "Total wallet: " + config.getString("money.moneySymbol") + wallet, false);

                    channel.sendMessage(eb.build()).queue();
                }
            } else {
                long delay = Duration.between(localTime, dayCooldown.get(discordIGN)).toSeconds();

                int delayHours = (int) delay / 3600;
                int delayMinutes = (int) (delay % 3600) / 60;
                int delaySeconds = (int) delay % 60;

                eb.addField("On Cooldown!", String.format("%s:%s:%s", delayHours,delayMinutes,delaySeconds), false);

                channel.sendMessage(eb.build()).queue();
            }
        }
    }


    public boolean checkCooldown(String user, String commandName) { // return true if cooldown is passed, false if not
        LocalTime timeNow = LocalTime.now(); // Create a date object
        LocalTime timeCalled;
        HashMap<String, LocalTime> hashMap;
        long delay;
        switch (commandName) {
            case "register" -> {
                delay = 43200;
                hashMap = regCooldown;
                timeCalled = regCooldown.get(user);
            }
            case "unregister" -> {
                delay = 43200;
                hashMap = unrCooldown;
                timeCalled = unrCooldown.get(user);
            }
            case "daily" -> {
                delay = 86400;
                hashMap = dayCooldown;
                timeCalled = dayCooldown.get(user);
            }
            default -> {
                System.out.println("Error, invalid command called");
                return false;
            }
        }

        if (timeCalled == null) {
            return true; // they aren't on the hashmap. that is, they haven't called the command at all
        }
        if (Duration.between(timeNow, timeCalled).minusSeconds(delay).isZero() || Duration.between(timeNow, timeCalled).minusSeconds(delay).isNegative()) {
            hashMap.remove(user, timeCalled);
            return true;
        } else {
            return false; // the current time is after the cooldown time, means the cooldown is over
        }
    }
}
