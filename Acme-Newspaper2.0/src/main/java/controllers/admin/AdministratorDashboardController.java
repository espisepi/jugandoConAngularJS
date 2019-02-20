
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import controllers.AbstractController;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping(value = "/admin")
public class AdministratorDashboardController extends AbstractController {

	//Services---------------------
	@Autowired
	private AdminService	adminService;


	//Displaying----------------------

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView display() {

		ModelAndView result;
		result = new ModelAndView("administrator/dashboard");
		Double theAvgAndStddevOfNewspapersForUser[];
		Double theAvgAndStddevOfArticlesPerWriter[];
		Double theAvgAndStddevOfArticlePerNewspaper[];
		Collection<Newspaper> newspaperHaveLeast10MorePercentFewerArtclesThanAverage;
		Collection<Newspaper> newspaperHaveLeast10LeastPercentFewerArtclesThanAverage;
		Double theRatioOfUsersWritingNewspaper;
		Double theRatioOfUsersWritingArticle;
		Double avgFollowupsPerArticle;
		Double avgNumberOfFollowUpsPerArticleAfterOneWeek;
		Double avgNumberOfFollowUpsPerArticleAfterTwoWeek;
		Double avgAndStddevOfNumberOfChirpPerUser[];
		Collection<User> ratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSer;
		Double ratioOfNewspaperPublicPerNespaperProvate;
		Double avgArticlePerNewspapersPrivate;
		Double avgArticlesPerNewspapersPublic;
		Double ratioOfSubscribersWhenNewspaperPrivatePerNumberCustomer;
		Double theAverageRatioOfPrivateVersusPublicNewspaperPerPublished;
		Double theRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement;
		Double theRatioOfAdvertisementsThatHaveTabooWords;
		Double theAverageNumberOfNewspaperPerVolume;
		Double theRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers;

		theAvgAndStddevOfNewspapersForUser = this.adminService.theAvgAndStddevOfNewspapersForUser();
		theAvgAndStddevOfArticlesPerWriter = this.adminService.theAvgAndStddevOfArticlesPerWriter();
		theAvgAndStddevOfArticlePerNewspaper = this.adminService.theAvgAndStddevOfArticlePerNewspaper();
		newspaperHaveLeast10MorePercentFewerArtclesThanAverage = this.adminService.newspaperHaveLeast10MorePercentFewerArtclesThanAverage();
		newspaperHaveLeast10LeastPercentFewerArtclesThanAverage = this.adminService.newspaperHaveLeast10LeastPercentFewerArtclesThanAverage();
		theRatioOfUsersWritingNewspaper = this.adminService.theRatioOfUsersWritingNewspaper();
		theRatioOfUsersWritingArticle = this.adminService.theRatioOfUsersWritingArticle();
		avgFollowupsPerArticle = this.adminService.avgFollowupsPerArticle();
		avgNumberOfFollowUpsPerArticleAfterOneWeek = this.adminService.avgNumberOfFollowUpsPerArticleAfterOneWeek();
		avgNumberOfFollowUpsPerArticleAfterTwoWeek = this.adminService.avgNumberOfFollowUpsPerArticleAfterTwoWeek();
		avgAndStddevOfNumberOfChirpPerUser = this.adminService.avgAndStddevOfNumberOfChirpPerUser();
		ratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSer = this.adminService.ratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSer();
		ratioOfNewspaperPublicPerNespaperProvate = this.adminService.ratioOfNewspaperPublicPerNespaperProvate();
		avgArticlePerNewspapersPrivate = this.adminService.avgArticlePerNewspapersPrivate();
		avgArticlesPerNewspapersPublic = this.adminService.avgArticlesPerNewspapersPublic();
		ratioOfSubscribersWhenNewspaperPrivatePerNumberCustomer = this.adminService.ratioOfSubscribersWhenNewspaperPrivatePerNumberCustomer();
		theAverageRatioOfPrivateVersusPublicNewspaperPerPublished = this.adminService.theAverageRatioOfPrivateVersusPublicNewspaperPerPublished();
		theRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement = this.adminService.theRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement();
		theAverageNumberOfNewspaperPerVolume = this.adminService.theAverageNumberOfNewspaperPerVolume();
		theRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers = this.adminService.theRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers();
		theRatioOfAdvertisementsThatHaveTabooWords = this.adminService.theRatioOfAdvertisementsThatHaveTabooWords();

		result.addObject("theAvgAndStddevOfNewspapersForUser", theAvgAndStddevOfNewspapersForUser);
		result.addObject("theAvgAndStddevOfArticlesPerWriter", theAvgAndStddevOfArticlesPerWriter);
		result.addObject("theAvgAndStddevOfArticlePerNewspaper", theAvgAndStddevOfArticlePerNewspaper);
		result.addObject("newspaperHaveLeast10MorePercentFewerArtclesThanAverage", newspaperHaveLeast10MorePercentFewerArtclesThanAverage);
		result.addObject("newspaperHaveLeast10LeastPercentFewerArtclesThanAverage", newspaperHaveLeast10LeastPercentFewerArtclesThanAverage);
		result.addObject("theRatioOfUsersWritingNewspaper", theRatioOfUsersWritingNewspaper);
		result.addObject("theRatioOfUsersWritingArticle", theRatioOfUsersWritingArticle);
		result.addObject("avgFollowupsPerArticle", avgFollowupsPerArticle);
		result.addObject("avgNumberOfFollowUpsPerArticleAfterOneWeek", avgNumberOfFollowUpsPerArticleAfterOneWeek);
		result.addObject("avgNumberOfFollowUpsPerArticleAfterTwoWeek", avgNumberOfFollowUpsPerArticleAfterTwoWeek);
		result.addObject("avgAndStddevOfNumberOfChirpPerUser", avgAndStddevOfNumberOfChirpPerUser);
		result.addObject("ratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSer", ratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSer);
		result.addObject("ratioOfNewspaperPublicPerNespaperProvate", ratioOfNewspaperPublicPerNespaperProvate);
		result.addObject("avgArticlePerNewspapersPrivate", avgArticlePerNewspapersPrivate);
		result.addObject("avgArticlesPerNewspapersPublic", avgArticlesPerNewspapersPublic);
		result.addObject("ratioOfSubscribersWhenNewspaperPrivatePerNumberCustomer", ratioOfSubscribersWhenNewspaperPrivatePerNumberCustomer);
		result.addObject("theAverageRatioOfPrivateVersusPublicNewspaperPerPublished", theAverageRatioOfPrivateVersusPublicNewspaperPerPublished);
		result.addObject("theRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement", theRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement);
		result.addObject("theAverageNumberOfNewspaperPerVolume", theAverageNumberOfNewspaperPerVolume);
		result.addObject("theRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers", theRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers);
		result.addObject("theRatioOfAdvertisementsThatHaveTabooWords", theRatioOfAdvertisementsThatHaveTabooWords);

		return result;

	}
}
