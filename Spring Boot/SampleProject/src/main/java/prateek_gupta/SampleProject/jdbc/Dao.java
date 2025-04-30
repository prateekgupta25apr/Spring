package prateek_gupta.SampleProject.jdbc;

import java.sql.Connection;

public interface Dao {
    boolean save(Connection connection, Table1 table1);

    void read(Connection connection);

    boolean update(Connection connection, Table1 table1);

    boolean delete(Connection connection, Table1 table1);
}
