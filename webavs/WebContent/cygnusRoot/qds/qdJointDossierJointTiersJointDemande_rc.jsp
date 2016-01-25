<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.qds.RFQdJointDossierJointTiersJointDemandeViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.RFNSSDTO"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PRF0011";
	//Les labels de cette page commence par le préfix "JSP_RF_QD_R"

 	RFQdJointDossierJointTiersJointDemandeViewBean viewBean = (RFQdJointDossierJointTiersJointDemandeViewBean) request.getAttribute("viewBean");
	
 	rememberSearchCriterias = true;
 	
 	bButtonNew=false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu"/>
<SCRIPT language="JavaScript">

	bFind = false;

	var warningObj = new Object();
	warningObj.text = "<LABEL for='error'><ct:FWLabel key='JSP_RF_QD_NSS_VIDE'/></LABEL>";

	usrAction = "<%=IRFActions.ACTION_QD_JOINT_DOSSIER_JOINT_TIERS_JOINT_DEMANDE%>.lister";
	

	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value= "";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("forDateNaissance")[0].value="";
		document.getElementsByName("forCsSexe")[0].value="";

		document.getElementsByName("forIdGestionnaire")[0].value="<%=viewBean.getIdGestionnaire()%>";
		
		//document.getElementsByName("orderBy")[0].value="ENDAQD,HTLDE1,HTLDE2,YADDEB DESC";
		
		document.forms[0].elements('partiallikeNumeroAVS').focus();
	}
	
	function likeNomChange(){
		if(document.getElementsByName("likeNom")[0].value!=""){
			//document.getElementsByName("orderBy")[0].value="ENDAQD,HTLDE1,HTLDE2,YADDEB DESC";
		}
	}
	
	function likePrenomChange(){
		if(document.getElementsByName("likePrenom")[0].value!=""){
			//document.getElementsByName("orderBy")[0].value="ENDAQD,HTLDE1,HTLDE2,YADDEB DESC";		
		}
	}

	function newQd(){
		document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD%>"+".afficher";
  		document.location.href="cygnus?userAction="+"<%=IRFActions.ACTION_SAISIE_QD%>"+".afficher&_method=add";
	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_QD_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">									
									<TR>
										<TD><LABEL for="forIdGestionnaire"><ct:FWLabel key="JSP_RF_QD_R_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
										<TD colspan="5"><ct:FWListSelectTag data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="<%--<viewBean.getSession().getUserId()%>--%>" name="forIdGestionnaire"/></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_RF_QD_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
																				<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
									  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof RFNSSDTO) {%>										
										<TD>
											<ct1:nssPopup avsMinNbrDigit="99"
													  nssMinNbrDigit="99"
													  newnss="<%=viewBean.isNNSS(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())%>"
													  name="likeNumeroAVS"													  
													  onChange=""
													  value="<%=NSUtil.formatWithoutPrefixe(((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), ((RFNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false)%>"/>
										</TD>
										<%}else{ %>
										<TD>
											<ct1:nssPopup avsMinNbrDigit="99"
													  nssMinNbrDigit="99"
													  name="likeNumeroAVS"
													  value=""
													  onChange=""/>
										</TD>
										<%} %>

										<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_RF_QD_R_NOM"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="likeNom" value="" onchange="likeNomChange();"></TD>
										<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_RF_QD_R_PRENOM"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="likePrenom" onchange="likePrenomChange();" value=""></TD>
									</TR>
									<TR>
										<TD><LABEL for="forNumeroDecision"><ct:FWLabel key="JSP_RF_QD_R_NUMERO_DECISION"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="forNumeroDecision" value=""/></TD>
										<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_RF_QD_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
										<TD><input data-g-calendar=" "  name="forDateNaissance" value=""/></TD>
										<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_RF_QD_R_SEXE"/></LABEL>&nbsp;</TD>	
										<TD><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										
										<TD><LABEL for="forAnneeQd"><ct:FWLabel key="JSP_RF_QD_R_ANNEE_QD"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="forAnneeQd" value=""/></TD>
										<TD><LABEL for="forIdDemande"><ct:FWLabel key="JSP_RF_QD_R_NUMERO_DEMANDE"/></LABEL>&nbsp;</TD>
										<TD><INPUT type="text" name="forIdDemande" value=""></TD>
										<TD><ct:FWLabel key="JSP_RF_QD_R_TYPE_PC"/>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=viewBean.getCsTypePcData(true)%>" defaut="" name="forCsTypePcData" /></TD>
									</TR>
									<TR>
										<TD colspan="2">&nbsp;</TD>
										<TD><LABEL for="forIdDossier"><ct:FWLabel key="JSP_RF_QD_R_NUMERO_DOSSIER"/></LABEL>&nbsp;</TD>
										<TD colspan="1"><INPUT type="text" name="forIdDossier" value=""></TD>
										
										<TD><LABEL for="forIdQd"><ct:FWLabel key="JSP_RF_QD_R_ID_QD"/></LABEL>&nbsp;</TD>
										<TD ><INPUT type="text" name="forIdQd" value=""></TD>
										
										
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><LABEL for="forCsEtatQd"><ct:FWLabel key="JSP_RF_QD_R_ETAT"/></LABEL>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=viewBean.getCsEtatQdData(true)%>" defaut="" name="forCsEtatQd" /></TD>
										<TD><ct:FWLabel key="JSP_RF_QD_R_GENRE_QD"/>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<%=viewBean.getCsGenreQdData(true)%>" defaut="" name="forCsGenreQd" /></TD>
										<%-- <TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
										<TD><ct:FWListSelectTag data="<viewBean.getOrderByData()>" defaut="" name="forOrderBy" /></TD> --%>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>		
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<ct:ifhasright element="<%=IRFActions.ACTION_SAISIE_QD%>" crud="c">					
					<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="newQd()">
				</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>