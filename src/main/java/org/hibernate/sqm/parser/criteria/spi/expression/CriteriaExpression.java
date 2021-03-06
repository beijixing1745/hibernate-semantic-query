/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or visit http://www.apache.org/licenses/LICENSE-2.0
 */
package org.hibernate.sqm.parser.criteria.spi.expression;

import org.hibernate.sqm.domain.Type;
import org.hibernate.sqm.query.expression.SqmExpression;

import org.hibernate.sqm.parser.criteria.spi.CriteriaVisitor;

/**
 * Adapter for dealing with a JPA Criteria Expression.
 *
 * @author Steve Ebersole
 */
public interface CriteriaExpression<X> extends javax.persistence.criteria.Expression<X> {
	SqmExpression visitExpression(CriteriaVisitor visitor);
	Type getExpressionSqmType();
}
