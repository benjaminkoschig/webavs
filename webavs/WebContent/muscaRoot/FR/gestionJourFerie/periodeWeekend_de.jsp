 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%> 

	<%@ page import="globaz.musca.db.gestionJourFerie.FAPeriodeWeekendViewBean"%>
	<%@page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerieUtil"%>

	<%
		idEcran="?";
		FAPeriodeWeekendViewBean viewBean = (FAPeriodeWeekendViewBean) session.getAttribute("viewBean");
		userActionValue = "musca.gestionJourFerie.periodeWeekend.executer";
	%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>

<%@ include file="/theme/process/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%> 
	<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart.jspf" %>

<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_PERIODE_WEEKEND"/><%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart2.jspf" %>

<%-- tpl:put name="zoneMain" --%> 

	<TR>
		<TD><ct:FWLabel key="DE_DATE"/></TD>
		<TD><ct:FWCalendarTag name="dateDebut" value=""/> &nbsp;&nbsp;&nbsp; &nbsp; &nbsp; 
			<ct:FWLabel key="A_DATE"/> &nbsp; <ct:FWCalendarTag name="dateFin" value=""/> &nbsp;&nbsp;&nbsp;
		</TD>
	</TR>
	
	<TR>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
	</TR>
	
	<TR>
		<TD><ct:FWLabel key="LIBELLE"/></TD>
		<TD><textarea name="libelle" cols="40" rows="3" class="input"></textarea></TD>
	</TR>
	
   <TR>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
	</TR>
	
	<TR>
		<TD><ct:FWLabel key="DOMAINE"/></TD>
		<TD><%=FAGestionJourFerieUtil.getHtmlCheckBoxDomaineFerie(viewBean.getDomaineFerie(), viewBean.getSession())%></TD>
		<%=FAGestionJourFerieUtil.getHtmlNullDomain()%>
	</TR>
	
	<TR>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
	</TR>
	
	<TR>
    	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
        <TD height="2"> 
       		<INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmail()%>">
        </TD>
   </TR>
   
   <TR>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
	</TR>
   

<%-- /tpl:put --%>					
	
	<%@ include file="/theme/process/footer.jspf" %>

<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
	
	<%@ include file="/theme/process/bodyClose.jspf" %>

<%-- /tpl:insert --%>