<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCF300X"; %>
<%
	globaz.helios.db.comptes.CGExerciceComptableViewBean exerciceComptable = (globaz.helios.db.comptes.CGExerciceComptableViewBean )session.getAttribute(globaz.helios.db.interfaces.CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	globaz.helios.db.comptes.CGPeriodeComptableImportJournalDebitViewBean viewBean = (globaz.helios.db.comptes.CGPeriodeComptableImportJournalDebitViewBean) session.getAttribute("viewBean");
	userActionValue = "helios.comptes.periodeComptableImportJournalDebit.executer";

	globaz.helios.db.comptes.CGPeriodeComptable periodeComptable = new globaz.helios.db.comptes.CGPeriodeComptable();
   	periodeComptable.setSession((globaz.globall.db.BSession)globaz.helios.translation.CodeSystem.getSession(session));
   	periodeComptable.setIdPeriodeComptable(viewBean.getIdPeriodeComptable());
   	periodeComptable.retrieve();
   	
   	String tous = "Alle";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">
<!-- //hide this script from non-javascript-enabled browsers
	top.document.title = "Process - Solljournal importieren - " + top.location.href;
// stop hiding -->
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Importer le journal des débits<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<tr>
			<td>Mandant</td>
			<input type="hidden" name='isEnvoyerAnnoncesOfas' value='on'>
			<td> 
			<ct:FWListSelectTag name="idMandat" defaut=""
				data="<%=globaz.helios.translation.CGListes.getMandatListe(session, tous, exerciceComptable.getIdMandat())%>"/>
				<input type="hidden" name="idExerciceComptable" value="<%=exerciceComptable.getIdExerciceComptable()%>">
			</td>
		</tr>
		<tr>
			<td>Rechnungsjahr</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=exerciceComptable.getFullDescription()%>'>
			     <input name='idComptabilite' type="hidden" value='<%=globaz.helios.translation.CodeSystem.CS_DEFINITIF%>'></td>
		</tr>

		<tr>
			<td>Rechnungsperiode</td>
			<td> <input name='libelle' class='libelleLongDisabled' readonly value='<%=periodeComptable.getFullDescription()%>'>
		</tr>

		<tr>
			<td>E-Mail Adresse</td>
			<td> <input name='eMailAddress' class='libelleLong' value='<%=viewBean.getEMailAddress()==null?"":viewBean.getEMailAddress()%>'></td>
		</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<script>
	document.forms[0].enctype = "multipart/form-data";
	document.forms[0].method = "post";
</script>
<ct:menuChange displayId="options" menuId="CG-periodeComptable" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPeriodeComptable()%>"/>

	<ct:menuActivateNode active="no" nodeId="periode_boucler"/>
   	<ct:menuActivateNode active="yes" nodeId="periode_envoyer_annonces_zas"/>
   	<ct:menuActivateNode active="yes" nodeId="periode_envoyer_annonces_ofas"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>