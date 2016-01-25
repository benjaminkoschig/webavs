<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@ page import="globaz.naos.db.avisMutation.*" %>
<%
idEcran = "CAF03012";
	//Récupération des beans
	AFAvisMutationViewBean viewBean = (AFAvisMutationViewBean) session.getAttribute("viewBean");
	subTableWidth = "700";
	userActionValue = "naos.avisMutation.avisMutation.imprimer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
function init() { } 

function onOk() {
	document.forms[0].submit();
} 

function onCancel() {
	document.forms[0].elements('userAction').value="back";
	document.forms[0].submit();
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Auswahl des Ausdrucks<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" colspan="2"/>
						<% } %>
						<TR>
							<TD height="13">E-Mail :</TD>
							<TD colspan="2"><INPUT type="text" name="email" value="<%=viewBean.getSession().getUserEMail()%>"></TD>
						</TR>
						<TR>
							<TD height="13">Meldungsdatum</TD>
							<TD colspan="2"><INPUT type="text" name="dateAvis" size="11" maxlength="10" value="<%=viewBean.getDateAvis()%>"></TD>
						</TR>
						<TR>
							<TD height="13">Meldungstyp : </TD>
							<TD colspan="2"> 
          						<input type="radio" name="selectionChangement" value="2" checked>Neue Erfassung
          					</TD>
           				</TR>
         				<TR>
         					<TD>&nbsp;</td>
          					<TD> 
          						<input type="radio" name="selectionChangement" value="1">Änderung der Erfassung
          					</TD>
          					<TD>
          						<INPUT type="checkbox" name="adresseChange" value="true">Adresse 
          						<INPUT type="checkbox" name="cantonChange" value="true"> mit Kantonwechsel
          					</TD>
          				</TR>
         				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="nomChange" value="true">Name
          					</TD>
          					
          				</TR>
          				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="avsChange" value="true">AHV
          					</TD>
          					
          				</TR>
          				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="pmChange" value="true">Hauspersonal
          					</TD>
          					
          				</TR>
          				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="siegeChange" value="true">Sitz
          					</TD>
          					
          				</TR>
						<TR>
							<TD height="13">&nbsp;</TD>
							<TD colspan="2"> 
          						<input type="radio" name="selectionChangement" value="4">Abgang
          					</TD>
           				</TR>
           				<TR>
							<TD height="13">&nbsp;</TD>
							<TD colspan="2"> 
          						<input type="checkbox" name="selectionSedex" value="true">Sendung der Meldung über SEDEX
          					</TD>
           				</TR>
         				<TR>
							<TD><p>Beobachtungen</p><p>(maximal 150 Zeichen)</p></TD>
							<TD colspan="2"><TEXTAREA name="observations" cols="60" rows="4" data-g-string="sizeMax:150,isPasteOverload:true"></TEXTAREA></TD>
						</TR>
						</TR>
         				<TR>
							<TD colspan="3">&nbsp;</TD>
						</TR>
        				<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>