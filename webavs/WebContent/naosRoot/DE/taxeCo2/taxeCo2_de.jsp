<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CAF0070";%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
 //contrôle des droits
 bButtonNew = objSession.hasRight(userActionNew, "ADD");
 
 AFTaxeCo2ViewBean viewBean = (AFTaxeCo2ViewBean)session.getAttribute ("viewBean");
 selectedIdValue = viewBean.getTaxeCo2Id();
 String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
 String method = request.getParameter("_method");
 String pageName = "taxeCo2";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.naos.db.taxeCo2.AFTaxeCo2ViewBean"%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
<%
	int autoDigiAff = globaz.musca.util.FAUtil.fetchAutoDigitAff(session);
%>
top.document.title = "Erfassung - Detail CO2-Abgabe"


function add() {
    document.forms[0].elements('userAction').value="naos.taxeCo2.taxeCo2.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.taxeCo2.taxeCo2.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.taxeCo2.taxeCo2.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {

    document.forms[0].elements('userAction').value="naos.taxeCo2.taxeCo2.chercher";
    }
 else
    document.forms[0].elements('userAction').value="naos.taxeCo2.taxeCo2.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="naos.taxeCo2.taxeCo2.supprimer";
        document.forms[0].submit();
    }
}


function init(){}

function updateAffilieNumero(tag){
	if(tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('numAffilie').value     = tag.select[tag.select.selectedIndex].value;
		//document.getElementById('affiliationId').value	= tag.select.options[tag.select.selectedIndex].affiliationId;
		//document.getElementById('idTiers').value           = tag.select.options[tag.select.selectedIndex].idTiers;
		document.getElementById('descriptionTiers1').value = tag.select.options[tag.select.selectedIndex].designation1;
		document.getElementById('descriptionTiers2').value = tag.select.options[tag.select.selectedIndex].designation2;
	} 
}

function updateLibelle(tag){
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("descriptionRubrique").value = element.libelle;
	}
}

function reloadAnneeMasse(){
	document.forms[0].elements('userAction').value="naos.taxeCo2.reloadAnnee.reloadAnneeMasse";
	document.forms[0].submit(); 
}

function reloadAnneeRedistri(){
	document.forms[0].elements('userAction').value="naos.taxeCo2.reloadAnnee.reloadAnneeRedistri";
	document.forms[0].submit(); 
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Abrechnungsdetail<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
					<TR><TD>&nbsp;</TD></TR>
						<TR>
						<TD nowrap width="121" height="31">Abr.-Nr.	
							<INPUT type="hidden" name="idTiers"    value="<%=viewBean.getIdTiers()%>">
							<INPUT type="hidden" name="affiliationId"    value="<%=viewBean.getAffiliationId()%>">
							<INPUT type="hidden" name="taxeCo2Id" value="<%=viewBean.getTaxeCo2Id()%>">
						</TD>
						<TD nowrap> 
							<% 
								//String nAff = request.getParameter("numAffilie");
								String nAff = viewBean.getNumAffilie();
								if(nAff==null){
									nAff="";
								}							
								if (method != null && method.equalsIgnoreCase("add")) { 
									String 	designation1 = "";
									String 	designation2 = "";
									if ( viewBean.getTiers() != null) {
										designation1 = viewBean.getTiers().getDesignation1();
										designation2 = viewBean.getTiers().getDesignation2();
									}
							%>
							<ct:FWPopupList 
								name="numAffilie" 
								value="<%=nAff%>" 
								className="libelle" 
								jspName="<%=jspLocation%>" 
								autoNbrDigit="<%=autoDigiAff%>" 
								size="15"
								minNbrDigit="3"
								onChange="updateAffilieNumero(tag);"
								/>
							<IMG
								src="<%=servletContext%>/images/down.gif"
								alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
								title="presser sur la touche 'flèche bas' pour effectuer une recherche"
								onclick="if (document.forms[0].elements('affilieNumero').value != '') affilieNumeroPopupTag.validate();">
							&nbsp; 
							<input type="text" name="descriptionTiers1" value="<%=designation1%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">&nbsp;
							<input type="text" name="descriptionTiers2" value="<%=designation2%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">
						<% } else  { %>
							<input name="numAffilie" value="<%=viewBean.getNumAffilie()%>" readonly="readonly" tabindex="-1" class="disabled" style="width:150px">&nbsp;
							<input type="text" name="descriptionTiers1" value="<%=viewBean.getTiers().getDesignation1()%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">&nbsp;
							<input type="text" name="descriptionTiers2" value="<%=viewBean.getTiers().getDesignation2()%>" readonly="readonly" tabindex="-1" class="libelleLongDisabled">
						<% } %>
						</TD>
					</TR>
					<TR><TD>&nbsp;</TD></TR>
					<TR> 
						<TD nowrap  height="11" colspan="6">
							<hr size="3" width="100%">
						</TD>
					</TR>
			<tr><td colspan="2">
				<TABLE border="0" cellspacing="0" cellpadding="0" id="tPartie1">
					<TBODY>
					<TR>
						<TD title="Jahr ab dem die Lohnsumme angerechnet wurde">Jahr der Lohnsumme:</TD>
						<TD title="Jahr ab dem die Lohnsumme angerechnet wurde">
						 	<INPUT type="text" name="anneeMasse" value="<%=viewBean.getAnneeMasse()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeRedistri()"">
						 	<INPUT type="hidden" name="pageName" value="<%=pageName%>" class="numeroCourt" style="width : 4.0cm" tabindex="-1"">
						</TD>
					</TR>
					<TR>
						<TD title="Jahr in dem die Lohnsumme ausbezahlt wird">Auszahlungsjahr:</TD>
						<TD title="Jahr in dem die Lohnsumme ausbezahlt wird">
						 	<INPUT type="text" name="anneeRedistri" value="<%=viewBean.getAnneeRedistri()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeMasse()"">
						</TD>
					</TR>
					<TR><TD>&nbsp;</TD></TR>
					<TR>
						<TD nowrap>Lohnsumme</TD>
            			<TD nowrap> 
							<INPUT name="masse" type="text" size="20" maxlength="15" value="<%=viewBean.getMasse()%>">
						</TD>
					</TR>
					<TR><TD>&nbsp;</TD></TR>
					<TR> 
						<TD nowrap width="128">Abgangsgrund</TD>
            			<TD nowrap colspan="2" width="298"> 
							<ct:FWCodeSelectTag 
	               				name="motifFin" 
								defaut="<%=viewBean.getMotifFin()%>"
								codeType="VEMOTIFFIN"
								wantBlank="true"/> 
						</TD>							
					</TR>
					<TR><TD>&nbsp;</TD></TR>
					<TR>
			            <TD nowrap width="128">Status</TD>
			            <TD >
			            <ct:FWCodeSelectTag 
	               				name="etat" 
								defaut="<%=viewBean.getEtat()%>"
								codeType="VEETATAXE"
								wantBlank="false"/>
						</TD>
			            <TD></TD>
			       </TR>
			       <TR><TD>&nbsp;</TD></TR>
					<TR>
						<TD nowrap width="128">Forcierter Beitragssatz</TD>
            			<TD nowrap> 
							<INPUT name="tauxForce" type="text" size="20" maxlength="15" value="<%=viewBean.getTauxForce()%>">
						</TD>
					</TR>
					<TR><TD>&nbsp;</TD></TR>
					<TR>
						<TD nowrap width="128">Journal-Nr.</TD>
            			<TD nowrap> 
							<INPUT name="idPassage" type="text" size="20" maxlength="15" value="<%=viewBean.getIdPassage()%>">
						</TD>
					</TR>
					<TR><TD>&nbsp;</TD></TR>
					<TR>
						<TD nowrap width="145">Rubrik</TD>
			            <TD nowrap>
							<%String jspLocationRubrique = servletContext + "/naosRoot/" + languePage + "/taxeCo2/rubrique_select.jsp";%>
							<input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>">
							<ct:FWPopupList name="idExterneRubrique" 
											onFailure="document.mainForm.idRubrique.value='';" 
											onChange="if (tag.select) document.mainForm.idRubrique.value = tag.select[tag.select.selectedIndex].idRubrique;updateLibelle(tag);" 
											validateOnChange="true" 
											value="<%=viewBean.getIdExterneRubrique()%>" 
											className="libelle" jspName="<%=jspLocationRubrique%>" 
											minNbrDigit="1" 
											forceSelection="true"/>&nbsp;&nbsp;
							<INPUT name="descriptionRubrique" type="text" value="<%=viewBean.getLibelleRubrique()%>" class="libelleLongDisabled" readonly style="width : 12.15cm" tabindex="-1">
						</TD>
					</TR>		
				</TBODY> 
			</TABLE>
			</td>
			</tr>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>
<script>
</script>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>