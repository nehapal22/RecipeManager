package gr.recipemanagement.service.recipeservice;

import gr.recipemanagement.dao.ingredientdao.IIngredientDAO;
import gr.recipemanagement.dao.recipedao.IRecipeDAO;
import gr.recipemanagement.dao.recipeingredientdao.IRecipeIngredientDAO;
import gr.recipemanagement.dto.recipedto.RecipeInsertDTO;
import gr.recipemanagement.dto.recipedto.RecipeUpdateDTO;
import gr.recipemanagement.factory.StorageFactory;
import gr.recipemanagement.model.Ingredient;
import gr.recipemanagement.model.Recipe;
import gr.recipemanagement.service.exceptions.IngredientNotFoundDAOException;
import gr.recipemanagement.service.exceptions.RecipeNotFoundDAOException;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RecipeServiceImpl implements IRecipeService {

    private final IRecipeDAO recipeDAO;
    private final IIngredientDAO ingredientDAO;
    private final IRecipeIngredientDAO recipeIngredientDAO;

    public RecipeServiceImpl(StorageFactory factory) {
        this.recipeDAO = factory.createRecipeDAO();
        this.ingredientDAO = factory.createIngredientDAO();
        this.recipeIngredientDAO = factory.createRecipeIngredientDAO();
    }

    @Override
    public Recipe insertRecipe(RecipeInsertDTO dto) throws RecipeNotFoundDAOException, IngredientNotFoundDAOException {
        System.out.println("RecipeServiceImpl.insertRecipe called with DTO: " + dto);
        if(dto == null) return null;

        try {
            Recipe recipe = map(dto);
            System.out.println("Mapped DTO to Recipe: " + recipe);
            Recipe insertedRecipe = recipeDAO.insert(recipe);
            System.out.println("Recipe inserted successfully: " + insertedRecipe);

            if (dto.getIngredientIds() != null && !dto.getIngredientIds().isEmpty()) {
                for (Integer ingredientId : dto.getIngredientIds()) {
                    try {
                        ingredientDAO.getById(ingredientId);
                        recipeIngredientDAO.linkRecipeAndIngredient(insertedRecipe.getId(), ingredientId);
                        System.out.println("Linked ingredient " + ingredientId + " to recipe " + insertedRecipe.getId());
                    } catch (IngredientNotFoundDAOException e) {
                        System.err.println("Warning: Ingredient " + ingredientId + " not found, skipping...");
                    }
                }
            }

            return insertedRecipe;
        } catch (RecipeNotFoundDAOException e) {
            System.err.println("Error inserting recipe: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Recipe updateRecipe(RecipeUpdateDTO dto) throws RecipeNotFoundDAOException, IngredientNotFoundDAOException {
        System.out.println("RecipeServiceImpl.updateRecipe called with DTO: " + dto);
        if (dto == null) return null;

        try {
            Recipe recipe = map(dto);
            System.out.println("Mapped DTO to Recipe: " + recipe);

            Recipe existingRecipe = recipeDAO.getById(recipe.getId());
            if (existingRecipe == null) {
                throw new RecipeNotFoundDAOException(recipe);
            }

            Recipe updatedRecipe = recipeDAO.update(recipe);
            System.out.println("Core recipe updated successfully: " + updatedRecipe);

            recipeIngredientDAO.unlinkAllIngredients(recipe.getId());

            if (recipe.getIngredients() != null) {
                for (Ingredient ingredient : recipe.getIngredients()) {
                    if (ingredient == null || ingredient.getIngredientName() == null) continue;

                    System.out.println("Processing ingredient: " + ingredient.getIngredientName());
                    Ingredient existing = ingredientDAO.getByName(ingredient.getIngredientName());

                    int ingredientId;
                    if (existing != null) {
                        ingredientId = existing.getId();
                        System.out.println("Found existing ingredient with ID: " + ingredientId);
                    } else {
                        ingredientId = ingredientDAO.insert(ingredient.getIngredientName());
                        System.out.println("Created new ingredient with ID: " + ingredientId);
                    }

                    recipeIngredientDAO.linkRecipeAndIngredient(recipe.getId(), ingredientId);
                }
            }

            updatedRecipe = recipeDAO.getById(recipe.getId());
            System.out.println("Recipe update completed successfully");
            return updatedRecipe;

        } catch (RecipeNotFoundDAOException | IngredientNotFoundDAOException e) {
            System.err.println("Error updating recipe: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteRecipe(int id) throws RecipeNotFoundDAOException {
        try {
            Recipe recipe = recipeDAO.getById(id);
            if(recipe == null){
                throw new RecipeNotFoundDAOException("Deletion Error! Recipe with id: " + id + " wasn't found!");
            }
            recipeDAO.delete(id);
        } catch (RecipeNotFoundDAOException e){
            e.printStackTrace();
            System.err.println("Error! Couldn't delete recipe with id: " +  id );
            throw e;
        }
    }

    @Override
    public Recipe getRecipeById(int id) throws RecipeNotFoundDAOException {
        try {
            Recipe recipe = recipeDAO.getById(id);
            if(recipe == null){
                throw new RecipeNotFoundDAOException("Error: recipe with id: " + id + " wasn't found!");
            }
            return recipe;
        } catch (RecipeNotFoundDAOException e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Recipe> getByFirstLetter(String firstLetter) throws RecipeNotFoundDAOException {
        try {
            return recipeDAO.getRecipeByFirstLetter(firstLetter);
        } catch (RecipeNotFoundDAOException e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Recipe getRecipeByName(String recipeName) throws RecipeNotFoundDAOException {
        try {
            Recipe recipe = recipeDAO.getRecipeByName(recipeName);
            if(recipe == null){
                throw new RecipeNotFoundDAOException("Error! Recipe with the name: " + recipeName + " wasn't found.");
            }
            return recipe;
        } catch (RecipeNotFoundDAOException e){
            throw e;
        }
    }

    @Override
    public List<Ingredient> getRecipeIngredients(int recipeId) throws RecipeNotFoundDAOException {
        try {
            return recipeDAO.getRecipeIngredients(recipeId);
        } catch (RecipeNotFoundDAOException e){
            e.printStackTrace();
            throw e;
        }
    }

    private Recipe map(RecipeInsertDTO dto) {
        if (dto == null) return null;
        return new Recipe(
            null,
            dto.getRecipeName(),
            dto.getInstructions(),
            dto.getCookingTime()
        );
    }

    private Recipe map(RecipeUpdateDTO dto) {
        if (dto == null) return null;

        Recipe recipe = new Recipe(
            dto.getId(),
            dto.getRecipeName(),
            dto.getInstructions(),
            dto.getCookingTime()
        );

        List<Ingredient> ingredients = dto.getIngredients();
        if (ingredients != null) {
            List<Ingredient> validIngredients = ingredients.stream()
                .filter(i -> i != null)
                .collect(Collectors.toList());
            recipe.setIngredients(validIngredients);
        } else {
            recipe.setIngredients(new ArrayList<>());
        }

        return recipe;
    }
}
