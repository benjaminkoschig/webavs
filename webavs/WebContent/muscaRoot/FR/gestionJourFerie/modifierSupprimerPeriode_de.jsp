 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%> 

	<%@page import="globaz.musca.db.gestionJourFerie.FAGestionJourFerieUtil"%>
	<%@page import="globaz.musca.db.gestionJourFerie.FAModifierSupprimerPeriodeViewBean"%>

	<%
		idEcran="?";
		FAModifierSupprimerPeriodeViewBean viewBean = (FAModifierSupprimerPeriodeViewBean) session.getAttribute("viewBean");
		
		okButtonLabel = viewBean.getOperationARealiser();
		
		userActionValue = "musca.gestionJourFerie.modifierSupprimerPeriode.executer";
	%>
			
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>

<%@ include file="/theme/process/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%> 
	<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart.jspf" %>

<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_MODIFIER_SUPPRIMER_FERIE_SELECTIONNE"/><%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart2.jspf" %>

<%-- tpl:put name="zoneMain" --%> 

	<TR>
		<TD><ct:FWLabel key="JOUR_TRAITE"/></TD>
		<TD><textarea name="jourTraite" cols="40" rows="3" class="input" readonly="readonly"><%=viewBean.getContenuForTextareaJourTraite()%></textarea></TD>
	</TR>
	
	 <TR>
		<TD>&nbsp;</TD>
		<TD>&nbsp;</TD>
	</TR>
	
	<%if(FAModifierSupprimerPeriodeViewBean.OPERATION_MODIFIER.equalsIgnoreCase(viewBean.getOperationARealiser())){ %>
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
	
	<%}%>
	
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