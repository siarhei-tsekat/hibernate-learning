import org.testng.Assert;
import org.testng.annotations.Test;
import service.HibernateRankingService;

public class RemoveRankingTest {
    private HibernateRankingService rankingService = new HibernateRankingService();

    @Test
    public void removeRanking() {
        rankingService.addRanking("R1", "R2", "R3", 8);
        Assert.assertEquals(rankingService.getRanking("R1", "R3"), 8);
        rankingService.removeRanking("R1", "R2", "R3");
        Assert.assertEquals(rankingService.getRanking("R1", "R3"), 0);
    }

    @Test
    public void removeNonExistingRanking() {
        rankingService.removeRanking("R1", "R2", "R3");
        Assert.assertEquals(rankingService.getRanking("R1", "R3"), 0);
    }
}
