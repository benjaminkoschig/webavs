<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>

<%
	LASuiviCaisseViewBean viewBean = (LASuiviCaisseViewBean) request.getAttribute("viewBean");
	if(viewBean == null){
		viewBean = new LASuiviCaisseViewBean();
	}
	request.setAttribute("suiviCaisseId", viewBean.getSuiviCaisseId());
	tableHeight = 230;
	bButtonCancel = false;
	idEcran = "LAA0011";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="db.LASuiviCaisseViewBean"%>
<%@page import="java.util.HashSet"%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<SCRIPT language="JavaScript">

	function add() {
		document.forms[0].elements('userAction').value="lacerta.fichier.suiviCaisse.insererSuivi";
	}

	function upd() {
	}

	function validate() {
		state = validateFields(); 
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="lacerta.fichier.suiviCaisse.modifierSuivi";
		else
			document.forms[0].elements('userAction').value="lacerta.fichier.suiviCaisse.insererSuivi";
		return (state);	
	}

	function cancel() {
	}

	function del() {
	}

	function init(){
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
Détail Fichier Central
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<%
	HashSet except = new HashSet();
	except.add(CodeSystem.GENRE_CAISSE_LAA);
	except.add(CodeSystem.GENRE_CAISSE_LPP);
%>
<tr>
	<td>Genre Caisse</td>
	<td><ct:FWCodeSelectTag name="genreCaisse"
		defaut="<%=viewBean.getGenreCaisse()%>" codeType="VEGENCAISS"
		except="<%=except%>" /></td>
	<td>N° Caisse</td>
	<td><input type="text" style="width: 80px;" name="numCaisse"
		value="<%=viewBean.getNumCaisseLibelle()%>" /></td>
	<td>N° affilié</td>
	<td><input type="text" style="width: 80px;" name="numeroAffilieCaisse" value="<%=viewBean.getNumeroAffileCaisse()%>" /></td>
</tr>
<tr>
	<td>Date de début :</td>
	<td><ct:FWCalendarTag name="dateDebut"
		value="<%=viewBean.getDateDebut()%>" /></td>
	<td>Date de fin :</td>
	<td><ct:FWCalendarTag name="dateFin"
		value="<%=viewBean.getDateFin()%>" /></td>
</tr>
<input type="hidden" name="suiviCaisseId" value="<%=viewBean.getSuiviCaisseId()%>">

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="LA-DetailFichier" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idAffiliation" value="<%=viewBean.getAffiliationId()%>" checkAdd="no"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdTiers()%>" checkAdd="no"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>