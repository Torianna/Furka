/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wizut.tpsi.ogloszenia;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import wizut.tpsi.ogloszenia.services.OffersService;

/**
 *
 * @author Klaudia Góralska
 */
@Controller
public class HomeController {
    
    @Autowired
    private OffersService offersService;
    
    @RequestMapping("/home")
    public String lab10(Model model) 
    {
        CarManufacturer manufactur = offersService.getCarManufacturer(2);
        CarModel mod=offersService.getModel(3);
        model.addAttribute("CarManufacturer", manufactur);
        model.addAttribute("CarModel", mod);
        return "home";
    }
    
    @GetMapping("/")
    public String home(Model model, OfferFilter offerFilter) {
        List<CarManufacturer> carManufacturers = offersService.getCarManufacturers();
        List<CarModel> carModels = offersService.getCarModels();

        List<Offer> offers;

        if(offerFilter.getManufacturerId()!=null) {
            offers = offersService.getOffersByManufacturer(offerFilter.getManufacturerId());
        } else {
            offers = offersService.getOffers();
        }
        List<BodyStyle> bodyStyle= offersService.getBodyStyles();
        List<FuelType> fuelType= offersService.getFuelTypes();

        model.addAttribute("carManufacturers", carManufacturers);
        model.addAttribute("carModels", carModels);
        model.addAttribute("offers", offers);
        model.addAttribute("bodyStyle", bodyStyle);
        model.addAttribute("fuelType", fuelType);

        return "offersList";
}
    @GetMapping("/offer/{id}")
    public String offerDetails(Model model, @PathVariable("id") Integer id) {
        Offer offer = offersService.getOffer(id);
        model.addAttribute("offer", offer);
        return "offerDetails";
    }
    
    @GetMapping("/newoffer")
    public String newOfferForm(Model model, Offer offer) {
        List<CarModel> carModels = offersService.getCarModels();
        List<BodyStyle> bodyStyles = offersService.getBodyStyles();
        List<FuelType> fuelTypes = offersService.getFuelTypes();

        model.addAttribute("carModels", carModels);
        model.addAttribute("bodyStyles", bodyStyles);
        model.addAttribute("fuelTypes", fuelTypes);
        model.addAttribute("header", "Nowe ogłoszenie");
        model.addAttribute("action", "/newoffer");
        return "offerForm";
    }
    
    @PostMapping("/newoffer")
    public String saveNewOffer(Model model, @Valid Offer offer, BindingResult binding) {
        if(binding.hasErrors()) {
            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();

            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);

            return "offerForm";
        }
        offer = offersService.createOffer(offer);

        return "redirect:/offer/" + offer.getId();
    }
    
    @GetMapping("/deleteoffer/{id}")
    public String deleteOffer(Model model, @PathVariable("id") Integer id) {
    Offer offer = offersService.deleteOffer(id);

    model.addAttribute("offer", offer);
    return "deleteOffer";
    }
    
 @GetMapping("/editoffer/{id}")
    public String editOffer(Model model, @PathVariable("id") Integer id) 
    {
        model.addAttribute("header", "Edycja ogłoszenia");
        model.addAttribute("action", "/editoffer/" + id);
        Offer offer = offersService.getOffer(id);
        model.addAttribute("offer", offer);
        List<CarModel> carModels = offersService.getCarModels();
        List<BodyStyle> bodyStyles = offersService.getBodyStyles();
        List<FuelType> fuelTypes = offersService.getFuelTypes();

        model.addAttribute("carModels", carModels);
        model.addAttribute("bodyStyles", bodyStyles);
        model.addAttribute("fuelTypes", fuelTypes);
        return "offerForm";
    }
    @PostMapping("/editoffer/{id}")
    public String saveEditedOffer(Model model, @PathVariable("id") Integer id, @Valid Offer offer, BindingResult binding) {
        if(binding.hasErrors()) {
            model.addAttribute("header", "Edycja ogłoszenia");
            model.addAttribute("action", "/editoffer/" + id);

            List<CarModel> carModels = offersService.getCarModels();
            List<BodyStyle> bodyStyles = offersService.getBodyStyles();
            List<FuelType> fuelTypes = offersService.getFuelTypes();

            model.addAttribute("carModels", carModels);
            model.addAttribute("bodyStyles", bodyStyles);
            model.addAttribute("fuelTypes", fuelTypes);

            return "offerForm";
        }

        offersService.saveOffer(offer);

        return "redirect:/offer/" + offer.getId();
    }
    
}
