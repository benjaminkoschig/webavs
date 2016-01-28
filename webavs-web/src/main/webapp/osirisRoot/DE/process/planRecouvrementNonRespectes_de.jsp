<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.print.itext.list.CAIListPlanRecouvNonRespectes"%>
<%@ page import="globaz.globall.util.JACalendar"%>
<%@ page import="globaz.osiris.db.process.CAPlanRecouvrementNonRespectesViewBean"%>
<%@ page import="globaz.osiris.db.comptes.CATypeSection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="globaz.osiris.db.comptes.CARole" %>
<%
	idEcran="GCA60009";
	CAPlanRecouvrementNonRespectesViewBean viewBean = (CAPlanRecouvrementNonRespectesViewBean)session.getAttribute("viewBean");

	subTableWidth="0";

	// mettre directement la bonne valeur pour appeller le process
	userActionValue="osiris.process.planRecouvrementNonRespectes.executer";
	
	int size = viewBean.getSession().getUserEMail().length()+5;
	if(size<20){
		size=20;
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function GereControle(Controleur, ControleActiv, ControleDesactiv) {
	var objControleur = document.getElementById(Controleur);
	var objControleActiv = document.getElementById(ControleActiv);
	var objControleDesactiv = document.getElementById(ControleDesactiv);

	objControleActiv.style.display='';
	objControleDesactiv.style.display='none';

	return true;
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Nicht eigehaltene Zahlungsvereinbarungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	</tbody>
</table>
<table cellspacing="0" cellpadding="0" style="height: 100px; width: 100%">
	<tbody>
	<tr>
		<td class="label">E-Mail</td>
		<td class="control" colspan="3"><input type="text" name="eMailAddress" value="<%=viewBean.getSession().getUserEMail()%>" size="<%=size%>"></td>
	</tr>
	<tr>
		<td colspan="4"><hr></td>
	</tr>
	<tr>
		<td class="label">Aufgabe auszuf�hren</td>
		<td class="control" colspan="3">
			<!-- La balise <label> permet de selectionner le bouton radio en cliquant sur la ligne. Les id doivent etre renseign�s -->
			<input type="radio" id="radio_10" name="tacheEff" value="1" onClick="GereControle('radio_30', 'susp', 'rappel');" checked >&nbsp;<label for="radio_10">Liste der der nicht eingehaltenen Zahlungsvereinbarungen drucken</label><br />
			<ct:ifhasright element="osiris.process.planRecouvrementNonRespectes" crud="u">
				<input type="radio" id="radio_20" name="tacheEff" value="2" onClick="GereControle('radio_30', 'susp', 'rappel');">&nbsp;<label for="radio_20">Automatische Annullierung der nicht eingehaltenen Zahlungsvereinbarungen</label><br />
				<input type="radio" id="radio_30" name="tacheEff" value="3" onClick="GereControle('radio_30', 'rappel', 'susp');"<% if (!viewBean.isRappelSurPlan()) { %> disabled<% } %> >&nbsp;<label for="radio_30">Mahnungen ausdrucken</label><br />
				<input type="radio" id="radio_40" name="tacheEff" value="4" onClick="GereControle('radio_30', 'rappel', 'susp');"<% if (!viewBean.isRappelSurPlan()) { %> disabled<% } %> >&nbsp;<label for="radio_40">Mahnungen mit Terminkalender ausdrucken</label>
			</ct:ifhasright>
		</td>
	</tr>
	<tr>
		<td colspan="4"><hr></td>
	</tr>
	<TR>
		<td>Art des Druckens</td>
        <TD>
        	<input type="radio" name="typeImpression" value="pdf" <%="pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>PDF&nbsp;
        	<input type="radio" name="typeImpression" value="xls" <%="xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "" %>/>Excel
        </TD>
    </TR>
	<tr>
		<td colspan="4"><hr></td>
	</tr>
	<tr id="susp">
		<td class="label">Frist in Tage f�r die Annullierung</td>
		<td class="control" colspan="3"><input type="text" name="delaiSuspension" size="10" style="text-align:right;" value="<%=globaz.osiris.application.CAParametres.getDelaiSuspension(new globaz.globall.db.BTransaction(viewBean.getSession()))%>"></td>
	</tr>
	<tr id="rappel" style='display:none'>
		<td class="label">Frist im Tag f�r die Mahnung</td>
		<td class="control"><input type="text" name="delaiRappel" size="10" style="text-align:right;" value="<%=globaz.osiris.application.CAParametres.getDelaiRappel(new globaz.globall.db.BTransaction(viewBean.getSession()))%>"></td>
	</tr>
	<tr>
		<td class="label">Stichtag</td>
		<td class="control"><ct:FWCalendarTag name="dateRef" doClientValidation="CALENDAR" value=""/></td>
	</tr>
	<tr>
		<td class="label" >Sortierung der Abrechnungskonti</td>
		<td class="control">
			<select name="triCA">
				<option value="<%=CAIListPlanRecouvNonRespectes.TRICA_NUMERO%>">Nummer</option>
				<option value="<%=CAIListPlanRecouvNonRespectes.TRICA_NOM%>">Name</option>
			</select>
		</td>
	</tr>
	<tr>
		<td colspan="4"><hr></td>
	</tr>
    <tr>
         <td>Ab der Abr-Nr.</td>
         <td><input type="text" name="fromNoAffilie" value="<%=viewBean.getFromNoAffilie()%>"></td>
         <td>&nbsp;&nbsp;bis</td>
         <td><input type="text" name="beforeNoAffilie" value="<%=viewBean.getBeforeNoAffilie()%>"></td>
    </tr>

          <tr>
            <td>Kontoart</td>
            <td>
              <select name="idRoles" size="5" multiple>
                <%
	                for (Iterator rolesIter = viewBean._getRoles().iterator(); rolesIter.hasNext();) {
					   	CARole tempRole = (CARole) rolesIter.next();
				%>
                <option value="<%=tempRole.getIdRole()%>"<%=viewBean.isSelectedIdRole(tempRole.getIdRole())?" selected":""%>><%=tempRole.getDescription()%></option>
                <% 	} %>
              </select>
            </td>
          </tr>
    <tr>
    	<td colspan="4">&nbsp;</td>
    </tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>