<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.al.vb.decision.ALDecisionViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp"
	import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	ALDecisionViewBean viewBean = (ALDecisionViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getId();
	btnDelLabel = objSession.getLabel("SUPPRIMER");
	btnValLabel = objSession.getLabel("VALIDER");
	btnCanLabel = objSession.getLabel("ANNULER");
	btnNewLabel = objSession.getLabel("NOUVEAU");
	//désactive le bouton new depuis cet écran
	bButtonNew = false;
	bButtonDelete = false;
	
	boolean hasRightUpdate = objSession.hasRight(userActionUpd, FWSecureConstants.UPDATE);
	bButtonValidate = hasRightUpdate;

	
	idEcran="AL0016";
	userActionValue = "al.decision.decision.modifier";
	
	
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<script type="text/javascript">
var CS_DOSSIER_SUSPENDU = "<%=ALCSDossier.ETAT_SUSPENDU%>";   
var ETAT_DOSSIER = "<%=viewBean.getDossierDecisionComplexModel().getDossierModel().getEtatDossier()%>";
 </script>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.ALCSDossier"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="globaz.globall.db.BSpy"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.al.business.constantes.ALCSCopie"%>
<%@page import="ch.globaz.al.business.constantes.ALConstAdiDecomptes"%>
<script type="text/javascript">
function ajaxQuery(query,handlerStateFunction){
	
	//if (event.keyCode== 40) { // curs DOWN
		if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
			else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
			else return; // fall on our sword
		req1.open('GET', query,false); 	
		req1.onreadystatechange = alert;
		req1.send(null);
	//}
}
function add() {
    document.forms[0].elements('userAction').value="al.decision.decision.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.decision.decision.modifier";  
}
function validate() {
	state = validateFields();
	 $('input[name|=impressionBatch]').each(function(){
			$('input[name="impressionBatchOverview"]').attr("value",$('input[name="impressionBatchOverview"]').attr("value")+$(this).attr("checked")+",");
     });
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.decision.decision.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.decision.decision.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.dossier.dossierMain.afficher";
	} else {
		document.forms[0].elements('userAction').value="al.decision.decision.afficher";
	}
}
function del() {
	var msgDelete = '<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.decision.decision.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	isFromSelectionTiers = '<%=JavascriptEncoder.getInstance().encode(request.getParameter("selectedIndex")!=null?request.getParameter("selectedIndex"):"")%>';

	if(isFromSelectionTiers!='' )
		document.getElementById('newCopie').style.display='block';
}

function postInit(){
}

function displayAjaxResult(result) {
	alert(result);
}

function printDecision(inGed, printPreview) {
	
	var typeDecompte = $('input[type=radio][name=typeDecompte]:checked').attr('value');
	
	var editionDecision = $('input[type=checkbox][name=editionDecompteAvecDecision]:checked').attr('value'); 
	
	
	var req = "<%=servletContext + mainServletPath%>?userAction=al.decision.decision.executer&idDossier=<%=viewBean.getDossierDecisionComplexModel().getDossierModel().getIdDossier()%>&dateImpression="+document.getElementsByName('dateImpression')[0].value+"&printGed="+inGed+"&printPreview="+printPreview+"&typeDecompte="+typeDecompte+"&editionDecompteAvecDecision="+editionDecision ;
	ajaxQuery(req, displayAjaxResult);
}


$(function() {
	
	$( "#popupAL0016" ).hide();
	// run the currently selected effect
	function showWarnPopup() {		
		$( "#popupAL0016" ).show(5, callback);
	};

	//callback function to bring a hidden box back
	function callback() {
		setTimeout(function() {		
			$( "#popupAL0016:visible" ).removeAttr( "style" ).fadeOut();	
		}, 2000 );
	};

	// set effect from select menu value
	$( "#btnSend" ).click(function() {
		printDecision(true, false);
		//on affiche la popup que si le dossier est suspendu, car seulement là il sera remis en actif
		if(CS_DOSSIER_SUSPENDU==ETAT_DOSSIER){
			showWarnPopup();
		}
		return false;
	});

	
	
});

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="AL0016_TITRE_GESTION"/> (<ct:FWLabel key="AL0016_TITRE_DOSSIER"/>


	
<%=viewBean.getDossierDecisionComplexModel().getDossierModel()
							.getIdDossier()%>
-
<%=viewBean.getDossierDecisionComplexModel()
							.getAllocataireComplexModel().getPersonneEtendueComplexModel()
							.getTiers().getDesignation1()%>
<%=viewBean.getDossierDecisionComplexModel()
							.getAllocataireComplexModel().getPersonneEtendueComplexModel()
							.getTiers().getDesignation2()%>

							)
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<tr>
	<td>
		<div id="popupAL0016" class="popup_warn">
			<ct:FWLabel key="AL0016_DOSSIER_REACTIVE"/>
		</div>
	<td>
</tr>

<tr>
<td>
	<ct:inputHidden name="printGed"/>
	<ct:inputHidden name="printPreview"/>
	
	<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdDecompteAdi())){ %>
	
	<%@page import="ch.globaz.al.business.constantes.ALConstAdiDecomptes"%>
	<table id="AL0016typeZone" class="zone">
	
	<tr>
		<td class="subtitle" colspan="2"><ct:FWLabel key="AL0016_ENTETE_SELECTION_TYPE"/></td>
	</tr> 
	<tr>

	 	<td><input type="radio" name="typeDecompte" value="<%=ALConstAdiDecomptes.ADI_DECOMPTE_ALL%>" checked="checked"/></td>
	
	 	<td><ct:FWLabel key="AL0016_IMPR_DECOMPTE_ADI_TOUS"/></td>
	</tr>
	<tr>
		<td><input type="radio"  name="typeDecompte" value="<%=ALConstAdiDecomptes.ADI_DECOMPTE_GLOBAL%>"/></td>
	 	<td><ct:FWLabel key="AL0016_IMPR_DECOMPTE_ADI_GLOBAL"/></td>
	 	
		<td>             	
	            <input type="checkbox" checked="checked" name="editionDecompteAvecDecision"/>            		         		
	    </td>
	     <td>  		 
			<ct:FWLabel key="AL0016_DECOMPTE_AVEC_DECISION"/>  
		</td>
	  
	</tr>	
	<tr>
	 	<td><input type="radio"  name="typeDecompte" value="<%=ALConstAdiDecomptes.ADI_DECOMPTE_DETAILLE%>"/></td>
	 	<td><ct:FWLabel key="AL0016_IMPR_DECOMPTE_ADI_DETAIL"/></td>
	 </tr>
	 
	</table>
	
	<%} %>
</td>
</tr>


<tr>
	<td><%-- tpl:insert attribute="zoneMain" --%>
	<table class="tab3Col">
		<tr>
			<td class="label"><ct:FWLabel key="AL0016_ENTETE_ALLOC"/></td>
			<td>
				<input name="nomPrenomAlloc" value="<%=viewBean.getDossierDecisionComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation1()
				+ " "+viewBean.getDossierDecisionComplexModel().getAllocataireComplexModel().getPersonneEtendueComplexModel().getTiers().getDesignation2() %>" class="normal readonly" readonly="true"/>
			</td>
			<td class="label"><ct:FWLabel key="AL0016_ENTETE_DOSSIER"/></td>
			<td>
				<input type="hidden" name="commentaireModel.idDossier" value="<%=JadeStringUtil.isEmpty(viewBean.getCommentaireModel().getIdDossier())?viewBean.getDossierDecisionComplexModel().getId():viewBean.getCommentaireModel().getIdDossier()%>"/>
				<input type="hidden" name="commentaireModel.type" value="<%=ALCSDossier.COMMENTAIRE_TYPE_DECISION%>"/>
				<ct:inputText name="dossierDecisionComplexModel.dossierModel.idDossier" styleClass="normal readonly" readonly="true"/>
			</td>
			<td class="label"><ct:FWLabel key="AL0016_ENTETE_REFERENCE"/></td>
			<td>
				<ct:inputText name="dossierDecisionComplexModel.dossierModel.reference" styleClass="medium" tabindex="1"/>
			</td>
		</tr>
		<tr>
			<td class="label"><ct:FWLabel key="AL0016_ENTETE_DEBUT_VALIDITE"/></td>
			<td>
				<ct:inputText name="dossierDecisionComplexModel.dossierModel.debutValidite" styleClass="medium readonly" readonly="true"/>
			</td>
			<td class="label"><ct:FWLabel key="AL0016_ENTETE_FIN_VALIDITE"/></td>
			<td>
				<ct:inputText name="dossierDecisionComplexModel.dossierModel.finValidite" styleClass="medium readonly" readonly="true"/>
			</td>
			<td class="label"><ct:FWLabel key="AL0016_ENTETE_DATE_IMPRESSION"/></td>
			<td>
				<ct:FWCalendarTag name="dateImpression"  tabindex="2"
					value="<%=viewBean.getTodayDate()%>"
					doClientValidation="CALENDAR"/>
				<ct:inputHidden name="dateImpression" id="dateImpressionValue"/>
				<script language="JavaScript">
					document.getElementsByName('dateImpression')[0].onblur=function(){fieldFormat(document.getElementsByName('dateImpression')[0],'CALENDAR');document.getElementById('dateImpressionValue').value = this.value;};
					function theTmpReturnFunctionDateImpression(y,m,d) { 
						if (window.CalendarPopup_targetInput!=null) {
							var d = new Date(y,m-1,d,0,0,0);
							window.CalendarPopup_targetInput.value = formatDate(d,window.CalendarPopup_dateFormat);
							document.getElementById('dateImpressionValue').value = document.getElementsByName('dateImpression')[0].value;		
						}else {
							alert('Use setReturnFunction() to define which function will get the clicked results!'); 
						}	
					}
					cal_dateImpression.setReturnFunction('theTmpReturnFunctionDateImpression');
				</script>
			</td>
		</tr>
	</table>
	<hr />

	<div><ct:FWLabel key="AL0016_TEXTE_TITRE"/></div>
		<textarea style="width:100%;" tabindex="3" name="commentaireModel.texte" rows="10" cols="120" title='<%=objSession.getLabel("AL0016_TEXTE_TITRE") %>'><%=JadeStringUtil.isEmpty(viewBean.getCommentaireModel().getTexte())?"":viewBean.getCommentaireModel().getTexte()%></textarea>
	<hr />
	
	<div style="width:100%;">
	<input type="hidden" name="selectedIndex" id="selectedIndex" value="-1"/>
	<table class="copieList">
		<tr>
			<th scope="col" style="width: 5%;"></th>
			<th scope="col" style="width: 10%;"><ct:FWLabel key="AL0016_ENVOI_TEXTE_DOC"/></th>
			<th scope="col" style="width: 55%;"><ct:FWLabel key="AL0016_ENVOI_TEXTE_ENVOI"/></th>
			<th scope="col" style="width: 5%;"><ct:FWLabel key="AL0016_ENVOI_FILE_ATTENTE"/></th>
			<th scope="col" style="width: 10%;"><ct:FWLabel key="AL0016_ENVOI_ENVOI"/></th>
			<th scope="col" style="width: 5%;"><ct:FWLabel key="AL0016_ENVOI_TIERS"/></th>
			<th scope="col" style="width: 5%;"><ct:FWLabel key="AL0016_ENVOI_ADMIN"/></th>
			<th scope="col" style="width: 5%;"><ct:FWLabel key="AL0016_ENVOI_BANQUE"/></th>

		</tr>
		<%
			int nbCopie = viewBean.getCopieComplexSearchModel().getSize();
			int i = 0;
			for (i=0; i < nbCopie ; i++) {
		%>
		<tr>
			<td>
				<ct:ifhasright element="al.decision.decision.supprimerCopie" crud="crud">
            		<a 	title="Supprimer copie" class="deleteLink" 
            			href="<%=servletContext + mainServletPath + "?userAction=al.decision.decision.supprimerCopie&id="+viewBean.getId()+"&idCopieToDelete="+viewBean.getCopieAt(i).getId()%>"
            			onclick="return confirm('<%=JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION")) %>')"/>
				</ct:ifhasright>
			</td>
			<%if (i == 0) {%>
				<td><ct:FWLabel key="AL0016_ENVOI_TEXTE_ORIGINAL"/></td>
			<%} else {%>
				<td><ct:FWLabel key="AL0016_ENVOI_TEXTE_COPIE"/></td>
			<%}%>
			<td class="text">
				<input class="normal readonly" type="text" style="width: 100%; " readonly="true"
				value="<%=viewBean.getLibelleCopie(i) %>" />
			</td>
			<td align="center">
				<input type="checkbox" name="impressionBatch-<%=i%>" <%=(viewBean.getCopieAt(i).getCopieModel().getImpressionBatch().booleanValue())?"CHECKED":""%>>		
			</td>
			<td align="center">Poste <%--TODO: dynamiser le mode d'envoi --%></td>
			<td align="center">
				<%
					Object[] tiersMethodsName = new Object[]{
						new String[]{"newIdTiersDestinataire","getIdTiers"}
					};
				String selectorName="tiersSelector"+i;
				%> 
				<ct:FWSelectorTag name="<%=selectorName%>"
					methods="<%=tiersMethodsName%>"
					providerApplication="pyxis" 
					providerPrefix="TI"
					providerAction="pyxis.tiers.tiers.chercher"
				/>
				<script language="JavaScript">
					document.getElementById('<%=selectorName%>').onfocus=function(){document.getElementById('selectedIndex').value='<%=i%>';};
				</script>
				
			</td>
			<td align="center">
			
				<%
					Object[] adminMethodsName = new Object[]{
						new String[]{"newIdTiersDestinataire","getIdTiers"}
					};
				String selectorAdminName="adminSelector"+i;
				%> 
				<ct:FWSelectorTag name="<%=selectorAdminName%>"
					methods="<%=adminMethodsName%>"
					providerApplication="pyxis" 
					providerPrefix="TI"
					providerAction="pyxis.tiers.administration.chercher"
				/>
				<script language="JavaScript">
					document.getElementById('<%=selectorAdminName%>').onfocus=function(){document.getElementById('selectedIndex').value='<%=i%>';};
				</script>
			</td>
			
			<td align="center">
			
				<%
					Object[] banqueMethodsName = new Object[]{
						new String[]{"newIdTiersDestinataire","getIdTiersBanque"}
					};
				String selectorBanqueName="banqueSelector"+i;
				%> 
				<ct:FWSelectorTag name="<%=selectorBanqueName%>"
					methods="<%=banqueMethodsName%>"
					providerApplication="pyxis" 
					providerPrefix="TI"
					providerAction="pyxis.tiers.banque.chercher"
				/>
				<script language="JavaScript">
					document.getElementById('<%=selectorBanqueName%>').onfocus=function(){document.getElementById('selectedIndex').value='<%=i%>';};
				</script>
			</td>
			
		</tr>
		<%
			} 
		%>
		<tr id='newCopie'>
			<td></td>
			<%if (i == 0) {%>
				<td><ct:FWLabel key="AL0016_ENVOI_TEXTE_ORIGINAL"/></td>
			<%} else {%>
				<td><ct:FWLabel key="AL0016_ENVOI_TEXTE_COPIE"/></td>
			<%}%>
			<td class="text">
				<ct:inputHidden name="copieComplexModel.copieModel.ordreCopie" defaultValue="<%=new Integer(i+1).toString()%>"/>
				<ct:inputHidden name="copieComplexModel.copieModel.idDossier" defaultValue="<%=viewBean.getId()%>"/>
				<ct:inputHidden name="copieComplexModel.copieModel.typeCopie" defaultValue="<%=ALCSCopie.TYPE_DECISION %>"/>
				<ct:inputHidden name="copieComplexModel.copieModel.idTiersDestinataire"/>
				<input class="editText" type="text" name="libelleCopie" style="width: 100%; " readonly="true"
				value="<%=viewBean.getLibelleCopie(i) %>"/>
			
			</td>
			<td align="center">
			
				<input type="checkbox" name="impressionBatch-<%=i%>">
			</td>
			<td></td>
			<td align="center">
				<%
					Object[] tiersMethodsName = new Object[]{
						new String[]{"newIdTiersDestinataire","getIdTiers"}
					};
				String selectorName="tiersSelector"+i;
				%> 
				<ct:FWSelectorTag name="<%=selectorName%>"
					methods="<%=tiersMethodsName%>"
					providerApplication="pyxis" 
					providerPrefix="TI"
					providerAction="pyxis.tiers.tiers.chercher"
				/>
				<script language="JavaScript">
					document.getElementById('<%=selectorName%>').onfocus=function(){document.getElementById('selectedIndex').value='<%=i%>';};
				</script>
			</td>
			
			<td align="center">
			
				<%
					Object[] adminMethodsName = new Object[]{
						new String[]{"newIdTiersDestinataire","getIdTiers"}
					};
				String selectorAdminName="adminSelector"+i;
				%> 
				<ct:FWSelectorTag name="<%=selectorAdminName%>"
					methods="<%=adminMethodsName%>"
					providerApplication="pyxis" 
					providerPrefix="TI"
					providerAction="pyxis.tiers.administration.chercher"
				/>
				<script language="JavaScript">
					document.getElementById('<%=selectorAdminName%>').onfocus=function(){document.getElementById('selectedIndex').value='<%=i%>';};
				</script>
			</td>
			
			<td align="center">
			
				<%
					Object[] banqueMethodsName = new Object[]{
						new String[]{"newIdTiersDestinataire","getIdTiersBanque"}
					};
				String selectorBanqueName="banqueSelector"+i;
				%> 
				<ct:FWSelectorTag name="<%=selectorBanqueName%>"
					methods="<%=banqueMethodsName%>"
					providerApplication="pyxis" 
					providerPrefix="TI"
					providerAction="pyxis.tiers.banque.chercher"
				/>
				<script language="JavaScript">
					document.getElementById('<%=selectorBanqueName%>').onfocus=function(){document.getElementById('selectedIndex').value='<%=i%>';};
				</script>
			</td>
			
			
		</tr>
		<tfoot>
			<tr>
				<td><a title="Nouvelle copie" class="addLink"
					href="#" onclick="document.getElementById('newCopie').style.display='block'"/>
				</td>
				<td colspan="5">
					<input type="hidden" name="impressionBatchOverview" value="">
				</td>
			</tr>
		</tfoot>
	</table>
	</div>

	<%-- /tpl:insert --%></td>
</tr>
<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:insert attribute="zoneButtons" --%>

<input class="btnCtrl" id="btnPrint" type="button" value="Aperçu" onclick="printDecision(false, true);">
<% if (objSession.hasRight("al", globaz.framework.secure.FWSecureConstants.ADD)|| objSession.hasRight("al", globaz.framework.secure.FWSecureConstants.UPDATE)){%>
<input class="btnCtrl" id="btnSend" type="button" value="Envoi">
<%} %>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>	
<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdDecompteAdi())){ %>
<ct:menuChange displayId="options" menuId="dossier-adi-detail" checkAdd="no"
	showTab="options">
	<ct:menuSetAllParams key="selectedId" checkAdd="no"
		value="<%=viewBean.getDossierDecisionComplexModel().getId()%>" />
		<ct:menuSetAllParams key="idDecompte" checkAdd="no"
		value="<%=viewBean.getIdDecompteAdi()%>" />
</ct:menuChange>
<%}else{ %>
<ct:menuChange displayId="options" menuId="dossier-detail" checkAdd="no"
	showTab="options">
	<ct:menuSetAllParams key="selectedId" checkAdd="no"
		value="<%=viewBean.getDossierDecisionComplexModel().getId()%>" />
</ct:menuChange>
<%} %>
<%-- tpl:insert attribute="zoneEndPage" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf"%>
