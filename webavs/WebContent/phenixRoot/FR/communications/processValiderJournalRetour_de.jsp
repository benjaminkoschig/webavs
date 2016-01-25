<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	//Récupération des beans
	 globaz.phenix.db.communications.CPJournalRetourViewBean viewBean = (globaz.phenix.db.communications.CPJournalRetourViewBean) session.getAttribute("viewBean");	 
	idEcran="CCP1013";
	//Définition de l'action pour le bouton valider
	userActionValue = "phenix.communications.journalRetour.executerValider";
%>
<SCRIPT language="JavaScript">
top.document.title = "Phenix - Validation du journal retour"
</SCRIPT>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Validation du journal retour<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
							<tr>
								<td>Journal</td>
								<td> <INPUT name="nomJournal" class="libelleLongDisabled" readonly 
									value="<%=viewBean.getIdJournalRetour() + " - " + viewBean.getLibelleJournal()%>" type="text"> </td>
							</tr>
					        <TR>
					            <TD>Adresse E-Mail</TD>
					            <TD><input type="text" name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'></TD>
					        </TR>
							<input type="hidden" name='idJournalRetour' class='libelleLong' value='<%=viewBean.getIdJournalRetour()%>'>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>