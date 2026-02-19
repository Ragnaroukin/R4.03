package miniprojet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import miniprojet.jdbc.JDBC;

public class Main {

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection(JDBC.getInstance().getUrl(), JDBC.getInstance().getUser(), JDBC.getInstance().getPassword())) {

            System.out.println("Connexion réussie !");

            Statement st = conn.createStatement();

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS test (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100))"
            );

            st.executeUpdate("INSERT INTO test(name) VALUES ('Arthur')");

            ResultSet rs = st.executeQuery("SELECT * FROM test");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " - " +
                    rs.getString("name")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
