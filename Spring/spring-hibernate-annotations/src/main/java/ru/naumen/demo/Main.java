/*
 * $$Id$$
 */
package ru.naumen.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.naumen.demo.entity.MyEntity;
import ru.naumen.demo.services.IMyEntityService;

/**
 *  <p>Created 17.06.2009
 *		@author psamolisov
 */
public class Main
{
    public static void main(String[] args)
    {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        IMyEntityService service = (IMyEntityService) ctx.getBean("entityService");

        MyEntity entity = new MyEntity();
        entity.setName("Pavel");

        service.saveEntity(entity);
    }
}
