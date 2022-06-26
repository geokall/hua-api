package com.hua.api.enums;

import com.hua.api.converter.PostgreSQLEnumType;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class MyPostgreSQLEnumType extends PostgreSQLEnumType {

    private static final String H2_DIALECT = "org.hibernate.dialect.H2Dialect";

    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index,
                            final SharedSessionContractImplementor session) throws SQLException {

        // H2 database compatibility
        if (H2_DIALECT.equals(session.getFactory().getJdbcServices().getDialect().toString())) {
            st.setObject(index, value != null ? ((Enum<?>) value).name() : null, Types.VARCHAR);
        } else {
            super.nullSafeSet(st, value, index, session);
        }
    }

}
