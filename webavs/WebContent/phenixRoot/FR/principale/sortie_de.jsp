<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="CCP0021";
	globaz.phenix.db.principale.CPSortieViewBean viewBean = (globaz.phenix.db.principale.CPSortieViewBean)session.getAttribute ("viewBean");
	
	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Cotisation - Sortie"

function add() {
	document.forms[0].elements('userAction').value="phenix.principale.sortie.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.principale.sortie.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.principale.sortie.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.principale.sortie.chercher";
//	document.forms[0].elements('selectedId2').value="<%=viewBean.getIdAffiliation()%>";
	document.forms[0].elements('idTiers').value="<%=viewBean.getIdTiers()%>";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{	
		if (window.confirm("N'oubliez pas de supprimer l'affact dans la facturation si le passage est en traitement!"))
		{
			document.forms[0].elements('userAction').value="phenix.principale.sortie.supprimer";
			document.forms[0].submit();
		}
	}
}
function updateInfoAffilie(tag) {
	if (tag.select && tag.select.selectedIndex != -1) {
 		document.getElementById('idTiers').value = tag.select[tag.select.selectedIndex].idTiers;
 	    document.getElementById('nomTiers').value = tag.select[tag.select.selectedIndex].nom;
 	    document.getElementById('noAffilie').value = tag.select[tag.select.selectedIndex].numAffilie;
 	    document.getElementById('idAffiliation').value = tag.select[tag.select.selectedIndex].idAffiliation;
 	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Sortie<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<TR style="font-weight: bolder">
					<TD>&nbsp;
					</TD>
					</TR>
					<TR>
						<TD nowrap height="20" colspan="4">
					</TR>
					<TR>
						<TD nowrap width="180">N° Affilié</TD>
						<td><ct:FWPopupList name="noAffilie" onChange="updateInfoAffilie(tag);" value="<%=viewBean.getNoAffilie()%>" className="libelle" 
							jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigiAff%>" size="25" minNbrDigit="3"/>
						</td>
						<TD><INPUT type="hidden" name="idSortie"
							value="<%=viewBean.getIdSortie()%>"></TD>
					</TR>
					<TR>
						<TD nowrap><INPUT type="hidden" name="idTiers"
							value="<%=viewBean.getIdTiers()%>"></TD>
						<TD nowrap><INPUT type="hidden" name="idAffiliation"
							value="<%=viewBean.getIdAffiliation()%>"></TD>
					</TR>
					<TR>
			            <TD nowrap width="50"></TD>
			            <TD nowrap >
							<INPUT type="text" name="nomTiers" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getNomTiers()%>" readonly>
						<TD></TD>
					</TR>
					<TR>
			            <TD nowrap width="50"></TD>
			            <TD nowrap ></TD>		
						<TD><INPUT type="hidden" name="checked"
							value="<%=viewBean.getChecked()%>"></TD>
					</TR>
					<TR>
						<TD nowrap height="20" colspan="4">
						<HR size="3">
						</TD>
					</TR>
					<tr> 
			            <TD nowrap width="180">Passage</TD>
			            <TD nowrap> 
			              <INPUT type="text" name="idPassage" maxlength="15" size="15"  value="<%=viewBean.getIdPassage()%>">
			              	<%
								Object[] psgMethodsName = new Object[]{
									new String[]{"setIdPassage","getIdPassage"},
									new String[]{"setLibellePassage","getLibelle"}
								};
								Object[] psgParams= new Object[]{};
								String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/sortie_de.jsp";	
							%>
							<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
				          	<ct:FWSelectorTag 
								name="passageSelector" 
								
								methods="<%=psgMethodsName%>"
								providerPrefix="FA"			
								providerApplication ="musca"			
								providerAction ="musca.facturation.passage.chercher"			
								providerActionParams ="<%=psgParams%>"
								redirectUrl="<%=redirectUrl%>"			
							/> 
							</ct:ifhasright>
							<input type="hidden" name="selectorName" value="">
						</TD>
					</TR>
			        <tr> 
			            <TD nowrap width="180"></TD>
            			<TD nowrap> 
			              <INPUT type="text" name="libellePassage" class="libelleLongDisabled" value="<%=viewBean.getLibellePassage()%>" readonly>
    			        </TD>
 			    	    <TD nowrap></TD>
   	        			<TD nowrap></TD>
   	        		
    		      	</tr>					
					<TR>
						<TD nowrap height="20" colspan="4">
					</TR>
					<TR>
						<TD nowrap width="180">Motif de sortie</TD>
						<TD nowrap>
						<ct:FWCodeSelectTag 
            				name="motif" 
							defaut="<%=viewBean.getMotif()%>"
							codeType="VEMOTIFFIN"
							wantBlank="true"/> 									
	                  	</TD>
	                  	<TD nowrap></TD>
						<TD nowrap></TD>
					</TR>
					<TR>
						<TD nowrap height="20" colspan="4">
					</TR>
					<TR>
						<TD nowrap width="180">CI extourne</TD>
						<TD nowrap><INPUT type="text" name="montantCI"
							class="montant" value="<%=viewBean.getMontantCI()%>"
							></TD>
						<TD nowrap></TD>
						<TD nowrap></TD>
					</TR>
					<TR>
						<TD nowrap height="20" colspan="4">
					</TR>
					<TR>
						<TD nowrap width="180">Année</TD>
						<TD nowrap><INPUT type="text" name="annee"
							maxlength="4" size="6"
							value="<%=viewBean.getAnnee()%>"
							></TD>
						<TD nowrap></TD>
						<TD nowrap></TD>
					</TR>
					<TR>
						<TD nowrap height="20" colspan="4">
					</TR>				
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-Sortie" showTab="options">
	<ct:menuSetAllParams key="idSortie" value="<%=viewBean.getIdSortie()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdSortie()%>"/>
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
	<ct:menuSetAllParams key="idAffiliation" value="<%=viewBean.getIdAffiliation()%>"/>  
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>