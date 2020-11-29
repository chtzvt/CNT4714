import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

public class DBServlet extends HttpServlet {
	private Connection conn;
	private Statement cmd;
	
	@Override
	public void init(ServletConfig c) throws ServletException {
		super.init(c);
		
		try {
			Class.forName(c.getInitParameter("driver"));
			conn = DriverManager.getConnection(c.getInitParameter("dbname"), c.getInitParameter("dbuser"), c.getInitParameter("dbpass"));
			cmd = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnavailableException("Servlet initialization failed.");
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String query = req.getParameter("query");
		String resBody = "";
		
		if(query.toLowerCase().contains("select")) {
			try {
				resBody = select(query);
			} catch (Exception e) {
				resBody = "<pre>" + e.getMessage() + "</pre>";
				e.printStackTrace();
			}
		} else {
			try {
				resBody = update(query);
			} catch (Exception e) {
				resBody = "<pre>" + e.getMessage() + "</pre>";
				e.printStackTrace();
			}
		}
		
		HttpSession sess = req.getSession();
		sess.setAttribute("result", resBody);
		sess.setAttribute("query", query);
		RequestDispatcher disp = getServletContext().getRequestDispatcher("/index.jsp");
		disp.forward(req, res);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
	
	private String select(String query) throws SQLException {
		ResultSet rs = cmd.executeQuery(query);
		int cols = rs.getMetaData().getColumnCount();
		String html = "<table><thead><tr>";
		
		for (int i = 1; i <= cols; i++) {
			html += "<th>" + rs.getMetaData().getColumnName(i) + "</th>";
		}
		html += "</tr></thead><tbody>";
		
		while(rs.next()){
			html += "<tr>";
			for (int i = 1; i <= cols; i++) {
				html += "<td>" + rs.getString(i) + "</th>";
			}
			html += "</tr>";
		}
		html += "</tbody></table>";
	
		return html;
	}
	
	private String update(String query) throws SQLException {
		String html = "";
		int updated = 0;
		
		ResultSet init = cmd.executeQuery("SELECT COUNT(*) FROM shipments WHERE QUANTITY >= 100");
		init.next();
		int initNum = init.getInt(1);
		
		cmd.executeUpdate("CREATE TABLE tmpshipments LIKE shipments");
		cmd.executeUpdate("INSERT INTO tmpshipments SELECT * FROM shipments");
		
		int numUpdated = cmd.executeUpdate(query);
		html += "<span>" + numUpdated + " rows updated.</span>";
		
		ResultSet after = cmd.executeQuery("SELECT COUNT(*) FROM shipments WHERE quantity >= 100");
		after.next();
		int afterNum = after.getInt(1);
		
		html += "<span>" + initNum + " shipments of quantity > 100 prior to update, " + afterNum + " total now in db</span>";
		
		if(initNum < afterNum){
			int tmp = cmd.executeUpdate("UPDATE suppliers SET status = status+5 WHERE snum IN (SELECT DISTINCT snum FROM shipments LEFT JOIN tmpshipments USING (snum,pnum,jnum,quantity) WHERE tmpshipments.snum IS NULL)");
			html += "<span>Business logic - updated " + tmp + " supplier statuses</span>";
		}
		
		cmd.executeUpdate("DROP TABLE tmpshipments");
		
		return html;
	}
	
}
