<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerieListViewBean"%>
<%@page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerie"%>
<%
	detailLink = "musca?userAction=musca.gestionJourFerie.gestionJourFerie.afficher&selectedId=";
	FAGestionJourFerieListViewBean viewBean = (FAGestionJourFerieListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	boolean isToAfficher = true;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

	
	
<%@page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerieUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%><TH nowrap><ct:FWLabel key="DATE_JOUR"/></TH>
	<TH nowrap><ct:FWLabel key="LIBELLE"/></TH>
	<TH nowrap><ct:FWLabel key="DOMAINE"/></TH>
	<TH nowrap><ct:FWLabel key="SELECTIONNER"/></TH>
	
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<%
		FAGestionJourFerie line = (FAGestionJourFerie) viewBean.getEntity(i);
	 	actionDetail = targetLocation + "='" + detailLink + line.getIdJour() + "'";
	 	String actif = "";	
	 %>	
	
 	<%
 	isToAfficher = true;  
 	for(String domaine : viewBean.getDomaineFerie()) {
 		isToAfficher = false;
 		if(line.getDomaineFerie().contains(domaine)){
 			isToAfficher = true;
 			break;
 		}
 	}
 	
 	if(isToAfficher){
 	%>
		<TD align="center" width="100em"  onClick="<%=actionDetail%>" ><%=line.getDateJour()%></TD>
		<TD align="left" class="mtd" onClick="<%=actionDetail%>" ><%=line.getLibelle()%></TD>
		<TD align="left" class="mtd" onClick="<%=actionDetail%>" ><%=FAGestionJourFerieUtil.getDomaineFerieToString(line.getDomaineFerie(),line.getSession())%></TD>
		<TD align="center" width="100em"  ><input type="checkbox" name="chkJourAModifierSupprimer"  value="<%=line.getIdJour()%>"></TD> 
	<%}%>		
	 
	
	
<%-- /tpl:put --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>