import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static final String DB_NAME = "kiosk.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\문태식\\Desktop\\" + DB_NAME;

    public static final String TABLE_MEMBERS = "members";
    public static final String TABLE_AMOUNTS = "amounts";
    public static final String TABLE_MENU = "menu";
    public static final String TABLE_OPTION = "option_choice";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TIMES = "times";
    public static final String COLUMN_PHONE_NUMBER = "phone_number";
    public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_COUNTS = "counts";
    public static final String COLUMN_GRADE = "grade";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_CHOICE = "first_choice";
    public static final String COLUMN_SECOND_CHOICE = "second_choice";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CHOICE = "choice";
    public static final String COLUMN_ADD_CHOICE = "add_choice";
    public static final String COLUMN_ADD_PRICE = "add_price";

    private Connection conn;

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            System.out.println("            🖐🖐🖐🖐어서오세요🖐🖐🖐🖐\n");
            return true;

        } catch (SQLException e) {
            System.out.println("Couldn't connect database: " + e.getMessage());
            return false;
        }
    }
    public void close() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("🙏🙏주문이 완료되었습니다. 조금만 기다려주세요🙏🙏");
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection:" + e.getMessage());
        }
    }

    public List<menu> menu() {
        StringBuilder sc = new StringBuilder();
        sc.append("SELECT * FROM "+TABLE_MENU);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {

            List<menu> E = new ArrayList<>();

            while (results.next()) {
                menu e = new menu();

                e.setId(results.getInt(COLUMN_ID));
                e.setFirst_choice(results.getString(COLUMN_FIRST_CHOICE));
                e.setSecond_choice(results.getString(COLUMN_SECOND_CHOICE));
                e.setPrice(results.getInt(COLUMN_PRICE));

                E.add(e);
            }
            return E;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<option_choice> option_choice(Integer second_choice) {

        StringBuilder sc = new StringBuilder();

        sc.append("SELECT * FROM "+TABLE_OPTION+
                " JOIN "+ TABLE_MENU + " ON "+TABLE_MENU+"."+COLUMN_FIRST_CHOICE+"="+TABLE_OPTION+"."+COLUMN_CHOICE+
                " WHERE "+TABLE_MENU+"."+COLUMN_ID+" = "+second_choice);


        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {

            List<option_choice> O = new ArrayList<>();
            while (results.next()) {
                option_choice o = new option_choice();

                o.setId(results.getInt(COLUMN_ID));
                o.setChoice(results.getString(COLUMN_CHOICE));
                o.setAdd_choice(results.getString(COLUMN_ADD_CHOICE));
                o.setAdd_price(results.getInt(COLUMN_ADD_PRICE));

                O.add(o);
            }
            return O;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<menu> menu1(Integer second_choice) {
        StringBuilder sc = new StringBuilder();
        sc.append("SELECT "+COLUMN_SECOND_CHOICE+" FROM "+TABLE_MENU+" WHERE "+ COLUMN_ID+" = "+second_choice);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {

            List<menu> E = new ArrayList<>();

            while (results.next()) {
                menu e = new menu();

                //e.setId(results.getInt(COLUMN_ID));
                e.setSecond_choice(results.getString(COLUMN_SECOND_CHOICE));

                E.add(e);
            }
            return E;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<option_choice> option_choice1(Integer second_choice,Integer option_choice) {

        StringBuilder sc = new StringBuilder();

        sc.append("SELECT "+COLUMN_ADD_CHOICE+", ("+ COLUMN_PRICE+"+"+COLUMN_ADD_PRICE+") AS total_price" +
                " FROM "+TABLE_MENU+" JOIN "+TABLE_OPTION +
                " ON "+TABLE_MENU+"."+COLUMN_FIRST_CHOICE+"="+TABLE_OPTION+"."+COLUMN_CHOICE +
                " WHERE "+TABLE_MENU+"."+COLUMN_ID+"="+ second_choice +
                " AND "+TABLE_OPTION+"."+COLUMN_ID+"="+ option_choice);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {

            List<option_choice> O = new ArrayList<>();
            while (results.next()) {
                option_choice o =new option_choice();

                o.setAdd_choice(results.getString(COLUMN_ADD_CHOICE));
                o.setAdd_price(results.getInt("total_price"));

                O.add(o);
            }
            return O;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<amounts> amounts(Integer number,Integer second_choice, Integer option_choice) {

        StringBuilder sc = new StringBuilder();

        sc.append(" INSERT INTO " + TABLE_AMOUNTS +" ("+COLUMN_PHONE+", "+COLUMN_SECOND_CHOICE+", "+COLUMN_ADD_CHOICE+", "+COLUMN_AMOUNT+", "+COLUMN_TIMES +") "+
                "VALUES ("+number+", (SELECT "+COLUMN_SECOND_CHOICE+" FROM "+TABLE_MENU+" WHERE "+COLUMN_ID+" = "+second_choice+"), "+
                "(SELECT "+COLUMN_ADD_CHOICE+" FROM "+TABLE_OPTION+" WHERE "+COLUMN_ID+" = "+option_choice+"), "+
                "(SELECT "+COLUMN_PRICE+"+"+COLUMN_ADD_PRICE+" FROM "+TABLE_MENU+" JOIN "+TABLE_OPTION+" ON "+ TABLE_MENU+"."+COLUMN_FIRST_CHOICE+" = "+TABLE_OPTION+"."+COLUMN_CHOICE+
                        " WHERE "+TABLE_MENU+"."+COLUMN_ID+" = "+second_choice+" AND "+TABLE_OPTION+"."+COLUMN_ID+" = "+option_choice+"), "+
                " datetime('now','localtime') " + ")");

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())){

            List<amounts> A = new ArrayList<>();
            while (results.next()) {
                amounts a =new amounts();
                a.setPhone(results.getInt(COLUMN_PHONE));
                a.setSecond_choice(results.getString(COLUMN_SECOND_CHOICE));
                a.setAdd_choice(results.getString(COLUMN_ADD_CHOICE));
                a.setAmount(results.getInt(COLUMN_AMOUNT));
                //a.setTimes(results.getDateTimeFormatter(COLUMN_TIMES));

                A.add(a);
            }
            return A;
        } catch (SQLException e) {
            System.out.println();
            return null;
        }
    }

    public List<members> members(Integer number) {
        StringBuilder sc = new StringBuilder();
        sc.append("SELECT * FROM "+TABLE_MEMBERS);
        sc.append(" WHERE "+COLUMN_PHONE_NUMBER+" = "+number);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())){
            List<members> M = new ArrayList<>();


            while (results.next()) {
                members m = new members();

                m.setPhone_number(results.getInt(COLUMN_PHONE_NUMBER));
                m.setGrade(results.getString(COLUMN_GRADE));
                m.setTotal_amount(results.getInt(COLUMN_TOTAL_AMOUNT));


                M.add(m);

            }
            return M;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<members> discount(Integer number) {
        StringBuilder sc = new StringBuilder();
        sc.append("select "+COLUMN_SECOND_CHOICE+", "+COLUMN_ADD_CHOICE+", "+COLUMN_GRADE+", case when "+COLUMN_GRADE+" = 'Silver' then ("+COLUMN_AMOUNT+")-("+COLUMN_AMOUNT+"*0.05)"+
                " when "+COLUMN_GRADE+" = 'Gold' then("+COLUMN_AMOUNT+")-("+COLUMN_AMOUNT+"*0.1)"+
                " when "+COLUMN_GRADE+" = 'Platinum' then ("+COLUMN_AMOUNT+")-("+COLUMN_AMOUNT+"*0.15)"+
                " when "+COLUMN_GRADE+" = 'Diamond' then ("+COLUMN_AMOUNT+")-("+COLUMN_AMOUNT+"*0.2)"+
                " else +"+COLUMN_AMOUNT+" end as 'discount_amount'"+
                " from "+TABLE_AMOUNTS +" join "+TABLE_MEMBERS+" on "+TABLE_MEMBERS+"."+COLUMN_PHONE_NUMBER+"="+TABLE_AMOUNTS+"."+COLUMN_PHONE+" WHERE "+
                TABLE_AMOUNTS+"."+COLUMN_PHONE+"="+number+" order by "+COLUMN_TIMES+" DESC limit 1");
        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())){
            List<members> M = new ArrayList<>();

            while (results.next()) {
                members m = new members();

                //m.setPhone_number(results.getInt(COLUMN_PHONE_NUMBER));
                m.setGrade(results.getString(COLUMN_GRADE));
                m.setTotal_amount(results.getInt("discount_amount"));
                M.add(m);
            }
            return M;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<members> insert(Integer number){
        StringBuilder sc = new StringBuilder();
        sc.append("INSERT INTO "+ TABLE_MEMBERS+" ( "+COLUMN_PHONE_NUMBER+", "+COLUMN_TOTAL_AMOUNT+", "+ COLUMN_COUNTS+" ) "+
                " VALUES ( "+number+", (SELECT SUM("+COLUMN_AMOUNT+") FROM " + TABLE_AMOUNTS+
                " WHERE "+ COLUMN_PHONE+" = "+number+" GROUP BY "+COLUMN_PHONE+"), "+
                "(SELECT COUNT(*) FROM "+TABLE_AMOUNTS+" WHERE "+COLUMN_PHONE+" = "+number+"))");

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {

            List<members> M = new ArrayList<>();
            while (results.next()) {
                members m =new members();

                m.setPhone_number(results.getInt(COLUMN_PHONE_NUMBER));
                m.setGrade(results.getString(COLUMN_GRADE));
                m.setTotal_amount(results.getInt(COLUMN_TOTAL_AMOUNT));

                M.add(m);
            }
            return M;
        } catch (SQLException e) {
            System.out.println();
            return null;
        }
    }

    public List<members> update_total_amount(Integer number){
        StringBuilder sc = new StringBuilder();
        sc.append("update "+TABLE_MEMBERS+" set "+COLUMN_TOTAL_AMOUNT+" = (SELECT SUM("+COLUMN_AMOUNT+")"+
                " FROM "+TABLE_AMOUNTS+" WHERE "+COLUMN_PHONE+" = "+number+" GROUP BY "+COLUMN_PHONE+")"+
                " WHERE "+COLUMN_PHONE_NUMBER+" = "+number);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {


            List<members> M = new ArrayList<>();
            while (results.next()) {
                members m =new members();

                m.setPhone_number(results.getInt(COLUMN_PHONE_NUMBER));
                m.setTotal_amount(results.getInt(COLUMN_TOTAL_AMOUNT));
                m.setGrade(results.getString(COLUMN_GRADE));

                M.add(m);
            }
            return M;
        } catch (SQLException e) {
            System.out.println();
            return null;
        }
    }

    public List<members> update_count(Integer number){
        StringBuilder sc = new StringBuilder();
        sc.append("update "+TABLE_MEMBERS+" set "+COLUMN_COUNTS+" = (SELECT COUNT(*)"+
                " FROM "+TABLE_AMOUNTS+" WHERE "+COLUMN_PHONE+" = "+number+")"+
                " WHERE "+COLUMN_PHONE_NUMBER+" = "+number);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {


            List<members> M = new ArrayList<>();
            while (results.next()) {
                members m =new members();

                m.setPhone_number(results.getInt(COLUMN_PHONE_NUMBER));
                m.setTotal_amount(results.getInt(COLUMN_TOTAL_AMOUNT));
                m.setCounts(results.getInt(COLUMN_COUNTS));
                m.setGrade(results.getString(COLUMN_GRADE));

                M.add(m);
            }
            return M;
        } catch (SQLException e) {
            System.out.println();
            return null;
        }
    }

    public List<members> update_grade(Integer number){
        StringBuilder sc = new StringBuilder();
        sc.append("UPDATE "+TABLE_MEMBERS+" SET "+COLUMN_GRADE+" = (CASE WHEN "+COLUMN_TOTAL_AMOUNT+" <= 6000 THEN 'Bronze' "+
                " WHEN "+COLUMN_TOTAL_AMOUNT+" BETWEEN 6001 AND 30000 THEN 'Silver' "+
                " WHEN "+COLUMN_TOTAL_AMOUNT+" BETWEEN 30001 AND 60000 THEN 'Gold' "+
                " WHEN "+COLUMN_TOTAL_AMOUNT+" BETWEEN 60001 AND 100000 THEN 'Platinum' " +
                " WHEN "+COLUMN_TOTAL_AMOUNT+" > 100000 THEN 'Diamond' end) where "+COLUMN_PHONE_NUMBER+" = "+number);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())) {


            List<members> M = new ArrayList<>();
            while (results.next()) {
                members m =new members();

                m.setPhone_number(results.getInt(COLUMN_PHONE_NUMBER));
                m.setTotal_amount(results.getInt(COLUMN_TOTAL_AMOUNT));
                m.setGrade(results.getString(COLUMN_GRADE));

                M.add(m);
            }
            return M;
        } catch (SQLException e) {
            System.out.println();
            return null;
        }
    }

    public List<members> update_members(Integer number) {
        StringBuilder sc = new StringBuilder();
        sc.append("SELECT * FROM "+TABLE_MEMBERS);
        sc.append(" WHERE "+COLUMN_PHONE_NUMBER+" = "+number);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())){
            List<members> M = new ArrayList<>();


            while (results.next()) {
                members m = new members();

                m.setPhone_number(results.getInt(COLUMN_PHONE_NUMBER));
                m.setGrade(results.getString(COLUMN_GRADE));
                m.setTotal_amount(results.getInt(COLUMN_TOTAL_AMOUNT));
                m.setCounts(results.getInt(COLUMN_COUNTS));


                M.add(m);

            }
            return M;
        } catch (SQLException e) {
            System.out.println();
            return null;
        }
    }

    public List<amounts> select_amounts(String time, Integer number) {

        StringBuilder sc = new StringBuilder();

        sc.append("SELECT * FROM "+TABLE_AMOUNTS+
                " WHERE date("+COLUMN_TIMES+")"+" like "+"\'"+time+"%"+"\'"+" AND "+COLUMN_PHONE+" = "+number);

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sc.toString())){

            List<amounts> A = new ArrayList<>();
            while (results.next()) {
                amounts a =new amounts();
                a.setPhone(results.getInt(COLUMN_PHONE));
                a.setSecond_choice(results.getString(COLUMN_SECOND_CHOICE));
                a.setAdd_choice(results.getString(COLUMN_ADD_CHOICE));
                a.setAmount(results.getInt(COLUMN_AMOUNT));
                a.setTimes(results.getTimestamp(COLUMN_TIMES));

                A.add(a);
            }
            return A;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }
}