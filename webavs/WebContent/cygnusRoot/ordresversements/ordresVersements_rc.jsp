<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@page import="globaz.cygnus.vb.ordresversements.RFOrdresVersementsListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%@page import="globaz.cygnus.vb.ordresversements.RFOrdresVersementsViewBean"%>
<%@page import="globaz.cygnus.api.ordresversements.IRFOrdresVersements"%>
<%-- tpl:put name="zoneInit" --%>
<%
	//Author : FHA	

	idEcran="PRF0054";	
	
	RFOrdresVersementsViewBean viewBean = (RFOrdresVersementsViewBean) request.getAttribute("viewBean");
	//globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	String idPrestation = request.getParameter("idPrestation");
	if (idPrestation == null) {
		idPrestation = "";
	}
	String idDecision = request.getParameter("idDecision");
	if (idDecision == null) {
		idDecision = "";
	}
	if(JadeStringUtil.isIntegerEmpty(idDecision)){
		idDecision = request.getParameter("selectedId");
	}

	bButtonNew = false;
	bButtonFind = false;
	IFrameDetailHeight = "330";
	IFrameListHeight = "200";
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.globall.db.BSession"%>
<SCRIPT language="javascript">

	bFind = true;
	detailLink = "<%=actionNew%>";	
	
	usrAction = "<%=globaz.cygnus.servlet.IRFActions.ACTION_ORDRES_VERSEMENTS%>.lister";

	function rechargerPage() {
		document.forms[0].elements("userAction").value = "<%=globaz.cygnus.servlet.IRFActions.ACTION_ORDRES_VERSEMENTS%>.chercher";
		document.forms[0].target = "fr_main";
		document.forms[0].submit();
	}

	function onClickNew() {		
	}

	function loadFrames() {
		// prevenir les cursor state not valid exception
		if(bFind) {
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
	<%if(!JadeStringUtil.isBlank(idPrestation)){ %>
		<ct:FWLabel key="JSP_ORDRE_VERSEMENT_PRESTATION_TITRE"/>
	<%}else if(!JadeStringUtil.isBlank(idDecision)){%>
		<ct:FWLabel key="JSP_ORDRE_VERSEMENT_DECISION_TITRE"/>
	<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<%if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {%>
								
								<TR><TD colspan="6" style="color:red"><strong>
									<%=viewBean.getMessage()%>
								</strong></TD></TR>
								
							<%}%>

							<TR>
								<TD>
									<%if (!JadeStringUtil.isBlank(idPrestation)) { %>
										<ct:FWLabel key="JSP_ORDRE_VERSEMENT_PRESTATION_NO" />&nbsp;:&nbsp;
									<%} else { %>
										<ct:FWLabel key="JSP_ORDRE_VERSEMENT_DECISION" />&nbsp;:&nbsp;
									<%} %>
								</TD>
								<TD>
									<%if (!JadeStringUtil.isBlank(idPrestation)) { %>
										<b><%=idPrestation%></b>
										<INPUT type="hidden" name="forIdPrestation" value="<%=idPrestation%>">
									<%} else { %>
									<b><%=idDecision%></b>
										<input type="hidden" name="forIdDecision" value="<%=idDecision%>" />
									<%} %>
									
									<INPUT type="hidden" name="selectedId" value="<%=idPrestation%>">
									<INPUT type="hidden" name="montantPrestation" value="<%=viewBean.getMontantTotalPrestation()%>">
									<INPUT type="hidden" name="wantLevelField" value="true">
								</TD>
							</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>