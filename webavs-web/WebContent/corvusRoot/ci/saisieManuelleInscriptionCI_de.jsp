<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_SCI_D"
	
	idEcran="PRE2059";

	globaz.corvus.vb.ci.RESaisieManuelleInscriptionCIViewBean viewBean = (globaz.corvus.vb.ci.RESaisieManuelleInscriptionCIViewBean) session.getAttribute("viewBean");	
	
	java.util.List cucsBrancheEconomique = globaz.prestation.tools.PRCodeSystem.getCUCS(viewBean.getSession(), "VEBRANCHEE");
	
	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonCancel = false;
	bButtonValidate = false;
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty">
</ct:menuChange>

<script language="JavaScript">

	function ajouter() {
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_MANUELLE_INSCRIPTION_CI%>.ajouterInscriptionCI";
		document.forms[0].submit();
	}

	function arreter() {
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_SAISIE_MANUELLE_INSCRIPTION_CI%>.arreter";
		document.forms[0].submit();
	}
  
 	function metAJourBrancheEconomique(){
 		<%java.util.Iterator iterator = cucsBrancheEconomique.iterator();
 		while(iterator.hasNext()){
 		String cu = (String)(iterator.next());
 		String cs = (String)(iterator.next());%>
 		if (document.forms[0].elements('CUBrancheEconomique').value == "<%=cu%>"){
 			document.forms[0].elements('brancheEconomiqueListe').value = "<%=cs%>";
 			document.forms[0].elements('brancheEconomique').value = "<%=cs%>";
 		} else
 		<%}%>
 		<%if (cucsBrancheEconomique.size() != 0){%>
 		{document.forms[0].elements('brancheEconomiqueListe').value = "";
 		document.forms[0].elements('brancheEconomique').value = "";}
 		<%}else{%>
 		document.forms[0].elements('brancheEconomiqueListe').value = "";
 		document.forms[0].elements('brancheEconomique').value = "";
 		<%}%>
 	}
 	
	function readOnly(flag) {
		// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
		for(i=0; i < document.forms[0].length; i++) {
	      if (!document.forms[0].elements[i].readOnly && 
	      	document.forms[0].elements[i].className != 'forceDisable' &&
	      	document.forms[0].elements[i].type != 'hidden') {
	          document.forms[0].elements[i].disabled = flag;
	      }
	  }
	}
  
	function fillWithZero(field){
		var value = field.value;
		while(value.length<3){
		
			value = "0" + value;
		}  	
		field.value = value;
	}

	function ajouterWithEnterKey(ev){  
		if (ev.type == "keypress" & ev.keyCode == 13){
			ajouter();
		}
		return ev;
	}
	
	function postInit(){
		// pour que la touche <enter> execute l'action ajouter
 	  	formulaire = document.getElementById("mainForm");
 	  	formulaire.onkeypress=function() {ajouterWithEnterKey(window.event);}
	}
	
	function convertAnneeAA2AAAA(anneeCoti){
		var value = anneeCoti.value;
		if(value.length==2){
			if(parseInt(value)>"48"){
				value = "19" + value;
			}else{
				value = "20" + value;
			}
			
			anneeCoti.value = value;
		}
	}
	
	function init(){}
	
	function add() {}
	function upd() {}
	function validate() {}
	function cancel() {}
	function del() {}

	  	

<%	
	//si on est dans une sequence d'ajout le focus sur <revenu>
	if (viewBean.isSequenceAjouterCI()) { %>
	$('html').bind(eventConstant.JADE_FW_ACTION_DONE, function() {
		$('#revenu').focus();
	});
<%	} %>
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SCI_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="requerantDescription"><ct:FWLabel key="JSP_SCI_D_TIERS"/></LABEL></TD>
							<TD colspan="5">
								<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
								<re:PRDisplayRequerantInfoTag
										session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
										idTiers="<%=viewBean.getIdTiers()%>"
										style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="idRCI"><ct:FWLabel key="JSP_SCI_D_NO_RASSEMBLEMENT"/></LABEL></TD>
							<TD colspan="5">
								<INPUT type="text" name="idRCI" value="<%=viewBean.getIdRCI()%>" readonly="readonly" class="disabled">
								<INPUT type="hidden" name="selectedId" value="<%=viewBean.getIdRCI()%>">
								<INPUT type="hidden" name="isSequenceAjouterCI" value="true">
							</TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><LABEL for="numeroCaisse"><ct:FWLabel key="JSP_SCI_D_CAISSE"/></LABEL></TD>
							<TD><INPUT type="text" name="numeroCaisse" value="<%=viewBean.getNumeroCaisse()%>" maxlength="3" size="3" onchange="fillWithZero(this);"></TD>
							<TD><LABEL for="numeroAgence"><ct:FWLabel key="JSP_SCI_D_AGENCE"/></LABEL></TD>
							<TD colspan="3"><INPUT type="text" name="numeroAgence" value="<%=viewBean.getNumeroAgence()%>" maxlength="3" size="3" onchange="fillWithZero(this);"></TD>
						</TR>
						<TR>
							<TD><LABEL for="moisDebutCotisations"><ct:FWLabel key="JSP_SCI_D_MOIS_DEBUT"/></LABEL></TD>
							<TD><INPUT type="text" name="moisDebutCotisations" value="<%=viewBean.getMoisDebutCotisations()%>" maxlength="2" size="2"></TD>
							<TD><LABEL for="moisFinCotisations"><ct:FWLabel key="JSP_SCI_D_MOIS_FIN"/></LABEL></TD>
							<TD><INPUT type="text" name="moisFinCotisations" value="<%=viewBean.getMoisFinCotisations()%>" maxlength="2" size="2"></TD>
							<TD><LABEL for="anneeCotisations"><ct:FWLabel key="JSP_SCI_D_ANNEE"/></LABEL></TD>
							<TD><INPUT type="text" name="anneeCotisations" value="<%=viewBean.getAnneeCotisations()%>" maxlength="4" size="4" onchange="convertAnneeAA2AAAA(this);"></TD>
						</TR>
						<TR>
							<TD><LABEL for="brancheEconomique"><ct:FWLabel key="JSP_SCI_D_BRANCHE_ECONOMIQUE"/></LABEL></TD>
							<TD colspan="5">
								<INPUT type="text" name="CUBrancheEconomique" value="<%=viewBean.getBrancheEconomiqueCode()%>" onkeyup="metAJourBrancheEconomique();">
								<INPUT type="hidden" name="brancheEconomique" value="<%=viewBean.getBrancheEconomique()%>">
								<ct:select name="brancheEconomiqueListe" defaultValue="<%=viewBean.getBrancheEconomique()%>" wantBlank="true" disabled="true" styleClass="forceDisable">
									<ct:optionsCodesSystems csFamille="VEBRANCHEE">
									</ct:optionsCodesSystems>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="codeExtourne"><ct:FWLabel key="JSP_SCI_D_CODE_EXTOURNE"/></LABEL></TD>
							<TD><INPUT name="codeExtourne" value="<%=viewBean.getCodeExtourne()%>" maxlength="1" size="1"></TD>
							<TD><LABEL for="genreCotisation"><ct:FWLabel key="JSP_SCI_D_GENRE_COTISATION"/></LABEL></TD>
							<TD><INPUT name="genreCotisation" value="<%=viewBean.getGenreCotisation()%>"  maxlength="1" size="1"></TD>
							<TD><LABEL for="codeParticulier"><ct:FWLabel key="JSP_SCI_D_CODE_SPLITTING"/></LABEL></TD>
							<TD><INPUT name="codeParticulier" value="<%=viewBean.getCodeParticulier()%>"  maxlength="1" size="1"></TD>
						</TR>
						<TR>
							<TD><LABEL for="revenu"><ct:FWLabel key="JSP_SCI_D_REVENU"/></LABEL></TD>
							<TD><INPUT name="revenu" id="revenu" value="<%=viewBean.getRevenu()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD><LABEL for="codeADS"><ct:FWLabel key="JSP_SCI_D_CODE_AMMORTISSEMENT"/></LABEL></TD>
							<TD><INPUT name="codeADS" value="<%=viewBean.getCodeADS()%>" maxlength="1" size="1"></TD>
							<TD><LABEL for="partBonifAssist"><ct:FWLabel key="JSP_SCI_D_NO_PART_BONIF_ASSIST"/></LABEL></TD>
							<TD><INPUT name="partBonifAssist" value="<%=viewBean.getPartBonifAssist()%>" maxlength="3" size="3"></TD>
						</TR>
						<TR>
							<TD><LABEL for="codeSpecial"><ct:FWLabel key="JSP_SCI_D_CODE_SPECIAL"/></LABEL></TD>
							<TD><INPUT name="codeSpecial" value="<%=viewBean.getCodeSpecial()%>" maxlength="2" size="2"></TD>
							<TD><LABEL for="noAffilie"><ct:FWLabel key="JSP_SCI_D_NO_AFFILIE"/></LABEL></TD>
							<TD colspan="3"><INPUT name="noAffilie" value="<%=viewBean.getNoAffilie()%>"></TD>
						</TR>
						<TR>
							<TD></TD>
							<TD></TD>
							<TD><LABEL for="attenteCiAdd"><ct:FWLabel key="JSP_ICI_D_ATTENTE_CI_ADD"/></LABEL></TD>
							<TD colspan="3"><ct:FWCodeSelectTag name="attenteCIAdditionnel" codeType="RECIADDITI" defaut="" wantBlank="true" /></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<INPUT class="btnCtrl" type="button" name="buttonStop" value="Arrêter" onclick="arreter();">
				<INPUT class="btnCtrl" type="button" name="buttonAdd" value="Ajouter" onclick="ajouter();">
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>