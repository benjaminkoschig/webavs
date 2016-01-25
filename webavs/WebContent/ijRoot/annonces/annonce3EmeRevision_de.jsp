<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PIJ0025";

	globaz.ij.vb.annonces.IJAnnonce3EmeRevisionViewBean viewBean = (globaz.ij.vb.annonces.IJAnnonce3EmeRevisionViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdAnnonce();
	
	//bButtonDelete = bButtonDelete && (!viewBean.getCsEtat().equals(globaz.ij.api.annonces.IIJAnnonce.CS_ENVOYEE));
	bButtonDelete = false;
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="ij.annonces.annonce3EmeRevision.ajouter";
  }
  function upd() {
  		document.forms[0].elements('userAction').value="ij.annonces.annonce3EmeRevision.modifier";
  }

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="ij.annonces.annonce3EmeRevision.ajouter";
    
    }else{
        document.forms[0].elements('userAction').value="ij.annonces.annonce3EmeRevision.modifier";
    }
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="ij.annonces.annonce3EmeRevision.chercher";
  }

  function del() {
	  if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="ij.annonces.annonce3EmeRevision.supprimer";
        document.forms[0].submit();
    }
  }
  

  	
  	function init(){

  	}
  	
  	function postInit(){
  		//initialiser l'affichage de la deuxieme période.
  		change2EmePeriode(document.forms[0].elements('deuxiemePeriode'));
  	}
  	
  	
  	function change2EmePeriode(checkBox){
  		if (checkBox.checked){
  			document.all('periode2').style.display = 'block';
  		} else {
  			document.all('periode2').style.display = 'none';
  		}
  	
  	}
  	

  	
	
	

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ANNONCE_3EME_REVISION"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_CODE_APPLICATION"/></TD>
							<TD><INPUT type="text" name="codeApplication" value="85" class="disabled" readonly></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_CODE_ENREGISTREMENT"/></TD>
							<TD><INPUT type="text" name="codeEnregistrement" value="<%=viewBean.getCodeEnregistrement()%>"></TD>
							<TD colspan="2"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_NUMERO_CAISSE"/></TD>
							<TD><INPUT type="text" name="noCaisse" value="<%=viewBean.getNoCaisse()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_NUMERO_AGENCE"/></TD>
							<TD><INPUT type="text" name="noAgence" value="<%=viewBean.getNoAgence()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_MOIS_ANNEE_COMPTABLE"/></TD>
							<TD><INPUT type="text" name="moisAnneeComptable" value="<%=viewBean.getMoisAnneeComptable()%>"> </TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_GENRE_CARTE"/></TD>
							<TD><INPUT type="text" name="codeGenreCarte" value="<%=viewBean.getCodeGenreCarte()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_NUMERO_ASSURE"/></TD>
							<TD><INPUT type="text" name="noAssure" value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNoAssure())%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_CODE_ETAT_CIVIL"/></TD>
							<TD><INPUT type="text" name="codeEtatCivil" value="<%=viewBean.getCodeEtatCivil()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_NOMBRE_ENFANTS"/></TD>
							<TD><INPUT type="text" name="nombreEnfants" value="<%=viewBean.getNombreEnfants()%>"> </TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_REVENU_MOYEN_EFFECTIF"/></TD>
							<TD><INPUT type="text" name="revenuJournalierDeterminant" value="<%=viewBean.getRevenuJournalierDeterminant()%>"> </TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_OFFICE_AI"/></TD>
							<TD><INPUT type="text" name="officeAI" value="<%=viewBean.getOfficeAI()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_PERSONNE_SEULE"/></TD>
							<TD><INPUT type="text" name="allocationPersonneSeule" value="<%=viewBean.isAllocationPersonneSeule()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_MENAGE"/></TD>
							<TD><INPUT type="text" name="allocationMenage" value="<%=viewBean.isAllocationMenage()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_ASSISTANCE"/></TD>
							<TD><INPUT type="text" name="allocationAssistance" value="<%=viewBean.isAllocationAssistance()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_ALLOCATION_EXPLOITATION"/></TD>
							<TD><INPUT type="text" name="allocationExploitation" value="<%=viewBean.isAllocationExploitation()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_SUPPLEMENT_READAPTATION"/></TD>
							<TD><INPUT type="text" name="supplementReadaptation" value="<%=viewBean.isSupplementReadaptation()%>"></TD>														 
							<TD><ct:FWLabel key="JSP_ANNONCES_PETITE_IJ"/></TD>
							<TD><INPUT type="text" name="petiteIJ" value="<%=viewBean.getPetiteIJ()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_ANNONCES_PREMIERE_PERIODE"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_NOMBRE_JOURS"/></TD>
							<TD><INPUT type="text" name="nombreJoursPeriode1" value="<%=viewBean.getNombreJoursPeriode1()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_TAUX_JOURNALIER"/></TD>
							<TD><INPUT type="text" name="tauxJournalierPeriode1" value="<%=viewBean.getTauxJournalierPeriode1()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_GENRE_READAPTATION"/></TD>
							<TD><INPUT type="text" name="codeGenreReadaptation" value="<%=viewBean.getCodeGenreReadaptation()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_GARANTIE_AA"/></TD>
							<TD><INPUT type="text" name="garantieAA" value="<%=viewBean.getGarantieAA()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_IJ_REDUITE"/></TD>
							<TD><INPUT type="text" name="ijReduite" value="<%=viewBean.getIjReduite()%>"></TD>
						</TR>
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_INCLURE_DEUXIEME_PERIODE"/></TD>
							<TD><INPUT type="checkbox" name="deuxiemePeriode" <%=viewBean.isDeuxiemePeriode()?"CHECKED":""%> onclick="change2EmePeriode(this)"></TD>
						</TR>
					</TBODY>
					<TBODY id="periode2">
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD colspan="6"><B><ct:FWLabel key="JSP_ANNONCES_DEUXIEME_PERIODE"/></B></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_NOMBRE_JOURS"/></TD>
							<TD><INPUT type="text" name="nombreJoursPeriode2" value="<%=viewBean.getNombreJoursPeriode2()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_TAUX_JOURNALIER"/></TD>
							<TD><INPUT type="text" name="tauxJournalierPeriode2" value="<%=viewBean.getTauxJournalierPeriode2()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_GENRE_READAPTATION"/></TD>
							<TD><INPUT type="text" name="codeGenreReadaptation" value="<%=viewBean.getCodeGenreReadaptation()%>"></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_GARANTIE_AA"/></TD>
							<TD><INPUT type="text" name="garantieAA" value="<%=viewBean.getGarantieAA()%>"></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_IJ_REDUITE"/></TD>
							<TD><INPUT type="text" name="ijReduite" value="<%=viewBean.getIjReduite()%>"></TD>
						</TR>
					</TBODY>
					
					<TBODY>
						
						
						
						<TR>
							<TD colspan="6"><HR></TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_ANNONCES_PERIODE_DE"/></TD>
							<TD><ct:FWCalendarTag name="periodeDe" value="<%=viewBean.getPeriodeDe()%>"/></TD>
							<TD><ct:FWLabel key="JSP_ANNONCES_A"/></TD>
							<TD><ct:FWCalendarTag name="periodeA" value="<%=viewBean.getPeriodeA()%>"/></TD>
						</TR>


						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>