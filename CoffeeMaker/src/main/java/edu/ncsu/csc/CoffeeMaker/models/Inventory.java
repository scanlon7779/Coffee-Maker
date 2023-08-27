package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long                   id;

    /** list of inventory ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        boolean isEnough = true;
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( r.getIngredient( ingredients.get( i ).getName() ) != null ) {
                if ( ingredients.get( i ).getAmount() < r.getIngredient( ingredients.get( i ).getName() )
                        .getAmount() ) {
                    isEnough = false;
                }
            }
        }

        return isEnough;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        if ( enoughIngredients( r ) ) {
            for ( int i = 0; i < ingredients.size(); i++ ) {
                if ( r.getIngredient( ingredients.get( i ).getName() ) != null ) {
                    ingredients.get( i ).setAmount( ingredients.get( i ).getAmount()
                            - r.getIngredient( ingredients.get( i ).getName() ).getAmount() );
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds ingredients to the inventory
     *
     * @param ingredient
     *            ingredient to be added
     *
     * @return true if successful, false if not
     */
    public boolean addIngredient ( final Ingredient ingredient ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).equals( ingredient ) ) {
                ingredients.get( i ).setAmount( ingredients.get( i ).getAmount() + ingredient.getAmount() );
                return true;

            }
        }
        ingredients.add( ingredient );

        return true;
    }

    /**
     * returns the list of ingredients contained in the inventory
     *
     * @return list of ingredients in the inventory
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            buf.append( ingredients.get( i ).getName() );
            buf.append( ": " );
            buf.append( ingredients.get( i ).getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
