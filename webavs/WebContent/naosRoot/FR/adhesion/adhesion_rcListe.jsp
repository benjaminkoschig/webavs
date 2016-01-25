<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.adhesion.adhesion.afficher&selectedId=";
	menuName="AdhesionCotisation";
	globaz.naos.db.adhesion.AFAdhesionListViewBean viewBean = (globaz.naos.db.adhesion.AFAdhesionListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	  	<th width="30">&nbsp;</th>
	  	<th width="400" align="left">Caisse / Association</th>
	  	<th width="150" align="left">Type d'adh&eacute;sion</th>
	  	<th width="100" align="center">Date D&eacute;but</th>
	  	<th width="100" align="center">Date Fin</th>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%    
 			actionDetail = targetLocation + "='" + detailLink + viewBean.getAdhesionId(i) + "'";
		%>
		<TD class="mtd" width="30" >
		<ct:menuPopup menu="AFOptionsAdhesion" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getAdhesionId(i)%>">
			<ct:menuParam key="adhesionId" value="<%=viewBean.getAdhesionId(i)%>"/>
			<ct:menuParam key="planCaisseId" value="<%=viewBean.getPlanCaisseId(i)%>"/>
		</ct:menuPopup>
		</TD>
		<TD class="mtd" onClick="<%=actionDetail%>" width="400" align="left"><%=viewBean.getLibelle(i)%></TD>
 		<TD class="mtd" onClick="<%=actionDetail%>" width="150" align="left"><%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getTypeAdhesion(i))%></TD>
 		<TD class="mtd" onClick="<%=actionDetail%>" width="100" align="center"><%=viewBean.getDateDebut(i)%></TD>
 		<TD class="mtd" onClick="<%=actionDetail%>" width="100" align="center"><%=viewBean.getDateFin(i)%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>