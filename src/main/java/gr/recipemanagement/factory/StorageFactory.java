package gr.recipemanagement.factory;

import gr.recipemanagement.dao.ingredientdao.IIngredientDAO;
import gr.recipemanagement.dao.recipedao.IRecipeDAO;
import gr.recipemanagement.dao.recipeingredientdao.IRecipeIngredientDAO;

public interface StorageFactory {
    IRecipeDAO createRecipeDAO();
    IIngredientDAO createIngredientDAO();
    IRecipeIngredientDAO createRecipeIngredientDAO();
}
