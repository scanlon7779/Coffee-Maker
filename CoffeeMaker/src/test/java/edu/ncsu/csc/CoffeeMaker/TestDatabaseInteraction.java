package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests Database Interactions
 *
 * @author rishipanthee
 *
 */
@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )

public class TestDatabaseInteraction {

    /** Recipie Service to manage the recipies for testing */
    @Autowired
    private RecipeService recipeService;

    /**
     * Tests Saving/Editing/Deleting Recipie
     */
    @Test
    @Transactional
    public void testRecipes () {
        recipeService.deleteAll();
        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient( "CHOCOLATE", 1 ) );
        r.addIngredient( new Ingredient( "COFFEE", 1 ) );
        r.addIngredient( new Ingredient( "MILK", 1 ) );
        r.addIngredient( new Ingredient( "SUGAR", 1 ) );
        r.setName( "R" );
        r.setPrice( 10 );
        recipeService.save( r );

        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getIngredient( "CHOCOLATE" ), dbRecipe.getIngredient( "CHOCOLATE" ) );
        assertEquals( r.getIngredient( "COFFEE" ), dbRecipe.getIngredient( "COFFEE" ) );
        assertEquals( r.getIngredient( "MILK" ), dbRecipe.getIngredient( "MILK" ) );
        assertEquals( r.getIngredient( "SUGAR" ), dbRecipe.getIngredient( "SUGAR" ) );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );

        final Recipe dbRecipe2 = recipeService.findByName( "R" );
        assertEquals( r.getName(), dbRecipe2.getName() );
        assertEquals( r.getIngredient( "CHOCOLATE" ), dbRecipe2.getIngredient( "CHOCOLATE" ) );
        assertEquals( r.getIngredient( "COFFEE" ), dbRecipe2.getIngredient( "COFFEE" ) );
        assertEquals( r.getIngredient( "MILK" ), dbRecipe2.getIngredient( "MILK" ) );
        assertEquals( r.getIngredient( "SUGAR" ), dbRecipe2.getIngredient( "SUGAR" ) );
        assertEquals( r.getPrice(), dbRecipe2.getPrice() );

        dbRecipe.setPrice( 15 );
        // dbRecipe.
        // dbRecipe.setSugar( 12 );
        recipeService.save( dbRecipe );

        final List<Recipe> dbRecipes3 = recipeService.findAll();
        assertEquals( 1, dbRecipes3.size() );
        assertEquals( 15, dbRecipes3.get( 0 ).getPrice() );
        // assertEquals( 12, dbRecipes3.get( 0 ).getIngredient(
        // IngredientType.SUGAR ) );

        recipeService.delete( r );
        final List<Recipe> dbRecipes4 = recipeService.findAll();
        assertEquals( 0, dbRecipes4.size() );

        final Recipe dbRecipe5 = recipeService.findByName( "R" );
        assertNull( dbRecipe5 );

    }

}
