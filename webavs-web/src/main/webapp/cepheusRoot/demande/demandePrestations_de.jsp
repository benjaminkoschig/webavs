<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont préfixés avec 'JSP_CTD_D'
--%>
<%
idEcran="GDO0001";
globaz.cepheus.vb.demande.DODemandePrestationsViewBean viewBean = (globaz.cepheus.vb.demande.DODemandePrestationsViewBean) session.getAttribute("viewBean");


bButtonCancel = false;
bButtonUpdate = false;
bButtonValidate = false;
bButtonDelete = false;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.babel.servlet.CTMainServletAction.ACTION_DOCUMENTS%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
    // recharger la page rcListe du parent si une modification a ete effectuee
	<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>

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

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_DEMANDE_PRESTATIONS"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><B>Détail assuré</B></TD>
							<TD><INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly></TD>
						</TR>

						<TR>
							<TD colspan="3" height="30">&nbsp;</TD>
						</TR>
						<TR>
							<TD align="left">
								<B><ct:FWLabel key="JSP_PRESTATIONS_EN_COURS"/></B>
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="2" align="center">
								<TABLE border="0" cellspacing="0" cellpadding="0" width="40%">
									<%if(viewBean.hasDemandeIj().booleanValue()){%>
									<TR>
										<TD height="25"><ct:FWLabel key="JSP_IJAI"/></TD>										
										<TD height="25">
											<A  href="<%=request.getContextPath() + mainServletPath + "?userAction=" + 
											             globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS + 
											             ".chercherDepuisDemande&idDemande=" + viewBean.getDemandeIjId() + 
											             "&idTiersMetaDossier=" + viewBean.getIdTiers() +
											             "&nomTiersMetaDossier=" + viewBean.getNom() +
											             "&prenomTiersMetaDossier=" + viewBean.getPrenom() +
											             "&noAvsTiersMetaDossier=" + viewBean.getNoAvs() +
											             "&csTypeDemande=" + globaz.prestation.api.IPRDemande.CS_TYPE_IJ +
											             "&idMetaDossier=" + viewBean.getMetaDossierIdDemandeIj() +
											             "&detailIntervenant=" + viewBean.getDetailRequerantDetail() %>" target="_parent">
												<ct:FWLabel key="JSP_INTERVENANTS"/>
											</A>
										</TD>
									</TR>
									<%}%>
									<%if(viewBean.hasDemandeApg().booleanValue()){%>
									<TR>
										<TD height="25"><ct:FWLabel key="JSP_APG"/></TD>										
										<TD height="25">
											<A  href="<%=request.getContextPath() + mainServletPath + "?userAction=" + 
											             globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS + 
											             ".chercherDepuisDemande&idDemande=" + viewBean.getDemandeApgId() + 
											             "&idTiersMetaDossier=" + viewBean.getIdTiers() +
											             "&nomTiersMetaDossier=" + viewBean.getNom() +
											             "&prenomTiersMetaDossier=" + viewBean.getPrenom() +
											             "&noAvsTiersMetaDossier=" + viewBean.getNoAvs() +
											             "&csTypeDemande=" + globaz.prestation.api.IPRDemande.CS_TYPE_APG +
											             "&idMetaDossier=" + viewBean.getMetaDossierIdDemandeApg() +
											             "&detailIntervenant=" + viewBean.getDetailRequerantDetail() %>" target="_parent">
												<ct:FWLabel key="JSP_INTERVENANTS"/>
											</A>
										</TD>
									</TR>
									<%}%>
									<%if(viewBean.hasDemandeMat().booleanValue()){%>
									<TR>
										<TD height="25"><ct:FWLabel key="JSP_MAT"/></TD>										
										<TD height="25">
											<A  href="<%=request.getContextPath() + mainServletPath + "?userAction=" + 
											             globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS + 
											             ".chercherDepuisDemande&idDemande=" + viewBean.getDemandeMatId() +
											             "&idTiersMetaDossier=" + viewBean.getIdTiers() +
											             "&nomTiersMetaDossier=" + viewBean.getNom() +
											             "&prenomTiersMetaDossier=" + viewBean.getPrenom() +
											             "&noAvsTiersMetaDossier=" + viewBean.getNoAvs() +
											             "&csTypeDemande=" + globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE +
											             "&idMetaDossier=" + viewBean.getMetaDossierIdDemandeMat() +
											             "&detailIntervenant=" + viewBean.getDetailRequerantDetail() %>" target="_parent">
												<ct:FWLabel key="JSP_INTERVENANTS"/>
											</A>
										</TD>									
									</TR>
									<%}%>
									<%if(viewBean.hasDemandeRentes().booleanValue()){%>
									<TR>
										<TD height="25"><ct:FWLabel key="JSP_RENTES"/></TD>										
										<TD height="25">
											<A  href="<%=request.getContextPath() + mainServletPath + "?userAction=" + 
											             globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS + 
											             ".chercherDepuisDemande&idDemande=" + viewBean.getDemandeRentesId() + 
											             "&idTiersMetaDossier=" + viewBean.getIdTiers() +
											             "&nomTiersMetaDossier=" + viewBean.getNom() +
											             "&prenomTiersMetaDossier=" + viewBean.getPrenom() +
											             "&noAvsTiersMetaDossier=" + viewBean.getNoAvs() +
											             "&csTypeDemande=" + globaz.prestation.api.IPRDemande.CS_TYPE_RENTE +
											             "&idMetaDossier=" + viewBean.getMetaDossierIdDemandeRentes() +
											             "&detailIntervenant=" + viewBean.getDetailRequerantDetail() %>" target="_parent">
												<ct:FWLabel key="JSP_INTERVENANTS"/>
											</A>
										</TD>
									</TR>
									<%}%>
								</TABLE>
							</TD>
							<TD>&nbsp;</TD>
						</TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>