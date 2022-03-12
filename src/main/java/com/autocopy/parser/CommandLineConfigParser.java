package com.autocopy.parser;

import org.apache.commons.cli.*;

public class CommandLineConfigParser {
    private CommandLine commandLine;

    public CommandLine getCommandLine(){
        return commandLine;
    }

    public void createParser(String[] args){
        Options options = new Options();
        options.addOption("src", "source",true, "input source directory path");
        options.addOption("dest", "destination",true, "input destination directory path");

        CommandLineParser parser = new DefaultParser();

        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String parseSourceDirectory(){
        String result = null;
        if(commandLine.hasOption("src") || commandLine.hasOption("source")) {
            result = commandLine.getOptionValue("src");
        }
        return result;
    }

    public String parseDestinationDirectory(){
        String result = null;
        if(commandLine.hasOption("dest") || commandLine.hasOption("destination")){
            result = commandLine.getOptionValue("dest");
        }
        return result;
    }

}
