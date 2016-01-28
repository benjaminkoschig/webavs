<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.cygnus.vb.prestationsaccordees.RFPrestationsAccordeesViewBean"%>
<%@page import="globaz.cygnus.db.paiement.RFPrestationAccordee"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.cygnus.vb.RFNSSDTO"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%

	idEcran="PRF0048";

 	RFPrestationsAccordeesViewBean viewBean =  (RFPrestationsAccordeesViewBean)request.getAttribute("viewBean");	
 	rememberSearchCriterias = true;
 	
 	bButtonNew = false;
	
 	String selectedId = request.getParameter("selectedId");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = false;	
	usrAction = "<%=IRFActions.ACTION_PRESTATION_ACCORDEE%>.lister";

	actionNew = "<%=IRFActions.ACTION_PRESTATION_ACCORDEE%>.afficher";	
	
	function postInit(){

	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_ACCORDEE_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>								
						<TR>
							<TD><LABEL for="likeNumeroAVS"><ct:FWLabel key="JSP_RF_ACCORDEE_R_NSS"/></LABEL>&nbsp;<INPUT type="hidden" name="hasPostitField" value="<%=true%>"></TD>
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
										  value="" />
							</TD>
							<%} %>
							<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_RF_ACCORDEE_R_NOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="likeNom" value="" onchange="likeNomChange();"></TD>
							<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_RF_ACCORDEE_R_PRENOM"/></LABEL>&nbsp;</TD>
							<TD><INPUT type="text" name="likePrenom" onchange="likePrenomChange();" value=""></TD>
						</TR>									
						<TR>
							<TD></TD><TD></TD>
							<TD><LABEL for="forDateNaissance"><ct:FWLabel key="JSP_RF_ACCORDEE_R_DATE_NAISSANCE"/></LABEL>&nbsp;</TD>
							<TD><input data-g-calendar=" "  name="forDateNaissance" value=""/></TD>
							<TD><LABEL for="forCsSexe"><ct:FWLabel key="JSP_RF_ACCORDEE_R_SEXE"/></LABEL>&nbsp;</TD>	
							<TD colspan="3"><ct:FWCodeSelectTag name="forCsSexe" codeType="PYSEXE" defaut="" wantBlank="true"/></TD>
						</TR>
						<TR><TD colspan="6">&nbsp;</TD></TR>						
						<TR>
							<TD><ct:FWLabel key="JSP_RF_ACCORDEE_TYPE"/></TD>
							<TD>							
								<ct:FWListSelectTag data="<%=viewBean.getCsTypesPrestationsAccordeesData()%>" defaut="" name="forCsTypesPrestationsAccordees" />													
							</TD>
							<TD><ct:FWLabel key="JSP_RF_ACCORDEE_ETAT"/></TD>
							<TD>
								<ct:FWListSelectTag data="<%=viewBean.getCsEtatsPrestationsAccordeesData()%>" defaut="" name="forCsEtatsPrestationsAccordees" />														
							</TD>
							<TD><ct:FWLabel key="JSP_RF_ACCORDEE_GENRE"/></TD>
							<TD>
								<ct:FWListSelectTag data="<%=viewBean.getCsGenresPrestationsAccordeesData()%>" defaut="" name="forCsGenresPrestationsAccordees" />								
							</TD>
							<TD><ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;</TD>
							<TD>
								<ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="forOrderBy" />
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