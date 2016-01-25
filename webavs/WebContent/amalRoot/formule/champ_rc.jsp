<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
//globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
//globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
//IFrameDetailHeight="160";
//IFrameListHeight="210";
%>
<% idEcran="IEN0140"; %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%--<%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %> --%>
<%
	bButtonFind = false;
	String selectedId = request.getParameter("selectedId");
	String csGroupe = request.getParameter("idCode");
	if ("".equals(csGroupe) || csGroupe == null){
		csGroupe = request.getParameter("csGroupe");
	}
	actionNew+="&csGroupe="+csGroupe;
	
	btnFindLabel = objSession.getLabel("RECHERCHER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	btnExportLabel = objSession.getLabel("EXPORT");
%>

<SCRIPT language="javaScript">
	usrAction = "amal.formule.champ.lister";
	bFind = true;
</SCRIPT>

<SCRIPT>
	reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ai:AIProperty key="LISTE_CHAMPS_POUR_GROUPE"/><ct:FWLabel key="LIBELLE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Groupe de champs --%>
						<TR>
							<TD width="20%" style="width:180px;"><ai:AIProperty key="GROUPE_CHAMPS"/><ct:FWLabel key="LIBELLE"/></TD>
							<TD width="25%" style="width:320px;">
								<INPUT type="text" name="csGroupeValue" value="<%=objSession.getCodeLibelle(csGroupe)%>" readonly="readonly" class="libelleLongDisabled">
								<INPUT type="hidden" name="forIdFormule" value="<%=selectedId%>"/>
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>									
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>