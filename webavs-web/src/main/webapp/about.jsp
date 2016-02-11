
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<HTML>
<HEAD>
<%@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>about.jsp</TITLE>
</HEAD>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.api.BISession currBISession = (controller != null ? controller.getSession() : null);
globaz.globall.db.BSession currSession = (globaz.globall.db.BSession)currBISession;
globaz.jade.admin.user.bean.JadeUser user = (currSession != null ? currSession.getUserInfo() : null);
String[] userGroups = (user != null ? globaz.jade.admin.JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getUserGroupService().findAllIdGroupForIdUser(user.getIdUser()): null);
String[] userRoles = (user != null ? globaz.jade.admin.JadeAdminServiceLocatorProvider.getInstance().getServiceLocator().getRoleUserService().findAllIdRoleForIdUser(user.getIdUser()): null);
globaz.globall.db.BApplication currApplication = (currSession != null ? currSession.getApplication() : null);
String jdbcUrl = globaz.jade.common.Jade.getInstance().getDefaultJdbcUrl();
globaz.jade.jdbc.JadeJdbcDatasource datasource = null;
if ((jdbcUrl != null) && (jdbcUrl.toLowerCase().startsWith("jdbc:jade:"))) {
	datasource = globaz.jade.jdbc.JadeJdbcDriver.getInstance().getDatasource(jdbcUrl.substring(10));
}
String jdbcDriverName = "";
String jdbcDriverUrl = "";
if (datasource != null) {
	try {
		globaz.jade.jdbc.JadeJdbcDatasourceDirect dsDirect = (globaz.jade.jdbc.JadeJdbcDatasourceDirect)datasource;
		jdbcDriverName = dsDirect.getDriverClassName();
		jdbcDriverUrl = dsDirect.getUrl();
	} catch (Exception e) {
	}
}
%>
<BODY>
<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title">Application informations</TH>
        </TR>
        <TR>
            <TD>
	            <TABLE border="0" bgcolor="#B3C4DB">
	                <COL span="1" width="10">
	                <COL span="1" width="140" valign="top">
	                <COL span="1" width="650">
	                <TBODY>
	                    <TR>
	                        <TD></TD>
	                        <TD>Application :</TD>
	                        <TD>
	                        	<B>Web@AVS / Web@AHV (technical version <%=globaz.webavs.common.WebavsDocumentionLocator.getVersionTechnique()%><%=globaz.webavs.common.WebavsDocumentionLocator.getServicePackVersion()%> released on <%=globaz.webavs.common.WebavsDocumentionLocator.getDate()%>)</B>
	                        	<BR>
	                        	<%
	                        		java.util.List applications = new java.util.LinkedList();
	                        		// liste les applications
	                        		applications.add("Framework");
	                        		applications.add("Pyxis");

	                        		java.util.List versions = new java.util.LinkedList();
	                        		for (java.util.Iterator iter = applications.iterator(); iter.hasNext(); ) {
	                        			String applicationName = (String)iter.next();
	                        			try {
		                        			globaz.globall.api.BIApplication app = globaz.globall.db.GlobazServer.getCurrentSystem().getApplication(applicationName.toUpperCase());
			                        		if (app != null) {
			                        			versions.add(applicationName + " " + app.getVersion() + " (" + (app.getDate() != null ? app.getDate() : "") + ")");
			                        		} else {
			                        			versions.add(applicationName + " APPLICATION NOT AVAILABLE");
			                        		}
			                        	} catch (java.lang.Exception e) {
			                        		// Do nothing
			                        	}
		                        	}
	                        		for (java.util.Iterator iter = versions.iterator(); iter.hasNext(); ) {
	                        			String version = (String)iter.next();
	                        	%>
	                        	- <%=version%><BR>
	                        	<%
	                        		}
	                        	%>
	                        </TD>
	                    </TR>
	                    <TR>
	                    <TD>&nbsp;</TD>
	                    <TD>&nbsp;</TD>
	                    <TD>&nbsp;</TD>
	                    </TR>
	                    <TR>
	                        <TD>&nbsp;</TD>
	                        <TD>Logs :</TD>
	                        <TD><a href="logs.jsp" target="_blank">Show logs folder</a></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>Globaz SA :</TD>
	                        <TD><B><A href="http://www.globaz.ch" target="_blank">http://www.globaz.ch</A></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>Licences :</TD>
	                        <TD><B><A href="shared/webavs_librairies_externes.pdf" target="_blank">Show</A></B></TD>
	                    </TR>
				        <TR>
				            <TD>&nbsp;</TD>
				        </TR>
	                </TBODY>
	            </TABLE>
            </TD>
        </TR>
        <TR>
            <TD class="title"><B>USER INFO</B></TD>
        </TR>
        <TR>
            <TD>
	            <TABLE border="0" bgcolor="#B3C4DB">
	                <COL span="1" width="10">
	                <COL span="1" width="150" valign="top">
	                <COL span="1" width="640">
	                <TBODY>
						<% if (user != null) { %>
	                    <TR>
	                        <TD></TD>
	                        <TD>Current session :</TD>
	                        <TD><B><%=(currSession != null ? currSession.getApplicationId() + " #" + currSession.getSessionId() + " connected as " + user.getVisa() + " [" + user.getIdUser() + "]" : "(null)")%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>Full user name :</TD>
	                        <TD><B><%=user.getFirstname() + " " + user.getLastname() + " (" + user.getEmail() + ")"%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>User group(s) :</TD>
	                        <TD><B><%=globaz.jade.client.util.JadeConversionUtil.toString(userGroups)%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>User role(s) :</TD>
	                        <TD><B><%=globaz.jade.client.util.JadeConversionUtil.toString(userRoles) %></B></TD>
	                    </TR>
				        <TR>
				            <TD>&nbsp;</TD>
				        </TR>
						<% } %>
	                </TBODY>
	            </TABLE>
            </TD>
        </TR>
        <TR>
            <TD class="title"><A href="systemStatus.jsp" target="_blank"><IMG src="<%=request.getContextPath()%>/images/bug.jpg" align="middle"/></A><B> JADE INFO</B></TD>
        </TR>
        <TR>
            <TD>
	            <TABLE border="0" bgcolor="#B3C4DB">
	                <COL span="1" width="10">
	                <COL span="1" width="140" valign="top">
	                <COL span="1" width="650">
	                <TBODY>
	                    <TR>
	                        <TD></TD>
	                        <TD>Status :</TD>
	                        <TD><B><%=globaz.jade.common.Jade.getInstance().getName() + (globaz.jade.common.Jade.getInstance().getStartTime() != "" ? " started at " + globaz.jade.common.Jade.getInstance().getStartTime() : " not started")%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>Version :</TD>
	                        <TD><B><%=globaz.jade.common.Jade.getInstance().getVersion() + " released on " + globaz.jade.common.Jade.getInstance().getDate() + " running on " + globaz.jade.client.util.JadeUtil.getCurrentIP()%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>Published on :</TD>
	                        <TD><B><%=globaz.jade.common.Jade.getInstance().getRootUrl() + " (RMI port:" + globaz.jade.common.Jade.getInstance().getRmiPort() + ")"%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>Memory usage :</TD>
	                        <TD><B><%="Memory usage: " + java.text.NumberFormat.getNumberInstance().format(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) + " bytes used on " + java.text.NumberFormat.getNumberInstance().format(Runtime.getRuntime().totalMemory()) + " (" + java.text.NumberFormat.getNumberInstance().format(Runtime.getRuntime().freeMemory()) + " free - " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())*100/Runtime.getRuntime().totalMemory() + "% used)"%></B></TD>
	                    </TR>

	                    <%
	                    String appsrv = System.getProperty("globaz.appsrv");
	                    if (!JadeStringUtil.isBlank(appsrv)) {
	                    %>
	                    <TR>
	                        <TD></TD>
	                        <TD>Application server :</TD>
	                        <TD><B><%=appsrv%></B></TD>
	                    </TR>
	                    <%
	                    }
	                    %>

				        <TR>
				            <TD>&nbsp;</TD>
				        </TR>
	                </TBODY>
	            </TABLE>
            </TD>
        </TR>
        <TR>
            <TD class="title"><B>JDBC INFO</B></TD>
        </TR>
        <TR>
            <TD>
	            <TABLE border="0" bgcolor="#B3C4DB">
	                <COL span="1" width="10">
	                <COL span="1" width="140" valign="top">
	                <COL span="1" width="650">
	                <TBODY>
	                    <TR>
	                        <TD></TD>
	                        <TD>Datasource :</TD>
	                        <TD><B><%=(datasource != null ? datasource.getName() + ": Max=" + datasource.getMaxConnections() + ",used=" + datasource.getNbUsedConnections() + ",free=" + datasource.getNbFreeConnections() : "(datasource unknown)")%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>Driver name :</TD>
	                        <TD><B><%=(jdbcDriverName != null ? jdbcDriverName : "(null)")%></B></TD>
	                    </TR>
	                    <TR>
	                        <TD></TD>
	                        <TD>URL :</TD>
	                        <TD><B><%=(jdbcDriverUrl != null ? jdbcDriverUrl : "(null)")%></B></TD>
	                    </TR>
				        <TR>
				            <TD>&nbsp;</TD>
				        </TR>
	                </TBODY>
	            </TABLE>
            </TD>
        </TR>
</TABLE>
</BODY>
</HTML>
