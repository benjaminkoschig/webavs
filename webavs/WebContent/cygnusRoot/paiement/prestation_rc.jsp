<%-- tpl:insert page="/theme/find.jtpl" --%><%@page import="globaz.cygnus.db.paiement.RFPrestationJointTiers"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>


<%@page import="globaz.corvus.api.lots.IRELot"%><script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%

idEcran="PRF0050";
rememberSearchCriterias = true;
bButtonNew = false;

String selectedId = request.getParameter("selectedId");
String idLot = request.getParameter("idLot");
String csTypeLot = request.getParameter("csTypeLot");
String csEtatLot = request.getParameter("csEtatLot");
String provenance = request.getParameter("provenance");
String description = request.getParameter("description");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "cygnus.paiement.prestation.lister";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_PRE_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<INPUT type="hidden" name="forOrderBy" value="<%=RFPrestationJointTiers.FIELD_NOMBENEFICIAIRE +", "+RFPrestationJointTiers.FIELD_PRENOMBENEFICIAIRE%>">
						
						<TR>
							<TD><ct:FWLabel key="JSP_RF_PRE_R_LOT"/></TD>
							<TD colspan="3">
								<INPUT type="text" name="lotDescription" value="<%=idLot%>" class="libelleLongDisabled" readonly="readonly">
								<INPUT type="hidden" name="forIdLot" value="<%=idLot%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_RF_PRE_R_ETAT"/></TD>
							<TD>
								<INPUT type="text" name="csEtatLotLibelle" value="<%=objSession.getCodeLibelle(csEtatLot)%>" class="disabled" readonly="readonly">
								<INPUT type="hidden" name="csEtatLot" value="<%=csEtatLot%>">
							</TD>
							<TD><ct:FWLabel key="JSP_RF_PRE_R_TYPE"/></TD>
							<TD>
								<INPUT type="text" name="csTypeLotLibelle" value="<%=objSession.getCodeLibelle(csTypeLot)%>" class="disabled" readonly="readonly">
								<INPUT type="hidden" name="csTypeLot" value="<%=csTypeLot%>">
							</TD>
							<TD><LABEL for="forCsEtatDecision"><ct:FWLabel key="JSP_RF_PRE_R_ETAT_PRESTATION"/></LABEL></TD>
							<TD><ct:FWCodeSelectTag name="forCsEtatDecision" codeType="RFETPRE" defaut="" wantBlank="true"/></TD>
						</TR>

						<TR><TD>&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_RF_PRE_R_NSS"/></LABEL>&nbsp;</TD>
							<TD>
								<ct1:nssPopup avsMinNbrDigit="99" 
										  nssMinNbrDigit="99" 
										  newnss="" 
										  name="likeNumeroAVS"/>
							</TD>
							<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_RF_PRE_R_SEXE"/></LABEL></TD>
							<TD><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>	
						</TR>
						<TR>
							<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_RF_PRE_R_NOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="likeNom" value=""></TD>
							<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_RF_PRE_R_PRENOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="likePrenom" value=""></TD>
							<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_RF_PRE_R_DATENAISSANCE"/></LABEL></TD>
							<TD><input data-g-calendar=" "  name="forDateNaissance" value=""/></TD>								
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