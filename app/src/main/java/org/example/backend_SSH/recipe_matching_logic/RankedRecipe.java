package org.example.backend_SSH.recipe_matching_logic;

import org.example.recipes_system.Recipe;

public class RankedRecipe {
        private Recipe recipe;
    private double qualityScore;

    public RankedRecipe(Recipe recipe, double qualityScore) {
        this.recipe = recipe;
        this.qualityScore = qualityScore;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public double getQualityScore() {
        return qualityScore;
    }
}
