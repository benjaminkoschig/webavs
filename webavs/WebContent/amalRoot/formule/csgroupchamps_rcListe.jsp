<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--<%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %>--%>
<%
	globaz.amal.vb.formule.AMCsgroupchampsListViewBean viewBean = (globaz.amal.vb.formule.AMCsgroupchampsListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = "amal?userAction=amal.formule.champ.chercher&idCode=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    	<TH><ai:AIProperty key="GROUPE_CHAMPS"/><ct:FWLabel key="LIBELLE"/></TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	globaz.amal.vb.formule.AMCsgroupchampsViewBean line = (globaz.amal.vb.formule.AMCsgroupchampsViewBean)viewBean.getEntity(i);
	if (isSelection) { // mode sélection
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
	} else { // détail "normal"
		actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
	}
	%>
	<%--<TD class="mtd" onclick="<%=actionDetail%>"><%=line.getCodeSysteme().getLibelle()%></TD>--%>
	<TD class="mtd"><%=line.getCodeSysteme().getLibelle()%></TD>		

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>