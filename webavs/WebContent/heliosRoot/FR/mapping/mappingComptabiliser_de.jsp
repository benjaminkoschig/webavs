<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import ="globaz.helios.db.mapping.*"%>
<%
	idEcran = "GCF4018";
	CGMappingComptabiliserViewBean viewBean = (CGMappingComptabiliserViewBean) session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdMapComptabiliser();
	userActionValue = "helios.mapping.mappingComptabiliser.modifier";

	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE")) {
		aucun = "Kein";
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="helios.mapping.mappingComptabiliser.ajouter"
}

function upd() {
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="helios.mapping.mappingComptabiliser.ajouter";
	} else {
    	document.forms[0].elements('userAction').value="helios.mapping.mappingComptabiliser.modifier";
    }

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.mapping.mappingComptabiliser.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.mapping.mappingComptabiliser.supprimer";
        document.forms[0].submit();
    }
}

function refreshPage() {
	document.forms[0].elements('_method').value == "add"
	document.forms[0].elements('userAction').value="helios.mapping.mappingComptabiliser.afficher";
	document.forms[0].submit();
}


function init(){}

function onCompteFailureSource(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}

	document.getElementById('idCompteSource').value = '';
	document.getElementById('compteSourceLibelle').value = '';
}

function onCompteFailureDestination(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}

	document.getElementById('idCompteDestination').value = '';
	document.getElementById('compteDestinationLibelle').value = '';
	document.getElementById('idContreEcritureDestination').value = '';
	document.getElementById('compteContreEcritureDestinationLibelle').value = '';
}

function updateCompteSource(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById('idCompteSource').value = element.idCompte;
		document.getElementById('compteSourceLibelle').value = element.compteLibelle;
	}
}

function updateCompteDestination(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById('idCompteDestination').value = element.idCompte;
		document.getElementById('compteDestinationLibelle').value = element.compteLibelle;
	}
}

function updateCompteContreEcritureDestination(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById('idContreEcritureDestination').value = element.idCompte;
		document.getElementById('compteContreEcritureDestinationLibelle').value = element.compteLibelle;
	}
}

function updateIdMandatSource() {
	document.getElementById('idExterneCompteSource').value = '';
	updateIdMandat();
}

function updateIdMandatDestination() {
	document.getElementById('idExterneCompteDestination').value = '';
	updateIdMandat();
}

function updateIdMandat() {
	idMandatSource = document.getElementById('idMandatSource').value;
	idMandatDestination = document.getElementById('idMandatDestination').value;

	idCompteSource = document.getElementById('idCompteSource').value;
	idExterneCompteSource = document.getElementById('idExterneCompteSource').value;

	idCompteDestination = document.getElementById('idCompteDestination').value;
	idExterneCompteDestination = document.getElementById('idExterneCompteDestination').value;

	idContreEcritureDestination = document.getElementById('idContreEcritureDestination').value;
	idExterneContreEcritureDestination = document.getElementById('idExterneContreEcritureDestination').value;

	document.location.href="<%=servletContext%><%=(mainServletPath)%>?userAction=helios.mapping.mappingComptabiliser.afficherSetIdMandat&_method=add&idMandatSource=" + idMandatSource + "&idMandatDestination=" + idMandatDestination + "&idExterneCompteSource=" + idExterneCompteSource + "&idExterneCompteDestination=" + idExterneCompteDestination + "&idContreEcritureDestination=" + idContreEcritureDestination + "&idExterneContreEcritureDestination=" + idExterneContreEcritureDestination + "&idCompteSource=" + idCompteSource + "&idCompteDestination=" + idCompteDestination;
}

/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail Mapping Comptabilisation<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD>Mandat <b>(source)</b></TD>
	<TD><%=CGMappingComptabiliserViewBean.getSelectMandatBlock(objSession, "idMandatSource", viewBean.getIdMandatSource(), "updateIdMandatSource")%></TD>
</TR>


<%
	String jspLocation = servletContext + "/heliosRoot/" + languePage + "/mapping/compte_select.jsp";
	String params = "idMandat=";

	String idMandatSource = new String();

	if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdMandatSource())) {
		idMandatSource = viewBean.getDefaultIdMandatSource();
	} else {
		idMandatSource = viewBean.getIdMandatSource();
	}

	params += idMandatSource;
	params += "&isMandatAVS=" + CGMappingComptabiliserViewBean.isMandatAVS(objSession, idMandatSource);
%>

<TR>
	<TD>Compte</TD>
	<TD>
	<input type="hidden" name="idCompteSource" value="<%=viewBean.getIdCompteSource()%>"/>
		<%
			int autoCompleteStart = globaz.helios.parser.CGAutoComplete.getAutoCompleteAutoStart();
		%>
	<ct:FWPopupList name="idExterneCompteSource" onFailure="onCompteFailureSource(window.event);" onChange="updateCompteSource(tag);" validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneCompteSource()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1"/> * <input name="compteSourceLibelle" class='libelleLongDisabled' style="width : 12.15cm" tabindex="-1" size="30" readonly value="<%=viewBean.getCompteSourceLibelle()%>"/>
	</TD>
</TR>

<TR>
	<TD>Centre de charge</TD>
	<TD><ct:FWListSelectTag name="idCentreChargeSource" defaut="<%=viewBean.getIdCentreChargeSource()%>" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, idMandatSource)%>"/></TD>
</TR>

<TR><TD colspan="2">&nbsp;</TD></TR>

<TR>
	<TD>Mandat <b>(destination)</b></TD>
	<TD><%=CGMappingComptabiliserViewBean.getSelectMandatBlock(objSession, "idMandatDestination", viewBean.getIdMandatDestination(), "updateIdMandatDestination")%></TD>
</TR>

<%
	String idMandatDestination = new String();

	if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdMandatDestination())) {
		idMandatDestination = viewBean.getDefaultIdMandatDestination();
	} else {
		idMandatDestination = viewBean.getIdMandatDestination();
	}

	params = "idMandat=" + idMandatDestination;
	params += "&isMandatAVS=" + CGMappingComptabiliserViewBean.isMandatAVS(objSession, idMandatDestination);
%>

<TR>
	<TD>Compte</TD>
	<TD>
	<input type="hidden" name="idCompteDestination" value="<%=viewBean.getIdCompteDestination()%>"/>
	<ct:FWPopupList name="idExterneCompteDestination" onFailure="onCompteFailureDestination(window.event);" onChange="updateCompteDestination(tag);" validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneCompteDestination()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1"/> * <input name="compteDestinationLibelle" class='libelleLongDisabled' style="width : 12.15cm" tabindex="-1" size="30" readonly value="<%=viewBean.getCompteDestinationLibelle()%>"/>
	</TD>
</TR>

<TR>
	<TD>Compte (contre &eacute;criture)</TD>
	<TD>
	<input type="hidden" name="idContreEcritureDestination" value="<%=viewBean.getIdContreEcritureDestination()%>"/>
	<ct:FWPopupList name="idExterneContreEcritureDestination" onFailure="onCompteFailureDestination(window.event);" onChange="updateCompteContreEcritureDestination(tag);" validateOnChange="true" params="<%=params%>" value="<%=viewBean.getIdExterneContreEcritureDestination()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1"/> * <input name="compteContreEcritureDestinationLibelle" class='libelleLongDisabled' style="width : 12.15cm" tabindex="-1" size="30" readonly value="<%=viewBean.getCompteContreEcritureDestinationLibelle()%>"/>
	</TD>
</TR>

<TR>
	<TD>Centre de charge</TD>
	<TD><ct:FWListSelectTag name="idCentreChargeDestination" defaut="<%=viewBean.getIdCentreChargeDestination()%>" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, idMandatDestination)%>"/></TD>
</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>