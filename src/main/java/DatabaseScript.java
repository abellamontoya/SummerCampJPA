import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseScript {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/";
        String dbName = "summercampjpa";
        String user = "postgres";
        String password = "postgres";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement()) {

            String createDBQuery = "CREATE DATABASE " + dbName;
            stmt.executeUpdate(createDBQuery);
            System.out.println("Base de datos " + dbName + " creada exitosamente.");

            String dbURL = url + dbName;
            try (Connection conn = DriverManager.getConnection(dbURL, user, password);
                 Statement statement = conn.createStatement()) {

                // Crear las tablas
                String[] createTableQueries = {
                        "CREATE TABLE guardian (dni VARCHAR(255) PRIMARY KEY, name VARCHAR(255) NOT NULL, phone VARCHAR(255), address VARCHAR(255), email VARCHAR(255));",
                        "CREATE TABLE child (idChild SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL, birthday DATE, specialMenu BOOLEAN, guardian_dni VARCHAR(255) REFERENCES guardian(dni));",
                        "CREATE TABLE activity (idActivity INT PRIMARY KEY, description VARCHAR(255) NOT NULL);",
                        "CREATE TABLE camp (idCamp INT PRIMARY KEY, site VARCHAR(255) NOT NULL, fromDate DATE, toDate DATE);",
                        "CREATE TABLE child_guardian (idChild INT REFERENCES child(idChild) PRIMARY KEY, dni VARCHAR(255) REFERENCES guardian(dni));",
                        "CREATE TABLE camp_child (idCamp INT REFERENCES camp(idCamp) PRIMARY KEY, idChild INT REFERENCES child(idChild));",
                        "CREATE TABLE activity_camp (idActivity INT REFERENCES activity(idActivity) PRIMARY KEY, idCamp INT REFERENCES camp(idCamp));"
                };

                for (String query : createTableQueries) {
                    statement.executeUpdate(query);
                }
                System.out.println("Tablas creadas exitosamente en la base de datos " + dbName + ".");

                // Realizar los inserts
                String[] insertQueries = {
                        "INSERT INTO guardian (dni, name, phone, address, email) VALUES ('12345678A', 'John Doe', '123456789', '123 Street', 'john@example.com');",
                        "INSERT INTO child (name, birthday, specialMenu, guardian_dni) VALUES ('Alice', '2010-01-01', true, '12345678A');",
                        "INSERT INTO activity (idActivity, description) VALUES (1, 'Swimming');",
                        "INSERT INTO camp (idCamp, site, fromDate, toDate) VALUES (1, 'Forest Camp', '2024-07-01', '2024-07-15');",
                        "INSERT INTO child_guardian (idChild, dni) VALUES (1, '12345678A');",
                        "INSERT INTO camp_child (idCamp, idChild) VALUES (1, 1);",
                        "INSERT INTO activity_camp (idActivity, idCamp) VALUES (1, 1);"
                };

                for (String query : insertQueries) {
                    statement.executeUpdate(query);
                }
                System.out.println("Inserts realizados exitosamente.");

            } catch (SQLException ex) {
                System.out.println("Error al conectar a la base de datos " + dbName + ": " + ex.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos " + dbName + ": " + e.getMessage());
        }
    }
}
