package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 * @author Jmbuck4
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long                   id;

    /** Recipe name */
    private String                 name;

    /** Recipe price */
    @Min ( 0 )
    private Integer                price;

    /** list of recipe ingredients */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private final List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        if ( ingredients.size() == 0 ) {
            return false;
        }
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getAmount() <= 0 ) {
                return false;
            }
            if ( price <= 0 ) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * adds a new ingredient to the ingredient list
     *
     * @param ing
     *            the ingredient being added
     */
    public void addIngredient ( final Ingredient ing ) {
        ingredients.add( ing );
    }

    /**
     * returns the list of ingredients contained in the recipe
     *
     * @return list of ingredients in the recipe
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * getter for a singe ingredient
     *
     * @param ing
     *            ingredient being gotten
     * @return RecipeIngredient
     */
    public Ingredient getIngredient ( final String ing ) {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getName() == ing ) {
                return ingredients.get( i );
            }
        }
        return null;
    }

    /**
     * Returns the name of the recipe and the name of all ingredients in the
     * recipe..
     *
     * @return String
     */
    @Override
    public String toString () {
        String out = name;
        for ( int i = 0; i < ingredients.size(); i++ ) {
            out += " ";
            out += ingredients.get( i ).toString();
        }
        return out;
    }

    @Override
    public int hashCode () {
        return Objects.hash( ingredients, name, price );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        return Objects.equals( name, other.name );
    }

}
