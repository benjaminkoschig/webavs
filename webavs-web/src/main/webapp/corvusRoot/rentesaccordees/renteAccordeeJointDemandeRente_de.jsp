<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="globaz.corvus.vb.rentesaccordees.REAdressePmtUtil"%><%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>

<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_BAC_D"

	idEcran="PRE0011";

	globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean viewBean = (globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPrestationAccordee();
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");

	java.util.List codesPrestations = globaz.prestation.tools.PRCodeSystem.getCUCS(viewBean.getSession(), globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_GENRE_PRESTATION_02_0);
	java.util.List codesCasSpeciaux = globaz.prestation.tools.PRCodeSystem.getCUCS(viewBean.getSession(), globaz.corvus.api.basescalcul.IRERenteAccordee.CS_GROUPE_CAS_SPECIAUX_RENTE_01);
	java.util.Iterator iterator4 = codesCasSpeciaux.iterator();

	boolean isBenefUpdate = false;

	bButtonDelete = !IRERenteAccordee.CS_ETAT_VALIDE.equals(viewBean.getCsEtat()) &&
					!IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtat());
	
	
	if (bButtonDelete) {
		bButtonDelete= viewBean.getSession().hasRight(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE, FWSecureConstants.REMOVE);
	}
	
	bButtonUpdate = bButtonUpdate && !IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtat());

	String params = "&provenance1=TIERS";
		  params += "&provenance2=CI";
		  params += "&extraParam1=forceSingleAdrMode";
	String jspLocation = servletContext + "/corvusRoot/numeroSecuriteSocialeSF_select.jsp";
	
%>
	
	
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestationAccordee()%>"/>
	<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
	<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPrestationAccordee()%>"/>
	<ct:menuSetAllParams key="idRenteAccordee" value="<%=viewBean.getIdPrestationAccordee()%>"/>
	<ct:menuSetAllParams key="idRenteCalculee" value="<%=viewBean.getIdRenteCalculee()%>"/>
	<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=viewBean.getIdTiersBeneficiaire()%>"/>
	<ct:menuSetAllParams key="montantRenteAccordee" value="<%=viewBean.getMontantPrestation()%>"/>
	<ct:menuSetAllParams key="idBaseCalcul" value="<%=viewBean.getIdBaseCalcul()%>"/>
	<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=viewBean.getCsTypeBasesCalcul()%>"/>
	<% if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(viewBean.getCsEtat())
		    || IRERenteAccordee.CS_ETAT_CALCULE.equals(viewBean.getCsEtat())
		    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtat()))
		    
		  || (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit()))
		  || !REPmtMensuel.isValidationDecisionAuthorise(objSession)) { %>
		<ct:menuActivateNode active="no" nodeId="optdiminution"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="optdiminution"/>
	<%} %>
	<%if (!viewBean.isPreparationDecisionValide(REPmtMensuel.getDateDernierPmt(viewBean.getSession()))){%>
		<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="preparerDecisionRA"/>
	<%} %>

	<% if ((IRERenteAccordee.CS_ETAT_VALIDE.equals(viewBean.getCsEtat())
		    || IRERenteAccordee.CS_ETAT_PARTIEL.equals(viewBean.getCsEtat()))						    
		  && globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit())) { %>
		<ct:menuActivateNode active="yes" nodeId="annoncePonctuelle"/>
	<% } else {%>
	 	<ct:menuActivateNode active="no" nodeId="annoncePonctuelle"/>
	 <%}%>	
	
	<% if (viewBean.isAfficherRepriseDuDroit()){%>
		<ct:menuActivateNode nodeId="repriseDroit"  active="yes"/>
	<%} else {%>
		<ct:menuActivateNode nodeId="repriseDroit"  active="no"/>
	<%} %>
	
	
	<ct:menuActivateNode active="yes" nodeId="optfamille"/>
	
</ct:menuChange>

<script language="JavaScript">


	//Tableau des adresses de paiements
	//---------------------------------------------------------------------------
	//--idiersAdrPmt  |  Nom Prénom    |  Adr pmt                              --
	//---------------------------------------------------------------------------
	//--  1234        |  Dupont Jean   |  Crédit Suisse\n clearing 123145\n... --
	//--  445678      |  Donzé Mélanie |  CCP 10-4567-23\n...                  --
	//---------------------------------------------------------------------------
	
	<%
	
	int size = -1;
	if (viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire())!=null) {
		size = viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire()).size();
	}
	
	if (size>0) {	
		Map mapAdrPmt = viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire());
		Set keys = mapAdrPmt.keySet();
		Iterator iter = keys.iterator();
		int i = 0;
	%>	
		adressePmtArray = new Array(<%=size%>);
		<%while (iter.hasNext()) {
			String k = (String)iter.next();
			REAdressePmtUtil adpmt = (REAdressePmtUtil)mapAdrPmt.get(k);			
			%>
			adressePmtArray[<%=i%>] = new Array(4);
			adressePmtArray[<%=i%>][0] = "<%=adpmt.getIdTiers()%>";
			adressePmtArray[<%=i%>][1] = "<%=adpmt.getNom() + " " + adpmt.getPrenom()%>";
			adressePmtArray[<%=i%>][2] = "<%=adpmt.getCcpOuBanqueFormatte()%>";
			adressePmtArray[<%=i%>][3] = "<%=adpmt.getAdresseFormattee()%>";

			
		<%	i++;
		}%>
	<%}%>
	


	function changeMbrFamille(selectObj) {
		 var idx = selectObj.selectedIndex;
		 var idTiers = selectObj.options[idx].value;	

			if (idx==0) {
				<%
					REAdressePmtUtil elm = new REAdressePmtUtil();
					elm.setAdresseFormattee(viewBean.getAdresseFormattee());
					elm.setCcpOuBanqueFormatte(viewBean.getCcpOuBanqueFormatte());
					elm.setIdTiers(viewBean.getIdTiersBeneficiaire());				
				%>
				
			 	document.getElementById("ccpOuBanqueF").innerHTML = "<%=elm.getCcpOuBanqueFormatte()%>";
			 	document.getElementById("adresseF").innerHTML = "<%=elm.getAdresseFormattee()%>";
			 	document.getElementById("idTiersAdressePmtIC").value = <%=elm.getIdTiers()%>;			 			 
			}


			else {		 
			 for(var i=0; i < adressePmtArray.length; i++) {
					if (idTiers==adressePmtArray[i][0]) {
					 	document.getElementById("ccpOuBanqueF").innerHTML = adressePmtArray[i][2];
					 	document.getElementById("adresseF").innerHTML = adressePmtArray[i][3];
					 	document.getElementById("idTiersAdressePmtIC").value = idTiers;					 	
					}
			}
		}

			
	}

	
	function limiteur(){
	}

  	function add() {
    	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" + ".ajouter";
  	}

  function upd() {
  	document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" + ".modifier";
	testDisabledCorps();
	document.getElementById("csEtat").disabled=true;

	<%if(IRERenteAccordee.CS_ETAT_VALIDE.equals(viewBean.getCsEtat()) ||
	     IRERenteAccordee.CS_ETAT_PARTIEL.equals(viewBean.getCsEtat())){%>
		document.getElementById("boutonBlocageRA").disabled=true;
	<%}%>
  }

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" + ".ajouter";
    }else{
        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" + ".modifier";
    }
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" + "back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" + ".chercher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" + ".supprimer";
        document.forms[0].submit();
    }
  }

  function init(){
  	document.getElementById("csEtat").disabled=true;
  }

  	function metAJourCodePrestation(){
 		<%java.util.Iterator iterator3 = codesPrestations.iterator();
 		while(iterator3.hasNext()){
 		String cu = ((String)(iterator3.next())).substring(0,2);
 		String cs = ((String)(iterator3.next())).substring(0,2);%>
 		if (document.forms[0].elements('codePrestation').value == "<%=cu%>"){
 			document.getElementById("imageOKKOCodePrestation").src='<%=request.getContextPath()+"/images/ok.gif"%>';
 		} else
 		<%}%>
 		<%if (codesPrestations.size() != 0){%>
 		{
 			document.getElementById("imageOKKOCodePrestation").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
 			//document.forms[0].elements('codePrestation').value = "";
 			}
 		<%}else{%>
 			document.getElementById("imageOKKOCodePrestation").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
 			//document.forms[0].elements('codePrestation').value = "";
 		<%}%>
 	}

 	function metAJourCodeCasSpeciaux(){
 		var areCodesCasSpeciauxValid = true;

 		var codeCasSpeciaux1 = document.forms[0].elements('codeCasSpeciaux1').value;
 		var codeCasSpeciaux2 = document.forms[0].elements('codeCasSpeciaux2').value;
 		var codeCasSpeciaux3 = document.forms[0].elements('codeCasSpeciaux3').value;
 		var codeCasSpeciaux4 = document.forms[0].elements('codeCasSpeciaux4').value;
 		var codeCasSpeciaux5 = document.forms[0].elements('codeCasSpeciaux5').value;

 		// check de la validite du cs1
 		if(codeCasSpeciaux1!=""){
 			var existCodeCS1 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux1 == "<%=cu%>"){
		 			existCodeCS1 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS1){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs2
 		if(codeCasSpeciaux2!=""){
 			var existCodeCS2 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux2 == "<%=cu%>"){
		 			existCodeCS2 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS2){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs3
 		if(codeCasSpeciaux3!=""){
 			var existCodeCS3 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux3 == "<%=cu%>"){
		 			existCodeCS3 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS3){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs4
 		if(codeCasSpeciaux4!=""){
 			var existCodeCS4 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux4 == "<%=cu%>"){
		 			existCodeCS4 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS4){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		// check de la validite du cs5
 		if(codeCasSpeciaux5!=""){
 			var existCodeCS5 = false;
	 		<%iterator4 = codesCasSpeciaux.iterator();
	 		while(iterator4.hasNext()){
	 			String cu = ((String)(iterator4.next())).substring(0,2);
	 			String cs = ((String)(iterator4.next())).substring(0,2);%>
		 		if (codeCasSpeciaux5 == "<%=cu%>"){
		 			existCodeCS5 = true;
		 		}
	 		<%}%>
 			if(! existCodeCS5){
 				areCodesCasSpeciauxValid = false;
 			}
 		}

 		if (areCodesCasSpeciauxValid){
 			document.getElementById("imageOKKOCodeCasSpeciaux").src='<%=request.getContextPath()+"/images/ok.gif"%>';
		}else{
 			document.getElementById("imageOKKOCodeCasSpeciaux").src='<%=request.getContextPath()+"/images/erreur.gif"%>';
 		}

 	}

 		function postInit(){

			metAJourCodePrestation();
			metAJourCodeCasSpeciaux();

			if (document.forms[0].elements('_method').value == "add"){
				document.all('updateTiersBeneficiaire').style.display = 'block';
				document.all('noUpdateTiersBeneficiaire').style.display = 'none';
			} else {
				document.all('updateTiersBeneficiaire').style.display = 'none';
				document.all('noUpdateTiersBeneficiaire').style.display = 'block';
			}

			document.getElementById("nomBenef").disabled=true;
			document.getElementById("prenomBenef").disabled=true;
			document.getElementById("dateNaissanceBenef").disabled=true;
			document.getElementById("sexeBenef").disabled=true;
			document.getElementById("nationBenef").disabled=true;
	  	}

	  	function updateTierBeneficiaire() {
	  		isBenefUpdate = true;
			document.all('updateTiersBeneficiaire').style.display = 'block';
			document.all('noUpdateTiersBeneficiaire').style.display = 'none';
	  	}

	  	function nssChange(tag) {
			if(tag.select==null) {

			}else {

				var element = tag.select.options[tag.select.selectedIndex];

				if (element.id!=null){
					document.getElementById("idTiersBeneficiaire").value=element.idAssure;
				}
			}

				document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.afficher";
				document.forms[0].elements('changeBeneficiaire').value="true";
				document.forms[0].submit();
		}

	  	function actionBlocageRA(){
  			document.forms[0].elements('userAction').value = "<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.actionBloquerRA";
	  		document.forms[0].submit();
	 	}


	  	function actionDeblocageMontant(){
  			document.forms[0].elements('userAction').value = "<%=globaz.corvus.servlet.IREActions.ACTION_DEBLOCAGE%>.afficher";
	  		document.forms[0].submit();
	 	}

	  	function actionDesactiverBloquage(){
  			document.forms[0].elements('userAction').value = "<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.actionDesactiverBlocage";
	  		document.forms[0].submit();
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
function testDisabledCorps(){
	<%if (IRERenteAccordee.CS_ETAT_VALIDE.equals(viewBean.getCsEtat()) ||
		  IRERenteAccordee.CS_ETAT_CALCULE.equals(viewBean.getCsEtat())){%>
		var body = document.getElementById("bodyCorps");
		disabledCorps(body.getElementsByTagName("input"));
		disabledCorps(body.getElementsByTagName("select"));
	<%}%>
}
function disabledCorps(elems){
	for(var i=0;i<elems.length;i++){
		var oName =  elems(i).name;

		if (oName.length >0){
			// dans l'etat "Calcule" les champs sont modifiable
		    <%if(IRERenteAccordee.CS_ETAT_CALCULE.equals(viewBean.getCsEtat())){%>
		    	//if(! oName.indexOf("codeCasSpeciaux")=='0'){
				//
				//	document.getElementsByName(oName)[0].readonly = 'true';
				//	document.getElementsByName(oName)[0].tabindex = '-1';
				//	document.getElementsByName(oName)[0].disabled = 'true';
				//}
			<%}else{%>
				document.getElementsByName(oName)[0].readOnly = 'true';
				document.getElementsByName(oName)[0].tabindex = '-1';
			<%}%>
		}
	}
}

$('html').bind(eventConstant.JADE_FW_ACTION_DONE, function(){
	testDisabledCorps();
});

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			
		<% 	// on n'affiche pas l'icône du post-it si on créer une nouvelle rente (donc si on a pas encore d'ID de préstation accordée)
			if(!JadeStringUtil.isBlank(viewBean.getIdPrestationAccordee())){%>
				<span class="postItIcon">
					<ct:FWNote sourceId="<%=viewBean.getIdTiersBeneficiaire()%>" tableSource="<%=globaz.corvus.application.REApplication.KEY_POSTIT_RENTES%>"/>
				</span>
		<%	} %>
			
			<ct:FWLabel key="JSP_RAC_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<%if (!JadeStringUtil.isBlankOrZero(viewBean.getIdCompteAnnexeIC())) {%>
							<tr>
							<TD colspan="6">
								<ct:ifhasright element="osiris.comptes.ordresVersement" crud="r">
								<A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuParSection.chercher&selectedId=<%=viewBean.getIdCompteAnnexeIC()%>&id=<%=viewBean.getIdCompteAnnexeIC()%>&idCompteAnnexe=<%=viewBean.getIdCompteAnnexeIC()%>" class="external_link">
									<ct:FWLabel key="JSP_RA_D_COMPTE_ANNEXE_LINK"/>
								</A><br/><br/>
								</ct:ifhasright>	
							</TD>
							</tr>
						<%} %>
						<TR>
							<TD colspan="6">
								<TABLE>
									<TBODY id="updateTiersBeneficiaire" style="display:none;">
										<TR>
											<TD>
												<b><ct:FWLabel key="JSP_RAC_R_BENEFICIAIRE"/></b>
											</TD>
											<TD colspan="5">
												<ct1:nssPopup name="nssTiersBeneficiairea" onFailure="" onChange="nssChange(tag);" params="<%=params%>"
													  value="" newnss=""
													  jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3" avsAutoNbrDigit="11" nssAutoNbrDigit="10"/>
												<input type="hidden" name="idTiersBeneficiaire" value="<%=viewBean.getIdTiersBeneficiaire()%>">
												<input type="hidden" name="changeBeneficiaire" value="">
												&nbsp;<ct:FWLabel key="JSP_RAC_R_INSERER_NOUVEAU_NSS"/>
											</TD>
										</TR>
									</TBODY>
									<TBODY id="noUpdateTiersBeneficiaire" style="display:none;">
										<TR>
											<TD colspan="5">
												<re:PRDisplayRequerantInfoTag
													session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
													idTiers="<%=viewBean.getIdTiersBeneficiaire()%>"
													style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_BEN%>"/>
													&nbsp;&nbsp;
												<%if(!IRERenteAccordee.CS_ETAT_VALIDE.equals(viewBean.getCsEtat()) &&
													 !IRERenteAccordee.CS_ETAT_PARTIEL.equals(viewBean.getCsEtat()) &&
													 !IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtat())){%>
												<A href="#" onclick="updateTierBeneficiaire();"><ct:FWLabel key="JSP_RAC_R_MODIFIER"/></A>
												<%}%>
											</TD>
											<TD>
												<A  href="#" onclick="window.open('<%=servletContext%><%=("/corvus")%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.actionAfficherDossierGed&amp;noAVSId=<%=viewBean.getNssTiersBeneficiaire()%>&amp;idTiersExtraFolder=<%=viewBean.getIdTiersBeneficiaire()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>')" ><ct:FWLabel key="JSP_LIEN_GED"/></A>
											</TD>
										</TR>
									</TBODY>
									<TBODY>
									<TR>
										<TD colspan="3">
											&nbsp;
										</TD>
										<TD>
											<LABEL for="addressePaiement"><ct:FWLabel key="JSP_ADDRESSE_PAIEMENT"/></LABEL>
											<INPUT type="hidden" name="idTiersAdressePmtIC" value="<%=viewBean.getIdTiersAdressePmtIC()%>">
										</TD>
										<TD colspan="2" rowspan="2">
											<PRE><span class="IJAfficheText" id="ccpOuBanqueF"><%=viewBean.getCcpOuBanqueFormatte()%></span></PRE>
											<PRE><span class="IJAfficheText" id="adresseF"><%=viewBean.getAdresseFormattee()%></span></PRE>
										</TD>
										
									</TR>
									
									<TR>
										<TD><LABEL for="noRenteAccordee"><ct:FWLabel key="JSP_RENT_ACC_ID"/></LABEL></TD>
										<TD><INPUT type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>">
											<INPUT type="text" name="dummy" value="<%=viewBean.getIdPrestationAccordee()%>" class="disabled" readonly>
										</TD>
										<TD>&nbsp;</TD>
										<TD>
											<%-- <% if (viewBean.isModifiable()){ %> --%>
												<ct:FWSelectorTag
													name="selecteurAdresses"

													methods="<%=viewBean.getMethodesSelectionAdressePaiement()%>"
													providerApplication="pyxis"
													providerPrefix="TI"
													providerAction="pyxis.adressepaiement.adressePaiement.chercher"
													target="fr_main"
													redirectUrl="<%=mainServletPath%>"/>
											<%-- <% } %> --%>
										</TD>
										<TD colspan="2">
											&nbsp;
										</TD>
									</TR>
									
<TR>
									
										<TD colspan="4">
											&nbsp;
										</TD>
										<TD colspan="1">
											<select name="selectMbrFam" onchange="changeMbrFamille(this);">
												<option value="0"><ct:FWLabel key="JSP_RAC_R_SELECT_ADRESSE_DE"/></option>
											<%													
												Map mapAdrPmtParMbrFamille = viewBean.getMapAdrPmtMbresFamille(viewBean.getIdTiersBeneficiaire());
												if (mapAdrPmtParMbrFamille!=null) {
													Set keys = mapAdrPmtParMbrFamille.keySet();
													Iterator iter = keys.iterator();
													while (iter.hasNext()) {
														String k = (String)iter.next();
														REAdressePmtUtil admpt = (REAdressePmtUtil)mapAdrPmtParMbrFamille.get(k);
											%>
													<option value="<%=admpt.getIdTiers()%>"><%=admpt.getNom() + " " + admpt.getPrenom()%></option>
											<%		}
												}
											%>
											</select>
										</TD>
									</TR>									

									
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_R_DATE_DECISION"/></TD>
										<TD><INPUT type="text" name="dateDecision" value="<%=viewBean.getDateDecision()%>" class="disabled" readonly></TD>
										<TD>&nbsp;</TD>
										<TD colspan="3">
											<ct:FWLabel key="JSP_RAC_R_REF_PAIEMENT"/><br>
											<textarea name="referencePmt" cols="50" rows="2" onKeyDown="limiteur();"><%=viewBean.getReferencePmt()%></textarea>
										</TD>
									</TR>
									<TR><TD colspan="6" valign="middle"><hr></TD></TR>
									</TBODY>
									<TBODY id="bodyCorps">

									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_GENRE_PRESTATION"/></TD>
										<TD>
											<INPUT type="text" id="codePrestation" name="codePrestation" size="8" value="<%=viewBean.getCodePrestation()%>" onkeyup="metAJourCodePrestation()">
											<IMG src="" alt="" name="imageOKKOCodePrestation">
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_CODE_AUXILIAIRE"/></TD>
										<TD>
											<INPUT type="text" size="1" id="codeAuxilliaire" name="codeAuxilliaire" value="<%=viewBean.getCodeAuxilliaireJSP()%>">
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_FRACTION_DE_RENTE"/></TD>
										<TD>
											<INPUT type="text" size="1" id="fractionRente" name="fractionRente" value="<%=viewBean.getFractionRenteJSP()%>">
										</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_ETAT_CIVIL"/></TD>
										<TD>
											<ct:select id="csEtatCivil" name="csEtatCivil" defaultValue="<%=viewBean.getCsEtatCivil()%>" disabled="true" wantBlank="true">
												<ct:optionsCodesSystems csFamille="PYETATCIVI"/>
											</ct:select>
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_REFUGIE"/></TD>
										<TD>
											<ct:inputText name="codeRefugie" style="" id="codeRefugie" styleClass="shortNumber"/>
											<%-- <INPUT type="checkbox" name="isRefugie" value="on" <%=viewBean.isRefugie().booleanValue()?"CHECKED":""%>> --%>
											<SCRIPT>
												document.getElementById("codeRefugie").maxLength ='1';
											</SCRIPT>
										</TD>
										<TD colspan="2">&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_NSS_COMPLEMENTAIRE_1"/></TD>
										<TD>
												<ct1:nssPopup name="nssTiersComplementaire1a" onFailure="" onChange="" params="<%=params%>"
													  value="<%=viewBean.getNssTiersCompl1()%>" newnss=""
													  jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3" />
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_NSS_COMPLEMENTAIRE_2"/></TD>
										<TD>
												<ct1:nssPopup name="nssTiersComplementaire2a" onFailure="" onChange="" params="<%=params%>"
													  value="<%=viewBean.getNssTiersCompl2()%>" newnss=""
													  jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3"   />
										</TD>
										<TD colspan="2">&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_MONTANT_PRESTATION"/></TD>
										<TD><INPUT type="text" name="montantPrestation" value="<%=viewBean.getMontantPrestation()%>" class="montant"></TD>
										<TD><ct:FWLabel key="JSP_RAC_D_MONTANT_RENTE_ORDI_REMPLACEE"/></TD>
										<TD><INPUT type="text" name="montantRenteOrdiRemplacee" value="<%=viewBean.getMontantRenteOrdiRemplaceeJSP()%>" class="montant"></TD>
										<TD colspan="2">&nbsp;</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_CAS_SPECIAUX"/></TD>
										<TD colspan="3">
											<INPUT type="text" name="codeCasSpeciaux1" size="2" maxlength="2" value="<%=viewBean.getCodeCasSpeciaux1()%>" onchange="metAJourCodeCasSpeciaux()">
											<INPUT type="text" name="codeCasSpeciaux2" size="2" maxlength="2" value="<%=viewBean.getCodeCasSpeciaux2()%>" onchange="metAJourCodeCasSpeciaux()">
											<INPUT type="text" name="codeCasSpeciaux3" size="2" maxlength="2" value="<%=viewBean.getCodeCasSpeciaux3()%>" onchange="metAJourCodeCasSpeciaux()">
											<INPUT type="text" name="codeCasSpeciaux4" size="2" maxlength="2" value="<%=viewBean.getCodeCasSpeciaux4()%>" onchange="metAJourCodeCasSpeciaux()">
											<INPUT type="text" name="codeCasSpeciaux5" size="2" maxlength="2" value="<%=viewBean.getCodeCasSpeciaux5()%>" onchange="metAJourCodeCasSpeciaux()">
											<IMG src="" alt="" name="imageOKKOCodeCasSpeciaux">
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_REDUCTION_FAUTE_GRAVE"/></TD>
										<TD><INPUT type="text" name="reductionFauteGrave" value="<%=viewBean.getReductionFauteGraveJSP()%>" class="montant"></TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_DATE_DEBUT"/></TD>
										<TD>
											<input	id="dateDebutDroit"
													name="dateDebutDroit"
													data-g-calendar="type:month"	
													value="<%=viewBean.getDateDebutDroit()%>" />
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_DATE_FIN"/></TD>
										<TD>
											<input	id="dateFinDroit"
													name="dateFinDroit"
													data-g-calendar="type:month" 
													value="<%=viewBean.getDateFinDroit()%>" />
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_CODE_MUTATION"/></TD>
										<TD>
											<INPUT type="text" name="codeMutation" value="<%=viewBean.getCodeMutation()%>">
										</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_DUREE_AJOURNEMENT"/></TD>
										<TD><INPUT type="text" name="dureeAjournement" value="<%=viewBean.getDureeAjournement()%>"></TD>
										<TD><ct:FWLabel key="JSP_RAC_D_SUPPLEMENT_AJOURNEMENT"/></TD>
										<TD><INPUT type="text" name="supplementAjournement" value="<%=viewBean.getSupplementAjournementJSP()%>" class="montant"></TD>
										<TD><ct:FWLabel key="JSP_RAC_D_DATE_REVOCATION_AJOURNEMENT"/></TD>
										<TD><INPUT type="text" name="dateRevocationAjournement" value="<%=viewBean.getDateRevocationAjournementJSP()%>"></TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_ANNEE_ANTICIPATION"/></TD>
										<TD>
											<INPUT type="text" size="3" name="anneeAnticipation" value="<%=viewBean.getAnneeAnticipationJSP()%>">
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_D_MONTANT_REDUCTION_ANTICIPATION"/></TD>
										<TD><INPUT type="text" name="montantReducationAnticipation" value="<%=viewBean.getMontantReducationAnticipationJSP()%>" class="montant"></TD>
										<TD><ct:FWLabel key="JSP_RAC_D_DATE_DEBUT_ANTICIPATION"/></TD>
										<TD><INPUT type="text" name="DateDebutAnticipation" value="<%=viewBean.getDateDebutAnticipationJSP()%>"></TD>
									</TR>
									
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_TAUX_REDUC_ANTICIPATION"/></TD>
										<TD>
											<INPUT type="text" name="tauxReductionAnticipation" value="<%=viewBean.getTauxReductionAnticipation()%>">
										</TD>
										<TD></TD>
										<TD></TD>
										<TD></TD>
										<TD></TD>
									</TR>


									<TR>
<%
	if ("13".equals(viewBean.getCodePrestation())) {
%>										<td>
											<ct:FWLabel key="JSP_RAC_D_CODE_SURVIVANT_INVALIDE" />
										</td>
										<td>
											<input	type="text" 
													name="codeSurvivantInvalide" 
													value="<%=viewBean.getCodeSurvivantInvalideJSP()%>" 
													data-g-integer="sizeMax:1" 
													class="shortNumber" />
										</td>
<%
	} else {
%>										<td colspan="2">
											&nbsp;
										</td>
<%
	}
%>
										<TD><ct:FWLabel key="JSP_RAC_D_SUPPLEMENT_VEUVAGE"/></TD>
										<TD>
											 <ct:inputText id="supplementVeuvage" name="supplementVeuvage" styleClass="shortNumber"/>
											 <%--<INPUT type="text" name="supplementVeuvage" value="<%=viewBean.getSupplementVeuvage()%>" >
											<INPUT type="checkbox" name="isSupplementVeuvage" value="on" <%=viewBean.isSupplementVeuvage().booleanValue()?"CHECKED":""%>>--%>
											<SCRIPT>
												document.getElementById("supplementVeuvage").maxLength ='1';
											</SCRIPT>

										</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_PRESCRIPTION_APPLIQUEE"/></TD>
										<TD>
											<INPUT type="text" size="1" name="prescriptionAppliquee" value="<%=viewBean.getPrescriptionAppliqueeJSP()%>">
										</TD>

										<TD><ct:FWLabel key="JSP_RAC_D_ANNEE_MONTANT_RAM"/></TD>
										<TD>
											<INPUT type="text" name="anneeMontantRAM" value="<%=viewBean.getAnneeMontantRAM()%>">
										</TD>

										<TD><ct:FWLabel key="JSP_RAC_D_ETAT"/></TD>
										<TD>
											<ct:select name="csEtat" defaultValue="<%=viewBean.getCsEtat()%>">
													<ct:optionsCodesSystems csFamille="REETATRA"/>
											</ct:select>
										</TD>
									</TR>
									<TR><TD colspan="6">&nbsp;</TD></TR>
									<TR>
										<TD><ct:FWLabel key="JSP_RAC_D_RENTE_BLOQUEE"/></TD>
										<TD>
											<INPUT type="checkbox" name="isPrestationBloquee" value="on" <%=viewBean.getIsPrestationBloquee().booleanValue()?"CHECKED":""%> class="forceDisable" disabled="disabled">
										</TD>
										<TD><ct:FWLabel key="JSP_RAC_L_DATE_ECHEANCE"/></TD>
										<TD>
											<input	id="dateEcheance"
													name="dateEcheance"
													data-g-calendar="type:month"
													value="<%=viewBean.getDateEcheance()%>" />
										</TD>																						
										</TD>
									</TR>
									
									
									
									
									
									
									
									</TBODY>
								</TABLE>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if(IRERenteAccordee.CS_ETAT_VALIDE.equals(viewBean.getCsEtat()) || IRERenteAccordee.CS_ETAT_PARTIEL.equals(viewBean.getCsEtat())  ){

					String valueBlkRA = "";
					
					 	if (viewBean.getIsPrestationBloquee()!=null && !viewBean.getIsPrestationBloquee().booleanValue()) {
					 		valueBlkRA = viewBean.getSession().getLabel("JSP_RAC_D_BLOQUER");
					 		%>
					 		<ct:ifhasright element="<%=IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" crud="u">
					 		<INPUT name="boutonBlocageRA" type="button" value="<%=valueBlkRA%>" onclick="actionBlocageRA()">
					 		</ct:ifhasright>
					 	<%}%>
				 		
				 		<%if (viewBean.isMontantADebloquer()) {%>
				 			<ct:ifhasright element="<%=IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" crud="r">
				 			<INPUT name="boutonDeblocageMontant" type="button" value="<%=viewBean.getSession().getLabel("JSP_RAC_D_DEBLOQUER_MNT")%>" onclick="actionDeblocageMontant()">
				 			</ct:ifhasright>
				 		<%} else if (viewBean.isDesactiverBlocageOnly()) {%>
				 			<ct:ifhasright element="<%=IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>" crud="u">
				 			<INPUT name="boutonDeblocageMontant" type="button" value="<%=viewBean.getSession().getLabel("JSP_RAC_D_DEBLOQUER")%>" onclick="actionDesactiverBloquage()">
				 			</ct:ifhasright>
				 		<%} %>
				<%}%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>