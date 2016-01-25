<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.amal.vb.formule.AMFormuleListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %> 
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" --%>
<%
    globaz.amal.vb.formule.AMFormuleListViewBean   viewBean = (globaz.amal.vb.formule.AMFormuleListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	detailLink = "amal?userAction=amal.formule.formule.afficher&selectedId=";
	//detailLink = "amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
		<TH>&nbsp;</TH>
    	<TH>
			<ct:FWLabel key="JSP_AM_PARAMETRAGE_R_FORMULE"/>
    	</TH>
    	<TH>		
    		<ct:FWLabel key="JSP_AM_PARAM_LIB"/>
    	</TH>
    	<TH>
    		<ct:FWLabel key="JSP_AM_PARAM_IMP"/>
    	</TH>
    	<TH>
    		<ct:FWLabel key="JSP_AM_PARAM_TENV"/>
    	</TH>
     <%--	<TH>
    		<ct:FWLabel key="JSP_AM_PARAM_RECVER"/>
    	</TH>--%> 
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
	globaz.amal.vb.formule.AMFormuleViewBean line = (globaz.amal.vb.formule.AMFormuleViewBean)viewBean.getEntity(i);
	//globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	//globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	if (isSelection) { // mode sélection
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";
	} else { // détail "normal"
		actionDetail = targetLocation  + "='" + detailLink + line.getId()+"'";
	}
	%>
    <TD class="mtd" width="">
		<% 	
			String optLabel = "";//line.getSession().getLabel("OPTIONS"); detailLabelId="MENU_OPTION_DETAIL"
		%>

		<ct:menuPopup menu="amal-optionsformules" detailLabelId="MENU_OPTION_DETAIL"  detailLink="<%=detailLink + line.getId()%>">
			<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
		</ct:menuPopup> 
		<%-- 	viewBean.getSession().getCodeLibelle(line.getCsManuAuto())--%>
		
    </TD>	
	<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getFormule().getLibelleDocument())?"&nbsp;":line.getFormule().getLibelleDocument()%></TD>		
	<TD class="mtd" onclick="<%=actionDetail%>"><%="".equals(line.getDefinitionFormule().getCsDocument())?"&nbsp;":objSession.getCodeLibelle(line.getDefinitionFormule().getCsDocument())%></TD>		
	<TD class="mtd" onclick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getFormule().getCsSequenceImpression())%></TD>		
	<TD class="mtd" onclick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getDefinitionFormule().getCsManuAuto())%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>