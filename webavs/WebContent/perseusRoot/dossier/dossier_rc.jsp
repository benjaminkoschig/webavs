<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="ch.globaz.perseus.web.servlet.PFNSSDTO"%>
<%@page import="globaz.perseus.utils.PFNssHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.commons.nss.NSSTag"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatLot"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeLot"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="ch.globaz.perseus.business.models.lot.Lot"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%@page import="java.util.Vector"%>
<%
	idEcran="PPF0512";
	//Les labels de cette page commence par le préfix "JSP_PF_DOS_R"
	
	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PF_DOS_R_TRIER_PAR_NOM")});
	rememberSearchCriterias = false;
	
%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>

<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu" />
<ct:menuChange displayId="options" menuId="perseus-optionsempty" />

<script type="text/javascript">

	bFind = true;
	usrAction = "perseus.dossier.dossier.lister";
	actionNew =  "<%=servletContext + mainServletPath + "?userAction=perseus.dossier.dossier.afficher&_method=add"%>";
	
	$(function(){
	
	});
	
	function clearFields () {
		$('.clearable,#searchModel\\.forCsSexe,[name=searchModel\\.likeNss],#partialsearchModel\\.likeNss,#searchModel\\.forIdGestionnaire').val('');
		$('#partialsearchModel\\.likeNss').focus();
	}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DOS_R_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						<tr><td>
	<TABLE border="0" cellspacing="5" cellpadding="0" width="100%">
		<TR>
			<TD>
			<LABEL for="searchModel.likeNss"><ct:FWLabel key="JSP_PF_DOS_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
			<TD>
			
			<!-- ///////  -->
			<%
				String nss = ""; 
				if(null != PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
						  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof PFNSSDTO &&
						  !JadeStringUtil.isBlankOrZero(NSUtil.formatWithoutPrefixe(((PFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), 
									((PFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false))){
						
						nss =  NSUtil.formatWithoutPrefixe(((PFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), 
							((PFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false);
					}else{
							nss = PFNssHelper.getNssLike(objSession);
					}%>

				<nss:nssPopup avsMinNbrDigit="99" 
					  nssMinNbrDigit="99"														   
					  name="searchModel.likeNss" value="<%= nss %>" />
					  
			<!-- ///////  -->
			
				<!--<nss:nssPopup avsMinNbrDigit="99"
						  nssMinNbrDigit="99"
						  newnss=""
						  name="searchModel.likeNss" value="<%=PFNssHelper.getNssLike(objSession) %>" />-->
			</TD>
			<TD><LABEL for="searchModel.likeNom"><ct:FWLabel key="JSP_PF_DOS_R_NOM"/></LABEL>&nbsp;</TD>
			<TD><INPUT type="text" name="searchModel.likeNom" class="clearable" value=""></TD>
			<TD><LABEL for="searchModel.likePrenom"><ct:FWLabel key="JSP_PF_DOS_R_PRENOM"/></LABEL>&nbsp;</TD>
			<TD><INPUT type="text" name="searchModel.likePrenom" class="clearable" value=""></TD>
		</TR>
		<TR>
			<TD><LABEL for="searchModel.forIdGestionnaire"><ct:FWLabel key="JSP_PF_DOS_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
			<TD><ct:FWListSelectTag data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="searchModel.forIdGestionnaire"/></TD>
			<TD><LABEL for="searchModel.forDateNaissance"><ct:FWLabel key="JSP_PF_DOS_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
			<TD><input type="text" name="searchModel.forDateNaissance" class="clearable" value="" data-g-calendar="mandatory:false"/></TD>
			<TD><LABEL for="searchModel.forCsSexe"><ct:FWLabel key="JSP_PF_DOS_R_SEXE"/></LABEL>&nbsp;</TD>	
			<TD><ct:FWCodeSelectTag name="searchModel.forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
		</TR>
		<TR>
			<TD colspan="2">&nbsp;</TD>
			<TD><LABEL for="searchModel.betweenDateRevisionDebut"><ct:FWLabel key="JSP_PF_DOS_R_REVISER_ENTRE"/></LABEL></TD>
			<TD><input type="text" name="searchModel.betweenDateRevisionDebut" class="clearable" value="" data-g-calendar="type:month"/></TD>
			<TD><LABEL for="searchModel.betweenDateRevisionFin"><ct:FWLabel key="JSP_PF_DOS_R_REVISER_ET"/></LABEL></TD>
			<TD><input type="text" name="searchModel.betweenDateRevisionFin" class="clearable" value="" data-g-calendar="type:month" /></TD>
		</TR>
		<TR>
			<TD colspan="6">&nbsp;</TD>
		</TR>
		<TR>
			<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
			<TD colspan="3">&nbsp;</TD>										
			<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
			<TD><ct:FWListSelectTag data="<%=orderList%>" defaut="" name="searchModel.orderBy" /></TD>
		</TR>
	</TABLE>
						</td></tr>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
