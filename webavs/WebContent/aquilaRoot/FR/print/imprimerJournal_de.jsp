<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCO2002"; %>
<%@ page import="globaz.aquila.db.print.*" %>
<%
COImprimerJournalViewBean viewBean = (COImprimerJournalViewBean) session.getAttribute("viewBean");

globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = "aquila.print.imprimerJournal.executer"; 
%>
top.document.title = "Impression des éléments du journal - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Impression des éléments du journal<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <tr> 
            <td width="128">E-mail</td>
            <td> 
              <input type="hidden" name="forIdJournal" value="<%=request.getParameter("selectedId")%>"/>
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>	
          
          <tr> 
            <td width="128">Liste</td>
            <td> 
            	<select name="typeListe">
            		<option value="<%=COImprimerJournalViewBean.IMPRIMER_ELEMENTS%>">Eléments du journal</option>
            		<option value="<%=COImprimerJournalViewBean.IMPRIMER_FICHIER_TS_OPGE%>">Présentation du fichier pour l'office de poursuite</option>
            		<option value="<%=COImprimerJournalViewBean.IMPRIMER_RECAPITULATIF%>">Liste des réquisitions de poursuite envoyées</option>
            	</select>
            </td>
          </tr>			
                 		        		
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>