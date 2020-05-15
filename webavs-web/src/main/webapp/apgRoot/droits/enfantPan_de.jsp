<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.apg.api.droits.IAPDroitMaternite" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="globaz.prestation.interfaces.util.nss.PRUtil" %>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
idEcran="PAP0007";

globaz.apg.vb.droits.APEnfantPanViewBean   viewBean = (globaz.apg.vb.droits.APEnfantPanViewBean ) session.getAttribute("viewBean");
bButtonCancel = false;

// on affiche la gestion des adoptions que pour les caisses ayant droit a la maternite cantonale
boolean isDroitMaterniteCantonale = "true".equals(globaz.prestation.application.PRAbstractApplication.getApplication(globaz.apg.application.APApplication.DEFAULT_APPLICATION_APG).getProperty("isDroitMaterniteCantonale"));

// les boutons suivants ne s'affichent dans leur contexte habituel QUE lorsque le droit est modifiable.
bButtonUpdate = viewBean.isModifiable() && bButtonUpdate &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_PAN, FWSecureConstants.UPDATE);
bButtonDelete = viewBean.isModifiable() && bButtonDelete &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_PAN, FWSecureConstants.UPDATE);
bButtonValidate = viewBean.isModifiable() && bButtonValidate &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_PAN, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="plausibilites.jsp" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
  function add() {
    document.forms[0].elements('userAction').value="<%=IAPActions.ACTION_ENFANT_PAN%>.ajouter"
  }

  function upd() {
	  parent.isModification = true;
  }

  function validate() {
    parent.isNouveau = false;
    parent.isModification = false;

    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=IAPActions.ACTION_ENFANT_PAN%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=IAPActions.ACTION_ENFANT_PAN%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=IAPActions.ACTION_ENFANT_PAN%>.actionEtape2";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=IAPActions.ACTION_ENFANT_PAN%>.supprimer";
        document.forms[0].submit();
    }
  }

  function init(){
  	parent.isNouveau = false;
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
		parent.isNouveau = true;
	<%}%>

	<%if(isDroitMaterniteCantonale){%>
		// set le label "Date de naissance" / "Date d'adoption" en fonction de isAdoption
	    <%if (viewBean.getIsAdoption().booleanValue()) {%>
			document.getElementById('dateNaissanceAdoption').innerText="<ct:FWLabel key="JSP_DATE_ADOPTION"/>";
		<%}else{%>
			document.getElementById('dateNaissanceAdoption').innerText="<ct:FWLabel key="JSP_DATE_NAISSANCE"/>";
		<%}%>
	<%}%>
  }

  function upperCaseFirstChar(input){
  	input.value = input.value.charAt(0).toUpperCase() +
                  input.value.substring(1).toLowerCase();
  }

  function boutonAdoptionChange() {
  	<%if(isDroitMaterniteCantonale){%>
		// change le label "Date de naissance" / "Date d'adoption"
		if (document.all('isAdoption').checked) {
			document.getElementById('dateNaissanceAdoption').innerText= "<ct:FWLabel key="JSP_DATE_ADOPTION"/>";
		}else{
			document.getElementById('dateNaissanceAdoption').innerText="<ct:FWLabel key="JSP_DATE_NAISSANCE"/>";
		}
	<%}%>
  }

  function nssChange(tag) {
	  var param_nss = "756." + document.getElementById("partialnoAVS").value;
	  if (tag.select !== null) {
		  _noAVS = removeDots(tag.select.options[tag.select.selectedIndex].nss);

		  if (_noAVS.length == 13) {
			  //formatte tag avec nouveau nss
			  nssCheckChar(43, 'noAVS');
			  nssAction('noAVS');
			  concatPrefixAndPartial('noAVS');
		  } else {
			  //formatte tag avec ancien nss
			  nssCheckChar(45, 'noAVS');
			  nssAction('noAVS');
			  concatPrefixAndPartial('noAVS');
		  }

		  var element = tag.select.options[tag.select.selectedIndex];

		  if (element.nss !== null) {
			  document.getElementById("noAVS").value = element.nss;
		  }

		  if (element.nom !== null) {
			  document.getElementById("nom").value = element.nom;
			  // document.getElementById("nomAffiche").value = element.nom;
		  }

		  if (element.prenom !== null) {
			  document.getElementById("prenom").value = element.prenom;
			  // document.getElementById("prenomAffiche").value = element.prenom;
		  }

		  if (element.nom !== null && element.prenom !== null) {
			  document.getElementById("nom").value = element.nom;
			  // document.getElementById("nomAffiche").value = element.nom;
			  document.getElementById("prenom").value = element.prenom;
			  // document.getElementById("prenomAffiche").value = element.prenom;
		  }

		  if (element.dateNaissance !== null) {
			  document.getElementById("dateNaissance").value = element.dateNaissance;
			  // document.getElementById("dateNaissanceAffiche").value = element.dateNaissance;
		  }

		  if ('<%=PRUtil.PROVENANCE_TIERS%>' === element.provenance) {
			  document.getElementById("nom").disabled = true;
			  document.getElementById("prenom").disabled = true;
			  document.getElementById("dateNaissance").disabled = true;
		  }
	  }
	  $('#nom').change();
	  $('#prenom').change();
	  $('#dateNaissance').change();
	  checkNss(param_nss);
  }

  function nssFailure() {
	  var param_nss = "756." + document.getElementById("partialnoAVS").value;
	  document.getElementById("noAVS").value = null;
	  document.getElementById("nom").disabled = false;
	  document.getElementById("prenom").disabled = false;
	  document.getElementById("dateNaissance").disabled = false;
	  checkNss(param_nss);
  }
</SCRIPT>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%
	HashSet except = new HashSet();
	except.add(IAPDroitMaternite.CS_TYPE_PERE);
%>

						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="type"><ct:FWLabel key="JSP_TYPE_SIT_FAM"/></LABEL></TD>
							<TD><ct:FWCodeSelectTag	name="type"
													   wantBlank="<%=false%>"
													   codeType="APTYPESITF"
													   defaut="<%=viewBean.getType()%>"
													   except="<%=except%>"/>
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="nom"><ct:FWLabel key="JSP_NOM"/>&nbsp;</LABEL></TD>
							<TD><INPUT type="text" name="nom" id="nom" value="<%=viewBean.getNom()%>" onblur="javascript:document.all('prenom').focus()"></TD>
							<TD><LABEL for="prenom"><ct:FWLabel key="JSP_PRENOM"/>&nbsp;</LABEL></TD>
							<TD><INPUT type="text" name="prenom" id="prenom" value="<%=viewBean.getPrenom()%>"></TD>
						</TR>
						<TR>
							<TD><LABEL id="dateNaissanceAdoption" for="dateNaissance">
								<ct:FWLabel key="JSP_DATE_NAISSANCE"/>&nbsp;</LABEL></TD>
							<TD><ct:FWCalendarTag name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/></TD>
							<TD><LABEL for="noAVS"><ct:FWLabel key="JSP_NSS_ABREGE"/>&nbsp;</LABEL></TD>
							<TD> <%
								String params = "&provenance1=TIERS&provenance2=CI";
								String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp";
							%>
								<ct1:nssPopup name="noAVS"
											  onFailure="nssFailure();"
											  onChange="nssChange(tag);"
											  params="<%=params%>"
											  value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>"
											  newnss="<%=viewBean.isNNSS()%>"
											  jspName="<%=jspLocation%>"
											  avsMinNbrDigit="3"
											  nssMinNbrDigit="3"
											  avsAutoNbrDigit="11"
											  nssAutoNbrDigit="10"/>

								<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroit()%>">

							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
