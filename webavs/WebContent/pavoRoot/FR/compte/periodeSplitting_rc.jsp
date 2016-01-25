
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*" %>
<%
	idEcran = "CCI0009";
    globaz.pavo.db.compte.CICompteIndividuelViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
	bButtonFind = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	bButtonNew = false;


%>
<SCRIPT>
bFind = true;
usrAction = "pavo.compte.periodeSplitting.lister";
top.document.title = "CI - Gestion des périodes de splitting";
timeWaiting = 1;
</script>
<%if(globaz.pavo.util.CIUtil.isSpecialist(session)){%>
	<ct:menuChange displayId="options" menuId="compteIndividuel-detail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
			<ct:menuSetAllParams key="compteIndividuelId" value="<%=viewBean.getCompteIndividuelId()%>"/>
			<ct:menuSetAllParams key="mainSelectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
		</ct:menuChange>
<%}else{%>
<ct:menuChange displayId="options" menuId="compteIndividuelNoSpez-detail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
			<ct:menuSetAllParams key="compteIndividuelId" value="<%=viewBean.getCompteIndividuelId()%>"/>
			<ct:menuSetAllParams key="mainSelectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
		</ct:menuChange>

<%}%>

<script>
function updateCompteIndividuel(data) {		
	arrayOfStrings = data.split("#");
	var compteId = "";	
	var numAvs = "";	
	var name = "";
	var dateNaissance = "";
	
	if (arrayOfStrings.length>=1) {
		compteId = arrayOfStrings[0];		
	}
	if (arrayOfStrings.length>=2) {
		numAvs = arrayOfStrings[1];
	}
	if (arrayOfStrings.length>=3) {
		name = arrayOfStrings[2];
	}	
	
	if (arrayOfStrings.length>=4) {
		dateNaissance = arrayOfStrings[3];
	}	

	document.forms[0].elements('forCompteIndividuelId').value=compteId;
	document.forms[0].elements('numeroAvsInv').value=numAvs;
	document.forms[0].elements('nomInv').value=name;
	document.forms[0].elements('naissance').value=dateNaissance;	
	

	 document.forms[0].submit();
	parent.fr_main.location.href = "<%=(servletContext + mainServletPath)%>?userAction=pavo.compte.periodeSplitting.chercherPeriodeSplitting&compteIndividuelId="+compteId;



	
	
}

function detailAssure() {
	this.location.href='<%=servletContext + mainServletPath%>?userAction=pavo.compte.compteIndividuel.afficher&selectedId='+document.forms[0].elements('forCompteIndividuelId').value+'&mainSelectedId='+<%=viewBean.getCompteIndividuelId()%>;
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aperçu des périodes de splitting<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD nowrap width="90"><a href="javascript:detailAssure();">Assuré</a></TD>

						<% 
 		  		Object[] ciLies = viewBean.getCILies();
 		  		if (ciLies.length>0) {%>
						<TD nowrap><select name="numAvsList"
							onchange="updateCompteIndividuel(this.options[this.selectedIndex].value);">
							<option SELECTED
								value="<%=viewBean.getCompteIndividuelId()+"#"+viewBean.getNssFormate()+"#"+viewBean.getNomPrenom()%>">
							<%=viewBean.getNssFormate() + " " + viewBean.getNomPrenom()%></option>
							<%
	 				for(int i=0;i<ciLies.length;i++) {
	 					String [] link = (String[])ciLies[i];
	 					if(!"".equals(link[0])) { %>
							<option value="<%=link[0]+"#"+link[1]+"#"+link[2]+"#"+link[3]%>">
							<%=link[1] + " " + link[2] + " "+link[3]%></option>
							<% }
	                  } 
	                %>
						</select></TD>
						<INPUT type="hidden" name="numeroAvsInv"
							value="<%=viewBean.getNssFormate()%>">

						<INPUT type="hidden" name="nomInv" size="60" class="disabled"
							readonly value="<%=viewBean.getNomPrenom()%>">
						<INPUT type="hidden" name="forCompteIndividuelId"
							value="<%=viewBean.getCompteIndividuelId()%>">

						<%}else{%>

						<TD nowrap><INPUT type="text" name="numeroAvs"
							size="17" class="disabled" readonly
							value="<%=viewBean.getNssFormate()%>">
						<INPUT type="text" name="nom" class="disabled" size="70" readonly
							value="<%=viewBean.getNomPrenom()%>"> <INPUT type="hidden"
							name="forCompteIndividuelId"
							value="<%=viewBean.getCompteIndividuelId()%>"></TD>
						<%}%>
					<tr>&nbsp;</tr>
					<TR>
						<TD nowrap>Date de naissance &nbsp;&nbsp;</TD>
						<TD nowrap><INPUT type="text" name="naissance" class="disabled"
							size="11" readonly value="<%=viewBean.getDateNaissance()%>">
							Sexe
							&nbsp;
							<INPUT type="text" name="sexe" class="disabled"
								size="11" readonly value="<%=viewBean.getSexeLibelle()%>">
							Pays &nbsp;
							<INPUT type="text" name="pays" class="disabled"
								size="48" readonly value="<%=viewBean.getPaysFormate()%>"></TD>
						</td>
					</TR>

					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>