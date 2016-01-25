<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.controleEmployeur.attributionPts.afficherParticuliere&selectedId=";
	AFAttributionPtsListViewBean viewBean = (AFAttributionPtsListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<%@page import="globaz.naos.db.controleEmployeur.AFAttributionPtsListViewBean"%>
<%@page import="globaz.naos.db.controleEmployeur.AFAttributionPtsViewBean"%>
<TH width="30">&nbsp;</TH>
	<TH width="100">Abr.-Nr.</TH>
	<TH width="40">Risiko Punkte</TH>
	<TH width="150">Benutzer</TH>
	<TH width="150">Änderungsdatum</TH>
	<TH width="200">Periode</TH>
	<TH width="200">Kommentar</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
    	AFAttributionPtsViewBean lineBean  = (AFAttributionPtsViewBean)viewBean.getEntity(i);
		actionDetail = targetLocation + "='" + detailLink + lineBean.getIdAttributionPts()+"'";
		String tmp = detailLink + lineBean.getIdAttributionPts();
	%>
	<TD class="mtd" width="16" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=tmp%>"/>
	</TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" width="100"><%=lineBean.getNumAffilie()%></TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" width="40"><%=lineBean.getNbrePoints()%></TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" width="150"><%=lineBean.getLastUser()%></TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" width="150"><%=lineBean.getLastModification()%></TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" width="200"><%=lineBean.getPeriodeDebut()+" - "+lineBean.getPeriodeFin()%></TD>
	<TD align="center" class="mtd" onClick="<%=actionDetail%>" width="200"><%=lineBean.getCommentaires()%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>