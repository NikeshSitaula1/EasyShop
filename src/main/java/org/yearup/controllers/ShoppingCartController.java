package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
@RestController
@RequestMapping("cart")
@CrossOrigin

// only log in users should have access to these actions
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao){
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }


    // each method in this controller requires a Principal object as a parameter

    @GetMapping()
    public ResponseEntity<ShoppingCart> getCart(Principal principal)
    {
        try
        {
            // get the currently log in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            // use the shoppingCartDao to get all items in the cart and return the cart
            return ResponseEntity.ok(cart);
        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added

    @PostMapping("/products/{productId}")
    public ShoppingCart addToCart(Principal principal, @PathVariable int productId) {
        try {
            if (principal == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in.");
            }

            String userName = principal.getName();
            User user = userDao.getByUserName(userName);

            Product product = productDao.getById(productId);
            if (product == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");
            }

            ShoppingCartItem item = new ShoppingCartItem();
            item.setProduct(product);
            item.setQuantity(1);

            // Add to cart and return the updated cart
            return shoppingCartDao.addToCart(user.getId(), item);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add product to cart.", e);
        }
    }



    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated

    @PutMapping("/products/{productId}")
    public void updateProductInCart(@PathVariable int productId, @RequestBody ShoppingCartItem item, Principal principal) {
        try {
            String username = principal.getName();
            User user = userDao.getByUserName(username);
            int userId = user.getId();

            // Ensure the product exists in the cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);
            if (!cart.contains(productId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in the cart.");
            }

            // Update the product quantity
            shoppingCartDao.updateCartItem(userId, productId, item.getQuantity());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update product in cart.", e);
        }
    }

    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Principal principal) {
        try {
            // Ensure the user is authenticated
            if (principal == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must be logged in.");
            }

            // Get the logged-in user's username
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Clear the user's shopping cart
            shoppingCartDao.clearCart(userId);

            // Return HTTP 204 No Content
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to clear the cart.", e);
        }
    }


}
