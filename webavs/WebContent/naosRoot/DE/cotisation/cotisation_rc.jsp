<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0009";
	globaz.naos.db.cotisation.AFCotisationViewBean viewBean = (globaz.naos.db.cotisation.AFCotisationViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	usrAction = "naos.cotisation.cotisation.lister";
	bFind = true;
	
	function updateFilter(choice) {
		var filtre = document.getElementById("forAnneeActive");
		var resume = document.getElementById("typeAffichage");
		
		if(choice=="actives") {
			filtre.value = "<%=globaz.globall.util.JACalendar.today().getYear()%>";
			resume.value = "";
		} 
		if(choice=="resume") {
			filtre.value = "";
			resume.value = "<%=globaz.naos.db.cotisation.AFCotisationManager.AFFICHAGE_RESUME%>";
		} 
		if (choice=="") {
			filtre.value = "";
			resume.value = "";
		}
	}
	
	function resetAdhesion() {
		document.getElementById("forAdhesionId").value = "";
	}
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" checkAdd="no"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options" checkAdd="no">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliation().getAffiliationId()%>" checkAdd="no"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliation().getAffiliationId()%>" checkAdd="no"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					Versicherung nach Erfassungsperiode
					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%
							if(viewBean.hasPlanAffiliation()) {
								actionNew += "&affiliationId=" + viewBean.getAffiliation().getAffiliationId();
							
								if (!globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getPlanAffiliationId())) {
									actionNew += "&planAffiliationId=" + viewBean.getPlanAffiliationId();
								} else {
									actionNew += "&planAffiliationId="; // on evite la chaine "null"
								}
							} else {
								bButtonNew = false;
							}
						%>
						<naos:AFInfoAffiliation name="forAffiliationId" affiliation="<%=viewBean.getAffiliation()%>" />
						<!--INPUT type="hidden" name="forPlanAffiliationId" value="<%=viewBean.getPlanAffiliationId()%>" -->
						<INPUT type="hidden" name="forAdhesionId" value="<%=viewBean.getAdhesionId()%>">
						
						
						<TR>
							<TD height="30">Art</TD>
							<TD>
								<ct:FWCodeSelectTag 
									name="forAssuranceGenre" 
									defaut=""
									codeType="VEASSURANC"
									wantBlank="true"/>
							</TD>
							<TD width="80">Anzeige</TD>
							<TD>
								<select name="filtreActif" onchange="updateFilter(this.options[this.selectedIndex].value)">
									<OPTION value="">Alle Beiträge</OPTION>
									<OPTION value="actives" selected>Aktive Beiträge <%=globaz.globall.util.JACalendar.today().getYear()%></OPTION>
									<OPTION value="resume">Zusammenfassung</OPTION>
								</select>
								<input type="hidden" name="forAnneeActive" value="<%=globaz.globall.util.JACalendar.today().getYear()%>">
								<input type="hidden" name="typeAffichage" value="">
								<input type="hidden" name="showInactif" value="true">
								<input type="hidden" name="order" value="MBTGEN,IDEXTERNE,MEDDEB";">
							</TD>
							<TD>&nbsp;Inaktive Beiträge</TD>
							<TD><INPUT type="checkbox" name="forInactive"></TD>
						</TR>
						<TR>
							<TD height="30">Erfassungsplan</TD>
							<TD>
								<select name="forPlanAffiliationId">
									<%=globaz.naos.util.AFUtil.getPlanAffiliationInfoRom280(viewBean.getAffiliation().getAffiliationId(), viewBean.getPlanAffiliationId(), session, true)%>
								</select>
							</TD>
							<TD height="30" width="80">Kassenplan</TD>
							<TD>
								<ct:FWListSelectTag name="forPlanCaisseId" data="<%=viewBean.getPlanCaisseAdhesions()%>" defaut="<%=viewBean.getPlanCaisseId()%>" doClientValidation="' onchange='resetAdhesion()"/>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>