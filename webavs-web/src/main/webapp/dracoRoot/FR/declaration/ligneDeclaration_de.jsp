
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.draco.db.declaration.*"%>
<%@ page import="globaz.draco.translation.*"%>
<%@ page import="globaz.globall.util.*"%>
<%@ page import="java.math.*"%>
<%
	idEcran = "CDS0008";

	DSLigneDeclarationViewBean viewBean = (DSLigneDeclarationViewBean)session.getAttribute ("viewBean");

	boolean updateCalled = false;
	String isReadonlyCotisationDue = "readonly";
    String isReadonlyAutres = "";
    String classCotisationDue = "montantDisabled";
    String classAutres = "montant";
    String classTaux = "libelle";
	if(viewBean.isAssuranceTypeCrpBasic()){
        isReadonlyCotisationDue = "";
        isReadonlyAutres = "readonly";
        classCotisationDue = "montant";
        classAutres = "montantDisabled";
        classTaux = "montantDisabled";
    }

	selectedIdValue = viewBean.getIdLigneDeclaration();
	userActionValue = "draco.declaration.ligneDeclaration.afficher";
	bButtonValidate = objSession.hasRight("draco.declaration.ligneDeclaration.afficher","ADD");
	bButtonCancel = objSession.hasRight("draco.declaration.ligneDeclaration.afficher","ADD");
%>
<%@page import="globaz.naos.translation.CodeSystem"%>
<%@page import="globaz.naos.db.tauxAssurance.AFTauxAssurance"%>
<%@ page import="ch.globaz.eavs.utils.StringUtils" %>
<SCRIPT language="JavaScript">
    console.log("<%=request.getParameter("_method")%>");
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers


$(function (){
    $("#assuranceId").change(function(){
        <% if (request.getParameter("_method") == null || request.getParameter("_method").equals("")){ %>
            location.href = "<%=request.getContextPath()%>/draco?userAction=draco.declaration.ligneDeclaration.afficher&selectedId=<%=request.getParameter("selectedId")%>&idAssurance="+this.value;
        <% } else if(request.getParameter("_method").equals("add")){ %>
            location.href = "<%=request.getContextPath()%>/draco?userAction=draco.declaration.ligneDeclaration.afficher&_method=add&idDeclaration=<%=viewBean.getIdDeclaration()%>&idAssurance="+this.value;
        <% }else{ %>
            location.href = "<%=request.getContextPath()%>/draco?userAction=draco.declaration.ligneDeclaration.afficher&_method=<%=request.getParameter("_method")%>&selectedId=<%=request.getParameter("selectedId")%>&idAssurance="+this.value;
        <% } %>
    });
})



function add() {
	updatePage();
	document.forms[0].elements('userAction').value="draco.declaration.ligneDeclaration.ajouter"
	document.forms[0].elements('cumulCotisation').value='';
	document.forms[0].elements('tauxAssuranceDeclaration').value='';
	document.forms[0].elements('fractionAssuranceDeclaration').value='';
	document.forms[0].elements('cotisationDue').value='';
	document.forms[0].elements('soldeCot').value='';

}
function upd() {
//	 On met à jour les infos du compte
	document.forms[0].target = "fr_main";
	document.forms[0].elements('selectedId').value = document.forms[0].elements('idDeclaration').value;
    <%updateCalled = true;%>;
	updatePage();
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="draco.declaration.ligneDeclaration.ajouter";
    else {
        document.forms[0].elements('userAction').value="draco.declaration.ligneDeclaration.modifier";
    }
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add") {
		document.forms[0].elements('userAction').value="draco.declaration.ligneDeclaration.afficher";
	} else {
		document.forms[0].elements('userAction').value="draco.declaration.ligneDeclaration.chercher";
	}
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="draco.declaration.ligneDeclaration.supprimer";
        document.forms[0].elements('selectedId').value = document.forms[0].elements('idDeclaration').value;
        document.forms[0].submit();
    }
}

function init(){
//	 On met à jour les infos
	updatePage();
//	 On raffraichit le _rcListe du parent (CAPage)
<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	if (parent.document.forms[0])
		parent.document.forms[0].submit();
<%}%>
}
function updatePage() {
}
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ligne de la déclaration<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr><td>
		<table>
		  <tr> 
            <td>Assurance</td>
            <td nowrap><ct:FWListSelectTag name="assuranceId" defaut="<%=viewBean.getAssuranceId()%>" data="<%=viewBean.getCotisations()%>"/></TD>
          </tr>
          <tr> 
            <td>Ann&eacute;e d&eacute;claration</td>
            <TD> 
              <input name="anneCotisation" value="<%if (viewBean.getDeclaration() != null) {%><%=viewBean.getAnneCotisation()%><%}%>"  size="4" >
            </TD>
            <TD>
			  <input name="idDeclaration" type="hidden" value="<%=viewBean.getIdDeclaration()%>">
            </TD>
          </tr>
          <tr> 
            <td nowrap>Masse effective</td>
            <td>
                <% if (viewBean.getDeclaration().getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {%>
                    <input name='montantDeclarationComp' <%=isReadonlyAutres%>  value="<%=viewBean.getMontantDeclaration()%>" class="<%=classAutres%>">
                <% } else {%>
                    <input name='montantDeclaration' <%=isReadonlyAutres%>  value="<%if (!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getMontantDeclaration())) {%><%=viewBean.getMontantDeclaration()%><%} else if (!"0.00".equals(viewBean.getDeclaration().getMasseSalTotal())) {%><%=viewBean.getDeclaration().getMasseSalTotal()%><%}%>" class="<%=classAutres%>">
                <%}%>
            </td>
          </tr>
          <tr> 
            <td>Cotisation due</td>
            <td>
                <input name='cotisationDue' <%=isReadonlyCotisationDue%> value="<%=globaz.globall.util.JANumberFormatter.formatNoRound(viewBean.getCotisationDue())%>" class="<%=classCotisationDue%>">
            </td>
          </tr>
          <tr> 
            <td>Acompte</td>
            <% if (viewBean.getDeclaration().getEtat().equalsIgnoreCase(DSDeclarationViewBean.CS_COMPTABILISE)) {%>
            <td><input name='cumulCotisation' readonly value="<%=globaz.globall.util.JANumberFormatter.formatNoRound(viewBean.getMontantFactureACEJour())%>" class="montantDisabled"></td>
            <%} else {%>
            <td><input name='cumulCotisation' readonly value="<%if (viewBean.getCompteur() != null) {%><%=globaz.globall.util.JANumberFormatter.formatNoRound(viewBean.getCompteur().getCumulCotisation())%><%}else{%><%="0.00"%><%}%>" class="montantDisabled"></td>
            <%}%>   
          </tr>
          <tr>
          	   <td>Solde cotisations</td>
            <td>
	            <input name='soldeCot'  readonly value="<%=JANumberFormatter.formatNoRound(viewBean.getSoldeCotisation())%>" class="montantDisabled">
            </td>
          </tr>
          <tr>
          	<td>Taux</td>
            <% 
            AFTauxAssurance tauxLigne = viewBean.getAssurance().getTaux("31.12."+viewBean.getDeclaration().getAnnee());
            if(tauxLigne!=null && CodeSystem.GEN_VALEUR_ASS_TAUX_VARIABLE.equals(tauxLigne.getGenreValeur())) {
            	if (!JAUtil.isStringEmpty(viewBean.getTauxAssuranceDeclaration())) {%>
                <td><INPUT name="tauxAssuranceDeclaration" size="10" value="<%=viewBean.getTauxAssuranceDeclaration()%>" class="<%=classTaux%>" <%=isReadonlyAutres%> >
                / <INPUT name="fractionAssuranceDeclaration" size="8" value="<%=viewBean.getFractionAssuranceDeclaration()%>"  class="<%=classTaux%>" readonly></td>
                <%} else {%>
                <td><INPUT name="tauxAssuranceDeclaration" size="10" value="<%=JANumberFormatter.fmt(viewBean.getTauxAssurance(),true,true,true,5)%>" class="<%=classTaux%>" <%=isReadonlyAutres%>>
                / <INPUT name="fractionAssuranceDeclaration" size="8" value="<%=viewBean.getFractionAssurance()%>" class="disabled" readonly></td>
                <%}
            } else {
            	if (!JAUtil.isStringEmpty(viewBean.getTauxAssuranceDeclaration())) {%>
	            <td><INPUT name="tauxAssuranceDeclaration" size="10" value="<%=viewBean.getTauxAssuranceDeclaration()%>" class="<%=classTaux%>" <%=isReadonlyAutres%>>
	            / <INPUT name="fractionAssuranceDeclaration" size="8" value="<%=viewBean.getFractionAssuranceDeclaration()%>" class="<%=classTaux%>" <%=isReadonlyAutres%>></td>
	            <%} else {%>
	            <td><INPUT name="tauxAssuranceDeclaration" size="10" value="<%=JANumberFormatter.fmt(viewBean.getTauxAssurance(),true,true,true,5)%>" class="<%=classTaux%>" <%=isReadonlyAutres%>>
	            / <INPUT name="fractionAssuranceDeclaration" size="8" value="<%=viewBean.getFractionAssurance()%>" class="<%=classTaux%>" <%=isReadonlyAutres%>></td>
	            <%}
	        } %>
          </tr>
          </table></td>
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
<ct:menuChange displayId="options" menuId="DS-OptionsDeclaration" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key="idDeclaration" checkAdd="no" value="<%=viewBean.getDeclaration().getIdDeclaration()%>"/>
	<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getIdDeclaration()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>