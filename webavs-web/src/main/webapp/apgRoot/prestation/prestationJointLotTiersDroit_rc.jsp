<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.pyxis.util.CommonNSSFormater"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@ page import="globaz.commons.nss.*"%>
		<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.jade.publish.client.JadePublishDocument"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@ page import="globaz.prestation.api.IPRDemande" %>
<%@ page import="globaz.apg.menu.MenuPrestation" %>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
idEcran="PAP0036";

rememberSearchCriterias = true;

globaz.apg.vb.prestation.APPrestationJointLotTiersDroitListViewBean viewBean = (globaz.apg.vb.prestation.APPrestationJointLotTiersDroitListViewBean) request.getAttribute("viewBean");

bButtonNew = false;
boolean wantTousType="tous".equals(request.getParameter("forTypeDroit"));
boolean wantTousEtat="tous".equals(request.getParameter("forEtat"));
MenuPrestation menuPrestation = MenuPrestation.of(session);

%>    
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="<%=menuPrestation.getMenuIdPrincipal()%>" showTab="menu"/>
<ct:menuChange displayId="options" menuId="<%=menuPrestation.getMenuIdOptionsEmpty()%>"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "apg.prestation.prestationJointLotTiersDroit.lister";
	
	
	function clearFields () {
	
	document.getElementsByName("likeNumeroAVS")[0].value="";	
	document.getElementsByName("forNoLot")[0].value="";
	document.getElementsByName("fromDateDebut")[0].value="";
	document.getElementsByName("toDateFin")[0].value="";
	document.getElementsByName("forEtat")[0].value="<%=globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager.ETAT_NON_DEFINITIF%>";
	document.getElementsByName("forIdDroit")[0].value="";
	document.getElementsByName("partiallikeNumeroAVS")[0].value="";
	document.getElementsByName("forCsSexe")[0].value="";
	document.getElementsByName("forDateNaissance")[0].value="";
	document.getElementsByName("likeNom")[0].value="";
	document.getElementsByName("likePrenom")[0].value="";	
	
	}

	function forEtatPrestation () {
	
		<%if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)) {%>
			var valeurOption = "<%=((globaz.apg.vb.prestation.APPrestationParametresRCDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)).getEtatPrestation()%>";		
			
			for (var i=0; i < document.getElementById('forEtat').length ; i++) {
								
				if (valeurOption==document.getElementById('forEtat').options[i].value) {				
					document.getElementById('forEtat').options[i].selected=true;
				}
			}
		<%}%>
	}
	
	function imprimerListePrestations(){
		document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT%>.imprimerListePrestations";
		document.forms[0].target="_main";
		document.forms[0].submit();
	}
	
    function imprimerListePrestationsExcel() {
        document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT%>.imprimerListePrestations";
        document.forms[0].elements('isExcel').value="true";
        document.forms[0].target = "_main";
        document.forms[0].submit();
    }

	var warningObj = new Object();
	warningObj.text = "";
	
	function showWarnings() {
		if (warningObj.text != "") {
			showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
		}
	}
				
	function checkPrestationsCalculee(){
		<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
				warningObj.text = "<%=viewBean.getMessage()%>";
				showWarnings();
				warningObj.text = "";
		<%}%>
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
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)=="52201001") {%>
				<ct:FWLabel key="JSP_PRESTATIONS_APG"/>
				<%}%>
				<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)=="52201002") {%>
				<ct:FWLabel key="JSP_PRESTATIONS_MAT"/>
				<%}%>
				<%if ((String) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION) == IPRDemande.CS_TYPE_PANDEMIE) {%>
				<ct:FWLabel key="JSP_PRESTATIONS_PAN"/>
				<%}%>
				<%if ((String) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION) == IPRDemande.CS_TYPE_PATERNITE) {%>
				<ct:FWLabel key="JSP_PRESTATIONS_PAT"/>
				<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR>
							<TD>
							<ct:FWLabel key="JSP_A_PARTIR_DE_NSS"/>
							</TD>
								<%if ((globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getForIdDroit())) && (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getForNoLot()))) {%>
									<%if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_DROIT_DTO)) {%>
										<TD>
											<ct1:nssPopup avsMinNbrDigit="99" 
														  nssMinNbrDigit="99" 
														  name="likeNumeroAVS"
														  newnss="<%=viewBean.isNNSS(((globaz.apg.vb.droits.APDroitDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_DROIT_DTO)).getNoAVS())%>" 
														  value="<%=NSUtil.formatWithoutPrefixe(((globaz.apg.vb.droits.APDroitDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_DROIT_DTO)).getNoAVS(), ((globaz.apg.vb.droits.APDroitDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_DROIT_DTO)).getNoAVS().length()>14?true:false)%>"/>
										</TD>
									<%} else {%>
										<TD>
											<ct1:nssPopup avsMinNbrDigit="99" 
														  nssMinNbrDigit="99"
														  newnss="<%=viewBean.isNNSS()%>"  
														  name="likeNumeroAVS"/>
										</TD>
									<%}%>
								<%} else {%>
									<TD>
										<ct1:nssPopup avsMinNbrDigit="99" 
													  nssMinNbrDigit="99"  
													  name="likeNumeroAVS"/>
									</TD>									
								<%}
								%>
							<TD>
								<INPUT type="hidden" name="hasPostitField" value="<%=true%>">
								<INPUT type="hidden" name="hasSumMontantNet" value="<%=true%>">
								<input type="hidden" name="forTypeDroit" value="<%=(String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)%>" class="libelleDisabled" />
								<LABEL for="likeNom"><ct:FWLabel key="JSP_NOM"/></LABEL></TD>
							<TD><INPUT type="text" name="likeNom" value=""></TD>
							<TD><LABEL for="likePrenom"><ct:FWLabel key="JSP_PRENOM"/></LABEL></TD>
							<TD><INPUT type="text" name="likePrenom" value=""></TD>	
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
							<TD><ct:FWLabel key="JSP_PERIODES_DU"/></TD>
							<%if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)) {%>
							<TD><ct:FWCalendarTag name="fromDateDebut" value="<%=((globaz.apg.vb.prestation.APPrestationParametresRCDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)).getPeriodeDateDebut()%>"/></TD>
							<%} else {%>
							<TD><ct:FWCalendarTag name="fromDateDebut" value=""/></TD>
							<%}%>
							
							<TD><ct:FWLabel key="JSP_AU"/></TD>
							<%if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)) {%>
							<TD><ct:FWCalendarTag name="toDateFin" value="<%=((globaz.apg.vb.prestation.APPrestationParametresRCDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)).getPeriodeDateFin()%>"/></TD>
							<%} else {%>
							<TD><ct:FWCalendarTag name="toDateFin" value=""/></TD>
							<%}%>
								
							<TD><ct:FWLabel key="JSP_ETAT"/></TD>
							<TD>
								<SELECT name="forEtat">
									<OPTION value="" <%=wantTousEtat?"selected":""%>>&nbsp;</OPTION>
									<OPTION value="<%=globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_OUVERT%>"><ct:FWLabel key="JSP_OUVERT"/></OPTION>
									<OPTION value="<%=globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_VALIDE%>"><ct:FWLabel key="JSP_VALIDE"/></OPTION>
									<OPTION value="<%=globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_CONTROLE%>"><ct:FWLabel key="JSP_CONTROLE"/></OPTION>
									<OPTION value="<%=globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_MIS_LOT%>"><ct:FWLabel key="JSP_MIS_LOT"/></OPTION>
									<OPTION value="<%=globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_DEFINITIF%>"><ct:FWLabel key="JSP_DEFINITIF"/></OPTION>
									<OPTION value="<%=globaz.apg.api.prestation.IAPPrestation.CS_ETAT_PRESTATION_ANNULE%>"><ct:FWLabel key="JSP_ANNULE"/></OPTION>
									<OPTION value="<%=globaz.apg.db.prestation.APPrestationJointLotTiersDroitManager.ETAT_NON_DEFINITIF%>" <%=wantTousEtat?"":"selected"%>><ct:FWLabel key="JSP_NON_DEFINITIF"/></OPTION>
								</SELECT>
							</TD>													
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_LOT"/></TD>
							<TD>
								<%if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getForNoLot())) {%>
									<%if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)) {%>
										<INPUT type="text" name="forNoLot" value="<%=((globaz.apg.vb.prestation.APPrestationParametresRCDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)).getNoLot()%>">
									<%} else {%>						
										<INPUT type="text" name="forNoLot" value="<%=viewBean.getForNoLot()%>">
									<%}%>
								<%} else {%>
									<INPUT type="text" name="forNoLot" value="<%=viewBean.getForNoLot()%>">
								<%}%>
								
							</TD>
							
							<TD><ct:FWLabel key="JSP_NO_DROIT"/></TD>
							<%if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getForIdDroit())) {%>
								<%if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)) {%>
									<TD colspan="3"><INPUT type="text" name="forIdDroit" value="<%=((globaz.apg.vb.prestation.APPrestationParametresRCDTO)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_PRESTATION_PARAMETRES_RC_DTO)).getNoDroit()%>"></TD>
								<%} else {%>
									<TD colspan="3"><INPUT type="text" name="forIdDroit" value="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getForIdDroit())?"":viewBean.getForIdDroit()%>"></TD>
								<%}%>
							<%} else {%>
								<TD colspan="3"><INPUT type="text" name="forIdDroit" value="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getForIdDroit())?"":viewBean.getForIdDroit()%>"></TD>
							<%}%>
						</TR>
						<TR>
							<TD><input type="button" onclick="clearFields()" accesskey="C" value="Clear">  [ALT+C]</TD>
							<td colspan="4">&nbsp;</td>
    <TD><INPUT type="button" value="<ct:FWLabel key="JSP_IMPRIMER"/> (alt+<ct:FWLabel key="AK_IMPRIMER"/>)"
               onclick="imprimerListePrestations()" accesskey="<ct:FWLabel key="AK_IMPRIMER"/>"> &nbsp; &nbsp;<img src="<%=request.getContextPath()%>/images/excel.png"
                                                                                                     onClick="imprimerListePrestationsExcel()"
                                                                                                     title="Exporter la liste"
                                                                                                     border="0"
                                                                                                     onMouseOver="this.style.cursor='hand';"
                                                                                                     onMouseOut="this.style.cursor='pointer';"
                                                                                                     width="20px"
                                                                                                     height="20px"
    ></TD>

						</TR>
<INPUT type="hidden" name="isExcel" value="">
						
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<SCRIPT language="javascript">
				forEtatPrestation();
				</SCRIPT>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
