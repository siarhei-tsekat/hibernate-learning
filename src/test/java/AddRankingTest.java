import org.testng.Assert;
import org.testng.annotations.Test;
import service.HibernateRankingService;
import service.RankingService;

public class AddRankingTest {
    RankingService rankingService = new HibernateRankingService();

    @Test
    public void addRanking() {
        rankingService.addRanking("Mike", "Drew", "Java", 8);
        rankingService.addRanking("Mike", "Drew 2", "Java", 4);
        rankingService.addRanking("Mike", "Drew 3", "Java", 3);
        Assert.assertEquals(rankingService.getRanking("Mike", "Java"), 5);
    }
}
