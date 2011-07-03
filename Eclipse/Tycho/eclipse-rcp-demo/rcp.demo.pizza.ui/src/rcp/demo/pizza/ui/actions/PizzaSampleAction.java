package rcp.demo.pizza.ui.actions;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import rcp.demo.pizza.processor.Pizza;
import rcp.demo.pizza.processor.PizzaProcessor;

public class PizzaSampleAction implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        List<Pizza> list = PizzaProcessor.getAllPizzas();
        StringBuffer buffer = new StringBuffer();

        for (Pizza pizza : list)
            buffer.append(pizza.getName()).append(" (").append(pizza.getCost()).append(")\n");

        MessageDialog.openInformation(window.getShell(), "Installed pizza toppings", buffer.toString());
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
