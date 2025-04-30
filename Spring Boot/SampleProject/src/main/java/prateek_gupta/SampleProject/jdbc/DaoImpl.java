package prateek_gupta.SampleProject.jdbc;

import java.sql.*;

public class DaoImpl implements Dao {
    @Override
    public boolean save(Connection connection, Table1 table1) {
        PreparedStatement preparedStatement = null;
        boolean result;
        try {
            preparedStatement = connection.prepareStatement(
                    "insert into table1 values(?,?)");
            preparedStatement.setInt(1, table1.id);
            preparedStatement.setString(2, table1.name);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public void read(Connection connection) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from table1");
            while (resultSet.next())
                System.out.println(resultSet.getInt(1) + " : " +
                        resultSet.getString(2));


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public boolean update(Connection connection, Table1 table1) {
        PreparedStatement preparedStatement = null;
        boolean result = false;
        try {
            preparedStatement = connection.prepareStatement(
                    "update table1 set name=? where id=?;");
            preparedStatement.setString(1, table1.name);
            preparedStatement.setInt(2, table1.id);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public boolean delete(Connection connection, Table1 table1) {
        PreparedStatement preparedStatement = null;
        boolean result = false;
        try {
            preparedStatement = connection.prepareStatement(
                    "delete from table1 where id=?;");
            preparedStatement.setInt(1, table1.id);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return result;
    }
}
