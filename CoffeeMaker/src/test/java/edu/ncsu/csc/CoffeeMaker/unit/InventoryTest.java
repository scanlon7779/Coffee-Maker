package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @Before
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        final Ingredient coffee = new Ingredient( "COFFEE", 500 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 500 );
        final Ingredient tea = new Ingredient( "TEA", 500 );

        ivt.addIngredient( coffee );
        ivt.addIngredient( chocolate );
        ivt.addIngredient( tea );

        inventoryService.save( ivt );
    }

    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient coffee = new Ingredient( "COFFEE", 20 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 10 );
        recipe.addIngredient( chocolate );
        recipe.addIngredient( coffee );

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assert.assertEquals( 480, (int) i.getIngredients().get( 0 ).getAmount() );
        Assert.assertEquals( 490, (int) i.getIngredients().get( 1 ).getAmount() );
        Assert.assertEquals( 500, (int) i.getIngredients().get( 2 ).getAmount() );

        final Recipe recipe2 = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient coffee2 = new Ingredient( "COFFEE", 500 );
        final Ingredient chocolate2 = new Ingredient( "CHOCOLATE", 500 );
        recipe2.addIngredient( chocolate2 );
        recipe2.addIngredient( coffee2 );

        recipe2.setPrice( 5 );

        assertFalse( i.useIngredients( recipe2 ) );

    }

    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();

        final Ingredient coffee = new Ingredient( "COFFEE", 5 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 2 );
        final Ingredient tea = new Ingredient( "TEA", 20 );
        ivt.addIngredient( tea );
        ivt.addIngredient( chocolate );
        ivt.addIngredient( coffee );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for coffee", 505,
                (int) ivt.getIngredients().get( 0 ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values for tea", 520,
                (int) ivt.getIngredients().get( 2 ).getAmount() );
        Assert.assertEquals( "Adding to the inventory should result in correctly-updated values chocolate", 502,
                (int) ivt.getIngredients().get( 1 ).getAmount() );

    }

    /**
     * test method for enoughIngredients
     */
    @Test
    @Transactional
    public void testEnoughIngredients () {
        final Inventory i = inventoryService.getInventory();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient coffee = new Ingredient( "COFFEE", 20 );
        final Ingredient chocolate = new Ingredient( "CHOCOLATE", 501 );
        recipe.addIngredient( chocolate );
        recipe.addIngredient( coffee );

        recipe.setPrice( 5 );

        assertFalse( i.enoughIngredients( recipe ) );

        final Recipe recipe2 = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient coffee2 = new Ingredient( "COFFEE", 501 );
        final Ingredient chocolate2 = new Ingredient( "CHOCOLATE", 10 );
        recipe2.addIngredient( chocolate2 );
        recipe2.addIngredient( coffee2 );

        assertFalse( i.enoughIngredients( recipe2 ) );

        final Recipe recipe3 = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        final Ingredient coffee3 = new Ingredient( "COFFEE", 10 );
        final Ingredient chocolate3 = new Ingredient( "CHOCOLATE", 10 );
        recipe3.addIngredient( chocolate3 );
        recipe3.addIngredient( coffee3 );

        assertTrue( i.enoughIngredients( recipe3 ) );

    }

    /**
     * Test method for toString
     */
    @Test
    @Transactional
    public void testToString () {
        final Inventory ivt = inventoryService.getInventory();
        Assert.assertEquals( "COFFEE: 500\nCHOCOLATE: 500\nTEA: 500\n", ivt.toString() );
        final Ingredient coffee = new Ingredient( "COFFEE", 40 );
        ivt.addIngredient( coffee );
        Assert.assertEquals( "COFFEE: 540\nCHOCOLATE: 500\nTEA: 500\n", ivt.toString() );
        inventoryService.save( ivt );

    }

}
