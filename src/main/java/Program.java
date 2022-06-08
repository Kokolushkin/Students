import java.sql.*;
import java.util.Scanner;

public class Program {

    private static Connection connect;
    private static Scanner scanner;
    private static Statement statement;

    private static final String JDBC = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:Student.db";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Program program = new Program();
        scanner = new Scanner(System.in);

        program.connect();
        program.createTable();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Для работы с программой используйте следующие числовые команды:\n")
                     .append("0 - Завершить работу\n")
                     .append("1 - Для добавления нового студента в таблицу\n")
                     .append("2 - Для удаления студента с определенным идентификатором\n")
                     .append("3 - Вывести все таблицу студентов\n");

        System.out.println(stringBuilder.toString());

        while (true) {
            switch (scanner.nextLine()){
                case "0":
                    program.exit();
                    break;
                case "1":
                    program.insertStudent();
                    break;
                case "2":
                    program.deleteStudentById();
                    break;
                case "3":
                    program.showListOfStudent();
                    break;
                default:
                    System.out.println("Введена не корректная команда.");
                    break;
            }
            System.out.println("Введите новую команду:");
        }
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC);
        connect = DriverManager.getConnection(DB_URL);
        statement = connect.createStatement();
        System.out.println("База Подключена!");
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS students " +
                "(id         INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name       VARCHAR(50)," +
                " surname    VARCHAR(50)," +
                " patronymic VARCHAR(50), " +
                " date       TEXT," +
                " groupName  VARCHAR(10))";

        statement.executeUpdate(sql);
    }

    private void insertStudent() throws SQLException {
        System.out.println("Введите имя студента:");
        String name = scanner.nextLine();

        System.out.println("Введите фамилию студента:");
        String surname = scanner.nextLine();

        System.out.println("Введите отчество студента:");
        String patronymic = scanner.nextLine();

        System.out.println("Введите группу студента:");
        String groupName = scanner.nextLine();

        System.out.println("Введите дату рождения студента (Пример: 1999-12-31):");
        String date = scanner.nextLine();

        String sql = String.format("INSERT INTO students (name,surname,patronymic,groupName,date) VALUES ('%s','%s','%s','%s','%s')", name, surname, patronymic, groupName, date);

        statement.executeUpdate(sql);
        System.out.println("Успешно");
    }

    private void deleteStudentById() throws SQLException {
        System.out.println("Введите идентификатор студента:");
        int id = scanner.nextInt();
        String sql = String.format("DELETE FROM students WHERE id=%d", id);
        statement.executeUpdate(sql);
        System.out.println("Успешно");
    }

    private void showListOfStudent() throws SQLException {
        String sql = "SELECT * FROM students";
        ResultSet result = statement.executeQuery(sql);
        StringBuilder stringBuilder = new StringBuilder();
        while (result.next()){
            stringBuilder.append("id: ").append(result.getInt("id"))
                    .append("\nname: ").append(result.getString("name"))
                    .append("\nsurname: ").append(result.getString("surname"))
                    .append("\npatronymic: ").append(result.getString("patronymic"))
                    .append("\ngroupName: ").append(result.getString("groupName"))
                    .append("\ndate: ").append(result.getString("date"))
                    .append("\n");
        }

        String output = stringBuilder.length() == 0 ? "В таблице не студентов\n"
                                                    : stringBuilder.toString();
        System.out.println(output);
    }

    private void exit() throws SQLException {
        connect.close();
        scanner.close();
        System.exit(0);
    }
}