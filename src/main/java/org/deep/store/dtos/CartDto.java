package org.deep.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.deep.store.entities.CartItem;
import org.deep.store.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private int cardId;

    private Date cartCreatedDate;

    private List<CartItemDto> items = new ArrayList<>();

    private UserDto user;

}
