<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays" %>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/droits/enfantUtils.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
idEcran="PAP0007";

globaz.apg.vb.droits.APEnfantPatViewBean viewBean = (globaz.apg.vb.droits.APEnfantPatViewBean) session.getAttribute("viewBean");
bButtonCancel = false;

// on affiche la gestion des adoptions que pour les caisses ayant droit a la paternite cantonale
boolean isDroitPaterniteCantonale = "true".equals(globaz.prestation.application.PRAbstractApplication.getApplication(globaz.apg.application.APApplication.DEFAULT_APPLICATION_APG).getProperty("isDroitPaterniteCantonale"));

// les boutons suivants ne s'affichent dans leur contexte habituel QUE lorsque le droit est modifiable.
bButtonUpdate = viewBean.isModifiable() && bButtonUpdate &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_PAT, FWSecureConstants.UPDATE);
bButtonDelete = viewBean.isModifiable() && bButtonDelete &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_PAT, FWSecureConstants.UPDATE);
bButtonValidate = viewBean.isModifiable() && bButtonValidate &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_PAT, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_PAT%>.ajouter"
  }
  
  function upd() {
	  parent.isModification = true;
  }

  function validate() {
    parent.isNouveau = false;
    parent.isModification = false;
    document.getElementById("nationalite").value = document.getElementById("nationaliteAffichee").value;

    if (isSuisse($('#nationalite').val())){
		$('#canton').val(document.getElementById("cantonAffiche").value);
	} else {
		$('#canton').val("");
	}
  
    state = true;
    if (!controleDateNaissance($('#dateNaissance').val())) {
    	state = false;
	}

    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_PAT%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_PAT%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_PAT%>.actionEtape2";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_PAT%>.supprimer";
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
	
	<%if(isDroitPaterniteCantonale){%>
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
  	<%if(isDroitPaterniteCantonale){%>
		// change le label "Date de naissance" / "Date d'adoption"
		if (document.all('isAdoption').checked) {
			document.getElementById('dateNaissanceAdoption').innerText= "<ct:FWLabel key="JSP_DATE_ADOPTION"/>";
		}else{
			document.getElementById('dateNaissanceAdoption').innerText="<ct:FWLabel key="JSP_DATE_NAISSANCE"/>";
		}
	<%}%>
  }

  function controleDateNaissance(dateNaissanceString) {
	  var today = new Date();
	  var from = dateNaissanceString.split(".")
	  var dateNaissance = new Date(from[2], from[1] - 1, from[0])
	  if (dateNaissance.getTime() > today.getTime()){
		  createDialog("erreur", "<ct:FWLabel key='JSP_DATA_NAISSANCE_NON_VALIDE'/>");
		  return false;
	  }
	  return true;
  }

  $(document).ready(function() {
	  onChangeNationalite();
	  $('#nationaliteAffichee').on('change',function () {
		  onChangeNationalite();
	  });

	  $('#dateNaissance').on('change', function (){
		  controleDateNaissance($('#dateNaissance').val());
	  });
  });
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="type"><ct:FWLabel key="JSP_TYPE_SIT_FAM"/></LABEL></TD>
							<TD><%=viewBean.getTypeLibelle()%></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR><TD colspan="4">&nbsp;</TD></TR>
						<TR>
							<TD><LABEL for="nom"><ct:FWLabel key="JSP_NOM"/>&nbsp;</LABEL></TD>
							<TD><INPUT type="text" name="nom" value="<%=viewBean.getNom()%>" onblur="javascript:document.all('prenom').focus()"></TD>
							<TD><LABEL for="prenom"><ct:FWLabel key="JSP_PRENOM"/>&nbsp;</LABEL></TD>
							<TD><INPUT type="text" name="prenom" value="<%=viewBean.getPrenom()%>"></TD>
						</TR>
						<TR>
							<TD><LABEL id="dateNaissanceAdoption" for="dateNaissance"><ct:FWLabel key="JSP_DATE_NAISSANCE"/>&nbsp;</LABEL></TD>
							<TD><ct:FWCalendarTag name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/></TD>
							<TD><LABEL for="noAVS"><ct:FWLabel key="JSP_NSS_ABREGE"/>&nbsp;</LABEL></TD>
							<TD>
									<ct1:nssPopup name="noAVS" onFailure="nssFailure();" onChange="nssChange(tag);"
										value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>" newnss="<%=viewBean.isNNSS()%>"
										avsMinNbrDigit="99" nssMinNbrDigit="99"   />

								<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroitPaternite()%>">
							</TD>
						</TR>
						<TR>
							<TD><LABEL><ct:FWLabel key="JSP_PAYS_DOMICILE"/>&nbsp;</LABEL></TD>
							<td><ct:FWListSelectTag name="nationaliteAffichee"
												data="<%=viewBean.getTiPays()%>"
												defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getNationalite()) ? TIPays.CS_SUISSE : viewBean.getNationalite()%>"/>
							</td>
							<TD><INPUT type="hidden" name="nationalite" id="nationalite" value="<%=viewBean.getNationalite()%>" onchange="onChangeNationalite()"></TD>
							<td></td>
						</TR>
						<tr id="blocCanton" style="display:none">
							<TD><LABEL for="canton"><ct:FWLabel key="JSP_MEMBRE_FAMILLE_CANTON"/>&nbsp;</LABEL></TD>
							<td><ct:FWCodeSelectTag name="cantonAffiche"
									wantBlank="<%=false%>"
									codeType="PYCANTON"
									defaut="<%=viewBean.getCanton()%>"/></td>
							<TD><INPUT type="hidden" name="canton" id="canton" value="<%=viewBean.getCanton()%>"></TD>
							<td></td>
						</tr>
						<%if(isDroitPaterniteCantonale){%>
						<TR>
							<TD><LABEL for="adoption"><ct:FWLabel key="JSP_ADOPTION"/>&nbsp;</LABEL></TD>
							<TD><INPUT type="checkbox" name="isAdoption" value="on" onclick="boutonAdoptionChange();" <%=viewBean.getIsAdoption().booleanValue()?"CHECKED":""%>></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<%}%>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
