
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->

<style type="text/css">
<!--
.txtLink {font-family: Verdana, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; text-decoration: none; margin-left: 0px}
-->
</style>

<%

	idEcran="GCF4003";
    globaz.helios.db.classifications.CGClasseCompteViewBean viewBean = (globaz.helios.db.classifications.CGClasseCompteViewBean)session.getAttribute ("viewBean");
	globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	if (viewBean==null || viewBean.isNew() && !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdClasseCompte()) && "add".equalsIgnoreCase(request.getParameter("_method")))
		viewBean.retrieve();
		

	//Label utilisé pour spécifier à l'utilisateur qu'il doit sélectionner une option.
	String labelChoisir = "Choisir une option";
	//Label utilisé pour spécifier à l'utilisateur qu'aucune option n'est sélectionnée.
	if (languePage.equalsIgnoreCase("de")) {
		labelChoisir = "Ihr auswhal";
	}


	bButtonNew = true;
	bButtonUpdate = true;
	bButtonDelete = true;
	bButtonValidate = true;
	bButtonCancel = true;

%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="helios.classifications.classeCompte.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.classifications.classeCompte.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.classifications.classeCompte.modifier";

    return state;

}
function cancel() {
  document.forms[0].elements('userAction').value="helios.classifications.classeCompte.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.classifications.classeCompte.supprimer";
        document.forms[0].submit();
    }
}


function init(){}


function editIdClassification() {
	document.forms[0].elements('idClassification').disabled=false;
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Classes de comptes<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

	<%
	if (viewBean!=null && !viewBean.isNew()) {
		actionNew += "&idClasseCompteForDisplay="+viewBean.getIdClasseCompte();
	}

	%>

	<tr>
      <TD>&nbsp;Mandat&nbsp;</td>
      <td><input name='libelle' class="libelleLongDisabled" readonly value="<%=exerciceComptable.getMandat().getLibelle()%>"></td>
      <TD>&nbsp;Exercice&nbsp;</td>
      <td><input name='fullDescription' readonly class="libelleLongDisabled" value="<%=exerciceComptable.getFullDescription()%>"></TD>
	</tr>
	
	
	<TR>
		  <td>Numéro classe</TD>
		  <TD colspan="3"><input name='noClasse' class="libelleLong" size="10" maxlength="10" value="<%=viewBean.getNoClasse()%>"></TD>
	  </TR>

	<tr>
	 <td>Référence parent</td>
	 <td><input name='idSuperClasse' class='libelleLong' value='<%=viewBean.getIdSuperClasse()%>'></td>
	 <td>Numéro ordre</td>
	 <td><input name='numeroOrdre' class='libelleLong' size="5" maxlength="5" value='<%=viewBean.getNumeroOrdre()%>'></td>
	</tr>

	<tr>
	 <td>Libelle FR</td>
	 <td colspan="3"><input name='libelleFr' class='libelleLong' value='<%=viewBean.getLibelleFr()%>'></td>
	</tr>

	<tr>
	 <td>Libelle DE</td>
	 <td colspan="3"><input name='libelleDe' class='libelleLong' value='<%=viewBean.getLibelleDe()%>'></td>
	</tr>

	<tr>
	 <td>Libelle IT</td>
	 <td colspan="3"><input name='libelleIt' class='libelleLong' value='<%=viewBean.getLibelleIt()%>'></td>
	</tr>

	<tr>
		<td>Imprimer titre</td>
        		<TD colspan="3"><input type="checkbox" name="imprimerTitre" <%=(viewBean.isImprimerTitre().booleanValue())?"CHECKED":""%>></TD>
      	</tr>
	<tr>
	<tr>
		<td>Imprimer total</td>
        		<TD colspan="3"><input type="checkbox" name="imprimerTotal" <%=(viewBean.isImprimerTotal().booleanValue())?"CHECKED":""%>></TD>
      	</tr>
	<tr>
	<tr>
		<td>Imprimer résultat</td>
        		<TD colspan="3"><input type="checkbox" name="imprimerResultat" <%=(viewBean.isImprimerResultat().booleanValue())?"CHECKED":""%>></TD>
      	</tr>
	<tr>

		<tr>
		<TD>Classification
		<%String idClasseCompte = null;
		  idClasseCompte = viewBean.getIdClasseCompte();
		  if (globaz.jade.client.util.JadeStringUtil.isBlank(idClasseCompte))
		  	idClasseCompte = "0";
		%>
		<input name='idClasseCompte' type="hidden" value="<%=viewBean.getIdClasseCompte()%>">
		</td>
		<td colspan="3">
				 <ct:FWListSelectTag name="idClassification"
				 defaut="<%=viewBean.getIdClassification()%>"
				 data="<%=globaz.helios.translation.CGListes.getClassificationListe(session, exerciceComptable.getIdMandat(), null)%>"/>
				 &nbsp;<a href="javascript:editIdClassification();" class="txtLink">editer</a>
				<script>				
					function updateIFrame() {
						document.all['classeCompteIFrame'].src = "<%=servletContext%><%=((String)request.getAttribute("mainServletPath")+"Root")%>/<%=globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)%>/comptes/classeCompte_extendedSelection.jsp?idClasseCompte=<%=idClasseCompte%>&idClassification="+document.all['idClassification'].value;
					}
					document.all['idClassification'].onchange = updateIFrame;
				</script>
				<input type="hidden" name="classesCompteList" value="">				
		  </Td>		  	
		</tr>
		<tr>
			<td></td>
			<td colspan="3">
			<iframe id="classeCompteIFrame" width=550px height=200px></iframe>
			</td>
		</tr>

				<script>updateIFrame();</script>



<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>