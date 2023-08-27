package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * Ingredient for the coffee maker. Ingredient is tied to the database using
 * Hibernate libraries.
 *
 * @author clscanlo
 */
@Entity
public class Ingredient extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    private String  name;

    /** Amount of recipe */
    @Min ( 0 )
    private Integer amount;

    /**
     * Creates a default ingredient for the coffee maker.
     */
    public Ingredient () {
        this.name = null;
    }

    /**
     * Creates a default recipe for the coffee maker.
     *
     * @param name
     *            the name of the ingredient
     * @param amount
     *            the amount of the ingredient in the inventory
     */
    public Ingredient ( final String name, final int amount ) {
        this.name = name.toUpperCase();
        this.amount = amount;
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkIngredient () {
        return amount >= 0;
    }

    /**
     * Get the ID of the Ingredient
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Ingredient
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Gets the amount the the ingredient in the inventory
     *
     * @return the amount in the inventory
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Sets the amount of the ingredient in the inventory
     *
     * @param amount
     *            The amount of the ingredient in the system
     */
    public void setAmount ( final Integer amount ) {
        this.amount = amount;
    }

    /**
     * Gets the name of the ingredient
     *
     * @return the ingredient name
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of the ingredient to the given name
     *
     * @param name
     *            The name of the ingredient
     */
    public void setName ( final String name ) {
        this.name = name.toUpperCase();
    }

    /**
     * Returns the string of the ingredient
     *
     * @return a string of the ingredient
     */
    @Override
    public String toString () {
        return "Ingredient [id=" + id + ", name=" + name + ", amount=" + amount + "]";
    }

    /**
     * Hash code for the Ingredient
     *
     * @return the hash code of the ingredient
     */
    @Override
    public int hashCode () {
        final int prime = 31;
        Integer result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * Returns if two ingredients are equal or not
     *
     * @return boolean for if they are equal or not
     */
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
        final Ingredient other = (Ingredient) obj;
        return Objects.equals( name, other.name );
    }

}
