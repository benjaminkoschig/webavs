<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<script type="text/javascript"
	src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
	idEcran="GLI0006";
 	globaz.libra.vb.journalisations.LIJournalisationsJointDossiersViewBean viewBean = (globaz.libra.vb.journalisations.LIJournalisationsJointDossiersViewBean) request.getAttribute("viewBean");

	if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdDossier())){
		viewBean.loadDossier();
	}

	if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdTiers())){
		viewBean.loadTiers();	
	}

	boolean isPrintBT = false;

	if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdDossier()) 
		 || !globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdTiers()) ){
		bButtonNew = objSession.hasRight(globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_DE, FWSecureConstants.ADD);
		actionNew  = "libra?userAction="+globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_DE + ".afficher&idDossier="+viewBean.getDossier().getIdDossier()+"&idTiers="+viewBean.getDossier().getIdTiers()+"&_method=add";
		bButtonFind = true;
		isPrintBT = true;
	} else {
		bButtonNew = false;
		bButtonFind = false;
	}
	
	String lienRetour = "";
	
	if (null!=globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) &&
		globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO) instanceof  globaz.libra.utils.LIRecherchesDTO) {
		lienRetour = ((globaz.libra.utils.LIRecherchesDTO) globaz.prestation.tools.PRSessionDataContainerHelper.getData(session, globaz.prestation.tools.PRSessionDataContainerHelper.KEY_LIBRA_DTO)).getUrlRetour();	
	}	
	
	String nssTiers 		= "";
	String designationTiers = "";
	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="li-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="LI-OnlyDetail"/>


<SCRIPT language="JavaScript">

	bFind = false;
	
	usrAction = "<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_RC%>.lister";
	
	<% if (bButtonFind) { %>
		bFind = true;
	<% } %>

	function effaceDossier(){
		document.getElementsByName("idDossierDomaine")[0].value="";
		document.getElementsByName("forIdDossier")[0].value="";
	}

	function effaceUser(){
		document.getElementsByName("forIdUser")[0].value="";
	}

	function printScreenHtml(){
	    document.getElementsByName("userAction")[0].value= "<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_RC%>.exporter";
	    document.forms[0].target="_blank";
	    document.forms[0].submit();
	    document.forms[0].target="fr_list";
	    document.getElementsByName("userAction")[0].value= "<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_RC%>.lister";
	       
	}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="ECRAN_JOU_RC_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

						
						<tr>
							<td><ct:FWLabel key="ECRAN_JOU_RC_TIERS"/></td>
							<td colspan="2">
							<INPUT type="hidden" name="userAction" id="userAction" value="<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_RC%>.lister">
							<INPUT type="hidden" name="selectorName" value="">
	
							<% 

								if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdTiers())){
									nssTiers = globaz.commons.nss.NSUtil.formatWithoutPrefixe(viewBean.getTiers().getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_NUM_AVS_ACTUEL), true);
									designationTiers = viewBean.getTiers().getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_NOM)
														+ " " +
														viewBean.getTiers().getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_PRENOM)
														+ " / " +
														viewBean.getTiers().getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_DATE_NAISSANCE)
														+ " / " +
														viewBean.getSession().getCodeLibelle(viewBean.getTiers().getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_SEXE))
														+ " / " +
														globaz.libra.utils.LIEcransUtil.getLibellePays(viewBean.getTiers().getProperty(globaz.prestation.interfaces.tiers.PRTiersWrapper.PROPERTY_ID_PAYS_DOMICILE), viewBean.getSession())
														;
								}			
							 %>
							<input type="hidden" name="forIdTiers" value="<%=viewBean.getIdTiers()%>">
							
							<ct1:nssPopup avsMinNbrDigit="99"
										  nssMinNbrDigit="99"
										  newnss=""
										  name="likeNumeroAVS"
										  value="<%= nssTiers %>"	/>
	
							<ct:FWSelectorTag
								name="selecteurTiers"
				
								methods="<%=viewBean.getMethodesSelecteurTiers()%>"
								providerApplication="pyxis"
								providerPrefix="TI"
								providerAction="pyxis.tiers.tiers.chercher"
								target="fr_main"
								redirectUrl="<%=mainServletPath%>"/>
								&nbsp;
								<b><%= designationTiers %></b>
							</td> 
							<td>
								<% if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(nssTiers)){ %>
									<A href="#" onclick="window.open('<%=servletContext%><%=("/libra")%>?userAction=<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS_RC%>.actionAfficherDossierGed&amp;noAVSId=<%=nssTiers%>','GED_CONSULT')" >GED</A>
									&nbsp;&nbsp;&nbsp;
								<% } %>
								
								<INPUT type="checkbox" name="isVueFamille" value="on" onclick="effaceDossier();">
								<ct:FWLabel key="ECRAN_JOU_RC_VUE_FAM"/>
							</td>
						</tr>
						<tr>
							
							<% 
							String idDossierDomaine = "";
							String idDossier		= "";
							
							if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getIdDossier())){
								idDossierDomaine = viewBean.getDossier().getIdDossier() + " - " + viewBean.getDossier().getLibelleDomaine();
								idDossier = viewBean.getDossier().getIdDossier();
							}
							
							%>
						
							<td><ct:FWLabel key="ECRAN_JOU_RC_DOSSIER"/></td><input type="hidden" name="forIdDossier" value="<%=idDossier%>">
							<td><input type="text" size="21" name="idDossierDomaine" value="<%=idDossierDomaine%>">

								<ct:FWSelectorTag
								name="selecteurDossier"
				
								methods="<%=viewBean.getMethodesSelecteurDossier()%>"
								providerApplication="libra"
								providerPrefix="LI"
								providerAction="libra.dossiers.dossiersJointTiers.chercher"
								target="fr_main"
								redirectUrl="<%=mainServletPath%>"/>
								&nbsp;
								<input type="button" name="effacerDossier" onClick="effaceDossier();" value="<ct:FWLabel key="ECRAN_JOU_RC_EFFACER"/>"> 
							</td> 
							<td><ct:FWLabel key="ECRAN_JOU_RC_UTILISATEUR"/></td>
							<td><input type="text" size="21" name="forIdUser" value="<%=viewBean.getIdUser()%>">

								<ct:FWSelectorTag
								name="selecteurUser"
				
								methods="<%=viewBean.getMethodesSelecteurUser()%>"
								providerApplication="libra"
								providerPrefix="LI"
								providerAction="libra.utilisateurs.utilisateurs.chercher"
								target="fr_main"
								redirectUrl="<%=mainServletPath%>"/>
								&nbsp;
								<input type="button" name="effacerUser" onClick="effaceUser();" value="<ct:FWLabel key="ECRAN_JOU_RC_EFFACER"/>"> 
							</td> 							
							
							</tr>
						<tr><td colspan="6"><hr></td></tr>
						<tr>
							<TD><ct:FWLabel key="ECRAN_ECH_RC_PERIODE"/></TD>
							<TD colspan="2">
								<ct:FWCalendarTag name="forDateDebut" value=""/>
								 - 
								<ct:FWCalendarTag name="forDateFin" value="<%= globaz.globall.util.JACalendar.todayJJsMMsAAAA() %>"/>
							</TD>
							<TD>
								<% if (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(lienRetour)){ %>
									<a href="<%=request.getContextPath() + "/" + lienRetour %>" class="external_link">
										<ct:FWLabel key="MENU_OPTION_RET_APP"/>
									</a>
								<% } %>
							</TD>
						</tr>
						
						<INPUT type="hidden" name="jointureFichier" value="<%=globaz.journalisation.db.journalisation.access.JOJournalisationManager.TYPE_PAR_IMBRICATION%>">
						<INPUT type="hidden" name="forTypeReferenceProvenance" value="<%=ch.globaz.libra.constantes.ILIConstantesExternes.REF_PRO_DOSSIER%>">
						<INPUT type="hidden" name="forIdCleReferenceProvenance" value="1">
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>

				<ct:ifhasright element="<%=globaz.libra.servlet.ILIActions.ACTION_JOURNALISATIONS %>" crud="r">
					<% if (globaz.libra.utils.LIEcransUtil.isWantButtonPrint(viewBean.getSession()) && bButtonFind){ %>
						<INPUT class="btnCtrl" type="button" id="btnImprimer" value="<ct:FWLabel key="ECRAN_ALL_RC_EDITER"/>" onclick="printScreenHtml();">
					<% } %>
				</ct:ifhasright>
				
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>