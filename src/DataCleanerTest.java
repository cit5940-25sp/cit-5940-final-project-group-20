
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
public class DataCleanerTest {

    @org.junit.Test
    public void testweirdChar() {
        String name = "u042eu043bnu044f u0421u043dnrnpu044c";
        assertTrue(DataCleaner.hasWeirdChar(name));
    }


    @org.junit.Test
    public void testClean() {
        assertEquals("Renee Zellweger", DataCleaner.clean("Renu00e9e Zellweger"));
    }
}
