package com.autocopy.watcher;

import java.io.IOException;

public interface Watcher {
    void stop();
    boolean isWatchingStopped();
    void doWatching() throws IOException;
}
