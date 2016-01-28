<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@page import="globaz.pyxis.util.CommonNSSFormater"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

		<%@ page import="globaz.commons.nss.*"%>
		<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.publish.client.JadePublishDocument"%>
<%@page import="globaz.ij.servlet.IIJActions"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.ij.vb.prononces.IJNSSDTO"%>
<%@page import="globaz.ij.vb.prestations.IJPrestationParametresRCDTO"%>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
idEcran="PIJ0014";


globaz.ij.vb.prestations.IJPrestationJointLotPrononceListViewBean viewBean = (globaz.ij.vb.prestations.IJPrestationJointLotPrononceListViewBean) request.getAttribute("viewBean");

IFrameListHeight = "220";
IFrameDetailHeight = "290";
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="javascript">
	bFind = true;
	usrAction = "<%=globaz.ij.servlet.IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE%>.afficher&_method=add";
	
	function clearFields () {
	
	document.getElementsByName("likeNumeroAVS")[0].value="";
	document.getElementsByName("fromDateDebutPrononce")[0].value="";	
	document.getElementsByName("forCsEtat")[0].value="";
	document.getElementsByName("forNoLot")[0].value="";
	document.getElementsByName("forNoBaseIndemnisation")[0].value="";
	document.getElementsByName("fromDatePaiement")[0].value="";
	document.getElementsByName("partiallikeNumeroAVS")[0].value="";
	document.getElementsByName("forCsSexe")[0].value="";
	document.getElementsByName("forDateNaissance")[0].value="";
	document.getElementsByName("likeNom")[0].value="";
	document.getElementsByName("likePrenom")[0].value="";	
	
	}	
	
	function forEtatPrestation () {
	
		<%if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO)) {%>
			var valeurOption = "<%=((globaz.ij.vb.prestations.IJPrestationParametresRCDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO)).getEtatPrestation()%>";		
			
			for (var i=0; i < document.getElementById('forCsEtat').length ; i++) {
								
				if (valeurOption==document.getElementById('forCsEtat').options[i].value) {				
					document.getElementById('forCsEtat').options[i].selected=true;
				}
			}
		<%}%>
	}
	
	function imprimerListePrestations(){
		document.forms[0].elements('userAction').value = "<%=IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE%>.imprimerListePrestations";
		document.forms[0].target="_main";
		document.forms[0].submit();
	}
</SCRIPT>

<%if(viewBean.getAttachedDocuments() != null && viewBean.getAttachedDocuments().size()>0){
	for(int i=0;i<viewBean.getAttachedDocuments().size();i++){
		String docName = ((JadePublishDocument)viewBean.getAttachedDocuments().get(i)).getDocumentLocation();
		int index = docName.lastIndexOf("/");
		if(index == -1){
			index = docName.lastIndexOf("\\");
		}
		docName = docName.substring(index);
		%>
		<script>
		window.open("<%=request.getContextPath()+ "/work/" + docName%>");
		</script>
<%	}
}
%>


<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PRESTATIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<TABLE border="0">
								<TR>
									<TD><LABEL for="forNSS"><ct:FWLabel key="JSP_NSS_ABREGE"/></LABEL></TD>
									<%
									if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) &&
										  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO) instanceof IJNSSDTO) {%>
											<TD>
												<ct1:nssPopup avsMinNbrDigit="99" 
															  nssMinNbrDigit="99"
															  newnss="<%=viewBean.isNNSS(((IJNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS())%>" 														   
															  name="likeNumeroAVS" 
															  value="<%=NSUtil.formatWithoutPrefixe(((IJNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS(), ((IJNSSDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_NSS_DTO)).getNSS().length()>14?true:false)%>"/>
											</TD>
										<%} else {%>
											<TD>
												<ct1:nssPopup avsMinNbrDigit="99" 
															  nssMinNbrDigit="99"
															  newnss="<%=viewBean.isNNSS()%>" 														   
															  name="likeNumeroAVS"/>
											</TD>
										<%
									}%>
									<TD><LABEL for="likeNom"><ct:FWLabel key="JSP_NOM"/></LABEL></TD>
									<TD><INPUT type="text" name="likeNom" value="<%=viewBean.getLikeNom()%>"></TD>
									<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PRENOM"/></LABEL></TD>
									<TD>
										<INPUT type="text" name="likePrenom" value="<%=viewBean.getLikePrenom()%>">
										<INPUT type="hidden" name="hasPostitField" value="<%=true%>">
										<INPUT type="hidden" name="hasSumMontantNet" value="<%=true%>">
									</TD>	
								</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_DATE_NAISSANCE"/></TD>
									<TD>
										<ct:FWCalendarTag name="forDateNaissance" value="<%=viewBean.getForDateNaissance()%>"/>
									</TD>
									<TD><ct:FWLabel key="JSP_SEXE"/></TD>
									<TD>
										<ct:FWCodeSelectTag name="forCsSexe" wantBlank="<%=true%>" codeType="PYSEXE" defaut="<%=viewBean.getForCsSexe()%>"/>
									</TD>
									<TD colspan="2">&nbsp;</TD>
							</TR>
								<TR>
									<TD><ct:FWLabel key="JSP_A_PARTIR_DE"/></TD>
									
									<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) &&
										  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) instanceof IJPrestationParametresRCDTO) {%>
									<TD><ct:FWCalendarTag name="fromDateDebutPrononce" value="<%=((IJPrestationParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO)).getDateDebut()%>"/></TD>
									<%} else {%>
									<TD><ct:FWCalendarTag name="fromDateDebutPrononce" value="<%=viewBean.getFromDateDebutPrononce()%>"/></TD>
									<%}%>
									
									<TD><ct:FWLabel key="JSP_ETAT"/></TD>
									<TD>
										<ct:select name="forCsEtat" wantBlank="true" defaultValue="">
											<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prestations.IIJPrestation.CS_GROUPE_ETAT_PRESTATION%>">	
											</ct:optionsCodesSystems>
											<ct:option label="<%=globaz.ij.db.prestations.IJPrestationJointLotPrononceManager.ETAT_NON_DEFINITIF%>" value="<%=globaz.ij.db.prestations.IJPrestationJointLotPrononceManager.ETAT_NON_DEFINITIF%>"></ct:option>
										</ct:select>
									</TD>
									<TD colspan="6">&nbsp;</TD>
								</TR>
								<TR>
								<TD><ct:FWLabel key="JSP_NO_LOT"/></TD>
								
								<%if (JadeStringUtil.isIntegerEmpty(viewBean.getForNoLot())) {%>
									<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) &&
										  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) instanceof IJPrestationParametresRCDTO) {%>
										<TD><INPUT type="text" name="forNoLot" value="<%=((IJPrestationParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO)).getNoLot()%>"></TD>
									<%} else {%>
										<TD><INPUT type="text" name="forNoLot" value="<%=viewBean.getForNoLot()%>"></TD>
									<%}%>
								<%} else {%>							
									<TD><INPUT type="text" name="forNoLot" value="<%=viewBean.getForNoLot()%>"></TD>
								<%}%>

								<TD><ct:FWLabel key="JSP_NO_BASE_INDEMNISATION"/></TD>
								<TD>
									
								<%if (JadeStringUtil.isIntegerEmpty(viewBean.getForNoBaseIndemnisation())) {%>
									<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) &&
										  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) instanceof IJPrestationParametresRCDTO) {%>
										<INPUT type="text" name="forNoBaseIndemnisation" value="<%=((IJPrestationParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO)).getNoIndemnisation()%>">
									<%} else {%>
										<INPUT type="text" name="forNoBaseIndemnisation" value="<%=viewBean.getForNoBaseIndemnisation()%>">
									<%}%>
								<%} else {%>
										<INPUT type="text" name="forNoBaseIndemnisation" value="<%=viewBean.getForNoBaseIndemnisation()%>">
								<%}%>
									
									<% if (viewBean.getForNoBaseIndemnisation() != null && viewBean.getForNoBaseIndemnisation().length() > 0) { %>
										<A href="<%=servletContext + mainServletPath + "?userAction=" + globaz.ij.servlet.IIJActions.ACTION_BASE_INDEMNISATION + ".chercher&idPrononce=" + viewBean.getIdPrononce()+ "&csTypeIJ=" + viewBean.getCsTypeIJ()%>">
											<ct:FWLabel key="JSP_BASES_INDEMNISATION"/>
										</A>
									<% } %>
								</TD>
								<TD>
									<ct:FWLabel key="JSP_APARTIR_DATE_PAIEMENT"/>
								</TD>
									<%if (null!=PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) &&
										  PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO) instanceof IJPrestationParametresRCDTO) {%>
									<TD><ct:FWCalendarTag name="fromDatePaiement" value="<%=((IJPrestationParametresRCDTO)PRSessionDataContainerHelper.getData(session, PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_IJ_RC_DTO)).getDatePaiement()%>"/></TD>
									<%} else {%>
									<TD><ct:FWCalendarTag name="fromDatePaiement" value="<%=viewBean.getFromDatePaiement()%>"/></TD>
									<%}%>
								</TR>					
							</TABLE>
						</TD></TR>
						<TR><TD colspan="6">&nbsp;</TD></TR>
						<TR>
							<TD colspan="5"><input type="button" onclick="clearFields()" accesskey="C" value="Clear">  [ALT+C] &nbsp;
							<ct:FWLabel key="JSP_TRIER_PAR"/>&nbsp;<ct:FWListSelectTag data="<%=viewBean.getOrderByData()%>" defaut="" name="orderBy"/>
							&nbsp;</TD>
							<TD><INPUT type="button" value="<ct:FWLabel key="JSP_IMPRIMER"/> (alt+<ct:FWLabel key="AK_IMPRIMER"/>)" onclick="imprimerListePrestations()" accesskey="<ct:FWLabel key="AK_IMPRIMER"/>"></TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<SCRIPT language="javascript">
				forEtatPrestation();
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>