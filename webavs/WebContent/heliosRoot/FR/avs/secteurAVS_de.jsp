<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.avs.*,globaz.helios.db.interfaces.*, globaz.helios.translation.*" %>
<%
	idEcran="GCF4001";
	CGSecteurAVSViewBean viewBean = (CGSecteurAVSViewBean) session.getAttribute ("viewBean");
	CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean)session.getAttribute (CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);

	selectedIdValue = viewBean.getIdSecteurAVS();
    userActionValue = "helios.avs.secteurAVS.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
function add() {
    document.forms[0].elements('userAction').value="helios.avs.secteurAVS.ajouter"
}
function upd() {
	updateSecteurAVSBilan();
	updateSecteurAVSResultat();
	updateExportComptaSiege();
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.avs.secteurAVS.ajouterSecteur";
    else
        document.forms[0].elements('userAction').value="helios.avs.secteurAVS.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.avs.secteurAVS.afficher";
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="helios.avs.secteurAVS.supprimer";
        document.forms[0].submit();
    }
}


function init(){
}

function postInit() {
	$("#isCreationAutomatique").attr('checked', true);
	updCreationAutomatique();
}

function updateSecteurAVSBilan() {	
	if (document.getElementById('compteBilan').checked) {
		document.getElementById('idSecteurBilan').disabled=true;
	}
	else {
		document.getElementById('idSecteurBilan').disabled=false;	
	}
}


function updateSecteurAVSResultat() {	
	if (document.getElementById('compteResultat').checked) {
		document.getElementById('idSecteurResultat').disabled=true;
	}
	else {
		document.getElementById('idSecteurResultat').disabled=false;	
	}
}
function updateExportComptaSiege() {	
	if (document.getElementById('exportationComptabiliteAuSiege').checked) {
		document.getElementById('ccAgenceSiege').disabled=false;
	}
	else {
		var empty = "";
		document.getElementById('ccAgenceSiege').value=empty;
		document.getElementById('ccAgenceSiege').disabled=true;	
	}
}

function updCreationAutomatique() {
	if (document.getElementById('isCreationAutomatique').checked) {
// 		document.getElementById('libelleFr').disabled=true;
// 		document.getElementById('libelleDe').disabled=true;
// 		document.getElementById('libelleIt').disabled=true;		
		document.getElementById('idSecteurBilan').disabled=true;
		document.getElementById('idSecteurResultat').disabled=true;
		document.getElementById('tauxVentilation').disabled=true;
	}
	else {
		document.getElementById('libelleFr').disabled=false;
		document.getElementById('libelleDe').disabled=false;
		document.getElementById('libelleIt').disabled=false;
		document.getElementById('clotureManuelle').disabled=false;
		document.getElementById('compteBilan').disabled=false;
		document.getElementById('idSecteurBilan').disabled=false;
		document.getElementById('compteExploitation').disabled=false;
		document.getElementById('compteAdministration').disabled=false;
		document.getElementById('compteInvestissement').disabled=false;
		document.getElementById('compteResultat').disabled=false;
		document.getElementById('idSecteurResultat').disabled=false;
		document.getElementById('tauxVentilation').disabled=false;	
	}
}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Aperçu des secteurs AVS<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
        
        <tr> 
          <td colspan="2">Numéro</td>
          <td colspan="2"><input type="text"  name="idSecteurAVS" <%=((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")))?"class='libelleLong'":"readonly class='libelleLongDisabled'"%> value="<%=viewBean.getIdSecteurAVS()==null?"":viewBean.getIdSecteurAVS()%>"> *
          </td>
        </tr>

        <tr> 
          <td colspan="2">Numéro du mandat</td>
          <td colspan="2"><input type="text" name="idMandat"  readonly class="libelleLongDisabled" value="<%=viewBean.getIdMandat()==null?"":viewBean.getIdMandat()%>"> *
          <input type="hidden" name="forIdMandat" value="<%=viewBean.getIdMandat()%>"/>
          </td>
        </tr>		

		<tr><td colspan="4">&nbsp;</td></tr>
<%if ("ADD".equalsIgnoreCase(request.getParameter("_method"))) {%>
	     <tr><td colspan="4">&nbsp;</td></tr>
        <tr> 
          <td colspan="4"><b><i>Création automatique</i></b>&nbsp;&nbsp;<input type="checkbox" name="isCreationAutomatique" id="isCreationAutomatique" onclick="updCreationAutomatique();" ></td>          
        </tr>
        <tr><td colspan="4">&nbsp;</td></tr>
<%}%>


        <tr> 
          <td>Libellé</td>
          <td>Fr</td>
          <td colspan="2"><input type="text" class="libelleLong" name="libelleFr" maxlength="40" value="<%=viewBean.getLibelleFr()%>"> *</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>De</td>
          <td colspan="2"><input type="text" class="libelleLong" name="libelleDe" maxlength="40" value="<%=viewBean.getLibelleDe()%>"></td>
         </tr>
        <tr>
          <td>&nbsp;</td>
          <td>It</td>
          <td colspan="2"><input type="text" class="libelleLong" name="libelleIt" maxlength="40" value="<%=viewBean.getLibelleIt()%>"></td>
        </tr>
        <tr> 
          <td colspan="2">Type de tâche</td>
          <td colspan="2"> 
            <ct:FWCodeSelectTag name="idTypeTache" defaut="<%=viewBean.getIdTypeTache()%>" codeType="CGSECAVS"/> 
			<script>		
				document.getElementById("idTypeTache").style.width = "4cm";
			</script>
		  *</td>
        </tr>
        <tr> 
          <td colspan="2">Clôture manuelle</td>
          <td colspan="2"><input type="checkbox" name="clotureManuelle" <%=(viewBean.isClotureManuelle().booleanValue())?"checked":""%>></td>
        </tr>
        <tr> 
          <td colspan="2">Compte de bilan</td>          
          <td> 
            <input type="checkbox" name="compteBilan" <%=(viewBean.isCompteBilan().booleanValue())?"checked":""%> onclick="updateSecteurAVSBilan();"> ou autre secteur 
          </td>
          <td> 
          <%
          String defaultValue = (viewBean.getIdSecteurBilan()==null||"0".equals(viewBean.getIdSecteurBilan()))?"0":viewBean.getIdSecteurBilan();
          java.util.Vector v = globaz.helios.translation.CGListes.getSecteurAVSBilanListe(session, viewBean.getIdMandat(), CGSecteurAVS.CS_TACHE_FEDERAL, " ");                   
          %>
	          <ct:FWListSelectTag name="idSecteurBilan" defaut="<%=defaultValue%>" data="<%=v%>"/>
     			<script>		
					document.getElementById("idSecteurBilan").style.width = "6cm";
				</script>

          </td>
        </tr>
        <tr> 
          <td colspan="2">Compte d'exploitation</td>
          <td colspan="2"><input type="checkbox" name="compteExploitation" <%=(viewBean.isCompteExploitation().booleanValue())?"checked":""%>></td>
        </tr>
        <tr> 
          <td colspan="2">Compte d'administration</td>
          <td colspan="2"><input type="checkbox" name="compteAdministration" <%=(viewBean.isCompteAdministration().booleanValue())?"checked":""%>></td>
        </tr>
        <tr> 
          <td colspan="2">Compte d'investissement</td>
          <td colspan="2"><input type="checkbox" name="compteInvestissement" <%=(viewBean.isCompteInvestissement().booleanValue())?"checked":""%>></td>
        </tr>
        <tr> 
          <td colspan="2">Compte de résultat</td>
          <td>
          	<input type="checkbox" name="compteResultat" <%=(viewBean.isCompteResultat().booleanValue())?"checked":""%> onclick="updateSecteurAVSResultat();"> ou autre secteur 
          </td>          
          <td> 
	          <%
	          String defaultValue2 = (viewBean.getIdSecteurResultat()==null||"0".equals(viewBean.getIdSecteurResultat()))?"0":viewBean.getIdSecteurResultat();
	          java.util.Vector v2 = globaz.helios.translation.CGListes.getSecteurAVSResultatListe(session, viewBean.getIdMandat(), CGSecteurAVS.CS_TACHE_FEDERAL, " ");                    
	          %>
          
	          <ct:FWListSelectTag name="idSecteurResultat" defaut="<%=defaultValue2%>" data="<%=v2%>"/>
     			<script>		
					document.getElementById("idSecteurResultat").style.width = "6cm";
				</script>
          </td>
        </tr>
        <tr>
          <td colspan="2">Taux de ventilation du compte 1102</td>
          <td colspan="2"><input type="text" name="tauxVentilation" size="10" maxlength="10" value="<%=viewBean.getTauxVentilation()%>"></td>
        </tr>
         <tr>
          <td colspan="2">Exportation de la comptabilité au siège de la caisse</td>
          <td colspan="2"><input type="checkbox" name="exportationComptabiliteAuSiege" <%=(viewBean.isExportationComptabiliteAuSiege().booleanValue())?"checked":""%> onclick="updateExportComptaSiege();"></td>
        </tr>
        <tr>
          <td colspan="2">Numéro du c/c de l'agence dans la comptabilité du siège</td>
          <td colspan="2"><input type="text" name="ccAgenceSiege" value="<%=viewBean.getCcAgenceSiege()%>"></td>
        </tr>        
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>