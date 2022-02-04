package io.github.omen44.indroEconomy.utils.sqlite;

import io.github.omen44.indroEconomy.IndroEconomy;

import java.util.logging.Level;

public class Error {
    public static void execute(IndroEconomy plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(IndroEconomy plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}

