package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Category;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public Profile create(Profile profile)
    {
        // SQL query to insert a new profile into the database
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate(); // Execute the insert query
            return profile; // Return the created profile
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Profile getByUserId(int userId){

        // SQL query to retrieve a profile by user ID
        String sql = """
            SELECT *
            FROM profiles
            WHERE user_id = ?;
            """;

        try (Connection connection = getConnection();
             PreparedStatement query = connection.prepareStatement(sql)) {

            query.setInt(1, userId); // Set the user ID for the query

            try (ResultSet results = query.executeQuery()) {
                if (results.next()) {
                    return mapRow(results); // Use the mapRow method to convert the result to a Profile object
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null; // Return null if no profile is found
    }


    @Override
    public Profile update(Profile profile)
    {
        // update category
        String sql = """
                UPDATE profiles
                SET first_name = ?, last_name = ?, phone = ?, email = ?, address = ?, city = ?, state = ?, zip = ?
                WHERE user_id = ?;
                """;
        try(Connection connection = getConnection();
            PreparedStatement query = connection.prepareStatement(sql);
        )
        {
            // Set the updated profile details
            query.setString(1, profile.getFirstName());
            query.setString(2, profile.getLastName());
            query.setString(3, profile.getPhone());
            query.setString(4, profile.getEmail());
            query.setString(5, profile.getAddress());
            query.setString(6, profile.getCity());
            query.setString(7, profile.getState());
            query.setString(8, profile.getZip());
            query.setInt(9, profile.getUserId());

            query.executeUpdate(); // Execute the update query
            return profile;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method
    private Profile mapRow(ResultSet row) throws SQLException
    {
        int userId = row.getInt("user_id");
        String firstName = row.getString("first_name");
        String lastName = row.getString("last_name");
        String phone = row.getString("phone");
        String email = row.getString("email");
        String address = row.getString("address");
        String city = row.getString("city");
        String state = row.getString("state");
        String zip = row.getString("zip");

        Profile profile = new Profile()
        {{
            setUserId(userId);
            setFirstName(firstName);
            setLastName(lastName);
            setPhone(phone);
            setEmail(email);
            setAddress(address);
            setCity(city);
            setState(state);
            setZip(zip);

        }};
        return profile;
    }
}
