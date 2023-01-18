package org.deep.store.controllers;

import java.util.List;

import org.deep.store.dtos.ApiResponse;
import org.deep.store.dtos.CreateOrderRequest;
import org.deep.store.dtos.OrderDto;
import org.deep.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;





@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private String userId = "a99a3807-1c52-491d-bed7-5dedded089a7";

    // create
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody CreateOrderRequest createOrderData) {

        OrderDto createOrder = orderService.createOrder(createOrderData, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createOrder);

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable String userId){
    	List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
    	return ResponseEntity.status(HttpStatus.OK).body(ordersOfUser);
    }
     
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(){
    	List<OrderDto> allOrders= orderService.getOrders();
    	return ResponseEntity.status(HttpStatus.OK).body(allOrders);
    }
    
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable String orderId){
    	orderService.deleteOrder(orderId);
    	return ResponseEntity.ok(ApiResponse.builder().message("Order is canceled successfully! ").success(true).build());
    }
}
