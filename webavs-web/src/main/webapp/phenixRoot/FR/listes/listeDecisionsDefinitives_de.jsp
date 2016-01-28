<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.phenix.db.listes.CPListeDecisionsDefinitivesViewBean"%>
<%@page import="globaz.phenix.db.principale.CPDecision"%>
<%@page import="java.util.HashSet"%>
<%@page import="globaz.naos.util.AFUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	idEcran="CCP2014";

	//Récupération des beans
	CPListeDecisionsDefinitivesViewBean viewBean = (CPListeDecisionsDefinitivesViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "phenix.listes.listeDecisionsDefinitives.executer";
	
	HashSet exceptType = new java.util.HashSet();
	exceptType.add(CPDecision.CS_ACOMPTE);
	exceptType.add(CPDecision.CS_RECTIFICATION);
	exceptType.add(CPDecision.CS_CORRECTION);
	
	HashSet exceptGenre = new java.util.HashSet();
	exceptGenre.add(CPDecision.CS_NON_SOUMIS);
	exceptGenre.add(CPDecision.CS_AGRICULTEUR);
	exceptGenre.add(CPDecision.CS_RENTIER);
	exceptGenre.add(CPDecision.CS_ETUDIANT);
	exceptGenre.add(CPDecision.CS_FICHIER_CENTRAL);
	exceptGenre.add(CPDecision.CS_TSE);
	
    int autoDigiAff = AFUtil.getAutoDigitAff(session);
    String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
    
   	String decisionActiveChecked = viewBean.getDecisionActive().booleanValue() ? "checked=\"checked\"" : "";
   	
   	String tmpTypeDecision = CPDecision.CS_DEFINITIVE;
   	if(!JadeStringUtil.isEmpty(viewBean.getTypeDecision())) {
   		tmpTypeDecision = viewBean.getTypeDecision();
   	}
   	String tmpGenreDecision = "";
   	if(!JadeStringUtil.isEmpty(viewBean.getGenreDecision())) {
   		tmpGenreDecision = viewBean.getGenreDecision();
   	}
%>

<%-- /tpl:put --%><%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<SCRIPT language="JavaScript">
	top.document.title = "Web@AVS - <ct:FWLabel key='COTISATION_PERSONNELLE'/>";
		
	function init() {
	}

	function postInit() {
	}
		
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_LISTE_DECISION_DEFINITIVE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

				
	<tr>
    	<td><ct:FWLabel key="DATE_INFO"/></td>
    	<td>
    		<ct:FWCalendarTag name="dateDebut" doClientValidation="CALENDAR" value='<%=viewBean.getDateDebut() != null ? viewBean.getDateDebut() : ""%>'/>
    		&nbsp;&nbsp;<ct:FWLabel key="DATE_AU"/>&nbsp;&nbsp;
    		<ct:FWCalendarTag name="dateFin" doClientValidation="CALENDAR" value='<%=viewBean.getDateFin() != null ? viewBean.getDateFin() : ""%>'/>
    	</td>
    </tr>	
	<tr>
		<td><ct:FWLabel key='NUM_AFFILIE'/></td>
		<td><ct:FWPopupList name="fromAffilie" 
					value='<%=viewBean.getFromAffilie() != null ? viewBean.getFromAffilie() : ""%>'
					className="libelle" 
					jspName="<%=jspLocation%>" 
					autoNbrDigit="<%=autoDigiAff%>" 
					size="15"
					minNbrDigit="3"/>
		&nbsp;&nbsp;<ct:FWLabel key='FIN_SELECTION'/>&nbsp;&nbsp;
		     <ct:FWPopupList name="toAffilie" 
					value='<%=viewBean.getToAffilie() != null ? viewBean.getToAffilie() : ""%>'
					className="libelle" 
					jspName="<%=jspLocation%>" 
					autoNbrDigit="<%=autoDigiAff%>" 
					size="15"
					minNbrDigit="3"/>
		</td>
	</tr>
	<tr>
    	<td><ct:FWLabel key="TYPE_DECISION"/></td>
    	<td><ct:FWCodeSelectTag name="typeDecision" defaut="<%=tmpTypeDecision%>" codeType="CPTYPDECIS" wantBlank="false" except="<%=exceptType%>"/></td>
    </tr>
	<tr>
    	<td><ct:FWLabel key="GENRE_DECISION"/></td>
    	<td><ct:FWCodeSelectTag name="genreDecision" defaut="<%=tmpGenreDecision%>" codeType="CPGENDECIS" wantBlank="true" except="<%=exceptGenre%>" /></td>
    </tr>
	<TR>
		<TD><ct:FWLabel key="ANNEE"/></TD>
		<TD><INPUT type="text" onkeypress="return filterCharForPositivInteger(window.event);" name="annee" id="annee" maxlength="4" size="4" value="<%= viewBean.getAnnee() != null ? viewBean.getAnnee() : "" %>"></TD>
	</TR>
	<TR>
		<TD><ct:FWLabel key="UNIQUEMENT_DECISIONS_ACTIVES"/></TD>
		<TD><INPUT type="checkbox" name="decisionActive" id="decisionActive" <%=decisionActiveChecked%>></TD>
	</TR>
	<TR>
       	<TD width="23%" height="2"><ct:FWLabel key="EMAIL"/></TD>
       	<TD height="2"><INPUT type="text" name="email" maxlength="40" size="40" style="width:8cm;" data-g-string="mandatory:true" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>"></TD>
     </TR>
          				
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>