<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.naos.db.particulariteAffiliation.AFParticulariteAffiliationViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0036";
	AFParticulariteAffiliationViewBean viewBean = (AFParticulariteAffiliationViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('dateDebut').value = "";
	document.forms[0].elements('dateFin').value = "";
	document.forms[0].elements('userAction').value="naos.particulariteAffiliation.particulariteAffiliation.ajouter";
}

function upd() {
}

function validate() {
	var exit = true;
	
	document.forms[0].elements('userAction').value="naos.particulariteAffiliation.particulariteAffiliation.modifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.particulariteAffiliation.particulariteAffiliation.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.particulariteAffiliation.particulariteAffiliation.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.particulariteAffiliation.particulariteAffiliation.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, den ausgewählten Beitrag zu löschen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.particulariteAffiliation.particulariteAffiliation.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Erfassungseigenheiten - Detail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="100" /> 
						<TR> 
							<TD nowrap colspan="2"><INPUT type="hidden" name="selectedId"  value="<%=viewBean.getParticulariteId()%>"></TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="2"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="167">Eigenheit</TD>
							
							<TD nowrap> 
								<ct:FWCodeSelectTag 
									name="particularite" 
									defaut="<%=viewBean.getParticularite()%>" 
									codeType="VEPARTICUL"
									wantBlank="false"/> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="167">Periode</TD>
							
							<TD nowrap> 
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" /> 
								bis 
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>" /> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="167">Numerischen Wert</TD>
							
							<TD nowrap > 
								<INPUT name="champNumerique" size="20" type="text" value='<%=!JadeStringUtil.isIntegerEmpty(viewBean.getChampNumerique())?viewBean.getChampNumerique():""%>'>
							</TD>
						</TR>
						<TR> 
						<TR> 
							<TD nowrap width="167" height="31">Alphanumerischen Wert</TD>
							
							<TD nowrap height="31"> 
								<INPUT name="champAlphanumerique"  class="libelleLong" size="20" type="text" value="<%=viewBean.getChampAlphanumerique()%>">
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