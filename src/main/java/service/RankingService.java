package service;

public interface RankingService {

    int getRanking(String subject, String skill);

    void addRanking(String subjectName, String observerName, String skillCaption, int rank);

    void updateRanking(String subjectName, String observerName, String skillCaption, int rank);

    void removeRanking(String subjectName, String observerName, String skillCaption);
}
