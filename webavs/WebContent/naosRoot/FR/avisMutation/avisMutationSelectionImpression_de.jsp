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
			<%-- tpl:put name="zoneTitle" --%>Choix d'impression<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" colspan="2"/>
						<% } %>
						<TR>
							<TD height="13">E-mail:</TD>
							<TD colspan="2"><INPUT type="text" name="email" value="<%=viewBean.getSession().getUserEMail()%>"></TD>
						</TR>
						<TR>
							<TD height="13">Date de l'avis</TD>
							<TD colspan="2"><INPUT type="text" name="dateAvis" size="11" maxlength="10" value="<%=viewBean.getDateAvis()%>"></TD>
						</TR>
						<TR>
							<TD height="13">Type d'avis: </TD>
							<TD colspan="2"> 
          						<input type="radio" name="selectionChangement" value="2" checked>Nouvelle affiliation
          					</TD>
           				</TR>
         				<TR>
         					<TD>&nbsp;</td>
          					<TD> 
          						<input type="radio" name="selectionChangement" value="1">Mutation d'affiliation
          					</TD>
          					<TD>
          						<INPUT type="checkbox" name="adresseChange" value="true">Adresse 
          						<INPUT type="checkbox" name="cantonChange" value="true"> avec changement de canton
          					</TD>
          				</TR>
         				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="nomChange" value="true">Nom
          					</TD>
          					
          				</TR>
          				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="avsChange" value="true">AVS
          					</TD>
          					
          				</TR>
          				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="pmChange" value="true">Personnel de maison
          					</TD>
          					
          				</TR>
          				<TR>
         					<TD>&nbsp;</td>
          					<TD>&nbsp;</TD>
          					<TD>
          						<INPUT type="checkbox" name="siegeChange" value="true">Siege
          					</TD>
          					
          				</TR>
						<TR>
							<TD height="13">&nbsp;</TD>
							<TD colspan="2"> 
          						<input type="radio" name="selectionChangement" value="4">Radiation d'affiliation
          					</TD>
           				</TR>
           				<TR>
							<TD height="13">&nbsp;</TD>
							<TD colspan="2"> 
          						<input type="checkbox" name="selectionSedex" value="true">Envoi de l'avis via SEDEX
          					</TD>
           				</TR>
         				<TR>
							<TD><p>Observations</p><p>(maximum 150 caractères)</p></TD>
							<TD colspan="2"><TEXTAREA name="observations" cols="60" rows="4" data-g-string="sizeMax:150,isPasteOverload:true"></TEXTAREA></TD>
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