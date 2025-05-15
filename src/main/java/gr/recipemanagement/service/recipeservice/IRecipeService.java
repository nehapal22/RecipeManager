package gr.recipemanagement.service.recipeservice;

import gr.recipemanagement.dto.recipedto.RecipeInsertDTO;
import gr.recipemanagement.dto.recipedto.RecipeUpdateDTO;
import gr.recipemanagement.model.Ingredient;
import gr.recipemanagement.model.Recipe;
import gr.recipemanagement.service.exceptions.IngredientNotFoundDAOException;
import gr.recipemanagement.service.exceptions.RecipeNotFoundDAOException;

import java.util.List;

public interface IRecipeService {
    Recipe insertRecipe(RecipeInsertDTO dto) throws RecipeNotFoundDAOException, IngredientNotFoundDAOException;
    Recipe updateRecipe(RecipeUpdateDTO dto) throws RecipeNotFoundDAOException, IngredientNotFoundDAOException;
    void deleteRecipe(int id) throws RecipeNotFoundDAOException;
    Recipe getRecipeById(int id) throws RecipeNotFoundDAOException;
    List<Recipe> getByFirstLetter(String firstLetter) throws RecipeNotFoundDAOException;
    List<Ingredient> getRecipeIngredients(int recipeId) throws RecipeNotFoundDAOException;
    Recipe getRecipeByName(String recipeName) throws RecipeNotFoundDAOException;
}
