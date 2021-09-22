<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	selectedIdValue = request.getParameter("selectedId");
	idEcran="PIJ0006";
	globaz.ij.vb.prononces.IJAbstractPrononceProxyViewBean viewBean = (globaz.ij.vb.prononces.IJAbstractPrononceProxyViewBean) session.getAttribute("viewBean");
	String detailRequerant = request.getParameter("detailRequerant");
	String noAVS = request.getParameter("noAVS");

	bButtonUpdate = bButtonUpdate && viewBean.isModifierPermis();
	bButtonValidate = false;
	bButtonDelete = false;
	bButtonCancel = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>
<SCRIPT language="JavaScript1.2" src="<%=servletContext%>/ijRoot/masks.js"></SCRIPT>

<SCRIPT language="JavaScript1.2">
	function add() {
	    //deja créé qd on arrive ici
	}

	function upd() {
		document.forms[0].elements('userAction').value="ij.prononces.saisiePrononce.modifier";
		document.forms[0].elements('modifie').value="true";
		$('#afficheWarning').prop( "checked", true);
	}

	function validate() {
	    if (document.forms[0].elements('_method').value == "read"){
	    	document.forms[0].elements('userAction').value="ij.prononces.saisiePrononce.actionEcranSuivant";
	    } else {
			document.forms[0].elements('userAction').value="ij.prononces.saisiePrononce.modifier";
	    }
	    return true;

	}

	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE%>.arreterEtape3";
  		document.forms[0].submit();
 	}

	function cancel() {

	if (document.forms[0].elements('_method').value == "add")
	  document.forms[0].elements('userAction').value="back";
	 else
	  document.forms[0].elements('userAction').value="ij.prononces.prononceJointDemande.chercher";

	}

	function del() {
	    //On peut pas ici
	}

	function init(){

		checkWarn();
	}

	function checkRevision(){
		fieldFormat(document.forms[0].elements('dateDebutPrononce'),'CALENDAR');

		<%if (viewBean instanceof globaz.ij.vb.prononces.IJGrandeIJPViewBean){%>
			if (compareDates(document.forms[0].elements('dateDebutPrononce').value, "dd.MM.yyyy","<%=viewBean.getSession().getApplication().getProperty(globaz.ij.application.IJApplication.PROPERTY_DATE_DEBUT_4EME_REVISION)%>","dd.MM.yyyy")==1){
				//4eme revision
				document.all('revision3').style.display = 'none';
			} else {
				document.all('revision3').style.display = 'block';
				//3eme revision
			}
		<%}%>
	}

	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.ij.servlet.IIJActions.ACTION_SAISIE_PRONONCE%>.arreterEtape3";
  		document.forms[0].submit();
 	}

 	function postInit(){
 		checkRevision();
 		initMaskCs(document.forms[0].elements('codesCasSpecial'));
 	}

 	function initMaskCs(champ){
		// Création du masque pour la saisie de cas special
		csMask = null;
	    csMask = new Mask("##-##-##-##-##", "string");
	    csMask.attach(champ);
 	}

 	function checkCodesCasSpecial(champ, valeur){
		// Si la chaine n'est pas vide ou ne se termine pas par "-"
		// on supprime le dernier charactere de la chaine
 		if(valeur!="" &&
 		   valeur.length!=14 &&
 		   valeur.charAt(valeur.length-1)!="-"){
 			champ.value=valeur.substr(0, valeur.length-1);
 		}
 	}

	function checkWarn() {
		$("#dialog_warn").dialog({
			resizable: false,
			height: 500,
			width: 500,
			modal: true,
			buttons: [{
					id: "correct",
					text: "<ct:FWLabel key='JSP_CORRIGER'/>",
					click: function () {
						$('#afficheWarning').prop( "checked", true);
						$(this).dialog("close");
					}
			}, {
				id: "continue",
				text: "<ct:FWLabel key='JSP_CONTINUER'/>",
				click: function () {
					$('#afficheWarning').prop( "checked", false);

					validate();
					action(COMMIT);
					$(this).dialog("close");
				}
			}],
			open : function() {
				$(".ui-dialog-titlebar-close",".ui-dialog-titlebar").hide();
				$("#Ok").focus();
				<% if(!viewBean.getWarningMessage().isEmpty()) { %>
					$('#dialog_warn').append('<%=viewBean.getWarningMessage()%>');
				<% } %>
			}
		});
	}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SAISIE_PRONONCE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="6">
								<INPUT type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
								<INPUT type="hidden" name="modifie" value="<%=viewBean.isModifie()%>">
								<INPUT type="hidden" name="avecDecision" value="<%=viewBean.getPrononce().getAvecDecision()%>">
								<INPUT type="hidden" name="noAVS" value="<%=noAVS%>">
							</TD>
						</TR>
						<TR>
							<TD><b><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></b></TD>
							<TD colspan="5">
								<INPUT type="text" value="<%=detailRequerant%>" size="100" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_PRONONCE"/></TD>
							<TD colspan="2"><ct:FWCalendarTag name="datePrononce" value="<%=viewBean.getDatePrononce()%>"/></TD>
							<TD><ct:FWLabel key="JSP_OFFICE_AI"/></TD>
							<TD colspan="2">
								<ct:FWListSelectTag data="<%=viewBean.getListeOfficeAI()%>" defaut="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getOfficeAI())?viewBean.getNoOfficeAICantonal():viewBean.getOfficeAI()%>" name="officeAI"/>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT_PRONONCE"/></TD>
							<TD colspan="2"><ct:FWCalendarTag name="dateDebutPrononce" value="<%=viewBean.getDateDebutPrononce()%>"/></TD>
							<TD><ct:FWLabel key="JSP_DATE_FIN_PRONONCE"/></TD>
							<TD colspan="2"><ct:FWCalendarTag name="dateFinPrononce" value="<%=viewBean.getDateFinPrononce()%>"/></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_GENRE_READAPTATION"/></TD>
							<TD colspan="2">	<ct:select name="csGenre" defaultValue="<%=viewBean.getCsGenre()%>">
										<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJPrononce.CS_GROUPE_GENRE_READAPTATION%>">
										</ct:optionsCodesSystems>
									</ct:select>
							</TD>
							<TD><ct:FWLabel key="JSP_TYPE_HEBERGEMENT"/></TD>
							<TD colspan="2">	<ct:select name="csTypeHebergement" defaultValue="<%=viewBean.getCsTypeHebergement()%>">
										<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJPrononce.CS_GROUPE_TYPE_HEBERGEMENT%>">
										</ct:optionsCodesSystems>
									</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_DECISION_AI_COMMUNICATION"/></TD>
							<TD colspan="5"><INPUT type="text" name="noDecisionAI" value="<%=viewBean.getNoDecisionAI()%>" class="libelleLong">&nbsp;Format : AAAANNNNNNC</TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_GARANTI_AA_REDUIT"/></TD>
							<TD><INPUT type="checkbox" value="on" name="montantGarantiAAReduit" <%=viewBean.getMontantGarantiAAReduit().booleanValue()?"CHECKED":""%>></TD>
							<TD><ct:FWLabel key="JSP_MONTANT_GARANTI_AA"/></TD>
							<TD><INPUT type="text" name="montantGarantiAA" value="<%=viewBean.getMontantGarantiAA()%>"></TD>
							<TD><ct:FWLabel key="JSP_DEMI_IJ_AC"/></TD>
							<TD><INPUT type="text" name="demiIJAC" value="<%=viewBean.getDemiIJAC()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_STATUT"/></TD>
							<TD colspan="5">	<ct:select name="csStatutProfessionnel" defaultValue="<%=viewBean.getCsStatutProfessionnel()%>">
										<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJPrononce.CS_GROUPE_STATUT_PROFESSIONNEL%>">
										</ct:optionsCodesSystems>
									</ct:select>
								</TD>
						</TR>
						<TR>
							<TD colspan="6" style="padding-top: 10px"><B><ct:FWLabel key="JSP_REVENU_DURANT_READAPTATION"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT"/></TD>
							<TD><INPUT type="text" name="montantRevenuReadaptation" value="<%=viewBean.loadRevenuReadaptation().getRevenu()%>"></TD>
							<TD><ct:FWLabel key="JSP_PERIODICITE"/></TD>
							<TD>
								<ct:select name="csPeriodiciteRevenuReadaptation" defaultValue="<%=viewBean.loadRevenuReadaptation().getCsPeriodiciteRevenu()%>">
									<ct:optionsCodesSystems csFamille="<%=globaz.ij.api.prononces.IIJSituationProfessionnelle.CS_GROUPE_PERIODICITE_SALAIRE%>"/>
								</ct:select>
							</TD>
							<TD><ct:FWLabel key="JSP_HEURES_SEMAINE"/></TD>
							<TD><INPUT type="text" name="heuresSemaineRevenuReadaptation" value="<%=viewBean.loadRevenuReadaptation().getNbHeuresSemaine()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNEE_NIVEAU"/></TD>
							<TD colspan="5"><INPUT type="text" name="anneeNiveauRevenuReadaptation" value="<%=viewBean.loadRevenuReadaptation().getAnnee()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6" style="padding-top: 10px"><B><ct:FWLabel key="JSP_MONTANT_RENTE_EN_COURS"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT"/></TD>
							<TD><INPUT type="text" name="montantRenteEnCours" value="<%=viewBean.getMontantRenteEnCours()==null?"":viewBean.getMontantRenteEnCours()%>"></TD>
							<TD><ct:FWLabel key="JSP_ECHELLE"/></TD>
							<TD><INPUT type="text" name="echelle" value="<%=viewBean.getEchelle()%>" maxlength="2" class="numeroCourt"></TD>
							<TD><ct:FWLabel key="JSP_RAM"/></TD>
							<TD><INPUT type="text" name="ram" value="<%=viewBean.getRam()%>" maxlength="8"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_CODE_CS"/></TD>
							<TD><INPUT type="text" name="codesCasSpecial" value="<%=viewBean.getCodesCasSpecial()%>" onblur="checkCodesCasSpecial(this, this.value)"></TD>
							<TD><ct:FWLabel key="JSP_ETAT"/></TD>
							<TD><INPUT type="text" name="anneeRenteEnCours" value="<%=viewBean.getAnneeRenteEnCours()%>" maxlength="4" class="numeroCourt"></TD>
							<TD><ct:FWLabel key="JSP_MESURE_READAPTATION_8A"/>
							<TD><INPUT type="checkbox" name="mesureReadaptation8a" <%=viewBean.getMesureReadaptation8a().booleanValue()?"CHECKED":""%>></TD>
						</TR>

				<%if (viewBean instanceof globaz.ij.vb.prononces.IJGrandeIJPViewBean){
					globaz.ij.vb.prononces.IJGrandeIJPViewBean grandeIJViewBean = (globaz.ij.vb.prononces.IJGrandeIJPViewBean)viewBean;%>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_INCAPACITE_POURCENT"/></TD>
							<TD colspan="5"><INPUT type="text" name="pourcentDegreIncapaciteTravail" value="<%=grandeIJViewBean.getPourcentDegreIncapaciteTravail()%>"></TD>
						</TR>

					</TBODY>
					<TBODY id="revision3">
						<TR>
							<TD><ct:FWLabel key="JSP_MONTANT_INDEMNITE_ASSISTANCE"/></TD>
							<TD><INPUT type="text" name="montantIndemniteAssistance" value="<%=grandeIJViewBean.getMontantIndemniteAssistance()%>"></TD>
							<TD><ct:FWLabel key="JSP_ALLOCATION_EXPLOITATION"/></TD>
							<TD colspan="3"><INPUT type="checkbox" name="indemniteExploitation" <%=grandeIJViewBean.getIndemniteExploitation().booleanValue()?"CHECKED":""%>></TD>
						</TR>
				<%} %>

						<input type="checkbox"
	   name="afficheWarning"
	   style="display:none"
	   id="afficheWarning"
	   value="<%=viewBean.getAfficheWarning().booleanValue()%>" />

<% if(!JadeStringUtil.isEmpty(viewBean.getWarningMessage())){ %>
	<div style="display:none" align="center" id="dialog_warn"
		 title="<ct:FWLabel key='JSP_ATTENTION'/>" >
		<h3> </h3>
	</div>
<% } %>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/> (alt+<ct:FWLabel key="AK_PRO_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_PRO_ARRET"/>">
				<INPUT type="button" value="<ct:FWLabel key="JSP_SUIVANT"/> (alt+<ct:FWLabel key="AK_PRO_SUIVANT"/>)" onclick="if(validate()) action(COMMIT);" accesskey="<ct:FWLabel key="AK_PRO_SUIVANT"/>">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%if (viewBean instanceof globaz.ij.vb.prononces.IJGrandeIJPViewBean){%>
	<SCRIPT language="javaScript">
		document.forms[0].elements('dateDebutPrononce').onblur=function() {checkRevision();};
	</SCRIPT>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>