<!doctype html>
<%
  String query = "", result = "";

  if (session.getAttribute("query") != null)
    query = (String)session.getAttribute("query");
    
  if (session.getAttribute("result") != null)
    result = (String)session.getAttribute("result");
%>

	<html>
		<head>
			<title>Project 4</title>
		</head>

		<body>
			<div>
				<center>
					<h1>Welcome to the Fall 2020 Project 4 Enterprise Database System
					</h1>
          <h1>A Servlet/JSP-Based Multi-tiered Enterprise Application Using A Tomcat Container</h1>
					<div>
						You are connected to the Project 4 Enterprise System Database.</div>
					<div>
						Please enter any valid SQL query or update statement.</div>
					<div>
					<div>All execution results will appear below.</div>
					<form action="/Project4/exec" method="post">
						<div>
							<div>
								<textarea name="query" id="query" rows="16" cols="100"><%= query %></textarea>
							</div>
						</div>

						<button type="submit">Execute Command</button>
						<button onClick="(document.getElementById('query').value = '';)();" type="reset">Reset Form</button>
					</form>
				</center>
			</div>
			<div>
				<%= result %>
			</div>
		</div>
	</body>
</html>
