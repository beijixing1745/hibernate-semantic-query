/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.test.sqm.parser.hql;

import org.hibernate.sqm.path.AttributeBinding;
import org.hibernate.sqm.query.SqmSelectStatement;
import org.hibernate.sqm.query.expression.AttributeReferenceSqmExpression;
import org.hibernate.sqm.query.from.FromElementSpace;
import org.hibernate.sqm.query.predicate.RelationalSqmPredicate;
import org.hibernate.sqm.query.select.SqmSelection;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Steve Ebersole
 */
public class AttributePathTests extends StandardModelTest {
	@Test
	public void testImplicitJoinReuse() {
		final SqmSelectStatement statement = interpretSelect( "select s.entity.basic1, s.entity.basic2 from Something s" );

		assertThat( statement.getQuerySpec().getFromClause().getFromElementSpaces().size(), is(1) );
		final FromElementSpace space = statement.getQuerySpec().getFromClause().getFromElementSpaces().get( 0 );

		assertThat( space.getJoins().size(), is(1) );
	}

	@Test
	public void testImplicitJoinReuse2() {
		final SqmSelectStatement statement = interpretSelect( "select s.entity from Something s where s.entity.basic2 = ?1" );

		assertThat( statement.getQuerySpec().getFromClause().getFromElementSpaces().size(), is(1) );
		final FromElementSpace space = statement.getQuerySpec().getFromClause().getFromElementSpaces().get( 0 );

		assertThat( space.getJoins().size(), is(1) );

		final SqmSelection selection = statement.getQuerySpec().getSelectClause().getSelections().get( 0 );
		assertThat( selection.getExpression(), instanceOf( AttributeBinding.class ) );
		final AttributeBinding selectExpression = (AttributeBinding) selection.getExpression();
		assertThat( selectExpression.getFromElement(), notNullValue() );

		final RelationalSqmPredicate predicate = (RelationalSqmPredicate) statement.getQuerySpec().getWhereClause().getPredicate();
		final AttributeReferenceSqmExpression predicateLhs = (AttributeReferenceSqmExpression) predicate.getLeftHandExpression();
		assertThat( predicateLhs.getLeftHandSide().getFromElement(), notNullValue() );

		assertThat( predicateLhs.getLeftHandSide().getFromElement(), sameInstance( selectExpression.getFromElement() ) );
	}
}
