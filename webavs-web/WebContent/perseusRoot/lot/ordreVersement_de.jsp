<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.perseus.vb.lot.PFOrdreVersementViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_LOT_ORDRE_VERSMENT_D"
	
	idEcran="PPF0741";

	PFOrdreVersementViewBean viewBean = (PFOrdreVersementViewBean) session.getAttribute("viewBean");	

	
	String idTierRequerant 		= request.getParameter("idTierRequerant"); 
	String idPrestation 		= request.getParameter("idPrestation");
	String montantPrestation	= request.getParameter("montantPrestation");	
	String idDecision			= request.getParameter("idDecision");
	
	bButtonNew 		= false;
	bButtonDelete 	= false;
	bButtonCancel   = false;
	bButtonUpdate   = false;
	bButtonValidate = false;
/*
	bButtonUpdate =;
	bButtonValidate = bButtonValidate ;
	bButtonCancel = bButtonCancel && ;
	
	*/
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_OVE_D_TITRE"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
			<%-- tpl:put name="zoneMain" --%>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			
			<TR>
				<TD><label><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_D_BENEFICIAIRE"/></label></TD>
				<TD>
					<span> <%=viewBean.getBeneficiaire()%> </span>
				</TD>
				<TD><label><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_D_TYPE"/></label></TD>
				<td><%=objSession.getCodeLibelle(viewBean.getOrdreVersement().getSimpleOrdreVersement().getCsTypeVersement()) %></td>
			</TR>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			<TR>	
				<TD><label><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_D_ADRESSE_PAIEMENT"/></label></TD>
				<TD>
					<PRE><span class="IJAfficheText"> <%=viewBean.getAdressePaiement()%></span></PRE>
				</TD>
				<td><label><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_D_MONTANT"/></label></td>	
				<td><span data-g-amountformater=" " > <%=JadeStringUtil.toNotNullString(viewBean.getOrdreVersement().getSimpleOrdreVersement().getMontantVersement())%></span></td>
			</TR>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			<TR>
				<TD><label><ct:FWLabel key="JSP_PF_LOT_ORDRE_VERSEMENT_D_NUMERO_REFERENCE"/></label></TD>
				<TD>
					<span> <%=viewBean.getOrdreVersement().getSimpleOrdreVersement().getNumFacture()%> </span>
				</TD>
				<TD colspan="2"></TD>
			</TR>
		
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>