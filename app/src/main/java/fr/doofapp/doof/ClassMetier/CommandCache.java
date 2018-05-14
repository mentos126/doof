package fr.doofapp.doof.ClassMetier;

import java.util.List;

public class CommandCache {

    private CommandCache (){}
    private static CommandCache INSTANCE = null;
    public static synchronized CommandCache getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CommandCache();
        }
        return INSTANCE;
    }

    private static Meal meal;
    private static List<Meal> meals;
    private static List<Integer> prices;
    private static List<String> allergens;
    private static String idOrder;

    public static Meal getMeal() {return meal;}
    public static void setMeal(Meal meal) {CommandCache.meal = meal;}

    public static List<Meal> getMeals() {return meals;}
    public static void setMeals(List<Meal> meals) {CommandCache.meals = meals;}

    public static List<Integer> getPrices() {return prices;}
    public static void setPrices(List<Integer> prices) {CommandCache.prices = prices;}

    public static List<String> getAllergens() {return allergens;}
    public static void setAllergens(List<String> allergens) {CommandCache.allergens = allergens;}

    public static String getIdOrder() {return idOrder;}
    public static void setIdOrder(String idOrder) {CommandCache.idOrder = idOrder;}
}
