package se.pellpin.conf;

import java.util.List;

/**
 * A Node
 */
public class DataSource {
    Server server;
    List<String> before;
    List<String> after;
    String table;
    List<String> columns;
    String query;

    /**
     * Build a select query either using columns and table name specified, or query.
     */
    public String select() {
        if(query != null) {
            return query;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ").append(String.join(",", columns));
        sql.append(" FROM ").append(table);
        return sql.toString();
    }

    /**
     * Build a select query either using columns and table name specified, or query.
     */
    public String insert() {
        if(query != null) {
            return query;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO ").append(table);
        sql.append("(").append(String.join(",", columns)).append(")");
        sql.append(" VALUES ( ?");

        for(int i=1; i<columns.size(); i++) {
            sql.append(",?");
        }
        sql.append(" )");
        return sql.toString();
    }
}
