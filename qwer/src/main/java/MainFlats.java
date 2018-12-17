
import java.sql.*;
import java.util.Scanner;

public class MainFlats {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/apartaments?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "pass";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                // create connection
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add flat");
                    System.out.println("2: delete flat");
                    System.out.println("3: change parameter of flat");
                    System.out.println("4: sort flat By Area");
                    System.out.println("5: sort flat By Price");
                    System.out.println("6: get all flats");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlat(sc);
                            break;
                        case "2":

                            deleteFlat(sc);
                            break;
                        case "3":
                            changeFlat(sc);
                            break;
                        case "4":
                            sortFlatByArea(sc);
                            break;
                        case "5":
                            sortFlatByPrice(sc);
                            break;
                        case "6":
                            getAllFlats();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();

//        для створення таблиці розкоментувати код (і для того щоб вона вдруге не перестворювалась закоментувати)
//        try {
//            st.execute("DROP TABLE IF EXISTS flats");
//            st.execute("CREATE TABLE flats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, zone VARCHAR(20), address VARCHAR(25), area FLOAT, rooms int, price int)");
//        } finally {
//            st.close();
//        }
    }

    private static void addFlat(Scanner sc) throws SQLException {
        System.out.print("Enter zone flat: ");
        String zone = sc.nextLine();
        System.out.print("Enter address flat: ");
        String address = sc.nextLine();
        System.out.print("Enter area flat: ");
        String sArea = sc.nextLine();
        System.out.print("Enter rooms: ");
        String sRooms = sc.nextLine();
        System.out.print("Enter PRICE flat: ");
        String sPrice = sc.nextLine();

        float area = Float.parseFloat(sArea);
        int rooms = Integer.parseInt(sRooms);
        int price = Integer.parseInt(sPrice);

        PreparedStatement ps = conn.prepareStatement("INSERT INTO flats (zone, address, area, rooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, zone);
            ps.setString(2, address);
            ps.setFloat(3, area);
            ps.setInt(4, rooms);
            ps.setInt(5, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void deleteFlat(Scanner sc) throws SQLException {
        System.out.print("Enter address flat: ");
        String address = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("DELETE FROM flats WHERE address = ?");
        try {
            ps.setString(1, address);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
        System.out.println("Квартира " + address + " успішно видалена");
    }

    private static void changeFlat(Scanner sc) throws SQLException {
        System.out.print("Enter address flat: ");
        String address = sc.nextLine();
        System.out.print("Enter new zone flat: ");
        String zone = sc.nextLine();
        System.out.print("Enter new area flat: ");
        String sArea = sc.nextLine();
        System.out.print("Enter new rooms: ");
        String sRooms = sc.nextLine();
        System.out.print("Enter new PRICE flat: ");
        String sPrice = sc.nextLine();

        float area = Float.parseFloat(sArea);
        int rooms = Integer.parseInt(sRooms);
        int price = Integer.parseInt(sPrice);

        PreparedStatement ps = conn.prepareStatement("UPDATE flats SET zone=?, area=?, rooms=?, price=? WHERE  address like ?");

        try {
            ps.setString(1, zone);
            ps.setFloat(2, area);
            ps.setInt(3, rooms);
            ps.setInt(4, price);
            ps.setString(5, address);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }

    private static void sortFlatByArea(Scanner sc) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM flats where area >= ? and area <= ?;");

        System.out.print("enter low AREA ");
        int AreaL = sc.nextInt();
        System.out.print("enter height AREA ");
        int AreaH = sc.nextInt();

        ps.setInt(1, AreaL);
        ps.setInt(2, AreaH);

        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();
            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    private static void sortFlatByPrice(Scanner sc) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM flats where price >= ? and price <= ?;");

        System.out.print("enter low price ");
        int low = sc.nextInt();
        System.out.print("enter height price ");
        int height = sc.nextInt();

        ps.setInt(1, low);
        ps.setInt(2, height);
        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();
            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }


    private static void getAllFlats() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("select  * from flats;");

        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();
            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }

    }
}

