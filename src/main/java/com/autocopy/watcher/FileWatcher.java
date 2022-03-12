package com.autocopy.watcher;

import com.autocopy.dispatcher.Dispatcher;
import com.autocopy.entity.EventType;
import com.autocopy.entity.FileEvent;
import com.autocopy.exception.FolderNotFoundException;
import com.autocopy.util.Utility;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class FileWatcher implements Watcher{
    public String dirpath;
    private boolean isStop = true;
    private Dispatcher dispatcher;

    public FileWatcher(String dirpath, Dispatcher dispatcher) throws FolderNotFoundException {
        Utility.checkDirectory(dirpath);

        this.dirpath = dirpath;
        this.dispatcher = dispatcher;
    }
    @Override
    public void stop() {
        isStop = true;
    }

    private void start(){
        isStop = false;
    }

    @Override
    public boolean isWatchingStopped() {
        return isStop;
    }

    @Override
    public void doWatching() throws IOException {
        System.out.println("Watch 시작합니다.");
        start();

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(dirpath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            iterateUntilTheWatchService(watchService);
        }
    }

    private void processDispatcherEvent(WatchEvent.Kind kind, String filepath, String filename){
        if(dispatcher == null){
            return;
        }

        if (kind.equals(StandardWatchEventKinds.ENTRY_CREATE)){
            dispatcher.dispatch(new FileEvent(EventType.CREATE, filepath, filename));
            System.out.println(filepath + " 에서 파일 생성 이벤트가 감지되었습니다.");
        }else if (kind.equals(StandardWatchEventKinds.ENTRY_DELETE)){
            dispatcher.dispatch(new FileEvent(EventType.DELETE, filepath, filename));
            System.out.println(filepath + " 에서 파일 삭제 이벤트가 감지되었습니다.");
        }else if (kind.equals(StandardWatchEventKinds.ENTRY_MODIFY)){
            dispatcher.dispatch(new FileEvent(EventType.MODIFY, filepath, filename));
            System.out.println(filepath + " 에서 파일 변경 이벤트가 감지되었습니다.");
        }else if(kind.equals(StandardWatchEventKinds.OVERFLOW)){
            // 운영체제에서 이벤트가 소실되었거나 버려질 경우 실행
            System.out.println(filepath + " 에서 오버플로우 이벤트가 감지되었습니다.");
        }
    }

    private void detectWatchEvent(WatchKey watchKey) {
        List<WatchEvent<?>> watchEventList = watchKey.pollEvents();
        for (WatchEvent<?> watchEvent : watchEventList){
            WatchEvent.Kind kind = watchEvent.kind();
            Path eventPath = (Path) watchEvent.context();
            String filepath = Path.of(dirpath, String.valueOf(eventPath)).toString();
            processDispatcherEvent(kind, filepath, String.valueOf(eventPath));
        }
    }



    private void iterateUntilTheWatchService(WatchService watchService){
        // 감지
        while(isAlive()){
            WatchKey watchKey = null;
            try{
                watchKey = watchService.take();
            }catch (InterruptedException ignore){
                /* ignore */
            }

            detectWatchEvent(watchKey);
            if(!watchKey.reset()) break;
        }
    }

    private boolean isAlive(){
        return !isStop;
    }
}
