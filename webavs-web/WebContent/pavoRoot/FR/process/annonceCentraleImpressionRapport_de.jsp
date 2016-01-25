<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.pavo.db.process.CIAnnonceCentraleImpressionRapportViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	idEcran="CCI2019";

	//Récupération des beans
	CIAnnonceCentraleImpressionRapportViewBean viewBean = (CIAnnonceCentraleImpressionRapportViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "pavo.process.annonceCentraleImpressionRapport.executer";
%>

<SCRIPT language="JavaScript">
	top.document.title = "<ct:FWLabel key='JSP_CI_ANNONCE_CENTRALE_IMPRESSION_RAPPORT_TITLE'/>";
</SCRIPT>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_IMPRESSION_RAPPORT_TITLE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

		<ct:inputHidden id="idAnnonceCentrale" name="idAnnonceCentrale" />
		<ct:inputHidden id="modeFonctionnement" name="modeFonctionnement" />
		
		<TR>
			<TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_PERIODE"/></TD>
			<TD>
				<ct:inputText id="moisAnneeCreation" name="moisAnneeCreation" disabled="disabled" readonly="readonly" style="width: 250px" />
			</TD>
		</TR>
		
		<TR>
			<TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_TYPE_LOT"/></TD>
			<TD>
				<ct:inputText id="libelleTypeAnnonce" name="libelleTypeAnnonce" disabled="disabled" readonly="readonly" style="width: 250px" />
			</TD>
		</TR>
				
		<TR>
			<TD></TD>
			<TD></TD>
		</TR>
					
		<TR>
        	<TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_MAIL"/></TD>
        	<TD> 
          		<ct:inputText name="eMailAddress" id="eMailAddress" defaultValue="<%=viewBean.getEmailAddress()%>" notation="data-g-email='mandatory:true'" style="width: 250px" />
        	</TD>
      	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>