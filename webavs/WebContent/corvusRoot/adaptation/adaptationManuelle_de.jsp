	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.vb.adaptation.REAdaptationManuelleViewBean"%>


<%@page import="globaz.corvus.api.adaptation.IREAdaptationRente"%><script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/libraRoot/librataglib.tld" prefix="li" %>
<%
	
	idEcran="PRE0100";

	REAdaptationManuelleViewBean viewBean = (REAdaptationManuelleViewBean) session.getAttribute("viewBean");
	
	bButtonDelete = false;
	boolean hasRigth = viewBean.getSession().hasRight("corvus.adaptation.adaptationManuelle.ajouter", FWSecureConstants.UPDATE);
	bButtonUpdate = hasRigth;
	bButtonValidate = hasRigth;
	bButtonCancel = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty">
</ct:menuChange>

<script language="JavaScript">

  function add() {
  }

  function upd() {
  }

  function cancel() {	  	
  }
	   
  function validate() { 	 
	  document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_ADAPTATION_ADAPTATION_MANUELLE%>.ajouter";
	  document.forms[0].submit();   
  }
  
  function init(){
  }

  function postInit(){
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_TITRE_PAGE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colpsan="6" align="center">
			<table width="95%">
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<tr>
								<td colspan="2"><b><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_RA_SEL"/></b></td>
							</tr>
							<tr>
				 		</table>
				 	</td>
				 </tr>
				 <tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
					    <table width="100%">							
							<tr>
								<td width="30%"><b><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_DET_ASSURE"/></b></td>
								<td width="70%" colspan="3">
									<b><%=viewBean.getDescriptionTiers()%></b>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ID"/></td>
								<td width="20%">
									<%=viewBean.getIdRenteAdaptee() %>
									<input type="hidden" name="idRenteAdaptee" value="<%=viewBean.getIdRenteAdaptee()%>">
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NO_RA"/></td>
								<td width="20%">
									<%=viewBean.getIdPrestationAccordee()%>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_CS_TYPE"/></td>
								<td width="70%" colspan="3">
									<%=viewBean.getSession().getCodeLibelle(viewBean.getCsTypeAdaptation())%>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_GENRE_PREST"/></td>
								<td width="70%" colspan="3">
									<%=viewBean.getGenrePrestation()+"."+viewBean.getFractionRente()%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<tr>
								<td colspan="4" ><b><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_CHAMPS_ADA"/></b></td>
							</tr>
						</table>
					</td>
				</tr>									
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_RAM"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b><%=viewBean.getAncienRam()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_RAM"/></td>
								<td width="20%">
									<input type="text" name="nouveauRam" value="<%=viewBean.getNouveauRam()%>" class="montant"/>
									<%if (!viewBean.getCsTypeAdaptation().equals(IREAdaptationRente.CS_TYPE_NON_AUGMENTEES)){ %>
										<input type="hidden" name="isDejaAdaptee" value="on"/>
									<%} %>								
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_MNT_PRE"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b><%=viewBean.getAncienMontantPrestation()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_MNT_PRE"/></td>
								<td width="20%">
									<input type="text" name="nouveauMontantPrestation" value="<%=viewBean.getNouveauMontantPrestation()%>" class="montant"/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_MNT_REN_ORD_REMP"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b><%=viewBean.getAncienMntRenteOrdinaireRempl()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_MNT_REN_ORD_REMP"/></td>
								<td width="20%">
									<input type="text" name="nouveauMntRenteOrdinaireRempl" value="<%=viewBean.getNouveauMntRenteOrdinaireRempl()%>" class="montant"/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_SUPP_AJOUR"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b><%=viewBean.getAncienSupplementAjournement()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_SUPP_AJOUR"/></td>
								<td width="20%">
									<input type="text" name="nouveauSupplementAjournement" value="<%=viewBean.getNouveauSupplementAjournement()%>" class="montant"/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_MNT_RED_ANTI"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b><%=viewBean.getAncienMntReductionAnticipation()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_MNT_RED_ANTI"/></td>
								<td width="20%">
									<input type="text" name="nouveauMntReductionAnticipation" value="<%=viewBean.getNouveauMntReductionAnticipation()%>" class="montant"/>
								</td>
							</tr>
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_ANC_ANNEE_RAM"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b><%=viewBean.getAncienAnneeMontantRAM()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_RENTES_ADAPTEES_NOU_ANNEE_RAM"/></td>
								<td width="20%">
									<input type="text" name="nouveauAnneeMontantRAM" value="<%=viewBean.getNouveauAnneeMontantRAM()%>" class="montant"/>
								</td>
							</tr>														
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_ANC_CCS1"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b>&nbsp;<%=viewBean.getAncienCCS1()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_NOU_CCS1"/></td>
								<td width="20%">
									<input type="text" name="nouveauCCS1" value="<%=viewBean.getNouveauCCS1()%>" class="montant"/>
								</td>
							</tr>	
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_ANC_CCS2"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b>&nbsp;<%=viewBean.getAncienCCS2()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_NOU_CCS2"/></td>
								<td width="20%">
									<input type="text" name="nouveauCCS2" value="<%=viewBean.getNouveauCCS2()%>" class="montant"/>
								</td>
							</tr>	
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_ANC_CCS3"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b>&nbsp;<%=viewBean.getAncienCCS3()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_NOU_CCS3"/></td>
								<td width="20%">
									<input type="text" name="nouveauCCS3" value="<%=viewBean.getNouveauCCS3()%>" class="montant"/>
								</td>
							</tr>	
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_ANC_CCS4"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b>&nbsp;<%=viewBean.getAncienCCS4()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_NOU_CCS4"/></td>
								<td width="20%">
									<input type="text" name="nouveauCCS4" value="<%=viewBean.getNouveauCCS4()%>" class="montant"/>
								</td>
							</tr>	
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_ANC_CCS5"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b>&nbsp;<%=viewBean.getAncienCCS5()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_NOU_CCS5"/></td>
								<td width="20%">
									<input type="text" name="nouveauCCS5" value="<%=viewBean.getNouveauCCS5()%>" class="montant"/>
								</td>
							</tr>	
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_ANC_BTE_FR"/></td>
								<td width="20%">
									<table width="100%">
										<tr>
											<td width="75%" align="right" class="areaGlobazBlue"><b>&nbsp;<%=viewBean.getAncienBTEFrancs()%></b></td>
											<td width="25%">&nbsp;</td>
										</tr>
									</table>
								</td>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_NOU_BTE_FR"/></td>
								<td width="20%">
									<input type="text" name="nouveauBTEFrancs" value="<%=viewBean.getNouveauBTEFrancs()%>" class="montant"/>
								</td>
							</tr>						
						</table>
					</td>
				</tr>	
				<tr><td colspan="6"><br/></td></tr>
  				<tr>
					<td colspan="6" class="ongletLightBlue">
					    <table width="100%">							
							<tr>
								<td colspan="4" ><b><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_TITRE_ANNONCE"/></b></td>
							</tr>
						</table>
					</td>
				</tr>									
  				<tr>
					<td colspan="6" class="areaGlobazBlue">
					    <table width="100%">							
							<tr>
								<td width="30%"><ct:FWLabel key="JSP_LIST_ADAP_MANUELLE_CREATE_ANNONCE"/></td>
								<td width="70%">
									<input type="checkbox" name="isCreateAnnonceSub" value="on" CHECKED/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>					
		</TD>
	</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>