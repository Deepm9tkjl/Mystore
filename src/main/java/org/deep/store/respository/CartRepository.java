package org.deep.store.respository;

import java.util.Optional;

import org.deep.store.entities.Cart;
import org.deep.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {

    //
    Optional<Cart> findByUser(User user);

}
