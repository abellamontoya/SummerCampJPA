import Entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    //Ejecuta DatabaseScript para crear las tablas y la base de datos automaticamente

    private static EntityManagerFactory entityManagerFactory;

    public static void main(String[] args) {
        entityManagerFactory = Persistence.createEntityManagerFactory("MyPersistenceUnit");

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("1. Listar entidades");
            System.out.println("2. Insertar entidad");
            System.out.println("3. Eliminar entidad");
            System.out.println("4. Actualizar entidad");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    listarEntidades();
                    break;
                case 2:
                    insertarEntidad(scanner);
                    break;
                case 3:
                    eliminarEntidad(scanner);
                    break;
                case 4:
                    actualizarEntidad(scanner);
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, seleccione otra opción.");
            }

        } while (opcion != 5);

        scanner.close();
        entityManagerFactory.close();
    }

    private static void listarEntidades() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // Listar Child
        TypedQuery<Child> childQuery = entityManager.createQuery("SELECT c FROM Child c", Child.class);
        List<Child> children = childQuery.getResultList();
        if (!children.isEmpty()) {
            System.out.println("Lista de Niños:");
            System.out.printf("%-10s | %-20s | %-12s | %s\n", "ID", "Nombre", "Fecha Nacimiento", "Menú Especial");
            System.out.println("-------------------------------------------------------------------");
            for (Child child : children) {
                System.out.printf("%-10d | %-20s | %-12s | %s\n",
                        child.getIdChild(),
                        child.getName(),
                        formatDate(child.getBirthday()),
                        child.isSpecialMenu() ? "Sí" : "No");
            }
            System.out.println();
        } else {
            System.out.println("No hay niños registrados.\n");
        }

        // Listar Guardian
        TypedQuery<Guardian> guardianQuery = entityManager.createQuery("SELECT g FROM Guardian g", Guardian.class);
        List<Guardian> guardians = guardianQuery.getResultList();
        if (!guardians.isEmpty()) {
            System.out.println("Lista de Guardianes:");
            System.out.printf("%-15s | %-20s | %-15s | %-30s | %s\n", "DNI", "Nombre", "Teléfono", "Dirección", "Correo electrónico");
            System.out.println("-------------------------------------------------------------------------------------------");
            for (Guardian guardian : guardians) {
                System.out.printf("%-15s | %-20s | %-15s | %-30s | %s\n",
                        guardian.getDni(),
                        guardian.getName(),
                        guardian.getPhone(),
                        guardian.getAddress(),
                        guardian.getEmail());
            }
            System.out.println();
        } else {
            System.out.println("No hay guardianes registrados.\n");
        }

        // Listar Camp
        TypedQuery<Camp> campQuery = entityManager.createQuery("SELECT c FROM Camp c", Camp.class);
        List<Camp> camps = campQuery.getResultList();
        if (!camps.isEmpty()) {
            System.out.println("Lista de Campamentos:");
            System.out.printf("%-10s | %-30s | %-12s | %s\n", "ID", "Sitio", "Desde", "Hasta");
            System.out.println("-------------------------------------------------------------------");
            for (Camp camp : camps) {
                System.out.printf("%-10d | %-30s | %-12s | %s\n",
                        camp.getIdCamp(),
                        camp.getSite(),
                        formatDate(camp.getFromDate()),
                        formatDate(camp.getToDate()));
            }
            System.out.println();
        } else {
            System.out.println("No hay campamentos registrados.\n");
        }

        // Listar Activity
        TypedQuery<Activity> activityQuery = entityManager.createQuery("SELECT a FROM Activity a", Activity.class);
        List<Activity> activities = activityQuery.getResultList();
        if (!activities.isEmpty()) {
            System.out.println("Lista de Actividades:");
            System.out.printf("%-10s | %s\n", "ID", "Descripción");
            System.out.println("---------------------------------------------");
            for (Activity activity : activities) {
                System.out.printf("%-10d | %s\n",
                        activity.getIdActivity(),
                        activity.getDescription());
            }
            System.out.println();
        } else {
            System.out.println("No hay actividades registradas.\n");
        }

        entityManager.close();
    }


    // Método para formatear la fecha en formato dd/MM/yyyy
    private static String formatDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }


    private static void insertarEntidad(Scanner scanner) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        System.out.println("Seleccione el tipo de entidad que desea insertar:");
        System.out.println("1. Child");
        System.out.println("2. Activity");
        System.out.println("3. Camp (no se puede insertar, solo se puede ver)");
        System.out.println("4. Guardian");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                insertarChild(entityManager, scanner);
                break;
            case 2:
                insertarActivity(entityManager, scanner);
                break;
            case 3:
                insertarCamp(entityManager, scanner);
                break;
            case 4:
                insertarGuardian(entityManager, scanner);
                break;
            default:
                System.out.println("Opción no válida.");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static void insertarChild(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el nombre del niño: ");
        String nombre = scanner.next();

        // Pedir al usuario que ingrese la fecha de nacimiento en el formato dd/mm/aaaa
        System.out.print("Ingrese la fecha de nacimiento del niño (dd/mm/aaaa): ");
        String fechaNacimientoString = scanner.next();

        // Convertir la cadena de texto a un objeto Date utilizando SimpleDateFormat
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaNacimiento = null;
        try {
            fechaNacimiento = formatter.parse(fechaNacimientoString);
        } catch (ParseException e) {
            System.out.println("Formato de fecha incorrecto. Use el formato dd/mm/aaaa.");
            return;
        }

        // Pedir al usuario que ingrese si el niño requiere menú especial (true/false)
        System.out.print("¿El niño requiere menú especial? (true/false): ");
        boolean menuEspecial = scanner.nextBoolean();

        System.out.print("Ingrese el DNI del guardián del niño: ");
        String dniGuardian = scanner.next(); // Leer el DNI como cadena de texto

        // Buscar el guardián en la base de datos
        Guardian guardian = entityManager.find(Guardian.class, dniGuardian);
        if (guardian == null) {
            System.out.println("No se encontró ningún guardián con el DNI proporcionado.");
            return;
        }

        // Crear una nueva instancia de Child y persistirla en la base de datos
        Child child = new Child(nombre, fechaNacimiento, menuEspecial, guardian);
        entityManager.persist(child);
        System.out.println("Niño insertado correctamente.");
    }

    private static void insertarActivity(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el ID de la actividad: ");
        int id = scanner.nextInt(); // Leer el ID proporcionado por el usuario
        scanner.nextLine(); // Consumir la nueva línea pendiente

        System.out.print("Ingrese la descripción de la actividad: ");
        String descripcion = scanner.nextLine();

        Activity activity = new Activity(id, descripcion); // Crear una nueva instancia de Activity
        entityManager.persist(activity); // Persistir la nueva actividad en la base de datos

        System.out.println("Actividad insertada correctamente.");
    }

    private static void insertarCamp(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el sitio del campamento: ");
        // Consumir nueva línea pendiente
        scanner.next();
        String sitio = scanner.nextLine();
        System.out.print("Ingrese la fecha de inicio del campamento (dd/mm/aaaa): ");
        String fechaInicioStr = scanner.nextLine();
        System.out.print("Ingrese la fecha de fin del campamento (dd/mm/aaaa): ");
        String fechaFinStr = scanner.nextLine();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio = null;
        Date fechaFin = null;
        try {
            fechaInicio = dateFormat.parse(fechaInicioStr);
            fechaFin = dateFormat.parse(fechaFinStr);
        } catch (ParseException e) {
            System.out.println("Error al convertir la fecha. Asegúrate de ingresarla en el formato correcto (dd/mm/aaaa).");
            return;
        }

        Camp camp = new Camp(sitio, fechaInicio, fechaFin);
        entityManager.persist(camp);

        System.out.println("Campamento insertado correctamente.");
    }

    private static void insertarGuardian(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el DNI del guardián: ");
        String dni = scanner.next();
        scanner.nextLine(); // Consumir el salto de línea

        System.out.print("Ingrese el nombre del guardián: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el teléfono del guardián: ");
        String telefono = scanner.nextLine();
        System.out.print("Ingrese la dirección del guardián: ");
        String direccion = scanner.nextLine();
        System.out.print("Ingrese el correo electrónico del guardián: ");
        String correo = scanner.nextLine();

        Guardian guardian = new Guardian(dni, nombre, telefono, direccion, correo);
        entityManager.persist(guardian);

        System.out.println("Guardián insertado correctamente.");
    }

    private static void eliminarEntidad(Scanner scanner) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        System.out.println("Seleccione el tipo de entidad que desea eliminar:");
        System.out.println("1. Child");
        System.out.println("2. Activity");
        System.out.println("3. Camp");
        System.out.println("4. Guardian");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                eliminarChild(entityManager, scanner);
                break;
            case 2:
                eliminarActivity(entityManager, scanner);
                break;
            case 3:
                eliminarCamp(entityManager, scanner);
                break;
            case 4:
                eliminarGuardian(entityManager, scanner);
                break;
            default:
                System.out.println("Opción no válida.");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static void eliminarChild(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el ID del niño que desea eliminar: ");
        int idChild = scanner.nextInt();
        Child child = entityManager.find(Child.class, idChild);
        if (child != null) {
            entityManager.remove(child);
            System.out.println("Niño eliminado correctamente.");
        } else {
            System.out.println("No se encontró ningún niño con el ID proporcionado.");
        }
    }

    private static void eliminarActivity(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el ID de la actividad que desea eliminar: ");
        int idActivity = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Activity activity = entityManager.find(Activity.class, idActivity);
        if (activity != null) {
            entityManager.remove(activity);
            System.out.println("Actividad eliminada correctamente.");
        } else {
            System.out.println("No se encontró ninguna actividad con el ID proporcionado.");
        }
    }

    private static void eliminarCamp(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el ID del campamento que desea eliminar: ");
        int idCamp = scanner.nextInt();

        Camp camp = entityManager.find(Camp.class, idCamp);
        if (camp != null) {
            entityManager.remove(camp);
            System.out.println("Campamento eliminado correctamente.");
        } else {
            System.out.println("No se encontró ningún campamento con el ID proporcionado.");
        }
    }

    private static void eliminarGuardian(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el DNI del guardián que desea eliminar: ");
        String dni = scanner.next();

        Guardian guardian = entityManager.find(Guardian.class, dni);
        if (guardian != null) {
            entityManager.remove(guardian);
            System.out.println("Guardián eliminado correctamente.");
        } else {
            System.out.println("No se encontró ningún guardián con el DNI proporcionado.");
        }
    }

    private static void actualizarEntidad(Scanner scanner) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        System.out.println("Seleccione el tipo de entidad que desea actualizar:");
        System.out.println("1. Child");
        System.out.println("2. Activity");
        System.out.println("3. Camp");
        System.out.println("4. Guardian");
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();

        switch (opcion) {
            case 1:
                actualizarChild(entityManager, scanner);
                break;
            case 2:
                actualizarActivity(entityManager, scanner);
                break;
            case 3:
                actualizarCamp(entityManager, scanner);
                break;
            case 4:
                actualizarGuardian(entityManager, scanner);
                break;
            default:
                System.out.println("Opción no válida.");
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private static void actualizarChild(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el ID del niño que desea actualizar: ");
        int idChild = scanner.nextInt();
        Child child = entityManager.find(Child.class, idChild);
        if (child != null) {
            System.out.print("Ingrese el nuevo nombre del niño: ");
            String nuevoNombre = scanner.next();
            child.setName(nuevoNombre);
            // Aquí puedes actualizar el resto de los campos necesarios
            // Por ejemplo, fecha de nacimiento, menú especial, etc.
            // Luego, usa entityManager.merge() para actualizar el niño en la base de datos
        } else {
            System.out.println("No se encontró ningún niño con el ID proporcionado.");
        }
    }

    private static void actualizarActivity(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el ID de la actividad que desea actualizar: ");
        int idActivity = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Activity activity = entityManager.find(Activity.class, idActivity);
        if (activity != null) {
            System.out.print("Ingrese la nueva descripción de la actividad: ");
            String nuevaDescripcion = scanner.nextLine();
            activity.setDescription(nuevaDescripcion);

            // Guardar los cambios en la base de datos
            entityManager.merge(activity);

            System.out.println("Actividad actualizada correctamente.");
        } else {
            System.out.println("No se encontró ninguna actividad con el ID proporcionado.");
        }
    }

    private static void actualizarCamp(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el ID del campamento que desea actualizar: ");
        int idCamp = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        Camp camp = entityManager.find(Camp.class, idCamp);
        if (camp != null) {
            System.out.print("Ingrese el nuevo sitio del campamento: ");
            String nuevoSitio = scanner.nextLine();
            camp.setSite(nuevoSitio);

            // Guardar los cambios en la base de datos
            entityManager.merge(camp);

            System.out.println("Campamento actualizado correctamente.");
        } else {
            System.out.println("No se encontró ningún campamento con el ID proporcionado.");
        }
    }

    private static void actualizarGuardian(EntityManager entityManager, Scanner scanner) {
        System.out.print("Ingrese el DNI del guardián que desea actualizar: ");
        String dni = scanner.next();
        scanner.nextLine(); // Consumir el salto de línea

        Guardian guardian = entityManager.find(Guardian.class, dni);
        if (guardian != null) {
            System.out.print("Ingrese el nuevo nombre del guardián: ");
            String nuevoNombre = scanner.nextLine();
            guardian.setName(nuevoNombre);

            // Guardar los cambios en la base de datos
            entityManager.merge(guardian);

            System.out.println("Guardián actualizado correctamente.");
        } else {
            System.out.println("No se encontró ningún guardián con el DNI proporcionado.");
        }
    }
}
