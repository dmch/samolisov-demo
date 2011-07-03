/*
 * $$Id$$
 */
package ru.naumen.demo.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ru.naumen.demo.entity.MyEntity;

/**
 *  <p>Created 17.06.2009
 *		@author psamolisov
 */
public class EntityDao extends HibernateDaoSupport implements IEntityDao
{
    @Override
    public void save(MyEntity entity)
    {
        getHibernateTemplate().save(entity);
    }
}
