package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * junit test class for Coffeemaker rest api calls
 *
 * @author jmbuck4
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * tests api calls to add a recipe, add ingredients to ingredient list, and
     * make the added recipe.
     *
     * @throws Exception
     *             handles exception thrown by mvc.perform()
     */
    @Test
    @Transactional
    public void testAPI () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        /* Figure out if the recipe we want is present */
        if ( !recipe.contains( "recipe1" ) ) {
            final Recipe r = new Recipe();
            r.addIngredient( new Ingredient( "CHOCOLATE", 1 ) );
            r.addIngredient( new Ingredient( "COFFEE", 2 ) );
            r.addIngredient( new Ingredient( "MILK", 3 ) );
            r.addIngredient( new Ingredient( "SUGAR", 4 ) );
            r.setPrice( 5 );
            r.setName( "recipe1" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        }
        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( recipe.contains( "recipe1" ) );

        final Inventory inven = new Inventory();
        inven.addIngredient( new Ingredient( "CHOCOLATE", 1 ) );
        inven.addIngredient( new Ingredient( "COFFEE", 2 ) );
        inven.addIngredient( new Ingredient( "MILK", 3 ) );
        inven.addIngredient( new Ingredient( "SUGAR", 4 ) );
        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( inven ) ) ).andExpect( status().isOk() );

        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "recipe1" ) )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( 100 ) ) )
                .andExpect( status().isOk() ).andDo( print() );
        mvc.perform(
                delete( String.format( "/api/v1/recipes/%s", "recipe1" ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andDo( print() );

    }

    /**
     * api calls to add a recipe, and gets the recipe by name from the
     * controller
     *
     * @throws Exception
     *             handles exception thrown by mvc.perform()
     */
    @Test
    @Transactional
    public void testGetAPI () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        /* Figure out if the recipe we want is present */
        if ( !recipe.contains( "recipe1" ) ) {
            final Recipe r = new Recipe();
            r.addIngredient( new Ingredient( "CHOCOLATE", 50 ) );
            r.addIngredient( new Ingredient( "COFFEE", 50 ) );
            r.addIngredient( new Ingredient( "MILK", 50 ) );
            r.addIngredient( new Ingredient( "SUGAR", 50 ) );
            r.setPrice( 50 );
            r.setName( "recipe1" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
        }
        recipe = mvc.perform( get( String.format( "/api/v1/recipes/%s", "recipe1" ) ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertTrue( recipe.contains( "recipe1" ) );
        mvc.perform(
                delete( String.format( "/api/v1/recipes/%s", "recipe1" ) ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andDo( print() );

    }
}
