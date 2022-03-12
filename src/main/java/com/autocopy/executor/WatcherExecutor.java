package com.autocopy.executor;

import com.autocopy.dispatcher.Dispatcher;
import com.autocopy.dispatcher.FileDispatcher;
import com.autocopy.exception.FolderNotFoundException;
import com.autocopy.watcher.FileWatcher;
import com.autocopy.watcher.Watcher;

import java.io.IOException;

public class WatcherExecutor {
    private String src;
    private String dest;
    private Watcher watcher;
    private Dispatcher dispatcher;

    public WatcherExecutor(String src, String dest){
        this.src = src;
        this.dest = dest;
        try {
            this.dispatcher = new FileDispatcher(dest);
            this.watcher = new FileWatcher(src, dispatcher);
        } catch (FolderNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void activateWatcher(){
        try {
            watcher.doWatching();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate(){
        watcher.stop();
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public Watcher getWatcher() {
        return watcher;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }
}
