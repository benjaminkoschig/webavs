 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.naos.db.beneficiairepc.AFQuittancePCGFacturationProcessViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran="";%>
<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="java.util.LinkedList" %>
<%
idEcran = "CAF0072";
AFQuittancePCGFacturationProcessViewBean viewBean = (AFQuittancePCGFacturationProcessViewBean) session.getAttribute("viewBean");
	//userActionValue = "naos.beneficiairepc.journalQuittances.executerGenerer";
	userActionValue = "naos.beneficiairepc.processGenererQuittances.executer";
	selectedIdValue = "";
	%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Génération des quittances";
window.onload = document.write("<span id='infobulle' style='position:absolute;visibility:hidden;padding:3px;'>&nbsp;</span>");
// stop hiding -->
function infobulle(corps,event){
   var couleur_fond = 'white';
   var couleur_texte = 'black';
   var couleur_bordure = 'gray';
   var type_bordure = 'solid'; //(solid dashed dotted double)
   var taille_bordure = '1px'; //px
   
   document.getElementById('infobulle').style.color = couleur_texte;
   document.getElementById('infobulle').style.backgroundColor = couleur_fond;
   document.getElementById('infobulle').style.borderColor = couleur_bordure;
   document.getElementById('infobulle').style.borderStyle = type_bordure;
   document.getElementById('infobulle').style.borderWidth = taille_bordure;
   document.getElementById('infobulle').innerHTML = corps;
   document.getElementById('infobulle').style.visibility = 'visible';
   document.getElementById('infobulle').style.left = event.clientX+10+"px";
   document.getElementById('infobulle').style.top = event.clientY+20+"px";
}

function infobulle_cache(){
   document.getElementById('infobulle').style.visibility = 'hidden';
}

function postInit(){
	/*
	$("#taux").hide();
	$('input[type=radio][name=typeMontant]').change(function () {
		if($(this).val()=="montantNet"){
			$('#montantBrut').checked = false;
			$("#taux").show();
		} else {
			$("#taux").hide();
			$('input[name=taux]').val("");
		}
	});
	*/
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Génération des quittances<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
<TR>
        	<TD nowrap="nowrap" width="350">Passage</TD>
            <TD><INPUT type="text" name="idPassageFacturation" class="libelleLongDisabled" data-g-string="mandatory:true" value="<%=globaz.globall.util.JAUtil.isIntegerEmpty(viewBean.getIdPassageFacturation())?"":viewBean.getIdPassageFacturation()%>" readonly="readonly" >
               <%
				Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassageFacturation","getIdPassage"},
				new String[]{"setLibellePassageFacturation","getLibelle"}
				};
               // viewBean.setDocListe(null);
				Object[] psgParams= new Object[]{};
				//String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/listeDecision_de.jsp";	
				String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/beneficiairepc/processGenererQuittances_de.jsp";	
				%>
			    <ct:FWSelectorTag 
				name="passageSelector" 
				
				methods="<%=psgMethodsName%>"
				providerPrefix="FA"			
				providerApplication ="musca"			
				providerAction ="musca.facturation.passage.chercher"			
				providerActionParams ="<%=psgParams%>"
				redirectUrl="<%=redirectUrl%>"			
				/> 
				<input type="hidden" name="selectorName" value="">
            </TD>	
</TR>
<TR>
    <TD nowrap="nowrap" width="350">Numéro du journal</TD>
    <TD><input type="text" name='dJournalQuittance' class='libelleLongDisabled' value="<%=viewBean.getIdJournalQuittances()%>" readonly="readonly"></TD>
</TR>
<TR>
    <TD nowrap="nowrap" width="350">Libellé du journal</TD>
    <TD><input type="text" name='libelle' class='libelleLongDisabled' value="<%=viewBean.getLibelleJournal()%>" readonly="readonly"></TD>
</TR>
<!-- 
<TR>
    <TD nowrap="nowrap" width="350">Mode de saisie des quittances</TD>
    <TD>
    	<input type="radio" id="montantBrut" name="typeMontant" value="montantBrut" checked="checked"/>brut
		<input type="radio" id="montantNet" name="typeMontant" value="montantNet"/>net		
		<INPUT name="taux" id="taux" type="text" class="montant"  data-g-rate="mandatory:true" style="width : 2.45cm;"></TD>
    </TD>
</TR>
 -->
<TR>
    <TD nowrap="nowrap" width="350">Adresse E-Mail</TD>
    <TD><input type="text" name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value="<%=viewBean.getEMailAddress()%>"></TD>
</TR>
<TR>
    <TD>&nbsp;</TD>
    <TD></TD>
</TR>

<TR>
<TD nowrap="nowrap" width="350">Suppression des données du passage de facturation?
<IMG onMouseOver="infobulle('<%=viewBean.getSession().getLabel("INFOBULLE_SUPP_FACTURATION")%>',event)" onMouseOut="infobulle_cache()" src="<%=request.getContextPath()%>/images/small_info.png" border="0">
</TD>
<TD nowrap height="31"><input type="checkbox" name="wantSuppressionEtatGenere" <%=(viewBean.getWantSuppressionEtatGenere().booleanValue())? "checked" : "unchecked"%>></TD>
</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>