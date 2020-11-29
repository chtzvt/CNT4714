<!doctype html>

<%--
/*
  Name: Charlton Trezevant
  Course: CNT 4714 - Fall 2020 - Project Four
  Assignment title: A Three-Tier Distributed Web-Based Application
  Date: December 4, 2020
*/
--%>

<%
  String query = "", result = "*";

  if (session.getAttribute("query") != null)
    query = (String)session.getAttribute("query");
    
  if (session.getAttribute("result") != null)
    result = (String)session.getAttribute("result");
%>

	<html>
		<head>
			<title>Project 4</title>
      
      <style>
        body {
          background-color: blue;
        }
      
        h1, span {
          color: white;
        }
        
        button {
          color: yellow;
          background-color: black;
        }
        
        table {
          border: 1px solid black;
        }
        
        textarea {
          background-color: black;
          color: green;
        }
        
        tr, thead {
          border-top: 1px solid black;
          border-bottom: 1px solix black;
          background-color: white;
        }
        
        td, th {
          border-left: 1px solid black;
          border-right: 1px solid black;
        }
      </style>
      
		</head>

		<body>
			<div>
				<center>
					<h1>Welcome to the Fall 2020 Project 4 Enterprise Database System
					</h1>
					<h1>A Servlet/JSP-Based Multi-tiered Enterprise Application Using A Tomcat Container</h1>
					<span>
						You are connected to the Project 4 Enterprise System Database.</span>
					<span>
						Please enter any valid SQL query or update command.</span>
					<div>
						<span>All execution results will appear below.</span>
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
        <hr />
				<div>
					<center>
            <span><strong>Database Results</strong></span>
						<%= result %>
					</center>
				</div>
			</div>
		</body>
	</html>
