/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008-2011, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.test.manytomanyassociationclass.surrogateid.generated;
import java.util.HashSet;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import org.junit.Test;

import org.hibernate.test.manytomanyassociationclass.AbstractManyToManyAssociationClassTest;
import org.hibernate.test.manytomanyassociationclass.Membership;

import static org.junit.Assert.fail;

/**
 * Tests on many-to-many association using an association class with a surrogate ID that is generated.
 *
 * @author Gail Badner
 */
public class ManyToManyAssociationClassGeneratedIdTest extends AbstractManyToManyAssociationClassTest {
	@Override
	public String[] getMappings() {
		return new String[] { "manytomanyassociationclass/surrogateid/generated/Mappings.hbm.xml" };
	}

	@Override
	public Membership createMembership(String name) {
		return new Membership( name );
	}

	@Test
	public void testRemoveAndAddEqualElement() {
		deleteMembership( getUser(), getGroup(), getMembership() );
		addMembership( getUser(), getGroup(), createMembership( "membership" ) );

		Session s = openSession();
		s.beginTransaction();
		try {
			// The new membership is transient (it has a null surrogate ID), so
			// Hibernate assumes that it should be added to the collection.
			// Inserts are done before deletes, so a ConstraintViolationException
			// will be thrown on the insert because the unique constraint on the
			// user and group IDs in the join table is violated. See HHH-2801.
			s.merge( getUser() );
			s.getTransaction().commit();
			fail( "should have failed because inserts are before deletes");
		}
		catch( ConstraintViolationException ex ) {
			// expected
			s.getTransaction().rollback();
		}
		finally {
			s.close();
		}
	}

	@Test
	public void testRemoveAndAddEqualCollection() {
		deleteMembership( getUser(), getGroup(), getMembership() );
		getUser().setMemberships( new HashSet() );
		getGroup().setMemberships( new HashSet() );
		addMembership( getUser(), getGroup(), createMembership( "membership" ) );

		Session s = openSession();
		s.beginTransaction();
		try {
			// The new membership is transient (it has a null surrogate ID), so
			// Hibernate assumes that it should be added to the collection.
			// Inserts are done before deletes, so a ConstraintViolationException
			// will be thrown on the insert because the unique constraint on the
			// user and group IDs in the join table is violated. See HHH-2801.
			s.merge( getUser() );
			s.getTransaction().commit();
			fail( "should have failed because inserts are before deletes");
		}
		catch( ConstraintViolationException ex ) {
			// expected
			s.getTransaction().rollback();
		}
		finally {
			s.close();
		}
	}

	@Test
	public void testRemoveAndAddEqualElementNonKeyModified() {
		deleteMembership( getUser(), getGroup(), getMembership() );
		Membership membershipNew = createMembership( "membership" );
		addMembership( getUser(), getGroup(), membershipNew );
		membershipNew.setName( "membership1" );

		Session s = openSession();
		s.beginTransaction();
		try {
			// The new membership is transient (it has a null surrogate ID), so
			// Hibernate assumes that it should be added to the collection.
			// Inserts are done before deletes, so a ConstraintViolationException
			// will be thrown on the insert because the unique constraint on the
			// user and group IDs in the join table is violated. See HHH-2801.
			s.merge( getUser() );
			s.getTransaction().commit();
			fail( "should have failed because inserts are before deletes");
		}
		catch( ConstraintViolationException ex ) {
			// expected
			s.getTransaction().rollback();
		}
		finally {
			s.close();
		}
	}
}