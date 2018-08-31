<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page
	import="globaz.vulpecula.vb.decomptesalaire.PTControlerMyProdisViewBean"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/process/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1119" />


<%-- visibiltés des boutons --%>
<%@ page import="globaz.globall.util.*"%>
<%
	okButtonLabel = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_BOUTON_CONTROLE_MYPRODIS");
	formEncType = "'multipart/form-data' method='post'";
	idEcran = "PPT2001";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();

    userActionValue = "";
	servletContext = request.getContextPath();
	mainServletPath = (String) request
			.getAttribute("mainServletPath");
	if (mainServletPath == null) {
		mainServletPath = "";
	}
	formAction = "";

	PTControlerMyProdisViewBean viewBean = (PTControlerMyProdisViewBean) session.getAttribute("viewBean");
	userActionValue = "vulpecula.decomptesalaire.controlerMyProdis.executer";
	formAction = servletContext + mainServletPath + "Root/decomptesalaire/controlerMyProdisChargementFichier_de.jsp";
%>

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/process/javascripts.jspf"%>
<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal"
	showTab="menu" />
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Contrôle des décomptes MyProdis";
// stop hiding -->
function validate()
{	
	if (document.getElementById("filename").value == ''){
		alert('<%=objSession.getLabel("ERROR_RENSEIGNEMENT_FICHIER")%>');
		return false;
	}else if (document.getElementById("email").value == ''){
		alert('<%=objSession.getLabel("ERROR_RENSEIGNEMENT_MAIL")%>');
		return false;
	}else{
		return true;
	}
}
</SCRIPT>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<%@ include file="/theme/process/bodyStart.jspf"%>
<label for="email"><ct:FWLabel key="JSP_CONTROLE_MYPRODIS" /></label>
<%@ include file="/theme/process/bodyStart2.jspf"%>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
		<tr>
			<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
		
			<td nowrap width="150"><label for="email"><ct:FWLabel
						key="JSP_EMAIL" /></label></td>
			<td nowrap width="576"><input id="email" type="text"
				class="libelleLong" name="email" value="<%=viewBean.getEmail()%>" /></td>
				

				
		</tr>
		<tr>
			<td nowrap width="150"><label for="wantControleCP"><ct:FWLabel
						key="JSP_MY_PRODIS_INCLURE_CONGES" /></label></td>
			<td nowrap width="576"><input id="wantControleCP" type="checkbox"
				class="libelleLong" name="wantControleCP"  checked="checked" value="on" />
				
				</td>
           	</td>
		</tr>
		<tr>
			<td nowrap width="150"><label for="wantControleSalaires"><ct:FWLabel
						key="JSP_MY_PRODIS_INCLURE_SALAIRES" /></label></td>
			<td nowrap width="576"><input id="wantControleSalaires" type="checkbox"
				class="libelleLong" name="wantControleSalaires"  checked="checked"/></td>
		</tr>
		<tr>
			<td nowrap width="150"><label for="file"><ct:FWLabel
						key="JSP_FILE_TO_IMPORT" /></label></td>
			<td nowrap width="576"><input id="filename" type="file" style="width: 650" accept=".csv" name="filename" class="libelleLong"></td>
		</tr>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/process/footer.jspf"%>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%@ include file="/theme/process/bodyClose.jspf"%>