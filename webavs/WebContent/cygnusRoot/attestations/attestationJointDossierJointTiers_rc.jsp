<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.cygnus.vb.attestations.RFAttestationJointDossierJointTiersViewBean "%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.RFNSSDTO"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1"%>
<%
	idEcran="PRF0035";

	RFAttestationJointDossierJointTiersViewBean viewBean = (RFAttestationJointDossierJointTiersViewBean) session.getAttribute("viewBean");
	
 	rememberSearchCriterias = true;
 	bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="menu"/>

<SCRIPT language="JavaScript">
	
	bFind = false;
	
	usrAction = "<%=IRFActions.ACTION_ATTESTATION_JOINT_TIERS%>.lister";

	actionNew = "<%=IRFActions.ACTION_ATTESTATION_JOINT_TIERS%>.afficher";	


	
	function clearFields(){			
		$("[name='dateDebut']").val("");
		$("[name='dateFin']").val("");
		$("[name='dateCreation']").val("");
		$("[name='orderBy']").val("");
		$("[name='csTypeAttestation']").val("");		
	}

	$(function(){	
		clearFields();		
	});

	function postInit(){		
		clearFields();
	}


</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_SAISIE_ATTESTATION_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_GESTIONNAIRE" /></TD>
	<TD colspan="5">					
		<ct:FWListSelectTag name="idGestionnaire" data="<%=RFGestionnaireHelper.getResponsableData(viewBean.getSession())%>" defaut=""/>
	</TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_RF_CONV_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
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
				  value=""/>
	</TD>
	<%} %>

	</TD>
	<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_RF_CONV_NOM"/></LABEL>&nbsp;</TD>
	<TD><INPUT type="text" name="likeNom" value="" ></TD>
	<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_RF_CONV_PRENOM"/></LABEL>&nbsp;</TD>
	<TD><INPUT type="text" name="likePrenom"  value=""></TD>
</TR>	
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_NUMERO_DOSSIER" /></TD>
	<TD><INPUT type="text" name="forIdDossier" value="" /></TD>
	<TD><LABEL for="dateNaissance"><ct:FWLabel key="JSP_RF_CONV_NAISSANCE"/></LABEL>&nbsp;</TD>
	<TD><input data-g-calendar=" "  name="forDateNaissance"  value=""/></TD>
	<TD><LABEL for="csSexe"><ct:FWLabel key="JSP_RF_CONV_SEXE"/></LABEL>&nbsp;</TD>	
	<TD><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" wantBlank="true" defaut=""/></TD>
</TR>
<TR>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_DATE_CREATION" /></TD>
	<TD><input data-g-calendar=" "  name="dateCreation" value=""/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_FROM" /></TD>
	<TD><input data-g-calendar=" "  name="dateDebut" value=""/></TD>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_TILL" /></TD>
	<TD><input data-g-calendar=" "  name="dateFin" value=""/></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_TYPE_DOCUMENT" /></TD>	
	<TD colspan="3"><ct:FWListSelectTag  data="<%=viewBean.getCsTypeAttestationData()%>" defaut="" name="csTypeAttestation" /></TD>
	<TD><ct:FWLabel key="JSP_RF_ATTESTATION_TRIE_PAR" /></TD>	
	<TD><ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="forOrderBy" /></TD>
</TR>	
<TR><TD colspan="6">&nbsp;</TD></TR>	
<TR>			
	<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
	<TD>&nbsp;</TD>
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