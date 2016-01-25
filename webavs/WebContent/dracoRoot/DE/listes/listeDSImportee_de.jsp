 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%
	idEcran="CDS2007";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.draco.db.listes.*"%>
<%
	//Récupération des beans
	DSListeDSImporteeViewBean viewBean = (DSListeDSImporteeViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "draco.listes.listeDSImportee.executer";
%>

<%@page import="globaz.draco.db.listes.DSListeDSImporteeViewBean"%><SCRIPT language="JavaScript">
top.document.title = "Draco - Liste der importierten LB"
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
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste der Lohnbescheinigungen nach ihrer Herkunft<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
						<TD>Von</TD>
						<td>
						<SELECT name="provenance" id="provenance" class="libelleLong" >
							<OPTION selected="selected" value='1'><%=viewBean.getSession().getLabel("PROVENANCE_PUCS") %></OPTION>
							<OPTION value='2' ><%=viewBean.getSession().getLabel("PROVENANCE_DAN") %></OPTION>
							<OPTION value='3'><%=viewBean.getSession().getLabel("PROVENANCE_PUCS_CCJU") %></OPTION>
							<OPTION value='4'><%=viewBean.getSession().getLabel("PROVENANCE_SWISSDEC") %></OPTION>
						</SELECT>
						</td>
						</TR>
						<TR>
							<TD>Jahr</TD>
						 	<TD> 
              				<INPUT type="text" name="anneeDeclaration" value="<%=globaz.globall.util.JACalendar.getYear(globaz.globall.util.JACalendar.todayJJsMMsAAAA())%>" maxlength="4" size="4">
            			</TD>
						</TR>
						 <tr>
            				<TD width="180">Referenz</TD>
            				<TD width="400"> 
              				<ct:FWCalendarTag name='periodeReferenceDebut' value="" errorMessage="Das Beginndatum ist falsch." doClientValidation="CALENDAR"/>
			 bis  
              <ct:FWCalendarTag name='periodeReferenceFin' value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>" errorMessage="Das Enddatum ist falsch." doClientValidation="CALENDAR"/>
			</TD>
			</tr>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR>
						<TR> 
  				          	<TD>E-Mail Adresse</TD>
 				          	<TD> 
								<INPUT name="eMailAddress" size="40" type="text" style="text-align : left;" value="<%=viewBean.getSession().getUserEMail()%>">                        
						  	<TD>&nbsp;</TD>
          				</TR>
          				<TR>
          					<TD>&nbsp;</TD>
                    	</TR> 
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>