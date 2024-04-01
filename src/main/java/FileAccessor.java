import Entities.*;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class FileAccessor {

    private ArrayList<Guardian> llistaGuardians = new ArrayList<>();
    private ArrayList<Child> llistaChildren = new ArrayList<>();
    private ArrayList<Activity> llistaActivities = new ArrayList<>();
    private ArrayList<Camp> llistaCamps = new ArrayList<>();
    private ArrayList<ChildActivity> llistaChildActivities = new ArrayList<>();
    private ArrayList<CampChild> llistaCampChildren = new ArrayList<>();

    private Connection connection;

    public FileAccessor() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/SummerCamp";
        String user = "postgres";
        String password = "postgres";
        connection = DriverManager.getConnection(url, user, password);
    }

    public void readGuardiansFile(String filename) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try (Scanner scanner = new Scanner(linea).useDelimiter(",")) {
                    String dni = scanner.next();
                    String name = scanner.next();
                    String phone = scanner.next();
                    String address = scanner.next();
                    String email = scanner.next();
                    Guardian guardian = new Guardian(dni, name, phone, address, email);
                    llistaGuardians.add(guardian);
                    insertarGuardian(guardian);
                }
            }
        }
    }

    public void readChildrenFile(String filename) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try (Scanner scanner = new Scanner(linea).useDelimiter(",")) {
                    int idChild = scanner.nextInt();
                    String name = scanner.next();
                    String birthdayString = scanner.next(); // Lee la fecha de nacimiento como una cadena de caracteres
                    java.sql.Date birthday = new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(birthdayString).getTime()); // Convierte la cadena a un objeto java.sql.Date
                    boolean specialMenu = scanner.nextBoolean();
                    String dniGuardian = scanner.next();
                    Guardian guardian = findGuardianByDni(dniGuardian);
                    Child child = new Child(idChild, name, birthday, specialMenu, guardian);
                    llistaChildren.add(child);
                    insertarChild(child);
                } catch (ParseException e) {
                    System.out.println("Error al convertir la fecha de nacimiento.");
                }
            }
        }
    }

    public void readActivitiesFile(String filename) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try (Scanner scanner = new Scanner(linea).useDelimiter(",")) {
                    int idActivity = scanner.nextInt();
                    String description = scanner.next();
                    Activity activity = new Activity(idActivity, description);
                    llistaActivities.add(activity);
                    insertarActivity(activity);
                }
            }
        }
    }

    public void readCampsFile(String filename) throws IOException, SQLException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try (Scanner scanner = new Scanner(linea).useDelimiter(",")) {
                    int idCamp = scanner.nextInt();
                    String site = scanner.next();
                    String fromDateStr = scanner.next();
                    String toDateStr = scanner.next();

                    // Parse String dates into java.util.Date objects
                    java.util.Date utilFromDate = dateFormat.parse(fromDateStr);
                    java.util.Date utilToDate = dateFormat.parse(toDateStr);

                    // Convert java.util.Date to java.sql.Date
                    Date fromDate = new Date(utilFromDate.getTime());
                    Date toDate = new Date(utilToDate.getTime());

                    // Create Camp object
                    Camp camp = new Camp(idCamp, site, fromDate, toDate);
                    llistaCamps.add(camp);

                    // Insert Camp object

                } catch (ParseException e) {
                    System.err.println("Error parsing dates: " + e.getMessage());
                }
            }
        }
    }

    public void readChildActivitiesFile(String filename) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try (Scanner scanner = new Scanner(linea).useDelimiter(",")) {
                    int idChild = scanner.nextInt();
                    int idActivity = scanner.nextInt();
                    Child child = findChildById(idChild);
                    Activity activity = findActivityById(idActivity);
                    ChildActivity childActivity = new ChildActivity(child, activity);
                    llistaChildActivities.add(childActivity);
                    insertarChildActivity(childActivity);
                }
            }
        }
    }

    public void readCampChildrenFile(String filename) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try (Scanner scanner = new Scanner(linea).useDelimiter(",")) {
                    int idCamp = scanner.nextInt();
                    int idChild = scanner.nextInt();
                    Camp camp = findCampById(idCamp);
                    Child child = findChildById(idChild);
                    CampChild campChild = new CampChild(camp, child);
                    llistaCampChildren.add(campChild);
                    insertarCampChild(campChild);
                }
            }
        }
    }

    private Guardian findGuardianByDni(String dni) {
        for (Guardian guardian : llistaGuardians) {
            if (guardian.getDni() == dni) {
                return guardian;
            }
        }
        return null; // Handle not found case appropriately
    }

    private Child findChildById(int idChild) {
        for (Child child : llistaChildren) {
            if (child.getIdChild() == idChild) {
                return child;
            }
        }
        return null; // Handle not found case appropriately
    }

    private Activity findActivityById(int idActivity) {
        for (Activity activity : llistaActivities) {
            if (activity.getIdActivity() == idActivity) {
                return activity;
            }
        }
        return null; // Handle not found case appropriately
    }

    private Camp findCampById(int idCamp) {
        for (Camp camp : llistaCamps) {
            if (camp.getIdCamp() == idCamp) {
                return camp;
            }
        }
        return null; // Handle not found case appropriately
    }

    private void insertarGuardian(Guardian guardian) throws SQLException {
        String sql = "INSERT INTO Guardian (dni, name, phone, address, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, guardian.getDni());
            pstmt.setString(2, guardian.getName());
            pstmt.setString(3, guardian.getPhone());
            pstmt.setString(4, guardian.getAddress());
            pstmt.setString(5, guardian.getEmail());
            pstmt.executeUpdate();
        }
    }

    private void insertarChild(Child child) throws SQLException {
        String sql = "INSERT INTO Child (idChild, name, birthday, specialMenu, guardian_dni) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, child.getIdChild());
            pstmt.setString(2, child.getName());
            pstmt.setDate(3, new java.sql.Date(child.getBirthday().getTime())); // Convertir java.util.Date a java.sql.Date
            pstmt.setBoolean(4, child.isSpecialMenu());
            pstmt.setString(5, child.getGuardian().getDni());
            pstmt.executeUpdate();
        }
    }


    private void insertarActivity(Activity activity) throws SQLException {
        String sql = "INSERT INTO Activity (idActivity, description) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, activity.getIdActivity());
            pstmt.setString(2, activity.getDescription());
            pstmt.executeUpdate();
        }
    }

    private static void insertarCamp(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el sitio del campamento: ");
        String sitio = scanner.nextLine();
        System.out.print("Ingrese la fecha de inicio del campamento (dd/mm/aaaa): ");
        String fechaInicioStr = scanner.nextLine();
        System.out.print("Ingrese la fecha de fin del campamento (dd/mm/aaaa): ");
        String fechaFinStr = scanner.nextLine();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            fechaInicio = new java.sql.Date(dateFormat.parse(fechaInicioStr).getTime());
            fechaFin = new java.sql.Date(dateFormat.parse(fechaFinStr).getTime());
        } catch (ParseException e) {
            System.out.println("Error al convertir la fecha. Asegúrate de ingresarla en el formato correcto (dd/mm/aaaa).");
            return;
        }

        // Convertir java.util.Date a java.sql.Date
        java.sql.Date sqlFechaInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFechaFin = new java.sql.Date(fechaFin.getTime());

        Camp camp = new Camp(sitio, sqlFechaInicio, sqlFechaFin);
        entityManager.persist(camp);

        System.out.println("Campamento insertado correctamente.");
    }


    private void insertarChildActivity(ChildActivity childActivity) throws SQLException {
        String sql = "INSERT INTO ChildActivity (idChild, idActivity) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, childActivity.getChild().getIdChild());
            pstmt.setInt(2, childActivity.getActivity().getIdActivity());
            pstmt.executeUpdate();
        }
    }

    private void insertarCampChild(CampChild campChild) throws SQLException {
        String sql = "INSERT INTO CampChild (idCamp, idChild) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, campChild.getCamp().getIdCamp());
            pstmt.setInt(2, campChild.getChild().getIdChild());
            pstmt.executeUpdate();
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
