 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="CCP1025";%>
<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="java.util.LinkedList" %>
<%
	globaz.phenix.process.communications.CPProcessValidationSelectionJournalRetourViewBean viewBean = (globaz.phenix.process.communications.CPProcessValidationSelectionJournalRetourViewBean) session.getAttribute("viewBean");
	userActionValue = "phenix.communications.validationJournalRetour.processValiderSelection";
	selectedIdValue = "";
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Der Prozess wurde erfolgreich gestartet.";
}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Validierung";
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Validierung der nach den steuerlichen Daten berechneten Verfügungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
							<TR>
								<TD width="300px"><b><u><%=viewBean.getNombreJournauxAValider()%></u> Steuermeldungen werden validiert sein </b></TD>
							</TR>
							<TR>
								<TD width="300px"><b></b></TD>
							</TR>	
								<TR>
					            <TD width="300px">E-Mail Adresse</TD>
					            <TD><input type="text" name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value="<%=viewBean.getEMailAddress()%>"></TD>
					    </TR>
					    <tr> 
				            <TD width="300px">Passage</TD>
				            <TD> 
				        	<INPUT type="text" name="idJournalFacturation" class="libelleCourt" value="<%=viewBean.getIdJournalFacturation()%>">
				               <%
								Object[] psgMethodsName = new Object[]{
								new String[]{"setIdJournalFacturation","getIdPassage"},
								new String[]{"setLibellePassage","getLibelle"}
								};
								Object[] psgParams= new Object[]{};
								String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/communications/validationSelectionJournalRetour_de.jsp";	
								%>
								<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
							    <ct:FWSelectorTag 
								name="passageSelector" 
								
								methods="<%=psgMethodsName%>"
								providerPrefix="FA"			
								providerApplication ="musca"			
								providerAction ="musca.facturation.passage.chercher"			
								providerActionParams ="<%=psgParams%>"
								redirectUrl="<%=redirectUrl%>"			
								/>
								</ct:ifhasright>
								<input type="hidden" name="selectorName" value="">
				            </TD>
            			</tr>
            			<tr> 
				            <TD width="300px"></TD>
				            <TD> 
				              <INPUT type="text" name="libellePassage" class="libelleLongDisabled" value="<%=viewBean.getLibellePassage()%>" readonly>
				            </TD>
          				</tr>
						<INPUT type="hidden" name="forAnnee" value="<%=viewBean.getForAnnee()%>">
						<INPUT type="hidden" name="likeNumAffilie" value="<%=viewBean.getLikeNumAffilie()%>">
						<INPUT type="hidden" name="forGrpTaxation" value="<%=viewBean.getForGrpTaxation()%>">
						<INPUT type="hidden" name="forGrpExtraction" value="<%=viewBean.getForGrpExtraction()%>">
						<INPUT type="hidden" name="forJournal" value="<%=viewBean.getForJournal()%>">
						<INPUT type="hidden" name="forGenreAffilie" value="<%=viewBean.getForGenreAffilie()%>">
						<INPUT type="hidden" name="forTypeDecision" value="<%=viewBean.getForTypeDecision()%>">
						<INPUT type="hidden" name="forStatus" value="<%=viewBean.getForStatus()%>">
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>