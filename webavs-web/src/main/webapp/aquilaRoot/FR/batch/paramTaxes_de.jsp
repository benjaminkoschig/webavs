<%@page import="globaz.aquila.db.batch.COParamTaxesViewBean"%>
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="GCO0036";
	COParamTaxesViewBean viewBean = (COParamTaxesViewBean) session.getAttribute("viewBean");
	String jspLocation = servletContext + mainServletPath + "Root/rubrique_select.jsp";

	String selectedId = "";
	if (JadeStringUtil.isBlank(request.getParameter("selectedId"))) {
		selectedId = (String)session.getAttribute("selectedId");
	} else {
		selectedId = request.getParameter("selectedId");
	}
%>

<%@page import="globaz.aquila.db.access.batch.COCalculTaxe"%>
<%@page import="java.util.HashMap"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.aquila.db.access.batch.COSequence"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.aquila.db.access.batch.COSequenceManager"%>
<script language="JavaScript"></script>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

	function add() {
		document.forms[0].elements('userAction').value="aquila.batch.paramTaxes.ajouter"
	}

	function upd() {
		document.forms[0].elements('selectSequence').disabled="true";
	}

	function validate() {
	    if (document.forms[0].elements('_method').value == "add") {
	        document.forms[0].elements('userAction').value="aquila.batch.paramTaxes.ajouter";
	    } else {
	        document.forms[0].elements('userAction').value="aquila.batch.paramTaxes.modifier";
	    }
	    return true;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="back";
		} else {
	 		document.forms[0].elements('userAction').value="aquila.batch.paramTaxes.chercher";
		}
	}

	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné ! Voulez-vous continuer ?")) {
			document.forms[0].elements('userAction').value="aquila.batch.paramTaxes.supprimer";
			document.forms[0].submit();
		}
	}

	function init() {
	}

	function rubriqueManuelleOn(){
		document.forms[0].idCompte.value="";
		//document.forms[0].idExterneRubriqueEcran.value="";
		document.forms[0].rubriqueDescription.value="";
	}

	function updateRubrique(el) {
		if (el == null || el.value== "" || el.options[el.selectedIndex] == null) {
			rubriqueManuelleOn();
		} else {
			var elementSelected = el.options[el.selectedIndex];
			document.forms[0].idCompte.value = elementSelected.idCompte;
			document.forms[0].rubriqueDescription.value = elementSelected.rubriqueDescription;
		}
	}

	function gereControle() {
		<%
			COSequenceManager seq = new COSequenceManager();
			seq.setSession(objSession);
			try {
				seq.find();
			} catch (Exception e) {
			}

			Iterator k = seq.getContainer().iterator();
			while (k.hasNext()) {
				COSequence sequence = (COSequence) k.next();
		%>
				//document.getElementById('seq_<%=sequence.getIdSequence()%>').style.display='none';
				jscss("add", document.getElementById('seq_<%=sequence.getIdSequence()%>'), "hidden");
				document.getElementById('etapes_<%=sequence.getIdSequence()%>').name = "a";

		<% } %>

			if (document.getElementById('idSequence').options.value != '') {
				//document.getElementById("seq_" + document.getElementById('forIdSequence').options.value).style.display='';
				jscss("remove", document.getElementById("seq_" + document.getElementById('idSequence').options.value), "hidden");
				document.getElementById("etapes_" + document.getElementById('idSequence').options.value).name = "forIdEtape";
			}

			return true;
	}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Détail d'une taxe<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<%
		COCalculTaxe ct = null;

		if(viewBean != null && !JadeStringUtil.isBlank(viewBean.getIdTraduction()))	{
			ct = viewBean.returnISOLangueLibelleValue(viewBean.getIdTraduction());
		}
	%>
	<tr>
		<td class="label">Id Taxe</td>
		<td class="control">
			<input type="text" name="" value='<%=viewBean.getIdTaxe()%>' class="libelleDisabled" readonly>
		</td>
	</tr>
	<tr>
		<td class="label">Etape</td>
		<td class="control">
			<ct:select tabindex="0" id="etape" defaultValue='<%=viewBean.getEtape()%>' name="etape" wantBlank="false">
				<ct:optionsCodesSystems csFamille="COETAPP"></ct:optionsCodesSystems>
			</ct:select>
		</td>
	</tr>
	<tr>
		<td class="label">Séquence</td>
		<td class="control">
			<select name="idSequence" onChange="gereControle();" id="selectSequence">
			<%
			Iterator i = seq.getContainer().iterator();
			while (i.hasNext()) {
			COSequence sequence = (COSequence) i.next();
			%>
			<option value="<%=sequence.getIdSequence()%>" <%=sequence.getIdSequence().equals(viewBean.getIdSequence())?"selected='selected'":"" %>><%=sequence.getLibSequenceLibelle()%></option>
			<% } %>
			</select>
			<input type="hidden" name="orderByLibEtapeCSOrder" value="true">
		</td>
	</tr>
	<tr>
		<td class="label">Type de taxe</td>
		<td class="control">
			<ct:select tabindex="0" id="typeTaxeEtape" defaultValue='<%=viewBean.getTypeTaxeEtape()%>' name="typeTaxeEtape" wantBlank="false">
				<ct:optionsCodesSystems csFamille="COTYPETAXE"></ct:optionsCodesSystems>
			</ct:select>
		</td>
	</tr>
	<tr>
		<td class="label">Rubrique</td>
		<td class="control">
			<ct:FWPopupList name="rubrique"
				onFailure="rubriqueManuelleOn();"
				onChange="updateRubrique(tag.select);"
				value='<%=viewBean.getRubriqueEntity()!=null?viewBean.getRubriqueEntity().getIdExterne():""%>'
				className="libelle"
				jspName="<%=jspLocation%>"
				minNbrDigit="1"
				forceSelection="true"
				validateOnChange="false"/>
			<input type="text" name="rubriqueDescription" size="60" value='<%=viewBean.getRubriqueEntity() != null?viewBean.getRubriqueEntity().getDescription():""%>' class="libelleLongDisabled"  readonly tabindex="-1">
			<input type="hidden" name="idCompte" value='<%=viewBean.getRubriqueEntity()!=null?viewBean.getRubriqueEntity().getIdRubrique():""%>' readonly tabindex="-1">
		</td>
	</tr>
	<tr>
		<td class="label">Libellé FR</td>
		<td class="control">
			<input size="50" type="text" name="libelleFR" value='<%=ct!=null?ct.getDescription("FR"):""%>'>
		</td>
	</tr>
	<tr>
		<td class="label">Libellé DE</td>
		<td class="control">
			<input size="50" type="text" name="libelleDE" value='<%=ct!=null?ct.getDescription("DE"):""%>'>
		</td>
	</tr>
	<tr>
		<td class="label">Libellé IT</td>
		<td class="control">
			<input size="50" type="text" name="libelleIT" value='<%=ct!=null?ct.getDescription("IT"):""%>'>
		</td>
	</tr>
	<tr>
		<td class="label">Type calcul</td>
		<td class="control">
			<ct:select tabindex="0" id="typeTaxe" defaultValue='<%=viewBean.getTypeTaxe()%>' name="typeTaxe" wantBlank="false">
				<ct:optionsCodesSystems csFamille="OSITYPTAX"></ct:optionsCodesSystems>
			</ct:select>
		</td>
	</tr>
	<tr>
		<td class="label">Base calcul</td>
		<td class="control">
			<ct:select tabindex="0" id="baseTaxe" defaultValue='<%=viewBean.getBaseTaxe()%>' name="baseTaxe" wantBlank="false">
				<ct:optionsCodesSystems csFamille="OSIBASTAX"></ct:optionsCodesSystems>
			</ct:select>
		</td>
	</tr>
	<tr>
		<td class="label">Montant fixe</td>
		<td class="control">
			<input type="text"  name="montantFixe" value='<%=viewBean.getMontantFixe()%>' class="montant">
		</td>
	</tr>
	<tr>
		<td class="label">Imputer taxe</td>
		<td class="control">
			<input type="checkbox" name="imputerTaxes" <%=viewBean.getImputerTaxes().booleanValue()==true?"CHECKED":""%>>
		</td>
	</tr>
	<%
	if(viewBean != null){
		session.setAttribute("idTaxe", viewBean.getIdTaxe());
		session.setAttribute("Etape", viewBean.getEtape());
		session.setAttribute("typeTaxeEtape", viewBean.getTypeTaxeEtape());
		session.setAttribute("Libelle", viewBean.getLibelle());
		session.setAttribute("idRubrique", viewBean.getIdRubrique());
		session.setAttribute("selectedId", selectedId);
	}
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<script>
</script>
<% } %>
		<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal"/>
		<ct:menuChange displayId="options" menuId="CO-ParamTaxe" showTab="options">
			<ct:menuActivateNode active="no" nodeId="a_id_c_p2"/>
			<ct:menuActivateNode active="yes" nodeId="a_id_c_p3"/>
			<ct:menuActivateNode active="yes" nodeId="a_id_c_p4"/>
		</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>