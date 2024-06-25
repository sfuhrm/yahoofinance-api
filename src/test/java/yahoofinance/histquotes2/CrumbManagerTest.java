package yahoofinance.histquotes2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.Ignore;
import org.junit.Test;
import yahoofinance.mock.MockedServersTest;

public class CrumbManagerTest extends MockedServersTest {

    @Ignore("Requires internet, for manual testing")
    @Test
    public void testGetCrumb() throws IOException {
        assertNotNull("Crumb not set", CrumbManager.getCrumb());
        assertEquals(11, CrumbManager.getCrumb().length());
    }

}
