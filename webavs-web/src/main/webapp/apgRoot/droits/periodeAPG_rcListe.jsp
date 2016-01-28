<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	String startOfPeriod = "";
	String endOfPeriod = "";
	globaz.apg.vb.droits.APPeriodeAPGListViewBean viewBean = (globaz.apg.vb.droits.APPeriodeAPGListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "apg?userAction=apg.droits.periodeAPG.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<SCRIPT language="JavaScript">
function supprimerPeriode(id){
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        location.href="<%=request.getContextPath()%>/apg?userAction=apg.droits.droitAPGP.actionSupprimerPeriode&selectedId="+id;
    }
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
    <TH>Du </TH>
	<TH>Au </TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.apg.vb.droits.APPeriodeAPGViewBean line = (globaz.apg.vb.droits.APPeriodeAPGViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdPeriode()+"'";
	%>
    <TD class="mtd" width="" onclick="supprimerPeriode(<%=line.getIdPeriode()%>)">
    	<% if (!"false".equalsIgnoreCase(request.getParameter("modifiable"))) { %>
    	<ct:ifhasright element="<%=IAPActions.ACTION_DROIT_LAPG%>" crud="ud">
    	<a href="#">supprimer</a>
    	</ct:ifhasright>
    	<% } %>
    </TD>
    <%
    	if (startOfPeriod == "" || JadeDateUtil.isDateBefore(line.getDateDebutPeriode(), startOfPeriod)) {
    		startOfPeriod = line.getDateDebutPeriode();	
    	}
    	if (endOfPeriod == "" || JadeDateUtil.isDateBefore(line.getDateFinPeriode(), endOfPeriod)) {
    		endOfPeriod = line.getDateFinPeriode();
    	}
    %>
    <TD class="mtd" nowrap="nowrap"><%= line.getDateDebutPeriode()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap"><%= line.getDateFinPeriode()%>&nbsp;</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<input type="hidden" id="startOfPeriod" value="<%=startOfPeriod %>"/>
<input type="hidden" id="endOfPeriod" value="<%=endOfPeriod %>"/>
	<%-- /tpl:insert --%>