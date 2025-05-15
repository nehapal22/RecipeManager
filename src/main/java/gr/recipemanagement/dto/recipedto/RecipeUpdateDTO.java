package gr.recipemanagement.dto.recipedto;

import gr.recipemanagement.dto.BaseDTO;
import gr.recipemanagement.model.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeUpdateDTO extends BaseDTO {

    private Integer id;
    private String recipeName;
    private List<Ingredient> ingredients;
    private String instructions;
    private double cookingTime;

    public RecipeUpdateDTO() {
        this.ingredients = new ArrayList<>();
    }

    public RecipeUpdateDTO(int id, String recipeName, String instructions, double cookingTime){
        this.id = id;
        this.recipeName = recipeName;
        this.instructions = instructions;
        this.cookingTime = cookingTime;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients(){
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        if (ingredient == null || ingredient.getId() == null) {
            throw new IllegalArgumentException("Cannot add null ingredient or ingredient without ID");
        }
        if (this.ingredients == null) {
            this.ingredients = new ArrayList<>();
        }
        if (!this.ingredients.contains(ingredient)) {
            this.ingredients.add(ingredient);
        }
    }

    @Override
    public String toString() {
        return "RecipeUpdateDTO{" +
                "id=" + id +
                ", recipeName='" + recipeName + '\'' +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", cookingTime=" + cookingTime +
                '}';
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public double getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(double cookingTime) {
        this.cookingTime = cookingTime;
    }
}
