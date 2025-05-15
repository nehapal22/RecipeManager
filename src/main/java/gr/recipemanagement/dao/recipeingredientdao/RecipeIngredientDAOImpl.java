package gr.recipemanagement.dao.recipeingredientdao;

import gr.recipemanagement.service.exceptions.RecipeNotFoundDAOException;
import gr.recipemanagement.service.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class RecipeIngredientDAOImpl implements IRecipeIngredientDAO {

    @Override
    public void unlinkAllIngredients(int recipeId) throws RecipeNotFoundDAOException {
        String sql = "DELETE FROM recipes_ingredients WHERE recipeid = ?";
        System.out.println("Unlinking all ingredients for recipe ID: " + recipeId);

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, recipeId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Unlinked " + rowsAffected + " ingredients from recipe ID: " + recipeId);
        } catch (SQLException e) {
            System.err.println("Error unlinking ingredients for recipe ID " + recipeId + ": " + e.getMessage());
            throw new RecipeNotFoundDAOException("Error unlinking ingredients for recipe ID: " + recipeId);
        }
    }

    @Override
    public void linkRecipeAndIngredient(int recipeId, int ingredientId) throws RecipeNotFoundDAOException {
        String sql = "INSERT INTO recipes_ingredients (recipeid, ingredientid) VALUES (?,?)";
        System.out.println("Linking recipe ID " + recipeId + " with ingredient ID " + ingredientId);

        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, recipeId);
            ps.setInt(2, ingredientId);
            ps.executeUpdate();
            System.out.println("Successfully linked recipe ID " + recipeId + " with ingredient ID " + ingredientId);
        } catch (SQLException e) {
            System.err.println("Error linking recipe ID " + recipeId + " with ingredient ID " + ingredientId + ": " + e.getMessage());
            throw new RecipeNotFoundDAOException("Error linking recipe ID: " + recipeId + " with ingredient ID: " + ingredientId);
        }
    }
}
