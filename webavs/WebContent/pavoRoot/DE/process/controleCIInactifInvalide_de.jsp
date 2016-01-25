<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.pavo.db.process.CIControleCIInactifInvalideViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	idEcran="CCI3013";

	//Récupération des beans
	CIControleCIInactifInvalideViewBean viewBean = (CIControleCIInactifInvalideViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "pavo.process.controleCIInactifInvalide.executer";
%>

<SCRIPT language="JavaScript">
	top.document.title = "<ct:FWLabel key='JSP_CI_CONTROLE_CI_INACTIF_INVALIDE_TITLE'/>";
</SCRIPT>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_CI_CONTROLE_CI_INACTIF_INVALIDE_TITLE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
				
		<TR>
        	<TD><ct:FWLabel key="JSP_CI_CONTROLE_CI_INACTIF_INVALIDE_MAIL"/></TD>
        	<TD> 
          		<ct:inputText name="eMailAddress" id="eMailAddress" defaultValue="<%=viewBean.getEmailAddress()%>" notation="data-g-email='mandatory:true'" style="width: 250px" />
        	</TD>
      	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>