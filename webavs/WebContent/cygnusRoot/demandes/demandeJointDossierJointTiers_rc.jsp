<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.demandes.RFDemandeJointDossierJointTiersViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.RFNSSDTO"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PRF0003";
	//Les labels de cette page commence par le préfix "JSP_RF_DEM_R"

 	RFDemandeJointDossierJointTiersViewBean viewBean = (RFDemandeJointDossierJointTiersViewBean) request.getAttribute("viewBean");
	
 	rememberSearchCriterias = true;
 	bButtonNew=false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- <%@ include file="../utils/typeSousTypeDeSoinsListe.jspf"> --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu"/>
<SCRIPT language="JavaScript">

	bFind = false;
	usrAction = "<%=IRFActions.ACTION_DEMANDE_JOINT_DOSSIER_JOINT_TIERS%>.lister";

	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value= "";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("forDateNaissance")[0].value="";
		document.getElementsByName("forCsSexe")[0].value="";
		document.getElementsByName("forIdDecision")[0].value="";
		document.getElementsByName("forNumeroDecision")[0].value="";
		document.getElementsByName("forCsEtatDemande")[0].value="";
		document.getElementsByName("fromDateDebut")[0].value="";
		document.getElementsByName("fromDateFin")[0].value="";
		document.getElementsByName("forNumeroDecision")[0].value="";
		document.getElementsByName("forCsStatutDemande")[0].value="";
		document.getElementsByName("codeTypeDeSoin")[0].value="";
		document.getElementsByName("codeSousTypeDeSoin")[0].value="";
		document.getElementsByName("codeTypeDeSoinList")[0].value="";
		document.getElementsByName("codeSousTypeDeSoinList")[0].value="";
		
		//document.getElementsByName("forIdGestionnaire")[0].value="<=viewBean.getIdGestionnaire()>";
		
		document.getElementsByName("orderBy")[0].value="HTLDE1,HTLDE2,YADDEB DESC";
		
		document.forms[0].elements('partiallikeNumeroAVS').focus();
		
	}
	
	function likeNomChange(){
		if(document.getElementsByName("likeNom")[0].value!=""){
			document.getElementsByName("orderBy")[0].value="HTLDE1,HTLDE2,YADDEB DESC";
		}
	}
	
	function likePrenomChange(){
		if(document.getElementsByName("likePrenom")[0].value!=""){
			document.getElementsByName("orderBy")[0].value="HTLDE1,HTLDE2,YADDEB DESC";		
		}
	}

	function newDemande(){
		document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_DEMANDE%>"+".afficher";
  		document.location.href="cygnus?userAction="+"<%=IRFActions.ACTION_SAISIE_DEMANDE%>"+".afficher&_method=add&isAfficherDetail=false";
  		document.forms[0].elements('_method').value = ADD;
	}

	function postInit(){
		document.getElementsByName("codeTypeDeSoin")[0].value="";
		document.getElementsByName("codeSousTypeDeSoin")[0].value="";
		document.getElementsByName("codeTypeDeSoinList")[0].value="";
		document.getElementsByName("codeSousTypeDeSoinList")[0].value="";
	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_DEM_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
									<TR>
										<TD><LABEL for="forIdGestionnaire"><ct:FWLabel key="JSP_RF_DEM_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
										<TD colspan="5"><ct:FWListSelectTag data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" 
										    defaut='<%=(JadeStringUtil.isBlankOrZero(viewBean.getIdDecision()) && JadeStringUtil.isBlankOrZero(viewBean.getNumeroDecision()))?viewBean.getSession().getUserId():""%>' name="forIdGestionnaire"/></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_RF_DEM_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
										<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
									       PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RFNSSDTO) {%>										
										<TD>
											<ct1:nssPopup avsMinNbrDigit="99"
													  nssMinNbrDigit="99"
													  newnss="<%=viewBean.isNNSS(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())%>"
													  name="likeNumeroAVS"													  
													  onChange="isRechercheFamille();"
													  value="<%=NSUtil.formatWithoutPrefixe(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), ((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false)%>"/>
										</TD>
										<%}else{ %>
										<TD>
											<ct1:nssPopup avsMinNbrDigit="99"
													  nssMinNbrDigit="99"
													  name="likeNumeroAVS"
													  value=""
													  onChange="isRechercheFamille();"/>
										</TD>
										<%} %>

										<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_RF_DEM_R_NOM"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="likeNom" value="" onchange="likeNomChange();"></TD>
										<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_RF_DEM_R_PRENOM"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="likePrenom" onchange="likePrenomChange();" value=""></TD>
									</TR>
									<TR>
										<TD colspan="2">&nbsp;</TD>
										<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_RF_DEM_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
										<TD><input data-g-calendar=" "  name="forDateNaissance" value=""/></TD>
										<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_RF_DEM_R_SEXE"/></LABEL>&nbsp;</TD>	
										<TD><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
									</TR>
									<TR>
										<TD><LABEL for="forNumeroDecision"><ct:FWLabel key="JSP_RF_DEM_R_NUMERO_DECISION"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="forNumeroDecision" value="<%=viewBean.getNumeroDecision()%>"></TD>
										<TD><LABEL for="forIdDecision"><ct:FWLabel key="JSP_RF_DEM_R_ID_DECISION"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="forIdDecision" value="<%=viewBean.getIdDecision()%>"></TD>
										<TD colspan="2">&nbsp;</TD>
									</TR>
									<TR>
										<TD><LABEL for="fromDateDebut"><ct:FWLabel key="JSP_RF_DEM_R_A_PARTIR_DU"/></LABEL>&nbsp;</TD>
										<TD><input data-g-calendar=" "  name="fromDateDebut" value=""/></TD>
										<TD><LABEL for="fromDateFin"><ct:FWLabel key="JSP_RF_DEM_R_JUSQU_AU"/></LABEL>&nbsp;</TD>
										<TD><input data-g-calendar=" "  name="fromDateFin" value=""/></TD>
										<!-- <TD><LABEL for="forDevis"><ct:FWLabel key="JSP_RF_DEM_R_DEVIS"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="checkbox" name="forDevis"></TD> -->
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
									<TR><TD colspan="6"><INPUT type="hidden" name="isSaisieDemande" value="false"/></TD></TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="forCsEtatDemande"><ct:FWLabel key="JSP_RF_DEM_R_ETAT"/></LABEL>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=viewBean.getCsEtatDemandeData(true)%>" defaut="" name="forCsEtatDemande" /></TD>
										<TD><ct:FWLabel key="JSP_RF_DEM_R_STATUT"/>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=viewBean.getCsStatutDemandeData(true)%>" defaut="" name="forCsStatutDemande" /></TD>
										<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="forOrderBy" /></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD colspan="6"><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>		
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:ifhasright element="<%=IRFActions.ACTION_SAISIE_DEMANDE%>" crud="c">
					<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="newDemande()">
				</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>