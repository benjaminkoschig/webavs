<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*,globaz.helios.application.CGProperties"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF0016";
    CGPeriodeComptableViewBean viewBean = (globaz.helios.db.comptes.CGPeriodeComptableViewBean)session.getAttribute ("viewBean");
    CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean )session.getAttribute(CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	selectedIdValue = viewBean.getIdPeriodeComptable();
	userActionValue = "helios.comptes.periodeComptable.modifier";
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="helios.comptes.periodeComptable.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.periodeComptable.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.comptes.periodeComptable.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.comptes.periodeComptable.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.comptes.periodeComptable.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Rechnungsperiode<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
      <tr>
            <td>Nummer</td>
        <td><input type="text" class="libelleDisabled" readonly name="idPeriodeComptable" value="<%=viewBean.getIdPeriodeComptable()%>"></td>
      </tr>
      <tr>
            <td>Periodetyp</td>
        <td><ct:FWCodeSelectTag name="idTypePeriode"
                        libelle="libelle"
			   			except="<%=viewBean.getExceptTypePeriode()%>"
                        codeType="CGPERCPT"
                        defaut="<%=viewBean.getDefaultTypePeriode()%>"/>*</td>
      </tr>
      <tr>
            <td>Abschlussart</td>
        <td><ct:FWListSelectTag name="idBouclement"
                        defaut="<%=viewBean.getIdBouclement()%>"
                            data="<%=globaz.helios.translation.CGListes.getBouclementListe(session)%>"/>
        </td>
      </tr>
      <tr>
        <td>Code</td>
	<td>
	<% if( exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() ){ %>
		<input name='code' class="disabled" SIZE ="2" maxlength=2 readonly value='<%=viewBean.getCode()%>'>
	<%} else { %>
		<input name='code'  SIZE ="2" maxlength=2  value='<%=viewBean.getCode()%>'> *
	<%}%>
	</td>


      </tr>

      <tr>
            <td>Beginndatum der Periode</td>
        <td><ct:FWCalendarTag name="dateDebut"
                          value="<%=viewBean.getDateDebut()%>" />*</td>
      </tr>
      <tr>
            <td>Enddatum der Periode</td>
        <td><ct:FWCalendarTag name="dateFin"
                          value="<%=viewBean.getDateFin()%>" />*</td>
      </tr>
      <tr>
            <td>Abgeschlosse Periode</td>
      <% if (viewBean.isEstCloture().booleanValue()){ %>
		<TD width="187"><input type="checkbox" name="estCloture" <%=(viewBean.isEstCloture().booleanValue())?"CHECKED":""%>></TD>
		<%} else { %>
		<TD width="187"><input disabled readonly class="disabled" type="checkbox" name="estCloture" <%=(viewBean.isEstCloture().booleanValue())?"CHECKED":""%>></TD>
		<%}%>
      </tr>
      <tr>
        <td>Abschlussjournal der Periode</td>
        <td><input type="text" class="libelleLongDisabled" readonly value="<%=viewBean.getLibelleJournal1()%>"></td>
      </tr>
      <tr>
        <td>Eröffnungsjournal der nächste Periode</td>
        <td><input type="text" class="libelleLongDisabled" readonly value="<%=viewBean.getLibelleJournal2()%>"></td>
      </tr>
      <tr>
        <td>Abschlussjournal der Abschlussperiode</td>
        <td><input type="text" class="libelleLongDisabled" readonly value="<%=viewBean.getLibelleJournal3()%>"></td>
      </tr>
	<% if( exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() ){ %>
      <tr>
            <td>Status der Meldung an die ZAS</td>
        <td>
		<input name='idAnnonce' readonly class="libelleLongDisabled" value='<%=viewBean.getIdAnnonce()%>'>

	</td>
      </tr>
      
      <% if(CGProperties.ACTIVER_ANNONCES_XML.getBooleanValue()){ %>      
      <tr>
      	<td>[DE]Nom fichier annonce XML transmis</td>
        <td>
			<input name='nomFichier' readonly class="libelleLongDisabled" value='<%=viewBean.getNomFichier()%>'>
		</td>
	  </tr>
	  <%} %>
	<%} %>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<ct:menuChange displayId="options" menuId="CG-periodeComptable" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPeriodeComptable()%>"/>

	<% if (viewBean.isEstCloture().booleanValue()) { %>
   		<ct:menuActivateNode active="no" nodeId="periode_boucler"/>

   		<% if (viewBean.isCloture() || (!exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() && !exerciceComptable.getMandat().isMandatConsolidation())) { %>
			<ct:menuActivateNode active="no" nodeId="periode_envoyer_annonces_zas"/>
    		<ct:menuActivateNode active="no" nodeId="periode_envoyer_annonces_ofas"/>
    		<ct:menuActivateNode active="no" nodeId="import_journal_debit"/>
   		<% } else { %>
	     	<ct:menuActivateNode active="yes" nodeId="periode_envoyer_annonces_zas"/>
    		<ct:menuActivateNode active="yes" nodeId="periode_envoyer_annonces_ofas"/>
    		<ct:menuActivateNode active="yes" nodeId="import_journal_debit"/>
 	    <% }%>
   	<% } else { %>
   		<ct:menuActivateNode active="yes" nodeId="periode_boucler"/>
     	<ct:menuActivateNode active="no" nodeId="periode_envoyer_annonces_zas"/>
    	<ct:menuActivateNode active="no" nodeId="periode_envoyer_annonces_ofas"/>
    	<ct:menuActivateNode active="no" nodeId="import_journal_debit"/>
   	<% } %>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>