
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*,globaz.globall.db.*, globaz.helios.translation.*" %>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF3007";
	//Récupération du viewBean
	CGPeriodeComptableEnvoyerAnnoncesViewBean viewBean = (CGPeriodeComptableEnvoyerAnnoncesViewBean) session.getAttribute ("viewBean");

  	CGPeriodeComptable periodeComptable = new CGPeriodeComptable();
   	periodeComptable.setSession((BSession)globaz.helios.translation.CodeSystem.getSession(session));
   	periodeComptable.setIdPeriodeComptable(viewBean.getIdPeriodeComptable());
   	periodeComptable.retrieve();	   	
 
	//Récupération de l'exercice comptable
	CGExerciceComptable exerciceComptable = (CGExerciceComptable) session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	//Label utilisé pour spécifier à l'utilisateur qu'il doit sélectionner une option.
	//String labelChoisir = "Choisir une option";
	//Label utilisé pour spécifier à l'utilisateur qu'aucune option n'est sélectionnée.
	String labelAucun = "Aucun";
	if (languePage.equalsIgnoreCase("de")) {
		labelAucun = "Kein";
	}

	userActionValue = "helios.comptes.periodeComptableEnvoyerAnnonces.executer";

//	String toutLexercice = "Tout l'exercice";
//	if (languePage.equalsIgnoreCase("de")) {
//		toutLexercice = "Ganze Rechnungsjahr";
//	}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<script>
function init() { } 
function onOk() {
	document.forms[0].submit();
} 
function onCancel() {
	document.forms[0].elements('userAction').value="back";
//	document.forms[0].submit();
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%> 


	Envoyer annonces OFAS
      <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			  
		<tr>
			<td>Mandat</td>
			<input type="hidden" name='isEnvoyerAnnoncesOfas' value='on'>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getMandat().getLibelle()%>'> <input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>"></td>
		</tr>
		<tr>
			<td>Exercice</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getFullDescription()%>'> 
			     <input name='idMandat' type="hidden" value='<%=exerciceComptable.getIdMandat()%>'>
			     <input name='idComptabilite' type="hidden" value='<%=CodeSystem.CS_DEFINITIF%>'></td>
		</tr>

		<tr>
			<td>Période comptable</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=periodeComptable.getFullDescription()%>'> 			     
		</tr>

		<tr>
			<td>Adresse E-Mail</td>
			<td> <input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>'> * </td>
		</tr>
			  
			  <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<ct:menuChange displayId="options" menuId="CG-periodeComptable" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPeriodeComptable()%>"/>
	
	<ct:menuActivateNode active="no" nodeId="periode_boucler"/>   	
   	<ct:menuActivateNode active="yes" nodeId="periode_envoyer_annonces_zas"/>
   	<ct:menuActivateNode active="yes" nodeId="periode_envoyer_annonces_ofas"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
