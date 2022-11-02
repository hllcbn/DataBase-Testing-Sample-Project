package DataBase.testing;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;

public class DataBaseTestDemoClass {

    Connection con = null;
    Statement stmt = null;
    CallableStatement cStmt;

    ResultSet rs;
    ResultSet rs1;
    ResultSet rs2;

    @BeforeClass
    void setup() throws SQLException {

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "1311adhb");

    }

    void tearDown() throws SQLException {
        con.close();
    }

    @Test(priority = 1)
    void test_storedProceduresExist() throws SQLException {
        stmt = con.createStatement();
        rs = stmt.executeQuery("SHOW PROCEDURE STATUS WHERE name = 'SelectAllCustomers'");
        rs.next();
        Assert.assertEquals(rs.getString("Name"), "SelectAllCustomers");
        System.out.println(rs);

    }

    @Test(priority = 2)
    void testDistinctState() throws SQLException {
        cStmt =   con.prepareCall("{CALL SelectNumberOfClientBaseState(?)}");
        cStmt.setInt(1,100);
        rs1= cStmt.executeQuery();

        Statement stmt = con.createStatement();
        rs2 = stmt.executeQuery("select distinct\n" +
                "state,count(*) total  \n" +
                "from classicmodels.customers \n" +
                "group by state  having count(*) < 100 order by total desc ;");

        Assert.assertEquals(comperResultSets(rs1,rs2),true);


    }


    @Test(priority = 3)
    void testAvgCalculated() throws SQLException {


        Statement stmt = con.createStatement();
        ResultSet rsAvgCalc = stmt.executeQuery("select avg(priceEach)\n" +
                "from \n" +
                "(\n" +
                "select *, row_number() over (order by priceEach desc) as desc_lat,\n" +
                "row_number() over (order by priceEach asc) as asc_lat \n" +
                "from dene\n" +
                ") a \n" +
                "where asc_lat in (desc_lat,desc_lat+1,desc_lat-1);");

        while (rsAvgCalc.next())
        {
            System.out.print(rsAvgCalc.getDouble(1));
            double result = rsAvgCalc.getDouble(1);
            double expected = 65.275;

            Assert.assertEquals(result,expected);

        }


    }


    public boolean comperResultSets(ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
        while (resultSet1.next()) {
            resultSet2.next();
            int count = resultSet1.getMetaData().getColumnCount();
            for (int i = 1; i <= count; i++) {
                if (!StringUtils.equals(resultSet1.getString(i), resultSet2.getString(i))) {
                    return false;
                }


            }

        }
        return true;




    }
}
