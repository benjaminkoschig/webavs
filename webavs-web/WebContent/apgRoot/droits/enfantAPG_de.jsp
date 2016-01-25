<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%

idEcran="PAP0008";

globaz.apg.vb.droits.APEnfantAPGViewBean viewBean = (globaz.apg.vb.droits.APEnfantAPGViewBean) session.getAttribute("viewBean");
bButtonCancel = false;
bButtonUpdate = false;
bButtonDelete = false;
bButtonValidate = false;
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.apg.servlet.IAPActions"%>
<SCRIPT>
var servlet = "<%=(servletContext + mainServletPath)%>";

var isFirstClick = true;

  function add() {
  }
  
  function upd() {
  
  }

  function validate() {
    state = true
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.actionEtape3";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="apg.droits.situationFamilialeAPG.supprimer";
        document.forms[0].submit();
    }
  }
  
  function init(){
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
  }
  
  function controlerMontantFraisGarde() {
	  if ($('#fraisGarde').val() < 20) {
		  $('#fraisGarde').val("");
		  alert("RAPG 308 : Les frais de garde ne peuvent pas être < 20 CHF");
	  } else {
		  if ($('#nbrEnfantsDebutDroit').val() < 1) {
			  $('#fraisGarde').val("");
			  alert("RAPG 309 : Les frais de garde ne peuvent pas être saisis sans enfant");
		  }
	  }
  }
  
  function ajouterEnfant(){
  		if (validate()){
  			document.forms[0].submit();
  		}
  
  }
  
  	function arret() {
		document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.arreterEtape3";
		document.forms[0].target="fr_main";
  		document.forms[0].submit();
	}
	
	function calculPrestation(){
		if(isFirstClick){
			isFirstClick=false;
			document.forms[0].elements('userAction').value = "<%=globaz.apg.servlet.IAPActions.ACTION_ENFANT_APG%>.actionCalculerToutesLesPrestation";
			document.forms[0].target="fr_main";
			document.forms[0].elements('selectedId').value = "<%=viewBean.getIdDroit()%>";
			document.forms[0].submit();
		}
		//top.document.location.href = servlet + "?userAction=apg.prestation.prestation.actionCalculerPrestations&<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>=<%=viewBean.getIdDroit()%>";
	}
  


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD colspan="5">
								<INPUT type="hidden" name="idSituationFamiliale" value="<%=viewBean.getIdSituationFamilialeAPG()%>">
								<INPUT type="hidden" name="<%=globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT%>" value="<%=request.getParameter(globaz.apg.servlet.APAbstractDroitDTOAction.PARAM_ID_DROIT)%>">
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="nbrEnfantsDebutDroit"><ct:FWLabel key="JSP_NOMBRE_ENFANTS_DEBUT_DROIT"/></LABEL></TD>
							<TD><INPUT type="text" name="nbrEnfantsDebutDroit" id="nbrEnfantsDebutDroit" value="<%=viewBean.getNbrEnfantsDebutDroit()%>" <%=viewBean.isModifiable()?"":"readonly disabled"%>></TD>
							<TD><LABEL for="fraisGarde"><ct:FWLabel key="JSP_FRAIS_GARDE"/></LABEL></TD>
							<TD><input onchange="validateFloatNumber(this);controlerMontantFraisGarde();" onkeypress="return filterCharForFloat(window.event);" type="text" class="libelle" style="text-align : right" name="fraisGarde" id="fraisGarde" value="<%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getFraisGarde(),true,true,false,2)%>" <%=viewBean.isModifiable()?"":"readonly disabled"%>/></TD>
										
						</TR>
						<TR>
							<TD colspan="5"> <HR> </TD>
						</TR>
						<TR>
							<TD><LABEL for="nombreEnfants"><ct:FWLabel key="JSP_NOMBRE_ENFANTS"/></LABEL></TD>
							<TD><INPUT type="text" name="nombreEnfants" value="" <%=viewBean.isModifiable()?"":"readonly disabled"%>></TD>
							<TD><LABEL for="desLe"><ct:FWLabel key="JSP_DES_LE"/></LABEL></TD>
							<TD><%if (viewBean.isModifiable()){%><ct:FWCalendarTag name="dateDebutDroit" value=""/><%}else{%><INPUT type="text" readonly disabled><%}%></TD>
							<TD>
								<%if (viewBean.isModifiable()){%>
								<ct:ifhasright element="<%=IAPActions.ACTION_ENFANT_APG%>" crud="u">
								<INPUT type="button" value="<ct:FWLabel key="JSP_AJOUTER"/>" onclick="ajouterEnfant()">
								</ct:ifhasright>
								<%}%>
							</TD>
						</TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
					<INPUT type="button" value="<ct:FWLabel key="JSP_ARRET"/>" onclick="arret()">
					<ct:ifhasright element="<%=IAPActions.ACTION_ENFANT_APG%>" crud="u">
					<%if(viewBean.isModifiable()) { %>
						<INPUT type="button" value="<ct:FWLabel key="JSP_CALCULER_PRESTATION"/>" onclick="calculPrestation()">
					<% } %>
					</ct:ifhasright>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>