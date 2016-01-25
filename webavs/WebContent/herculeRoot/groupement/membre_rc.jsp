<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/capage/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.hercule.db.groupement.CEGroupeViewBean"%>

<%
	idEcran = "CCE0008";
	CEGroupeViewBean viewBean = (CEGroupeViewBean) session.getAttribute("viewBeanFK");
	IFrameDetailHeight = "500";
	String idGroupe = request.getParameter("idGroupe");
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
	bButtonNew = objSession.hasRight(userActionNew, "ADD");
%>

<SCRIPT	language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
	top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
	usrAction = "hercule.groupement.membre.lister";
	detailLink = servlet+"?userAction=hercule.groupement.membre.afficher&_method=add";
	bFind = true;
</SCRIPT>

<ct:menuChange displayId="options" menuId="CE-OptionsGroupe" showTab="options" checkAdd="false">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdGroupe()%>"/>
	<ct:menuSetAllParams key="idGroupe" value="<%=viewBean.getIdGroupe()%>"/>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_APERCU_MEMBRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

<TR>
	<td width="250"><ct:FWLabel key="NOM_GROUPE"/></td>
	<td width="250">
		<input type="text" class="disabled" readonly="readonly" size="20" value="<%=viewBean.getLibelleGroupe()%>" /> 
		<INPUT type="hidden" name="forIdGroupe" value="<%=viewBean.getIdGroupe()%>">
	</td>
</TR>
<TR>
	<td width="250"><ct:FWLabel key="GROUPE_COUVERTURE"/></td>
	<td width="250"><input type="text" class="disabled" readonly="readonly" size="4" value="<%=viewBean.getAnneeCouvertureMinimal()%>" /> 
	</td>
</TR>

<%@ include file="/theme/capage/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf"%>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf"%>
<%-- /tpl:insert --%>