/*
 *   $Id$
 *
 *   Copyright 2006 University of Dundee. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */
package ome.model.internal;

// Java imports
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import ome.model.IObject;
import ome.model.meta.ExternalInfo;
import ome.util.Filter;
import ome.util.Filterable;

import org.hibernate.annotations.Parent;

/**
 * value type for low-level (row-level) details for all
 * {@link ome.model.IObject} objects. Details instances are given special
 * treatment through the Omero system, especially during {@link ome.api.IUpdate 
 * update}.
 * 
 * @author Josh Moore &nbsp;&nbsp;&nbsp;&nbsp; <a
 *         href="mailto:josh.moore@gmx.de">josh.moore@gmx.de</a>
 * @version 3.0 <small> (<b>Internal version:</b> $Rev$ $Date$) </small>
 * @since 3.0
 * @author josh
 * @see ome.api.IUpdate
 */
@Embeddable
public class GlobalDetails implements Filterable, Serializable {

    private static final long serialVersionUID = 1176546684904748976L;

    public final static String PERMISSIONS = "Details_permissions";

    public final static String EXTERNALINFO = "Details_externalInfo";

    protected IObject _context;

    protected Permissions _perms;

    protected ExternalInfo _externalInfo;

    // Non-entity fields
    Set<String> _filteredCollections;

    Map _counts;

    /** default constructor. Leaves values null to save resources. */
    public GlobalDetails() {
    }

    /** copy-constructor */
    public GlobalDetails(GlobalDetails copy) {
        setContext(copy.getContext());
        setPermissions(new Permissions().revokeAll(copy.getPermissions()));
        // Non-entity fields
        _filteredCollections = copy.filteredSet();
        _counts = copy.getCounts() == null ? null : new HashMap(copy
                .getCounts());
    }

    public GlobalDetails shallowCopy() {
        GlobalDetails newDetails = new GlobalDetails();
        newDetails.setPermissions(this.getPermissions() == null ? null
                : new Permissions().revokeAll(this.getPermissions()));
        newDetails.setExternalInfo(this.getExternalInfo() == null ? null
                : new ExternalInfo(this.getExternalInfo().getId(), false));
        newDetails._filteredCollections = this.filteredSet();
        newDetails.setCounts(this.getCounts() == null ? null : new HashMap(this
                .getCounts()));
        return newDetails;
    }

    public GlobalDetails newInstance() {
        return new GlobalDetails();
    }

    // Loaded&Filtering methods
    // ===========================================================
    /**
     * consider the collection named by <code>collectionName</code> to be a
     * "filtered" representation of the DB. This collection should not be saved,
     * at most compared with the current DB to find <em>added</em> entities.
     */
    public void addFiltered(String collectionName) {
        if (_filteredCollections == null) {
            _filteredCollections = new HashSet<String>();
        }

        _filteredCollections.add(collectionName);
    }

    /**
     * consider all the collections named by the elements of collection to be a
     * "filtered" representation of the DB. This collection should not be saved,
     * at most compared with the current DB to find <em>added</em> entities.
     */
    public void addFiltered(Collection<String> collection) {
        if (_filteredCollections == null) {
            _filteredCollections = new HashSet<String>();
        }

        _filteredCollections.addAll(collection);
    }

    /**
     * Was this collection filtered during creation? If so, it should not be
     * saved to the DB.
     */
    public boolean isFiltered(String collectionName) {
        if (_filteredCollections == null) {
            return false;
        }
        if (_filteredCollections.contains(collectionName)) {
            return true;
        }
        return false;
    }

    /**
     * all currently marked collections are released. The space taken up by the
     * collection is also released.
     */
    public void clearFiltered() {
        _filteredCollections = null;
    }

    /**
     * the count of collections which were filtered.
     * 
     * @return number of String keys in the filtered set.
     */
    public int filteredSize() {
        if (_filteredCollections == null) {
            return 0;
        }
        return _filteredCollections.size();
    }

    /**
     * copy of the current collection of filtered names. Changes to this
     * collection are not propagated.
     * 
     * @return filtered set copy.
     */
    public Set<String> filteredSet() {
        if (_filteredCollections == null) {
            return new HashSet<String>();
        }
        return new HashSet<String>(_filteredCollections);
    }

    // ~ Other
    // ===========================================================
    /**
     * reference to the entity which this Details is contained in. This value is
     * <em>not</em> maintained by the backend but is an internal mechanism.
     */
    @Parent
    // TODO
    public IObject getContext() {
        return _context;
    }

    /**
     * set entity to which this Details belongs. This may cause erratic behavior
     * if called improperly.
     * 
     * @param myContext
     *            entity which this Details belongs to
     */
    public void setContext(IObject myContext) {
        _context = myContext;
    }

    /**
     * simple view of the Details. Accesses only the ids of the contained
     * entities
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("GlobalDetails:{");
        toString(sb);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Appends the individual fields for this {@link Class}
     */
    public void toString(StringBuilder sb) {
        sb.append(";perm=");
        sb.append(_perms == null ? null : _perms.toString());
        if (_externalInfo != null) {
            sb.append(";external=" + _externalInfo.getId());
        }
    }

    // Getters & Setters
    // ===========================================================

    /**
     * Permissions is a component embedded into the Details component. Similar
     * rules apply as to Details, but it is <em>not</em> suggested that users
     * attempt to directly query Permissions object as its internal state is not
     * public.
     */
    public Permissions getPermissions() {
        return _perms;
    }

    public void setPermissions(Permissions perms) {
        _perms = perms;
    }

    @ManyToOne
    @JoinColumn(name = "external_id", nullable = true, unique = true, insertable = true, updatable = false)
    // TODO
    public ExternalInfo getExternalInfo() {
        return _externalInfo;
    }

    public void setExternalInfo(ExternalInfo info) {
        _externalInfo = info;
    }

    @Transient
    public Map getCounts() {
        return _counts; // TODO unmodifiable?
    }

    public void setCounts(Map counts) {
        _counts = counts;
    }

    public boolean acceptFilter(Filter filter) {
        setPermissions((Permissions) filter.filter(PERMISSIONS,
                getPermissions()));
        setExternalInfo((ExternalInfo) filter.filter(EXTERNALINFO,
                getExternalInfo()));
        return true;

    }

    // Dynamic Getter/Setter
    protected Map _dynamicFields;

    public Object retrieve(String field) {
        if (field == null) {
            return null;
        } else if (field.equals(PERMISSIONS)) {
            return getPermissions();
        } else if (field.equals(EXTERNALINFO)) {
            return getExternalInfo();
        } else {
            if (_dynamicFields != null) {
                return _dynamicFields.get(field);
            }
            return null;
        }
    }

    public void putAt(String field, Object value) {
        if (field == null) {
            return;
        } else if (field.equals(PERMISSIONS)) {
            setPermissions((Permissions) value);
        } else if (field.equals(EXTERNALINFO)) {
            setExternalInfo((ExternalInfo) value);
        } else {
            if (_dynamicFields == null) {
                _dynamicFields = new HashMap();
            }

            _dynamicFields.put(field, value);
        }
    }

}
