<%@page import="globaz.hercule.db.groupement.CEGroupeListViewBean"%><%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.hercule.db.groupement.CEGroupe"%>
<%@page import="globaz.hercule.db.groupement.CEMembreListViewBean"%>
<%@page import="globaz.hercule.db.groupement.CEMembre"%>

<%
	target = "parent.fr_detail";
	targetLocation = target+".location.href";
	detailLink ="hercule?userAction=hercule.groupement.membre.afficher&selectedId=";
	CEMembreListViewBean viewBean = (CEMembreListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	    
		<th><ct:FWLabel key="NUMERO_AFFILIE"/></th>
	    <th><ct:FWLabel key="NOM_AFFILIE_GROUPE"/></th>
	    
<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

		<% 
			CEMembre line = (CEMembre)viewBean.getEntity(i); 
			actionDetail = targetLocation+"='"+detailLink+line.getIdMembre()+"'"; 
		%>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNumeroAffilie()%></TD>
		<TD class="mtd" onClick="<%=actionDetail%>"><%=line.getNomAffilie()%></td>
				
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>