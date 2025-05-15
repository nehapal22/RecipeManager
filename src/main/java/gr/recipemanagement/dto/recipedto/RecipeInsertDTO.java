package gr.recipemanagement.dto.recipedto;

import gr.recipemanagement.dto.BaseDTO;
import java.util.ArrayList;
import java.util.List;

public class RecipeInsertDTO extends BaseDTO {
    private String recipeName;
    private String instructions;
    private List<Integer> ingredientIds;
    private double cookingTime;

    public RecipeInsertDTO() {
        this.ingredientIds = new ArrayList<>();
    }

    public RecipeInsertDTO(String recipeName, String instructions, double cookingTime) {
        this();
        setRecipeName(recipeName);
        setInstructions(instructions);
        setCookingTime(cookingTime);
    }

    public List<Integer> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Integer> ingredientIds) {
        this.ingredientIds = ingredientIds != null ? ingredientIds : new ArrayList<>();
    }

    public void addIngredientId(Integer ingredientId) {
        if (ingredientId != null) {
            if (this.ingredientIds == null) {
                this.ingredientIds = new ArrayList<>();
            }
            this.ingredientIds.add(ingredientId);
        }
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        if (recipeName == null) throw new IllegalArgumentException("Recipe name cannot be null");
        this.recipeName = recipeName.trim();
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        if (instructions == null) throw new IllegalArgumentException("Instructions cannot be null");
        this.instructions = instructions.trim();
    }

    public double getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(double cookingTime) {
        if (cookingTime < 0) throw new IllegalArgumentException("Cooking time cannot be negative");
        this.cookingTime = cookingTime;
    }
}
