<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.db.recouvrement.*"%>
<%
	idEcran="GCA60003";
	rememberSearchCriterias = true;
	CAPlanRecouvrementViewBean viewBean = (CAPlanRecouvrementViewBean) request.getAttribute("viewBean");

	String compteAnnexeTitulaireEntete = "";
	String compteAnnexeRoleDateDebutDateFin = "";
	String compteAnnexeSoldeFormate = "";

	try {
		compteAnnexeTitulaireEntete = viewBean.getCompteAnnexe().getTitulaireEntete();
		compteAnnexeRoleDateDebutDateFin = viewBean.getCompteAnnexe().getRole().getDateDebutDateFin(viewBean.getCompteAnnexe().getIdExterneRole());
		compteAnnexeSoldeFormate = viewBean.getCompteAnnexe().getSoldeFormate();
	} catch (Exception e) {
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-PlanRecouvrement" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
</ct:menuChange>

<SCRIPT language="JavaScript">
	var usrAction = "osiris.recouvrement.echeancePlan.lister";
	bFind = true;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Echéances<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<INPUT type="hidden" name="forIdPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>">
						<TR>
							<TD class="label"><A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getIdCompteAnnexe()%>">Compte</A></TD>
							<TD class="control" colspan="3" rowspan="2"><TEXTAREA rows="4" class="disabled" readonly><%=compteAnnexeTitulaireEntete%></TEXTAREA></TD>
							<TD class="label">Période</TD>
							<TD class="control"><INPUT type="text" value="<%=compteAnnexeRoleDateDebutDateFin%>" class="libelleLongDisabled" readonly></TD>
						</TR>
						<TR>
							<TD>&nbsp;</TD>
							<TD class="label">Solde compte</TD>
							<TD class="control"><INPUT type="text" value="<%=compteAnnexeSoldeFormate%>" class="montantDisabled" readonly></TD>
						</TR>
						<TR>
							<TD class="label"><a href="osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&selectedId=<%=viewBean.getIdPlanRecouvrement() %>">Plan</a></TD>
							<TD class="control"><INPUT type="text" value="<%=viewBean.getIdPlanRecouvrement() + " " + viewBean.getLibelle()%>" class="disabled" readonly></TD>
							<TD class="label">Date</TD>
							<TD class="control"><INPUT type="text" value="<%=viewBean.getDate()%>" class="dateDisabled" readonly></TD>
							<TD class="label">Solde du plan</TD>
							<TD class="control"><INPUT type="text" value="<%=viewBean.getSoldeResiduelPlanFormate()%>" class="montantDisabled" readonly></TD>
						</TR>
						<input type=hidden name=order value="DATEEXIGIBILITE">
						<%if(viewBean.getMsgType().equals(globaz.framework.bean.FWViewBeanInterface.WARNING)){
							viewBean.setMsgType(globaz.framework.bean.FWViewBeanInterface.OK);

						%>
						<TR>
							<TD colspan=6><%=viewBean.getMessage()%></TD>
						</TR>
						<%
							viewBean.setMessage("");
						}%>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if(!viewBean.hasEcheances() && viewBean.hasEcheanceAuto() && objSession.hasRight("osiris.recouvrement.echeancePlan.calculer", FWSecureConstants.UPDATE)){%>
					<input id="eButton" type="button" value="Calculer les échéances" onClick="fr_list.location.href='<%=servletContext + mainServletPath %>?userAction=osiris.recouvrement.echeancePlan.calculer&mode=preview&selectedId=<%=viewBean.getIdPlanRecouvrement()%>';">
				<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<script type="text/javascript"><!--
<%
if(request.getParameter("message")!=null){%>
	var errorObj = new Object();
	errorObj.text = "<%=request.getParameter("message")%>";
	showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
<%}%>
//-->
</script>
<%-- /tpl:insert --%>