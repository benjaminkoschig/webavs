 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 

<%@ page import="globaz.hercule.db.controleEmployeur.*"%>
<%@page import="java.net.URLDecoder"%>

<%

	idEcran="CCE2004";
	
	//Récupération des beans
	CELettreLibreViewBean viewBean = (CELettreLibreViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "hercule.controleEmployeur.lettreLibre.executer";
	
	String idTiers = (String) request.getParameter("idTiers");
	String dateDebutControle = (String) request.getParameter("dateDebutControle");
	String dateFinControle = (String) request.getParameter("dateFinControle");
	String dateEffective = (String) request.getParameter("dateEffective");
	String visaReviseur = (String) request.getParameter("visaReviseur");
	String numAffilie = (String) request.getParameter("numAffilie");
	String idAffiliation = (String) request.getParameter("idAffiliation");
	String selectedId = (String) request.getParameter("selectedId");
	
	String dateEnvoi = viewBean.getDateEnvoi() != null ? viewBean.getDateEnvoi() : "";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 


<%@page import="globaz.jade.client.util.JadeStringUtil"%><SCRIPT language="JavaScript">
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
function init(){}
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_IMPRIMER_LETTRE_LIBRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<input type="hidden" name="idTiers" id="idTiers" value="<%=idTiers%>"/>
	<input type="hidden" name="dateDebutControle" id="dateDebutControle" value="<%=dateDebutControle%>"/>
	<input type="hidden" name="dateFinControle" id="dateFinControle" value="<%=dateFinControle%>"/>
	<input type="hidden" name="dateEffective" id="dateEffective" value="<%=dateEffective%>"/>
	<input type="hidden" name="visaReviseur" id="visaReviseur" value="<%=visaReviseur%>"/>
	<input type="hidden" name="idAffiliation" id="idAffiliation" value="<%=idAffiliation%>"/>
	<input type="hidden" name="selectedId" id="selectedId" value="<%=selectedId%>"/>
	
	<TR>
		<TD nowrap width="100" height="31"><ct:FWLabel key="NUMERO_AFFILIE"/></TD>
		<TD><INPUT type="text" name="numAffilie" id="numAffilie"  value="<%=numAffilie%>" size="15" style="background-color:#b3c4db;" readonly="readonly"/></TD>
	</TR>
	<TR>
		<TD>&nbsp;</TD>
		<TD><TEXTAREA name="infoTiers" id="infoTiers" cols="45" rows="3" style="overflow:hidden; background-color:#b3c4db;" readonly="readonly"><%=viewBean._getInfoTiers(idAffiliation)%></TEXTAREA></TD>
	</TR>
	<TR>
		<TD align="left"><ct:FWLabel key="DATE_DU_CONTROLE"/></TD>
		<TD>
			<INPUT name="dateDebutControle" id="dateDebutControle" class="disabled" value="<%=dateDebutControle%>" readonly="readonly" size="10"/>
			 -
			<INPUT name="dateFinControle" id="dateFinControle" class="disabled" value="<%=dateFinControle%>" readonly="readonly" size="10" />
		</TD>
	</TR>
	<TR><TD colspan="2"><HR></HR></TD></TR>
    <TR>
    	<td width="23%" height="2"><ct:FWLabel key="EMAIL"/></td>
        <td height="2"><INPUT type="text" name="email" id="email" maxlength="40" size="40" style="width:8cm;" value="<%= viewBean.getEmail() != null ? viewBean.getEmail() : "" %>" /></TD></td>
    </TR>
	<TR>
		<TD><ct:FWLabel key="DATE_ENVOI"/></TD>
		<TD>
			<ct:FWCalendarTag name="dateEnvoi" value="<%=dateEnvoi%>" />
		</TD>
	</TR>
    <TR>
    	<td width="23%" height="2"><ct:FWLabel key="TEXTE_LIBRE"/></td>
        <td height="2"><textarea name="texteLibre" cols="85" rows="10" ><%=!JadeStringUtil.isEmpty(viewBean.getTexteLibre()) ? viewBean.getTexteLibre() : ""%></textarea></td>
    </TR>
	<TR><TD>&nbsp;</TD></TR>
	<TR><TD>&nbsp;</TD></TR>
			
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

	<ct:menuChange displayId="options" menuId="CE-OptionsControlEmployeur" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=selectedId%>"/>
		<ct:menuSetAllParams key="idTiers" value="<%=idTiers%>"/>
		<ct:menuSetAllParams key="dateDebutControle" value="<%=dateDebutControle%>"/>
		<ct:menuSetAllParams key="dateFinControle" value="<%=dateFinControle%>"/>
		<ct:menuSetAllParams key="dateEffective" value="<%=dateEffective%>"/>
		<ct:menuSetAllParams key="visaReviseur" value="<%=visaReviseur%>"/>
		<ct:menuSetAllParams key="numAffilie" value="<%=numAffilie%>"/>
		<ct:menuSetAllParams key="idAffiliation" value="<%=idAffiliation%>"/>
	</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>