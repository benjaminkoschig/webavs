<%@page import="globaz.al.vb.droit.ALDroitListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>
	<% 
	ALDroitListViewBean viewBean = (ALDroitListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    detailLink = "al?userAction=al.droit.droit.afficher&selectedId=";		
	%>
<%-- /tpl:insert --%>

<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>

<%@page import="globaz.al.vb.droit.ALDroitResultViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>


	<th></th>
	<th>GERER EN LABELS ! Nom, prénom</th>
	<th>PR</th>
	<th>Né(e) le</th>
	<th>Début</th>
	<th>Fin</th>
	<th>Motif</th>
	<th>Type</th>
	<th>Montant</th>
	<th>Tarif</th>
						
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%-- /tpl:insert --%>
    
    
<%@ include file="/theme/list/lineStyle.jspf" %>

<%-- tpl:insert attribute="zoneList" --%>
<TD class="mtd">
</TD>
<%
	
ALDroitResultViewBean line = (ALDroitResultViewBean) viewBean.getEntity(i);

if (isSelection) { // mode sélection
	actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
} else { // détail "normal"
	actionDetail = targetLocation  + "='" + detailLink +"'";
}
%>		

<TD onClick="<%=actionDetail%>">
	<%=line.getDroitListComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()%>
	<%=line.getDroitListComplexModel().getEnfantComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2()%>
</TD>
<TD onClick="<%=actionDetail%>"><%= objSession.getCodeLibelle(line.getDroitListComplexModel().getEnfantComplexModel().getEnfantModel().getIdPaysResidence())%></TD>	
<TD onClick="<%=actionDetail%>" style="color:red;">31.05.1983</TD>	
<TD onClick="<%=actionDetail%>"><%=line.getDroitListComplexModel().getDroitModel().getDebutDroit()%></TD>	
<TD onClick="<%=actionDetail%>"><%=line.getDroitListComplexModel().getDroitModel().getFinDroitForcee()%></TD>	
<TD onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getDroitListComplexModel().getDroitModel().getMotifFin())%></TD>	
<TD onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getDroitListComplexModel().getDroitModel().getTypeDroit())%></TD>	
<TD onClick="<%=actionDetail%>">0.00</TD>	
<TD onClick="<%=actionDetail%>"><%=line.getDroitListComplexModel().getDroitModel().getTarifForce()%></TD>	
	
<%-- /tpl:insert --%>		

<%@ include file="/theme/list/lineEnd.jspf" %>


<%@ include file="/theme/list/tableEnd.jspf" %>




