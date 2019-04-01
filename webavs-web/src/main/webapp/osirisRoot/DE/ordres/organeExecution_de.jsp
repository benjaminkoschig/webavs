<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4020"; %>
<%
	globaz.osiris.db.ordres.CAOrganeExecutionViewBean viewBean = (globaz.osiris.db.ordres.CAOrganeExecutionViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdOrganeExecution();
	String jspLocation =  servletContext + mainServletPath + "Root/rubrique_select.jsp";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init(){}

function add() {
}

function upd() {
}

function del() {
	if (window.confirm("Sie sind dabei, den ausgewählten Sammelauftrag zu löschen ! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.ordres.organeExecution.supprimer";
        document.forms[0].submit();
    }
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.ordres.organeExecution.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.ordres.organeExecution.modifier";

    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.ordres.organeExecution.afficher";
}

top.document.title = "Auftrag - Detail eines Ausführungsorgans - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail eines Ausführungsorgans<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<tr>
	  <td nowrap>Nummer</td>
	  <td nowrap>
	    <input type="text" name="idOrganeExecution"	value="<%=viewBean.getIdOrganeExecution()%>" class="libelleDisabled" tabindex="-1" readonly size="20" maxlength="15">
	  </td>
	</tr>

	<tr>
	  <td nowrap>Name</td>
	  <td nowrap>
	    <input type="text" name="nom" size="40" maxlength="40" value="<%=viewBean.getNom()%>">
	  </td>
	</tr>

  	<tr>
		<td>Rubrik</td>
		<td><ct:FWPopupList
		validateOnChange="true" value="<%=viewBean.getNumeroRubrique()%>"
		name="numeroRubrique" size="15" className="visible" jspName="<%=jspLocation%>"
		minNbrDigit="3" autoNbrDigit="11" />
		</td>
	</tr>

	<tr>
		<td>Art</td>
		<td>
			<ct:FWCodeSelectTag name="genre" defaut="<%=viewBean.getGenre()%>" codeType="OSIGENORG" wantBlank="false"/>
		</td>
	</tr>

	<tr>
		<td>Nummer der Zahlungsadresse</td>
		<td>
			<input type="text" name="idAdressePaiement" size="40" maxlength="40" value="<%=viewBean.getIdAdressePaiement()%>">
		</td>
	</tr>

	<tr>
		<td>DTA Identifizierung</td>
		<td>
			<input type="text" name="identifiantDTA" size="40" maxlength="40" value="<%=viewBean.getIdentifiantDTA()%>">
		</td>
	</tr>

	<tr>
		<td>Nummer der Adresse des Steuerausstosses</td>
		<td>
			<input type="text" name="idAdresseDebitTaxes" size="40" maxlength="40" value="<%=viewBean.getIdAdresseDebitTaxes()%>">
		</td>
	</tr>

	<tr>
		<td>Art von Behandlung BV</td>
		<td>
			<ct:FWCodeSelectTag name="idTypeTraitementBV" defaut="<%=viewBean.getIdTypeTraitementBV()%>" codeType="OSIOGRBVR" wantBlank="false"/>
		</td>
	</tr>
	<tr>
		<td>Behandlungstyp LS</td>
		<td>
			<ct:FWCodeSelectTag name="idTypeTraitementLS" defaut="<%=viewBean.getIdTypeTraitementLS()%>" codeType="OSIOGRLSV" wantBlank="false"/>
		</td>
	</tr>
	<tr>
		<td>Art von Behandlung SA</td>
		<td>
			<ct:FWCodeSelectTag name="idTypeTraitementOG" defaut="<%=viewBean.getIdTypeTraitementOG()%>" codeType="OSIOGROG" wantBlank="false"/>
		</td>
	</tr>
	<tr>
		<td>Name des parser ESR</td>
		<td>
			<input type="text" name="nomClasseParserBvr" size="40" value="<%=viewBean.getNomClasseParserBvr()%>">
		</td>
	</tr>
	<tr>
		<td>Nummer des Mitgliedes ESR</td>
		<td>
			<input type="text" name="noAdherentBVR" size="40" maxlength="40" value="<%=viewBean.getNoAdherentBVR()%>">
		</td>
	</tr>

	<tr>
		<td>Name des parser LSV</td>
		<td>
			<input type="text" name="nomClasseParserLSV" size="40" value="<%=viewBean.getNomClasseParserLSV()%>">
		</td>
	</tr>
	<tr>
		<td>Nummer des Mitgliedes</td>
		<td>
			<input type="text" name="noAdherent" size="40" maxlength="40" value="<%=viewBean.getNoAdherent()%>">
		</td>
	</tr>

	<tr>
		<td>LSV interne Nummer</td>
		<td>
			<input type="text" name="numInterneLsv" size="40" maxlength="40" value="<%=viewBean.getNumInterneLsv()%>">
		</td>
	</tr>

	<tr>
		<td>Übertragungsmethode</td>
		<td>
			<ct:FWCodeSelectTag name="modeTransfert" defaut="<%=viewBean.getModeTransfert()%>" codeType="OSIMODTRA" wantBlank="false"/>
		</td>
	</tr>
			<tr>
		<td>Wurzelverzeichnis</td>
		<td>
			<input type="text" name="dossierRacineChemin" value="<%=viewBean.getDossierRacineChemin()%>"  STYLE="background-color: #b3c4db;"  readonly size="40" maxlength="40"/>
		</td>
	</tr>
	<tr>
		<td>Sous r&eacute;pertoire de transfert</td>
		<td>
			<input type="text" name="sousDossierChemin" size="40" maxlength="40" value="<%=viewBean.getSousDossierChemin()%>" />
		</td>
	</tr>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>