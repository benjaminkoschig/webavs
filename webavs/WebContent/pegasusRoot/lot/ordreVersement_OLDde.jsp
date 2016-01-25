<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.lot.PCOrdreVersementViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_LOT_ORDRE_VERSMENT_D"
	
	idEcran="PPC0095";

	PCOrdreVersementViewBean viewBean = (PCOrdreVersementViewBean) session.getAttribute("viewBean");	

	
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
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
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
				<TD><label><b><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_BENEFICIAIRE"/></b></label></TD>
				<TD>
					<span> <%=viewBean.getBeneficiaire()%> </span>
				</TD>
				<TD><label><b><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_TYPE"/></b></label></TD>
				<td><%=objSession.getCodeLibelle(viewBean.getOrdreVersement().getSimpleOrdreVersement().getCsType()) %></td>
			</TR>
			<TR><TD colspan="4">&nbsp;</TD></TR>
			<TR>	
				<TD><label><b><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_ADRESSE_PAIEMENT"/></b></label></TD>
				<TD>
					<PRE><span class="adressePre"> <%=viewBean.getAdressePaiement()%></span></PRE>
				</TD>
				<td><label><b><ct:FWLabel key="JSP_LOT_ORDRE_VERSEMENT_D_MONTANT"/></b></label></td>	
				<td><span data-g-amountformater=" " > <%=JadeStringUtil.toNotNullString(viewBean.getOrdreVersement().getSimpleOrdreVersement().getMontant())%></span></td>
			</TR>
			
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>