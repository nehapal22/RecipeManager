package gr.recipemanagement.viewcontroller;

import gr.recipemanagement.dao.ingredientdao.IngredientDAOImpl;
import gr.recipemanagement.dao.recipedao.RecipeDAOImpl;
import gr.recipemanagement.dao.recipeingredientdao.RecipeIngredientDAOImpl;
import gr.recipemanagement.dto.recipedto.RecipeInsertDTO;
import gr.recipemanagement.factory.SQLStorageFactory;
import gr.recipemanagement.model.Ingredient;
import gr.recipemanagement.model.Recipe;
import gr.recipemanagement.service.exceptions.IngredientNotFoundDAOException;
import gr.recipemanagement.service.exceptions.RecipeNotFoundDAOException;
import gr.recipemanagement.service.recipeservice.IRecipeService;
import gr.recipemanagement.service.recipeservice.RecipeServiceImpl;
import gr.recipemanagement.validator.RecipeValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import static gr.recipemanagement.viewcontroller.Menu.createStyledButton;


public class AddRecipe extends JFrame {
    private static final long serialVersionUID = 123457;
    private JPanel contentPane;
    private JTextField recipeNameField;
    private JTextField ingredientsField;
    private JTextField instructionsField;
    private JTextField cookingTimeField;

    private JButton saveButton;
    private JButton cancelButton;
    private IngredientDAOImpl ingredientDAO;
    private RecipeDAOImpl recipeDAO;
    private RecipeIngredientDAOImpl recipeIngredientDAO;

    SQLStorageFactory factory = new SQLStorageFactory();
    IRecipeService recipeService = new RecipeServiceImpl(factory);

    public AddRecipe() {
        setTitle("Add Recipe");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.recipeDAO = new RecipeDAOImpl();
        this.ingredientDAO = new IngredientDAOImpl();
        this.recipeIngredientDAO = new RecipeIngredientDAOImpl();

        setupUI();
    }

    private void setupUI() {
        Color backgroundColor = Menu.backgroundColor;
        Color buttonColor = Menu.buttonColor;
        Font font = Menu.buttonFont;

        setSize(500, 400);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBackground(backgroundColor);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        saveButton = createStyledButton("Save", buttonColor, font);
        cancelButton = createStyledButton("Cancel", buttonColor, font);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));  // aligns buttons to the right
        buttonPanel.setBackground(backgroundColor);  // set the background color to match the contentPane
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        recipeNameField = new JTextField(15);
        ingredientsField = new JTextField(15);
        instructionsField = new JTextField(15);
        cookingTimeField = new JTextField(15);

        // Add some rigid areas to create spacing between components
        contentPane.add(new JLabel("Recipe Name:"));
        contentPane.add(recipeNameField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(new JLabel("Ingredients (Separate with ,): "));
        contentPane.add(ingredientsField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(new JLabel("Instructions: "));
        contentPane.add(instructionsField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPane.add(new JLabel("Cooking Time in Minutes:"));
        contentPane.add(cookingTimeField);
        contentPane.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Add buttonPanel to the contentPane
        contentPane.add(buttonPanel);

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String recipeName;
                String ingredients;
                String instructions;
                double cookingTime;
                Map<String, String> recipeErrors;
                RecipeInsertDTO dto;

                try {
                    recipeName = recipeNameField.getText().trim();
                    ingredients = ingredientsField.getText();
                    instructions = instructionsField.getText().trim();
                    
                    // Validate cooking time format
                    try {
                        cookingTime = Double.parseDouble(cookingTimeField.getText());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid number for cooking time", 
                                "Validation Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    dto = new RecipeInsertDTO();
                    dto.setRecipeName(recipeName);
                    dto.setInstructions(instructions);
                    dto.setCookingTime(cookingTime);

                    // Validate Data
                    recipeErrors = RecipeValidator.validate(dto);

                    if(!recipeErrors.isEmpty()){
                        StringBuilder errorMessage = new StringBuilder();
                        if(recipeErrors.get("recipename") != null) {
                            errorMessage.append("Recipe Name: ").append(recipeErrors.get("recipename")).append("\n");
                        }
                        if(recipeErrors.get("instructions") != null) {
                            errorMessage.append("Instructions: ").append(recipeErrors.get("instructions")).append("\n");
                        }
                        JOptionPane.showMessageDialog(null, errorMessage.toString(), "Validation Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Attempt to insert the recipe
                    Recipe newRecipe = recipeService.insertRecipe(dto);
                    
                    if(newRecipe == null){
                        JOptionPane.showMessageDialog(null, "Failed to create recipe", 
                                "Error!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int recipeId = newRecipe.getId();

                    // Only proceed if we have ingredients to add
                    if (!ingredients.trim().isEmpty()) {
                        // Set ingredients
                        String[] allIngredients = ingredients.replaceAll("\\s", "").split(",");

                        for(String ingredientName : allIngredients){
                            if (ingredientName.isEmpty()) continue;
                            
                            int ingredientId;

                            // Check if ingredient exists
                            Ingredient existingIngredient = ingredientDAO.getByName(ingredientName);

                            if(existingIngredient != null){
                                // In case it exists, get its ID
                                ingredientId = existingIngredient.getId();
                            } else {
                                // If it doesn't exist, insert it and get the new ID
                                ingredientId = ingredientDAO.insert(ingredientName);
                            }

                            recipeIngredientDAO.linkRecipeAndIngredient(recipeId, ingredientId);
                        }
                    }

                    JOptionPane.showMessageDialog(null, "Recipe \"" + newRecipe.getRecipeName() 
                            + "\" was inserted successfully", "Successful Insertion!", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear the fields after successful insertion
                    recipeNameField.setText("");
                    ingredientsField.setText("");
                    instructionsField.setText("");
                    cookingTimeField.setText("");
                    
                } catch (RecipeNotFoundDAOException ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(), 
                            "Error! Recipe not found.", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (IngredientNotFoundDAOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), 
                            "Error! Issue with ingredient.", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Unexpected error: " + ex.getMessage(), 
                            "Error!", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        add(contentPane);
    }
}