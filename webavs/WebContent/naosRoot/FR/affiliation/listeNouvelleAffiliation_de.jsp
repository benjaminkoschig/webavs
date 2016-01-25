<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.naos.db.affiliation.AFListeNouvelleAffiliationViewBean"%>

<%
	idEcran="CAF2014";

	//Récupération des beans
	AFListeNouvelleAffiliationViewBean viewBean = (AFListeNouvelleAffiliationViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton ok
	userActionValue = "naos.affiliation.listeNouvelleAffiliation.executer";
%>

<SCRIPT language="JavaScript">
top.document.title = "Web@AVS - Liste des nouvelles affiliations";
</SCRIPT>

<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">

	function validate() {
	    state = validateFields();
	
	    return state;
	}

</SCRIPT>


<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Liste des nouvelles affiliations<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<TR>			
		 	<TD>Dès le</TD>
			<TD>
				<ct:FWCalendarTag name="fromDateCreation" doClientValidation="CALENDAR"  value="" />
			</TD>
		</TR>
		
		<TR>			
		 	<TD>Jusqu'au'</TD>
			<TD>
				<ct:FWCalendarTag name="toDateCreation" doClientValidation="CALENDAR"  value="" />
			</TD>
		</TR>
		
		<TR>			
		 	<TD>Critère de sélection</TD>
			<TD nowrap>
				<ct:FWCodeSelectTag name="critereSelection"
				              defaut=""
						wantBlank="<%=false%>"
				        codeType="VECRISELEC"
						libelle="libelle"
				/>
	     	</TD>
		</TR>
		<TR>
        	<TD width="23%" height="2">Email</TD>
        	<TD height="2"> 
          		<INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmail()%>">
        	</TD>
      	</TR>
          										
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>