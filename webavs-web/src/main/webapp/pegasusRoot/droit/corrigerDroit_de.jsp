<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.pegasus.business.services.models.pcaccordee.PCAccordeeService"%>
<%@page import="globaz.pegasus.vb.droit.PCCorrigerDroitViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DRO_D"
	idEcran="PPC0005";

	PCCorrigerDroitViewBean viewBean = (PCCorrigerDroitViewBean) session.getAttribute("viewBean");
	Droit droitCourant = viewBean.getDroit();
	PersonneEtendueComplexModel personne=droitCourant.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();

	autoShowErrorPopup = true;
	
	bButtonNew      = false;
	bButtonUpdate   = true;
	bButtonValidate = true;
	bButtonCancel 	= true;	
	bButtonDelete   = false;
	userActionDel="pegasus.droit.droit.supprimer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/jadeBaseFormulaire.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/corrigerDroit_de.js"/></script>
<%-- tpl:put name="zoneScripts" --%>

<style type="text/css">
#dialog-confirm-creation-lot{
	display:none;
}
</style>

<script type="text/javascript">
  var ACTION_DROIT="<%=IPCActions.ACTION_DROIT_CORRIGER%>";
  var url = ACTION_DROIT;
  globazGlobal.csMotifDeces = <%=IPCDroits.CS_MOTIF_DROIT_DECES%>;
  globazGlobal.idVersionDroit = <%=viewBean.getIdDroit()%>;
  globazGlobal.numVersionDroit = <%=viewBean.getDroit().getSimpleVersionDroit().getNoVersion()%>+1;
  globazGlobal.pcAccordeeServiceClassName = "<%=PCAccordeeService.class.getName()%>";
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_DROIT_CORRIGER_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colspan="6" align="center">
			<table width="90%">

					<tr>
					    <TD><ct:FWLabel key="JSP_PC_DRO_D_DETAIL_ASSURE"/></TD>
					    <TD>
						   <input type="hidden" name="idDemandePc" value="<%=	droitCourant.getDemande().getSimpleDemande().getIdDemande()%>" />
							<%=PCUserHelper.getDetailAssure(objSession,personne)%>
						</TD>
					</tr>
					<tr>
						<TD><ct:FWLabel key="JSP_PC_DRO_D_ETAT"/></TD>
						<TD>
						<%=objSession.getCodeLibelle(droitCourant.getSimpleVersionDroit().getCsEtatDroit())%>
					</TD>
					<tr><td style="border-bottom:2px solid #226194" colspan="2">&nbsp;</td><tr>
					<tr>
						<TD><ct:FWLabel key="JSP_PC_DRO_D_MOTIF"/></TD>
						<TD>
							<ct:FWCodeSelectTag codeType="<%=IPCDroits.CS_TYPE_MOTIF%>" name="csMotif" defaut="<%=viewBean.getCsMotif()%>"/>
						</TD>
					
					</tr>
					<tr>
						<TD><ct:FWLabel key="JSP_PC_DRO_D_DATE_ANNONCE"/></TD>
						<TD>
							<input type="text" data-g-calendar="mandatory:true" value="<%=viewBean.getDateAnnonce() %>" name="dateAnnonce" />
						</TD>
					</tr>
					<tr id="dateSuppressionTr">
						<TD><ct:FWLabel key="JSP_PC_DRO_D_DATE_SUPPRESSION"/></TD>
						<TD>
							<input type="text" data-g-calendar="mandatory:false,type:month" value="<%=viewBean.getDateSuppression() %>" name="dateSuppression" id="dateSuppression" />
						</TD>
					</tr>
					<tr id="dateDecisionTr">
						<TD><ct:FWLabel key="JSP_PC_PREP_DECISION_SUPPR_DE_DECDU"/></TD>
						<TD>
							<input type="text" data-g-calendar="mandatory:false" value="<%=viewBean.getDateDecision() %>" name="dateDecision" id="dateDecision"/>
						</TD>
					</tr>
					<tr id="montantRestitutionTr">
						<TD><ct:FWLabel key="JSP_PC_DRO_D_MONTANT_RESTITUTION"/></TD>
						<TD>
							<input type="text" value=" " name="montantRestitution" id="montantRestitution"  readonly="readonly" disabled="disabled" data-g-amount=" " />
						</TD>
					</tr>
			</TABLE>
			
			<!-- **************************** Confirmation que l'utilisateur veut bien créer un lot de décision de restitution -->
			<div id="dialog-confirm-creation-lot" title="<%= objSession.getLabel("JSP_PC_DECALCUL_D_CONFIRMATION_COMPTA_AUTO_TITRE")%>">
						
    			<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span><%= objSession.getLabel("JSP_PC_DECALCUL_D_CONFIRMATION_COMPTA_AUTO")%></p>
<!--     				<p> -->
<%--     					<%= objSession.getLabel("JSP_VALID_LOT_D_EMAIL")%> --%>
<%-- 						<INPUT type="text" name="mailProcessCompta" value="<%=viewBean.getMailProcessCompta()%>" class="libelleLong"> --%>
<!-- 					</p> -->
			</div>
			
			<input type="hidden" name="isComptabilisationAuto" id="isComptabilisationAuto" value="<%=viewBean.isComptabilisationAuto()%>"/>
			<INPUT type="hidden" name="mailProcessCompta" id="mailProcessCompta" value="<%=viewBean.getMailProcessCompta()%>"/>
		</TD>
	</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>