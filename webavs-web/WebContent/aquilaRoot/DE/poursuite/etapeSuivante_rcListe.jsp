<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.aquila.db.poursuite.*"%>
<%@ page import="globaz.aquila.db.access.poursuite.*"%>
<%
	COContentieux contentieuxViewBean = (COContentieux) session.getAttribute("contentieuxViewBean");
	COEtapeSuivanteListViewBean viewBean = (COEtapeSuivanteListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	wantPagination = false;
%>
<%@ taglib uri="/WEB-INF/aquila.tld" prefix="co"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    	<TH>Folgende Etappe</TH>
    	<TH>Ausführungsdatum</TH>
    	<TH>Automatisch</TH>
    	<TH>Manuell</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    	
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		COEtapeSuivanteViewBean line = (COEtapeSuivanteViewBean) viewBean.getEntity(i);
		String dateDeclenchement = "-";
		if (globaz.jade.client.util.JadeStringUtil.isEmpty(contentieuxViewBean.getProchaineDateDeclenchement())) {
			dateDeclenchement = line.calculerDateProchainDeclenchement(contentieuxViewBean);
		} else {
			dateDeclenchement = contentieuxViewBean.getProchaineDateDeclenchement();
		}
		
		if (objSession.hasRight("aquila.batch.transition.effectuertransition", globaz.framework.secure.FWSecureConstants.UPDATE)) {
			actionDetail = targetLocation + "='aquila?userAction=aquila.batch.transition.afficher&selectedId=" + line.getIdTransition() + "'";
		} else {
			actionDetail = "";
		}
	%>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getEtapeSuivante().getLibActionLibelle()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=dateDeclenchement%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
		<co:COTrueFalseImg flag="<%=line.getAuto()%>"/>
	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="center">
		<co:COTrueFalseImg flag="<%=line.getManuel()%>"/>
	</TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>