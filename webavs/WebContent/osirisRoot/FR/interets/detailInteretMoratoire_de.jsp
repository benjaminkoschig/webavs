<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0032"; %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%
CADetailInteretMoratoireViewBean viewBean = (CADetailInteretMoratoireViewBean)session.getAttribute("viewBean");
userActionValue = "osiris.interets.detailInteretMoratoire.afficher";

bButtonValidate = objSession.hasRight(userActionNew, globaz.framework.secure.FWSecureConstants.ADD);

if(!JadeStringUtil.isEmpty(request.getParameter("domaine")))
{
	 viewBean.setDomaine(request.getParameter("domaine"));
}

if(!JadeStringUtil.isEmpty(request.getParameter("idInteretMoratoire")))
{
	 viewBean.setIdInteretMoratoire(request.getParameter("idInteretMoratoire"));
}

String secondServletPath = viewBean.isDomaineCA()?"/osiris":"/musca";
CAInteretMoratoire interetMoratoire = viewBean.getInteretMoratoire();
//définition des boutons à afficher
//si aucune ligne n'a été sélectionnée
if(!JadeStringUtil.isEmpty(request.getParameter("decisionFacturee"))) {
	//si la décision est facturée
	if(request.getParameter("decisionFacturee").equalsIgnoreCase("ok")) {
		bButtonCancel = false;
		bButtonValidate = false;
	}
}
if(interetMoratoire.getStatus().equals(CAInteretMoratoire.STATUS_COMPTABILISE) || interetMoratoire.getStatus().equals(CAInteretMoratoire.STATUS_BLOQUE)) {
	bButtonDelete = false;
	bButtonUpdate = false;
}

if(!JadeStringUtil.isEmpty(request.getParameter("_method"))) {
	bButtonCancel = false;
}

tableHeight = 150;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	String tmpDomaine = "";
	if (viewBean.isDomaineCA()) {
		tmpDomaine = "CA";
	} else {
		tmpDomaine = "FA";
	}
%>

<ct:menuChange displayId="options" menuId="CA-DecisionInteretsMoratoires" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdInteretMoratoire()%>"/>
	<ct:menuSetAllParams key="domaine" value="<%=tmpDomaine%>"/>
</ct:menuChange>

<SCRIPT language="javascript">
function add()
{
	updatePage();
	document.forms[0].elements('userAction').value="osiris.interets.detailInteretMoratoire.ajouter"
	document.forms[0].elements('dateDebut').value='';
	document.forms[0].elements('dateFin').value='';
	document.forms[0].elements('montantSoumis').value='';
	document.forms[0].elements('taux').value='';
	document.forms[0].elements('montantInteret').value='';
	document.forms[0].elements('idDetailInteretMoratoire').value='';
}


function upd() {
//	 On met à jour les infos du compte
	document.forms[0].target = "fr_main";
	document.forms[0].elements('selectedId').value = document.forms[0].elements('idInteretMoratoire').value;
	updatePage();
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
    {
        document.forms[0].elements('userAction').value="osiris.interets.detailInteretMoratoire.ajouter";
        document.forms[0].elements('selectedId').value = document.forms[0].elements('idInteretMoratoire').value;
    }
    else {
        document.forms[0].elements('userAction').value="osiris.interets.detailInteretMoratoire.modifier";
    }
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="back";
	} else {
		document.forms[0].elements('userAction').value="osiris.interets.detailInteretMoratoire.chercher";
	}
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="osiris.interets.detailInteretMoratoire.supprimer";
        document.forms[0].elements('selectedId').value = document.forms[0].elements('idInteretMoratoire').value;
        document.forms[0].submit();
    }
}

function init(){
//	 On met à jour les infos
	updatePage();
//	 On raffraichit le _rcListe du parent (CAPage)
<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	if (parent.document.forms[0])
		parent.document.forms[0].submit();
<%}%>
}
function updatePage() {
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'une ligne de calcul d'intérêt<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
						      <td style="width: 120px;">Du</td>
						      <td style="width: 180px;">
							      <input type="hidden" name="idDetailInteretMoratoire" value="<%=viewBean.getIdDetailInteretMoratoire()%>">
							      <%
							      String idInteretMoratoire = "";
							      if(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdInteretMoratoire())) {
							      	idInteretMoratoire = viewBean.getIdInteretMoratoire();
							      }
							      else {
							      	idInteretMoratoire = request.getParameter("idInteretMoratoire");
							      }
							      %>
							      <input type="hidden" name="idInteretMoratoire" value="<%=idInteretMoratoire%>">
							      <input type="hidden" name="id" value="<%=idInteretMoratoire%>">
							      <input type="hidden" name="domaine" value="<%=viewBean.getDomaine()%>">
							      <input type="hidden" name="idJournalFacturation" value="<%=viewBean.getIdJournalFacturation()%>">
							      <ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>" />
							      <script language="Javascript">
									document.getElementById("dateDebut").style.width=123;
								  </script>
						      </td>
						      <td>Au</td>
						      <td>
							      <ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>" />
							      <script language="Javascript">
									document.getElementById("dateFin").style.width=123;
								  </script>
							  </td>
						    </tr>
						    <tr>
						      <td>Montant soumis</td>
						      <td><input type="text" name="montantSoumis" value="<%= viewBean.getMontantSoumis()%>" style="width: 150px;"></td>
						      <td>Taux</td>
						      <td><input type="text" name="taux" value="<%= viewBean.getTaux()%>" style="width: 150px;"></td>
						    </tr>
						    <tr>
						      <td>Montant de l'int&eacute;r&ecirc;t</td>
						      <td><input type="text" name="montantInteret" value="<%= viewBean.getMontantInteret()%>" style="width: 150px;"></td>
						      <td>Année</td>
						      <td><input type="text" name="anneeCotisation" value="<%= viewBean.getAnneeCotisation()%>" size="4" maxlength="4"></td>
						    </tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>