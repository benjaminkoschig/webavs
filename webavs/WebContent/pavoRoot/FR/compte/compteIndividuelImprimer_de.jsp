<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@ page import="globaz.globall.util.*" %>

<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CCI2001";
    globaz.pavo.db.compte.CICompteIndividuelImprimerViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelImprimerViewBean)session.getAttribute ("viewBean");
    globaz.pavo.db.compte.CICompteIndividuelViewBean viewBeanCompte = (globaz.pavo.db.compte.CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
	userActionValue = "pavo.compte.compteIndividuelImprimer.executer";
	tableHeight = 150;

	boolean ciPlusieursLies= false;
%>

<script type="text/javascript">
	top.document.title = <%=viewBean.getSession().getLabel("JSP_CCI2001_TITLE")%>
	var langue = "<%=languePage%>";
</script>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/utils.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

function init() {
}

function updateCompteIndividuel(data) {
	var numAvs="";
	var compteId="";

	arrayOfStrings = data.split("#");
	if (arrayOfStrings.length>=1) {
		compteId = arrayOfStrings[0];
	}
	if (arrayOfStrings.length>=1) {
		numAvs = arrayOfStrings[1];
	}

	document.forms[0].elements('numeroAvsInv').value=numAvs;
	document.forms[0].elements('selectedId').value=compteId;
	document.forms[0].elements('compteIndividuelId').value=compteId;
	this.location.href="<%=servletContext + mainServletPath%>?userAction=pavo.compte.compteIndividuel.imprimer&compteIndividuelId="+compteId;
}

function detailAssure() {
	this.location.href='<%=servletContext + mainServletPath%>?userAction=pavo.compte.compteIndividuel.afficher&selectedId='+document.forms[0].elements('compteIndividuelId').value+'&mainSelectedId='+<%=viewBean.getCompteIndividuelId()%>;
}

function setFocus() {
	var defaultFocusElement = document.forms[0].elements[0];
	if (defaultFocusElement == null) {
		return;
	}
	try {
		defaultFocusElement.focus();
	} catch (e) {
	}
}
</script>
<%-- /tpl:put --%>

<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='JSP_CCI2001_ZONE_TITLE'/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
 <tr>
	<td><a href="javascript:detailAssure();"><ct:FWLabel key='JSP_CCI2001_ASSURE'/></a></td>

    <%
  		Object[] ciLies = viewBean.getCi().getCILies();
  		if (ciLies.length>0) {
  			ciPlusieursLies=true;
  	%>
	<td nowrap>
		<select name="numAvsList" onchange="updateCompteIndividuel(this.options[this.selectedIndex].value);">
			<option SELECTED value="<%=viewBean.getCi().getId()%>">
				<%=viewBean.getCi().getNssFormate() + " " + viewBean.getCi().getNomPrenom()%>
			</option>
	<%
			for(int i=0;i < ciLies.length;i++) {
				String [] link = (String[])ciLies[i];
				if(!"".equals(link[0])) { 
	%>
			<option value="<%=link[0]+"#"+link[1]+"#"+link[2]+"#"+link[3]%>">
				<%=link[1] + " " + link[2]+" "+link[3]%>
			</option>
	<% 			}
			}
	%>
		</select>
		<input type="hidden" name="numeroAvsInv" value="<%=viewBean.getCi().getNssFormate()%>">
		<input type="hidden" name="nomInv" size="70" class="disabled" readonly value="<%=viewBean.getCi().getNomPrenom()%>">
		<input type="hidden" name="compteIndividuelId" value="<%=viewBean.getCi().getCompteIndividuelId()%>">
	</td>

	<% } else { %>
	<td>
		<input type="text" name="numeroAvsInv" size="17" class="disabled" readonly value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getCi().getNumeroAvs())%>"><INPUT type="text" name="nomInv" size="70" class="disabled" readonly value="<%=viewBean.getCi().getNomPrenom()%>">
		<input type="hidden" name="compteIndividuelId" value="<%=viewBean.getCi().getCompteIndividuelId()%>">
	</td>
	<% } %>
</tr>
<tr>
	<td><ct:FWLabel key='JSP_CCI2001_DATE_NAISSANCE'/></td>
	<td>
		<input type="text" name="naissance" class="disabled" size="11" readonly value="<%=viewBean.getCi().getDateNaissance()%>">
		&nbsp;<ct:FWLabel key='JSP_CCI2001_SEXE'/>&nbsp;
		<input type="text" name="sexe" class="disabled" size="11" readonly value="<%=viewBean.getCi().getSexeLibelle()%>">
		&nbsp;<ct:FWLabel key='JSP_CCI2001_PAYS'/> &nbsp;
		<input type="text" name="pays" class="disabled" size="48" readonly value="<%=viewBean.getCi().getPaysFormate()%>"></td>
	</td>
</tr>
<tr>
	<td><ct:FWLabel key='JSP_CCI2001_EMAIL'/></td>
	<td>
		<input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()!=null?viewBean.getEMailAddress():""%>">*
	</td>
</tr>
       <% if(JAUtil.isStringEmpty(viewBean.getRassemblementEcritureId())){ %>
<tr>
	<td><ct:FWLabel key='JSP_CCI2001_CLOTURE'/></td>
	<td>
       	<ct:FWListSelectTag name="etatEcritures" defaut="active" data="<%=viewBean.getCi().getListClotures()%>"/>
	</td>
</tr>
       <% } %>
<tr>
	<td><ct:FWLabel key='JSP_CCI2001_PERIODE'/></td>
	<td>
		<ct:FWLabel key='JSP_CCI2001_DE'/>&nbsp;<input type="text" size="4" maxlength="4" name="fromAnnee" value="">&nbsp;<ct:FWLabel key='JSP_CCI2001_A'/>&nbsp;<input type="text" size="4" maxlength="4" name="untilAnnee" value="">
	</td>
<tr>
	<td><ct:FWLabel key='JSP_CCI2001_LANGUE'/></td>
	<td>
		<select name="langueImp">
     		<option name="FR" <%="FR".equals(languePage)?"selected":""%> value="FR">Français</option>
			<option name="DE" <%="DE".equals(languePage)?"selected":""%> value="DE">Deutsch</option>
			<option name="IT" <%="IT".equals(languePage)?"selected":""%> value="IT">Italiano</option>
		</select>
	</td>
</tr>

<% if (ciPlusieursLies) { %>
<tr>
	<td><ct:FWLabel key='JSP_CCI2001_IMPRIMER_COMPTES_LIES'/></td>
	<td><input type="checkbox" name="imprimerLies" checked></td>
</tr>
<% } %>

<tr>
	<td><ct:FWLabel key='JSP_CCI2001_TITRE'/></td>
	<td>
		<ct:FWListSelectTag name="titreAssure" data="<%=viewBean.getListeTitre()%>" defaut="" />		
	</td>
</tr>
<tr>
	<td><ct:FWLabel key='JSP_CCI2001_ADRESSE'/></td>
	<td>
		<textarea name="adresseAssure" id="adresseAssure" rows="5" cols="50"></textarea>
	</td>
</tr>
<tr>
	<td>&nbsp;<input type="hidden" name="rassemblementEcritureId" value="<%=viewBean.getRassemblementEcritureId()%>"></td>
	<td>&nbsp;</td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%  }  %>

<% if(globaz.pavo.util.CIUtil.isSpecialist(session)) { %>
	<ct:menuChange displayId="options" menuId="compteIndividuel-detail" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getCi().getId()%>"/>
		<ct:menuSetAllParams key="mainSelectedId" value="<%=viewBean.getCi().getId()%>"/>
		<ct:menuSetAllParams key="compteIndividuelId" value="<%=viewBean.getCi().getId()%>"/>
	</ct:menuChange>
<% } else { %>
	<ct:menuChange displayId="options" menuId="compteIndividuelNoSpez-detail" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getCi().getId()%>"/>
		<ct:menuSetAllParams key="mainSelectedId" value="<%=viewBean.getCi().getId()%>"/>
		<ct:menuSetAllParams key="compteIndividuelId" value="<%=viewBean.getCi().getId()%>"/>
	</ct:menuChange>
<% } %>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>