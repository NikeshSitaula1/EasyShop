package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {

    ShoppingCart getByUserId(int userId);
    ShoppingCart addToCart(int userId, ShoppingCartItem item);
    void updateCartItem(int userId, int productId, int quantity);
    void clearCart(int userId);
}
