package no.ntnu.appdev.group15.teawebsitebackend.controllers.rest;

import no.ntnu.appdev.group15.teawebsitebackend.RegisterTestData;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.OrderJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.database.UserJPA;
import no.ntnu.appdev.group15.teawebsitebackend.model.exceptions.CouldNotAddOrderException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

  private OrderJPA orderJPA;

  public OrderController (OrderJPA orderJPA, UserJPA userJPA){
    this.orderJPA = orderJPA;
    try {
      RegisterTestData.addTestOrder(orderJPA, userJPA);
    } catch (CouldNotAddOrderException e) {
      System.err.println("Testdata could not be added for orders");
    }
  }
}
