<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA5005"; %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="java.util.Enumeration" %>

<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.ordres.*" %>
<%@ page import="globaz.framework.util.*" %>
<%
		
	globaz.osiris.utils.CASessionDataContainerHelper sessionContainerHelper = new globaz.osiris.utils.CASessionDataContainerHelper();
	CAPaiementEtrangerViewBean lastPmtAdded = (CAPaiementEtrangerViewBean)sessionContainerHelper.getData(session,globaz.osiris.utils.CASessionDataContainerHelper.KEY_LAST_PAIEMENT_ETRANGER);
	
	boolean prefillData = false;
	if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")) && lastPmtAdded!=null) 
		prefillData = true;
		

	CAPaiementEtrangerViewBean viewBean = (CAPaiementEtrangerViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);	
	userActionValue = "osiris.comptes.journalOperationPaiementEtranger.modifierAvantCompensation";
//	selectedIdValue = viewBean.getIdJournal();
	selectedIdValue = viewBean.getIdOperation();
	// Interdire les modifications si le journal n'est pas mutable
	if (viewBean.getJournal() != null) {
      if (!viewBean.getJournal().isUpdatable()) {
	    bButtonNew = false;
	    bButtonUpdate = false;
	  } else {
		  bButtonNew = true;
	  }
	}
  	bButtonUpdate = (bButtonUpdate && viewBean.isUpdatable());
	bButtonDelete = (bButtonDelete && !viewBean.getEstComptabilise().booleanValue());
	bButtonNew = (bButtonNew && viewBean.hasRightAdd() && !viewBean.getEstComptabilise().booleanValue());
	actionNew = actionNew + "&idJournal="+viewBean.getIdJournal();

%>
 <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-JournalOperation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdJournal()%>"/>
	<% if ((viewBean.getJournal() != null) && (!viewBean.getJournal().isAnnule())) {%>
	<ct:menuActivateNode active="no" nodeId="journal_rouvrir"/>
	<% } else { %>
	<ct:menuActivateNode active="yes" nodeId="journal_rouvrir"/>
	<% } %>
</ct:menuChange>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementEtranger.ajouterAvantCompensation"; 
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementEtranger.modifierAvantCompensation";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le paiement sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementEtranger.supprimerPaiement";
        document.forms[0].submit();
    }

}


function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementEtranger.ajouterAvantCompensation";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementEtranger.modifierAvantCompensation";
    
    return state;
}


function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.journalOperationPaiementEtranger.afficher";
}
function init(){}

function postInit() {
	document.getElementById("idLog").disabled = false;
}

function quittancer() {
	if (document.forms[0]._quittanceLogEcran.checked == true)
		document.forms[0].quittanceLogEcran.value="on";
	else
		document.forms[0].quittanceLogEcran.value="";
}


function validateNoAvs(cell) {
	var keyCode = new String(String.fromCharCode(event.keyCode));	
	if (genericFilter(new RegExp("[0-9]"), keyCode)) {	
	}
	else {		
		event.keyCode = null;
		return;
	}	
}


function tabNoAvs(cell) {			
	
	
	
	// touche tab, shift et flèches
	if (event.keyCode==null || event.keyCode==9 || event.keyCode==16 || event.keyCode==37 || event.keyCode==38 || event.keyCode==39 || event.keyCode==40)
		return;
	
	if(cell.name=='noAvs1') {
		if(cell.value.length>=3) {
			document.all('noAvs2').focus();			
		}
	}

	if(cell.name=='noAvs2') {
		if(cell.value.length>=2) {
				document.all('noAvs3').focus();				
			}
		}


	if(cell.name=='noAvs3') {
		if(cell.value.length>=3) {
				document.all('noAvs4').focus();				
			}
		}
}


function rubriqueManuelleOn(){
	document.forms[0].idCompte.value="";
}

function updateRubrique(el) {
	if (el == null || el.value== "" || el.options[el.selectedIndex] == null)
		rubriqueManuelleOn();
	else {
		var elementSelected = el.options[el.selectedIndex];
		document.forms[0].idCompte.value = elementSelected.idCompte;
		document.forms[0].idExterneRubriqueEcran.value = elementSelected.idExterneRubriqueEcran;
		document.forms[0].rubriqueDescription.value = elementSelected.rubriqueDescription;
	}
}

function checkCodeIsoME() {
	if (document.forms[0].codeIsoME.value == "CHF") {
		document.forms[0].coursME.disabled = true;
		document.forms[0].coursME.value = "1.00";
		document.forms[0].montantEcran.disabled = true;
		document.forms[0].montantEcran.value = "";
	} else {
		document.forms[0].coursME.disabled = false;
		document.forms[0].coursME.value = "";
		document.forms[0].montantEcran.disabled = false;
		document.forms[0].montantEcran.value = "";	
	}
}

function initialisationMontantCHF(object, valeur){
	document.forms[0].montantEcran.value="0.00";
	validateFloatNumber(object, valeur);
}

top.document.title = "Comptes - détail d'un paiement étranger - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Saisir un paiement étranger<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <TR> 
            <TD nowrap colspan="2" width="628">
				<span readonly style="text-align : left;"><b>Journal - <% if (viewBean.getJournal() != null) {%><%=viewBean.getJournal().getLibelle()%><% } %></b></span>
            </TD>			
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 
          <TR> 
			<TD width="1" nowrap>&nbsp;</TD>                        
			<TD width="1" nowrap>&nbsp;</TD>                        
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 
          
          
          <TR>			
            <TD nowrap width="128">No AVS </TD>
            
            
            <TD nowrap width="500"> 
			<INPUT type="hidden" name="forIdJournal" value="<%=viewBean.getIdJournal()%>">
			<input type="hidden" name="montant" value="<%=viewBean.getMontant()%>"/>
			<input type="hidden" name="montantME" value="<%=viewBean.getMontantME()%>"/>
			<input type="hidden" name="codeDebitCredit" value="<%=viewBean.getCodeDebitCredit()%>"/>            
			<input type="hidden" name="idOperation" value="<%=viewBean.getIdOperation()%>"/>
			<INPUT type="text" name="noAvs1" size="3" maxlength="3" style="text-align : right;" value="<%=viewBean.getNoAvs1()%>">
			<script language="javascript">
				element = document.getElementById("noAvs1");
				element.onkeypress=function() {validateNoAvs(this);}
				element.onkeyup=function() {tabNoAvs(this);}							 	  	
			</script> 	
			.
			<INPUT type="text" name="noAvs2" size="2" maxlength="2" style="text-align : right;" value="<%=viewBean.getNoAvs2()%>">
			<script language="javascript">
				element = document.getElementById("noAvs2");
				element.onkeypress=function() {validateNoAvs(this);}
				element.onkeyup=function() {tabNoAvs(this);}							 	  	
			</script> 	
			.
			<INPUT type="text" name="noAvs3" size="3" maxlength="3" style="text-align : right;" value="<%=viewBean.getNoAvs3()%>">
			<script language="javascript">
				element = document.getElementById("noAvs3");
				element.onkeypress=function() {validateNoAvs(this);}
				element.onkeyup=function() {tabNoAvs(this);}							 	  	
			</script> 	
			.
			<INPUT type="text" name="noAvs4" size="3" maxlength="3" style="text-align : right;" value="<%=viewBean.getNoAvs4()%>">

          </TD>
            
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 

          <TR> 
            <TD nowrap width="128">Montant</TD>
            <TD nowrap width="500" colspan="2">        
              <INPUT type="text" onchange="initialisationMontantCHF(this, 0)" onkeypress="filterCharForFloat(window.event)" name="montantMEEcran" maxlength="10" style="text-align : right;" class="date" value="<%=globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getMontantMEEcran())?"":viewBean.getMontantMEEcran()%>">&nbsp;
                  <SELECT name="codeDebitCreditEcran" style="width : 2cm;">
	                <% if (viewBean.getCodeDebitCreditEcran().equalsIgnoreCase("D")){%>
	                <OPTION value="D">D&eacute;bit</OPTION>
	                <option value="C" >Cr&eacute;dit</option>
	                <%}else{%>
	                <option value="D" >D&eacute;bit</option>
	                <option value="C" selected>Cr&eacute;dit</option>
	                <%}%>
	              </SELECT>
              
            </TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 
   


          <TR> 
            <TD nowrap width="128">Code ISO</TD>
            <TD nowrap width="500">                                  
              <%	globaz.globall.parameters.FWParametersSystemCode codeIsoME = null;               
			  
			  String selectedCodeIsoME = viewBean.getCodeIsoME();                      
              if (prefillData)
              	selectedCodeIsoME = lastPmtAdded.getCodeIsoME();
            %>
              
              
              <select name="codeIsoME" style="width : 2cm;" onChange="checkCodeIsoME()">
                <%	for (int i=0; i < viewBean.getCsMonnaies().size(); i++) { 
								codeIsoME = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsMonnaies().getEntity(i);
								if (codeIsoME.getCurrentCodeUtilisateur().getCodeUtilisateur().equalsIgnoreCase(selectedCodeIsoME)) { %>
                <option selected value="<%=codeIsoME.getCurrentCodeUtilisateur().getCodeUtilisateur()%>"><%=codeIsoME.getCurrentCodeUtilisateur().getCodeUtilisateur()%></option>
                <%	} else { %>
                <option value="<%=codeIsoME.getCurrentCodeUtilisateur().getCodeUtilisateur()%>"><%=codeIsoME.getCurrentCodeUtilisateur().getCodeUtilisateur()%></option>
                <%	}
				} %>
              </select>
            </TD>                                                                  
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 

          <TR> 
          <%
            String cours = "";
              if (prefillData)
              	cours = lastPmtAdded.getCoursME();
              else
	              cours = viewBean.getCoursME();
            
          %>
            <TD nowrap width="128">Cours </TD>
            <TD nowrap width="500"> 
              <INPUT type="text" onchange="initialisationMontantCHF(this,5)" onkeypress="filterCharForFloat(window.event)" name="coursME" maxlength="10" style="text-align : right;" class="date" value="<%=globaz.globall.util.JAUtil.isDecimalEmpty(cours)?"":cours%>">
            </TD>
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 
     

          <TR> 
            <TD nowrap width="128">Montant CHF</TD>
            <TD nowrap width="500"> 
              <INPUT type="text" onchange="validateFloatNumber(this)" onkeypress="filterCharForFloat(window.event)" name="montantEcran" maxlength="10" style="text-align : right;" class="date" value="<%=globaz.globall.util.JAUtil.isDecimalEmpty(viewBean.getMontantEcran())?"":viewBean.getMontantEcran()%>">
            </TD>
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 


<%
String jspLocation = servletContext + mainServletPath + "Root/rubriqueFinanciere_select.jsp";

CARubrique rubrique = new CARubrique();
rubrique.setSession(objSession);
String idCompte = null;
if (prefillData) {
	rubrique.setIdRubrique(lastPmtAdded.getIdCompte());
	idCompte=lastPmtAdded.getIdCompte();
	}
else {
	rubrique.setIdRubrique(viewBean.getIdCompte());
	idCompte=viewBean.getIdCompte();
	}
rubrique.retrieve();

%>			          
          
          <TR> 
            <TD nowrap width="129">Rubrique</TD>
            <TD width="100%" colspan="4"> 
            <table border="0" width="100%" cellspacing="0" cellpadding="0"><tr>
            	<td>            
            	<ct:FWPopupList name="idExterneRubriqueEcran" onFailure="rubriqueManuelleOn();"
				onChange="updateRubrique(tag.select);"
				value="<%=rubrique.getIdExterne()%>"
				className="libelle" 
				jspName="<%=jspLocation%>"
				minNbrDigit="1" 
				forceSelection="true" 
				validateOnChange="false"
				 />
            	</td>
            	<td align="left" width="60%">
            	  	&nbsp;&nbsp;<input type="text" name="rubriqueDescription" size="30" maxlength="30" class="inputDisabled"  readonly tabindex="-1" value="<%=rubrique.isNew()?"":rubrique.getDescription()%>">
            	</td>
            	<td width="40%">&nbsp;</td>
            	</tr>
            </table>
				<input type="hidden" name="idCompte" value="<%=idCompte%>" >
				<input type="hidden" name="saisieEcran" value="true" tabindex="-1" >
			</TD>                                                          
          </TR>


          
          
                    
     <%String date = "";
       String libelle = "";
     
              if (prefillData) {
              	date= lastPmtAdded.getDate();
              	libelle = lastPmtAdded.getLibelle();
              }
              else {
	              date = viewBean.getDate();
	              libelle = viewBean.getLibelle();
	          }
            %>        

          <TR> 
            <TD nowrap width="128">Date </TD>
            <TD nowrap width="500"> 
				<ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=date%>"/>
            </TD>
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR> 

          <TR> 
            <TD nowrap width="128">Libell&eacute;</TD>
            <TD nowrap width="500"> 
              <INPUT type="text" name="libelle" maxlength="80" class="libelleLong" value="<%=libelle%>">
            </TD>
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR>                                       
          <TR> 
            <TD nowrap colspan="5"> 
              <HR>
            </TD>
          </TR>

          
          <TR>
            <TD nowrap width="128">Etat</TD>
            <TD nowrap width="500"> 
              <input type="text" name="etatJournal" size="30" maxlength="30" value="<%=viewBean.getUcEtat().getLibelle()%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="128">Message</TD>
            <TD nowrap width="500"> 
              <select name="idLog" style="width : 16cm;" class="libelleErrorLongDisabled" tabindex=16 >
                <%if (viewBean.getLog() != null) {%>
                <%Enumeration e = viewBean.getLog().getMessagesToEnumeration();
					while (e.hasMoreElements()){
						FWMessage msg = (FWMessage)e.nextElement();%>
                <%if(msg.getIdLog().equalsIgnoreCase(viewBean.getIdLog())) {%>
                  <option selected  value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
                <%}else {%>
                  <option value="<%=msg.getIdLog()%>"><%=msg.getMessageText()%></option>
                <%}%>
                <% } %>
                <% } %>
              </select>
            </TD>
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR>          
          <TR>
            <TD nowrap width="128">Quittancer</TD>
            <TD width="500">
              <input type="checkbox" name="_quittanceLogEcran" tabindex="15" onClick="quittancer()" disabled>
			  <input type="hidden" name="quittanceLogEcran" value="">
            </TD>
            <TD width="1" nowrap>&nbsp;</TD>
            <TD nowrap width="10">&nbsp;</TD>
          </TR>
          
          
          

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		//mettreTauxBase();
		</SCRIPT> <%	} 
%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
