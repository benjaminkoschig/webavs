<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.osiris.db.mapping.CAJournalDebitViewBean"%>
<%@page import="globaz.osiris.db.mapping.CAJournalDebit"%>
<%@page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA4028"; %>
<%
	CAJournalDebitViewBean viewBean = (CAJournalDebitViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdLink();
	String jspLocationCompteCourant =  servletContext + mainServletPath + "Root/compteCourantJournalDebit_select.jsp";
	String jspLocationRubrique =  servletContext + mainServletPath + "Root/rubrique_select.jsp";
	String jspLocationCompteCG =  servletContext + "/helios" + "Root/compte_select.jsp";
	String jspLocationMandat =  servletContext + mainServletPath + "Root/mandat_select.jsp";
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
<!-- //hide this script from non-javascript-enabled browsers

function init(){
}

function add() {
}

function upd() {
}

function del() {
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="osiris.mapping.journalDebit.ajouter";
    } else {
        document.forms[0].elements('userAction').value="osiris.mapping.journalDebit.modifier";
	}
    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {
		document.forms[0].elements('userAction').value="osiris.mapping.journalDebit.afficher";
	}
}

function updateCompteCourantSrc(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
		resetCompteCourantSrc();
	} else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].compteCourantSrcDescription.value = elementSelected.CCEcran;
	}
}

function updateContrePartieSrc(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
		resetContrePartieSrc();
	} else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].contrePartieSrcDescription.value = elementSelected.rubriqueDescription;
	}
}

function updateCompteCourantDest(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
		resetCompteCourantDest();
	} else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].compteCourantDestDescription.value = elementSelected.libelleCompte;
	}
}

function updateContrePartieDest(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
		resetContrePartieDest();
	} else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].contrePartieDestDescription.value = elementSelected.libelleCompte;
	}
}

function updateMandat(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
		resetMandat();
	} else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].mandatDescription.value = elementSelected.libelleMandat;
	}
}

function resetCompteCourantSrc() {
	document.forms[0].compteCourantSrcDescription.value="";
}
function resetContrePartieSrc() {
	document.forms[0].contrePartieSrcDescription.value="";
}
function resetCompteCourantDest() {
	document.forms[0].compteCourantDestDescription.value="";
}
function resetContrePartieDest() {
	document.forms[0].contrePartieDestDescription.value="";
}
function resetMandat() {
	document.forms[0].mandatDescription.value="";
}

top.document.title = "détail - " + top.location.href;
// stop hiding -->
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>D&eacute;tail du mapping<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
  	<tr>
		<td>Compte courant source</td>
		<td>
			<ct:FWPopupList name="compteCourantSrc"
				onFailure="resetCompteCourantSrc();"
				onChange="updateCompteCourantSrc(tag.select);"
				value='<%=viewBean.getCompteCourantSrc()%>'
				className="libelle"
				jspName="<%=jspLocationCompteCourant%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"/>
			<input type="text" name="compteCourantSrcDescription" size="60" value='<%=viewBean.getCompteCourantSrcDescription()%>' class="libelleLongDisabled"  readonly tabindex="-1">
		</td>
	</tr>
  	<tr>
		<td>Rubrique source</td>
		<td>
			<ct:FWPopupList name="contrePartieSrc"
				onFailure="resetContrePartieSrc();"
				onChange="updateContrePartieSrc(tag.select);"
				value='<%=viewBean.getContrePartieSrc()%>'
				className="libelle"
				jspName="<%=jspLocationRubrique%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"/>
			<input type="text" name="contrePartieSrcDescription" size="60" value='<%=viewBean.getContrePartieSrcDescription()%>' class="libelleLongDisabled"  readonly tabindex="-1">
		</td>
	</tr>
	<tr>
	  <td nowrap>Mandat destination</td>
	  <td nowrap>
		<ct:FWPopupList name="idMandat"
			onFailure="resetMandat();"
			onChange="updateMandat(tag.select);"
			value='<%=viewBean.getIdMandat()%>'
			className="libelle"
			jspName="<%=jspLocationMandat%>"
			minNbrDigit="1"
			forceSelection="true"
			validateOnChange="false"/>
		<input type="text" name="mandatDescription" size="60" value='<%=viewBean.getMandatDescription()%>' class="libelleLongDisabled"  readonly tabindex="-1">
	  </td>
	</tr>
	<tr>
	  <td nowrap>Compte courant dest.</td>
	  <td nowrap>
		<ct:FWPopupList name="compteCourantDest"
			onFailure="resetCompteCourantDest();"
			onChange="updateCompteCourantDest(tag.select);"
			value='<%=viewBean.getCompteCourantDest()%>'
			className="libelle"
			jspName="<%=jspLocationCompteCG%>"
			minNbrDigit="1"
			forceSelection="true"
			validateOnChange="false"/>
		<input type="text" name="compteCourantDestDescription" size="60" value='<%=viewBean.getCompteCourantDestDescription()%>' class="libelleLongDisabled"  readonly tabindex="-1">
	  </td>
	</tr>
	<tr>
	  <td nowrap>Contre partie dest.</td>
	  <td nowrap>
		<ct:FWPopupList name="contrePartieDest"
			onFailure="resetContrePartieDest();"
			onChange="updateContrePartieDest(tag.select);"
			value='<%=viewBean.getContrePartieDest()%>'
			className="libelle"
			jspName="<%=jspLocationCompteCG%>"
			minNbrDigit="1"
			forceSelection="true"
			validateOnChange="false"/>
		<input type="text" name="contrePartieDestDescription" size="60" value='<%=viewBean.getContrePartieDestDescription()%>' class="libelleLongDisabled"  readonly tabindex="-1">
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