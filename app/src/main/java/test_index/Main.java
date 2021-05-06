package test_index;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(final String[] args) {
        final int numOfValues = 100_000;
        final String url = "jdbc:sqlite:test.db";
        final String sql = new StringBuilder("insert into test(value) values")
            .append(Stream.generate(() -> "(?)")
                .limit(numOfValues)
                .collect(Collectors.joining(",")))
            .append(";")
            .toString();
        final Random random = new Random();
        try (final Connection connection = DriverManager.getConnection(url);
             final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 10_000; i++) {
                for (int j = 1; j <= numOfValues; j++) {
                    preparedStatement.setInt(j, random.nextInt(1000));
                }
                preparedStatement.executeUpdate();
            }
        } catch (final SQLException e) {
            e.printStackTrace();
        }
    }
}
