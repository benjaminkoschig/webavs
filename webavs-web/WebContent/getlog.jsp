<%
String directory = (String) request.getParameter("directory");
String fileName = (String) request.getParameter("filename");

try {
	java.io.FileReader reader = new java.io.FileReader(globaz.jade.common.Jade.getInstance().getHomeDir() + directory + "/" + fileName);
	java.io.BufferedReader br = new java.io.BufferedReader(reader); 
	
	String line = null;
	while ((line = br.readLine()) != null) {
		out.println(line);
	}
	
	reader.close();
} catch (Exception e) {
	out.println("Error reading file : " + directory + "/" + fileName);
	out.println(e.toString());
}

%>
