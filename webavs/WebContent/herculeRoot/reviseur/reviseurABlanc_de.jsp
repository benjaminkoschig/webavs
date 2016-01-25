<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CCE0013";
	globaz.hercule.db.reviseur.CEReviseurABlancViewBean viewBean = (globaz.hercule.db.reviseur.CEReviseurABlancViewBean)session.getAttribute ("viewBean");
	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.reviseur.reviseurABlanc.executer";
	
	// Récupération des réviseurs à lister
	CEReviseurManager reviseurs = viewBean._getReviseursList();
%>	

<%@page import="java.util.Iterator"%>
<%@page import="globaz.hercule.db.reviseur.CEReviseurManager"%>
<%@page import="globaz.hercule.db.reviseur.CEReviseur"%>

<script>
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_ECRAN_REVISEUR_A_BLANC"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD nowrap colspan="2" style="font-weight : bolder;">
			<ct:FWLabel key="DESCRIPTION_FONCTION_REVISEUR_A_BLANC"/>	
		</TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="REVISEUR"/></TD>
		<TD>
			<SELECT name="idReviseur">
		   		<OPTION value=""></OPTION>
		   		<%
		   			for (Iterator iter = reviseurs.iterator(); iter.hasNext();) {
						CEReviseur reviseur = (CEReviseur) iter.next();
				%>
				<OPTION value="<%=reviseur.getIdReviseur()%>" <%= reviseur.getIdReviseur().equals(viewBean.getIdReviseur()) ? "selected=\"selected\"" : ""%> ><%=reviseur.getVisa()%></OPTION>
				<%}%>
			</SELECT>   							
		</TD>
	</TR>
	<TR>
		<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
	    <TD height="2"> 
	    	<INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmail()%>">
	    </TD>
	</TR>
	
<%-- /tpl:put --%>		
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>