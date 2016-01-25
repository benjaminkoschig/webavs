<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "0195GCA"; %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.db.comptes.CACompteCourant" %>
<%@ page import="globaz.osiris.db.comptes.CACompteCourantManager" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CAListDecompteAnnuelFondsViewBean viewBean = (CAListDecompteAnnuelFondsViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);

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
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listDecompteAnnuelFonds.executer"; 
%>
top.document.title = "Jährliche Abrechnung anderer Aufgaben - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Jährliche Abrechnung anderer Aufgaben<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <tr> 
            <td width="128">E-Mail</td>
            <td> 
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>		
				<tr>
					<td>Periode </td>
					<td>von <ct:FWCalendarTag name="dateDebutPeriode" value="" /> <span style="margin-left:10px">bis</span> <ct:FWCalendarTag name="dateFinPeriode" value="" /></td>
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
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>