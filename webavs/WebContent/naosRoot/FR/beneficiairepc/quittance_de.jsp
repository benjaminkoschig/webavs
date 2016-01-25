<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0065";
	AFQuittanceViewBean viewBean = (AFQuittanceViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
	String jspLocationNSS = servletContext + mainServletPath + "Root/ci_select.jsp";
	String avsState = "tabindex='-1'";
	boolean modeExtourne = true;
%>
<%@page import="globaz.naos.db.beneficiairepc.AFQuittanceViewBean"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="java.util.Vector"%>
<SCRIPT language="JavaScript">

</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.beneficiairepc.quittance.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;

	document.forms[0].elements('userAction').value="naos.beneficiairepc.quittance.modifier";
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.beneficiairepc.quittance.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.beneficiairepc.quittance.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
 		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="naos.beneficiairepc.quittance.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer une quittance! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.beneficiairepc.quittance.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}

function updateNumAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('nomBenef').value =  tag.select[tag.select.selectedIndex].nom;
	}
}

function updateForm(tag){
	field = document.forms[0].elements('nomPrenom');
	if (tag.select) {
		nom = tag.select[tag.select.selectedIndex].nom;
		field.value = nom;
		if(field.readOnly==false) {
			field.readOnly = true;
			field.className = 'disabled';
			field.tabIndex = -1;
		}
	}
}

function resetNom() {
	field = document.forms[0].elements('nomPrenom');
	field.value = '';
	field.readOnly = false;
	field.className = 'libelle';
	field.tabIndex = 0;
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
				Saisie d'une quittance
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
							<TD> <strong>Bénéficiaire de prestations complémentaires</strong></TD>
		</TR>
		<TR>
			<TD>&nbsp;</TD>
		</TR>
		<TR>
							<td>
								<table>
								<tr>
									<td>N° affili&eacute;</td>
									<td>
										<ct:FWPopupList
											name="idAffBeneficiaire"
											value="<%=viewBean.getNumAffilie()%>"
											className="libelle"
											jspName="<%=jspLocation%>"
											autoNbrDigit="<%=autoDigiAff%>"
											size="25"
											onChange="updateNumAffilie(tag);"
											minNbrDigit="6"/>
										<!--IMG
											src="<%=servletContext%>/images/down.gif"
											alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
											title="presser sur la touche 'flèche bas' pour effectuer une recherche"
											onclick="if (document.forms[0].elements('numAffilie').value != '') numAffiliePopupTag.validate();"-->
									</td>
									<td>
										<%
											Object[] tiersMethodsName= new Object[]{
												new String[]{"setNumAffilie","getNumAffilieActuel"},
												new String[]{"setIdTiers","getIdTiers"},};
											Object[] tiersParams = new Object[]{
												new String[]{"selection","_pos"},};
											String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/beneficiairepc/quittance_de.jsp";
										%>
										<ct:FWSelectorTag
											name="tiersSelector"
											methods="<%=tiersMethodsName%>"
											providerApplication ="pyxis"
											providerPrefix="TI"
											providerAction ="pyxis.tiers.tiers.chercher"
											providerActionParams ="<%=tiersParams%>"
											redirectUrl="<%=redirectUrl%>"/>
										<input type="hidden" value="<%=viewBean.getIdTiers()%>" name="idTiers">
									</td>
								</tr>
								</table>
							</td>
							<td>
								<input type="text" name="nomBenef" disabled="disabled" readonly="readonly" value="<%=viewBean.getNomBenefJSP()%>"></input>
							</td>
			</TR>

			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
							<TD> <strong>Aide de ménage</strong></TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<%if(viewBean.getNumAvs().length()<=14){%>
			<tr>
					<td colspan="4">N°AVS &nbsp;
					<nss:nssPopup
 					onChange="updateForm(tag)"
 					onFailure="resetNom()"
					name = "numAvsAideMenage"
					jspName="<%=jspLocationNSS%>"
					nssMinNbrDigit="8"
					avsAutoNbrDigit="11"
					nssAutoNbrDigit="10"
					avsMinNbrDigit="5"
					forceSelection="true"
					value="<%=viewBean.getNumAvs()%>"
					newnss="<%=viewBean.isNNSS()%>"
					 />

						<input name='nomPrenom' size="55"
							value='<%=viewBean.getNomPrenom()%>' tabindex="-1" disabled="disabled" readonly="readonly">
						</td>
			</tr>
			<%}else{%>
			<tr>
					<td colspan="4">N°AVS &nbsp;
					<nss:nssPopup
 					onChange="updateForm(tag)"
 					onFailure="resetNom()"
					name = "numAvsAideMenage"
					jspName="<%=jspLocationNSS%>"
					nssMinNbrDigit="8"
					avsAutoNbrDigit="11"
					nssAutoNbrDigit="10"
					avsMinNbrDigit="5"
					forceSelection="true"
					value="<%=viewBean.getNumAvs().substring(3,viewBean.getNumAvs().length())%>"
					newnss="<%=viewBean.isNNSS()%>"
					 />

						<input name='nomPrenom' size="55"
							value='<%=viewBean.getNomPrenom()%>' tabindex="-1" disabled="disabled" readonly="readonly">
						</td>
			</tr>
			<%}%>


			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
							<TD> <strong>Facture</strong></TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
							<TD>
								Période début
							</TD>

							<TD>
								<ct:FWCalendarTag name="periodeDebut" doClientValidation="CALENDAR" value="<%=viewBean.getPeriodeDebut()%>"/>
							</TD>
			</TR>
			<TR>
							<TD>
								Période fin
							</TD>
							<TD>
								<ct:FWCalendarTag name="periodeFin" doClientValidation="CALENDAR" value="<%=viewBean.getPeriodeFin()%>"/>
							</TD>
			</TR>
			<TR>
							<TD>
								Nombre d'heures
							</TD>
							<TD>
								<input type="text" name="nombreHeures" value="<%=viewBean.getNombreHeures()%>"></input>
							</TD>
			</TR>
			<TR>
							<TD>
								Prix de l'heure
							</TD>
							<TD>
								<input type="text" name="prixHeure" value="<%=viewBean.getPrixHeure()%>"></input>
							</TD>
			</TR>
			<TR>
							<TD>
								Total versé
							</TD>
							<TD>
								<input type="text" name="totalVerse" value="<%=viewBean.getTotalVerse()%>"></input>
							</TD>
			</TR>
			<TR>
							<TD>
								Date de valeur
							</TD>

							<TD>
								<ct:FWCalendarTag name="dateValeur" doClientValidation="CALENDAR" value="<%=viewBean.getDateValeur()%>"/>
							</TD>
							
			</TR>
			
			<TR>
				<TD>Montant pour CI</TD>
							<TD>
								<input type="text" name="montantCI" class="disabled"  value="<%=viewBean.getMontantCI()%>" readonly="readonly"></input>
							</TD>
			</TR>
			
			<% if (viewBean.getCiTraite().equals(Boolean.TRUE)) { %>
			<TR>
				<TD>Inscrit au CI automatiquement</TD>
				<TD nowrap height="31"><input type="checkbox" readonly="readonly" name="ciTraite" <%=(viewBean.getCiTraite().booleanValue())? "checked" : "unchecked"%>></TD>
			</TR>
			<% } else { %>
			<TR>
				<TD>Inscrit au CI manuellement</TD>
				<TD nowrap height="31"><input type="checkbox" name="ciManuel" <%=(viewBean.getCiManuel().booleanValue())? "checked" : "unchecked"%>></TD>
			</TR>
			<% } %>
			<TR>
				<TD>&nbsp;</TD>
			</TR>

			<TR>
				<TD> <strong>Journal des quittances</strong></TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<TD><ct:FWListSelectTag name="idJournal" data="<%=viewBean.getListeJournaux()%>" defaut="<%=viewBean.getIdJournalQuittance()%>"/></TD>
			</TR>
			<TR>
						<TD colspan="4" height="99">
						<%if(viewBean.getMessageErreurLibelle()!=null) {%>
						<textarea rows="5" style="width: 100%" readonly="readonly"><%=viewBean.getMessageErreurLibelle()%></textarea>
						<%}else{ %>
						<textarea rows="5" style="width: 100%" readonly="readonly"></textarea>
						<%} %>
						</TD>
			</TR>




			<input type="hidden" name="idLocalite" value="1"></input>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>