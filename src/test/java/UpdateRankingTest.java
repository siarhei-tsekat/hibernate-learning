import org.testng.Assert;
import org.testng.annotations.Test;
import service.HibernateRankingService;

public class UpdateRankingTest {

    private HibernateRankingService rankingService = new HibernateRankingService();
    public final String MIKE = "Mike";
    public final String BOB = "Bob";
    public final String JAVA = "Java";
    public final String C_PLUS = "C++";


    @Test
    public void updateExistingRanking() {
        rankingService.addRanking(MIKE, BOB, JAVA, 8);
        Assert.assertEquals(rankingService.getRanking(MIKE, JAVA), 8);
        rankingService.updateRanking(MIKE, BOB, JAVA, 7);
        Assert.assertEquals(rankingService.getRanking(MIKE, JAVA), 7);
    }

    @Test
    public void updateNonExistingRanking() {
        rankingService.updateRanking(MIKE, BOB, C_PLUS, 7);
        Assert.assertEquals(rankingService.getRanking(MIKE, C_PLUS), 7);
    }
}
