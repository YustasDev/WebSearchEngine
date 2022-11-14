package com.example.taco_cloud.controllers;

import com.example.taco_cloud.data.IngredientRepository;
import com.example.taco_cloud.dto.Ingredient;
import com.example.taco_cloud.dto.Taco;
import com.example.taco_cloud.dto.TacoOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

    @Slf4j
    @Controller
    @RequestMapping("/design")
    @SessionAttributes("tacoOrder")
    public class DesignTacoController {

        @Autowired
        private final IngredientRepository ingredientRepo;

        public DesignTacoController(IngredientRepository ingredientRepo) {
            this.ingredientRepo = ingredientRepo;
        }

        @ModelAttribute
        public void addIngredientsToModel(Model model) {
            Iterable<Ingredient> ingredientsIterable = ingredientRepo.findAll();
            List<Ingredient> ingredients = new ArrayList<>();
            ingredientsIterable.forEach(ingredients::add);

            Ingredient.Type[] types = Ingredient.Type.values();
            for (Ingredient.Type type : types) {
                model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
            }
        }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @GetMapping
    public String showDesignForm() {
        return "design";
    }


    private Iterable<Ingredient> filterByType(
            List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getTypeingredient().equals(type))
                .collect(Collectors.toList());
    }

        @PostMapping
        public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
            if (errors.hasErrors()) {
                return "design";
            }
            tacoOrder.addTaco(taco);
            log.info("Processing taco: {}", taco);
            return "redirect:/orders/current";
        }




}