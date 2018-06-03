
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LabelProductService;
import services.ManagerService;
import controllers.AbstractController;
import domain.LabelProduct;
import domain.Manager;

@Controller
@RequestMapping("/labelProduct/manager")
public class LabelProductManagerController extends AbstractController {

	//	Services --------------------------------------------------------

	@Autowired
	private LabelProductService	labelProductService;

	@Autowired
	private ManagerService		managerService;


	//	Listing ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Manager managerConnected;
		Collection<LabelProduct> labelProducts;

		managerConnected = this.managerService.findByPrincipal();

		Assert.isTrue(managerConnected instanceof Manager);

		labelProducts = this.labelProductService.findAll();

		result = new ModelAndView("labelProduct/list");
		result.addObject("labelProducts", labelProducts);
		result.addObject("requestURI", "labelProduct/manager/list.do?d-16544-p=1");
		result.addObject("RequestURIedit", "messageFolder/manager/edit.do");
		result.addObject("RequestURImessages", "message/manager/list.do?d-16544-p=1");

		return result;
	}

	// Create-------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LabelProduct labelProduct;

		labelProduct = this.labelProductService.create();
		result = this.createEditModelAndView(labelProduct);

		return result;

	}
	// Edit---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int labelProductId) {
		ModelAndView result;
		LabelProduct labelProduct;
		final Collection<LabelProduct> labelProductsWithProduct;
		labelProductsWithProduct = this.labelProductService.labelProductsOfAllProducts();

		labelProduct = this.labelProductService.findOne(labelProductId);

		Assert.isTrue(labelProduct.getByDefault() == false);
		Assert.isTrue(!labelProductsWithProduct.contains(labelProduct), "This label is asociated with one product or more");

		Assert.notNull(labelProduct);
		result = this.createEditModelAndView(labelProduct);

		return result;

	}

	//	Save-------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(LabelProduct labelProduct, final BindingResult bindingResult) {
		ModelAndView result;

		labelProduct = this.labelProductService.reconstruct(labelProduct, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(labelProduct);
		else
			try {
				this.labelProductService.save(labelProduct);
				result = new ModelAndView("redirect:/labelProduct/manager/list.do?d-16544-p=1");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("This label is asociated with one product or more"))
					result = this.createEditModelAndView(labelProduct, "labelProduct.labelContainsProduct.error");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(labelProduct, "labelProduct.nameUnique");
				else
					result = this.createEditModelAndView(labelProduct, "labelProduct.commit.error");

			}
		return result;
	}

	//Delete---------------------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int labelProductId) {
		ModelAndView result;
		LabelProduct labelProduct;

		labelProduct = this.labelProductService.findOne(labelProductId);
		Assert.notNull(labelProduct);
		try {
			this.labelProductService.delete(labelProduct);
			result = new ModelAndView("redirect:list.do?d-16544-p=1");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This label is asociated with one product or more"))
				result = this.createDeleteModelAndView(labelProduct, "labelProduct.labelContainsProduct.error");
			else
				result = this.createDeleteModelAndView(labelProduct, "labelProduct.commit.error");

		}
		return result;
	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final LabelProduct labelProduct) {
		ModelAndView result;
		result = this.createEditModelAndView(labelProduct, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final LabelProduct labelProduct, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("labelProduct/edit");
		result.addObject("labelProduct", labelProduct);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "labelProduct/manager/edit.do");

		return result;

	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createDeleteModelAndView(final LabelProduct labelProduct) {
		ModelAndView result;
		result = this.createDeleteModelAndView(labelProduct, null);
		return result;
	}

	protected ModelAndView createDeleteModelAndView(final LabelProduct labelProduct, final String messageCode) {
		ModelAndView result;
		Collection<LabelProduct> labelProducts;
		labelProducts = this.labelProductService.findAll();

		result = new ModelAndView("labelProduct/list");
		result.addObject("labelProduct", labelProduct);
		result.addObject("labelProducts", labelProducts);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "labelProduct/manager/list.do?d-16544-p=1");

		return result;

	}

}
