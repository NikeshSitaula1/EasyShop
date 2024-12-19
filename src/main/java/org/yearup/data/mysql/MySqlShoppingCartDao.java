package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    public MySqlShoppingCartDao(DataSource dataSource, MySqlProductDao productDao) {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart(); // Create an empty shopping cart
        String sql = """
        SELECT sc.product_id, sc.quantity, p.name, p.price, p.category_id, p.description,
               p.color, p.stock, p.featured, p.image_url
        FROM shopping_cart sc
        JOIN products p ON sc.product_id = p.product_id
        WHERE sc.user_id = ?;
    """;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId); // Set the user ID for the query

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Map the result set row to a Product object
                Product product = mapRow(resultSet);
                int quantity = resultSet.getInt("quantity");

                // Create a shopping cart item and add it to the cart
                ShoppingCartItem item = new ShoppingCartItem();
                item.setProduct(product);
                item.setQuantity(quantity);

                cart.add(item);// Add the item to the cart
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve shopping cart.", e);
        }
        return cart;
    }


    @Override
    public ShoppingCart addToCart(int userId, ShoppingCartItem item) {
        String sqlCheck = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String sqlInsert = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String sqlUpdate = "UPDATE shopping_cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection()) {
            // Check if the product is already in the cart
            PreparedStatement checkStatement = connection.prepareStatement(sqlCheck);
            checkStatement.setInt(1, userId);
            checkStatement.setInt(2, item.getProduct().getProductId());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Update the quantity if the product already exists in the cart
                PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate);
                updateStatement.setInt(1, item.getQuantity());
                updateStatement.setInt(2, userId);
                updateStatement.setInt(3, item.getProduct().getProductId());
                updateStatement.executeUpdate();
            } else {
                // Insert a new row for the product if it's not in the cart
                PreparedStatement insertStatement = connection.prepareStatement(sqlInsert);
                insertStatement.setInt(1, userId);
                insertStatement.setInt(2, item.getProduct().getProductId());
                insertStatement.setInt(3, item.getQuantity());
                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add item to cart.", e);
        }

        // Return the updated cart
        return getByUserId(userId);
    }


    @Override
    public void updateCartItem(int userId, int productId, int quantity) {
        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, quantity); // Set the new quantity
            statement.setInt(2, userId); // Set the user ID
            statement.setInt(3, productId); // Set the product ID
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId) {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId); // Set the user ID to clear the cart for
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear the shopping cart.", e);
        }
    }

    // Helper method
    private Product mapRow(ResultSet resultSet) throws SQLException {
        int productId = resultSet.getInt("product_id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        int categoryId = resultSet.getInt("category_id");
        String description = resultSet.getString("description");
        String color = resultSet.getString("color");
        int stock = resultSet.getInt("stock");
        boolean isFeatured = resultSet.getBoolean("featured");
        String imageUrl = resultSet.getString("image_url");

        return new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imageUrl);
    }
}

