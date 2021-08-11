<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CAF0041";
	globaz.naos.db.releve.AFApercuReleveViewBean viewBean = (globaz.naos.db.releve.AFApercuReleveViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");

	boolean bButtonFacturer = false;
	boolean bButtonDevalider = false;

	if (viewBean.getEtat().equals(globaz.naos.translation.CodeSystem.ETATS_RELEVE_SAISIE)) {
		bButtonDelete = true;
		bButtonUpdate = true;
		bButtonFacturer = true;
	} else {
		if (viewBean.getEtat().equals(globaz.naos.translation.CodeSystem.ETATS_RELEVE_FACTURER)) {
			FAPassageManager passage = new FAPassageManager();
			passage.setForIdPassage(viewBean.getIdPassage());
			passage.setSession(viewBean.getSession());

				passage.find();

				FAPassage passage1 = (FAPassage)passage.getEntity(0);
				if (!passage1.getStatus().equalsIgnoreCase(FAPassage.CS_ETAT_COMPTABILISE)){
			bButtonDevalider = true;
			bButtonUpdate = false;
				}
				else{

		bButtonDelete = false;
		bButtonUpdate = false;
				}
		}
		}
	actionNew = servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficherPreSaisie&_method=add";
	
	viewBean.fillWarningMessage();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.musca.db.facturation.FAPassageManager"%>
<%@page import="globaz.musca.db.facturation.FAPassage"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<SCRIPT>
function add() {
	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;

	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.modifier";
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.releve.apercuReleve.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.releve.apercuReleve.modifier";
	return (exit);
}

function cancel() {
 if (document.forms[0].elements('_method').value == "add")
 	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.chercher";
 else
  	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le détail d'un relevé ! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.releve.apercuReleve.supprimer";
		document.forms[0].submit();
	}
}

function init(){
checkTypeReleve();
}

function calcul(){
	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.calculer";
	document.forms[0].submit();
}

function previous() {
	document.forms[0].elements('userAction').value="naos.releve.apercuReleve.afficherPreSaisie";
	document.forms[0].submit();
}

function facturer() {
	document.forms[0].elements('newEtat').value= "<%=globaz.naos.translation.CodeSystem.ETATS_RELEVE_FACTURER%>";


	document.getElementById("btnFacturer").disabled = true;

	if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="naos.releve.apercuReleve.ajouter";
        document.forms[0].elements('doCalculation').value="true";

    } else if (document.forms[0].elements('_method').value == "upd") {
        document.forms[0].elements('userAction').value="naos.releve.apercuReleve.modifier";
        document.forms[0].elements('doCalculation').value="true";
    } else {
        document.forms[0].elements('userAction').value="naos.releve.apercuReleve.modifier";
        document.forms[0].elements('doCalculation').value="false";
    }
	document.forms[0].submit();
}

function devalider() {
	document.forms[0].elements('newEtat').value= "<%=globaz.naos.translation.CodeSystem.ETATS_RELEVE_SAISIE%>";
    document.forms[0].elements('userAction').value="naos.releve.apercuReleve.modifier";
	document.forms[0].submit();
}

function checkTypeReleve() {
	var type = <%=viewBean.getType()%>;
	if(type==<%=globaz.naos.translation.CodeSystem.TYPE_RELEVE_DECOMP_FINAL%> || type==<%=globaz.naos.translation.CodeSystem.TYPE_RELEVE_BOUCLEMENT_ACOMPTE%>|| type==<%=globaz.naos.translation.CodeSystem.TYPE_RELEVE_DECOMP_FINAL_COMPTA%>) {
		document.all('dateReceptionBlock').style.display='block';
	} else {
		document.all('dateReceptionBlock').style.display='none';
	}
}

$(function () {
	var warningMessage = "<%=viewBean.getWarningMessage()%>";
	
	if(warningMessage.length > 0) {
		globazNotation.utils.consoleWarn(warningMessage,'<ct:FWLabel key="RELEVE_AVERTISSEMENT" />', true);
	}
});


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Relev&eacute; - D&eacute;tail<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD nowrap height="31">Affili&eacute;</TD>
						<TD nowrap >
							<INPUT type="hidden" name="apercuReleve" value='details'>
							<INPUT type="hidden" name="selectedId" value='<%=viewBean.getIdReleve()%>'>
							<INPUT type="hidden" name="newEtat" value='<%=viewBean.getEtat()%>'>
							<INPUT type="hidden" name="doCalculation" value='true'>
							<!--INPUT type="hidden" name="idTiers"    value='<=viewBean.getIdTiers()>'-->
						</TD>
						<!--TD nowrap colspan="4">
						<%
							String 	designation1 = "";
							String 	designation2 = "";
							if ( viewBean.getTiers() != null) {
								designation1 = viewBean.getTiers().getDesignation1();
								designation2 = viewBean.getTiers().getDesignation2();
							}
						%>
							<INPUT type="text" name="affilieNumeroReadOnly"  size="10" value="<%=viewBean.getAffilieNumero()%>" readonly="readonly" tabindex="-1" class="Disabled">
							<INPUT type="text" name="descriptionTiers1" value="<%=designation1%>" size="38" readonly="readonly" tabindex="-1" class="Disabled">
							<INPUT type="text" name="descriptionTiers2" value="<%=designation2%>" size="38" readonly="readonly" tabindex="-1" class="Disabled">
						</TD-->
						<TD nowrap>
								<% if(viewBean.getTiers().idTiersExterneFormate().length()!=0) { %>
									<INPUT type="text" name="affilieNumeroReadOnly" size="18" maxlength="18" value="<%=viewBean.getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
									<input type="text" name="idExterne" size="18" maxlength="18" value="<%=viewBean.getTiers().idTiersExterneFormate()%>" readOnly tabindex="-1" class="disabled">
								<% } else { %>
									<INPUT type="text" name="affilieNumeroReadOnly" size="40" maxlength="40" value="<%=viewBean.getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
								<% } %>


								<br>
									<INPUT type="text" name="nomTiers" value="<%=viewBean.getTiers().getNom()%>" size="40" readonly="readonly" tabindex="-1" class="Disabled">
									<!--INPUT type="text" name="descriptionTiers2" value="<%=viewBean.getTiers().getDesignation2()%>" size="38" readonly="readonly" tabindex="-1" class="Disabled"-->
						</TD>
						<!--TD nowrap colspan="3">
							<TABLE border="0" cellspacing="0" cellpadding="0">
								<TBODY>
								<TR>
							<TD nowrap width="150">Caisse</TD>
							<TD nowrap>
								<INPUT type="text" name="noCaisse" size="3" value="123" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>
							</TR>
							</TBODY>
							</TABLE>
						</TD-->
					</TR>
					<TR>
						<TD nowrap height="31">Type de décompte</TD>
						<TD nowrap></TD>
						<TD nowrap>
							<INPUT type="text" name="typeReadOnly" size="20" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session, viewBean.getType())%>" readonly="readonly" tabindex="-1" class="disabled">
						</TD>
						<TD nowrap colspan="3">
							<TABLE border="0" cellspacing="0" cellpadding="0">
							<TBODY>
							<TR>
						<TD nowrap width="150">N&deg; de facture</TD>
						<TD nowrap >
							<INPUT type="text" name="idExterneFacture" value="<%=viewBean.getIdExterneFacture()%>" readonly="readonly" tabindex="-1" class="disabled">
						</TD>
							</TR>
							</TBODY>
							</TABLE></TD>
					</TR>

					<TR>
						<% if(CodeSystem.TYPE_RELEVE_SALAIRE_DIFFERES.equals(viewBean.getType())) {%>
					    <TD height="31" width="150">Année de référence pour le taux</TD>
					    <TD nowrap width="30"></TD> 	
						<TD height="31" width="30"><INPUT name="anneeReference" id="anneeReference" size="4" maxlength="4" class="Disabled" readonly="readonly" tabindex="-1" value="<%= viewBean.getAnneeReference() %>" ></TD>    
						<TD colspan="3">&nbsp;</TD>
						<% } else { %>
						<TD colspan="6">&nbsp;</TD>
						<% } %>
					</TR>
					<TR>
						<TD height="31">P&eacute;riode </TD>
						<TD align="right">du&nbsp;</TD>
						<TD>
							<INPUT type="text" name="dateDebutReadOnly"  size="10" value="<%=viewBean.getDateDebut()%>" readonly="readonly" tabindex="-1" class="Disabled">
							&nbsp;au&nbsp;
							<INPUT type="text" name="dateFinReadOnly"   size="10"  value="<%=viewBean.getDateFin()%>" readonly="readonly" tabindex="-1" class="Disabled">
						</TD>
						<TD nowrap colspan="3">
							<TABLE border="0" cellspacing="0" cellpadding="0">
							<TBODY>
							<TR>
						<TD nowrap width="150">Collaborateur</TD>
						<TD nowrap>
							<INPUT type="text" name="collaborateur" value="<%=viewBean.getCollaborateur()%>" readonly="readonly" tabindex="-1" class="Disabled">
						</TD>
							</TR>
							</TBODY>
							</TABLE></TD>
					</TR>







					<TR>
						<TD nowrap height="31">Int&eacute;r&ecirc;ts</TD>
						<TD nowrap></TD>
						<TD nowrap>
	            			<%
				     			java.util.HashSet except = new java.util.HashSet();
				            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
				            	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_A_CONTROLER);
				            %>

							<ct:FWCodeSelectTag
                				name="interets"
								defaut="<%=viewBean.getInterets()%>"
								codeType="OSIIMINMO"
								except="<%=except%>"
								/>
						</TD>
						<TD nowrap colspan="3">
							<TABLE border="0" cellspacing="0" cellpadding="0" id="dateReceptionBlock">
							<TBODY>
							<TR>
							<TD nowrap width="150">Date de réception</TD>
							<TD nowrap>
								<ct:FWCalendarTag name="dateReception" value="<%=viewBean.getDateReception()%>" />
							</TD>
							</TR>
							</TBODY>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD height="31">Etat</TD>
						<TD></TD>
						<TD>
						<INPUT type="hidden" name="etat" value="<%=viewBean.getEtat()%>">
						<INPUT type="text" name="etatLibelle"
							value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getEtat())%>"
							readonly="readonly"  tabindex="-1" class="disabled">
						</TD>
						<TD nowrap colspan="3">
							<TABLE border="0" cellspacing="0" cellpadding="0">
							<TBODY>
							<TR>
						<TD nowrap width="150">
						<%
	              			if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdPassage()) && !"add".equals(method)) {
	              		%>
	              		<ct:ifhasright element="musca.facturation.passage.afficher" crud="r">
	              		<A href="<%=request.getContextPath()%>/musca?userAction=musca.facturation.passage.afficher&selectedId=<%=viewBean.getIdPassage()%>" class="external_link">Journal de facturation</A>
	              		</ct:ifhasright>
	              		<%
	              			} else {
	              		%>
							Journal de facturation
	              		<%
	              		}
	          			%>
						</TD>
						<TD nowrap>
							<INPUT type="text" name="numJob" value="<%=viewBean.getIdPassage()%>" readonly="readonly" tabindex="-1" class="disabled">
						</TD>
							</TR>
							</TBODY>
							</TABLE></TD>
					</TR>
					<TR>
						<TD nowrap colspan="6" height="14">
							<HR>
						</TD>
					</TR>
					<TR>
						<TD nowrap height="31">Total de contr&ocirc;le</TD>
						<TD nowrap></TD>
						<TD nowrap>
							<INPUT type="text" name="totalControl" size="19" value="<%=viewBean.getTotalControl()%>" style="text-align : right;">
						</TD>
							<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdPassage()) && CodeSystem.ETATS_RELEVE_FACTURER.equals(viewBean.getEtat()) && !"add".equals(method)) { %>

							<TD
							<ct:ifhasright element="musca.facturation.afact.chercher" crud="r">
							nowrap width="161"><A href="<%=request.getContextPath()%>\musca?userAction=musca.facturation.afact.chercher&idEnteteFacture=<%=viewBean.getIdEntete()%>&idPassage=<%=viewBean.getIdPassage()%>&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Création afacts</A>
							 </ct:ifhasright>
							</TD>
							<% } %>
					</TR>
					<TR>
						<TD colspan="7" height="14"><%if(viewBean.isSalaireDifferesPresent()) { %><p style ="color : #ff0000; text-align: center; font-size:15px" ><ct:FWLabel key="WARNING_SALAIRE_DIFFERE_EXISTANTE"/></p><%} %>&nbsp;</TD>
					</TR>
					<TR>
					<%
						if (viewBean.getCotisationList().size() > 0) {
					%>
						<TD nowrap height="31" colspan="3"><b>Assurance de l'affili&eacute;</b></TD>
						<TD nowrap><b>Masse/Montant</b></TD>
						<TD nowrap><b>Taux</b></TD>
						<TD nowrap><b>Montant de la cotisation</b></TD>
					<%
						java.util.List cotisationList = viewBean.getCotisationList();
						String idPlan = "";
						for (int i=0; i <cotisationList.size(); i++) {
							globaz.naos.db.releve.AFApercuReleveLineFacturation cotisation =
								(globaz.naos.db.releve.AFApercuReleveLineFacturation)cotisationList.get(i);
								if(!idPlan.equals(cotisation.getIdPlan())) {
					%>
					<TR><TD colspan="7" height="31"> <%=cotisation.getLibelleInfoRom280()%></TD></TR>
					<%				idPlan = cotisation.getIdPlan();
					 			} %>
					<TR>
						<TD nowrap height="31" colspan="3" nowrap>
							<INPUT type="hidden" name="assuranceId<%=i%>" value='<%=cotisation.getAssuranceId()%>'>
							&nbsp;&nbsp;<INPUT type="text" name="libelleAssurance<%=i%>" size="55"
								value="<%=cotisation.getAssuranceLibelle((globaz.globall.db.BSession)globaz.naos.translation.CodeSystem.getSession(session))%>"
								readonly="readonly" tabindex="-1" class="Disabled">
							<!--INPUT type="text" name="noCaisse<%=i%>" size="3" value="123" readonly="readonly" tabindex="-1" class="Disabled"-->

						</TD>
					<%
						if (viewBean.getEtat().equals(globaz.naos.translation.CodeSystem.ETATS_RELEVE_SAISIE)) {
							if (globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE.equals(cotisation.getGenreAssurance()) &&
								!globaz.naos.translation.CodeSystem.TYPE_CALCUL_COTISATION.equals(cotisation.getTypeCalcul())) {
								if (cotisation.getTauxGenre().equals(globaz.naos.translation.CodeSystem.GEN_VALEUR_ASS_MONTANT)) {
					%>
								<TD nowrap>
									<INPUT type="text" name="masse<%=i%>" size="20" value="<%=cotisation.getMasseString(viewBean.isFirstCalculation())%>"
										style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
								</TD>
								<TD nowrap>
									<INPUT type="text" name="dateDebutMontant<%=i%>" size="10" value="<%=cotisation.getDebutPeriode()%>"
										readonly="readonly" tabindex="-1" class="Disabled">
									<INPUT type="text" name="dateFinMontantReadOnly" size="10" value="<%=cotisation.getFinPeriode()%>"
										readonly="readonly" tabindex="-1" class="Disabled">
								</TD>
					<% 			} else { %>
								<TD nowrap>
									<% if(globaz.naos.translation.CodeSystem.TYPE_CALCUL_MONTANT_FIXE.equals(cotisation.getTypeCalcul())) {%>
									<INPUT type="text" name="montantFixe<%=i%>" size="20" value="<%=cotisation.getMontantFixeString(viewBean.isFirstCalculation())%>"
										   style="text-align : right;" readonly="true" tabindex="-1" class="Disabled">
									<% } else {%>
									<INPUT type="text" name="masse<%=i%>" size="20" value="<%=cotisation.getMasseString(viewBean.isFirstCalculation())%>"
										style="text-align : right;">
									<% } %>
								</TD>
								<TD nowrap>
									<INPUT type="text" name="taux<%=i%>" size="8" value="<%=cotisation.getTauxString()%>"
										style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">%
								</TD>
					<%			}
							} else { %>
							<TD nowrap>
							<INPUT type="text" name="masse<%=i%>Dummy" size="18" value=""
								style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>
							<TD nowrap>
								<INPUT type="text" name="taux<%=i%>Dummy" size="8" value=""
									style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>
					<% 		} %>
						<TD nowrap>
							<INPUT type="text" name="montantCalculer<%=i%>" size="20" value="<%=cotisation.getMontantCalculerString()%>" style="text-align : right;">
						</TD>
					<% 	} else {
							if (globaz.naos.translation.CodeSystem.GENRE_ASS_PARITAIRE.equals(cotisation.getGenreAssurance())) { %>
							<TD nowrap>
								<INPUT type="text" name="masse<%=i%>" size="20" value="<%=cotisation.getMasseString(viewBean.isFirstCalculation())%>"
									style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>
							<TD nowrap>
								<INPUT type="text" name="taux<%=i%>" size="8" value="<%=cotisation.getTauxString()%>"
									style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">%
							</TD>
							<TD nowrap>
								<INPUT type="text" name="montantCalculer<%=i%>" size="20" value="<%=cotisation.getMontantCalculerString()%>"
									style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>
							<% } else { %>
							<TD nowrap>
								<INPUT type="text" name="masse<%=i%>Dummy" size="20" value=""
									style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>
							<TD nowrap>
								<INPUT type="text" name="taux<%=i%>Dummy" size="8" value=""
									style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>

							<TD nowrap>
								<INPUT type="text" name="montantCalculer<%=i%>" size="20" value="<%=cotisation.getMontantCalculerString()%>"
									style="text-align : right;" readonly="readonly" tabindex="-1" class="Disabled">
							</TD>
					<% }}}} %>
					</TR>
					<TR>
						<TD colspan="6" height="14">
							<HR>
						</TD>
					</TR>
					<TR>
						<TD nowrap colspan="3">&nbsp;</TD>
						<TD nowrap height="31">
						<%=viewBean.isMontantNegatif() ? "<span style='font-weight: bold; color: red;'>Attention négatif</span>" : ""%>	<INPUT class="btnCtrl" id="btnCalcul" type="button" value="Calculer" onclick="calcul();">
						</TD>
						<TD nowrap>Total</TD>
						<TD nowrap>
							<INPUT type="text" name="totalCalculerReadOnly" size="20" value="<%=viewBean.getTotalCalculer()%>"
								readonly="readonly" tabindex="-1" class="Disabled" style="text-align : right;">
						</TD>

					</TR>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if (objSession.hasRight("naos.releve.apercuReleve.afficherPreSaisie",FWSecureConstants.ADD)){ %>
				<%if ((method != null) && (method.equalsIgnoreCase("ADD"))) { %><INPUT class="btnCtrl" id="btnPrevious" type="button" value="<<" onclick="previous();" >
				<% } else { %><INPUT class="btnCtrl" type="button" id="btnNew" value="<%=btnNewLabel%>" onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='<%=actionNew%>'" ><% } %>
				<% if (bButtonFacturer) {%><INPUT class="btnCtrl" id="btnFacturer" type="button" value="Facturer" onclick="facturer();" ><% } %>
				<% if (bButtonDevalider) {%><INPUT class="btnCtrl" id="btnDevalider" type="button" value="D&eacute;valider" onclick="devalider();" ><% } %>
				<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>