<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.modeles.*,globaz.helios.db.comptes.*" %>

<%
	idEcran = "GCF4012";
	CGGestionModeleViewBean viewBean = (CGGestionModeleViewBean) session.getAttribute ("viewBean");

	globaz.helios.db.comptes.CGExerciceComptable exerciceComptable = viewBean.getLastExercice();

	selectedIdValue = viewBean.getIdEnteteModeleEcriture();
   	userActionValue = "helios.parammodeles.gestionModele.modifier";

	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE")) {
		aucun = "Kein";
	}

	java.util.Vector centreChargeListe = globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, exerciceComptable.getIdMandat());
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

shortKeys[107] = "plus";

var nextRowToShow = <%=viewBean.getShowRows()%>;
var maxRows = <%=viewBean.getMaxRows()%>;

function add() {
	document.forms[0].elements('userAction').value="helios.parammodeles.gestionModele.ajouter";
	disableCentreCharge();
}

function upd() {
	document.forms[0].elements('userAction').value="helios.parammodeles.gestionModele.modifier";
	disableCentreCharge();
}

function validate() {
    state = validateFields();
    return state;
}

function cancel() {
  document.forms[0].elements('userAction').value="back";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.parammodeles.gestionModele.supprimer";
        document.forms[0].submit();
    }
}
function init(){
	hideRows();
	showMontantEtranger();

	self.focus();

    updateSum();
}


function showMontantEtranger() {
	for (i=0; i<maxRows; i++) {
		if (document.getElementById("me" + i) != null && document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != "0.00") {
			document.getElementById("me" + i).className = "montantShort";
			document.getElementById("me" + i).disabled = false;
			document.getElementById("me" + i).readOnly = false;
		}

		if (document.getElementById("c" + i) != null && document.getElementById("c" + i).value != null && document.getElementById("c" + i).value != "0.00") {
			document.getElementById("c" + i).className = "montantShort";
			document.getElementById("c" + i).disabled = false;
			document.getElementById("c" + i).readOnly = false;
		}
	}
}
function hideRows() {
	for (i=nextRowToShow; i<maxRows; i++) {
		document.getElementById("ligne" + i).style.display = "none";
	}
}



function showNextRow() {
	if (nextRowToShow < maxRows) {
		document.getElementById("ligne" + nextRowToShow).style.display = "block";
		nextRowToShow ++;
	}
}

function focusOnNextCompte() {
	for (i=0; i<maxRows; i++) {
		if (document.getElementById("idext" + i).value == "") {
			self.focus();
   			document.getElementById("idext" + i).focus();

   			return;
		}
	}
}

function updateCompte(tag, i) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idc" + i).value = element.idCompte;

		if (element.idNature == <%=CGCompte.CS_CENTRE_CHARGE%>) {
			document.getElementById("idcc" + i).className = "libelle";
			document.getElementById("idcc" + i).disabled = false;
			document.getElementById("idcc" + i).className = "selectEnabled";

			if (element.defaultIdCentreCharge > 0) {
				document.getElementById("idcc" + i).value = element.defaultIdCentreCharge;
			}
		} else {
			document.getElementById("idcc" + i).className = "selectDisabled";
			document.getElementById("idcc" + i).disabled = true;
			document.getElementById("idcc" + i).value = "0";
		}

		if (element.idNature == <%=CGCompte.CS_MONNAIE_ETRANGERE%>) {
			document.getElementById("me" + i).className = "montantShort";
			document.getElementById("me" + i).disabled = false;
			document.getElementById("me" + i).readOnly = false;
			document.getElementById("c" + i).className = "montantShort";
			document.getElementById("c" + i).disabled = false;
			document.getElementById("c" + i).readOnly = false;
		} else {
			document.getElementById("me" + i).className = "montantShortDisabled";
			document.getElementById("me" + i).disabled = true;
			document.getElementById("me" + i).readOnly = true;
			document.getElementById("me" + i).value = "0.00";
			document.getElementById("c" + i).className = "montantShortDisabled";
			document.getElementById("c" + i).disabled = true;
			document.getElementById("c" + i).readOnly = true;
			document.getElementById("c" + i).value = "0.00";
		}
	}
}

function disableCentreCharge() {
	for (i=0; i<maxRows; i++) {
		if (document.getElementById("idcc" + i).value == 0) {
			document.getElementById("idcc" + i).className = "selectDisabled";
			document.getElementById("idcc" + i).disabled = true;
		}
	}
}


function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Das Konto existiert nicht.");
	}
}

function onLibelleFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert("Der Bezeichnung existiert nicht.");
	}
}

function updateSum() {
	var decote=/'/g;

	montantDebit = parseFloat('0.0');
	montantCrebit = parseFloat('0.0');
	montantEtranger = parseFloat('0.0');

	for (i=0; i<maxRows; i++) {
		if (document.getElementById("md" + i).value != null && document.getElementById("md" + i).value != '') {
			tmp = document.getElementById("md" + i).value;
			tmp = tmp.replace(decote,"");
			montantDebit += parseFloat(tmp);
		}

		if (document.getElementById("mc" + i).value != null && document.getElementById("mc" + i).value != '') {
			tmp = document.getElementById("mc" + i).value;
			tmp = tmp.replace(decote,"");
			montantCrebit += parseFloat(tmp);
		}

		if (document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != '') {
			tmp = document.getElementById("me" + i).value;
			tmp = tmp.replace(decote,"");
			montantEtranger += parseFloat(tmp);
		}
	}

	document.getElementById("sd").value = '';
	document.getElementById("sd").value = montantDebit+'';
	validateFloatNumber(document.getElementById("sd"));

	document.getElementById("sc").value = '';
	document.getElementById("sc").value = montantCrebit+'';
	validateFloatNumber(document.getElementById("sc"));

	document.getElementById("se").value = '';
	document.getElementById("se").value = montantEtranger+'';
	validateFloatNumber(document.getElementById("se"));
}

function updateIdMandat() {
	idMandat = document.getElementById("idMandat").value;

	document.location.href="<%=servletContext%><%=(mainServletPath)%>?userAction=helios.parammodeles.gestionModele.afficher&_method=add&idModeleEcriture=<%=viewBean.getIdModeleEcriture()%>&idMandat=" + idMandat;
}

function clearDebitCredit(debitOrCredit, i) {
	document.getElementById(debitOrCredit + i).value = "";
}

function fillCell(cell) {
	/**
	* touche '=' pressée
	*/
	if (event.keyCode==61 && cell.value=='') {
		if (cell.name == "pieceModele") {
			<% if (request.getParameter("pieceModele") != null) { %>
				cell.value="<%=request.getParameter("pieceModele")%>";
			<% } %>
		}

		<%
			for (int i=0; i<viewBean.getMaxRows(); i++) {
				if (request.getParameter("idext" + i) != null) {
		%>
			if (cell.name == "idext<%=i%>") {
				cell.value="<%=request.getParameter("idext" + i)%>";
			}
		<%
				}

				if (request.getParameter("l" + i) != null) {
		%>
			if (cell.name == "l<%=i%>") {
				cell.value="<%=request.getParameter("l" + i)%>";
			}
		<%
				}
			}
		%>
		event.keyCode=null;
	}
}

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un modèle d'écriture double/collective<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr>
<td width="120">Nummer</td>
<td><input type="text" name="idEnteteModeleEcriture" class="libelleDisabled" readonly value="<%=viewBean.getIdEnteteModeleEcriture()%>"/>
</td>
</tr>

<tr>
<td>Mandant
<input type="hidden" name="idModeleEcriture" value="<%=viewBean.getIdModeleEcriture()%>">
</td>
<td>
	<%
		if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getModeleIdMandat()) && (request.getParameter("_method") != null && request.getParameter("_method").equals("add"))) {
	%>
  		<%=globaz.helios.parser.CGSelectBlockParser.getMandatSelectBlock(objSession, exerciceComptable.getMandat().getIdMandat(), false, "updateIdMandat")%>
  	<%  } else { %>
  		<input type="hidden" name="idMandat" value="<%=viewBean.getIdMandat()%>"/>
  		<input type="text" name=="idMandatLibelle" value="<%=viewBean.getMandat().getLibelle()%>" class="libelleLongDisabled"/>
  	<%  } %>
</td>
</tr>

</TBODY>
</TABLE>
<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
<TBODY>

<tr>
<td align="right" height="24">
<a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" tabindex="-1" id="plus"><img tabindex="-1" src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Zeile hinzufügen"/></a>
</td>
</tr>

<tr>
<td>
	<table width="100%" cellspacing="0" class="borderBlack">
	<tr>
	<th align="left">Konto</th>
	<th align="left">Kostenstelle</th>
	<th>Bezeichnung</th>
	<th>Soll</th>
	<th>Haben</th>
	<th>Währung (&euro;,&#36;...)</th>
	<th>Kurz</th>
	</tr>

	<%
		String jspLocation = servletContext + "/heliosRoot/compte_select.jsp";
		String params = "idExerciceComptable=" + exerciceComptable.getIdExerciceComptable() + "&isMandatAVS=" + exerciceComptable.getMandat().isEstComptabiliteAVS();
		int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();

		String jspLocationLibelle = servletContext + "/heliosRoot/" + languePage + "/comptes/label_select.jsp";
		String paramsLibelle = "idMandat=" + exerciceComptable.getIdMandat() + "&langue=" + languePage;

		for (int i=0; i<viewBean.getMaxRows(); i++) {
			String style = "row";
			if (i % 2 == 1) {
				style = "rowOdd";
			}
	%>
	<tr class="<%=style%>" id="ligne<%=i%>">
	<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
	<%
        String tmpIdext = "idext" + i;
    	String tmpIdextScript = "updateCompte(tag, " + i + ");updateSum();";
    %>
	<ct:FWPopupList name="<%=tmpIdext%>" onFailure="onCompteFailure(window.event);" onChange="<%=tmpIdextScript%>" validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExt(i)%>" className="compte" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/>
	<input type="hidden" name="idc<%=i%>" value="<%=viewBean.getIdCompte(i)%>"/>
	<input type="hidden" name="ide<%=i%>" value="<%=viewBean.getIdEcriture(i)%>"/>

	<script language="JavaScript">
		element = document.getElementById("<%=("idext" + i)%>");
	  	element.onkeypress=function() {fillCell(this);}
	</script>

	</td>
	<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
	<%
        String tmpIdcc = "idcc" + i;
    %>
	<ct:FWListSelectTag name="<%=tmpIdcc%>" defaut="<%=viewBean.getIdCompteCharge(i)%>" data="<%=centreChargeListe%>"/>
	</td>
	<td style="vertical-align: middle; text-align: left;" class="mtdShortPadding">
	<%
        String tmpLibelle = "l" + i;
    %>
	<ct:FWPopupList name="<%=tmpLibelle%>" size="30" maxlength="40" onFailure="onLibelleFailure(window.event);" onChange="fillCell(this);" params="<%=paramsLibelle%>" value="<%=viewBean.getLibelle(i)%>" validateOnChange="false" className="libelle" jspName="<%=jspLocationLibelle%>" minNbrDigit="2" forceSelection="false"/>

	<script language="JavaScript">
		element = document.getElementById("<%=("l" + i)%>");
	  	element.onkeypress=function() {fillCell(this);}
	</script>

	</td>

	<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);clearDebitCredit('mc', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="md<%=i%>" value="<%=viewBean.getMontantDebit(i)%>"/></td>
	<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);clearDebitCredit('md', <%=i%>);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="mc<%=i%>" value="<%=viewBean.getMontantCrebit(i)%>"/></td>
	<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);updateSum();" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="me<%=i%>" value="<%=viewBean.getMontantEtranger(i)%>" readonly="true" disabled="true"/></td>
	<td style="vertical-align: middle; text-align: center;" class="mtdShortPadding"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="c<%=i%>" value="<%=viewBean.getCours(i)%>" readonly="true" disabled="true"/></td>
	</tr>
	<%
		}
	%>

	<tr class="somme">
	<td colspan="3" align="right" class="mtdBold">Total :</td>
	<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sd" value="<%=globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2)%>" readonly="true" disabled="true"/></td>
	<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShort" style="text-align : right" name="sc" value="<%=globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2)%>" readonly="true" disabled="true"/></td>
	<td class="mtdMontant"><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="montantShortDisabled" style="text-align : right" name="se" value="<%=globaz.globall.util.JANumberFormatter.fmt("0.00",true,true,false,2)%>" readonly="true" disabled="true"/></td>
	<td>&nbsp;</td>
	</tr>

	</table>
</td>
</tr>

<tr>
<td align="right" height="24">
<a href="#" border="noborder" onclick="showNextRow();focusOnNextCompte();" tabindex="-1"><img tabindex="-1" src="<%=request.getContextPath()%>/images/plus.jpg" border="0" title="Eine Zeile hinzufügen"/></a>
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