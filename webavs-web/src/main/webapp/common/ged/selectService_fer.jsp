<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@page import="globaz.jade.client.util.JadeStringUtil"%><HTML>
 <!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<HEAD>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
String lastModification = ""; 
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String usrAction = "";
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
if (mainServletPath == null) {
	mainServletPath = "/naos";
}
String selectedIdValue = "";
int tableHeight = 243;
String subTableWidth = "100%";
String formAction = servletContext + mainServletPath;
String applicationId = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION)).getApplicationId();

String idEcran = null;
%>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 

function doActionCancel() {
	self.close();
}


function doActionOk() {
	if (document.getElementsByName("serviceNameId")[0].value =="CTX") {
		if ((document.getElementsByName("aquila.contentieux.idContentieux")[0].value =="null") ||
			(document.getElementsByName("aquila.contentieux.idContentieux")[0].value =="")) {

			// rêgle spécifique FER 
			// si acces au CTX sans id de dossier, on utilise le dossier 00000, sans rôle
			document.getElementsByName("aquila.contentieux.idContentieux")[0].value ="0000000";
			document.getElementsByName("pyxis.role.tiers.document")[0].value ="*";
		}
	}
	document.forms[0].submit();
}
</SCRIPT>
<%
java.util.ListIterator iter = null;
java.util.List services = null;
java.util.Map <String, java.util.List> mService = null;
boolean hasManyService = "true".equals(globaz.jade.properties.JadePropertiesService.getInstance().getProperty("common.many.ged.actives"));
if(hasManyService) {
    String[] adaptersName = globaz.jade.properties.JadePropertiesService.getInstance().getProperty("common.many.ged.adapter").split(",");
    mService = new java.util.HashMap();
    for(String adapterName : adaptersName){
        java.util.List aservices = globaz.jade.ged.client.JadeGedFacade.getServicesList(adapterName);
        mService.put(adapterName, aservices);
    }
} else {
	services = globaz.jade.ged.client.JadeGedFacade.getServicesList();
	if (services != null) {
		iter = services.listIterator();
	}
}
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" /> 
<link rel="stylesheet" type="text/css" href="../../theme/master.css"/>
</head>
<body>
<FORM name="mainForm" action="<%=formAction%>" >
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
		<TR>
			<TH colspan="3" height="10" class="title">
				<DIV style="width: 100%">
					<SPAN class="idEcran"><%=(null==idEcran)?"":idEcran%></SPAN>
					<ct:FWLabel key="FW_GED_SERVICE_CHOICE"/>
				</DIV>
      		</TH>
		</TR>
		<TR>
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD valign="top">
			<ct:FWLabel key="FW_GED_SERVICE_PLEASE_CHOOSE"/>
			<% if (hasManyService) {%>
			<select name="serviceNameId">
			<%   for(java.util.Map.Entry<String, java.util.List> entry : mService.entrySet()){%>
			    <option value="<%=entry.getKey()%>" label="<%=entry.getKey()%>" disabled><%=entry.getKey()%></option>
			    <%   for(Object oservice  : entry.getValue()){
			        globaz.jade.ged.message.JadeGedService service = (globaz.jade.ged.message.JadeGedService) oservice;
			        	if (service == null) { %>
							<option value="(-)" label="(-)">(-)</option>
						<% } else { %>
							<option value="<%=service.toString()%>" label="&nbsp;&nbsp;&nbsp;<%=service.getText()%>">&nbsp;&nbsp;&nbsp;<%=service.getText()%></option>
						<%
						}
					}
			    } %>
			    </select>
			    <%
			} else { %>
			<% if (iter == null) {%>
				(iter: <%=iter%> and services: <%=services%>)
			<% } else { %>
				<select name="serviceNameId">
				<%
				while (iter.hasNext()) {
					globaz.jade.ged.message.JadeGedService nextService = (globaz.jade.ged.message.JadeGedService)iter.next();
					if (nextService == null) { %>
						<option value="(-)" label="(-)">(-)</option>
					<% } else { %>
					<option value="<%=nextService.toString()%>" label="<%=nextService.getText()%>"><%=nextService.getText()%></option>
				<%
					}
				}
				%>
				</select>
				<script>
					// pour le cas ou la fenêtre cette écran est appelé depuis un menu qui appartient a une liste 
					//(et qui ne peut donc pas traité le paramètre responseTarget="_blank" du menu...
					if (!opener) {
						document.getElementsByName("mainForm")[0].target="_new" ; // ouvre la fenêtre de l'appel GED dans une nouvelle fenêtre
					}
				</script>
			<% 
				}
			
			} // fin du else
			
java.util.Enumeration params = request.getParameterNames();
String service ="";

while(params.hasMoreElements()) {
	String nextName = (String)params.nextElement();
	String nextValue = request.getParameter(nextName);
%>
		<br><input type="hidden" name="<%=nextName%>" value="<%=nextValue%>"/>
<%
}

if (!JadeStringUtil.isIntegerEmpty(request.getParameter("osiris.section.id")) || !JadeStringUtil.isIntegerEmpty(request.getParameter("aquila.contentieux.idContentieux"))){
	service ="CTX";
}
if (!JadeStringUtil.isIntegerEmpty(service)) {
	%>
	<script>document.getElementsByName("serviceNameId")[0].value ="<%=service%>"</script>
	<%
} else {
	%>
	<script>document.getElementsByName("serviceNameId")[0].value ="REN"</script>
	<%
	
}

%>
<br>
		<input type="hidden" name="userAction" value="naos.affiliation.affiliation.gedafficherdossier"/>
		</TD>
			<TD width="5">&nbsp;</TD>
		</TR>
		<TR>
			<TD colspan="3" height="13">&nbsp;</TD>
		</TR>
		
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18">
				<INPUT class="btnCtrl" id="btnVal" type="button" value="Valider" onclick="doActionOk()">
				<INPUT class="btnCtrl" id="btnCan" type="button" value="Annuler" onclick="doActionCancel()">
			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
</BODY>
</HTML>