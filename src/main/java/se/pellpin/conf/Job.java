package se.pellpin.conf;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class Job {

    DataSource source;
    DataSource target;
    HashMap<String,String> columns;



    public void execute(Connection c, List<String> queries) throws Exception {
        if(queries == null) {
            return;
        }
        c.setAutoCommit(false);
        Statement stmt = c.createStatement();
        for (String sql : queries) {
            System.out.println("Executing query: " + sql);
            stmt.execute(sql);
        }
        stmt.close();
        c.commit();
    }

    public void process() {
        Connection sCon = null;
        Connection tCon = null;
        PreparedStatement select = null;
        PreparedStatement insert = null;
        ResultSet rs = null;

        try {

            sCon  = source.server.getConnection();
            tCon  = target.server.getConnection();

            execute(sCon, source.before);
            execute(tCon, target.before);

            select = sCon.prepareStatement(source.select());
            insert = tCon.prepareStatement(target.insert());

            select.setFetchSize(2000);
            select.setQueryTimeout(60);
            sCon.setAutoCommit(false);
            rs = select.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int row = 1;
            System.out.println("Starting data copy");
            tCon.setAutoCommit(false);
            while(rs.next()) {

                if(row % 500 == 0) {
                    tCon.commit();
                    tCon.setAutoCommit(false);
                    System.out.println(row + " rows committed");
                }

                insert.clearParameters();

                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    insert.setObject(i, rs.getObject(i));
                }

                insert.execute();
                row++;
            }
            tCon.commit();

            execute(sCon, source.after);
            execute(tCon, target.after);

            insert.close();
            select.close();
            sCon.close();
            tCon.close();

        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
