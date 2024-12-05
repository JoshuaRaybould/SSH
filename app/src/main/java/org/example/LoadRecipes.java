package org.example;

public class LoadRecipes {

    Recipe recipe1 = new Recipe("Lemon Chicken", 
                                "Marinate chicken with lemon juice, minced garlic, olive oil and pepper. Bake or pan-fry until it's golden and cooked through.",
                                new String[] {"Chicken","Lemons","Garlic","Oil","Black Pepper"},
                                new int[] {1,5,3,10,10});

    Recipe recipe2 = new Recipe("Tomato and Basil Pasta", 
                                "Cook pasta until al dente. Fry off garlic in olive oil and then add tomato puree and pepper. Mix the pasta with the sauce and stir.",
                                new String[] {"Pasta","Garlic","Oil","Tomato Puree","Black Pepper"},
                                new int[] {500,3,10,7,7});

    Recipe recipe3 = new Recipe("Veg Stir Fry", 
                                "Heat oil in a pan or a wok. Add and stir fry the chopped vegetables until nice and tender. Add soy sauce and any other condiments that you wish to add and serve.",
                                new String[] {"Salad","Broccoli","Oil","Carrots","Bell Peppers", "Soy Sauce"},
                                new int[] {50,30,7,10,7,6});
        

    public Recipe[] ReturnRecipes(){
        return new Recipe[] {recipe1, recipe2,recipe3};
    }

    public LoadRecipes(){

    }
}
