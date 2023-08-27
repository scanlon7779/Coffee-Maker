package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
    }

    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Ingredient coffee = new Ingredient( "COFFEE", 1 );
        final Ingredient milk = new Ingredient( "MILK", 1 );
        final Ingredient sugar = new Ingredient( "SUGAR", 1 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 1 );
        final Recipe r = new Recipe();
        r.addIngredient( coffee );
        r.addIngredient( milk );
        r.addIngredient( sugar );
        r.addIngredient( chocolate );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Ingredient coffee = new Ingredient( "COFFEE", 1 );
        final Ingredient milk = new Ingredient( "MILK", 1 );
        final Ingredient sugar = new Ingredient( "SUGAR", 1 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 1 );
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( coffee );
        recipe.addIngredient( milk );
        recipe.addIngredient( sugar );
        recipe.addIngredient( chocolate );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assert.assertEquals( 1, (int) service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        final Ingredient coffee = new Ingredient( "COFFEE", 1 );
        final Ingredient milk = new Ingredient( "MILK", 1 );
        final Ingredient sugar = new Ingredient( "SUGAR", 1 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 1 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ings );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, ings );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Ingredient coffee = new Ingredient( "COFFEE", 1 );
        final Ingredient milk = new Ingredient( "MILK", 1 );
        final Ingredient sugar = new Ingredient( "SUGAR", 1 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 1 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        service.save( r1 );

        final Ingredient coffee2 = new Ingredient( "COFFEE", 10 );
        final Ingredient milk2 = new Ingredient( "MILK", 10 );
        final Ingredient sugar2 = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate2 = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings2 = new ArrayList<Ingredient>();
        ings.add( coffee2 );
        ings.add( milk2 );
        ings.add( sugar2 );
        ings.add( chocolate2 );

        final Recipe r2 = createRecipe( "Mocha", 50, ings2 );
        service.save( r2 );

        final Ingredient coffee3 = new Ingredient( "COFFEE", 10 );
        final Ingredient milk3 = new Ingredient( "MILK", 10 );
        final Ingredient sugar3 = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate3 = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings3 = new ArrayList<Ingredient>();
        ings.add( coffee3 );
        ings.add( milk3 );
        ings.add( sugar3 );
        ings.add( chocolate3 );

        final Recipe r3 = createRecipe( "Latte", 60, ings3 );
        service.save( r3 );

        Assert.assertEquals( "Creating three recipes should result in three recipes in the database", 3,
                service.count() );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, ings );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assert.assertEquals( "Creating a fourth recipe should not get saved", 3, service.count() );
    }

    private Recipe createRecipe ( final String name, final Integer price, final List<Ingredient> ingredients ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        for ( int i = 0; i < ingredients.size(); i++ ) {
            recipe.addIngredient( ingredients.get( i ) );
        }

        return recipe;
    }

}
