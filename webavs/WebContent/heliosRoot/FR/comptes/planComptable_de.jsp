
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*" %>

<style type="text/css">
<!--
.txtLink {font-family: Verdana, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; text-decoration: none; margin-left: 0px}
-->
</style>

<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="GCF0018";
    CGPlanComptableViewBean viewBean = (CGPlanComptableViewBean)session.getAttribute ("viewBean");
    CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean)session.getAttribute (CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
	selectedIdValue = viewBean.getIdCompte();
    userActionValue = "helios.comptes.planComptable.modifier";

	String analyseBudgetaireUserAction = "helios.comptes.analyseBudgetaire.afficherAnalyseBudgetaire";
	String addLink = "helios.classifications.liaisonCompteClasse.ajouterLink";
	String removeLink = "helios.classifications.liaisonCompteClasse.supprimerLink";

	String aucun = "Aucun";
	if (languePage.equalsIgnoreCase("DE"))
		aucun = "Kein";


%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers


function add() {
    document.forms[0].elements('userAction').value="helios.comptes.planComptable.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.planComptable.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.comptes.planComptable.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.comptes.planComptable.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.comptes.planComptable.supprimer";
        document.forms[0].submit();
    }
}


function init(){
	<%
	if (exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {
		if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) { %>
			if ((document.getElementById('idExterne').value != null) && (document.getElementById('idExterne').value != "")) {
				document.getElementById('idExterne1').value = document.getElementById('idExterne').value.substring(0,4);
				document.getElementById('idExterne2').value = document.getElementById('idExterne').value.substring(5,9);
				document.getElementById('idExterne3').value = document.getElementById('idExterne').value.substring(10,14);
			}
	<% 	} else { %>
			if ((document.getElementById('idExterne').value != null) && (document.getElementById('idExterne').value != "")) {
				document.getElementById('idExterne1').value = document.getElementById('idExterne').value.substring(3,4);
				document.getElementById('idExterne3').value = document.getElementById('idExterne').value.substring(10,14);
			}
	<% 	}
	}%>
}


function onChangeMonnaieEtrangere() {
	if (document.getElementById("idNature").value!=<%=CGCompte.CS_MONNAIE_ETRANGERE%>) {
		alert(" La nature du compte doit être de type monnaie étrangère. ");
		document.getElementById("codeISOMonnaie").selectedIndex=0;
	}
}

function onChangeNature() {

<%if (!exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {%>
	if (document.getElementById("idNature").value==<%=CGCompte.CS_CENTRE_CHARGE%>) {
		document.getElementById('defaultIdCentreCharge').disabled=false;
	}
	else {
		document.getElementById('defaultIdCentreCharge').disabled=true;
	}
<%}%>

	if (document.getElementById("idNature").value!=<%=CGCompte.CS_MONNAIE_ETRANGERE%>) {
			document.getElementById("codeISOMonnaie").selectedIndex=0;
	}
}

function callAnalyseBudgetaire() {
	document.forms[0].elements('userAction').value="<%=analyseBudgetaireUserAction%>";
	document.forms[0].submit();

}

function addLink() {
	document.forms[0].elements('userAction').value="<%=addLink%>";
	document.forms[0].submit();
}

function removeLink() {
	document.forms[0].elements('userAction').value="<%=removeLink%>";
	document.forms[0].submit();

}


function editClassification() {
	document.forms[0].elements('idClassification').disabled=false;
	document.forms[0].elements('currentClassesComptes').disabled=false;
	document.forms[0].elements('newClassesComptes').disabled=false;

}

/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un compte<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr><td>
	<table border="0">

<tr>
            <TD width="184">Numéro du compte
            <input type="hidden" name="idMandat" value="<%=viewBean.getIdMandat()%>">
            <input type="hidden" name="idExerciceComptable" value="<%=viewBean.getIdExerciceComptable()%>">
            </TD>
            <%
            	if (exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {
            		if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {
            %>
            <TD width="508"> <input type="hidden" name='idExterne' value='<%=viewBean.getIdExterne()%>'>
            	<input onkeypress="return filterCharForPositivInteger(window.event);" maxlength="4" size="4" name="idExterne1" onChange="document.getElementById('idExterne').value=document.getElementById('idExterne1').value+'.'+document.getElementById('idExterne2').value+'.'+document.getElementById('idExterne3').value">.<input onkeypress="return filterCharForPositivInteger(window.event);" maxlength="4" size="4" name="idExterne2" onChange="document.getElementById('idExterne').value=document.getElementById('idExterne1').value+'.'+document.getElementById('idExterne2').value+'.'+document.getElementById('idExterne3').value">.<input onkeypress="return filterCharForPositivInteger(window.event);" maxlength="4" size="4" name="idExterne3" onChange="document.getElementById('idExterne').value=document.getElementById('idExterne1').value+'.'+document.getElementById('idExterne2').value+'.'+document.getElementById('idExterne3').value"> * </TD>
            <%
            		} else {
            %>
				<TD width="508">
					<input type="hidden" name='idExterne' value='<%=viewBean.getIdExterne()%>'>
					<%=viewBean.getIdExterne()%> *
				</TD>
            <%
            		}
            	} else {
		            if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) { %>
			            <TD width="508"> <input type="text" name='idExterne' class='libelleLong'> * </TD>
					<%} else {%>
	    	        	<TD width="508"> <input type="text" name='idExterne' class='libelleLongDisabled' readonly value='<%=viewBean.getIdExterne()%>'> * </TD>
    				<%}%>
             <% } %>
          </tr>

<tr>
            <TD width="184">Libelle Fr</TD>
            <TD width="508"> <input name="libelleFr" class="libelleLong" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelleFr(),"\"","&quot;")%>"\>
<%=CGLibelle.getLibelleMandatory((CGLibelleInterface)viewBean,"FR")%></TD>
          </tr>

<tr>
            <TD width="184">Libelle De</TD>
            <TD width="508"> <input name="libelleDe" class="libelleLong" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelleDe(),"\"","&quot;")%>"\>
<%=CGLibelle.getLibelleMandatory((CGLibelleInterface)viewBean,"DE")%></TD>
          </tr>

<tr>
            <TD width="184">Libelle It
            </TD>
            <TD width="508"> <input name="libelleIt" class="libelleLong" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibelleIt(),"\"","&quot;")%>"\>
<%=CGLibelle.getLibelleMandatory((CGLibelleInterface)viewBean,"IT")%></TD>
          </tr>

<tr>
            <TD width="184">Nature</TD>
<%
            if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {
%>
            <TD width="508"> <ct:FWCodeSelectTag name="idNature" defaut="<%=viewBean.getIdNature()%>" codeType="CGNATCPT" /> * </TD>
   					<script>
						document.getElementById("idNature").onchange=function() {onChangeNature()};
					</script>
<%} else {%>
				<TD><input name='Nature' class='disabled' readonly value='<%=globaz.helios.translation.CodeSystem.getLibelle(session,viewBean.getIdNature())%>'>
					<input name='idNature' type='hidden' value='<%=viewBean.getIdNature()%>'>
				</TD>

<%}%>

          </tr>

<tr>
            <TD width="184">Domaine du compte</TD>
            <TD width="508">
<% if( exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() ){ %>
	<input name="Domaine" readonly class="disabled" value="<%=globaz.helios.translation.CodeSystem.getLibelle(session,viewBean.getIdDomaine())%>" >
	<input name="idDomaine" type='hidden' value="<%=viewBean.getIdDomaine()%>" >
<%} else { %>
<ct:FWCodeSelectTag name="idDomaine" defaut="<%=viewBean.getIdDomaine()%>" codeType="CGDOMCPT" except="<%=viewBean.getExceptListDomaine()%>"/> *
<%}%>
</TD>
          </tr>

<tr>
            <TD width="184">Genre du compte</TD>
            <TD width="508">
<% if( exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() ){ %>
<input type="idGenre" readonly class="disabled" value="<%=globaz.helios.translation.CodeSystem.getLibelle(session,viewBean.getIdGenre())%>" >
<%}%>
<% if (!exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() ){ %>
<ct:FWCodeSelectTag name="idGenre" defaut="<%=viewBean.getIdGenre()%>" codeType="CGGENCPT" except="<%=viewBean.getExceptListGenre()%>"/> *
<%}%>
</TD>
          </tr>

<tr>
            <TD width="184">Monnaie </TD>
            <TD width="508">

<%
            if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {
%>
				<SELECT id='codeISOMonnaie' name='codeISOMonnaie' doClientValidation='' onchange='onChangeMonnaieEtrangere();'>
<%
		  	globaz.globall.parameters.FWParametersSystemCodeManager mgr = new globaz.globall.parameters.FWParametersSystemCodeManager();
			mgr.setForIdGroupe("PYMONNAIE");
			mgr.setSession(objSession);
			mgr.find(globaz.globall.db.BManager.SIZE_NOLIMIT);
			for (int i = 0; i < mgr.size(); i++) {
				globaz.globall.parameters.FWParametersSystemCode entity = (globaz.globall.parameters.FWParametersSystemCode)mgr.getEntity(i);
				globaz.globall.parameters.FWParametersUserCode  userCode = entity.getCodeUtilisateur(objSession.getIdLangue());
				%>
					<OPTION value='<%=userCode.getCodeUtilisateur()%>'><%=userCode.getCodeUtilisateur()%> - <%=userCode.getLibelle()%></OPTION>
				<%
			}
%>
            </SELECT> *
<%} else {%>
			<input name='codeISOMonnaie' class='disabled' readonly value='<%=viewBean.getCodeISOMonnaie()%>'>
<%}%>
            </TD>
          </tr>

<% if( exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() ){ %>
<tr>
            <TD width="184">Secteur AVS</TD>
            <TD width="508">
<input name='idSecteurAVS' class='disabled' readonly value='<%=viewBean.getIdSecteurAVS()%>'>
</TD>
          </tr>
<%}%>

<% if( exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue() ){ %>
<tr>
            <TD width="184">Compte AVS</TD>
            <TD width="508">
<input name='numeroCompteAVS' class='disabled' readonly value='<%=viewBean.getNumeroCompteAVS()%>'>
</TD>
          </tr>
<%}%>
		<tr>
            <TD width="184">Compte TVA</TD>
            <TD width="508"> <input name='idCompteTVA' class='libelleLong' value='<%=viewBean.getIdCompteTVA()%>'> </TD>
          </tr>



<tr>
            <TD width="184">Compte verrouillé</TD>
            <TD width="508"> <input type="checkbox" name="estVerrouille" <%=(viewBean.isEstVerrouille().booleanValue())?"CHECKED":""%>> </TD>
          </tr>


<tr>
            <TD width="184">Verrouiller écritures manuelles</TD>
            <TD width="508"> <input type="checkbox" name="ecritureManuelleEstVerrouille" <%=(viewBean.isEcritureManuelleEstVerrouille().booleanValue())?"CHECKED":""%>> </TD>
          </tr>

<tr>
            <TD width="184">A rouvir l'exercice suivant</TD>

<%if ((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add"))) {%>
            <TD width="508"> <input type="checkbox" name="aReouvrir" CHECKED> </TD>
<%} else {%>
            <TD width="508"> <input type="checkbox" name="aReouvrir" <%=(viewBean.isAReouvrir().booleanValue())?"CHECKED":""%>> </TD>
<%

}%>
          </tr>

<tr>
            <TD width="184">Remarque</TD>
            <TD width="508"> <input type="hidden" name="idRemarque" value="<%=viewBean.getIdRemarque()%>"><textarea name='remarque' class='libelleLong' rows='5' width='250' align='left'><%=viewBean.getRemarque()%></textarea> </TD>
</tr>


<%if (!exerciceComptable.getMandat().isEstComptabiliteAVS().booleanValue()) {%>
<tr><TD width="184">Centre de charge </TD>
	<TD width="508">
		<ct:FWListSelectTag name="defaultIdCentreCharge" defaut="<%=viewBean.getDefaultIdCentreCharge()%>" data="<%=globaz.helios.translation.CGListes.getCentreChargeListe(aucun, session, exerciceComptable.getIdMandat())%>"/>
	</TD>
<%}%>



<%if (CGCompte.CS_GENRE_CHARGE.equals(viewBean.getIdGenre()) || CGCompte.CS_GENRE_PRODUIT.equals(viewBean.getIdGenre())) {

	globaz.framework.util.FWCurrency montantAnalyseBudgetaire = new globaz.framework.util.FWCurrency(viewBean.getBudget());
%>
	<tr>
	            <TD width="184">Budget</TD>
	            <TD width="508"> <input name='budgetAnnuel' class='disabled' readonly value="<%=globaz.globall.util.JANumberFormatter.fmt(montantAnalyseBudgetaire.toString(),true,true,false,2)%>">
	            &nbsp;<a href="javascript:callAnalyseBudgetaire();" class="txtLink">Analyse budgétaire</a></TD>
	</tr>
<%}%>
</table>
</td>

<%

if ((request.getParameter("_method")==null)||(!request.getParameter("_method").equals("add"))) {

  if (!CGMandat.CS_PC_AVS.equals(viewBean.getExerciceComptable().getMandat().getIdTypePlanComptable()) &&
	 !CGMandat.CS_PC_USAM.equals(viewBean.getExerciceComptable().getMandat().getIdTypePlanComptable())) {
%>

<td valign="top"><table>
		<tr>
            <TD align="left" valign="top">
            	<table>
            	<tr><td colspan="2">Classes compte</td></tr>
            	<tr><td>
		              <select name="currentClassesComptes" size="5" readonly  disabled class="libelleLongDisabled" multiple>
						<%
						String idClasseCompte = "";
						if (!globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getIdCompte())) {
							globaz.helios.db.classifications.CGLiaisonCompteClasseManager mgr = new globaz.helios.db.classifications.CGLiaisonCompteClasseManager();
							mgr.setSession(viewBean.getSession());
							mgr.setForIdCompte(viewBean.getIdCompte());
							mgr.find();
							for (int i=0; i < mgr.size(); i++) {
								globaz.helios.db.classifications.CGLiaisonCompteClasse entity = (globaz.helios.db.classifications.CGLiaisonCompteClasse)mgr.getEntity(i);
								globaz.helios.db.classifications.CGClasseCompte classeCompte = new globaz.helios.db.classifications.CGClasseCompte();
								classeCompte.setSession(viewBean.getSession());
								classeCompte.setIdClasseCompte(entity.getIdClasseCompte());
								classeCompte.retrieve();
								idClasseCompte = classeCompte.getIdClasseCompte();
							%>
								<option value="<%=classeCompte.getIdClasseCompte()%>"><%=classeCompte.getNoClasse() + " " + classeCompte.getLibelle()%></option>
						<%	}
						}
						if (globaz.jade.client.util.JadeStringUtil.isBlank(idClasseCompte)) {
							idClasseCompte = "0";
						}
						%>
		              </select>
	             </td>
	             <td align="left">
	             	<ct:ifhasright element="<%=removeLink%>" crud="d">
	             		<a href="javascript:removeLink()" class="txtLink">&nbsp;supprimer liaisons</a>
	             	</ct:ifhasright>
	             </td>
	        </tr>
			<tr><td colspan="2">&nbsp;</td></tr>

        	<tr><td>
	              <select name="newClassesComptes" size="5" readonly  disabled class="libelleLongDisabled" multiple>
	              </select>
             </td>
             <td align="left">
             	<ct:ifhasright element="<%=addLink%>" crud="c">
             		<a href="javascript:addLink()" class="txtLink">&nbsp;ajouter liaisons</a>
             	</ct:ifhasright>
             </td>
        </tr>
		</table>
	</td></tr>

	<tr><td>&nbsp;</td></tr>

	<tr>
	<%
		String idClassificationPrincipale = "";
		CGMandat mandat = exerciceComptable.getMandat();
		if (mandat!=null)
			idClassificationPrincipale = mandat.getIdClassificationPrincipale();
	%>
	    <td align="left" valign="top">Classification&nbsp;
				 <ct:FWListSelectTag name="idClassification" defaut="<%=idClassificationPrincipale%>"
					 data="<%=globaz.helios.translation.CGListes.getClassificationListe(session, exerciceComptable.getIdMandat(), null)%>"/>
				 <ct:ifhasright element="helios.classifications.liaisonCompteClasse" crud="u">
					<a href="javascript:editClassification()" class="txtLink">editer</a>
				 </ct:ifhasright>
				<script>
					function updateIFrame() {
						document.all['classeCompteIFrame'].src = "<%=servletContext%><%=((String)request.getAttribute("mainServletPath")+"Root")%>/<%=globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)%>/comptes/classeCompte_extendedSelectionPlanComptable.jsp?idClasseCompte=<%=idClasseCompte%>&idClassification="+document.all['idClassification'].value;
					}
					document.all['idClassification'].onchange = updateIFrame;
				</script>
				<input type="hidden" name="classesCompteList" value="">
		</td>
	</tr>
	<tr>
		<td align="left">
			<iframe id="classeCompteIFrame" width=400px height=200px></iframe>
		</td>
	</tr>
	<script>updateIFrame();</script>
</table></td>
<%}
}%>

</tr>

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

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>