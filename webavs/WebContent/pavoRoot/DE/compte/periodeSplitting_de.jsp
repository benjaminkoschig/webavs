
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.pavo.util.CIUtil"%><SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
top.document.title = "IK - Detail der Splittingperiode"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ page import="globaz.globall.util.*,globaz.globall.parameters.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran = "CCI0010";
    globaz.pavo.db.compte.CIPeriodeSplittingViewBean viewBean = (globaz.pavo.db.compte.CIPeriodeSplittingViewBean)session.getAttribute ("viewBean");
	globaz.pavo.db.compte.CICompteIndividuelViewBean viewBeanFK = (globaz.pavo.db.compte.CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
	selectedIdValue = viewBean.getPeriodeSplittingId();
	userActionValue = "pavo.compte.periodeSplitting.modifier";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	String formState=new String("");
	String fieldDisable = "class='disabled' readonly tabindex='-1'";
	if(!viewBean.getTypeEnregistrementWA().equals("322001")){
	
		formState= fieldDisable;
	}
%>
<%
bButtonDelete = false;
%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.compte.periodeSplitting.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.compte.periodeSplitting.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.compte.periodeSplitting.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.compte.periodeSplitting.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pavo.compte.periodeSplitting.supprimer";
        document.forms[0].submit();
    }
}
function init(){}
-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail der Splittingperiode<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR>
						<TD>Versicherte</TD>
						<TD><INPUT type="text" name="numeroAvs" size="17 class="disabled"
							readonly
							value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBeanFK.getNumeroAvs())%>">
						<INPUT type="text" name="nom" class="disabled" size="76" readonly
							value="<%=viewBeanFK.getNomPrenom()%>"></TD>
					</TR>
					<TR>
							<TD nowrap>Geburtsdatum &nbsp;&nbsp;</TD>
							<TD nowrap><INPUT type="text" name="naissance" class="disabled"
							size="11" readonly value="<%=viewBeanFK.getDateNaissance()%>">
							Geschlecht
							&nbsp;
							<INPUT type="text" name="sexe" class="disabled"
							size="11" readonly value="<%=viewBeanFK.getSexeLibelle()%>">
							&nbsp;Heimatstaat &nbsp;
							<INPUT type="text" name="pays" class="disabled"
							size="44" readonly value="<%=viewBeanFK.getPaysFormate()%>"></TD>

					</TR>
					<tr>
						<td colspan="2">
						<hr size='1'>
						</td>
					</tr>
					<tr>
						<td>Ehepartner</td>
						<td>	
							<nss:nssPopup newnss="<%=viewBean.getNSSPartenaireNNSS()%>"
							name="partenaireNumeroAvs" 
							avsMinNbrDigit="99" avsAutoNbrDigit="99" 
				  			nssMinNbrDigit="99" nssAutoNbrDigit="99"
							value="<%=viewBean.getNSSPartenaireWithoutPrefixe()%>"
							 />
							 <INPUT type="text" name="nomPartenaire" class="disabled" size="71" readonly
							value="<%=viewBean.getNomPrenomPar()%>"></TD>
						</td>

					</tr>
					<TR>
							<TD nowrap>Geburtsdatum &nbsp;&nbsp;</TD>
							<TD nowrap><INPUT type="text" name="naissance" class="disabled"
							size="11" readonly tabindex="-1" value="<%=viewBean.getDateDeNaissancePar()%>">
							Geschlecht
							&nbsp;
							<INPUT type="text" name="sexe" class="disabled"
							size="11" readonly tabindex="-1" value="<%=viewBean.getSexeLibellePar()%>">
							&nbsp;Heimatstaat &nbsp;
							<INPUT type="text" name="pays" class="disabled"
							size="44" readonly tabindex="-1" value="<%=viewBean.getPaysFormatePar()%>"></TD>

					</TR>
					<tr>
						<td>Beginnjahr</td>
						<td><input name='anneeDebut' <%=formState%> class='disable'
							size='5'
							value='<%=JANumberFormatter.formatZeroValues(viewBean.getAnneeDebut(),false,true)%>'>
						</td>

					</tr>

					<tr>
						<td>Endjahr</td>
						<td><input name='anneeFin' <%=formState%> class='disable' size='5'
							value='<%=JANumberFormatter.formatZeroValues(viewBean.getAnneeFin(),false,true)%>'>
						</td>

					</tr>

					<tr>
						<td>Spezialcode</td>

						<td><%if(viewBean.getTypeEnregistrementWA().equals("322001")){%> <select
							name="particulier" <%=formState%> style="width: 12cm">
							<OPTION></OPTION>
							<%
FWParametersSystemCodeManager list = globaz.pavo.translation.CodeSystem.getLcsIdGenreSplittingWithoutBlank(session);
for(int i=0;i<list.size();i++) {
	FWParametersSystemCode codeSystem = (FWParametersSystemCode)list.getEntity(i);
	int code = Integer.parseInt(codeSystem.getCurrentCodeUtilisateur().getCodeUtilisateur());
	if(code<4) {
		String libelle = codeSystem.getCurrentCodeUtilisateur().getLibelle();
		String selected = "";
		if(codeSystem.getIdCode().equals(viewBean.getParticulier())) {
			selected = "selected";
		}
%>
							<OPTION <%=selected%> value='<%=codeSystem.getIdCode()%>'><%=libelle%></OPTION>
							<% } 
}%>
						</select> <%}else{%> <INPUT type="text" size="40" <%=formState%>
							value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getParticulier(), session)%>"></td>
						<%}%>
					</tr>

					<tr>
						<td>Auftraggebende AK</td>
						<td><textarea  name='caisseAgenceCommettante'
							 rows='5' width='250' class='disabled' tabindex='-1' align='left'><%=viewBean.getCaisseAgenceCommettante()%>
<%=viewBean.getNomCaisseCommettante()%>
</textarea></td>
					</tr>

					<tr>
						<td>Datum der Rückgängigmachung</td>
						<td><%if(CIUtil.isSpecialist(session)){%> <ct:FWCalendarTag
							name="dateRevocation" value="<%=viewBean.getDateRevocation()%>" />
						<%}else{%> <INPUT name="dateRevocation" <%=formState%> type="text"
							value="<%=viewBean.getDateRevocation()%>"> <%}%></td>


					</tr>

					<tr>
						<td>Eintragstyp</td>
						<td><input type="typeEnregistrementInv" readonly
							class="libelleLongDisabled"
							value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getTypeEnregistrementWA(),session)%>">
						</td>

					</tr>
					<% String caisseTenant = viewBean.getCaisseTenantCIWA();
if(!JAUtil.isStringEmpty(caisseTenant)) { %>
					<tr>
						<td>IK-führende Ausgleichskasse</td>
						<td><input name='caisseTenantInv' class='disabled' size='11'
							readonly value='<%=caisseTenant%>'></td>

					</tr>
					<% } %>

					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>
<%
	
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>