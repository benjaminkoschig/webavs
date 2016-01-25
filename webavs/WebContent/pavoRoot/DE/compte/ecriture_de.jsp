<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.pavo.db.inscriptions.CIJournal"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pavo.translation.CodeSystem"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.pavo.db.compte.CIEcriture"%><style type="text/css">
.visible {
	visibility: hidden;
	display: none;
}

.hiddenPage {
	visibility: visible;
	display: inline;
}

.selected {
	text-decoration: underline;
	cursor: default;
	font-weight: bold;
}

.notselected {
	text-decoration: none;
	cursor: hand;
	font-weight: normal;
}
</style>

<%@ page import="globaz.globall.util.*,globaz.pavo.util.*,globaz.globall.db.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
    globaz.pavo.db.compte.CIEcritureViewBean viewBean = (globaz.pavo.db.compte.CIEcritureViewBean)session.getAttribute ("viewBean");
//    globaz.pavo.db.inscriptions.CIJournalViewBean viewBeanJournal=(globaz.pavo.db.inscriptions.CIJournalViewBean)session.getAttribute("viewBeanJournal");
	selectedIdValue = viewBean.getEcritureId();
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select_all.jsp";
	boolean modeExtourne = "extourne".equals(request.getParameter("modeAjout"))?true:false;
	viewBean.cacherMontantSiProtege(modeExtourne);
	//si le montant est caché on masque le bouton valider
	if(viewBean.getCacherMontant()){
		bButtonValidate=false;
	}
				
	String fieldDisable = "class='disabled' readonly tabindex='-1'";			
	String codeState = fieldDisable;
	String avsState = fieldDisable;
	String formState = fieldDisable;
	String suspSupp = "";
	String greDefault = "27".equals(CIUtil.getCaisseInterneEcran())?"00":"01";
	int autoDigitAff = CIUtil.getAutoDigitAff(session);
	//int autoDigitAff=11;
	boolean isAdd = ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")));
	
	if((!modeExtourne && (viewBean.CS_TEMPORAIRE.equals(viewBean.getIdTypeCompte()) || viewBean.CS_TEMPORAIRE_SUSPENS.equals(viewBean.getIdTypeCompte()))) 
	|| ( viewBean.getMsgType().equals (viewBean.ERROR) && viewBean.isNew())) {
		// écriture temporaire
		codeState = "";
		formState = "";
		//avsState = "";
	}
	if(viewBean.CS_CODE_PROVISOIRE.equals(viewBean.getCode())) {
		codeState = "";
	}
	if(viewBean.CS_CI_SUSPENS.equals(viewBean.getIdTypeCompte()) || viewBean.CS_TEMPORAIRE_SUSPENS.equals(viewBean.getIdTypeCompte())) {
		avsState = "";
	}
    String disableCompt= "";
    if(viewBean.getIdTypeCompte().equals(globaz.pavo.db.compte.CIEcriture.CS_CI)){
    	disableCompt="class='disabled' readonly tabindex='-1'";
    }
    if(viewBean.CS_CI_SUSPENS_SUPPRIMES.equals(viewBean.getIdTypeCompte()) || viewBean.CS_CORRECTION.equals(viewBean.getIdTypeCompte())){
    	suspSupp = fieldDisable;
    }
    String defaultValue =viewBean.getNoNomEmployeur().indexOf(' ')>1?viewBean.getNoNomEmployeur().substring(0,viewBean.getNoNomEmployeur().indexOf(' ')):"";
    
	String defaultValueCaisseChomage = viewBean.getCaisseChomage();
	String defaultValueDetailInv = "";
	defaultValueDetailInv = CIUtil.getAffiliesNom(defaultValue,session);
	
	String cloture="";
	if(!JAUtil.isIntegerEmpty(viewBean.getRassemblementOuvertureId()))
	{
		cloture="class='disabled' readonly tabindex='-1'";
			
	}
	
	String ext="";
		
	if(modeExtourne){
		ext="class='disabled' readonly tabindex='-1'";
		
	}
	
    java.util.ArrayList ChampsCaches = CIUtil.getChampsAffiche(session);
    
    //savoir si les champs sont à cacher
	boolean BtaAff = true;
	boolean BrEcAff = true;
	boolean SpecialAff = true;
	boolean CacAff = true;//savoir si les champs sont à cacher
	boolean tempEditable = false;
	
	//if(ChampsCaches.indexOf("BTA") >= 0){
 	//	BtaAff = false;
 	//}
 	
 	if(ChampsCaches.indexOf("CAC") >= 0){
 		CacAff = false;
 	}
 	if(ChampsCaches.indexOf("BEC") >= 0){
 		BrEcAff = false;
 	}
 	
 	if(ChampsCaches.indexOf("Special") >= 0){
 		SpecialAff = false;
 	}
 	bButtonDelete=false;
 	if(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE.equals(viewBean.getIdTypeCompte()) 
 			|| globaz.pavo.db.compte.CIEcriture.CS_CORRECTION.equals(viewBean.getIdTypeCompte())
 			|| globaz.pavo.db.compte.CIEcriture.CS_CI_SUSPENS_SUPPRIMES.equals(viewBean.getIdTypeCompte())
 		|| globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(viewBean.getIdTypeCompte())){
 		bButtonUpdate = false;
 	}
 	
 	idEcran = "CCI0004";
	boolean isSpecEtSept = false;
	// Mandat 572
	boolean isDroitSpecial = false;
	//Pour ouvrir aux spec la possibilité de modifier le n° d'affilie et le genre
	if (!isAdd && CIUtil.isSpecialist(session) && JadeStringUtil.isBlankOrZero(viewBean.getDateCloture()) && !viewBean.CS_CIGENRE_6.equalsIgnoreCase(viewBean.getGenreEcriture())&& !viewBean.CS_CIGENRE_8.equalsIgnoreCase(viewBean.getGenreEcriture())){
		CIJournal journal = viewBean.getJournal(viewBean.getSession());
		if(JadeStringUtil.isBlankOrZero(viewBean.getCaisseChomage()) && JadeStringUtil.isBlankOrZero(viewBean.getPartBta())){
			isDroitSpecial =true;
		}
		if(journal!=null && (journal.getIdTypeInscription().equalsIgnoreCase(CIJournal.CS_APG)||journal.getIdTypeInscription().equalsIgnoreCase(CIJournal.CS_ASSURANCE_MILITAIRE)||journal.getIdTypeInscription().equalsIgnoreCase(CIJournal.CS_IJAI))){
			isDroitSpecial =false;
		}
	}
	//Pour ouvrir aux spec la possibilité de modifier les gre 7
	if (CIUtil.isSpecialist(session) && 
		globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_7.equals(viewBean.getGenreEcriture())){
			isSpecEtSept =true;
	}
 	//|| viewBean.getMsgType().equals(viewBean.ERROR)) {
%>
<SCRIPT language="JavaScript">
top.document.title = "IK - Detail einer Buchung"

function updateForm(tag){
	field = document.forms[0].elements('nomPrenom');
	if (tag.select) {
		nom = tag.select[tag.select.selectedIndex].nom;
		field.value = nom;
		document.forms[0].elements('dateNaissance').value = tag.select[tag.select.selectedIndex].date;
		document.forms[0].elements('sexe').value = tag.select[tag.select.selectedIndex].sexeFormate;
		document.forms[0].elements('pays').value = tag.select[tag.select.selectedIndex].paysFormate;
		if(field.readOnly==false) {
			field.readOnly = true;
			field.className = 'disabled';
			field.tabIndex = -1;						
		}
		document.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp?compteIndividuelId='+tag.select[tag.select.selectedIndex].idci;
	}
}

function confirmeMoisVide(){
	
	if(document.getElementById('annee').value < 1979 &&(document.forms(0).all('moisDebut').value=="" || document.forms(0).all('moisFin').value==""
		|| document.getElementById('moisDebut').value=="00" || document.getElementById('moisFin').value=="00")){
		if(confirm("<%=	viewBean.getSession().getLabel("MSG_PERIODE_NON_RENSEIGNEE")%>"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	else
	{
		return true;
	}
}

function resetNom() {
	field = document.forms[0].elements('nomPrenom');
	field.value = '';
	field.readOnly = false;
	field.className = 'libelle';
	field.tabIndex = 0;
	document.forms[0].elements('sexe').value = '';
	document.forms[0].elements('pays').value = '';
	document.forms[0].elements('dateNaissance').value = '';
	
}

function selectPartner(data) {
	//document.getElementById('partner').className = 'selected';
	//document.getElementById('mitglieder').className = 'notselected';
	//resetEmployeurPartenaire sans la clause qui vide le champ contenant le numéro AVS
	document.getElementById('partBtaInv').value="";
	document.getElementById('aijApgAmi').value="";
	<%if(!modeExtourne){%>
	document.getElementById('partnerNum').value="";
	document.getElementById('partnerNum').className = 'visible';
	document.getElementById('partialmitgliedNum').className = 'hiddenPage';
	document.getElementById('caisseChomage').value="";	
		//Si le num est vide, on est pas en copie, donc le numNNSS est seté
	if(document.getElementById('mitgliedNum').value == ''){
		if( 'true' == '<%=viewBean.getDefautltNNSSDisplay()%>' ){
			nssCheckChar('43', "mitgliedNum");

    	}else{
      		nssCheckChar('45', "mitgliedNum");
     	}
     }else{ 
     	
     	if( 'true' == '<%=viewBean.getMitgliedNumNNSS()%>' ){
     		nssCheckChar('43', "mitgliedNum");
	  	}else{
      		nssCheckChar('45', "mitgliedNum");
     	}
    }
     
	
	
	document.getElementById('caisseChomage').style.visibility='hidden';
	document.getElementById('caisseChomage').style.display='none';	
	document.getElementById('partBta').style.visibility='hidden';
	document.getElementById('partBta').style.display='none';
	document.getElementById('aijApgAmi').value=data;
	
	<%}%>
}
function selectMitglieder(data) {
	//document.getElementById('partner').className = 'notselected';
	//document.getElementById('mitglieder').className = 'selected';
	
	document.getElementById('partialmitgliedNum').value="";
	document.getElementById('mitgliedNum').value="";
	document.getElementById('partBtaInv').value="";
	document.getElementById('aijApgAmi').value="";
	document.getElementById('caisseChomage').value="";
	<%if(!modeExtourne){%>
	document.getElementById('partnerNum').className = 'hiddenPage';
	document.getElementById('partialmitgliedNum').className = 'visible';
	document.getElementById('mitgliedNumNssPrefixe').style.visibility='hidden';
	document.getElementById('mitgliedNumNssPrefixe').style.display='none';
	document.getElementById('caisseChomage').style.visibility='hidden';
	document.getElementById('caisseChomage').style.display='none';
	document.getElementById('partBta').style.visibility='hidden';
	document.getElementById('partBta').style.display='none';
	document.getElementById('aijApgAmi').value=data;
	<%}%>
}

function selectCaissChom(data){
	<%if(!modeExtourne){%>
	document.getElementById('partnerNum').className = 'visible';
	document.getElementById('partialmitgliedNum').className = 'visible';
	document.getElementById('mitgliedNumNssPrefixe').style.visibility='hidden';
	document.getElementById('mitgliedNumNssPrefixe').style.display='none';
	document.getElementById('caisseChomage').style.visibility='visible';
	document.getElementById('caisseChomage').style.display='inline';
	document.getElementById('partBta').style.visibility='hidden';
	document.getElementById('partBta').style.display='none';
	document.getElementById('aijApgAmi').value=data;
	document.getElementById('numeroDetailInv').value="<%=viewBean.getSession().getLabel("MSG_ECRITURE_NOM_AC")%>";
	<%}%>

}
function selectPartBta(data){
	resetEmployeurPartenaire();
	<%if(!modeExtourne){%>
	document.getElementById('partnerNum').className = 'visible';
	document.getElementById('partialmitgliedNum').className = 'visible';
	document.getElementById('mitgliedNumNssPrefixe').style.visibility='hidden';
	document.getElementById('mitgliedNumNssPrefixe').style.display='none';
	document.getElementById('caisseChomage').style.visibility='hidden';
	document.getElementById('caisseChomage').style.display='none';
	document.getElementById('partBta').style.visibility='visible';
	document.getElementById('partBta').style.display='inline';
	document.getElementById('aijApgAmi').value=data;

	//document.getElementById('numeroDetailInv').style.visibility='hidden';
	document.getElementById('moisDebut').value="00";
	document.getElementById('moisFin').value="00";
	document.getElementById('gre').value="00";
	document.getElementById('numeroDetailInv').value="<%=viewBean.getSession().getLabel("MSG_ECRITURE_NOM_BGS")%>";
	<%}%>
	

}
		 
function selectAmiAiApg(data){
	resetEmployeurPartenaire();
	<%if(!modeExtourne){%>
	document.getElementById('partnerNum').className = 'visible';
	document.getElementById('partialmitgliedNum').className = 'visible';
	document.getElementById('mitgliedNumNssPrefixe').style.visibility='hidden';
	document.getElementById('mitgliedNumNssPrefixe').style.display='none';
	document.getElementById('caisseChomage').style.visibility='hidden';
	document.getElementById('caisseChomage').style.display='none';
	document.getElementById('partBta').style.visibility='hidden';
	document.getElementById('partBta').style.display='none';
	document.getElementById('aijApgAmi').value=data;
	if(data==6){
		document.getElementById('numeroDetailInv').value="<%=viewBean.getSession().getLabel("MSG_ECRITURE_NOM_MIL")%>";
	}
	if(data==7){
		document.getElementById('numeroDetailInv').value="<%=viewBean.getSession().getLabel("MSG_ECRITURE_NOM_APG")%>";
	}
	if(data==8){
		document.getElementById('numeroDetailInv').value="<%=viewBean.getSession().getLabel("MSG_ECRITURE_NOM_AI")%>";
	}
	<%}%>
}

function updateEmployeurPartenaire(tag) {
	<%if (JAUtil.isStringEmpty(viewBean.getExtourne())) {%>
		var codeExtourne = 0;
	<%} else {%>
		var codeExtourne = <%=viewBean.getExtourne()%>;
	<%}%>

	if (tag.select) {
		if (codeExtourne=="0") {
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804002){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "01";
				<%} else {%>
					document.getElementById("genreEcriture").value = "1";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804001){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "03";
				<%} else {%>
					document.getElementById("genreEcriture").value = "3";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804004){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "04";
				<%} else {%>
					document.getElementById("genreEcriture").value = "4";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804008){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "02";
				<%} else {%>
					document.getElementById("genreEcriture").value = "2";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804005){
		
				if(document.getElementById("avs").value == tag.select[tag.select.selectedIndex].numAvsTiers){
					<%if(!isDroitSpecial){%>
						document.getElementById("gre").value = "03";
					<%} else {%>
						document.getElementById("genreEcriture").value = "3";
					<%}%>
				}else{
					<%if(!isDroitSpecial){%>
						document.getElementById("gre").value = "01";
					<%} else {%>
						document.getElementById("genreEcriture").value = "1";
					<%}%>
				}
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804006){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "04";
				<%} else {%>
					document.getElementById("genreEcriture").value = "4";
				<%}%>
				document.getElementById("code").options[2].selected = true;
		
			}
				
		} else {
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804002){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "11";
				<%} else {%>
					document.getElementById("genreEcriture").value = "1";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804001){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "13";
				<%} else {%>
					document.getElementById("genreEcriture").value = "3";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804004){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "14";
				<%} else {%>
					document.getElementById("genreEcriture").value = "4";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804008){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "12";
				<%} else {%>
					document.getElementById("genreEcriture").value = "2";
				<%}%>
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804005){
			
				if(document.getElementById("avs").value == tag.select[tag.select.selectedIndex].numAvsTiers){
					<%if(!isDroitSpecial){%>
						document.getElementById("gre").value = "13";
					<%} else {%>
						document.getElementById("genreEcriture").value = "3";
					<%}%>
				}else{
					<%if(!isDroitSpecial){%>
							document.getElementById("gre").value = "11";
					<%} else {%>
						document.getElementById("genreEcriture").value = "1";
					<%}%>
				}
			}
			if(tag.select[tag.select.selectedIndex].typeAffiliation==804006){
				<%if(!isDroitSpecial){%>
					document.getElementById("gre").value = "14";
				<%} else {%>
					document.getElementById("genreEcriture").value = "4";
				<%}%>
				document.getElementById("code").options[2].selected = true;
				
			}
		}
		document.getElementById('numeroDetailInv').value = tag.select[tag.select.selectedIndex].nom;
		document.getElementById('employeurPartenaire').value = tag.select[tag.select.selectedIndex].valueEmp;
	
		
		
		//détermine si affilié paritaire
		if(tag.select[tag.select.selectedIndex].typeAffiliation==804002){
			document.getElementById('forAffilieParitaire').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804005){
			document.getElementById('forAffilieParitaire').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804010){
			document.getElementById('forAffilieParitaire').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804012){
			document.getElementById('forAffilieParitaire').value = true;
		}else{
			document.getElementById('forAffilieParitaire').value = false;
		}
		
		//détermine si affilié personnel
		if(tag.select[tag.select.selectedIndex].typeAffiliation==804001){
			document.getElementById('forAffiliePersonnel').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804004){
			document.getElementById('forAffiliePersonnel').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804005){
			document.getElementById('forAffiliePersonnel').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804006){
			document.getElementById('forAffiliePersonnel').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804008){
			document.getElementById('forAffiliePersonnel').value = true;
		}else if(tag.select[tag.select.selectedIndex].typeAffiliation==804011){
			document.getElementById('forAffiliePersonnel').value = true;
		}else{
			document.getElementById('forAffiliePersonnel').value = false;
		}
		
		if (tag.select[tag.select.selectedIndex].brancheecoCode)
		try{	
		for (var i=0 ; i<document.forms[0].brancheEconomique.options.length ; i++){

				if (document.forms[0].brancheEconomique.options[i].value == tag.select[tag.select.selectedIndex].brancheecoCode) {
					
					document.forms[0].brancheEconomique.options[i].selected = true;
					document.forms[0].brancheEconomiqueView.value=document.forms[0].brancheEconomique.options[i].text;
				}
		}
		}catch(e){
		}
	}
}
function resetEmployeurPartenaire() {
	<%if(!modeExtourne){%>
	document.getElementById('partnerNum').value="";
	document.getElementById('partialmitgliedNum').value="";
	document.getElementById('mitgliedNum').value="";
	document.getElementById('numeroDetailInv').value="";
	document.getElementById('partBtaInv').value="";
	document.getElementById('aijApgAmi').value="";
	document.getElementById('caisseChomage').value="";

	try{
	for (var i=0 ; i<document.forms[0].brancheEconomique.options.length ; i++)
		if (document.forms[0].brancheEconomique.options[i].value == '') {
			document.forms[0].brancheEconomique.options[i].selected = true;
			document.forms[0].brancheEconomiqueView.value='';
		}
	}catch(e){
	}
	<%}%>		
}

function resetEmployeurPartenaireUp() {
	document.getElementById('numeroDetailInv').value="";
	document.getElementById('brancheEconomiqueView').value="";
}

function resetAfterError(data){
	
	document.getElementById('caisseChomage').value ="";
	document.getElementById('partialmitgliedNum').value="";
	document.getElementById('mitgliedNum').value="";
	document.getElementById('partnerNum').value="";
	document.getElementById('numeroDetailInv').value="";
	document.getElementById('brancheEconomiqueView').value="";
	
	
	if(data==6 || data==8 || data==7)
	{	
		selectAmiAiApg(data);
	}
	if(data==3)
	{
		selectCaissChom(data);
	}
	if(data==4)
	{
		selectPartBta(data);
	}
		
}

function updateGre(){
	if(document.getElementById("partnerNum").value==<%=CIUtil.getNumeroTicketEtudiants(session)%>)
	{
		document.getElementById("gre").value="04";
	}
	
}


</SCRIPT>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function updateGenreAff(data){
	
	
	if(data==""){
	}
	if(data==1){
		selectMitglieder(data);
	}
	if(data==2){
		selectPartner(data);
	}
	if(data==3){
		selectCaissChom(data);
		
	}
	if(data==4){
		selectPartBta(data);
	}
	if(data==6 || data==8 || data==7){
		selectAmiAiApg(data);
	}
}

function add() {
    document.forms[0].elements('userAction').value="pavo.compte.ecriture.ajouter";
    
}
function upd() {
	
}
function validate() {
	
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.compte.ecriture.ajouter";
    else{
    	if("False"==document.getElementById("ecranInscriptionsSuspens").value){
     	   	document.forms[0].elements('userAction').value="pavo.compte.ecriture.modifier";
     	  }else{
     	  	document.forms[0].elements('userAction').value="pavo.compte.ecrituresSuspens.modifier";
     	  }
    }
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.compte.ecriture.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
    	if (document.getElementById("idTypeCompte").value == "303002") {
			//var choix = window.showModalDialog('<%=servletContext%>/pavoRoot/<%=languePage%>/compte/supprimerSuspens.jsp');
			//if (choix == "SUPPRIMER_SUSPENS") {
			
				document.getElementById("idTypeCompte").value = "303003";
				document.getElementById("modeAjout").value = "<%=globaz.pavo.db.compte.CIEcriture.MODE_EXTOURNE%>";
				
			if("False"==document.getElementById("ecranInscriptionsSuspens").value){
				document.forms[0].elements('userAction').value="pavo.compte.ecriture.modifier";
				
			}else{
				
				document.forms[0].elements('userAction').value="pavo.compte.ecrituresSuspens.supprimer";
			}
			//} else {
			//	document.forms[0].elements('userAction').value="pavo.compte.ecriture.supprimer";
			//}
		//} else {
		//	document.forms[0].elements('userAction').value="pavo.compte.ecriture.supprimer";
		}else {
			document.forms[0].elements('userAction').value="pavo.compte.ecriture.supprimer";
		}
		document.forms[0].submit();
    }
}


function init() { 
<%if ("add".equals(request.getParameter("_method"))) {%>
	var elemCount = 3;
	var elementsToInit = new Array("gre", "moisDebut", "moisFin");
	var initValues = new Array("01", "01", "12");
	for (var i = 0; i < elemCount; i++) {
		var currElement = document.all[elementsToInit[i]];
		if (currElement.value == "") {
			currElement.value = initValues[i];
		}
	}
<%}%>
	try{
		if( 2!= document.getElementById('aijApgAmi').value){
		
			document.getElementById('mitgliedNumNssPrefixe').style.visibility='hidden';
			document.getElementById('mitgliedNumNssPrefixe').style.display='none';
		}
			
	}catch(e){
	}
	
	updateGenreAff(document.getElementById('aijApgAmi').value);
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail der Buchung<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

					<tr>
						<td>Versicherte</td>
						<td colspan="5"><% if(JAUtil.isStringEmpty(avsState)) { %> <nss:nssPopup
							validateOnChange="false" onChange="updateForm(tag);"
							onFailure="resetNom();" name="avs" jspName="<%=jspLocation%>"
							avsMinNbrDigit="8" avsAutoNbrDigit="11"
							nssMinNbrDigit="7" nssAutoNbrDigit="10"
							value="<%=viewBean.getPartialNss()%>"
							newnss="<%=viewBean.getNumeroavsNNSS()%>"
							/> <% } else { %>
						<input name='avs' size="17" <%=avsState%>
							value='<%=viewBean.getNssFormate()%>'> <% } %>
						<input name='nomPrenom' class='disabled' size="85" readonly
							value="<%=viewBean.getNomPrenom()%>" tabindex="-1"> 
						<input type='hidden' name='modeAjout'
						<%  if(modeExtourne) { %>
						
							value='<%=globaz.pavo.db.compte.CIEcriture.MODE_EXTOURNE%>' 
						<% }else{ %>
							value='<%=globaz.pavo.db.compte.CIEcriture.MODE_NORMAL%>'
						<%}%>
						>
						<input type="hidden" name="avsNNSS" value="<%=viewBean.getAvsNNSSDetailEcriture()%>" >
						</td>
					</tr>
					<tr>
						<td>
							&nbsp;
						</td>
						<td colspan="5">
							Geburtsdatum &nbsp;
							<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getDateNaissance()%>">
							&nbsp;
							Geschlecht &nbsp;
							<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getSexeForNNSS()%>">
							Heimatstaat &nbsp;
							<input type="text" size = "44" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getPaysForNNSS()%>">
						</td>
					
					</tr>
					<tr>
						<td colspan='5'>
						<hr size='1'>
						</td>
					</tr>
					<tr>
						<td><%if (isAdd&&!modeExtourne && JAUtil.isStringEmpty(formState)) {%> <!--<span id="mitglieder"
							class="selected" onclick="selectMitglieder();">Affilié</span> ou
						<span id="partner" class="notselected" onclick="selectPartner();">partenaire</span>-->
						<select name="genreAff" onChange="updateGenreAff(this.options[this.selectedIndex].value);" onClick="resetAfterError(this.options[this.selectedIndex].value);">
							<option name="affilieSelec" <%=(viewBean.getAijApgAmi().equals("1"))?"selected":""%> value="1">Mitglied</option>
							<option name="partenaireSelec" <%=(viewBean.getAijApgAmi().equals("2"))?"selected":""%> value="2">Ehepartner</option>
							<option name="caisseCho" <%=(viewBean.getAijApgAmi().equals("3"))?"selected":""%> value="3">ALV</option>
							<%if (BtaAff){%>
								<option name="partBtaSelec"<%=(viewBean.getAijApgAmi().equals("4"))?"selected":""%> value="4">BGS</option>
							<%}%>
							<option name="AmiSelec"<%=(viewBean.getAijApgAmi().equals("6"))?"selected":""%> value="6">MV</option>
							<option name="ApgSelec"<%=(viewBean.getAijApgAmi().equals("7"))?"selected":""%> value="7">ALV</option>
							<option name="AiSelec" <%=(viewBean.getAijApgAmi().equals("8"))?"selected":""%> value="8">IVTG</option>

						</select>
							<!--on le met en hidden pour permettre la copie, en cas d'extourne on récupère l'employeur
							existant, on le fait donc seulement en mode ajout -->
							<input type="hidden" value="" name="employeur">
						
						
						<%} else {%> Mitglied oder Partner <%}%></td>
						<td colspan="4"><%if ((isAdd&&!modeExtourne)||(isDroitSpecial)) {%> <input
							type="hidden" value="<%=defaultValue%>"
							name="employeurPartenaire"> <nss:nssPopup
							validateOnChange="true" 
							onChange="updateGre();updateEmployeurPartenaire(tag);"
							onFailure="resetEmployeurPartenaireUp();" name="mitgliedNum"
							cssclass="visible" jspName="<%=jspLocation%>" newnss="<%=viewBean.getMitgliedNumNNSS()%>"
							avsMinNbrDigit="8" avsAutoNbrDigit="11"
							nssMinNbrDigit="7" nssAutoNbrDigit="10" value="<%=viewBean.getNumeroAvsConjointWithoutPrefixe()%>"
							 />
							
							<ct:FWPopupList
							validateOnChange="true" value="<%=defaultValue%>"
							onChange="updateEmployeurPartenaire(tag);updateGre();"
							onFailure="resetEmployeurPartenaireUp();" name="partnerNum"
							size="15" jspName="<%=jspLocation2%>" className="hiddenPage"
							minNbrDigit="3" autoNbrDigit="<%=autoDigitAff%>" />
							<script>
								document.getElementById("partnerNum").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
							</script>
							<input name="caisseChomage" size="5" maxlength="5" type="text" display="none" 
							value="<%=defaultValueCaisseChomage%>">
							<script>
								document.getElementById('caisseChomage').style.visibility='hidden';
								document.getElementById('caisseChomage').style.display='none';
							</script>
							 
							 <input	name='numeroDetailInv' class='disabled' size="65" readonly 
								value="<%=defaultValueDetailInv%>"
								tabindex="-1">
								</td>
								</tr>
							<tr>
							<td>
								<input id="forAffilieParitaire" type="hidden" name="forAffilieParitaire" value="<%=viewBean.isForAffilieParitaire()%>">
								<input id="forAffiliePersonnel" type="hidden" name="forAffiliePersonnel" value="<%=viewBean.isForAffiliePersonnel()%>">
							</td>							
							<td colspan="4">							
								<ct:FWCodeSelectTag name="partBta" defaut="" libelle="both" codeType="CIPARBTA" wantBlank="true"/>
	              				<script>document.all("partBta").style.width="6cm"
	              					document.getElementById('partBta').style.visibility='hidden';
									document.getElementById('partBta').style.display='none';
	              				</script>
	              			</td>
              				</tr>	 
							
							<%} else { %> <input name='employeurPartenaire'
							size="17" <%=formState%> value="<%=defaultValue%>"
							onChange="document.getElementById('numeroDetailInv').value=''"> <input
							name='numeroDetailInv' class='disabled' size="65" readonly
							value="<%=viewBean.getNoNomEmployeur().indexOf(' ')>0?viewBean.getNoNomEmployeur().substring(viewBean.getNoNomEmployeur().indexOf(' ')+1):""%>"
							tabindex="-1"> <%}%>
							
							</td>
					</tr>
					<tr>
						<td colspan="5">
							<input type="hidden" name="aijApgAmi" value="<%=viewBean.getAijApgAmi()%>">
							<!--<input type="hidden" name="idJournal" value="">-->
							
						</td>
					</tr>
					<tr>
						<td>SZ</td>
						<td colspan="4">
						<%if (isDroitSpecial && !isAdd){
							String extourne= CodeSystem.getCodeUtilisateur(viewBean.getExtourne(), session);
								if(JadeStringUtil.isEmpty(extourne)){
									extourne = "0";
								}
							%>
							<input
							name="extourne" class="disabled" readonly="readonly" size="1" maxlength="1"
							value="<%=extourne%>">
							<input
							onkeypress="return filterCharForPositivInteger(window.event);"
							name="genreEcriture" size="2" maxlength="2"
							value="<%=CodeSystem.getCodeUtilisateur(viewBean.getGenreEcriture(), session)%>">
						<%}else{%>		
							<input
							onkeypress="return filterCharForPositivInteger(window.event);"
							name="gre" <%=formState%> size="3" maxlength="3"
							value="<%=JAUtil.isIntegerEmpty(viewBean.getGenreEcriture())?greDefault:viewBean.getGreFormat(session)%>">
						<%}%>
						</td>
						
					</tr>
					
					<tr>
						<td>Periode</td>
						<td colspan="4"><input
							onkeypress="return filterCharForPositivInteger(window.event);"
							<%=cloture%> <%=suspSupp%><%if(!"0".equals(viewBean.getRassemblementOuvertureId())){%>
							<%=formState%> <%}%>name='moisDebut' size='3' maxlength="2"
							value='<%=viewBean.getMoisDebutPad()%>'> <b> - </b> <input
							<%=suspSupp%>
							onkeypress="return filterCharForPositivInteger(window.event);"
							<%=cloture%> <%if(!"0".equals(viewBean.getRassemblementOuvertureId())){%>
							<%=formState%> <%}%>name='moisFin' size='3' maxlength="2"
							value='<%=viewBean.getMoisFinPad()%>'> <b> . </b> <input
							onkeypress="return filterCharForPositivInteger(window.event);"
							name='annee' size='5' maxlength="4" <%=formState%> 
							value='<%=JANumberFormatter.formatZeroValues(viewBean.getAnnee(),false,true)%>'>

						</td>
					</tr>

					<tr>
						<td>Betrag</td>
						<td colspan="4"><% if(JAUtil.isStringEmpty(formState)) { %> <input
							onchange="validateFloatNumber(this);"
							onkeypress="return filterCharForPositivFloat(window.event);"
							name='montant' size='12'
							value="<%=viewBean.getMontantFormat()%>">
						<% } else { 
		String montant = viewBean.getMontantFormat();
		int pos = montant.indexOf('.'); 
		String valEntiere = "";
		String valDecimale = "";
		
		if(pos==-1) { %> <input name='montantInv' class='disabled' readonly
							size='12' value="<%=montant%>" style="text-align: right"
							tabindex="-1"> <% 	
		} else {
			valEntiere = montant.substring(0,pos);
			valDecimale = montant.substring(pos+1);
		
%> <%String essai = viewBean.getRassemblementOuvertureId();%> <input
							name='montantInv' class='disabled' readonly size='9'
							value="<%=valEntiere%>" style="text-align: right" tabindex="-1">
						<% if(valEntiere.length()!=0) { %> <b> . </b> <input
							name='montantCts'<%=suspSupp%>
							<%if(!"0".equals(viewBean.getRassemblementOuvertureId())){%>
							<%}%> <%if(modeExtourne){%><%=ext%><%}%> size='3' maxlength='2'
							value='<%=valDecimale%>'> <% } } } %></td>
					</tr>
					
						
					
					<tr>
						<td>Code</td>
						<td colspan="4"><% if(JAUtil.isStringEmpty(codeState) && !modeExtourne) { %> <ct:FWCodeSelectTag
							name="code" libelle="both" defaut="<%=viewBean.getCode()%>"
							codeType="CICODAMO" wantBlank="true" /> <script>document.all("code").style.width="14cm"</script>
						<% } else { %> <input name="codeInv" <%=ext%><%=codeState%> size="74"
							value="<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCode(),session)%><%=JAUtil.isStringEmpty(globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCode(),session))?"":"-"%><%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getCode(),session)%>">
						<% } %></td>
					</tr>
					 <% if(JAUtil.isStringEmpty(formState)) { %>
              	<tr>
						<td colspan="5"><input name="partBtaInv" type="hidden"
							value="<%=JANumberFormatter.formatZeroValues(viewBean.getPartBta(),false,true)%>"
							tabIndex="-1"></td>
					</tr>
              
              <% } else { %>
              <%if(BtaAff){%>
              <tr> 
            	<td>(BGS)</td>
            	<td colspan="4">
              <input name="partBtaInv" class='disabled' readonly size="3" value="<%=JANumberFormatter.formatZeroValues(viewBean.getPartBta(),false,true)%>" tabIndex="-1">
            </td>
          </tr>  
              <%}else{%>
              <tr>
						<td colspan="5"><input name="partBtaInv" type="hidden"
							value="<%=JANumberFormatter.formatZeroValues(viewBean.getPartBta(),false,true)%>"
							tabIndex="-1"></td>
					</tr>
              
              
            <%}%>
            <%}%>
            
          
		
					
				
					<!-- 	End of replacement (po 38, b)-->

					<%if(CIUtil.isCodeSpecialIndiv(session)  ){%> 
						<tr>
						<td>Sonderfallcode</td>
						<td colspan="4"><% if(JAUtil.isStringEmpty(formState) || isSpecEtSept) { %> 
						<ct:FWCodeSelectTag	name="codeSpecial" libelle="both" defaut="<%=viewBean.getCodeSpecial()%>" codeType="CICODSPE" wantBlank="true" /> 
						<script>document.all("codeSpecial").style.width="14cm";</script>
						<% } else { %> <input name="codeSpecialInv" <%=formState%>
							size="74"
							value="<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCodeSpecial(),session)%> <%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getCodeSpecial(),session)%>">
						<% } %></td>
						</tr>
					<%}%>
					<tr>
						<td>Erwerbszweig</td>
						<td colspan="4"><% if(JAUtil.isStringEmpty(formState)) { %> <input
							name="brancheEconomiqueView" class='disabled' readonly
							value="<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getBrancheEconomique(),session)%> <%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getBrancheEconomique(),session)%>"
							tabindex='-1' size="74" > <ct:FWCodeSelectTag name="brancheEconomique" libelle="both"
							defaut="<%=viewBean.getBrancheEconomique()%>"
							codeType="VEBRANCHEE" wantBlank="true" /> <script>              	
	              	document.all("brancheEconomique").style.display="none";              	
	              </script> <% } else { %> <input
							name="brancheEconomiqueInv" <%=formState%> size="74"
							value="<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getBrancheEconomique(),session)%> <%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getBrancheEconomique(),session)%>"
							tabindex="-1"> <% } %></td>
					</tr>
          <tr> 
            <td>Personalkategorie</td>
            <td colspan="4"> 
              <% if(JAUtil.isStringEmpty(formState) || CIUtil.isSpecialist(session)) { %>
              <ct:FWCodeSelectTag name="categoriePersonnel" defaut="<%=viewBean.getCategoriePersonnel()%>" codeType="CICATPER" wantBlank="true"/>
              <script>document.all("categoriePersonnel").style.width="8cm"</script>
              <% } else { %>
              <input name="categoriePersonnelInv" <%=formState%> size="74" value="<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCategoriePersonnel(),session)%> <%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getCategoriePersonnel(),session)%>" >
              <% } %>
            </td>
          </tr>
          	
					<tr>
						<td valign="top">IK-Buchungen &nbsp;</td>
						<td valign="top" colspan="4"><IFRAME name="dernieresEcritures"
							scrolling="YES" style="border: solid 1px black; width: 14.2cm"
							height="100"> </IFRAME> <script>document.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp?compteIndividuelId=<%=viewBean.getCompteIndividuelId()%>';</script>
						</td>
					</tr>
					<tr>
						<td>Buchungsart</td>
						<td colspan="4"><input name="idTypeCompteInv" class="disabled"
							readonly size="19"
							value='<%=!JAUtil.isStringEmpty(viewBean.getIdTypeCompte())&&!isAdd?globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdTypeCompte(),session):""%>'
							tabindex="-1"> <input name="idTypeCompte" type="hidden"
							<%if(!isAdd){%>
								value="<%=viewBean.getIdTypeCompte()%>"
							<%}else{%>
								value=<%=globaz.pavo.db.compte.CIEcriture.CS_CI%>
							<%}%>
							>
						</td>
					</tr>
					<tr>
						<td>Datum</td>
						<td width="60" align="left">Auftrag</td>
						<td width="60" align="left">Abschluss</td>
						<td width="60" align="left">Uebermittlung</td>
						<td width="60" align="left">ZAS-Meldung</td>
						<td width="200" align="left">&nbsp;</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td align="left"><input name='dateOrdre' size='11'
							class='disabled' readonly value='<%=viewBean.getDateOrdre()%>'
							tabindex="-1"></td>
						<td align="left"><input name='dateClotureInv' size='11'
							class='disabled' readonly value='<%=viewBean.getDateCloture()%>'
							tabindex="-1"></td>
						<td align="left"><input name='dateCiAdditionnelInv' size='11'
							class='disabled' readonly
							value='<%=viewBean.getDateTransmission()%>' tabindex="-1"></td>
						<td align="left"><input name='dateAnnonceCentraleInv' size='11'
							class='disabled' readonly
							value='<%=JACalendar.format(viewBean.getDateAnnonceCentrale())%>'
							tabindex="-1"></td>
							<input type="hidden" name="ecranInscriptionsSuspens" value="<%=viewBean.getEcranInscriptionsSuspens()%>">
							<input type="hidden" name="fromEcran" value="true">

					</tr>
					<tr>
						<td colspan='6'>
						<hr size='1'>
						</td>
					</tr>
					<!--tr> 
            <td>Date de clôture</td>
            <td colspan="4"> 
              <input name='dateClotureInv' size='11' class='disabled' readonly value='<%=viewBean.getDateCloture()%>' tabindex="-1">
            </td>
          </tr>
          
          
		  <tr> 
            <td>Uebermittlung</td>
            <td colspan="4"> 
              <input name='dateCiAdditionnelInv' size='11' class='disabled' readonly value='<%=viewBean.getDateTransmission()%>' tabindex="-1">
            </td>
          </tr>
          <tr> 
            <td>ZAS-Meldung</td>
            <td colspan="4"> 
              <input name='dateAnnonceCentraleInv' size='11' class='disabled' readonly value='<%=JACalendar.format(viewBean.getDateAnnonceCentrale())%>' tabindex="-1">
            </td>
          </tr-->

					<tr>
						<td>Buchung vorgenommen am</td>
						<td colspan="4"><% 
	if(!JAUtil.isStringEmpty(viewBean.getEspionSaisie())&& !isAdd) {
		BSpy saisie = new BSpy(viewBean.getEspionSaisie());
%> <input name='dateSaisieInv' class='disabled' size='11' readonly
							value='<%=saisie.getDate()%>' tabindex="-1"> um <input
							name='timeSaisieInv' class='disabled' size='9' readonly
							value='<%=saisie.getTime()%>' tabindex="-1"> <% } else { %> <input
							name='dateSaisieInv' class='disabled' size='11' readonly value=''
							tabindex="-1"> um <input name='timeSaisieInv' class='disabled'
							size='9' readonly value='' tabindex="-1"> <% } %> &nbsp;durch&nbsp;
						<input name='utilisateurSaisieInv' class='disabled' size="30"
							readonly value='<%=viewBean.getNomUserCreation()%>' tabindex="-1">
					</tr>
					<%
	if(viewBean.getIdTypeCompte().equals(viewBean.CS_CI_SUSPENS_SUPPRIMES)){
		BSpy saisie = new BSpy(viewBean.getEspionSaisieSup());%>
					<tr>
						<td>Gelöscht am</TD>
						<%if(!JAUtil.isStringEmpty(viewBean.getEspionSaisieSup())){%>
						<td colspan="4"><INPUT name='dateSuppression'class='disabled' size='11' readonly value='<%=saisie.getDate()%>'> um 
						<INPUT name='timeSaisieSup' class='disabled' size='9' readonly value='<%=saisie.getTime()%>' tabindex="-1">
						 &nbsp;durch&nbsp;
						<INPUT name='userSaisieSup' class='disabled' size='30' readonly value='<%=viewBean.getNomSuppressor()%>'></td><%}else{%>
						<td colspan="4"><INPUT name='dateSuppression'class='disabled' size='11' readonly value=''> um 
						<INPUT name='timeSaisieSup' class='disabled' size='9' readonly value='' tabindex="-1">
						 &nbsp;durch&nbsp;
						<INPUT name='userSaisieSup' class='disabled' size='30' readonly value=''></td>
						<%}}%>
						
					</tr>
					<tr>
						<td>Verlauf Abr.-Nr.</td>
						<td>
							<input type = "text" onkeypress="return filterCharForFloat(window.event);" size = "14" maxlength="14" name ="affHist" value="<%=viewBean.getAffHist()%>">
						</td>
					</tr>
					<tr>
						<td>Beschreibung Verlauf</td>
						<td colspan = "4">
							<input type = "text" size = "42" maxlength="42" name ="libelleAff" value="<%=viewBean.getLibelleAff()%>">
						</td>
					</tr>
					<tr>
						<td valign="top">Bemerkungen</td>
						<td colspan="4"><textarea name="remarque" rows="3"
							style="border: solid 1px black; width: 14.4cm"><%=viewBean.getRemarque()%></textarea>
						</td>
					</tr>

					<!--tr>
          		<td>Journal</td>
          		<td><input type="text" value="<%=viewBean.getIdJournal()%>, <%=viewBean.getJournal(null,true).getLibelle()%>" class="disabled" readonly size="<%=(viewBean.getIdJournal()+", "+viewBean.getJournal(null,true).getLibelle()).length()+10%>">&nbsp;<A href="pavo?userAction=pavo.inscriptions.journal.afficher&selectedId=<%=viewBean.getIdJournal()%>">journal</A> </td>

        
          </tr-->
          <%
          	
          	if(CIUtil.isSpecialist(session) && (globaz.pavo.db.compte.CIEcriture.CS_CI_SUSPENS.equals(viewBean.getIdTypeCompte()) 
          		|| CIEcriture.CS_CIGENRE_8.equals(viewBean.getGenreEcriture()))){
          		bButtonDelete=true;
          	}
          %>

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

	

	<%if(CIUtil.isSpecialist(session)){
		if(CIEcriture.CS_CI_SUSPENS.equals(viewBean.getIdTypeCompte())){%>
		<ct:menuChange displayId="options" menuId="ecriture-detailSuspens" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getEcritureId()%>"/>
		</ct:menuChange>
		<%}else	if(tempEditable) {%>
		<ct:menuChange displayId="options" menuId="ecriture-detailTemp" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getEcritureId()%>"/>
		</ct:menuChange>
			
		<%}else{%>
		<ct:menuChange displayId="options" menuId="ecriture-detail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getEcritureId()%>"/>
		</ct:menuChange>

	    <%}
	  } else{%>
		<ct:menuChange displayId="options" menuId="ecriture-detailNoSpez" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getEcritureId()%>"/>
		</ct:menuChange>	  
		
	<%}%>



<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>