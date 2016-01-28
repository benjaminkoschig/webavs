<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont préfixés avec 'JSP_CTD_D'
--%>
<%
idEcran="GDO0003";
globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean viewBean = (globaz.cepheus.vb.dossier.DOMetaDossierJointIntervenantsViewBean) session.getAttribute("viewBean");


bButtonUpdate = true;
bButtonValidate = true;
bButtonDelete = true;
bButtonCancel = true;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
		<script language="JavaScript">

  function add() {
    document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS%>.ajouter"
  }
  function upd() {}

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS%>.afficher";
  }

  function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="<%=globaz.cepheus.servlet.IDOActions.ACTION_CHERCHER_PRESTATION_INTERVENANTS%>.supprimer";
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TITRE_PRESTATION_INTERVENANTS"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<LABEL for="nomIntervenant"><ct:FWLabel key="JSP_INTERVENANT"/></LABEL>
								<INPUT type="hidden" name="idIntervenant" value="<%=viewBean.getIdIntervenant()%>">						
							</TD>
							<TD>
								<INPUT type="hidden" name="idTiersIntervenant" value="<%=viewBean.getIdTiersIntervenant()%>">	
								<INPUT type="text" name="nomIntervenant" value="<%=viewBean.getNomPrenomIntervenant()%>" disabled="disabled">
								<ct:FWSelectorTag
									name="selecteurIntervenant"
									
									methods="<%=viewBean.getMethodesSelecteurIntervenant()%>"
									providerApplication="pyxis"
									providerPrefix="TI"
									providerAction="pyxis.tiers.tiers.chercher"
									target="_parent"
									redirectUrl="<%=mainServletPath%>"/>
							</TD>
							<TD>
								<LABEL for="csDescription"><ct:FWLabel key="JSP_GENRE"/></LABEL>
							</TD>
							<TD>
								<ct:select name="csDescription" defaultValue="<%=viewBean.getCsDescription()%>" >
									<%if(globaz.prestation.api.IPRDemande.CS_TYPE_APG.equals(viewBean.getCsTypeDemande())){%>
										<ct:optionsCodesSystems csFamille="DOGINTAPG"/>
									<%} else if(globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE.equals(viewBean.getCsTypeDemande())){%>
										<ct:optionsCodesSystems csFamille="DOGINTMAT"/>
									<%} else if(globaz.prestation.api.IPRDemande.CS_TYPE_IJ.equals(viewBean.getCsTypeDemande())){%>
										<ct:optionsCodesSystems csFamille="DOGINTIJA"/>
									<%}%>
								</ct:select>
							</TD>
						</TR>
						<TR>
							<TD>
								<LABEL for="dateDebut"><ct:FWLabel key="JSP_DATE_DEBUT"/></LABEL>
							</TD>
							<TD>
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/> 
							</TD>
							<TD>
								<LABEL for="dateFin"><ct:FWLabel key="JSP_DATE_FIN"/></LABEL>
							</TD>
							<TD>	
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/> 
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