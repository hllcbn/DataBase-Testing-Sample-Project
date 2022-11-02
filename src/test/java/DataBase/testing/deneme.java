package DataBase.testing;

import org.testng.annotations.Test;

import java.sql.*;

public class deneme {
    public static void main(String[] args) throws SQLException {


    test();


    }

    public static void test() throws SQLException {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "1311adhb");
        try{
            String query = "select avg(priceEach)\n" +
                    "from \n" +
                    "(\n" +
                    "select *, row_number() over (order by priceEach desc) as desc_lat,\n" +
                    "row_number() over (order by priceEach asc) as asc_lat \n" +
                    "from dene\n" +
                    ") a \n" +
                    "where asc_lat in (desc_lat,desc_lat+1,desc_lat-1);";

            Statement stmt =  con.createStatement();
            ResultSet res = stmt.executeQuery(query);


            while (res.next())
            {
                System.out.print(res.getDouble(1));


            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
