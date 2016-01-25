 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCO2004"; %>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="globaz.aquila.servlet.CODefaultServletAction" %>
<%@ page import="globaz.aquila.db.print.*" %>
<%@ page import="globaz.aquila.db.access.batch.*"%>
<%@page import="globaz.globall.util.JACalendarGregorian"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.osiris.db.comptes.*"%>
<%@page import="globaz.aquila.db.access.paiement.COPaiementManager"%>
<%
COListPaiementExcelViewBean viewBean = (COListPaiementExcelViewBean) session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
userActionValue = "aquila.print.listPaiementExcel.executer"; 
%>
top.document.title = "Liste des paiements concernant le contentieux " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste des paiements concernant le contentieux (Excel)<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
        <td style="width:200;" >E-mail</td>
        <td colspan="3"> 
			<input type="text" name="eMailAddress" value="<%=viewBean.getEMailAddress()%>" class="libelleLong">
		</td>
</tr>	

<TR >
	<TD style="width:200;">Date de début des opérations</TD>	
	<%
	JACalendarGregorian jDate = new JACalendarGregorian();
	String tmp = jDate.addDays(JACalendarGregorian.today(), -1).toStr(".");
	%>	
	<td style="width:300;">
	<ct:FWCalendarTag name="fromDate" value="<%=tmp%>" />
	</td>	
	<TD >Date de fin des opérations</td>	
	<TD>
	<ct:FWCalendarTag name="untilDate" value="" />
	</TD>
 	
 </TR>
 
<TR >          
	<TD style="width:200;">Sélection des opérations</TD>	
	<TD>
		<select name="forIdTypeOperation">
			<option
				value="<%=COPaiementManager.FOR_ALL_IDTYPEOPERATION%>">Toutes</option>
			<%
				CATypeOperation tempTypeOperation;
				CATypeOperationManager manTypeOperation = new CATypeOperationManager();
				ArrayList liste = new ArrayList();
				liste.add("A");
				liste.add("AP");
				liste.add("E");
				liste.add("EP");
				liste.add("EPB");
				liste.add("EPR");
				liste.add("EC");
				manTypeOperation.setForIdTypeOperationIn(liste);
				manTypeOperation.setSession(viewBean.getSession());
				manTypeOperation.find();
				for (int i = 0; i < manTypeOperation.size(); i++) {
					tempTypeOperation = (CATypeOperation) manTypeOperation.getEntity(i);
					if (!tempTypeOperation.getIdTypeOperation().equalsIgnoreCase("PEND"))
			%>
			<option value="<%=tempTypeOperation.getIdTypeOperation()%>">
			<%=tempTypeOperation.getDescription()%></option>
			<%
			}
			%>
		</select>
	</TD>		
	<TD style="width:200;">Sélection des étapes</TD>	
	<TD><select name="forSelectionEtapes" class="libelleCourt">
		<% String sForSelectionEtapes = request.getParameter("forSelectionEtapes");
		   if (sForSelectionEtapes == null)
			   sForSelectionEtapes ="1000";
		%>
               <option <%=(sForSelectionEtapes.equals("1000")) ? "selected" : "" %> value="1000">toutes</option>
               <option <%=(sForSelectionEtapes.equals("1")) ? "selected" : "" %> value="1">Jusqu'à RP</option>
               <option <%=(sForSelectionEtapes.equals("2")) ? "selected" : "" %> value="2">RP et suivantes</option>
              </select>
     </TD>
</tr>

	


<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>