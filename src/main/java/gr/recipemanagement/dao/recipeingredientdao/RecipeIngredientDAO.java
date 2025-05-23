package gr.recipemanagement.dao.recipeingredientdao;
import gr.recipemanagement.service.exceptions.RecipeNotFoundDAOException;

public interface RecipeIngredientDAO {
    void unlinkAllIngredients(int recipeId) throws RecipeNotFoundDAOException;

    void linkRecipeAndIngredient(int recipeId, int ingredientId) throws RecipeNotFoundDAOException;
}
