package org.deep.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.deep.store.dtos.CartDto;
import org.deep.store.dtos.CartItemRequest;
import org.deep.store.entities.Cart;
import org.deep.store.entities.CartItem;
import org.deep.store.entities.Product;
import org.deep.store.entities.User;
import org.deep.store.excetions.BadRequestException;
import org.deep.store.excetions.ResourceNotFountException;
import org.deep.store.respository.CartItemRepository;
import org.deep.store.respository.CartRepository;
import org.deep.store.respository.ProductRepository;
import org.deep.store.respository.UserRepository;
import org.deep.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cardItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(CartItemRequest cartItemRequest, String userId) {

        String productId = cartItemRequest.getProductId();
        int quantity = cartItemRequest.getQuantity();

        if (quantity <= 0) {
            throw new BadRequestException("Quantity is invalid !!");
        }

        // get the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFountException("User not found"));

        // get the product to added in cart
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFountException("Product not found !!"));

        Cart cart = null;
        // get the cart of user.
        try {
            // if cart is present in database
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            // no cart of this user is present in database
            cart = new Cart();
            cart.setCartCreatedDate(new Date());
        }

        // cart-> cartitem

        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();

        // if cartitem exists update the quantity
        List<CartItem> updatedItemList = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        // create new cartitem if not exists

        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getPrice())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(int itemId, String userId) {
        CartItem item = cardItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFountException("Item not found !!"));
        cardItemRepository.delete(item);

    }

    @Override
    public CartDto getCart(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFountException("User not found !!"));

        Cart cart = cartRepository.findByUser(user).get();
        return mapper.map(cart, CartDto.class);
    }

    @Override
    public CartDto getCartByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFountException("User not found !!"));
        Cart cart = cartRepository.findByUser(user).get();
        return mapper.map(cart, CartDto.class);
    }

}
