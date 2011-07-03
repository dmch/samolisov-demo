package name.samolisov.servlet.ds.demo;

import javax.servlet.ServletException;

import name.samolisov.servlet.ds.demo.servlet.WelcomeServlet;

import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class WelcomeServletComponent 
{
	private static final String SERVLET_PATH = "/welcome";

    private HttpService _httpService;

    public void bindHttpService(HttpService httpService)
    {
        _httpService = httpService;
    }

    protected void register()
    {
        try
        {
            System.out.println("Staring up sevlet at " + SERVLET_PATH);
            _httpService.registerServlet(SERVLET_PATH, new WelcomeServlet(), null, null);
        }
        catch (ServletException e)
        {
            e.printStackTrace();
        }
        catch (NamespaceException e)
        {
            e.printStackTrace();
        }
    }

    protected void unregister()
    {
        _httpService.unregister(SERVLET_PATH);
    }
}
