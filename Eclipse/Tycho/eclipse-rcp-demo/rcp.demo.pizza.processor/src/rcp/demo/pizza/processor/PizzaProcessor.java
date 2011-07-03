package rcp.demo.pizza.processor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class PizzaProcessor {   
    private static final String COST = "cost";
    private static final String NAME = "name";
    private static final String PIZZA_EXTENSION_ID = "rcp.demo.pizza";

    public static List<Pizza> getAllPizzas() {
        List<Pizza> result = new ArrayList<Pizza>();

        IExtensionRegistry reg = Platform.getExtensionRegistry();
        IConfigurationElement[] extensions = reg.getConfigurationElementsFor(PIZZA_EXTENSION_ID);

        for (IConfigurationElement extension : extensions) {
            result.add(new Pizza(extension.getAttribute(NAME), Float.parseFloat(extension.getAttribute(COST))));
        }

        return result;
    }
}
