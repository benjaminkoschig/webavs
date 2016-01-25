<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%@page import="globaz.osiris.external.IntRole"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.osiris.api.APISection"%>
<%@page import="globaz.corvus.api.basescalcul.IREFactureARestituer"%>
<%@page import="globaz.corvus.vb.rentesaccordees.REFactureARestituerViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_FAR_D"
	idEcran="PRE0024";

	REFactureARestituerViewBean viewBean = (REFactureARestituerViewBean) session.getAttribute("viewBean");
	
	bButtonDelete = false;
	bButtonNew = false;
	bButtonUpdate = bButtonUpdate && !viewBean.isNew();
	bButtonCancel = bButtonCancel && !viewBean.isNew();
	bButtonValidate = bButtonValidate && !viewBean.isNew();
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_FACTURE_A_RESTITUER%>.ajouter"
  }

  function upd() {
  }

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_FACTURE_A_RESTITUER%>.ajouter";
    else

        document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_FACTURE_A_RESTITUER%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.corvus.servlet.IREActions.ACTION_FACTURE_A_RESTITUER%>.chercher";
  }

  function init(){
    // recharger la page rcListe du parent si une modification a ete effectuee
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>
	document.forms[0].target="fr_main";
  }

  function readOnly(flag) {
	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
	for(i=0; i < document.forms[0].length; i++) {
	  if (!document.forms[0].elements[i].readOnly &&
	      document.forms[0].elements[i].className != 'forceDisable' &&
	      document.forms[0].elements[i].type != 'hidden') {
	        document.forms[0].elements[i].disabled = flag;
	  }
	}
  }
  
  function actionChangeEtat(){
    document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_FACTURE_A_RESTITUER%>.actionChangeEtat";
	//document.forms[0].target="_self";
	document.forms[0].submit();
  }


</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_FAR_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_FAR_D_BENEFICIAIRE"/></TD>
							<TD colspan="3">
								<re:PRDisplayRequerantInfoTag
									session="<%=(globaz.globall.db.BSession)controller.getSession()%>"
									idTiers="<%=viewBean.getIdTiersBenefPrincipal()%>"
									style="<%=globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>"/>
							</TD>
						</TR>
						<TR><TD>&nbsp;</TD></TR>
						<TR>
							<TD><ct:FWLabel key="JSP_FAR_D_MONTANT_A_RESTITUER"/></TD>
							<TD><INPUT type="text" name="montantFactARestituer" value="<%=new FWCurrency(viewBean.getMontantFactARestituer()).toStringFormat()%>" class="montant"></TD>
							<TD><ct:FWLabel key="JSP_FAR_D_ETAT"/></TD>
							<TD>
								<INPUT type="text" name="csEtatLibelle" value="<%=viewBean.getCsEtatLibelle()%>" readonly="readonly" class="disabled">
							</TD>
						</TR>						
						<TR>
							<TD><ct:FWLabel key="JSP_FAR_D_CATEGORIE_SECTION"/></TD>
							<TD>
								<INPUT type="text" name="catSection" value="<%=viewBean.getCsCatSectionLibelle()%>" readonly="readonly" class="disabled">
							</TD>
							<TD><ct:FWLabel key="JSP_FAR_D_ROLE"/></TD>
							<TD>
								<INPUT type="text" name="role" value="<%=viewBean.getCsRoleLibelle()%>" readonly="readonly" class="disabled">
								<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>">	
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%if(IREFactureARestituer.CS_A_FACTURER.equals(viewBean.getCsEtat())){%>
				 	<INPUT name="boutonBlocage" type="button" value="<%=viewBean.getSession().getCodeLibelle(IREFactureARestituer.CS_ATTENTE)%>" onclick="actionChangeEtat();">
				<%} else if(IREFactureARestituer.CS_ATTENTE.equals(viewBean.getCsEtat())){%>
					<INPUT name="boutonBlocage" type="button" value="<%=viewBean.getSession().getCodeLibelle(IREFactureARestituer.CS_A_FACTURER)%>" onclick="actionChangeEtat();">
				<%} %>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>