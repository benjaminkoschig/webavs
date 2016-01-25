<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<script type="text/javascript"
			src="<%=servletContext%>/scripts/nss.js"></script>
		<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%
idEcran="PAP0007";

globaz.apg.vb.droits.APEnfantMatViewBean viewBean = (globaz.apg.vb.droits.APEnfantMatViewBean) session.getAttribute("viewBean");
bButtonCancel = false;

// on affiche la gestion des adoptions que pour les caisses ayant droit a la maternite cantonale
boolean isDroitMaterniteCantonale = "true".equals(globaz.prestation.application.PRAbstractApplication.getApplication(globaz.apg.application.APApplication.DEFAULT_APPLICATION_APG).getProperty("isDroitMaterniteCantonale"));

// les boutons suivants ne s'affichent dans leur contexte habituel QUE lorsque le droit est modifiable.
bButtonUpdate = viewBean.isModifiable() && bButtonUpdate &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_MAT, FWSecureConstants.UPDATE);
bButtonDelete = viewBean.isModifiable() && bButtonDelete &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_MAT, FWSecureConstants.UPDATE);
bButtonValidate = viewBean.isModifiable() && bButtonValidate &&  viewBean.getSession().hasRight(IAPActions.ACTION_ENFANT_MAT, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_MAT%>.ajouter"
  }
  
  function upd() {
	  parent.isModification = true;
  }

  function validate() {
    parent.isNouveau = false;
    parent.isModification = false;
  
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_MAT%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_MAT%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_MAT%>.actionEtape2";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_MAT%>.supprimer";
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

								<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=viewBean.getIdDroitMaternite()%>">
							</TD>
						</TR>
						<%if(isDroitMaterniteCantonale){%>
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