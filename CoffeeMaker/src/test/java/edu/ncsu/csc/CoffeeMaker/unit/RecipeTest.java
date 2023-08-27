package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService     service;

    @Autowired
    private IngredientService ingService;

    @Before
    public void setup () {
        service.deleteAll();

    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        final Ingredient coffee = new Ingredient( "COFFEE", 1 );

        r1.addIngredient( coffee );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );

        final Ingredient coffee2 = new Ingredient( "COFFEE", 1 );
        final Ingredient milk2 = new Ingredient( "MILK", 1 );
        final Ingredient sugar2 = new Ingredient( "SUGAR", 1 );
        final Ingredient chocolate2 = new Ingredient( "CHOCOLATE", 1 );

        r2.addIngredient( coffee2 );
        r2.addIngredient( milk2 );
        r2.addIngredient( sugar2 );
        r2.addIngredient( chocolate2 );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, recipes.size() );

        Assert.assertEquals( "The retrieved recipe should match the created one", r1, recipes.get( 0 ) );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", -10 );
        r1.addIngredient( coffee );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( coffee );
        r2.addIngredient( milk );
        r2.addIngredient( sugar );
        r2.addIngredient( chocolate );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assert.assertEquals(
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved",
                    0, service.count() );
        }
        catch ( final Exception e ) {
            Assert.assertTrue( e instanceof ConstraintViolationException );
        }

    }

    @Test
    @Transactional
    public void testAddRecipe1 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ings );

        service.save( r1 );

        Assert.assertEquals( "There should only one recipe in the CoffeeMaker", 1, service.findAll().size() );
        Assert.assertNotNull( service.findByName( name ) );

    }

    /* Test2 is done via the API for different validation */

    @Test
    @Transactional
    public void testAddRecipe3 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );

        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, -50, ings );

        try {
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative price", service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe5 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", -10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ings );

        try {
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of an ingredient",
                    service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe7 () {

        final Ingredient coffee = new Ingredient( "COFFEE", -10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, ings );

        try {
            service.save( r1 );

            Assert.assertNull( "A recipe was able to be created with a negative amount of another ingredient",
                    service.findByName( name ) );
        }
        catch ( final ConstraintViolationException cvee ) {
            // expected
        }

    }

    @Test
    @Transactional
    public void testAddRecipe13 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

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

        Assert.assertEquals( "Creating two recipes should result in two recipes in the database", 2, service.count() );

    }

    @Test
    @Transactional
    public void testAddRecipe14 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

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

    }

    @Test
    @Transactional
    public void testDeleteRecipe1 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        service.save( r1 );

        Assert.assertEquals( "There should be one recipe in the database", 1, service.count() );

        service.delete( r1 );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );
    }

    @Test
    @Transactional
    public void testDeleteRecipe2 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

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

        Assert.assertEquals( "There should be three recipes in the database", 3, service.count() );

        service.deleteAll();

        Assert.assertEquals( "`service.deleteAll()` should remove everything", 0, service.count() );

    }

    @Test
    @Transactional
    public void testEditRecipe1 () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        Assert.assertEquals( "There should be no Recipes in the CoffeeMaker", 0, service.findAll().size() );

        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Coffee" );

        Assert.assertEquals( 70, (int) retrieved.getPrice() );
        Assert.assertEquals( ings, retrieved.getIngredients() );

        Assert.assertEquals( "Editing a recipe shouldn't duplicate it", 1, service.count() );

    }

    @Test
    @Transactional
    public void testCheckRecipie () {
        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        Assert.assertTrue( r1.checkRecipe() );
        final ArrayList<Ingredient> ings2 = new ArrayList<Ingredient>();
        final Ingredient coffee2 = new Ingredient( "COFFEE", 0 );
        final Ingredient milk2 = new Ingredient( "MILK", 0 );
        final Ingredient sugar2 = new Ingredient( "SUGAR", 0 );
        final Ingredient chocolate2 = new Ingredient( "CHOCOLATE", 0 );
        ings2.add( coffee2 );
        ings2.add( milk2 );
        ings2.add( sugar2 );
        ings2.add( chocolate2 );
        final Recipe r2 = createRecipe( "Coffee", 0, ings2 );
        Assert.assertFalse( r2.checkRecipe() );
    }

    @Test
    @Transactional
    public void testEquals () {

        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        final Recipe r2 = createRecipe( "Coffee", 50, ings );
        Assert.assertTrue( r1.equals( r2 ) );
    }

    @Test
    @Transactional
    public void testHashCode () {
        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        assertTrue( r1.hashCode() > Integer.MIN_VALUE );
    }

    @Test
    @Transactional
    public void testToString () {
        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        Assert.assertEquals(
                "Coffee Ingredient [id=null, name=COFFEE, amount=10] Ingredient [id=null, name=MILK, amount=10] Ingredient [id=null, name=SUGAR, amount=10] Ingredient [id=null, name=CHOCOLATE, amount=10]",
                r1.toString() );
    }

    @Test
    @Transactional
    public void testGetIngredient () {
        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        Assert.assertEquals( ings.get( 0 ), r1.getIngredient( "COFFEE" ) );
    }

    @Test
    @Transactional
    public void testGetIngredients () {
        final Ingredient coffee = new Ingredient( "COFFEE", 10 );
        final Ingredient milk = new Ingredient( "MILK", 10 );
        final Ingredient sugar = new Ingredient( "SUGAR", 10 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        final ArrayList<Ingredient> ings = new ArrayList<Ingredient>();
        ings.add( coffee );
        ings.add( milk );
        ings.add( sugar );
        ings.add( chocolate );
        final Recipe r1 = createRecipe( "Coffee", 50, ings );
        Assert.assertEquals( ings, r1.getIngredients() );
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
