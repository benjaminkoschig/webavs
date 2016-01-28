<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
  idEcran ="GTI0048";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	globaz.pyxis.db.tiers.TIMoyenCommunicationViewBean viewBean = (globaz.pyxis.db.tiers.TIMoyenCommunicationViewBean)session.getAttribute("viewBean");
	selectedIdValue = request.getParameter("selectedId");
	tableHeight = 180;
	
    if ("add".equals(request.getParameter("_method"))) {
	    bButtonValidate = objSession.hasRight("pyxis.tiers.moyenCommunication", "ADD");
	    bButtonCancel = objSession.hasRight("pyxis.tiers.moyenCommunication", "ADD");
    } else {
	    bButtonValidate = objSession.hasRight("pyxis.tiers.moyenCommunication", "UPDATE");
	    bButtonCancel   = objSession.hasRight("pyxis.tiers.moyenCommunication", "UPDATE");
    }
	
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put  --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Moyen communication Détail"
function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.moyenCommunication.ajouter"
}
function upd() {
}
function validate() {
	var exit = true;
	
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.moyenCommunication.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.moyenCommunication.modifier";
	return (exit);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {
  // fix
   document.forms[0].elements('userAction').value="pyxis.tiers.moyenCommunication.chercher";
  top.fr_appicons.icon_back.click();
  
 } else {
  document.forms[0].elements('userAction').value="pyxis.tiers.moyenCommunication.afficher";
 }

}
function del() {
	if (window.confirm("Sie sind dabei, alle Kommunikationsmittel zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.moyenCommunication.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Kommunikationsmittel<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
		<TR>
			<TD nowrap width="250">Kommunikationsmitteltyp</TD>
			<TD nowrap width="250">
				<ct:FWCodeSelectTag name="typeCommunication"
					      defaut="<%=viewBean.getTypeCommunication()%>"
						wantBlank="<%=false%>"
					      codeType="PYTYPECOMM"
				/>
			</TD>
			<TD width="*">&nbsp;</TD>

		</TR>
		<TR>
			<TD nowrap width="250">Wert</TD>
			<TD nowrap width="250">
				<INPUT width="220" maxlength="100" size="50" type="text" name="moyen" value="<%=viewBean.getMoyen()%>">
			</TD>
			<TD width="*">(Telefonnummer, E-Mail ...)</TD>

		</TR>
		<TR>
			<TD nowrap width="250">Anwendungsbereich</TD>
			<TD nowrap width="250">
				<ct:FWCodeSelectTag name="idApplication"
						defaut="<%=viewBean.getIdApplication()%>"
						wantBlank="<%=false%>"
					      codeType="PYAPPLICAT"
				/>
			</TD>
			<TD width="*">&nbsp;

			<input type="hidden" value="<%=request.getParameter("idContact")%>" name="idContact">
			</TD>

		</TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>


 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>