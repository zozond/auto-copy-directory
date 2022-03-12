import com.autocopy.executor.WatcherExecutor;
import com.autocopy.watcher.Watcher;
import org.junit.jupiter.api.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatcherExecutorTest {

    private String sourceDirectory;
    private String targetDirectory;
    private ExecutorService service;
    private WatcherExecutor executor;

    @BeforeEach
    public void setup(){
        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();

        service = Executors.newCachedThreadPool();

        sourceDirectory = directoryName + "\\src\\test\\resources\\src";
        targetDirectory = directoryName + "\\src\\test\\resources\\dest";
        executor = new WatcherExecutor(sourceDirectory, targetDirectory);

    }

    @AfterEach
    @DisplayName("특정 폴더 및 파일 전부 삭제")
    public void teardown() {
        executor.terminate();
        service.shutdownNow();
    }

    @Test
    @DisplayName("watcher와 dispatcher가 생성 및 실행 되었는지 확인, 그리고 각 src, dest 디렉토리명을 가져온다")
    public void activateWatcherTest() throws InterruptedException {
        service.submit(new Runnable() {
            @Override
            public void run() {
                executor.activateWatcher();
            }
        });

        Thread.sleep(1000);
        Watcher watcher = executor.getWatcher();

        Assertions.assertNotNull(watcher);
        Assertions.assertEquals(false, watcher.isWatchingStopped());

        Assertions.assertNotNull(executor.getDispatcher());

        Assertions.assertEquals(sourceDirectory, executor.getSrc());
        Assertions.assertEquals(targetDirectory, executor.getDest());
    }
}
