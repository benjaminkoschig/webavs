<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "0194GCA"; %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.db.comptes.CACompteCourant" %>
<%@ page import="globaz.osiris.db.comptes.CACompteCourantManager" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CAListNominativeFsfpViewBean viewBean = (CAListNominativeFsfpViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

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
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listNominativeFsfp.executer"; 
%>
top.document.title = "Namentliche Liste der Anderer Aufgaben - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Namentliche Liste der Anderer Aufgaben<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <tr> 
            <td width="128">E-Mail</td>
            <td> 
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>		
          
          		<tr>
	            	<td>Datum:</td>
					<td><ct:FWCalendarTag name="fromDateValeur" value="<%=viewBean.getFromDateValeur()%>"/></td>
	        	</tr>
	        		        	
				<tr>
	            	<td width="128">Kontokorrent</td>
	            	<td>
 					<select name="forIdCompteCourant" >
				   	<%
				   		CACompteCourant tempCC;
					 	CACompteCourantManager manCC = new CACompteCourantManager();
						manCC.setSession(viewBean.getSession());
						manCC.find();
						
						for(int i = 0; i < manCC.size(); i++) {
							tempCC = (CACompteCourant)manCC.getEntity(i); 
					%>
                		<option value="<%=tempCC.getIdCompteCourant()%>"><%=tempCC.getIdExterne()%>&nbsp;-&nbsp;<%=tempCC.getRubrique().getDescription()%></option>
		            <% 
		            	} 
		            %>
	              	</select>
		           	</td>
	        	</tr>

		<tr> 
            <td nowrap width="128">Kontoart</td>
            <td nowrap width="576"> 
              <select name="forSelectionRole" tabindex="2">
              	<%=CARoleViewBean.createOptionsTags(objSession, request.getParameter("forSelectionRole"))%>
              </select>
            </td>
            <td width="65">&nbsp;</td>
            <td width="67" nowrap>&nbsp;</td>
            <td nowrap width="167">&nbsp;</td>
		</tr>
		<tr>
			<td>Nur die Fälle die in Betreibung oder uneinbringlich sind auflisten : <input type="checkbox" name="seulPoursuiteIrrecouvrable"></td>
		</tr>
		<tr>
			<td>Zusammenfassung : <input name="listeResumee" size="20" type="checkbox" style="text-align : right;">
		</tr>	
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>