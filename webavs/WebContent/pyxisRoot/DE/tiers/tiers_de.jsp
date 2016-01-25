<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
 <%@page import="globaz.jade.client.util.JadeStringUtil"%>
<script type="text/javascript"
      src="<%=servletContext%>/scripts/nss.js"></script>

<%@ page import="globaz.pyxis.db.adressecourrier.*"%>

<%
		idEcran ="GTI0002";
		globaz.pyxis.db.tiers.TITiersViewBean viewBean = (globaz.pyxis.db.tiers.TITiersViewBean)session.getAttribute ("viewBean");
		selectedIdValue = viewBean.getIdTiers();
		key = "globaz.pyxis.db.tiers.TITiersViewBean-"+viewBean.getIdTiers();
		bButtonNew = objSession.hasRight(userActionNew, "ADD");

%>
<!-- Creer l'enregitrement s'il n'existe pas -->

<SCRIPT language="JavaScript">

</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<style>
	.navLinkTiers {
	cursor:hand;
	border : 0 0 0 0;
	color:blue;
	text-decoration:underline;
	background : #B3C4DB;
	
	margin : 0 0 0 0;
	padding : 0 0 0 0;
	width : 100%;
	font-weight:normal;
	font-size: 8pt
	
	}
</style>
		<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/pyxisRoot/ajax.js"></SCRIPT>
	

<SCRIPT language="JavaScript">



top.document.title = "Tiers - Tiers Détail"
var FenAvs;
var FenAff;
var FenCon;


function HistoriqueDesignation1() {
FenAvs=window.open('pyxis?userAction=pyxis.tiers.historiqueTiers.lister&forIdTiers=<%=viewBean.getIdTiers()%>&forChamp=1','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}
function HistoriqueDesignation2() {
FenAvs=window.open('pyxis?userAction=pyxis.tiers.historiqueTiers.lister&forIdTiers=<%=viewBean.getIdTiers()%>&forChamp=2','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}
function HistoriqueDesignation3() {
FenAvs=window.open('pyxis?userAction=pyxis.tiers.historiqueTiers.lister&forIdTiers=<%=viewBean.getIdTiers()%>&forChamp=3','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}
function HistoriqueDesignation4() {
FenAvs=window.open('pyxis?userAction=pyxis.tiers.historiqueTiers.lister&forIdTiers=<%=viewBean.getIdTiers()%>&forChamp=4','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}
function HistoriqueTitre() {
FenAvs=window.open('pyxis?userAction=pyxis.tiers.historiqueTiers.lister&forIdTiers=<%=viewBean.getIdTiers()%>&forChamp=5','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}
function HistoriqueNationalite() {
FenAvs=window.open('pyxis?userAction=pyxis.tiers.historiqueTiers.lister&forIdTiers=<%=viewBean.getIdTiers()%>&forChamp=6','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}



function HistoriqueNumAvs() {
FenAvs=window.open('pyxis?userAction=pyxis.tiers.historiqueAvs.lister&forIdTiers=<%=viewBean.getIdTiers()%>','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}
function HistoriqueNumAffilie() {
FenAff=window.open('pyxis?userAction=pyxis.tiers.historiqueAffilie.lister&forIdTiers=<%=viewBean.getIdTiers()%>','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}
function HistoriqueNumContribuable() {
FenCon=window.open('pyxis?userAction=pyxis.tiers.historiqueContribuable.lister&forIdTiers=<%=viewBean.getIdTiers()%>','MaFenetre','toolbar=no,location=no,personalbar=no,status=no,menubar=no,titlebar=no,resizable=yes,scrollbars=yes,alwaisRaised=yes,dependent=yes,Width=700,Height=100,Top=250,Left=200');
}

function FermetureFenetre() {
if ((FenAvs)&&(!FenAvs.closed)) {
FenAvs.close();
}
if ((FenAff)&&(!FenAff.closed)) {
FenAff.close();
}
if ((FenCon)&&(!FenCon.closed)) {
FenCon.close();
}
}




<!--hide this script from non-javascript-enabled browsers





function avsAction(elem) {
    var dateN = document.all("dateNaissance");
    var eSexe = document.all("sexe");
    var ePays = document.all("idPays");

    var state = fieldFormat(elem,'NUMERO_AVS');
    var avsp = new AvsParser(elem.value);
    var codeSuisse = "514002";


    if (avsp.isWellFormed()) {


        var avsSexe = avsp.getSexe();
        var code = "";
        if (avsSexe =="M") {
            code = "516001"
        } else if (avsSexe ="F") {
            code = "516002"
        }


        if (state == true){  // numero avs valide
            
            // remplissage automatique des champs vide en fonction 
            // du numero avs


            // --------------------------------------------------------
            // date de naissance
            // --------------------------------------------------------



            if (dateN.value=="") {
                    if (isDate(avsp.getBirthDate(),"dd.MM.yyyy") == true){
                      dateN.innerText=avsp.getBirthDate();
                      validateElement(dateN,false);
                    }
            }
            // --------------------------------------------------------
            // sexe
            // --------------------------------------------------------
            
            var isEmpty = true;
            for (i=0;i<eSexe.length;i++) {  // regarde si on a deja une valeur checked
                if (eSexe[i].checked== true) {
                    isEmpty = false;
                    break;
                }
            }
 


            if (isEmpty == true) {
                for (i=0;i<eSexe.length;i++) {
                    if (eSexe[i].value == code) {
                         
                        // si on a aucune valeur checked, on en check une en
                        // fonction du numero avs   
                        eSexe[i].checked = true;
                    	    break;
                    }
                 }
            }
            // --------------------------------------------------------

            // pays
            // --------------------------------------------------------
            if (avsp.isSwiss()) {
                                isEmpty = true;

                for (i=0;i<ePays.length;i++) {  // regarde si on a deja une valeur checked
                    if (ePays[i].selected== true) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty == true) {
                    for (i=0;i<ePays.length;i++) {
                        if (ePays[i].value == codeSuisse) {
                         
                            ePays[i].selected = true;
                            break;
                        }
                     }
                }
            }
    
        
        } // fin du remplissage automatique des champs vide en fonction du numero avs       



    }
	
}

function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.tiers.controler"
}
function upd() {
}

function checkModification() {

    var result = true;
	
	
	
	    // avs
    var cspAvs = new CodeSystemPopup();
    cspAvs.setMotifElement("motifModifAvs");
    cspAvs.setDateElement("dateModifAvs");
    cspAvs.setElementToCheck("oldNumAvs","numAvsActuel");
    cspAvs.setLibelle("Wählen Sie den Grund der Änderung des Felds 'SVN' und das Inkrafttreten");
    if (cspAvs.hasChanged()) {
      result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspAvs,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
    }
    if (result) {
    	var cspContribuable = new CodeSystemPopup();
	cspContribuable.setMotifElement("motifModifContribuable");
    	cspContribuable.setDateElement("dateModifContribuable");
    	cspContribuable.setElementToCheck("oldNumContribuable","numContribuableActuel");
    	cspContribuable.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Steuerpflichtiger-Nr.' und das Inkrafttreten");
    	if (cspContribuable.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspContribuable,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // titre
    if (result) {
    	var cspTitre = new CodeSystemPopup();
	cspTitre .setMotifElement("motifModifTitre");
    	cspTitre .setDateElement("dateModifTitre");
    	cspTitre .setElementToCheck("oldTitre","titreTiers");
    	cspTitre .setLibelle("Wählen Sie den Grund der Änderung des Felds 'Anrede' und das Inkrafttreten");
    	if (cspTitre.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspTitre ,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // Designation1
    if (result) {
    	var cspNom = new CodeSystemPopup();
	cspNom.setMotifElement("motifModifDesignation1");
    	cspNom.setDateElement("dateModifDesignation1");
    	cspNom.setElementToCheck("oldDesignation1","designation1");
    	cspNom.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Name' und das Inkrafttreten");
    	if (cspNom.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspNom ,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // Designation2
    if (result) {
    	var cspDesi2 = new CodeSystemPopup();
	cspDesi2.setMotifElement("motifModifDesignation2");
    	cspDesi2.setDateElement("dateModifDesignation2");
    	cspDesi2.setElementToCheck("oldDesignation2","designation2");
    	cspDesi2.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Vorname' und das Inkrafttreten");
    	if (cspDesi2.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspDesi2,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // Designation3
    if (result) {
    	var cspDesi3= new CodeSystemPopup();
	cspDesi3.setMotifElement("motifModifDesignation3");
    	cspDesi3.setDateElement("dateModifDesignation3");
    	cspDesi3.setElementToCheck("oldDesignation3","designation3");
    	cspDesi3.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Name (weiter) 1' und das Inkrafttreten");
    	if (cspDesi3.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspDesi3,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // Designation4
    if (result) {
    	var cspDesi4= new CodeSystemPopup();
	cspDesi4.setMotifElement("motifModifDesignation4");
    	cspDesi4.setDateElement("dateModifDesignation4");
    	cspDesi4.setElementToCheck("oldDesignation4","designation4");
    	cspDesi4.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Name (weiter)' 2 und das Inkrafttreten");
    	if (cspDesi4.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspDesi4,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // Nationalite
    if (result) {
    	var cspNationalite= new CodeSystemPopup();
		cspNationalite.setMotifElement("motifModifPays");
    	cspNationalite.setDateElement("dateModifPays");
    	cspNationalite.setElementToCheck("oldPays","idPays");
    	cspNationalite.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Nationalität' und das Inkrafttreten");
    	if (cspNationalite.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspNationalite,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

	
	
    return result;
}
function validate() {

	var state = true;
	//validateFields();
	if (state) {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="pyxis.tiers.tiers.controler";
			

		} else {
			document.forms[0].elements('userAction').value="pyxis.tiers.tiers.modifier";
			state = checkModification();
		}
	}

	
	return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].elements('userAction').value="back";
 } else {
  document.forms[0].elements('userAction').value="pyxis.tiers.tiers.afficher";
 }
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.tiers.supprimer";
		document.forms[0].submit();
	}
}


function init(){
	
}

function postInit() {
try {
		
		
		elem = document.getElementById("extSelectorId");
		elem.active = true;
		elem.disabled = false;	
		elem.selectedIdndex=0;	
	}catch(e) {
	
	}
	var navLinks = document.getElementsByName("navLink");
	
	for (i=0;i<navLinks.length;i++) {
			navLinks[i].disabled = '';
	}
}



/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<%if(!JadeStringUtil.isIntegerEmpty(viewBean.getIdTiers())) { %>
			<span class="postItIcon"><ct:FWNote sourceId="<%=viewBean.getIdTiers()%>" tableSource="globaz.pyxis.db.tiers.TITiersViewBean"/></span>
			<%} %>
			<span style="font-family:webdings;font-weight:normal">€</span> - Detail eines Partners <%=viewBean.getDecorations()%><%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

<tr><td>
<div id="zoneAutoComplete" style="position:absolute;display:none;"></div>
<ct:FWPanelDHTMLTag/>
<table name="main" border=0  cellpadding=0 cellspacing=0>
		<jsp:include page="extensions/default_tiers_zone1.jsp" flush="false" />
		<jsp:include page="extensions/default_tiers_zone2.jsp" flush="false" />
		<jsp:include page="extensions/default_tiers_zone3.jsp" flush="false" />
</table>

</td></tr>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} %>
		
		<ct:menuChange displayId="options" menuId="tiers-detail" showTab="options" >
			<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"  />
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdTiers()%>"  />
			
		</ct:menuChange>
		
		
 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>