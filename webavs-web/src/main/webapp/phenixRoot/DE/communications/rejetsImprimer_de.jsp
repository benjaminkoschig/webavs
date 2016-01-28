 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@page import="globaz.phenix.db.communications.CPRejetsListViewBean"%>
<%@page import="globaz.phenix.db.communications.CPRejetsViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="CCP1040";
    CPRejetsListViewBean viewBean = new CPRejetsListViewBean();
	selectedIdValue = "";
	userActionValue = "phenix.communications.rejets.imprimer";
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();

String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function init(){
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der Zurückweisungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
<TR>
				            <TD nowrap>Mitglieder-Nr. (Kassen Referenz)</TD>
							<td>
				            	<INPUT name="likeYourBusinessReferenceId" class="libelleShort"/>
					       	</td>
					       	<td nowrap>Geschäftsreferenz (FISKUS)</td>
							<TD width=50%>
								<INPUT name="likeOurBusinessReferenceId" class="libelleShort"/>
							</TD>
					     	<TD nowrap>Kanton</TD>
					     	<TD nowrap>
						 	<%
								java.util.HashSet except = new java.util.HashSet();
								except.add(IConstantes.CS_LOCALITE_ETRANGER);
							%>
							<ct:FWCodeSelectTag name="forCanton"
							        defaut=""
									wantBlank="<%=true%>"
							        codeType="PYCANTON"
									libelle="codeLibelle"
									except="<%=except%>"
							/>
					     	</TD>
					    </TR>
					    <TR>
				            <TD nowrap>Steuer-Nr.</TD>
							<td>
				            	<INPUT name="likeNumContribuable" class="libelleShort"/>
					       	</td>
					       	<td nowrap>Jahr</td>
							<TD width=50%>
								<INPUT name="forAnnee" class="numeroCourt"/>
							</TD>
					    </TR>
						<TR>
				            <td nowrap>Nachricht ID</td>
							<TD width=50%>
								<INPUT name="likeMessageId" class="libelleShort"/>
							</TD>
							<TD nowrap>Nachricht Referenz ID</TD>
					     	<TD nowrap>
								<INPUT name="likeReferenceMessageId" class="libelleShort"/>
					     	</TD> 
					     	<TD nowrap>Person Nummer</TD>
					     	<TD nowrap>
								<INPUT name="likePersonId" class="libelleShort"/>
					     	</TD> 
				          </TR>
				         <TR>
				         	<TD nowrap>Name</TD>
					     	<TD nowrap>
								<INPUT name="likeOfficialName" class="libelleShort"/>
					     	</TD> 
				            <TD nowrap>Vorname</TD>
				            <TD width=50%>
								<INPUT type="text" name="likeFirstName"  class="libelleShort">
							</TD>
				            <TD nowrap>Grund der Zurückweisung</TD>
				            <TD nowrap>
				            <%
			        		java.util.Vector listeRejets = globaz.phenix.db.communications.CPRejets.getListRejets(session);
							%>
							<ct:FWListSelectTag name="forReasonOfRejection"
							defaut=""
	            			data="<%=listeRejets%>"/>
				            </TD>
				        </TR>
				        <TR>
				        	<TD nowrap>Status</TD>
				            <TD nowrap>
				            <ct:FWCodeSelectTag name="forEtat"
								defaut=""
								wantBlank="<%=true%>"
							    codeType="CPETATREJ"
								/>
				            </TD>
				        </TR>
				        <TR>
							<TD height="2">Gesendete anzeigen</TD>
							<TD nowrap height="31" width="259"><input type="checkbox" name="wantEnvoye" id="wantEnvoye"></TD>
							<TD height="2">Annulierte anzeigen</TD>
							<TD nowrap height="31" width="259"><input type="checkbox" name="wantAbandonne" id="wantAbandonne"></TD>
					   </TR>
				        <TR>
            				<TD height="2" width="165">E-Mail</TD>
            				<TD height="2" width="513">
            				<input name='eMailAddress' id='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=objSession.getUserEMail()%>' >
              				</TD>
          				</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>