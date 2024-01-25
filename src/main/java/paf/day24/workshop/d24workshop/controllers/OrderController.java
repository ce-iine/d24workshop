package paf.day24.workshop.d24workshop.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;
import paf.day24.workshop.d24workshop.models.Detail;
import paf.day24.workshop.d24workshop.models.Order;
import paf.day24.workshop.d24workshop.repo.OrderException;
import paf.day24.workshop.d24workshop.service.MessageSvc;
import paf.day24.workshop.d24workshop.service.OrderService;

@Controller
@RequestMapping
public class OrderController {

    @Autowired
    OrderService orderSvc;

    @Autowired 
    MessageSvc mSvc;

    @GetMapping("")
    public String form(Model model, HttpSession sess) {
        Detail detail = new Detail();
        List<Detail> detailList = new ArrayList<>();
        detailList = (List<Detail>) sess.getAttribute("detailList");
        if (detailList == null){
            List<Detail> orderDetails = new ArrayList<>();
            sess.setAttribute("detailList", orderDetails);   
        }

        model.addAttribute("detail", detail);
        model.addAttribute("orderDetails", detailList);
        return "orderdetails";
    }

    @PostMapping("/order")
    public String insert(@ModelAttribute Detail detail, HttpSession sess, Model model) {
        System.out.println("NEW DETAIL LOOKS LIKE THAT CURRENTLY>>>>" + detail);
        List<Detail> detailList = (List<Detail>) sess.getAttribute("detailList");
        detailList.add(detail);

        Detail newDetail = new Detail();
        model.addAttribute("detail", newDetail);
        model.addAttribute("orderDetails", detailList);
        return "orderdetails";
    }

    @GetMapping("/checkoutpage")
    public String checkoutpage(Model model) {
        Order order = new Order();

        //redis messaging wksp25
		String[] names = mSvc.getAllRegisteredCust();
		model.addAttribute("names", names);
        
        model.addAttribute("order", order);
        return "order";
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> updateSQL(@ModelAttribute Order order, HttpSession sess) throws OrderException{
        //before messageing implementation it returns string

        List<Detail> detailList = (List<Detail>) sess.getAttribute("detailList");

        // try {
        //     if(orderSvc.updateTables(order, detailList)){
        //         sess.invalidate();
        //         return "success";
        //     }
        // } catch (OrderException ex){
        //     System.out.println("----- exception occured ----------");
        //     throw ex;
        // }

        order.setAllDetails(detailList);

        System.out.println("ORDER>>>" +order);
        String orderJson = mSvc.makeJson(order);

        if(mSvc.updateUserList(order, orderJson)){
            sess.invalidate();
            return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderJson);
            // return "success";
        }

        // return "orderdetails";
        JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "cant update table")
                    .build();
        return new ResponseEntity<String>(errorJson.toString(), HttpStatus.NOT_FOUND);
    }
}
