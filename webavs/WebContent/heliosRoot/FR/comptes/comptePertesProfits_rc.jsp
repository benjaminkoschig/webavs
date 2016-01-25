<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.helios.db.interfaces.*,globaz.helios.db.comptes.*,globaz.helios.translation.*,globaz.helios.tools.CGSessionDataContainerHelper" %>
<%
	idEcran = "GCF0002";
	rememberSearchCriterias = true;
	CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
	bButtonNew = false;

	String selectPeriodeId = "";
	CGSessionDataContainerHelper sessionContainerHelper = new CGSessionDataContainerHelper();
	selectPeriodeId = (String)sessionContainerHelper.getData(session,CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_PERIODE);
	String selectTypeComptaId = (String)sessionContainerHelper.getData(session,CGSessionDataContainerHelper.KEY_LAST_SELECTED_ID_TYPE_COMPTA);


	String toutLexercice = "Tout l'exercice";
	if (languePage.equalsIgnoreCase("de"))
		toutLexercice = "Ganze Rechnungsjahr";

	String tout = "Tous";
	if (languePage.equalsIgnoreCase("de"))
		tout = "Alles";

	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE"))
		aucun = "Kein";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CG-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CG-OnlyDetail"/>

<%
	globaz.framework.menu.FWMenuBlackBox bb = (globaz.framework.menu.FWMenuBlackBox) session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_USER_MENU);
	bb.setNodeOpen(false, "parameters", "CG-MenuPrincipal");
%>

<SCRIPT>
usrAction = "helios.comptes.comptePertesProfits.lister";
bFind = true;
timeWaiting = 0;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu du compte de pertes et profits<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<tr>
					<td nowrap align="left">Mandat&nbsp;</td>
					<td align="left"><input name='_libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'/></td>
					<td align="right">Exercice&nbsp;</td>
					<td align="left"><input name='_exercice' class='disabled' readonly value='<%=exerciceComptable.getFullDescription()%>'/></td>
				</tr>

				<tr>
					<td align="left" nowrap>Période&nbsp;</td>
					<td align="left"><ct:FWListSelectTag name="reqForListPeriodesComptable" defaut="<%=selectPeriodeId%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session,toutLexercice)%>"/></td>
					<td align="right">Inclure périodes précédentes&nbsp;</td>
					<td align="left"><input type="checkbox" name="inclurePeriodesPrec"/>
					<script>
						document.getElementById("reqForListPeriodesComptable").style.width = "5cm";
					</script>
					</td>
				</tr>

				<tr>
					<td colspan="4" width="100%"><hr/>
					<input type="hidden" name="forSoldeOpen" value="true"/>
					<input type="hidden" name="forIdExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"/>
				</td>
				</tr>

				<tr>
					<td align="left" nowrap>Comptabilité</td>
					<td nowrap align="left"><ct:FWCodeSelectTag name="reqComptabilite" defaut="<%=selectTypeComptaId%>" codeType="CGPRODEF" /></td>
					<td align="right">Domaine&nbsp;</td>
					<td align="left"><ct:FWCodeSelectTag name="reqDomaine" except="<%=CGComptePertesProfitsListViewBean.getExcept()%>" defaut="<%=CGCompte.CS_COMPTE_TOUS%>" codeType="CGDOMCPT" /></td>
				</tr>

				<tr>
					<td colspan="2">&nbsp;</td>
					<td align="right">Centre de charge&nbsp;</td>
					<td align="left"><ct:FWListSelectTag name="forIdCentreCharge" defaut="" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, exerciceComptable.getIdMandat())%>"/>
					<script>
						document.getElementById("reqComptabilite").style.width = "3cm";
						document.getElementById("reqDomaine").style.width = "5.5cm";
					</script>
					</td>
					</tr>

<%
	if (exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {
%>
					<tr>
					<td align="left">Secteur AVS&nbsp;</td>
					<td align="left"><ct:FWListSelectTag name="forIdSecteurAVS" defaut="" data="<%=globaz.helios.translation.CGListes.getSecteurAVSListe(session, exerciceComptable.getIdMandat(), tout)%>"/>
					<script>
						document.getElementById("reqComptabilite").style.width = "3cm";
						document.getElementById("reqDomaine").style.width = "5.5cm";
					</script>
					<td align="right">Plan comptable OFAS&nbsp;</td>
					<td align="left"><input type="checkbox" value="true" name="groupIdCompteOfas"/></td>
					</tr>
<%
	}
%>
                        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>