import com.autocopy.exception.FolderNotFoundException;
import com.autocopy.util.Utility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class UtilityTest {
    private String rootDir;

    @BeforeEach
    public void setup(){
        Path path = FileSystems.getDefault().getPath("");
        rootDir = path.toAbsolutePath().toString();
    }

    @Test
    public void checkDirectory_if_exist_test() {
        String directory = rootDir + "\\src\\test\\resources";
        try{
            Utility.checkDirectory(directory);
            Assertions.assertEquals(true, true);
        }catch (FolderNotFoundException e){
            Assertions.assertEquals(true, false);
        }

    }

    @Test
    public void checkDirectory_if_not_exist_test() {
        String directory = rootDir + "\\src\\test\\resources1";
        try{
            Utility.checkDirectory(directory);
        }catch (FolderNotFoundException e){
            Assertions.assertEquals(true, true);
        }
    }
}
