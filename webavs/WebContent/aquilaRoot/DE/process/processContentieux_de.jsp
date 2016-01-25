<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@page import="globaz.aquila.vb.process.COProcessContentieuxViewBean" %>
<%@ page import="globaz.aquila.util.CODateUtils"%>
<%@ page import="globaz.aquila.db.access.batch.COSequenceManager"%>
<%@ page import="globaz.aquila.db.access.batch.COSequence"%>
<%@ page import="globaz.aquila.db.access.batch.COEtape"%>
<%@ page import="globaz.osiris.db.comptes.CATypeSection" %>
<%@ page import="globaz.osiris.parser.CASelectBlockParser"%>
<%@ page import="globaz.osiris.db.comptes.CARole" %>
<%@ page import="java.util.Iterator" %>
<%
idEcran = "GCO3001";

COProcessContentieuxViewBean viewBean = (COProcessContentieuxViewBean) session.getAttribute("viewBean");

userActionValue = "aquila.process.processContentieux.executer";

globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) viewBean.getISession();
%>
<LINK id="aquilaCSS" rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<SCRIPT language="JavaScript">
<!-- hide this script from non-javascript-enabled browsers

function validate() {
	jscss("add", document.getElementById("btnOk"), "hidden");
	//document.getElementById("btnOk").disabled = true;
	return true;
}

function deselectionner(select) {
	for (i=0; i< select.options.length; i++)
        select.options(i).selected = false;
}

function selectionner(selectedId, value) {
	if (value != null && value != '') {
		document.getElementById(selectedId).value = value;
	}
}

var oldSels = [];

<% for (java.util.Iterator seqIter = viewBean.getSequences().iterator(); seqIter.hasNext();) { %>
oldSels[<%=((globaz.aquila.db.access.batch.COSequence) seqIter.next()).getIdSequence()%>] = [];
<% } %>

function select(select, idSequence, idCreer) {
	var oldSel = oldSels[idSequence];
	
	// on itére sur les options du select pour trouver laquelle a été cliquée
    for (var id = 0; id < select.options.length; id++){
        if (select.options[id].selected != oldSel[id]) {
        	// c'est l'option avec l'index 'id' qui a été cliquée
            oldSel[id] = select.options[id].selected;
            
            if (select.options[id].selected && select.options[id].className == 'mark') {
            	// si l'option cliquée a été sélectionnée et qu'elle est marquée comme dépendante, on sélectionne l'option créer
                document.getElementById('etape_'+idCreer+'_'+idSequence).selected = true;
            }
        } 
    }
}

function GereControle() {
	<%
		COSequenceManager seq = new COSequenceManager();
		seq.setSession(objSession);
		try {
			seq.find();
		} catch (Exception e) {
		}
	%>
	<%
	Iterator k = seq.getContainer().iterator();
	while (k.hasNext()) {
		COSequence sequence = (COSequence) k.next();
	%>
		document.getElementById('sequ_<%=sequence.getIdSequence()%>').style.display='none';
	<% } %>

	if (document.getElementById('forIdSequence').options.value != '') {
		document.getElementById("sequ_" + document.getElementById('forIdSequence').options.value).style.display='';
	}

	return true;
}

function changePrevisionnel() {
	if (document.getElementById("modePrevisionnel").checked) {
		$("#imprimerDocument").attr('disabled', false);
	} else {
		$("#imprimerDocument").attr('checked', true);
		$("#imprimerDocument").attr('disabled', true);
	}
	return true;
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Rechtspflege<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD width="20%">E-Mail</TD>
            <TD> 
              <INPUT type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelle">
            </TD>
            <TD width="20%">Kontaktperson</TD>
            <TD><input type="text" name="userIdCollaborateur" maxlength="20" size="20" value="<%=(viewBean.getUserIdCollaborateur()== null)?"":viewBean.getUserIdCollaborateur()%>"></TD>
          </TR>
          <TR> 
            <TD colspan="4"><HR></TD>
          </TR>
          <TR> 
            <TD>Vorsorglicher Modus</TD>
            <TD> 
              <input type="checkbox" name="modePrevisionnel" onclick="changePrevisionnel();" value="on"<%=viewBean.getModePrevisionnel().booleanValue()?" checked":""%>>
            </TD>
            <TD>Bezeichnung des Verbuchungsjournals</TD>
            <TD colspan="3"> 
              <input type="text" name="libelleJournal" class="libelleLong" value="<%=viewBean.getLibelleJournal()%>">
            </TD>
          </TR>
          <TR> 
            <TD colspan="4"><HR></TD>
          </TR>
          <TR> 
            <TD>Auslösungsliste ausdrucken</TD>
            <TD> 
              <input type="checkbox" name="imprimerListeDeclenchement" value="on" <%if (viewBean.getImprimerListeDeclenchement().booleanValue()) {%> checked <%}%> >
            </TD>
            <TD>Betreibungslisten nach Ämter ausdrucken</TD>
            <TD> 
              <input type="checkbox" name="imprimerListePourOP" value="on" <%if (viewBean.getImprimerListePourOP().booleanValue()) {%> checked <%}%> >
            </TD>
          </TR>
          <TR>
            <TD>Spezifische Verarbeitung</TD>
            <TD> 
              <input type="checkbox" name="executeTraitementSpecifique" value="on" <%if (viewBean.isExecuteTraitementSpecifique()) {%> checked <%}%>/>
            </TD>
            <TD>Dokumente ausdrucken</TD>
            <TD>
              <input type="checkbox" name="imprimerDocument" id="imprimerDocument" value="on" <%if (viewBean.getImprimerDocument().booleanValue()) {%> checked <%}%>/>
            </TD>
          </TR>
          <TR>
            <TD>Excel Arbeitsdatei ausdrucken</TD>
            <TD>
              <input type="checkbox" name="imprimerJournalContentieuxExcelml" value="on" <%if (viewBean.getImprimerJournalContentieuxExcelml().booleanValue()) {%> checked <%}%> >
            </TD>
            <TD colspan="2"> </TD>
          </TR>
          <TR>
            <TD>Referenzdatum</TD>
            <TD> 
              <ct:FWCalendarTag name="dateReference" value="<%=viewBean.getDateReference()%>"/>
            </TD>
            <TD>Datum auf Dokumente</TD>
            <TD> 
              <ct:FWCalendarTag name="dateSurDocument" value="<%=viewBean.getDateSurDocument()%>"/>
            </TD>
          </TR>
          <TR>
            <TD>Zahlungsfrist</TD>
            <TD colspan="3"> 
              <ct:FWCalendarTag name="dateDelaiPaiement" value="<%=CODateUtils.getDateDelaiPaiement((globaz.globall.db.BSession) viewBean.getISession())%>"/>
            </TD>
          </TR>
          <TR> 
            <TD>1. Sortierung Vorsorgliche Liste und Dokumente</TD>
            <TD> 
              <select name="selectionTriListeCA" >
                <option value="1"<%="1".equals(viewBean.getSelectionTriListeCA())?" selected":""%>>Abrechnungskonto (Name)</option>
                <option value="2"<%="2".equals(viewBean.getSelectionTriListeCA())?" selected":""%>>Abrechnungskonto (Nummer)</option>
              </select>
            </TD>
            <TD>2. Sortierung Vorsorgliche Liste
              und Dokumente</TD>
            <TD> 
              <select name="selectionTriListeSection" >
                <option value="1"<%="1".equals(viewBean.getSelectionTriListeSection())?" selected":""%>>Sektion (Nummer)</option>
                <option value="2"<%="2".equals(viewBean.getSelectionTriListeSection())?" selected":""%>>Sektion (Datum)</option>
              </select>
            </TD>
          </TR>
          <TR> 
            <TD colspan="4"><HR></TD>
          </TR>
          <TR> 
            <TD>Ab Mitglied-Nr.</TD>
            <TD><INPUT type="text" name="fromNoAffilie" value="<%=viewBean.getFromNoAffilie()%>"></TD>
            <TD>Bis Mitglied-Nr.</TD>
            <TD><INPUT type="text" name="beforeNoAffilie" value="<%=viewBean.getBeforeNoAffilie()%>"></TD>
          </TR>

<%
	String selectBlock = CASelectBlockParser.getForIdGenreSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) { %>
	<tr>
		<td>Art des Abrechnungskontos</td>
		<td>
			<%=selectBlock%>
			
			<script type="text/javascript">
				selectionner('forIdGenreCompte', '<%=viewBean.getForIdGenreCompte()%>');
			</script>
		</td>
		<td class="label">Sequenz</td>
		<td class="control">
			<select name="forIdSequence" onChange="GereControle();">
			<%
				Iterator i = seq.getContainer().iterator();
				while (i.hasNext()) {
					COSequence sequence = (COSequence) i.next();
			%>
				<option value="<%=sequence.getIdSequence()%>"><%=sequence.getLibSequenceLibelle()%></option>
			<% } %>
			</select>
			<script type="text/javascript">
				selectionner('forIdSequence', '<%=viewBean.getForIdSequence()%>');
			</script>
			<input type="hidden" name="orderByLibEtapeCSOrder" value="true">
		</td>
	</tr>
	<% }

	String selectCategorieSelect= CASelectBlockParser.getForIdCategorieSelectBlock(objSession);
              		
	if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCategorieSelect)) { %>
		<tr>
			<td>Kategorie des Abrechnungskontos</td>
			<td>
				<%=selectCategorieSelect%>
				<script type="text/javascript">
					selectionner('forIdCategorie', '<%=viewBean.getForIdCategorie()%>');
				</script>
			</td>
		</tr>
	<% } %>        
          <TR>
          	<TD>Sektionstyp</TD>
            <TD> 
              <select name="idTypesSections" size="5" multiple>
                <% 
                for (Iterator typesIter = viewBean.getTypesSections().iterator(); typesIter.hasNext();) {
				   	CATypeSection tempTypeSection = (CATypeSection) typesIter.next();
				%>
                <option value="<%=tempTypeSection.getIdTypeSection()%>"<%=viewBean.isSelectedIdTypeSection(tempTypeSection.getIdTypeSection())?" selected":""%>><%=tempTypeSection.getDescription()%></option>
                <% } %>
              </select>
            </TD>
            <TD>Rolle</TD>
            <TD> 
              <select name="idRoles" size="5" multiple>
              	
                <% 
                for (Iterator rolesIter = viewBean.getRoles().iterator(); rolesIter.hasNext();) {
				   	CARole tempRole = (CARole) rolesIter.next();
				%>
                <option value="<%=tempRole.getIdRole()%>"<%=viewBean.isSelectedIdRole(tempRole.getIdRole())?" selected":""%>><%=tempRole.getDescription()%></option>
                <% } %>
              </select>
            </TD>
          </TR>
          <TR>
          	<TD colspan="4"><HR></TD>
          </TR>
          <%
			Iterator i = seq.getContainer().iterator();
			while (i.hasNext()) {
				COSequence sequence = (COSequence) i.next();
	         	request.setAttribute("globaz.aquila.vb.process.COProcessContentieuxViewBean.SEQUENCE", sequence);
          %>
          <TR id="sequ_<%=sequence.getIdSequence()%>" <%=sequence.getLibSequence().equals(COSequence.CS_SEQUENCE_AVS)?"":"style='display:none'"%>>
		  	<TD colspan="4">
	          <jsp:include flush="true" page="listeEtapes.jsp"/>
		  	</TD>
          </TR>
          <% } %>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>