package gr.recipemanagement.factory;

import gr.recipemanagement.dao.ingredientdao.IIngredientDAO;
import gr.recipemanagement.dao.ingredientdao.IngredientDAOImpl;
import gr.recipemanagement.dao.recipedao.IRecipeDAO;
import gr.recipemanagement.dao.recipedao.RecipeDAOImpl;
import gr.recipemanagement.dao.recipeingredientdao.IRecipeIngredientDAO;
import gr.recipemanagement.dao.recipeingredientdao.RecipeIngredientDAOImpl;

public class SQLStorageFactory implements StorageFactory {
    @Override
    public IRecipeDAO createRecipeDAO() {
        return new RecipeDAOImpl();
    }

    @Override
    public IIngredientDAO createIngredientDAO() {
        return new IngredientDAOImpl();
    }

    @Override
    public IRecipeIngredientDAO createRecipeIngredientDAO() {
        return new RecipeIngredientDAOImpl();
    }
}
