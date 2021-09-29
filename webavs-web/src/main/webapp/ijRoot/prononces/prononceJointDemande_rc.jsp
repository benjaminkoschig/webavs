<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.pyxis.util.CommonNSSFormater"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@ page import="globaz.commons.nss.*"%>
		<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.ij.vb.prononces.IJPrononceParametresRCDTO"%>
<%@page import="globaz.ij.utils.IJGestionnaireHelper"%>
<%@page import="globaz.ij.vb.prononces.IJNSSDTO"%>
<%@page import="globaz.ij.servlet.IIJActions"%>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%

idEcran="PIJ0000";
bButtonNew = false;

rememberSearchCriterias = true;

// le viewBean doit avoir été créé puis sauvé dans la session par l'action custom ijPrononceJointDemandeAction
globaz.ij.vb.prononces.IJPrononceJointDemandeViewBean viewBean = (globaz.ij.vb.prononces.IJPrononceJointDemandeViewBean) request.getAttribute("viewBean");

String actionNewGrandeIJ 		 =  servletContext + mainServletPath + "?userAction=ij.prononces.requerant.afficher&csTypeIJ="+globaz.ij.api.prononces.IIJPrononce.CS_GRANDE_IJ+"&_method=add";
String actionNewPetiteIJ 		 =  servletContext + mainServletPath + "?userAction=ij.prononces.requerant.afficher&csTypeIJ="+globaz.ij.api.prononces.IIJPrononce.CS_PETITE_IJ+"&_method=add";
String actionNewPrononceAit 	 =  servletContext + mainServletPath + "?userAction=ij.prononces.requerant.afficher&csTypeIJ="+globaz.ij.api.prononces.IIJPrononce.CS_ALLOC_INIT_TRAVAIL+"&_method=add";
String actionNewPrononceFpi 	 =  servletContext + mainServletPath + "?userAction=ij.prononces.requerant.afficher&csTypeIJ="+globaz.ij.api.prononces.IIJPrononce.CS_FPI+"&_method=add";
String actionNewPrononceAllocAss =  servletContext + mainServletPath + "?userAction=ij.prononces.requerant.afficher&csTypeIJ="+globaz.ij.api.prononces.IIJPrononce.CS_ALLOC_ASSIST+"&_method=add";

	
String btnNewGrandeIJLabel 	  	   = objSession.getLabel("JSP_NOUVELLE_GRANDE_IJ");
String btnNewPetiteIJLabel 	  	   = objSession.getLabel("JSP_NOUVELLE_PETITE_IJ");
String btnNewPrononceAitLabel 	   = objSession.getLabel("JSP_NOUVELLE_AIT");
String btnNewPrononceFpiLabel 	   = objSession.getLabel("JSP_NOUVELLE_FPI");
String btnNewPrononceAllocAssLabel = objSession.getLabel("JSP_NOUVELLE_ALLOC_ASS");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javascript">
	bFind = false;
	usrAction = "ij.prononces.prononceJointDemande.lister";
	
	
	function clearFields () {
		document.getElementsByName("forIdGestionnaire")[0].value= "";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("likeNumeroAVS")[0].value="";
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("forCsEtatDemande")[0].value="blank";
		document.getElementsByName("forCsEtatPrononce")[0].value="<%=globaz.ij.db.prononces.IJPrononceJointDemandeManager.CLE_DROITS_TOUS%>";
		document.getElementsByName("orderBy")[0].value="<%=globaz.ij.vb.prononces.IJPrononceJointDemandeViewBean.FIELDNAME_NUM_AVS%>";
		document.getElementsByName("forCsSexe")[0].value="";
		document.getElementsByName("forDateNaissance")[0].value="";	}
		
	function likePrenomChange(){
		if(document.getElementsByName("likePrenom")[0].value!=""){										 
			document.getElementsByName("orderBy")[0].value="<%=((String[]) viewBean.getOrderByData().elementAt(0))[0].toString()%>";						
		}
	}
	
	function likeNomChange(){
		if(document.getElementsByName("likeNom")[0].value!=""){
			document.getElementsByName("orderBy")[0].value="<%=((String[]) viewBean.getOrderByData().elementAt(0))[0].toString()%>";
		}
	}
	
	function fromNumeroAVSChange(){
		if (document.getElementsByName("likeNumeroAVSNNSS")[0].value=="true"){
			if(removeDots(document.getElementsByName("likeNumeroAVS")[0].value).length==13){
				document.getElementsByName("forCsEtatPrononce")[0].value="<%=globaz.ij.db.prononces.IJPrononceJointDemandeManager.CLE_DROITS_TOUS%>";
			}
		}else{
			if (removeDots(document.getElementsByName("likeNumeroAVS")[0].value).length==11){
				document.getElementsByName("forCsEtatPrononce")[0].value="<%=globaz.ij.db.prononces.IJPrononceJointDemandeManager.CLE_DROITS_TOUS%>";
			}
		}
		
		if(document.getElementsByName("likeNumeroAVS")[0].value!=""){
			document.getElementsByName("orderBy")[0].value="<%=((String[]) viewBean.getOrderByData().elementAt(1))[0].toString()%>";
		}
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_PRONONCES"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
						<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
							<TR>
								<TD><LABEL for="forIdGestionnaire"><ct:FWLabel key="JSP_GESTIONNAIRE"/></LABEL></TD>
								<TD><ct:FWListSelectTag data="<%=IJGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut="" name="forIdGestionnaire"/></TD>
								<TD colspan="4"></TD>
							</TR>
							<TR><TD colspan="6">&nbsp;</TD></TR>
							<TR>
								<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_NSS_ABREGE"/></LABEL></TD>
								
								<%
								if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
									  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof IJNSSDTO
									  && (!JadeStringUtil.isEmpty(((IJNSSDTO) PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS()))) {%>
									  <script>bFind = true;</script>
								<TD>
									<ct1:nssPopup avsMinNbrDigit="99" 
												  nssMinNbrDigit="99" 												  
												  name="likeNumeroAVS"
												  newnss="<%=viewBean.isNNSS(((IJNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())%>" 
												  onChange="fromNumeroAVSChange();" 
												  value="<%=NSUtil.formatWithoutPrefixe(((IJNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), ((IJNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false)%>"/>
								</TD>
								<%} else {%>
								<TD>
									<ct1:nssPopup avsMinNbrDigit="99" 
												  nssMinNbrDigit="99" 												  
												  name="likeNumeroAVS" 
												  newnss="<%=viewBean.isNNSS()%>"
												  onChange="fromNumeroAVSChange();" />
								</TD>
								<%}
								%>
								
								<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_NOM"/></LABEL></TD>
								<TD><INPUT type="text" name="likeNom" value="" onchange="likeNomChange();"></TD>
								<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PRENOM"/></LABEL></TD>
								<TD>
									<INPUT type="text" name="likePrenom" value="" onchange="likePrenomChange();">
									<INPUT type="hidden" name="hasPostitField" value="<%=true%>">
								</TD>
							</TR>
							<TR>
								<TD><ct:FWLabel key="JSP_DATE_NAISSANCE"/></TD>
								<TD>
									<ct:FWCalendarTag name="forDateNaissance" value=""/>
								</TD>
								<TD><ct:FWLabel key="JSP_SEXE"/></TD>
								<TD>
									<ct:FWCodeSelectTag name="forCsSexe" wantBlank="<%=true%>" codeType="PYSEXE" defaut=""/>
								</TD>
								<TD colspan="2">&nbsp;</TD>
							</TR>
							<TR>
								<TD><LABEL for="forCsEtatDemande"><ct:FWLabel key="JSP_ETAT_DEMANDE"/></LABEL></TD>

								<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
									  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof IJPrononceParametresRCDTO) {%>
								<TD><ct:FWCodeSelectTag codeType="PRETADEMAN" name="forCsEtatDemande" wantBlank="true" defaut="<%=((IJPrononceParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getEtatDemande()%>"/></TD>
								<%} else {%>
								<TD><ct:FWCodeSelectTag codeType="PRETADEMAN" name="forCsEtatDemande" wantBlank="true" defaut=" "/></TD>
								<%}%>
								
								<TD><LABEL for="forCsEtatPrononce"><ct:FWLabel key="JSP_ETAT_PRONONCE"/></LABEL></TD>
								
								<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
									  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof IJPrononceParametresRCDTO) {%>
								<TD><ct:FWListSelectTag data="<%=viewBean.getCsEtatPrononceData()%>" defaut="<%=((IJPrononceParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getEtatPrononce()%>" name="forCsEtatPrononce"/></TD>
								<%} else {%>
								<TD><ct:FWListSelectTag data="<%=viewBean.getCsEtatPrononceData()%>" defaut="<%=globaz.ij.db.prononces.IJPrononceJointDemandeManager.CLE_DROITS_TOUS%>" name="forCsEtatPrononce"/></TD>
								<%}%>
								
								<TD colspan="2"></TD>
							</TR>
							<TR><TD colspan="6">&nbsp;</TD></TR>
							<TR>
								<TD><ct:FWLabel key="JSP_TRIER_PAR"/></TD>
								
								<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) &&
									  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO) instanceof IJPrononceParametresRCDTO) {%>
								<TD><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="<%=((IJPrononceParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRONONCE_PARAMETRES_RC_DTO)).getOrderBy()%>" name="orderBy"/></TD>
								<%} else {%>
								<TD><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="orderBy"/></TD>
								<%}%>
								
								<TD colspan="4"></TD>
							</TR>
							<TR><TD colspan="6">&nbsp;</TD></TR>
							<TR>
								<TD><input type="button" onclick="clearFields()" accesskey="C" value="Clear">  [ALT+C]</TD>
							</TR>
						</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
					<ct:ifhasright element="<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE_AIT%>" crud="c">
						<INPUT type="button" name="btnNewPrononceAit" value="<%=btnNewPrononceAitLabel%>" onClick="btnNewPrononceAit.onclick='';document.location.href='<%=actionNewPrononceAit%>'" >
					</ct:ifhasright>
<%--					Pour 1.27.1--%>
<%--					<ct:ifhasright element="<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE%>" crud="c">--%>
<%--						<INPUT type="button" name="btnNewPrononceFpi" value="<%=btnNewPrononceFpiLabel%>" onClick="btnNewPrononceFpi.onclick='';document.location.href='<%=actionNewPrononceFpi%>'" >--%>
<%--					</ct:ifhasright>--%>
					<ct:ifhasright element="<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST%>" crud="c">
						<INPUT type="button" name="btnNewPrononceAllocAss" value="<%=btnNewPrononceAllocAssLabel%>" onClick="btnNewPrononceAllocAss.onclick='';document.location.href='<%=actionNewPrononceAllocAss%>'" >
					</ct:ifhasright>
					<ct:ifhasright element="<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE%>" crud="c">
						<INPUT type="button" name="btnNewGrandeIJ" value="<%=btnNewGrandeIJLabel%>" onClick="btnNewGrandeIJ.onclick='';document.location.href='<%=actionNewGrandeIJ%>'" >
					</ct:ifhasright>
					<ct:ifhasright element="<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE%>" crud="c">
						<INPUT type="button" name="btnNewPetiteIJ" value="<%=btnNewPetiteIJLabel%>" onClick="btnNewPetiteIJ.onclick='';document.location.href='<%=actionNewPetiteIJ%>'" >
					</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>