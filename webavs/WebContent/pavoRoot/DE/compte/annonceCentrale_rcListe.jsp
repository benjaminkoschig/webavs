<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.hermes.utils.DateUtils"%>
<%@page import="globaz.pavo.translation.CodeSystem"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.pavo.db.compte.*,globaz.globall.util.*"%>
<%
	detailLink ="pavo?userAction=pavo.compte.annonceCentrale.afficher&selectedId=";
    CIAnnonceCentraleListViewBean viewBean = (CIAnnonceCentraleListViewBean)request.getAttribute ("viewBean");
    size = viewBean.getSize();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	    
<Th width="16">&nbsp;</Th>
<Th width=""><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_ANNEE"/></Th>
<Th width=""><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_MOIS"/></Th>
<Th width=""><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_TYPE_LOT"/></Th>
<Th width=""><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_DATE_ENVOI"/></Th>
<Th width=""><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_UTILISATEUR"/></Th>
<Th width=""><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_ETAT"/></Th>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% CIAnnonceCentrale line = (CIAnnonceCentrale)viewBean.getEntity(i);

	%>
	

	
     <% actionDetail = targetLocation+"='"+detailLink+line.getAnnonceCentraleId()+"'"; %>
     <td>
     	<ct:menuPopup menu="CI-MenuOptionAnnonceCentrale">
			<ct:menuParam key="selectedId" value="<%=line.getAnnonceCentraleId()%>"/>
			
			<%if(!line.isGenerationAnnoncePossible()) {%>
				<ct:menuExcludeNode nodeId="generer_annonce_centrale" />
			<% } %>
			
			<%if(!line.isImpressionAnnoncePossible()) {%>
				<ct:menuExcludeNode nodeId="imprimer_protocole_annonce_centrale" />
			<% } %>
			
		</ct:menuPopup>
     </td>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.returnAnneeCreation()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.returnNomMoisCreation()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=CodeSystem.getLibelle(line.getIdTypeAnnonce(),session)%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=CIAnnonceCentrale.CS_ETAT_ENVOYE.equalsIgnoreCase(line.getIdEtat())?line.getDateEnvoi():""%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=line.returnUserVisaFromSpy()%></TD>
     <TD class="mtd" onClick="<%=actionDetail%>" width=""><%=CodeSystem.getLibelle(line.getIdEtat(),session)%></TD>
 
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
