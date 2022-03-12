import com.autocopy.dispatcher.Dispatcher;
import com.autocopy.dispatcher.FileDispatcher;
import com.autocopy.entity.FileEvent;
import com.autocopy.entity.EventType;
import com.autocopy.exception.FolderNotFoundException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DispatcherTest {
    private String targetDirectory;
    private String sourceDirectory;
    private Dispatcher dispatcher;

    @BeforeEach
    public void setup() {
        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();

        targetDirectory = directoryName + "\\src\\test\\resources\\dest";
        sourceDirectory = directoryName + "\\src\\test\\resources\\src";
        try {
            dispatcher = new FileDispatcher(targetDirectory);
        } catch (FolderNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    @DisplayName("특정 폴더 및 파일 전부 삭제")
    public void teardown() throws IOException {
        deleteTargetDiretoryFiles();
        deleteSourceDiretoryFiles();
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

    private int getNumberOfFilesInDirectory(String directory) {
        File f = new File(directory);
        if (f.isDirectory()){
            return f.list().length;
        }else {
            return -1;
        }
    }

    @Test
    @DisplayName("파일이 생성 되었다면, 아무것도 안함")
    public void file_created() throws IOException, InterruptedException {
        // given
        String filename = "test.txt";
        Path filepath = Paths.get(sourceDirectory, filename);
        Files.createFile(filepath);

        dispatcher.dispatch(new FileEvent(EventType.CREATE, filepath.toString(), filename));

        Thread.sleep(500);
        Assertions.assertEquals(0, getNumberOfFilesInDirectory(targetDirectory));
    }

    @Test
    @DisplayName("파일이 변경되었다면, dest 폴더로 이동시킴")
    public void file_modifed() throws IOException, InterruptedException {
        // given
        String filename = "test.txt";
        Path filepath = Paths.get(sourceDirectory, filename);
        Files.createFile(filepath);

        dispatcher.dispatch(new FileEvent(EventType.MODIFY, filepath.toString(), filename));

        Thread.sleep(1000);
        Assertions.assertEquals(1, getNumberOfFilesInDirectory(targetDirectory));
    }


    @Test
    @DisplayName("파일이 변경 된 후, 삭제 되었다면, dest 폴더에서도 삭제")
    public void file_modified_and_then_delete() throws IOException, InterruptedException {
        // given
        String filename = "test.txt";
        Path filepath = Paths.get(sourceDirectory, filename);
        Files.createFile(filepath);

        dispatcher.dispatch(new FileEvent(EventType.MODIFY, filepath.toString(), filename));
        Thread.sleep(500);
        Assertions.assertEquals(1, getNumberOfFilesInDirectory(targetDirectory));

        deleteSourceDiretoryFiles();
        dispatcher.dispatch(new FileEvent(EventType.DELETE, filepath.toString(), filename));
        Thread.sleep(500);
        Assertions.assertEquals(0, getNumberOfFilesInDirectory(targetDirectory));
    }

}
