package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.repositories.IngredientRepository;

/**
 * Ingredient service class for the ingredient repository
 *
 * @author colinscanlon
 *
 * @author jmbuck4
 */
@Component
@Transactional
public class IngredientService extends Service<Ingredient, Long> {

    /**
     * RecipeRepository, to be autowired in by Spring and provide CRUD
     * operations on Recipe model.
     */
    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    protected JpaRepository getRepository () {
        return ingredientRepository;
    }

    /**
     * Find an ingredient with the provided name
     *
     * @param name
     *            Name of the ingredient to find
     * @return found ingredient, null if none
     */
    public Ingredient findByName ( final String name ) {
        return ingredientRepository.findByName( name );
    }

}
