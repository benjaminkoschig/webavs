<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.apg.application.APApplication"%>
<%@ page import="globaz.apg.vb.droits.APRecapitulatifDroitPaiViewBean" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP0014";

APRecapitulatifDroitPaiViewBean viewBean = (APRecapitulatifDroitPaiViewBean) session.getAttribute("viewBean");

selectedIdValue = viewBean.getIdDroit();

bButtonCancel = true;
bButtonValidate = false;
bButtonDelete = viewBean.isModifiable() && bButtonDelete;
bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
bButtonNew = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="<%=viewBean.resolveMenuPrincipale()%>"/>
<ct:menuChange displayId="options" menuId="<%=viewBean.resolveMenuOptionDroit()%>" showTab="options">
	<ct:menuSetAllParams key="forIdDroit" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="genreService" value="<%=viewBean.getGenreService()%>"/>
</ct:menuChange>

<SCRIPT>


  function add() {
    // pas cense y arriver
  }

  function upd() {
  	document.forms[0].elements('userAction').value = "<%=viewBean.action(session)%>.afficher";
  	document.forms[0].submit();
  }

  function validate() {
    // pas cense y arriver
  }

  function cancel() {
    document.forms[0].elements('userAction').value="apg.droits.droitLAPGJointDemande.chercher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=viewBean.action(session)%>.supprimer";
        document.forms[0].submit();
    }
  }

  function init(){
  }

  function simulerPaiement(){
  	document.forms[0].elements('userAction').value = "apg.droits.droitLAPGJointDemande.simulerPaiementDroit";
  	document.forms[0].submit();
  }

  function popupClose(){
	  $html.dialog( "close" );
  }

  function popupSubmit(){
	  var pidAnnonce = $('#pidAnnoncePopup').val();
	  $('#pidAnnonce').val(pidAnnonce);
	  popupClose();
	  simulerPaiement();
  }

  function simulerPaiementAvecPID(){
	  	var html = '<div>';
		html += '<div><input id="pidAnnoncePopup" name="pidAnnoncePopup" type="text" style="min-width:200px;" /></div>';
		html += '<div  style="font-size:10px; margin-top: 12px;">';
		html += '<strong>Saisie du BusinessProcessID (BPID) : </strong></br>';
		html += "Le numéro de caisse ou d'agence ne doivent pas pré-fixer le BPID</br>";
		html += "Exemple : 1xx.002.<strong>000062414</strong> donne le BPID : <strong>000062414</strong> (Saisir les '0')</br>";
		html += '</div></div>';

	  	$html = $(html);
	 	var inputField = $html.find('#pidAnnoncePopup');
		inputField.prop("disabled",true);
		inputField.css("background-color", "#DDD");

	  	$html.dialog({
			position: 'center',
			title: "Saisie du BPID de l'annonce à créer",
			width: 400,
			height: 350,
			show: "blind",
			hide: "blind",
			closeOnEscape: true,
			buttons: {'Ok':popupSubmit, 'Close':popupClose}
		});

	  	// ACK LGA avril 2013... Lors de l'affichage de la popup, le focus est bien dans le champs input
	  	// mais disparaît après environ 1 sec... Du coup.....
	  	setTimeout(function (){
	  		inputField.prop("disabled",false)
	  		inputField.css("background-color", "#FFF");
	  		inputField.focus();
	  	},1200)

  }

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdDroit()%>" tableSource="<%=APApplication.KEY_POSTIT_DROIT_APG_MAT%>"/></span>
			<ct:FWLabel key="<%=viewBean.resolveTitle(session)%>"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_NO_DROIT"/></TD>
							<TD><INPUT TYPE="text" NAME="" VALUE="<%=viewBean.getNoDroit()%>" CLASS="numeroCourtDisabled" READONLY></TD>
							<TD COLSPAN="4">
								<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdDroit()%>">
							</TD>
						</TR>
						<TR><TD COLSPAN="6">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ASSURE"/></TD>
							<TD COLSPAN="5">
								<%=viewBean.getDetailRequerantListe()%>&nbsp;/&nbsp;
								<A href="#" onclick="window.open('<%=servletContext%><%=("/apg")%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>')" ><ct:FWLabel key="JSP_GED"/></A>
								&nbsp;/&nbsp;<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getTiers()%>" ><ct:FWLabel key="JSP_TIERS" /></A>
							</TD>
						</TR>
						<TR><TD COLSPAN="6">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_TYPE_DROIT"/></TD>
							<TD COLSPAN="5"><ct:FWCodeSelectTag codeType="APGENSERVI" defaut="<%=viewBean.getGenreService()%>" name=" "/></TD>
						</TR>
						<TR><TD COLSPAN="6">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_DATE_DEBUT"/></TD>
							<TD><INPUT TYPE="text" NAME="" VALUE="<%=viewBean.getDateDebutDroit()%>" CLASS="dateDisabled" READONLY></TD>
							<TD><ct:FWLabel key="JSP_DATE_REPRISE_ACTIVITE"/></TD>
							<TD><INPUT TYPE="text" NAME="" VALUE="<%=viewBean.getDateRepriseActivite()%>" CLASS="dateDisabled" READONLY></TD>
							<TD><ct:FWLabel key="JSP_DATE_DECES"/></TD>
							<TD><INPUT TYPE="text" NAME="" VALUE="<%=viewBean.getDateDeces()%>" CLASS="dateDisabled" READONLY></TD>
						</TR>
						<TR><TD COLSPAN="6">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_EMPLOYEURS"/></TD>
							<TD COLSPAN="5">
								<TABLE BORDER="3">
									<THEAD>
										<TR>
											<TH><ct:FWLabel key="JSP_NO_AFFILIE"/></TH>
											<TH><ct:FWLabel key="JSP_EMPLOYEUR"/></TH>
											<TH><ct:FWLabel key="JSP_VERSEMENT_ASSURE"/></TH>
										</TR>
									</THEAD>
									<TBODY>
									<%
									int count = 0;

									for (java.util.Iterator employeurs = viewBean.getEmployeurs().iterator(); employeurs.hasNext(); ++count) {
										if (count > 5) { %>
										<TR>
											<TD style="text-align: center" colspan="3">...</TD>
										</TR>
										<%
											break;
										} else {
										globaz.apg.db.droits.APSitProJointEmployeur employeur = (globaz.apg.db.droits.APSitProJointEmployeur) employeurs.next();
									%>
										<TR>
											<TD><%=employeur.getNoAffilie()%></TD>
											<TD><%=employeur.getNom()%></TD>
											<TD style="text-align: center">
												<IMG SRC="<%=request.getContextPath()+(!employeur.getIsVersementEmployeur().booleanValue()?"/images/ok.gif":"/images/erreur.gif")%>" ALT="">
											</TD>
											<TD><A href="#" onclick="window.open('<%=servletContext%><%=("/apg")%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.actionAfficherDossierGed&amp;noAffiliationId=<%=employeur.getNoAffilie()%>&amp;noAVSId=<%=employeur.retrieveNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED_COTI)%>')" ><ct:FWLabel key="JSP_GED"/></A>&nbsp;/&nbsp;
												<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=employeur.getIdTiers()%>" ><ct:FWLabel key="JSP_TIERS" /></A></TD>
										</TR>
									<% }} %>
									</TBODY>
								</TABLE>
							</TD>
						</TR>
						<TR><TD COLSPAN="6">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ENFANTS"/></TD>
							<TD COLSPAN="5">
								<TABLE BORDER="3">
									<THEAD>
										<TR>
											<TH><ct:FWLabel key="JSP_NSS_ABREGE"/></TH>
											<TH><ct:FWLabel key="JSP_NOM"/></TH>
											<TH><ct:FWLabel key="JSP_PRENOM"/></TH>
											<TH><ct:FWLabel key="JSP_DATE_NAISSANCE"/></TH>
										</TR>
									</THEAD>
									<TBODY>
									<%
									count = 0;

									for (java.util.Iterator enfants = viewBean.getEnfants().iterator(); enfants.hasNext(); ++count) {
										if (count > 5) { %>
										<TR>
											<TD style="text-align: center" colspan="4">...</TD>
										</TR>
										<%
											break;
										} else {
										globaz.apg.db.droits.APEnfantPat enfant = (globaz.apg.db.droits.APEnfantPat) enfants.next();
									%>
										<TR>
											<TD><%=enfant.getNoAVS()%></TD>
											<TD><%=enfant.getNom()%></TD>
											<TD><%=enfant.getPrenom()%></TD>
											<TD><%=enfant.getDateNaissance()%></TD>
										</TR>
									<% }} %>
									</TBODY>
								</TABLE>
							</TD>
						</TR>
						<TR><TD COLSPAN="6">&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ETAT_DROIT"/></TD>
							<TD COLSPAN="5"><ct:FWCodeSelectTag codeType="APETADROIT" defaut="<%=viewBean.getEtat()%>" name=" "/></TD>
						</TR>
						<tr>
							<td colspan="2">
								<input type="hidden" id="pidAnnonce" name="pidAnnonce" value="" />
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%--
				Fonction pas implémenter pour les proches aidant
				<%
				if(viewBean.isModifiable()){
					if(viewBean.isAdministrateur() && controller.getSession().hasRight(IAPActions.ACTION_DROIT_LAPG, FWSecureConstants.UPDATE)){ %>
						<%if(viewBean.isAfficherBoutonSimulerPmtBPID()){ %>
							<input type="button" onclick="simulerPaiementAvecPID()" value="<ct:FWLabel key="MENU_OPTION_SIMULER_PAIEMENT_DROIT_AVEC_PID"/>" />
						<% } else {%>
							<input type="button" name="simulerPaiement" onclick="simulerPaiement()" value="<ct:FWLabel key="MENU_OPTION_SIMULER_PAIEMENT_DROIT"/>">
						<% }
					}
				}%>--%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
