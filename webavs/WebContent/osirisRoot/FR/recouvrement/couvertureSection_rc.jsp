<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.db.access.recouvrement.*,globaz.osiris.db.recouvrement.*"%>
<%
	idEcran="GCA60001";
	String tmp = request.getParameter("userAction");
	CAPlanRecouvrementViewBean viewBean = (CAPlanRecouvrementViewBean) request.getAttribute("viewBean");

	actionNew += "&idCompteAnnexe="+viewBean.getCompteAnnexe().getIdCompteAnnexe();

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
	bButtonNew = objSession.hasRight(userActionNew, globaz.framework.secure.FWSecureConstants.ADD) && !viewBean.getIdEtat().equals(CAPlanRecouvrement.CS_SOLDE);

	String detailLink = "osiris?userAction=osiris.recouvrement.couvertureSection.afficher&selectedId=-1";
	String idCouvertureSection = request.getParameter("idCouvertureSection");
	if (idCouvertureSection != null && idCouvertureSection.length() > 0) {
		detailLink = "osiris?userAction=osiris.recouvrement.couvertureSection.afficher&selectedId=" + idCouvertureSection;
	}

%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-PlanRecouvrement" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPlanRecouvrement()%>" checkAdd="no"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPlanRecouvrement()%>" checkAdd="no"/>
</ct:menuChange>

<SCRIPT language="JavaScript">
	var usrAction = "osiris.recouvrement.couvertureSection.lister";
	bFind = true;

	detailLink = "<%=detailLink%>";

	function onClickNew(){
	}

	function onClickFind(){}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Sections couvertes<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<INPUT type="hidden" name="forIdPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>">
						<INPUT type="hidden" name="order" value="<%=CACouvertureSectionViewBean.FIELD_NUMEROORDRE+","+CACouvertureSectionViewBean.FIELD_IDCOUVERTURESECTION%>">
						<TR>
							<TD class="label"><a href="osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&selectedId=<%=viewBean.getIdPlanRecouvrement() %>">Plan</a></TD>
							<TD class="control"><INPUT type="text" value="<%=viewBean.getIdPlanRecouvrement() + " " + viewBean.getLibelle()%>" class="disabled" readonly></TD>
							<TD class="label">Date</TD>
							<TD class="control"><INPUT type="text" value="<%=viewBean.getDate()%>" class="dateDisabled" readonly></TD>
							<TD>&nbsp;</TD>
							<TD>Solde du plan</TD>
							<TD>&nbsp;</TD>
							<TD>
								<INPUT type="text" value="<%=viewBean.getCumulSoldeSectionsFormate()%>" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD class="label">Compte Annexe</TD>
							<TD>
								<INPUT type="text" value="<%=viewBean.getCompteAnnexe().getIdExterneRole() %>"  width="<%=(viewBean.getCompteAnnexe().getIdExterneRole().length()+5)%>" class="disabled" readonly>
							</TD>
							<TD colspan=2>
								<INPUT type="text" value="<%=viewBean.getCompteAnnexe().getDescription() %>"  width="<%=viewBean.getCompteAnnexe().getDescription().length() %>" class="disabled" readonly>
							</TD>
							<TD>&nbsp;</TD>
							<TD class="label">Etat</TD>
							<TD>&nbsp;</TD>
							<TD>
								<INPUT type="text" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getIdEtat()) %>" class="disabled" readonly>
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