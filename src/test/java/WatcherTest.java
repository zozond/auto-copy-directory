import com.autocopy.dispatcher.FileDispatcher;
import com.autocopy.exception.FolderNotFoundException;
import com.autocopy.watcher.FileWatcher;
import com.autocopy.watcher.Watcher;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WatcherTest {

    private String sourceDirectory;
    private String targetDirectory;
    private Watcher watcher;
    private ExecutorService service;


    @BeforeEach
    public void setup() {
        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();

        service = Executors.newCachedThreadPool();

        sourceDirectory = directoryName + "\\src\\test\\resources\\src";
        targetDirectory = directoryName + "\\src\\test\\resources\\dest";
        try {
            watcher = new FileWatcher(sourceDirectory, new FileDispatcher(targetDirectory));
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        watcher.doWatching();
                    } catch (IOException ignore) {
                        // ignore
                    }
                }
            });
        } catch (FolderNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    @DisplayName("특정 폴더 및 파일 전부 삭제")
    public void teardown() throws IOException, InterruptedException {
        watcher.stop();
        deleteSourceDiretoryFiles();
        deleteTargetDiretoryFiles();
        Thread.sleep(500);
        service.shutdownNow();
    }

    private void deleteSourceDiretoryFiles() throws IOException {
        Files.walk(Path.of(sourceDirectory))
                .map(Path::toFile)
                .forEach((file)->{
                    if (file.isFile()){
                        file.delete();
                    }
                });
    }

    private void deleteTargetDiretoryFiles() throws IOException {
        Files.walk(Path.of(targetDirectory))
                .map(Path::toFile)
                .forEach((file)->{
                    if (file.isFile()){
                        file.delete();
                    }
                });
    }


    @Test
    @DisplayName("파일을 생성하고 그 다음 감지가 되었는지 확인한다")
    public void create_file_then_check() throws IOException {
        // given
        String filename = "test.txt";
        Path filePath = Paths.get(sourceDirectory, filename);
        Path newFilePath = Files.createFile(filePath);

        // then
        Assertions.assertEquals(filePath.toString(), newFilePath.toString());
        Assertions.assertEquals(true, Files.isReadable(newFilePath));
    }

    @Test
    @DisplayName("파일을 생성한 다음 변경하고 그 다음 감지가 되었는지 확인한다")
    public void modify_file_then_check() throws IOException, InterruptedException {
        // given
        String filename = "test.txt";
        Path filepath = Paths.get(sourceDirectory, filename);
        Path newFilePath = Files.createFile(filepath);
        Assertions.assertEquals(filepath.toString(), newFilePath.toString());
        Assertions.assertEquals(true, Files.isReadable(newFilePath));

        Files.writeString(filepath, "hello", StandardOpenOption.APPEND);
        Assertions.assertEquals("hello", Files.readAllLines(newFilePath).get(0));
    }

    @Test
    @DisplayName("파일을 생성한 다음 삭제하고 그 다음 감지가 되었는지 확인한다")
    public void delete_file_then_check() throws IOException, InterruptedException {
        // given
        String filename = "test.txt";
        Path filepath = Paths.get(sourceDirectory, filename);
        Path newFilePath = Files.createFile(filepath);
        Assertions.assertEquals(filepath.toString(), newFilePath.toString());
        Assertions.assertEquals(true, Files.isReadable(newFilePath));

        Files.delete(filepath);
        Assertions.assertEquals(false, Files.isReadable(newFilePath));
    }

    @Test
    @DisplayName("파일을 생성한 다음 변경하고, 삭제한 후 감지가 되었는지 확인한다")
    public void create_and_modify_after_delete_file_then_check() throws IOException {
        // given
        String filename = "test.txt";
        Path filepath = Paths.get(sourceDirectory, filename);
        Path newFilePath = Files.createFile(filepath);
        Assertions.assertEquals(filepath.toString(), newFilePath.toString());
        Assertions.assertEquals(true, Files.isReadable(newFilePath));

        Files.writeString(filepath, "hello", StandardOpenOption.APPEND);
        Assertions.assertEquals(true, Files.isReadable(newFilePath));
        Assertions.assertEquals("hello", Files.readAllLines(newFilePath).get(0));

        Files.delete(filepath);
        Assertions.assertEquals(false, Files.isReadable(newFilePath));
    }

    @Test
    public void stop_the_watcher() {
        watcher.stop();
        Assertions.assertEquals(true, watcher.isWatchingStopped());
    }
}
