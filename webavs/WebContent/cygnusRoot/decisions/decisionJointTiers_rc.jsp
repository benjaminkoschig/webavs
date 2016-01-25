<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.decisions.RFDecisionJointTiersViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.RFNSSDTO"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script type="text/javascript"	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="PRF0046";

 	RFDecisionJointTiersViewBean viewBean = (RFDecisionJointTiersViewBean) request.getAttribute("viewBean");
	
 	rememberSearchCriterias = true;
 	
 	boolean disabledSearchByGestionaire = ("true").equalsIgnoreCase(request.getParameter("disabledSearchByGestionaire"));
 	
 	bButtonNew=false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu"/>
<SCRIPT language="JavaScript">

	bFind = false;
	usrAction = "<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.lister";

	actionNew = "<%=IRFActions.ACTION_DECISION_JOINT_TIERS%>.afficher";	

	function clearFields () {
		document.getElementsByName("likeNumeroAVS")[0].value= "";
		document.getElementsByName("partiallikeNumeroAVS")[0].value="";
		document.getElementsByName("likeNom")[0].value="";
		document.getElementsByName("likePrenom")[0].value="";
		document.getElementsByName("forDateNaissance")[0].value="";
		document.getElementsByName("forCsSexe")[0].value="";

		document.getElementsByName("forIdDecision")[0].value= "";
		document.getElementsByName("forCsEtatDecision")[0].value="";
		document.getElementsByName("forPreparePar")[0].value="";
		document.getElementsByName("forValidePar")[0].value="";
		document.getElementsByName("forAnneeQD")[0].value="";
		document.getElementsByName("forValideFrom")[0].value="";
		document.getElementsByName("forPrepareFrom")[0].value="";
		document.getElementsByName("forOrderBy")[0].value="";

		document.getElementsByName("forIdGestionnaire")[0].value="";

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

	function postInit(){

	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_DECISION_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
		<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD>
				<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
					<TR>
						<TD><LABEL for="idGestionnaire"><ct:FWLabel key="JSP_RF_DECISION_GESTIONNAIRE"/></LABEL>&nbsp;</TD>
						<TD colspan="5">					
						<%if(viewBean.isNew()){ %>										
							<ct:FWListSelectTag name="forIdGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut='<%=(JadeStringUtil.isBlankOrZero(viewBean.getIdDecision()) && !disabledSearchByGestionaire)?viewBean.getSession().getUserId():""%>'/>
						<%}else {%>
							<ct:FWListSelectTag name="forIdGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut='<%=(JadeStringUtil.isBlankOrZero(viewBean.getIdDecision()) && !disabledSearchByGestionaire)?viewBean.getIdGestionnaire():""%>'/>
						<%}%></TD>
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

						<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_RF_DECISION_NOM"/></LABEL>&nbsp;</TD>
						<TD><INPUT type="text" name="likeNom" value="" onchange="likeNomChange();"></TD>
						<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_RF_DECISION_PRENOM"/></LABEL>&nbsp;</TD>
						<TD><INPUT type="text" name="likePrenom" onchange="likePrenomChange();" value=""></TD>
					</TR>									
					<TR>
						<TD></TD><TD></TD>
						<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_RF_DECISION_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
						<TD><input data-g-calendar=" "  name="forDateNaissance" value=""/></TD>
						<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_RF_DECISION_SEXE"/></LABEL>&nbsp;</TD>	
						<TD colspan="3"><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
					</TR>
					<TR><TD colspan="6">&nbsp;</TD></TR>							
					<TR>
						<TD><LABEL for="forNumeroDecision"><ct:FWLabel key="JSP_RF_DECISION_NUMERO"/></LABEL>&nbsp;</TD>
						<TD><INPUT type="text" name="forNumeroDecision" value="" ></TD>
						<TD><LABEL for="forIdDecision"><ct:FWLabel key="JSP_RF_DECISION_ID_DECISION"/></LABEL>&nbsp;</TD>
						<TD><INPUT type="text" name="forIdDecision" value='<%=JadeStringUtil.isBlankOrZero(viewBean.getIdDecision())?"":viewBean.getIdDecision()%>' ></TD>
						<TD><LABEL for="forCsEtatDecision"><ct:FWLabel key="JSP_RF_DECISION_ETAT"/></LABEL>&nbsp;</TD>
						<TD><ct:FWListSelectTag data="<%=viewBean.getCsEtatDecisionData(true)%>" defaut="" name="forCsEtatDecision" /></TD>
					</TR>									
					<TR>
						<TD><LABEL for="forPreparePar"><ct:FWLabel key="JSP_RF_DECISION_PREPARE_PAR"/></LABEL>&nbsp;</TD>
						<TD><INPUT type="text" name="forPreparePar" value="" ></TD>
						<TD><LABEL for="forValidePar"><ct:FWLabel key="JSP_RF_DECISION_VALIDE_PAR"/></LABEL>&nbsp;</TD>
						<TD><INPUT type="text" name="forValidePar" value="" ></TD>
						<TD><LABEL for="forAnneeQD"><ct:FWLabel key="JSP_RF_DECISION_ANNEE_QD"/></LABEL>&nbsp;</TD>
						<TD><INPUT type="text" name="forAnneeQD" value="" >[aaaa]</TD>										
					</TR>	
					<TR><TD colspan="6">&nbsp;</TD></TR>									
					<TR>
						<TD><LABEL for="forDecideFrom"><ct:FWLabel key="JSP_RF_DECISION_DECIDE_FROM"/></LABEL>&nbsp;</TD>
						<TD><input data-g-calendar=" "  name="forDecideFrom" value=""/></TD>
						<TD><LABEL for="forPrepareFrom"><ct:FWLabel key="JSP_RF_DECISION_PREPARER_FROM"/></LABEL>&nbsp;</TD>
						<TD><input data-g-calendar=" "  name="forPrepareFrom" value=""/></TD>
						<TD><LABEL for="forValideFrom"><ct:FWLabel key="JSP_RF_DECISION_VALIDE_FROM"/></LABEL>&nbsp;</TD>
						<TD><input data-g-calendar=" "  name="forValideFrom" value=""/></TD>									
					</TR>	
					<TR><TD colspan="6">&nbsp;</TD></TR>
					<TR>
						<TD colspan="2"><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
						<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
						<TD colspan="2"><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="forOrderBy" /></TD>
					</TR>
				</TABLE>
			</TD>
		</TR>		
		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>