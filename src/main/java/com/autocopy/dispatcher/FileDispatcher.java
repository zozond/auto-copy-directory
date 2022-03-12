package com.autocopy.dispatcher;

import com.autocopy.entity.DispatchEvent;
import com.autocopy.entity.EventType;
import com.autocopy.exception.FolderNotFoundException;
import com.autocopy.util.Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FileDispatcher implements Dispatcher {

    public String dirpath;

    public FileDispatcher(String dirpath) throws FolderNotFoundException {
        Utility.checkDirectory(dirpath);
        this.dirpath = dirpath;
    }

    @Override
    public void dispatch(DispatchEvent event) {
        EventType type = event.getEventType();
        String filepath = event.getPath();
        String filename = event.getName();

        manageFileEvents(type, filepath, filename);
    }

    private void manageFileEvents(EventType type, String filepath, String filename){
        try{
            switch (type) {
                case CREATE:
                    /* do nothing */
                    break;
                case DELETE:
                    Files.deleteIfExists(Path.of(dirpath, filename));
                    break;
                case MODIFY:
                    Files.copy(Path.of(filepath), Path.of(dirpath, filename), StandardCopyOption.REPLACE_EXISTING);
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
