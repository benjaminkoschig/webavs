<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.lot.PFOrdreVersementViewBean"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.globall.db.BSession"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix ""

	idEcran="PPF0742";	
	
	PFOrdreVersementViewBean viewBean = (PFOrdreVersementViewBean) request.getAttribute("viewBean");
	FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	BSession bsession = (BSession)controller.getSession();
	
	String idTierRequerant 		= request.getParameter("idTierRequerant");
	String idTierBenef			= request.getParameter("idTierBeneficiaire");
	String idPrestation 		= request.getParameter("selectedId");
	String idDecision   		= request.getParameter("idDecision");
	
	//String idDroit 
	if(JadeStringUtil.isIntegerEmpty(idDecision)){
		idDecision = request.getParameter("selectedId");
	}
	
	String montantPrestation	= request.getParameter("montantPrestation");

	bButtonNew = false;
	bButtonFind = false;
	IFrameDetailHeight = "330";
	IFrameListHeight = "200";
	
	//String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<SCRIPT language="javascript">

	bFind = true;
	detailLink = "<%=actionNew%>";	
	usrAction = "perseus.lot.ordreVersement.lister";

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_R_TYPE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<TR>
								<TD><b><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_R_BENEFICIAIRE"/></b></TD>
								<TD colspan="5">
				 					<input type="hidden" name="ordreVersementSearch.forIdPrestation" value="<%=idPrestation%>">
								</TD>
							</TR>
							<TR><TD colspan="6">&nbsp;</TD></TR>
							<TR>
								<TD><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_R_TIERS"/></TD>
								<TD colspan="5">
								   	<re:PRDisplayRequerantInfoTag 
									 session="<%=bsession%>" 
									 idTiers="<%=idTierRequerant%>"
									 style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
								</TD>
							</TR>
							<TR><TD colspan="6">&nbsp;</TD></TR>
							<TR>
								<TD><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_R_NO"/></TD>
								<TD>
									<INPUT type="text" name="forIdPrestation" value="<%=idPrestation%>" class="disabled" readonly>
								</TD>
								<TD><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_R_MONTANT"/></TD>
								<TD><INPUT type="text" name="montant" value="<%=new globaz.framework.util.FWCurrency(montantPrestation).toStringFormat()%>" class="montantDisabled" readonly></TD>
								<TD colspan="2">&nbsp;</TD>
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