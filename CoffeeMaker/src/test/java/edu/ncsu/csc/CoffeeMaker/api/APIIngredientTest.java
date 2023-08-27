package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * test class for ingredient API
 *
 * @author jmbuck4
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    @Test
    @Transactional
    public void testCreateIngredient () throws Exception {
        service.deleteAll();
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        final String response = mvc
                .perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( i ) ) )
                .andDo( print() ).andReturn().getResponse().getContentAsString();
        Assert.assertTrue( response.contains( "Ingredient with the name COFFEE already exists" ) );
    }

    @Test
    @Transactional
    public void testDeleteIngredient () throws Exception {
        service.deleteAll();
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        final String response = mvc
                .perform( delete( "/api/v1/ingredients/MILK" ).contentType( MediaType.APPLICATION_JSON ) )
                .andDo( print() ).andReturn().getResponse().getContentAsString();

        Assert.assertTrue( response.contains( "No ingredient found for name MILK" ) );

        mvc.perform( delete( "/api/v1/ingredients/COFFEE" ).contentType( MediaType.APPLICATION_JSON ) );

        Assert.assertEquals( 0, (int) service.count() );
    }

    @Test
    @Transactional
    public void testGetIngredient () throws Exception {
        service.deleteAll();
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) );

        Assert.assertEquals( 1, (int) service.count() );

        String response = mvc.perform( get( String.format( "/api/v1/ingredients/%s", "COFFEE" ) ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assert.assertTrue( response.contains( "COFFEE" ) );

        response = mvc.perform( get( String.format( "/api/v1/ingredients/%s", "MILK" ) ) ).andDo( print() ).andReturn()
                .getResponse().getContentAsString();
        Assert.assertTrue( response.contains( "No ingredient found with name MILK" ) );
    }

    @Test
    @Transactional
    public void testGetIngredients () throws Exception {
        service.deleteAll();

        final Ingredient i = new Ingredient( "COFFEE", 1 );
        final Ingredient i1 = new Ingredient( "MILK", 1 );
        final Ingredient i2 = new Ingredient( "SUGAR", 1 );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i1 ) ) );

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i2 ) ) );

        Assert.assertEquals( 3, (int) service.count() );

        final String response = mvc.perform( get( String.format( "/api/v1/ingredients" ) ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assert.assertTrue( response.contains( "COFFEE" ) );
        Assert.assertTrue( response.contains( "MILK" ) );
        Assert.assertTrue( response.contains( "SUGAR" ) );
    }

}
