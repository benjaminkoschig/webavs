<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GLX0007"; %>
<%@ page import="globaz.lynx.db.fournisseur.*" %>
<%
LXInformationComptableViewBean viewBean = (LXInformationComptableViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdInformation();

String jspLocation = servletContext + "/lynxRoot/autocomplete/compte_select.jsp";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>
<%@page import="globaz.lynx.db.informationcomptable.LXInformationComptableViewBean"%><SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="lynx.informationcomptable.informationComptable.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="lynx.informationcomptable.informationComptable.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.informationcomptable.informationComptable.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.informationcomptable.informationComptable.modifier";

    return state;
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.informationcomptable.informationComptable.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'information comptable sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="lynx.informationcomptable.informationComptable.supprimer";
        document.forms[0].submit();
    }
}

function updateCompteCreance(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idCompteCreance").value = element.idCompte;
		document.getElementById("libelleCompteCreance").value = element.libelleCompte;
	}
}

function updateCompteCharge(tag) {
	document.getElementById("idCompteCharge").value = "";
	document.getElementById("libelleCompteCharge").value = "";
	
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idCompteCharge").value = element.idCompte;
		document.getElementById("libelleCompteCharge").value = element.libelleCompte;
	}
}

function updateCompteTva(tag) {
	document.getElementById("idCompteTva").value = "";
	document.getElementById("libelleCompteTva").value = "";	
	
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idCompteTva").value = element.idCompte;
		document.getElementById("libelleCompteTva").value = element.libelleCompte;
	}
}

function updateCompteEscompte(tag) {
	document.getElementById("idCompteEscompte").value = "";
	document.getElementById("libelleCompteEscompte").value = "";
	
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idCompteEscompte").value = element.idCompte;
		document.getElementById("libelleCompteEscompte").value = element.libelleCompte;
	}
}

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;

		document.getElementById("idCompteCreance").value = "";
		document.getElementById("idExterneCompteCreance").value = "";
		document.getElementById("libelleCompteCreance").value = "";
		document.getElementById("idCompteCharge").value = "";
		document.getElementById("idExterneCompteCharge").value = "";
		document.getElementById("libelleCompteCharge").value = "";
		document.getElementById("idCompteTva").value = "";
		document.getElementById("idExterneCompteTva").value = "";
		document.getElementById("libelleCompteTva").value = "";
		document.getElementById("idCompteEscompte").value = "";
		document.getElementById("idExterneCompteEscompte").value = "";
		document.getElementById("libelleCompteEscompte").value = "";
	
		idExterneCompteCreancePopupTag.updateJspName('<%=jspLocation%>?forDate=<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>&idMandat=' + element.idMandat + '&like=');
		idExterneCompteChargePopupTag.updateJspName('<%=jspLocation%>?forDate=<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>&idMandat=' + element.idMandat + '&like=');
		idExterneCompteTvaPopupTag.updateJspName('<%=jspLocation%>?forDate=<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>&idMandat=' + element.idMandat + '&like=');
		idExterneCompteEscomptePopupTag.updateJspName('<%=jspLocation%>?forDate=<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>&idMandat=' + element.idMandat + '&like=');
	}
}

function init() {}

function postInit() {
	document.getElementById("idExterneSociete").focus();
}

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" La société débitrice n'existe pas.");
	}
}

top.document.title = "Détail de l'information comptable - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>D&eacute;tail de l'information comptable<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	     <TR>
            <TD>Num&eacute;ro</TD>
            <TD colspan="2">
                <%
            		if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdFournisseur())) {
            			viewBean.setIdFournisseur(request.getParameter("idFournisseurNouveau")); 
            		}
            		
            	%>
            
            	<input type="hidden" name="idFournisseur" value="<%=viewBean.getIdFournisseur()%>"/>
                <input type="text" name="idInformation" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getIdInformation()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
          </TR>
         <TR>
         	<TD>Soci&eacute;t&eacute; d&eacute;bitrice</TD>
            <TD colspan="2">
			<%@ include file="/lynxRoot/include/societe.jsp" %>
			</TD>
		</TR>
		<TR> 
			<TD height="11" colspan="3"> 
				<hr size="3" width="100%">
			</TD>
		</TR>
         <TR>
            <TD>Compte cr&eacute;ancier</TD>
            <TD colspan="2">
				<%
					String params = "forDate=" + globaz.globall.util.JACalendar.todayJJsMMsAAAA() + "&idMandat=" + viewBean.getIdMandat();
					int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
				%> 
            	<input type="hidden" name="idCompteCreance" value="<%=viewBean.getIdCompteCreance()%>"/>
				<ct:FWPopupList name="idExterneCompteCreance" onFailure="onCompteFailure(window.event);" onChange="updateCompteCreance(tag)"  validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneCompteCredit()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true" tabindex="1"/>
				&nbsp;
            	<input type="text" name="libelleCompteCreance" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getLibelleCompteCreance()%>" class="libelleDisabled" readonly="readonly" tabindex="-1"/>
            </TD>
        </TR>
        <TR>
            <TD>Monnaie par d&eacute;faut</TD>
            <TD colspan="2">
            	<ct:FWCodeSelectTag name="csCodeIsoMonnaie" defaut="<%=viewBean.getCsCodeIsoMonnaie()%>" codeType="PYMONNAIE" wantBlank="false" tabindex="1"/>
            </TD>
        </TR>
		<TR> 
			<TD height="11" colspan="3"> 
				<hr size="3" width="100%">
			</TD>
		</TR>
         <TR>
            <TD><i>Valeurs par d&eacute;fauts (optionnelles) :</i></TD>
            <TD colspan="2">&nbsp;</TD>
        </TR>
         <TR>
            <TD>Ech&eacute;ance </TD>
            <TD colspan="2">
				 <input type="text" name="echeance" style="width:1cm" size="5" maxlength="3" value="<%=viewBean.getEcheance()%>" class="libelle" tabindex="1"/>&nbsp;jour(s)
            </TD>
        </TR>
         <TR>
            <TD>Libell&eacute; facture</TD>
            <TD colspan="2">
				 <input type="text" name="libelleFacture" style="width:7cm" size="41" maxlength="40" value="<%=viewBean.getLibelleFacture()%>" class="libelle" tabindex="1"/>
            </TD>
        </TR>
        <TR>
            <TD>Compte de charge</TD>
            <TD colspan="2">
            	<input type="hidden" name="idCompteCharge" value="<%=viewBean.getIdCompteCharge()%>"/>
				<ct:FWPopupList name="idExterneCompteCharge" onFailure="onCompteFailure(window.event);" onChange="updateCompteCharge(tag)"  validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneCompteCharge()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true" tabindex="1"/>
				&nbsp;
            	<input type="text" name="libelleCompteCharge" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getLibelleCompteCharge()%>" class="libelleDisabled" readonly="readonly" tabindex="-1"/>
 	        </TD>
        </TR>
        <TR>
            <TD>Compte TVA</TD>
            <TD colspan="2">
            	<input type="hidden" name="idCompteTva" value="<%=viewBean.getIdCompteTva()%>"/>
				<ct:FWPopupList name="idExterneCompteTva" onFailure="onCompteFailure(window.event);" onChange="updateCompteTva(tag)"  validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneCompteTva()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true" tabindex="1"/>
				&nbsp;
            	<input type="text" name="libelleCompteTva" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getLibelleCompteTva()%>" class="libelleDisabled" readonly="readonly" tabindex="-1"/>
	        </TD>
        </TR>
        <TR>
            <TD>Code TVA</TD>
            <TD colspan="2">
			<%
				String selectCsCodeTVASelect = globaz.lynx.parser.LXSelectBlockParser.getCsCodeTVASelectBlock(objSession, "csCodeTva", viewBean.getCsCodeTva(), false, true);

				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCsCodeTVASelect)) {
					out.print(selectCsCodeTVASelect);
				}
			%>
            </TD>
        </TR>
        <TR>
            <TD>Compte d'escompte</TD>
            <TD colspan="2">
            	<input type="hidden" name="idCompteEscompte" value="<%=viewBean.getIdCompteEscompte()%>"/>
				<ct:FWPopupList name="idExterneCompteEscompte" onFailure="onCompteFailure(window.event);" onChange="updateCompteEscompte(tag)"  validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneCompteEscompte()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="<%=autoCompleteStart%>" forceSelection="true" tabindex="1"/>
				&nbsp;
            	<input type="text" name="libelleCompteEscompte" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getLibelleCompteEscompte()%>" class="libelleDisabled" readonly="readonly" tabindex="-1"/>
            </TD>
        </TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>