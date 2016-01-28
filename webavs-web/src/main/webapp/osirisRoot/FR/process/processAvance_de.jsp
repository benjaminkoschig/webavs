<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA3015"; %>
<%
globaz.osiris.db.process.CAProcessAvanceViewBean viewBean = (globaz.osiris.db.process.CAProcessAvanceViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
userActionValue = "osiris.process.processAvance.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-Avance" showTab="options"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Process - Exécuter avances - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Exécuter avances<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		  <TR> 
            <TD align="left" width="180" height="21" valign="middle">E-mail</TD>
            <TD align="left"> 
              <INPUT type="text" name="eMailAddress" class="libelleLong" value="<%=viewBean.getEMailAddress()%>"/>
            </TD>
          </TR>	
          
          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Motif</TD>
            <TD align="left"> 
              <INPUT type="text" name="motif" value="" class="libelleLong">
            </TD>
          </TR>	          
          
          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Date valeur</TD>
            <TD align="left"> 
              <ct:FWCalendarTag name="executeForDate" doClientValidation="CALENDAR" value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>"/>
            </TD>
          </TR>	
          
          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Echéance</TD>
            <TD align="left"> 
				<SELECT id="forEcheanceType" name="forEcheanceType">
					<OPTION value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_ANNUELLE%>">Annuelle</OPTION>
					<OPTION selected value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_MENSUELLE%>">Mensuelle</OPTION>
					<OPTION value="<%=globaz.osiris.db.avance.CAAvanceViewBean.CS_ECHEANCE_TRIMESTRIELLE%>">Trimestrielle</OPTION>
				</SELECT>
            </TD>
          </TR>		
          
          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Mode</TD>
            <TD align="left"> 
            	<%
            	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
				globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
				%>
			<%@page import="globaz.osiris.db.avance.CAAvanceViewBean"%>
			<ct:FWListSelectTag data="<%=CAAvanceViewBean.modesPourUtilisateurCourant(objSession)%>" defaut="" name="forIdModeRecouvrement"/>			
            </TD>
          </TR>		    
          
          <TR> 
            <TD align="left" width="200" height="21" valign="middle">Rôle</TD>
            <TD align="left"> 
            	<SELECT name="forSelectionRole">
            		<%=globaz.osiris.db.comptes.CARoleViewBean.createOptionsTags(objSession, null)%>
            	</SELECT>
            </TD>
          </TR>		    
          
          <TR> 
            <TD align="left" width="180" height="21" valign="middle">Organe d'ex&eacute;cution d&eacute;sir&eacute;</TD>
            <TD align="left">
            	<SELECT size="width:100%" name="idOrganeExecution" class="libelleCourt">            
            		
            	<%
				globaz.osiris.db.ordres.CAOrganeExecutionManager csOrganeExecution = new globaz.osiris.db.ordres.CAOrganeExecutionManager();
				csOrganeExecution.setSession(objSession);
				csOrganeExecution.setForIdTypeTraitementOG(true);
				csOrganeExecution.find(); 
				
				for (int i=0; i < csOrganeExecution.size(); i++) {
					globaz.osiris.db.ordres.CAOrganeExecution organeExecution = (globaz.osiris.db.ordres.CAOrganeExecution) csOrganeExecution.getEntity(i);
					
					%>
					 <option selected value="<%=organeExecution.getIdOrganeExecution()%>"><%=organeExecution.getNom()%></option>
					<%
				}
				%>
			              
				</SELECT>
            </TD>
          </TR>           
          			
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>