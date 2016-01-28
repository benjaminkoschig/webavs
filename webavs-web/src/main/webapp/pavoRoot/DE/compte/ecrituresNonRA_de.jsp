<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	globaz.pavo.db.compte.CIEcrituresNonRAViewBean viewBean = (globaz.pavo.db.compte.CIEcrituresNonRAViewBean)session.getAttribute("viewBean");

	//selectedIdValue=viewBean.getEcritureId();
	selectedIdValue=viewBean.getIdJournal();
	userActionValue="pavo.compte.ecrituresNonRA.modifierDepuisEcriture";
	bButtonDelete=false;
	idEcran = "CCI0032";
	
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
//function add(){}
function upd(){}
function validate(){
	state=validateFields();
	document.forms[0].elements('userAction').value="pavo.compte.ecrituresNonRA.modifierDepuisEcriture";
	return state;
}
function cancel(){
	document.forms[0].elements('userAction').value="pavo.compte.ecrituresNonRA.afficher";}
function del(){}
function init(){}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer im VR unbekannter Buchung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<input type="hidden" name='ecritureId' value='<%=viewBean.getEcritureId()%>'>
						<tr>
						<td>Versicherte</td>
						<td colspan="4">
						<input type="text" name='avs' size="17" value='<%=viewBean.getNssFormate()%>'class="disabled" readonly tabIndex="-1"> 
						<input name='nomPrenom' class='disabled' size="72" readonly value="<%=viewBean.getNomPrenom()%>" tabindex="-1"> 
						</td>
						</tr>
						<tr>
							<td>
								Geburtsdatum
							</td>
							<td colspan="4">
								<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissance()%>">
								Geschlecht &nbsp;
								<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeForNNSS()%>">
								&nbsp;Heimatstaat &nbsp;
								<input type="text" size = "45" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysForNNSS()%>">
							</td>
						</tr>
						<tr>
						<td>Arbeitgeber</td>
						<td><input name='employeurPartenaire'
							size="15" value="<%=viewBean.getNoNomEmployeur().indexOf(' ')>1?viewBean.getNoNomEmployeur().substring(0,viewBean.getNoNomEmployeur().indexOf(' ')):""%>"
							onChange="document.getElementById('numeroDetailInv').value=''" class="disabled" readonly> <input
							name='numeroDetailInv' class='disabled' size="55" 
							value="<%=viewBean.getNoNomEmployeur().indexOf(' ')>0?viewBean.getNoNomEmployeur().substring(viewBean.getNoNomEmployeur().indexOf(' ')+1):""%>"
							tabindex="-1" readonly></td>
						</tr>
						<tr>
						<td>VA einverlangen</td>
						<%if(viewBean.isCertificat().booleanValue()){%>
						<td><input type="checkBox" name="certificat" checked></td>
						<%}else{%>
						<td><input type="checkbox" name="certificat"></td>
						<%}%>
						</tr>
					
					
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
		<ct:menuChange displayId="options" menuId="journal-detail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>"/>
			<ct:menuSetAllParams key="idJournal" value="<%=viewBean.getIdJournal()%>"/>
			<ct:menuSetAllParams key="fromIdJournal" value="<%=viewBean.getIdJournal()%>"/>
		</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>