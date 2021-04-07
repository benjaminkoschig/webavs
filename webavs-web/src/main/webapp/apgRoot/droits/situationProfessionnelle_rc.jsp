<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ page import="globaz.apg.servlet.APAbstractDroitDTOAction"%>
<%@ page import="globaz.apg.vb.droits.APSituationProfessionnelleViewBean"%>
<%@ page import="globaz.apg.servlet.IAPActions"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.prestation.api.IPRDemande" %>
<%@ page import="globaz.apg.api.droits.IAPDroitLAPG" %>
<%@ page import="globaz.apg.db.droits.APDroitLAPG" %>
<%@ page import="globaz.apg.menu.MenuPrestation" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0016";

APSituationProfessionnelleViewBean viewBean = (globaz.apg.vb.droits.APSituationProfessionnelleViewBean) request.getAttribute("viewBean");
actionNew += "&" + APAbstractDroitDTOAction.PARAM_ID_DROIT + "=" + viewBean.getIdDroit();
APDroitLAPG droitLAPG = new APDroitLAPG();
droitLAPG.setSession(viewBean.getSession());
droitLAPG.setIdDroit(viewBean.getIdDroit());
droitLAPG.retrieve();
bButtonNew = viewBean.isModifiable() && bButtonNew &&  viewBean.getSession().hasRight(IAPActions.ACTION_SITUATION_PROFESSIONNELLE, FWSecureConstants.UPDATE);
IFrameDetailHeight = "460";
IFrameListHeight = "100";
subTableHeight = 20;
scrollingDetailActive = "YES";
	MenuPrestation menuPrestation = MenuPrestation.of(session);
%>
	<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="<%=menuPrestation.getMenuIdPrincipal()%>" showTab="menu"/>
<ct:menuChange displayId="options" menuId="<%=menuPrestation.getMenuIdOptionsEmpty()%>"/>
<SCRIPT>
// pour le gestion des avertissements après l'action EcranSuivant
	var isModification = false;
	var isNouveau = true;
	
	var isFirstClick = true;
	
	bFind = true;
	usrAction = "<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.lister";
	detailLink = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.afficher&<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>=<%=viewBean.getIdDroit()%>&_method=add";
	
	function arret() {
		document.location.href = servlet + "?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.chercher";
	}
	
	var warningObj = new Object();
	warningObj.text = "";
	
	function showWarnings() {
		if (warningObj.text != "") {
			showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
		}
	}
	
	function versEcranSuivant() {
		if(isModification ||(isNouveau && document.fr_detail.document.forms[0].elements('idAffilieEmployeur').value.length>0)){
			warningObj.text="<ct:FWLabel key='JSP_MODIFS_NON_VALIDEES_PERDUES'/>";
			showWarnings();
			isModification=false;
			isNouveau=false;
		}else if(<%=globaz.apg.helpers.droits.APSituationProfessionnelleHelper.hasSituationProfWithoutIncome(viewBean.getIdDroit(), viewBean.getSession())%>){
			warningObj.text="<ct:FWLabel key='JSP_SIT_PROF_SANS_REVENU'/>";
			showWarnings();
		}else{
			if(isFirstClick){
				isFirstClick=false;
				document.location.href = servlet + "?userAction=apg.droits.situationProfessionnelle.ecranSuivant&<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>=<%=viewBean.getIdDroit()%>";
			}
		}
	}
	
	function onClickNew() {
		isNouveau = true;
		return 1;
	}
	
	function loadFrames() {
		// prevenir les cursor state not valid exception
		if(bFind) {
			// document.forms[0].submit(); appelle depuis l'ecran DE
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
	}

</SCRIPT>
	<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><%=viewBean.getTitrePage()%><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR><TD>
							<TABLE border="0">
								<TR>
									<TD><B><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></B></TD>
									<TD>
										<INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly>
										&nbsp;/&nbsp;<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTier()%>" ><ct:FWLabel key="JSP_TIERS" /></A>
									</TD>
									<TD width="30px">&nbsp;</TD>
									<TD rowspan="2">
										<PRE style="font-size:1em"><%=viewBean.getAdressePaiementRequerant()%></PRE>
									</TD>
								</TR>
								<TR>									
									<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
									<TD>
										<INPUT type="text" name="dateDebutDroit" value="<%=viewBean.getDateDebutDroit()%>" class="date disabled" readonly>
										
										<!-- le seul champ de restriction de la recherche ACTIF est privé -->
										<INPUT type="hidden" name="forIdDroit" value="<%=viewBean.getIdDroit()%>">
										<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroit()%>">
									</TD>
								</TR>
							</TABLE>
						</TD></TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<FORM action="#" target="fr_main" style="text-align: right">
	<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_SITPRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_SITPRO_ARRET"/>">
	<% if (globaz.prestation.api.IPRDemande.CS_TYPE_APG.equals((String) session.getAttribute(globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION))) { %>
	<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_SITPRO_SUIVANT"/>)" onclick="versEcranSuivant()" accesskey="<ct:FWLabel key="AK_SITPRO_SUIVANT"/>">
	<% } else if(!IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF.equals(droitLAPG.getEtat()))  { %>
	<ct:ifhasright element="<%=IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>" crud="u">
	<INPUT type="button" name="bCalculer" value="<ct:FWLabel key="JSP_CALCULER_PRESTATION"/>" onclick="versEcranSuivant()">
	</ct:ifhasright>
	<% } %>
	<!--<INPUT type="button" value="<ct:FWLabel key="JSP_AJOUTER"/>" onclick="ajouter">-->
</FORM>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>
