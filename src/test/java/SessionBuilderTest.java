import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.SessionUtil;

public class SessionBuilderTest {
    @Test
    public void testSessionFactory() {
        try (Session session = SessionUtil.openSession()) {
            Assert.assertNotNull(session);
        }
    }
}
