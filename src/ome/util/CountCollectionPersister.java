/*
 *   $Id$
 *
 *   Copyright 2008 Glencoe Software, Inc. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */

package ome.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.HibernateException;
import org.hibernate.cache.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.mapping.Collection;
import org.hibernate.persister.collection.BasicCollectionPersister;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * {@link CollectionPersister} which knows how to handle the count views
 * generated by dsl/resources/ome/dsl/views.dm. In general, attempts to data
 * modifications and properly loads the counts.
 */
public class CountCollectionPersister extends BasicCollectionPersister {

    private static Logger log = LoggerFactory.getLogger(CountCollectionPersister.class);

    public CountCollectionPersister(Collection collection,
            CollectionRegionAccessStrategy regions, Configuration cfg,
            SessionFactoryImplementor factory) {
        super(collection, regions, cfg, factory);
    }

    @Override
    public void insertRows(PersistentCollection collection, Serializable id,
            SessionImplementor session) throws HibernateException {
        // Do nothing
    }

    @Override
    protected int doUpdateRows(Serializable id,
            PersistentCollection collection, SessionImplementor session)
            throws HibernateException {
        return 0;
    }

    @Override
    public void updateRows(PersistentCollection collection, Serializable id,
            SessionImplementor session) throws HibernateException {
        // Do nothing
    }

    @Override
    public void deleteRows(PersistentCollection collection, Serializable id,
            SessionImplementor session) throws HibernateException {
        // Do nothing
    }

    @Override
    public void remove(Serializable id, SessionImplementor session)
            throws HibernateException {
        // Do nothing
    }
}