<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->

<SCRIPT language="JavaScript">
<%
	idEcran="CCP0026";
	globaz.phenix.db.principale.CPDecisionViewBean viewBean = (globaz.phenix.db.principale.CPDecisionViewBean)session.getAttribute ("viewBean");
	key="globaz.phenix.db.principale.CPDecisionViewBean-idDecision"+viewBean.getIdDecision();
%>
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
<SCRIPT language="JavaScript">
top.document.title = "Cotisation - Décision de remise"
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="phenix.principale.decision.ajouter";
}
function upd() {
}
function validate() {
	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="phenix.principale.decision.ajouter";
	else
		document.forms[0].elements('userAction').value="phenix.principale.decision.modifier";
	return (state);
}
function cancel() {
	document.forms[0].elements('userAction').value="phenix.principale.decision.chercher";
	document.forms[0].elements('selectedId2').value="<%=viewBean.loadAffiliation().getAffiliationId()%>";
	document.forms[0].elements('idTiers').value="<%=viewBean.loadTiers().getIdTiers()%>";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="phenix.principale.decision.supprimer";
		document.forms[0].submit();
	}
}
function init(){
}

function postInit() {
	var warningMsg = "<%=viewBean.getWarningMessage()%>";
	if (warningMsg.length > 0) {
		alert(warningMsg);
		$("#wantWarning").val("non");
		$("#btnVal").click();
	}
}
/*
*/

function showBlock(blockToShow) {
	document.all(blockToShow).style.display='block';
}
function hideBlock(blockToHide) {
	document.all(blockToHide).style.display='block';
}

function showInline(inputToShow) {
	document.all(inputToShow).style.display = 'inline';
}

function hide(inputToHide) {
	document.all(inputToHide).style.display = 'none';
}
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Remise<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	<!-- Entete -->
          <TR>
            <TD nowrap width="157" height="10">Tiers</TD>
            <TD nowrap width="150"> 
              <INPUT type="text" name="numAffilie" value="<%=viewBean.loadAffiliation().getAffilieNumero()%>" class="libelleLongDisabled" tabindex="-1" readonly>
	     </TD>
            <TD nowrap width="25"><INPUT name="wantWarning" id="wantWarning" type="hidden"></TD>
            <TD nowrap width="110" colspan="2"><INPUT type="text" name="nom" value="<%=viewBean.loadTiers().getNom()%>" class="libelleLongDisabled" tabindex="-1" readonly></TD>
          </TR>
	  <TR>
	   <TD nowrap  height="11" colspan="5"> 
              <hr size="3">
            </TD>
          </TR>          
          <TR>
            <TD nowrap width="157">Décision pour</TD>
            <TD nowrap width="244"> 
              <INPUT name="libelleGenreAffilie" type="text" value="<%=viewBean.getLibelleGenreAffilie()%>" class="libelleLongDisabled" readonly>
            </TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="110">Responsable</TD>
            <TD nowrap width="171">
		       <ct:FWListSelectTag name="responsable"
            		defaut="<%=viewBean.getResponsable()%>"
            		data="<%=viewBean.getUserList(session)%>"/>
            </TD>
          </TR>

          <TR>
            <TD nowrap width="157">Type de décision</TD>
            <TD nowrap><INPUT name="libelleTypeDecision" type="text" value="<%=viewBean.getLibelleTypeDecision()%>" class="libelleLongDisabled" readonly></TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="110">N° IFD associé</TD>
            <TD nowrap width="171"><INPUT name="numIfdDefinitif" type="text" value="<%=viewBean.getNumIfdDefinitif()%>" class="numeroCourtDisabled" readonly></TD>
          </TR>
	     <TR> 
            <TD nowrap width="157">Conjoint</TD>
            <TD nowrap colspan="4"><INPUT type="text" name="selectionCjt" value="<%=viewBean.getSelectionCjt()%>" class="numeroId" style="color : purple;"> 
 		  <%
			Object[] tiersMethodsName= new Object[]{
				new String[]{"setSelectionCjt","getNumAffilie"},
				new String[]{"setIdConjoint","getIdTiers"},
				new String[]{"setConjoint","getNom"},		
			};
			Object[] tiersParams = new Object[]{
				new String[]{"selection","_pos"},
			};

			String redirectUrl1 = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionRemise_de.jsp";	
			%>
			<ct:ifhasright element="pyxis.tiers.tiers.chercher" crud="r">
			<ct:FWSelectorTag 
				name="tiersSelector" 
				
				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.tiers.chercher"
				providerActionParams ="<%=tiersParams%>"
				redirectUrl="<%=redirectUrl1%>"
			/>
			</ct:ifhasright>
			<INPUT name="idConjoint" type="hidden" value="<%=viewBean.getIdConjoint()%>"><INPUT name="conjoint" type="text" value="<%=viewBean.getConjoint()%>" class="libelleLongDisabled" readonly style="color : purple;">
		<input type="checkbox" name="division2" <%=(viewBean.getDivision2().booleanValue())? "checked" : "unchecked"%>> </TD>
          </TR>
	   <TR>
            <TD nowrap width="157">Année de décision</TD>
            <TD nowrap><INPUT name="anneeDecision" type="text" size="4" value="<%=viewBean.getAnneeDecision()%>" maxlength="4" class="numeroCourtDisabled" readonly></TD>
            <TD nowrap width="25"><INPUT type="hidden" name="idDecision" value="<%=viewBean.getIdDecision()%>"></TD>
            
                      <%
		      	String idEntete =viewBean.rechercheIdEnteteFacture();
			    String linkFacture ="";
		      	if(!JadeStringUtil.isBlankOrZero(idEntete)){
		      		linkFacture = "musca?userAction=musca.facturation.afact.chercher&idEnteteFacture=" + idEntete + "&idPassage=" + viewBean.getIdPassage();
		      		
		      	%>
		      	<TD width="110">
		      	<ct:ifhasright element="musca.facturation.afact.chercher" crud="r">
		   		<A href="<%=request.getContextPath() + "/" + linkFacture%> class="external_link">N° de passage</A>
		   		</ct:ifhasright>
		   		</TD>
		   		<%} else {%>
		   		  <TD width="110">N° de passage</TD>
		   		  <%}%>
            <TD nowrap width="171"><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroIdDisabled" readonly></TD>
          </TR>
          <TR>
		<TD nowrap width="157">P&eacute;riode de la décision</TD>
            <TD nowrap width="280">
		<ct:FWCalendarTag name="debutDecision"
		value="<%=viewBean.getDebutDecision()%>"
		errorMessage="la date de début est incorrecte"
		doClientValidation="CALENDAR,NOT_EMPTY"
	  /> &nbsp;au&nbsp; <ct:FWCalendarTag name="finDecision"
		value="<%=viewBean.getFinDecision()%>"
		errorMessage="la date de fin est incorrecte"
		doClientValidation="CALENDAR,NOT_EMPTY"
	 /> </TD>
            <TD nowrap width="25"></TD>
            <TD nowrap width="110">Indication au</TD>
            <TD nowrap width="171"><ct:FWCalendarTag
									name="dateInformation"
									value="<%=viewBean.getDateInformation()%>"
									errorMessage="la date d'information est incorrecte"
									doClientValidation="CALENDAR,NOT_EMPTY" /></TD>
          </TR>
	   <TR> 
            <TD nowrap  height="11" colspan="5">
            <HR size="3" width="100%">
            </TD>
          </TR>
		  <TR>
            <TD nowrap width="157">Cotisation</TD>
            <TD nowrap width="96"><INPUT name="cotisation1" type="text" value="<%=viewBean.getCotisation1()%>" class="montant" style="width : 2.45cm;"></TD>
            <TD nowrap width="24">&nbsp; </TD>
            <TD nowrap width="110" height="31">Facturation</TD>
            <TD nowrap height="31"><input type="checkbox" name="facturation" <%=(viewBean.getFacturation().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD nowrap width="108"></TD>
            <TD nowrap width="98"></TD>
          </TR>
          <TR>
            <TD nowrap width="157">Commune concernée</TD>
            <td><INPUT name="communeCode" onkeydown="document.getElementsByName('communeLibelle')[0].value='';document.getElementsByName('idCommune')[0].value=''" class="montant" style="width : 2.45cm;" value="<%=viewBean.getCommuneCode()%>">
  	 <!--     <input type="button" onClick="_pos.value=localiteCode.value;_meth.value=_method.value;_act.value='pyxis.adressecourrier.adresse.afficher';userAction.value='pyxis.adressecourrier.localite.chercher';submit()" value="..."> -->
			<%
			Object[] localiteMethodsName = new Object[]{
				new String[]{"setIdCommune","getIdLocalite"},
				new String[]{"setCommuneLibelle","getLocalite"},
				new String[]{"setCommuneCode","getNumPostalEntier"}
			};
			Object[] localiteParams = new Object[]{new String[]{"localiteCode","_pos"} };
			%>
			<ct:ifhasright element="pyxis.adressecourrier.localite.chercher" crud="r">
			<ct:FWSelectorTag 
				name="localiteSelector" 
				methods="<%=localiteMethodsName %>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.adressecourrier.localite.chercher"
				providerActionParams ="<%=localiteParams%>"
				redirectUrl="<%=redirectUrl1%>"
				/>
				</ct:ifhasright>
				</td>
			 <td><INPUT type="hidden" name="idCommune" value='<%=viewBean.getIdCommune()%>'></td>
            <TD nowrap width="110" height="31"></TD>
            <TD nowrap height="31"></TD>
            <TD nowrap width="108"><INPUT type="hidden" name="selectedId2" value='<%=viewBean.loadAffiliation().getAffiliationId()%>'></TD>
            <TD nowrap width="98"><INPUT type="hidden" name="idTiers" value='<%=viewBean.loadTiers().getIdTiers()%>'></TD>
          </TR>
          <TR>
            <TD nowrap width="157">&nbsp;</TD>
              <TD nowrap width="265"><INPUT name="communeLibelle" type="text" value="<%=viewBean.getCommuneLibelle()%>" class="libelleLongDisabled" readonly></TD>
            <TD nowrap width="24">&nbsp; </TD>
	        <TD nowrap height="31" width="110">Impression</TD>
	         <TD nowrap height="31"><input type="checkbox" name="impression" <%=(viewBean.getImpression().booleanValue())? "checked" : "unchecked"%>></TD>
	         <TD nowrap width="108"></TD>
            <TD nowrap width="98"></TD>
	     </TR>
		<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<ct:menuChange displayId="options" menuId="CP-decision" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDecision()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>