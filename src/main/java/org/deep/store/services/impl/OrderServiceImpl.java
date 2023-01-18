package org.deep.store.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.deep.store.dtos.CreateOrderRequest;
import org.deep.store.dtos.OrderDto;
import org.deep.store.entities.Cart;
import org.deep.store.entities.CartItem;
import org.deep.store.entities.Order;
import org.deep.store.entities.OrderItem;
import org.deep.store.entities.User;
import org.deep.store.excetions.BadRequestException;
import org.deep.store.excetions.ResourceNotFountException;
import org.deep.store.respository.CartRepository;
import org.deep.store.respository.OrderRepository;
import org.deep.store.respository.UserRepository;
import org.deep.store.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDto createOrder(CreateOrderRequest orderRequest, String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFountException("User not found !!"));

        int cartId = orderRequest.getCartId();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFountException("Cart not found !!"));
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.size() <= 0) {
            throw new BadRequestException("Invalid Number of Items in cart");
        }

        // creating order
        Order order = Order.builder()
                .billingName(orderRequest.getBillingName())
                .billingPhone(orderRequest.getBillingPhone())
                .billingAddress(orderRequest.getBillingAddress())
                .deliveredDate(orderRequest.getDeliveredDate())
                .orderedDate(orderRequest.getOrderedDate())
                .paymentStatus(orderRequest.getPaymentStatus())
                .orderStatus(orderRequest.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();

        AtomicReference<Integer> orderTotalAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice())
                    .order(order)
                    .build();

            orderTotalAmount.set(orderTotalAmount.get() + orderItem.getTotalPrice());

            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setTotalAmount(orderTotalAmount.get());
        Order savedOrder = orderRepository.save(order);

        // cart items blank

        cart.getItems().clear();

        cartRepository.save(cart);

        return mapper.map(savedOrder, OrderDto.class);

    }

    @Override
    public void deleteOrder(String orderId) {
    	
        // TODO Auto-generated method stub
    	
    	Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFountException("Order of given id cannot be found!!"));
    	orderRepository.delete(order);
    	

    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
       List<Order> orders = orderRepository.findAll();
       User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFountException("User cannot be found!!"));
       List<Order> userOrders = orders.stream().filter(order-> order.getUser().equals(user)).collect(Collectors.toList());
       return userOrders.stream().map(userOrder-> mapper.map(userOrder, OrderDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrders() {
    	List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order-> mapper.map(order, OrderDto.class)).collect(Collectors.toList());
    }

}
