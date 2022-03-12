import com.autocopy.parser.CommandLineConfigParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommandLineConfigParserTest {
    private String[] args;
    private String[] noargs;
    private CommandLineConfigParser parser;

    @BeforeEach
    public void setup(){
        args = new String[]{ "--source", "src", "--destination", "dest" };
        noargs = new String[]{};
        parser = new CommandLineConfigParser();
    }

    @Test
    @DisplayName("파싱한 객체를 리턴하고, 그 객체가 null 이 아님을 보이는 테스트")
    public void createParserThenNotNull(){
        parser.createParser(args);
        Assertions.assertNotNull(parser.getCommandLine());
    }

    @Test
    @DisplayName("파싱한 객체를 리턴하고, 그 객체가 null 임을 보이는 테스트")
    public void createParserThenNull(){
        // 이거 널임을 어떻게 보장하지...?
    }

    @Test
    @DisplayName("입력받은 source 디렉토리를 가져온다")
    public void parse_source_directory(){
        parser.createParser(args);
        String src = parser.parseSourceDirectory();
        Assertions.assertEquals("src", src);
    }

    @Test
    @DisplayName("입력받은 destination 디렉토리를 가져온다")
    public void parse_destination_directory(){
        parser.createParser(args);
        String dest = parser.parseDestinationDirectory();
        Assertions.assertEquals("dest", dest);
    }

    @Test
    @DisplayName("args에 source가 입력되지 않음.")
    public void parse_noargs_source_directory(){
        parser.createParser(noargs);
        String src = parser.parseSourceDirectory();
        Assertions.assertNull(src);
    }

    @Test
    @DisplayName("args에 destination이 입력되지 않음.")
    public void parse_noargs_destination_directory(){
        parser.createParser(noargs);
        String dest = parser.parseDestinationDirectory();
        Assertions.assertNull(dest);
    }
}
