package DataBase;

import utils.Props;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dbconn {

    private static volatile Dbconn instance;
    private static Connection conn = null;
    private PreparedStatement statmt = null;
    private final Props props = Props.getInstance();
    private ResultSet rst;

    private Dbconn() throws IOException { }

    public static Dbconn getInstance() throws IOException {
        if (instance == null) {
            synchronized (Dbconn.class){
                if (instance == null) {
                    instance = new Dbconn();
                }
            }
        }
        return instance;
    }

    public ResultSet Recordset () throws SQLException {
        rst = statmt.executeQuery();
        return rst;
    }

    private void setConn () {
        Context ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/SSC-Data");
            conn = ds.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getAutorityData(int RobotID, int AccID) throws SQLException {

        String[] result = new String[2];

        if (conn == null) {
            this.setConn();
        }else if (conn.isClosed()){
            conn = null
            this.setConn();
        }
        this.setStatmt(
                "SELECT ta.login,ta.pass FROM " + props.getDbName() + " r " +
                "LEFT JOIN app_BindApp b1 ON r.idRob = b1.id1 AND b1.typeBind = 'RobPar' AND b1.activeFlag = 1 " +
                "LEFT JOIN app_RobotsParameters rp ON b1.id2 = rp.idPar AND rp.activeFlag = 1 " +
                "LEFT JOIN app_BindApp b2 ON r.idRob = b2.id1 AND b2.typeBind = 'RobVer' AND b2.activeFlag = 1 " +
                "LEFT JOIN app_Versions v ON b2.id2 = v.idVer AND v.activeFlag = 1 " +
                "LEFT JOIN app_BindApp b3 ON r.idRob = b3.id1 AND b3.typeBind = 'RobVm' AND b3.activeFlag = 1 " +
                "LEFT JOIN app_VmList vl ON b3.id2 = vl.idVm AND vl.activeFlag = 1 " +
                "LEFT JOIN app_BindApp b4 ON r.idRob = b4.id1 AND b4.typeBind = 'RobDep' AND b4.activeFlag = 1 " +
                "LEFT JOIN app_Departments d ON b4.id2 = d.idDep AND d.activeFlag = 1 " +
                "LEFT JOIN app_BindApp b5 ON r.idRob = b5.id1 AND b5.typeBind = 'RobAcc' AND b5.activeFlag = 1 " +
                "LEFT JOIN app_TechnicalAccounts ta ON b5.id2 = ta.idAcc AND ta.activeFlag = 1 " +
                "WHERE r.activeFlag = 1 AND b1.id1 = ? AND b1.id2 = ?"
        );

        statmt.setInt(1,RobotID);
        statmt.setInt(2,AccID);

        rst = this.Recordset();

        while (rst.next()) {

            result[0] = rst.getString("login");
            result[1] = rst.getString("pass");
            break;

        }

        this.closeRecordSet();
        return result;
    }

    public void setStatmt (String sql) throws SQLException {
        statmt = conn.prepareStatement(sql);
    }

    public boolean Exec (String sql) throws SQLException {
        return statmt.execute(sql);
    }

    public void closeRecordSet() throws SQLException {
        rst.close();
    }

    public void closeStatement() throws SQLException {
        statmt.close();
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}