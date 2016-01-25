<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCF3010"; %>
<%
globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

globaz.helios.db.process.CGProcessSoldeRectificatifViewBean viewBean = (globaz.helios.db.process.CGProcessSoldeRectificatifViewBean) session.getAttribute("viewBean");
userActionValue = "helios.process.processSoldeRectificatif.executer";

String toutLexercice = "Tout l'exercice";
if (languePage.equalsIgnoreCase("de")) {
	toutLexercice = "Ganzes Rechnungsjahr";
}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

top.document.title = "Prozess - Saldo berichtigen - " + top.location.href;

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Das Konto existiert nicht.");
	}
}

function updateCompte(compte,tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];

		if (element.idCompte != "") {
			document.getElementById('idCompte').value = element.idCompte;
		} else {
			document.getElementById('idCompte').value = '';
		}

		if (element.libelleCompte != "") {
			document.getElementById('compteDebiteLibelle').value = element.libelleCompte;
		} else {
			document.getElementById('compteDebiteLibelle').value = '';
		}
	}
}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Process - Saldo berichtigen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <tr>
			 <td>Mandant</td>
			 <td> <input name="libelle" class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>"/> <input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"/></td>
		  </tr>
		  <TR>
            <TD align="left" width="180" height="21" valign="middle">E-Mail Adresse</TD>
            <TD align="left">
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
              <input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"/>
            </TD>
          </TR>
          <tr>
			<td>Buchhaltung</td>
			<td><ct:FWCodeSelectTag name="idComptabilite" defaut="<%=viewBean.getIdComptabilite()%>" codeType="CGPRODEF" /></td>
		  </tr>
          <tr>
			<td>Rechnungsperiode</td>
			<td><ct:FWListSelectTag name="idPeriodeComptable" defaut="<%=viewBean.getIdPeriodeComptable()%>" data="<%=globaz.helios.translation.CGListes.getPeriodeComptableListe(session, toutLexercice)%>"/> </td>
		  </tr>

		  <tr>
			<td>Konto</td>
			<td>
			<%
				String jspLocation = servletContext + "/heliosRoot/compte_select.jsp";
				String params = "idExerciceComptable=" + exerciceComptable.getIdExerciceComptable() + "&isMandatAVS=" + exerciceComptable.getMandat().isEstComptabiliteAVS();

				globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
				globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
				int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
			%>

				<input type="hidden" name="idCompte" value="<%=viewBean.getIdCompte()%>">
				<ct:FWPopupList name="idExterneCompte" onFailure="onCompteFailure(window.event);" onChange="updateCompte('compteDebite',tag);" validateOnChange="true" params="<%=params%>" value="" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true"/> * <input name="compteDebiteLibelle" class='libelleLongDisabled' style="width : 12.15cm" tabindex="-1" size="30" readonly value=''>
			 </td>
		   </tr>

		   <tr>
			   <td>Betrag CHF</td>
			   <td><input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="libelle" style="text-align : right" name="newSolde" value=""/></td>
		   </tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<SCRIPT>
document.forms[0].enctype = "multipart/form-data";
document.forms[0].method = "post";
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>