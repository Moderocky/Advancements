package mx.kenzie.advancements;

import org.junit.Test;

public class DisplayTest {
    
    @Test
    public void title() {
        final Display display = new Display();
        assert display.title != null;
        assert display.title.size() > 0;
        assert display.description != null;
        assert display.description.size() > 0;
    }
    
}
