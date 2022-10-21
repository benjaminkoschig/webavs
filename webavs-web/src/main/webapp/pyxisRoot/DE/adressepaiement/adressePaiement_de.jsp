<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.pyxis.db.adressepaiement.TIAdressePaiement" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0025";
    globaz.pyxis.db.adressepaiement.TIAdressePaiementViewBean viewBean = (globaz.pyxis.db.adressepaiement.TIAdressePaiementViewBean)session.getAttribute ("viewBean");
    selectedIdValue = request.getParameter("selectedId");
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>


<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Adresse paiement D�tail"
function add() {
    document.forms[0].elements('userAction').value="pyxis.adressepaiement.adressePaiement.ajouter"
}
function upd() {
}
function validate() {

    //state = validateFields();
    var state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.adressepaiement.adressePaiement.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.adressepaiement.adressePaiement.modifier";
    
    return state;

}
function cancel() {
  if (document.forms[0].elements('_method').value == "add") {
 	 document.forms[0].elements('userAction').value="";
  	top.fr_appicons.icon_back.click();
  }

 else
  document.forms[0].elements('userAction').value="pyxis.adressepaiement.adressePaiement.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgew�hlte Objekt zu l�schen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pyxis.adressepaiement.adressePaiement.supprimer";
        document.forms[0].submit(); 
    }
}


function init(){
	changeNumeroCompte();
}

function changeNumeroCompte() {
	if (isQRIban()) {
		$('#referenceQR').show();
	} else {
		$('#referenceQR').hide();
	}
}

function isQRIban() {
	var pattern = /^CH\d{2}3[0-1]\d{3}\d{12}$/;
	var value = document.getElementsByName('numCompteBancaire')[0].value.replace(/\s/g, '');
	return pattern.test(value);
}

/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><span style="font-family:wingdings;font-weight:normal">*</span> - Detail einer Zahlungsadresse<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
	<tr>
<td width="100%">
<table>
<col width="80">
<tr>
	<td>Postkonto-Nr.</td>
	<td>
		<input type="text" name="numCcp" value="<%=viewBean.getNumCcp()%>">
	</td>
</tr>
<tr>
	<td>Konto-Nr.</td>
	<td>
		<input   name="numCompteBancaire"  class="libellelong" value="<%=viewBean.getNumCompteBancaireFormateIban()%>" onfocusout="changeNumeroCompte()" type="text">
		<%
		java.util.HashSet except = new java.util.HashSet();
			if (! viewBean.getCode().equals(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_INCORRECT)) {
				except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_INCORRECT);
			}  
			if (!viewBean.getCode().equals(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_OK)){ 
		        except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_OK);
		    }
		    if (!viewBean.getCode().equals(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT)){ 
		       except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT);
		    }
		    if (!viewBean.getCode().equals(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT_AUTRE)){ 
		      except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_IBAN_CONVERSION_INCORRECT_AUTRE);
		    }
		    if (!viewBean.getCode().equals(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_FUSION)){ 
		      except.add(globaz.pyxis.constantes.IConstantes.CS_ADRESSE_PAIEMENT_FUSION);
		    }
		
		%>
		&nbsp; Status &nbsp; 
	
		<ct:FWCodeSelectTag name="code"
			  defaut="<%=viewBean.getCode()%>"
				wantBlank="<%=true%>"
				codeType="PYIBAN"
				except="<%=except%>"
			  />
		
	</td>
</tr>
<tr>
	<td>Clearing</td>
	<td>	
		<input name="clearing" type="text" value="<%=viewBean.getClearing()%>" onkeydown="document.getElementsByName('idTiersBanque')[0].value=''">

		<%
			Object[] banqueMethodsName = new Object[]{
			new String[]{"setClearing","getClearing"},
			new String[]{"setIdTiersBanque","getIdTiersBanque"},
			};
			Object[] banqueParams = new Object[]{new String[]{"clearing","_pos"} };
		%>

		<ct:FWSelectorTag 
			name="banqueSelector" 
			
			methods="<%=banqueMethodsName %>"
			providerApplication ="pyxis"
			providerPrefix="TI"
			providerAction ="pyxis.tiers.banque.chercher"
			providerActionParams ="<%=banqueParams%>"
			/>
	
	
		<INPUT type="hidden" name="_act" value="">
		<INPUT type="hidden" name="idTiersBanque" value="<%=viewBean.getIdTiersBanque()%>">
	</td>					
</tr>
<tr>
	<td>Adresse<br><br><b><%=viewBean.getAdresseDomaineTypeDesc() %></b></td>
	<td>
		<TEXTAREA rows="5" align="left" readonly class="libelleLongDisabled"><%=viewBean.getDetailAdresse()%></TEXTAREA>							
			<%
			Object[] adresseMethodsName = new Object[]{ new String[]{"setIdAdresse","getIdAdresseUnique"},new String[]{"setIdTiersAdresse","getIdTiers"}};
			Object[] adresseParams = new Object[]{ new String[]{"critereParam","critere"}};
		%>
		<input type="hidden" name="critereParam" value="512006">

		<ct:FWSelectorTag 
			name="adresseSelector1" 
			
			methods="<%=adresseMethodsName%>"
			providerApplication ="pyxis"
			providerPrefix="TI"
			providerAction ="pyxis.adressecourrier.adresse.chercher"
			providerActionParams ="<%=adresseParams%>"
		/>

		<INPUT type="hidden" name="colonneSelection" value="<%=request.getParameter("colonneSelection")%>">
	</td>
</tr>

	<tr id="referenceQR">
		<td>QR-Referenz</td>
		<td>

			<input type="hidden"  name="forIdTiers" value="<%=!JadeStringUtil.isEmpty(viewBean.getIdTiers()) ? viewBean.getIdTiers() : viewBean.getIdTiersAdresse()%>">
		    <input type="hidden"  name="forIdAdressePaiement" value="<%=!JadeStringUtil.isEmpty(viewBean.getIdAdressePaiement()) ? viewBean.getIdAdressePaiement() : viewBean.getOldIdAdressePaiement()%>">
			<input type="hidden"  name="forCompteLike" value="<%=viewBean.getNumCompteBancaire()%>">
			<input type="hidden"  name="hideColonneSelection" value="yes">

			<%
				Object[] referencePaiementSelectionMethodsName = new Object[]{ new String[]{ }};
				Object[] referencePaiementChercherParams = new Object[]{ new String[]{"forIdTiers","forIdTiers"}, new String[]{"forIdAdressePaiement","forIdAdressePaiement"}, new String[]{"forCompteLike","forCompteLike"}};
			%>
			<ct:FWSelectorTag
					name="referencePaiementSelector1"
					methods="<%=referencePaiementSelectionMethodsName%>"
					providerApplication ="pyxis"
					providerPrefix="TI"
					providerAction ="pyxis.tiers.referencePaiement.chercher"
					providerActionParams ="<%=referencePaiementChercherParams%>"
					target="fr_main"
					redirectUrl="<%=mainServletPath%>"
			/>

		</td>
	</tr>

<tr>
			<td>Land</td>
			<td nowrap>
				<input type="text" class="libelleLongDisabled" readonly value="<%=viewBean.getNomPays()%>">

			<%
				Object[] paysMethodsName = new Object[]{
					new String[]{"setIdPays","getIdPays"},
					new String[]{"setNomPays","getLibelle"}
				};
				
			%>
			<ct:FWSelectorTag 
				name="paysSelector" 
				
				methods="<%=paysMethodsName %>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.adressecourrier.pays.chercher"
			/>
			<script>document.getElementById("paysSelector").tabIndex="18"</script>

				
			<input type="hidden"  name="idPays" value="<%=viewBean.getIdPays()%>">
		</td>
</tr>
<tr>
	<td>W�hrung</td>
	<td>
		<ct:FWCodeSelectTag name="idMonnaie"
		        defaut="<%=viewBean.getIdMonnaie()%>"
		        codeType="PYMONNAIE"/>
	
		<!-- pour copy -->
		<INPUT type="hidden" name="mode" value="<%=((request.getParameter("mode")==null)?"":request.getParameter("mode"))%>">
		<INPUT type="hidden" name="idAvoirPaiement" value="<%=request.getParameter("idAvoirPaiement")%>">
		<INPUT type="hidden" name="idTiers" value="<%=request.getParameter("idTiers")%>">
		<input type="hidden" name="oldIdAdressePaiement" value="<%=viewBean.getOldIdAdressePaiement()%>">
		<input type="hidden" name="doSynchroAvoirPaiement" value="<%=viewBean.getDoSynchroAvoirPaiement()%>">
	</td>
</tr>	

</table>
</td>
</tr>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%  }  %>

<ct:menuChange displayId="options" menuId="adressePaiement-detail" showTab="options">
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdAdressePmtUnique()%>"/>
</ct:menuChange>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>