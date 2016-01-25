<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0026";
	globaz.naos.db.lienAffiliation.AFLienAffiliationViewBean viewBean = (globaz.naos.db.lienAffiliation.AFLienAffiliationViewBean)session.getAttribute ("viewBean");	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.lienAffiliation.lienAffiliation.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	
	document.forms[0].elements('userAction').value="naos.lienAffiliation.lienAffiliation.modifier";
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.lienAffiliation.lienAffiliation.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.lienAffiliation.lienAffiliation.modifier";
	return (exit);
}

function cancel() {
 if (document.forms[0].elements('_method').value == "add")
 document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="naos.lienAffiliation.lienAffiliation.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer un Lien d'affiliation ! Voulez-vous continuer ?")) {
		document.forms[0].elements('userAction').value="naos.lienAffiliation.lienAffiliation.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

function affilieSelect() {
	document.forms[0].elements('userAction').value="naos.lienAffiliation.affilieLienAffiliation.selectAffilie";
	document.forms[0].submit();
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Lien Affiliation - D&eacute;tail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="150" contentWidth="450"/>
						<TD>&nbsp;</TD>
						<TR>
							<TD nowrap  height="11" colspan="3"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR>
							<TD nowrap width="125" colspan="3">&nbsp;</TD>
						</TR>	
						<TR>
							<!--TD nowrap width="125">Li&eacute; &agrave; l'affili&eacute;:</TD-->
	         				
		        			<TD nowrap colspan="2"> 
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getLienAffiliationId()%>'>
								<TABLE border="0" cellspacing="0" cellpadding="0">
									<naos:AFInfoAffiliation affiliation="<%=viewBean.getLienAffiliation()%>" titleWidth="150" contentWidth="450" showTitle="Lié à l'affilié"/>
								
								</TABLE>
							</TD>
							<TD nowrap>
								<INPUT type="submit" value="..." onclick="affilieSelect()">
	            			</TD>
						</TR>
						<TR>
							<TD nowrap width="125" colspan="3">&nbsp;</TD>
						</TR>
           				<TR>
            				<TD nowrap width="125" height="31">Type de Lien</TD>
           				<TD nowrap colspan="2">
            					<ct:FWCodeSelectTag 
            						name="typeLien" 
            						defaut="<%=viewBean.getTypeLien()%>"
									codeType="VETYPELIEN"/>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="125" height="31">Date D&eacute;but</TD>
							<TD nowrap colspan="2"> 
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>
							</TD>
						</TR>
						<TR>
							<TD nowrap width="125" height="31">Date Fin</TD>
							<TD nowrap colspan="2">
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<% } %>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>