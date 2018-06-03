
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import controllers.AbstractController;
import domain.Editor;
import domain.Item;
import domain.LabelProduct;
import domain.Material;
import domain.Newscast;

@Controller
@RequestMapping(value = "/admin")
public class DashboardAdminController extends AbstractController {

	//Services---------------------
	@Autowired
	private AdminService	adminService;


	//Displaying----------------------
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		result = new ModelAndView("admin/dashboard");

		Collection<Newscast> findNewWithMoreComments;
		Collection<Editor> findEditorsWithMoreNewsRedacted;
		Collection<LabelProduct> findTop5LabelProducts;
		Double avgMinMaxAndStddevOfCoursesByBuyer[];
		Double avgOfIncidencesResolved;
		Double avgOfRecyclerWithAtLeastOneProduct;
		Double avgOfUsersBanned;
		Double avgMinMaxAndStddevOfCommentsByNews[];
		Collection<Item> findLatestItems;
		String nameTitleRecyclerWithItemMostValue[];
		Double avgMinMaxAndStddevOfRequestByManager[];
		Collection<Material> findTop3Materials;
		Double ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest;

		findNewWithMoreComments = this.adminService.findNewWithMoreComments();
		findEditorsWithMoreNewsRedacted = this.adminService.findEditorsWithMoreNewsRedacted();
		findTop5LabelProducts = this.adminService.findTop5LabelProducts();
		avgMinMaxAndStddevOfCoursesByBuyer = this.adminService.avgMinMaxAndStddevOfCoursesByBuyer();
		avgOfIncidencesResolved = this.adminService.avgOfIncidencesResolved();
		avgOfRecyclerWithAtLeastOneProduct = this.adminService.avgOfRecyclerWithAtLeastOneProduct();
		avgOfUsersBanned = this.adminService.avgOfUsersBanned();
		avgMinMaxAndStddevOfCommentsByNews = this.adminService.avgMinMaxAndStddevOfCommentsByNews();
		findLatestItems = this.adminService.findLatestItems();
		nameTitleRecyclerWithItemMostValue = this.adminService.nameTitleRecyclerWithItemMostValue();
		avgMinMaxAndStddevOfRequestByManager = this.adminService.avgMinMaxAndStddevOfRequestByManager();
		findTop3Materials = this.adminService.findTop3Materials();
		ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest = this.adminService.ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest();

		result.addObject("findNewWithMoreComments", findNewWithMoreComments);
		result.addObject("findEditorsWithMoreNewsRedacted", findEditorsWithMoreNewsRedacted);
		result.addObject("findTop5LabelProducts", findTop5LabelProducts);
		result.addObject("avgMinMaxAndStddevOfCoursesByBuyer", avgMinMaxAndStddevOfCoursesByBuyer);
		result.addObject("avgOfIncidencesResolved", avgOfIncidencesResolved);
		result.addObject("avgOfRecyclerWithAtLeastOneProduct", avgOfRecyclerWithAtLeastOneProduct);
		result.addObject("avgOfUsersBanned", avgOfUsersBanned);
		result.addObject("avgMinMaxAndStddevOfCommentsByNews", avgMinMaxAndStddevOfCommentsByNews);
		result.addObject("findLatestItems", findLatestItems);
		result.addObject("nameTitleRecyclerWithItemMostValue", nameTitleRecyclerWithItemMostValue);
		result.addObject("avgMinMaxAndStddevOfRequestByManager", avgMinMaxAndStddevOfRequestByManager);
		result.addObject("findTop3Materials", findTop3Materials);
		result.addObject("ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest", ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest);

		return result;

	}
}
