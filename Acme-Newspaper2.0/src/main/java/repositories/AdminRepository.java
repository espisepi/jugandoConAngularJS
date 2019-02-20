
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;
import domain.Newspaper;
import domain.User;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	@Query("select a from Admin a where a.userAccount.id = ?1")
	Admin findByUserAccountId(int id);

	//C1: The average and the standard deviation of newspapers created per user.
	@Query("select avg(u.newspapers.size), stddev(u.newspapers.size) from User u")
	Double[] theAvgAndStddevOfNewspapersForUser();

	//C2: The average and the standard deviation of articles written by writer.
	@Query("select avg(n.articles.size),stddev(n.articles.size) from User n")
	Double[] theAvgAndStddevOfArticlesPerWriter();

	//C3: The average and the standard deviation of articles per newspaper.
	@Query("select avg(n.articles.size),stddev(n.articles.size) from Newspaper n")
	Double[] theAvgAndStddevOfArticlePerNewspaper();

	//C4: The newspapers that have at least 10% more articles than the average.
	@Query("select n from Newspaper n where n.articles.size>(select 1.10 * avg(n.articles.size) from Newspaper n)")
	Collection<Newspaper> newspaperHaveLeast10MorePercentFewerArtclesThanAverage();

	//C5: The newspapers that have at least 10% fewer articles than the average.
	@Query("select n from Newspaper n where n.articles.size<(select 0.90 * avg(n.articles.size) from Newspaper n)")
	Collection<Newspaper> newspaperHaveLeast10LeastPercentFewerArtclesThanAverage();

	//C6: The ratio of users who have ever created a newspaper.
	@Query("select count(u)*1.0/(select count(us) from User us) from User u where u.newspapers.size!=0)")
	Double theRatioOfUsersWritingNewspaper();

	//C7: The ratio of users who have ever written an article.
	@Query("select count(u)*1.0/(select count(us) from User us) from User u where u.articles.size!=0)")
	Double theRatioOfUsersWritingArticle();

	//B1: The average number of follow-ups per article.
	@Query("select avg(a.followUps.size) from Article a")
	Double avgFollowupsPerArticle();
	//B2: The average number of follow-ups per article up to one week after the corresponding newspaperâs been published.
	@Query("select avg(a.followUps.size) from Article a where a.newspaper.publicationDate<?1")
	Double avgNumberOfFollowUpsPerArticleAfterOneWeek(Date since);
	//B3: The average number of follow-ups per article up to two weeks after the corresponding newspapers been published.
	@Query("select avg(a.followUps.size) from Article a where a.newspaper.publicationDate<?1")
	Double avgNumberOfFollowUpsPerArticleAfterTwoWeek(Date since);
	//B4: The average and the standard deviation of the number of chirps per user.
	@Query("select avg(u.chirps.size),stddev(u.chirps.size) from User u")
	Double[] avgAndStddevOfNumberOfChirpPerUser();
	//B5: The ratio of users who have posted above 75% the average number of chirps per user.
	@Query("select u from User u where u.chirps.size>=(select 1.75 * avg(n.articles.size) from Newspaper n)")
	Collection<User> ratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSer();
	//A1: The ratio of public versus private newspapers.
	@Query("select count(n)*1.0/(select count(ne) from Newspaper ne where ne.open=false) from Newspaper n where n.open=true")
	Double ratioOfNewspaperPublicPerNespaperProvate();
	//A2: The average number of articles per private newspapers.
	@Query("select avg(n.articles.size) from Newspaper n where n.open=false")
	Double avgArticlePerNewspapersPrivate();
	//A3: The average number of articles per public newspapers.
	@Query("select avg(n.articles.size) from Newspaper n where n.open=true")
	Double avgArticlesPerNewspapersPublic();
	//A4: The ratio of subscribers per private newspaper versus the total number of customers.
	@Query("select count(s)*1.0/(select count(u) from User u) from Subscription s where s.newspaper.open=false")
	Double ratioOfSubscribersWhenNewspaperPrivatePerNumberCustomer();
	//A5: The average ratio of private versus public newspapers per publisher.
	@Query("select count(ne)*1.0/(select count(n) from User us join us.newspapers n where n.open=true)from User u join u.newspapers ne where ne.open=false")
	Double theAverageRatioOfPrivateVersusPublicNewspaperPerPublished();

	//Acme-Newspaper 2.0

	//C1: The ratio of newspapers that have at least one advertisement versus the newspapers that haven’t any.
	@Query("select (select count(n) from Newspaper n where n.advertisements.size>1)*1.0/count(ne) from Newspaper ne where ne.advertisements.size=0")
	Double theRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement();

	//C2: The ratio of advertisements that have taboo words. Se ha realizado entre la query siguiente y un metodo en el servicio.
	@Query("select count(a) from Advertisement a")
	Double sizeOfTheListAdvertisement();

	//B1: The average number of newspapers per volume.
	@Query("select avg(a.newspapers.size) from Volume a")
	Double theAverageNumberOfNewspaperPerVolume();

	//B2: The ratio of subscriptions to volumes versus subscriptions to newspapers.
	@Query("select count(u)*1.0 /(select count(s) from Subscription s) from Underwrite u")
	Double theRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers();

}
