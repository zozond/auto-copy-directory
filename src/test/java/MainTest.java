import com.autocopy.Main;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MainTest {

    @Test
    public void startMain(){
        Path path = FileSystems.getDefault().getPath("");
        String directoryName = path.toAbsolutePath().toString();
        String src = directoryName + "\\src\\test\\resources\\src";
        String dest = directoryName + "\\src\\test\\resources\\dest";
        String[] args = new String[]{ "--source", src, "--destination", dest };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Main.main(args);
            }
        });
        thread.start();
    }
}
