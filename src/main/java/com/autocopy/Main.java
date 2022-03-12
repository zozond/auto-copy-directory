package com.autocopy;

import com.autocopy.executor.WatcherExecutor;
import com.autocopy.parser.CommandLineConfigParser;

public class Main {

    public static void main(String[] args) {
        CommandLineConfigParser parser = new CommandLineConfigParser();
        parser.createParser(args);
        String src = parser.parseSourceDirectory();
        String dest = parser.parseDestinationDirectory();

        if(src != null || dest != null) {
            WatcherExecutor executor = new WatcherExecutor(src, dest);
            executor.activateWatcher();
        }
    }
}

