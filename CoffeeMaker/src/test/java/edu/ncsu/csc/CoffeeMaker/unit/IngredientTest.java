package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

@RunWith ( SpringRunner.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class IngredientTest {

    /**
     * Test with a empty constructor
     */
    @Test
    @Transactional
    void testIngredient () {
        final Ingredient i = new Ingredient();
        assertEquals( "", i.getName() );
        assertEquals( "", i.toString() );
        assertEquals( 0, i.getAmount() );

        i.setName( "COFFEE" );
        assertEquals( "COFFEE", i.getName() );
        assertEquals( "Coffee", i.toString() );

        i.setAmount( 1 );
        assertEquals( 1, i.getAmount() );
    }

    /**
     * Test with a empty constructor
     */
    @Test
    @Transactional
    void testIngredientFilled () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertEquals( "COFFEE", i.getName() );
        assertEquals( "Coffee", i.toString() );
        assertEquals( 1, i.getAmount() );
    }

    /**
     * Test setAmount/getAmount
     */
    @Test
    @Transactional
    void testAmountValid () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertEquals( 1, i.getAmount() );

        i.setAmount( 100 );
        assertEquals( 100, i.getAmount() );
    }

    /**
     * Test setAmount/getAmount with 0
     */
    @Test
    @Transactional
    void testAmountZero () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertEquals( 1, i.getAmount() );

        try {
            i.setAmount( 0 );
            fail();
        }
        catch ( final Exception e ) {
            assertEquals( "", e.getMessage() );
        }

        assertEquals( 1, i.getAmount() );
    }

    /**
     * Test setAmount/getAmount with -1
     */
    @Test
    @Transactional
    void testAmountNegative () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertEquals( 1, i.getAmount() );

        try {
            i.setAmount( -1 );
            fail();
        }
        catch ( final Exception e ) {
            assertEquals( "", e.getMessage() );
        }

        assertEquals( 1, i.getAmount() );
    }

    /**
     * Test get/set Name with valid name
     */
    @Test
    @Transactional
    void testNameValid () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertEquals( "COFFEE", i.getName() );

        i.setName( "LATTE" );
        assertEquals( "LATTE", i.getName() );

    }

    /**
     * Test get/set Name with blank name
     */
    @Test
    @Transactional
    void testNameNull () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertEquals( "Ing1", i.getName() );

        try {
            i.setName( null );
            fail();
        }
        catch ( final Exception e ) {
            assertEquals( "", e.getMessage() );
        }

        assertEquals( "COFFEE", i.getName() );

    }

    /**
     * Test equals true
     */
    @Test
    @Transactional
    void testEqualsTrue () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertTrue( i.equals( i ) );

    }

    /**
     * Test equals false
     */
    @Test
    @Transactional
    void testEqualsFalse () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        final Ingredient i2 = new Ingredient( "LATTE", 1 );
        assertFalse( i.equals( i2 ) );

    }

    /**
     * Test hash code
     */
    @Test
    @Transactional
    void testHashCode () {
        final Ingredient i = new Ingredient( "COFFEE", 1 );
        assertEquals( 0, i.hashCode() );

    }

}
