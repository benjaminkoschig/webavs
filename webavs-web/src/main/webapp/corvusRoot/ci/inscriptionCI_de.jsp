<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_ICI_D"
	
	idEcran="PRE0045";
	
	globaz.corvus.vb.ci.REInscriptionCIViewBean viewBean = (globaz.corvus.vb.ci.REInscriptionCIViewBean) session.getAttribute("viewBean");
	
	java.util.List cucsBrancheEconomique = globaz.prestation.tools.PRCodeSystem.getCUCS(viewBean.getSession(), "VEBRANCHEE");
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

	function add() {
	}
	
	function upd() {
	}
	
	function validate() {
	    state = true;
	    //On n'ajoute rien ici, on est forcément sur une prestation existante
	    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INSCRIPTION_CI+".modifier"%>";
	    return state;
	}
	
	function cancel() {
		document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INSCRIPTION_CI%>.chercher";
		document.forms[0].submit();
	}
	
	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_INSCRIPTION_CI+".supprimer"%>";
	        document.forms[0].submit();
	    }
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
  
  function init(){
  }
  
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ICI_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<LABEL for="requerantDescription"><ct:FWLabel key="JSP_ICI_D_TIERS"/></LABEL>
								<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>">
								<INPUT type="hidden" name="forIdTiers" value="<%=viewBean.getIdTiers()%>">
							</TD>
							<TD colspan="5">
								<re:PRDisplayRequerantInfoTag
										session="<%=(globaz.globall.db.BSession)controller.getSession()%>" 
										idTiers="<%=viewBean.getIdTiers()%>"
										style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="idRCI"><ct:FWLabel key="JSP_ICI_D_NO_RASSEMBLEMENT"/></LABEL></TD>
							<TD><INPUT type="text" name="idRCI" value="<%=viewBean.getIdRCI()%>" readonly="readonly" class="disabled"></TD>
							<TD><LABEL for="idInscription"><ct:FWLabel key="JSP_ICI_D_NO_INSCRIPTION"/></LABEL></TD>
							<TD colspan="3"><INPUT type="text" name="idInscription" value="<%=viewBean.getIdInscription()%>" readonly="readonly" class="disabled"></TD>
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
							<TD><LABEL for="numeroCaisse"><ct:FWLabel key="JSP_ICI_D_CAISSE"/></LABEL></TD>
							<TD><INPUT type="text" name="numeroCaisse" value="<%=viewBean.getNumeroCaisse()%>" maxlength="3" size="3" onchange="fillWithZero(this);"></TD>
							<TD><LABEL for="numeroAgence"><ct:FWLabel key="JSP_ICI_D_AGENCE"/></LABEL></TD>
							<TD colspan="3"><INPUT type="text" name="numeroAgence" value="<%=viewBean.getNumeroAgence()%>" maxlength="3" size="3" onchange="fillWithZero(this);"></TD>
						</TR>
						<TR>
							<TD><LABEL for="moisDebutCotisations"><ct:FWLabel key="JSP_ICI_D_MOIS_DEBUT"/></LABEL></TD>
							<TD><INPUT type="text" name="moisDebutCotisations" value="<%=viewBean.getMoisDebutCotisations()%>"  maxlength="2" size="2"></TD>
							<TD><LABEL for="moisFinCotisations"><ct:FWLabel key="JSP_ICI_D_MOIS_FIN"/></LABEL></TD>
							<TD><INPUT type="text" name="moisFinCotisations" value="<%=viewBean.getMoisFinCotisations()%>"  maxlength="2" size="2"></TD>
							<TD><LABEL for="anneeCotisations"><ct:FWLabel key="JSP_ICI_D_ANNEE"/></LABEL></TD>
							<TD><INPUT type="text" name="anneeCotisations" value="<%=viewBean.getAnneeCotisations()%>"  maxlength="4" size="4"></TD>
						</TR>
						<TR>
							<TD><LABEL for="brancheEconomique"><ct:FWLabel key="JSP_ICI_D_BRANCHE_ECONOMIQUE"/></LABEL></TD>
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
							<TD><LABEL for="codeExtourne"><ct:FWLabel key="JSP_ICI_D_CODE_EXTOURNE"/></LABEL></TD>
							<TD><INPUT name="codeExtourne" value="<%=viewBean.getCodeExtourne()%>" maxlength="1" size="1"></TD>
							<TD><LABEL for="genreCotisation"><ct:FWLabel key="JSP_ICI_D_GENRE_COTISATION"/></LABEL></TD>
							<TD><INPUT name="genreCotisation" value="<%=viewBean.getGenreCotisation()%>" maxlength="1" size="1"></TD>
							<TD><LABEL for="codeParticulier"><ct:FWLabel key="JSP_ICI_D_CODE_SPLITTING"/></LABEL></TD>
							<TD><INPUT name="codeParticulier" value="<%=viewBean.getCodeParticulier()%>" maxlength="1" size="1"></TD>
						</TR>
						<TR>
							<TD><LABEL for="revenu"><ct:FWLabel key="JSP_ICI_D_REVENU"/></LABEL></TD>
							<TD><INPUT name="revenu" value="<%=viewBean.getRevenu()%>"  class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"></TD>
							<TD><LABEL for="codeADS"><ct:FWLabel key="JSP_ICI_D_CODE_AMMORTISSEMENT"/></LABEL></TD>
							<TD><INPUT name="codeADS" value="<%=viewBean.getCodeADS()%>" maxlength="1" size="1"></TD>
							<TD><LABEL for="partBonifAssist"><ct:FWLabel key="JSP_ICI_D_NO_PART_BONIF_ASSIST"/></LABEL></TD>
							<TD><INPUT name="partBonifAssist" value="<%=viewBean.getPartBonifAssist()%>" maxlength="3" size="3"></TD>
						</TR>
						<TR>
							<TD><LABEL for="codeSpeciale"><ct:FWLabel key="JSP_ICI_D_CODE_SPECIAL"/></LABEL></TD>
							<TD><INPUT name="codeSpeciale" value="<%=viewBean.getCodeSpecial()%>" maxlength="2" size="2"></TD>
							<TD><LABEL for="noAffilie"><ct:FWLabel key="JSP_ICI_D_NO_AFFILIE"/></LABEL></TD>
							<TD><INPUT name="noAffilie" value="<%=viewBean.getNoAffilie()%>"></TD>
							<TD><LABEL for="provenance"><ct:FWLabel key="JSP_ICI_D_PROVENANCE"/></LABEL></TD>
							<TD><INPUT name="rovenance" value="<%=viewBean.getProvenance()%>"  maxlength="2" size="2"></TD>
						</TR>
						<TR>
							<TD></TD>
							<TD></TD>
							<TD><LABEL for="ciAdd"><ct:FWLabel key="JSP_ICI_D_ATTENTE_CI_ADD"/></LABEL></TD>
							<TD colspan="1"><ct:FWCodeSelectTag name="attenteCIAdditionnelCS" codeType="RECIADDITI" defaut="<%=viewBean.getAttenteCIAdditionnelCS() %>" wantBlank="true" /></TD>
						</TR>
						<TR >
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><H5><ct:FWLabel key="JSP_ICI_D_SUPP_DIRECTIVE_ZAS"/></H5></TD>
							<TD colspan="5"><HR></TD>
						</TR>
						<TR >
							<TD colspan="6">&nbsp;</TD>
						</TR>
						<TR>
							<TD><LABEL for="codeApplication"><ct:FWLabel key="JSP_ICI_D_CODE_APPLICATION"/></LABEL></TD>
							<TD><INPUT name="codeApplication" value="<%=viewBean.getCodeApplication()%>"></TD>
							<TD><LABEL for="codeEnregistrement01"><ct:FWLabel key="JSP_ICI_D_CODE_ENREGISTREMENT"/></LABEL></TD>
							<TD><INPUT name="codeEnregistrement01" value="<%=viewBean.getCodeEnregistrement01()%>"></TD>
							<TD><LABEL for="refInterneCaisse"><ct:FWLabel key="JSP_ICI_D_REF_INT_CAISSE"/></LABEL></TD>
							<TD><INPUT name="refInterneCaisse" value="<%=viewBean.getRefInterneCaisse()%>"></TD>
						</TR>
						<TR>
							<TD><LABEL for="noAssure"><ct:FWLabel key="JSP_ICI_D_ASSURE"/></LABEL></TD>
							<TD><INPUT name="noAssure" value="<%=viewBean.getNoAvsTiers()%>"></TD>
							<TD><LABEL for="noAyantDroit"><ct:FWLabel key="JSP_ICI_D_AYANT_DROIT"/></LABEL></TD>
							<TD colspan="3"><INPUT name="noAyantDroit" value="<%=viewBean.getNoAvsTiersAyantDroit()%>"></TD>
						</TR>
						<TR>
							<TD><LABEL for="motif"><ct:FWLabel key="JSP_ICI_D_MOTIF"/></LABEL></TD>
							<TD><INPUT name="motif" value="<%=viewBean.getMotif()%>"></TD>
							<TD><LABEL for="dateCloture"><ct:FWLabel key="JSP_ICI_D_DATE_CLOTURE"/></LABEL></TD>
							<TD><INPUT name="dateCloture" value="<%=viewBean.getDateCloture()%>"></TD>
							<TD><LABEL for="dateOrdre"><ct:FWLabel key="JSP_ICI_D_DATE_ORDRE"/></LABEL></TD>
							<TD><INPUT name="dateOrdre" value="<%=viewBean.getDateOrdre()%>"></TD>
						</TR>
						<TR>
							<TD><LABEL for="caisseTenantCI"><ct:FWLabel key="JSP_ICI_D_CAISSE_TENANT_CI"/></LABEL></TD>
							<TD><INPUT name="caisseTenantCI" value="<%=viewBean.getNoCaisseTenantCI()+"."+viewBean.getNoAgenceTenantCI()%>"></TD>
							<TD><LABEL for="noAffilie"><ct:FWLabel key="JSP_ICI_D_CODE"/></LABEL></TD>
							<TD colspan="3"><INPUT name="noAffilie" value="<%=viewBean.getCodeParticulier()%>"></TD>
						</TR>
						<TR>
							<TD><LABEL for="codeSpeciale"><ct:FWLabel key="JSP_ICI_D_CODE_SPECIAL"/></LABEL></TD>
							<TD><INPUT name="codeSpeciale" value="<%=viewBean.getCodeSpecial()%>"></TD>							
						</TR>

						
						
						
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>