<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "CFA0015";
	rememberSearchCriterias = true;
%>

<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@page import="globaz.musca.servlet.FAActionGestionInterets"%>

<%
CAApercuInteretMoratoireListViewBean viewBean = (CAApercuInteretMoratoireListViewBean) session.getAttribute (FAActionGestionInterets.VBL_ELEMENT);
CAApercuInteretMoratoireViewBean element = (CAApercuInteretMoratoireViewBean) session.getAttribute (FAActionGestionInterets.VB_ELEMENT);
viewBean.setForIdCompteAnnexe(globaz.jade.client.util.JadeStringUtil.isBlank(element.getIdCompteAnnexe())?viewBean.getForIdCompteAnnexe():element.getIdCompteAnnexe());

String idCompteAnnexe;
if(!JAUtil.isStringNull(request.getParameter("idCompteAnnexe"))) {
	idCompteAnnexe = request.getParameter("idCompteAnnexe");
}
else if(globaz.jade.client.util.JadeStringUtil.isBlank(element.getIdCompteAnnexe())) {
	idCompteAnnexe = viewBean.getForIdCompteAnnexe();
}
else {
	idCompteAnnexe = element.getIdCompteAnnexe();
}

viewBean.setForIdCompteAnnexe(idCompteAnnexe);
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="FA-MenuPrincipal" showTab="menu"/>

<script language="JavaScript">
usrAction = "musca.interets.gestionInterets.lister";
bFind = true;

function jsInitForIdCompteAnnexe(){
	document.forms[0].forIdCompteAnnexe.value = "";
}

top.document.title = "Anzeige der Verzugszinsen - " + top.location.href;

function checkSuspens() {
	if(document.forms['mainForm'].elements['forIdJournalFacturation'].checked) {
		document.forms['mainForm'].elements['forIdMotifCalcul'].selectedIndex = 0;
		document.forms['mainForm'].elements['forIdMotifCalcul'].disabled = true;
	}
	else {
		document.forms['mainForm'].elements['forIdMotifCalcul'].disabled = false;
	}
}

function validate() {
    state = validateFields();

    return state;
}

function traiter() {
	var listIdIMATraiter = "";
	var listCheckBox = $('[name=chkTraiter]:checked',$('[name=fr_list]').contents()) ;

	listCheckBox.each(function(checkBox){
		listIdIMATraiter = listIdIMATraiter + $(this).val() + ",";
	});
	listIdIMATraiter = listIdIMATraiter.substring(0, listIdIMATraiter.length - 1);
	$("#listIdIMATraiter").val(listIdIMATraiter);
	setUserAction("musca.interets.changementJournalIM.afficher");
	document.forms[0].target= "fr_main";
	document.forms[0].submit();
}

$(function(){
	$("#btnTraiter").click(function(){traiter();});	
});



</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Anzeige der Verzugszinsen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	</tbody>
</table>
<script language="JavaScript">
	element = document.getElementById("subtable");
  	element.style.height = "10px";
  	element.style.width = "100%";
</script>
<table cellspacing="0" cellpadding="0" style="height: 100px; width: 100%">
	<tbody>

          <tr>
            <td>Mitglied-Nr.</td>
            <td>
            	<input type="hidden" name="forIdCompteAnnexe" value="<%=viewBean.getForIdCompteAnnexe()%>">
            	<input type="hidden" name="forDomaine" value="CA">
            	 <input type="hidden" id="listIdIMATraiter" name="listIdIMATraiter" value=""/>
            	
            	<%
	              	CACompteAnnexe tempCompteAnnexe = new CACompteAnnexe();
					tempCompteAnnexe.setIdCompteAnnexe(viewBean.getForIdCompteAnnexe());
					tempCompteAnnexe.setSession(objSession);
					tempCompteAnnexe.retrieve();
				%>
				<input type="text" name="forIdExterneRole" value="<%if (request.getParameter("forIdExterneRole") != null){%><%=request.getParameter("forIdExterneRole")%><%}%>" onChange="jsInitForIdCompteAnnexe()">
				<select name="forIdRole" class="libelleStandard">
                <% String selectionRole = request.getParameter("forSelectionRole");
					if (selectionRole == null && tempCompteAnnexe.getIdRole() != null)
						selectionRole = tempCompteAnnexe.getIdRole();
					else if (selectionRole == null)
						selectionRole = "";%>
                <option selected value="">Tous</option>
                <%CARole tempRole;
					 		CARoleManager manRole = new CARoleManager();
							manRole.setSession(objSession);
							manRole.find();
							for(int i = 0; i < manRole.size(); i++){
								tempRole = (CARole)manRole.getEntity(i);
								if(selectionRole.equalsIgnoreCase(tempRole.getIdRole())){ %>
               	 <option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                <%}else{%>
               		<option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                	<%}%>
                <%}%>
              	</select>
            </td>
            <td>Sortierung&nbsp;</td>
            <td>
              <select name="forSelectionTri" style="width:5cm;">
              	<option selected="selected" value="nom">Name</option>
                <option value="datenumero">Datum, Nummer</option>
                <option value="datenom">Datum, Name</option>
                <option value="numero">Nummer</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>Zinsenart</td>
            <td>
           		<ct:FWSystemCodeSelectTag
           			name="forIdGenreInteret"
           			defaut=""
           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsGenreInteret(objSession)%>"
           		/>
            </td>
            <td>Betrag</td>
            <td>
              <input type="text" class="montant" name="fromTotalMontantInteret" style="width:5cm;">
              <INPUT type="hidden" name="selectorName" value="">
              <script>
           		document.getElementById("forIdGenreInteret").style.width=260;
           		</script>
            </td>
          </tr>
           <tr>
			<td>Verfügungsgrund</td>
			<td>
			<%
			   	java.util.HashSet except = new java.util.HashSet();
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_AUTOMATIQUE);
			%>

			<ct:FWSystemCodeSelectTag name="forIdMotifCalcul"
				defaut="<%=CAInteretMoratoire.CS_A_CONTROLER%>"
				codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsMotifDecisionInteret(objSession)%>"
				except="<%=except%>"
			/>
			</td>
			<td colspan="2">&nbsp;</td>
			</tr>

			<%if(!globaz.jade.client.util.JadeStringUtil.isEmpty((String)request.getAttribute("message"))){%>
				<tr>
					<TD colspan="4">
						<font color="#FF0000"><b><%=request.getAttribute("message")%></b></font>
					</TD>
				</tr>
			<%}%>

			<tr>
			<td align="right" valign="top" colspan="4">
			<%
				String idAdministrateurSrc = request.getParameter("idAdministrateurSrc");

				if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(idAdministrateurSrc)) {
			%>
				<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=<%=idAdministrateurSrc%>" class="external_link">Verwalter</A>
			<%
				} else {
			%>
				&nbsp;
			<%
				}
			%>
            </td>
            </tr>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<ct:ifhasright element="musca.interets.changementJournalIM.afficher" crud="u">
	<INPUT type="button" name="btnTraiter" id="btnTraiter" value="Job verbinden" onClick="onClickCheckBox('musca.interets.changementJournalIM.afficher');" >
</ct:ifhasright>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>