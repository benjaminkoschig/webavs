<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA2020"; %>
<%@ page import="globaz.osiris.db.print.*" %>
<%@ page import="globaz.osiris.db.comptes.CACompteCourant" %>
<%@ page import="globaz.osiris.db.comptes.CACompteCourantManager" %>
<%@ page import="globaz.osiris.servlet.action.CADefaultServletAction" %>
<%
CAListSuiviPaiementsAutresTachesViewBean viewBean = (CAListSuiviPaiementsAutresTachesViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME + ".print.listSuiviPaiementsAutresTaches.executer"; 
%>
top.document.title = "Liste suivi paiements pour autres tâches - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste suivi paiements pour autres tâches<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <tr> 
            <td width="128">E-mail</td>
            <td> 
              <input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
            </td>
          </tr>		
          
          		<tr>
	            	<td width="128">P&eacute;riode</td>
	            	<td>
	            	<ct:FWCalendarTag displayType ="month" name="forMonthYear" doClientValidation="CALENDAR" value=""/>&nbsp;mois.année 
	            	</td>
	        	</tr>
	        		        	
				<tr>
	            	<td width="128">Compte Courant</td>
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