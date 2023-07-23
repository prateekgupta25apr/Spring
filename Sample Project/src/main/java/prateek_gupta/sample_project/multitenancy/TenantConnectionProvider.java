package prateek_gupta.sample_project.multitenancy;

import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static prateek_gupta.sample_project.multitenancy.TenantContext.
        DEFAULT_SCHEMA_NAME;

@Component
public class TenantConnectionProvider implements MultiTenantConnectionProvider {

    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String schemaName) throws SQLException {
        final Connection connection = getAnyConnection();
        try {
            if (schemaName != null) {
                connection.createStatement().execute("USE " + schemaName);
            } else {
                connection.createStatement().execute("USE " + DEFAULT_SCHEMA_NAME);
            }
        }
        catch ( SQLException e ) {
            throw new HibernateException(
                    "Could not connect to the schema " + schemaName, e
            );
        }
        return connection;
    }

    @Override
    public void releaseConnection(String schemaName, Connection connection)
            throws SQLException {
        try {
            connection.createStatement().execute( "USE " + DEFAULT_SCHEMA_NAME );
        }
        catch ( SQLException e ) {
            throw new HibernateException(
                    "Could not release connection to the schema " + schemaName, e
            );
        }
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class aClass) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return null;
    }
}
