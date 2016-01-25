<%@page import="globaz.perseus.utils.PFNssHelper"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%@page import="globaz.perseus.vb.demande.PFDemandeViewBean"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>

<%
	idEcran="PPF0312";
	//Les labels de cette page commence par le préfix "JSP_PF_DEM_R"

	Vector orderList=new Vector(2);
	orderList.add(new String[]{"nomPrenom",objSession.getLabel("JSP_PF_DOS_R_TRIER_PAR_NOM")});	
	rememberSearchCriterias = false;
	
	String idDossier = request.getParameter("idDossier");
	if (JadeStringUtil.isNull(idDossier)){
		idDossier="";
		bButtonNew=false;
	}else{
		//bButtonNew = !SimpleDemandeChecker.existeDemandeInVlalidEtatWithOutException(idDossier);
		actionNew =  servletContext + mainServletPath + "?userAction=" + IPFActions.ACTION_DEMANDE + ".afficher&_method=add&idDossier="+idDossier;
	}	
	
	
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu" />
<ct:menuChange displayId="options" menuId="perseus-optionsempty" />

<SCRIPT language="JavaScript">
	bFind = true;
	usrAction = "perseus.demande.demande.lister";
	
	function clearFields () {
		$('.clearable,#searchModel\\.forCsSexe,[name=searchModel\\.likeNss],#partialsearchModel\\.likeNss,#searchModel\\.forCsEtatDemande,#searchModel\\.forCsTypeDemande,#searchModel\\.forIdGestionnaire').val('');
		$('#partialsearchModel\\.likeNss').focus();
	}
	
	$(function(){
		<%if(JadeStringUtil.isEmpty(idDossier)){%>
		// hide forIdDossier field and fill cell with blank
		$('#forIdDossier,[for=forIdDossier]').hide().after('&nbsp;');
		<%}%>
		
	})
		
</SCRIPT>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_DEM_R_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
	<TR>
		<TD>
	<TABLE border="0" cellspacing="5" cellpadding="0" width="100%">
				<TR>
					<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_PF_DEM_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="searchModel.hasPostitField" id="hasPostitField" value="true"></TD>
					<TD>
						<nss:nssPopup avsMinNbrDigit="99"
								  nssMinNbrDigit="99"
								  newnss=""
								  name="searchModel.likeNss" value="<%=PFNssHelper.getNssLike(objSession) %>" />
					</TD>
					<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_PF_DEM_R_NOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likeNom" name="searchModel.likeNom" class="clearable" value=""></TD>
					<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PF_DEM_R_PRENOM"/></LABEL>&nbsp;</TD>
					<TD><INPUT type="text" id="likePrenom" name="searchModel.likePrenom" class="clearable" value=""></TD>
				</TR>
				<TR>
					<TD><LABEL for="forIdDossier"><ct:FWLabel key="JSP_PF_DEM_R_NO_DOSSIER"/></LABEL></TD>
					<TD><INPUT type="text" name="searchModel.forIdDossier" id="forIdDossier" value="<%=idDossier%>" class="disabled" readonly tabindex="-1"></TD>
					<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_PF_DEM_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
					<TD><input type="text" id="forDateNaissance" name="searchModel.forDateNaissance" class="clearable" value="" data-g-calendar="mandatory:false"/></TD>
					<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PF_DEM_R_SEXE"/></LABEL>&nbsp;</TD>	
					<TD><ct:FWCodeSelectTag name="searchModel.forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
				</TR>
				<TR>
					<TD><LABEL for="forIdGestionnaireDemande"><ct:FWLabel key="JSP_PF_DEM_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
					<TD><ct:FWListSelectTag data="<%=PFGestionnaireHelper.getResponsableData(objSession)%>" defaut="" name="searchModel.forIdGestionnaire"/></TD>
					<TD><LABEL for="forCsEtatDemande"><ct:FWLabel key="JSP_PF_DEM_R_ETAT_DEMANDE"/></LABEL>&nbsp;</TD>
					<TD><ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_ETAT_DEMANDE%>" name="searchModel.forCsEtatDemande" wantBlank="true" defaut="blank"/></TD>
					<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_PF_DEM_R_TYPE_DEMANDE"/></LABEL>&nbsp;</TD>	
					<TD><ct:FWCodeSelectTag codeType="<%=IPFConstantes.CSGROUP_TYPE_DEMANDE%>" name="searchModel.forCsTypeDemande" wantBlank="true" defaut="blank"/></TD>
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
		</TD>
	</TR>							

	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
