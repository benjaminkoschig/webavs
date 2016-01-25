<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="java.util.Map"%>
<%@ page import="globaz.globall.api.GlobazSystem"%>
<%@ page import="globaz.corvus.application.REApplication"%>
<%@ page import="globaz.prestation.interfaces.fx.PRGestionnaireHelper"%>
<%@ page import="globaz.globall.db.*, globaz.framework.util.*" %>
<%@ page import="globaz.prestation.tools.PRSession"%>
<%@ page import="globaz.corvus.db.demandes.REDemandeRente"%>
<%@ page import="globaz.corvus.api.demandes.IREDemandeRente"%>
<%@ page import="globaz.corvus.db.demandes.REDemandeRenteAPI"%>
<%@ page import="globaz.corvus.db.demandes.REDemandeRenteInvalidite"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	String idGestionnaire     	= (request.getParameter("idGestionnaire") == null || request.getParameter("idGestionnaire") == "0")?"":request.getParameter("idGestionnaire");
	String idDemandeRente 	  	= request.getParameter("idDemandeRente");
	String csTypeDemandeRente 	= request.getParameter("csTypeDemandeRente");
	BSession bsession			= (BSession)PRSession.getSession(session);
	
	Map<String, String> idEtNomGestionnaires = PRGestionnaireHelper.getIdsEtNomsGestionnairesInMap(((REApplication) GlobazSystem.getApplication(REApplication.DEFAULT_APPLICATION_CORVUS)).getProperty(REApplication.PROPERTY_GROUPE_CORVUS_GESTIONNAIRE));
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css" />
		
		<script type="text/javascript">
			function doCancel() {
				window.returnValue = "<%=idGestionnaire%>";
				window.close();
			}
			function doValidate() {
				document.mainForm.status.value = "ok";
				document.mainForm.submit();
			}
			function showError(text) {
				alert('show error');
				document.getElementById("errorLabel").innerHTML = text;
			}
		</script>
		<titlre></title>
	</head>
	<body style="margin-left:0px ; margin-bottom:0px">
		<iframe id="internalFrame" name="internalFrame" style="width:0px ; height:0px"></iframe>
		<table bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="170px">
			<tr>
				<th colspan="3" height="10" class="title">
					<%=bsession.getLabel("MODAL_DB_GESTIONNAIRE_TITLE")%>
				</th>
			</tr>
			<tr>
				<td bgcolor="gray" colspan="3" height="0">
				</td>
			</tr>
			<tr>
				<td colspan="3">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td width="5">
					&nbsp;
				</td>
				<td valign="top">
					<form 	name="mainForm" 
							id="mainForm" 
							target="internalFrame" 
							action="<%=request.getContextPath()%><%=(request.getParameter("mainServletPath")+"Root")%>/demandes/majGestionnaire_dialog.jsp">
						<input 	type="hidden" 
								name="idDemandeRente" 
								value="<%=idDemandeRente%>" />
						<input 	type="hidden" 
								name="status" 
								value="" />
<%	
	if (request.getParameter("status") != null && request.getParameter("status").equals("ok")) {
%>
						<input	type="hidden" 
								name="idGestionnaire" 
								value="<%=idGestionnaire%>">
<%
		REDemandeRente demande = REDemandeRente.loadDemandeRente(bsession, null, idDemandeRente, csTypeDemandeRente);
		demande.setIdGestionnaire("0".equals(idGestionnaire)?"":idGestionnaire);
		
		boolean error = false;
		try {
			demande.update();
		} catch (Exception e) {
			error = true;
		}
		if (error) {
%>
						<script type="text/javascript">
							parent.showError('<%=bsession.getLabel("MODAL_DB_GENR_PR_ERROR1")%>');
						</script>
<%
		} else {
%>
						<script type="text/javascript">
							window.returnValue = "<%=idGestionnaire.equals("0")?"":idGestionnaire%>";
							window.close();
						</script>
<%
		}
	} else {
%>
						<%=bsession.getLabel("MODAL_DB_GESTIONNAIRE_TEXTE1")%>
						<br/>
						<br/>
						<%=bsession.getLabel("MODAL_DB_GESTIONNAIRE_TEXTE2")%>
						<br/>
						<br/>
						<b>
							<%=bsession.getLabel("MODAL_DB_GESTIONNAIRE_TITLE")%>
						</b> 
						<select id="idGestionnaire" 
								name="idGestionnaire" 
								tabindex="1">
							<option value="0">&nbsp;</option>
					<%	for(String unIdGestionnaire : idEtNomGestionnaires.keySet()){%>
							<option value="<%=unIdGestionnaire%>"<%=unIdGestionnaire.equals(idGestionnaire)?" selected":""%>><%=idEtNomGestionnaires.get(unIdGestionnaire)%></option>
					<%	} %>
						</select>
<%	}%>
						<br/>
					</form>
				</td>
			</tr>
			<tr>
			<%
				String imgRef = request.getContextPath() + "/images/"+ bsession.getIdLangueISO().toUpperCase()+ "/btnOkCancel.gif";		    
			%>
				<td bgcolor="#FFFFFF" colspan="3" align="right" height="18">
					<img style="filter:progid:DXImageTransform.Microsoft.BasicImage(grayscale=1, xray=0, mirror=0, invert=0, opacity=0.65, rotation=0, enabled=false)" name="buttons" src="<%=imgRef%>" border="0" usemap="#btnOkCancel">
				</td>
			</tr>
		</table>
		<map name="btnOkCancel">
			<area tabindex="2" shape="rect" coords="1,1,78,17" onclick="javascript:doValidate();" href="#" />
			<area tabindex="3" shape="rect" coords="78,1,157,17" onclick="javascript:doCancel();" href="#" />
			<area shape="default" nohref />
		</map>
	</body>
</html>