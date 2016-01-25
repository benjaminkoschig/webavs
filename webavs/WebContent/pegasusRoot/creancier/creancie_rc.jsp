
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.models.droit.DroitSearch"%>
<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="ch.globaz.pegasus.business.services.PegasusServiceLocator"%>
<%@ page language="java" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.pegasus.utils.PCCreancierHandler"%>
<%@ include file="/theme/capage/header.jspf" %>
<%
// Les labels de cette page commence par la préfix "JSP_PC_CREANCIER_R"
	idEcran="PPC0050";
	IFrameDetailHeight = "520";
	String idDemande = request.getParameter("idDemandePc");
	actionNew = actionNew+"&idDemandePc="+idDemande;
	BSession objSession = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION));

	boolean afficherRepartireCrance = true;
	
	DroitSearch search = new DroitSearch();
	search.setForIdDemandePc(idDemande);
	search.setWhereKey("currentVersion");
	DroitSearch DroitSearch = PegasusServiceLocator.getDroitService().searchDroit(search);
	Droit droit = (Droit)DroitSearch.getSearchResults()[0];
	if("1".equals(droit.getSimpleVersionDroit().getNoVersion()) && 
			(IPCDroits.CS_AU_CALCUL.equals(droit.getSimpleVersionDroit().getCsEtatDroit()) || 
					IPCDroits.CS_ENREGISTRE.equals(droit.getSimpleVersionDroit().getCsEtatDroit())
		    )
		){
		afficherRepartireCrance = false;
	} else {
		afficherRepartireCrance = true;
	}
	
%>
 
<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/formTableLess.css">
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<script language="JavaScript">
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var ACTION_CREANCE_ACCORDEE= "<%=IPCActions.ACTION_CREANCE_ACCORDEE%>";
	var LABEL_BUTTON_CRANCE_ACCRODEE='Répartir les créances';
	var usrAction = "<%=IPCActions.ACTION_CREANCIER + ".lister" %>";
	var idDemande = <%=idDemande%>;
	var b_afficheBoutton = <%=Boolean.toString(afficherRepartireCrance)%>
	var creance = {
			addButtonRepartirCreance: function(){
				var $userAction = $('[name=userAction]');
				var $form= $('form')
				var s_target=$form.attr('target');
				$('<input/>',{
					type: 'button',
					value:LABEL_BUTTON_CRANCE_ACCRODEE,
					click: function() {
						$userAction.val(ACTION_CREANCE_ACCORDEE+".afficher"); 
						$form.attr('target','_self');
						//$form.attr('target','fr_detail');
						$form.submit();
						form.attr('target',s_target);
					},
					"class": 'btnCtrl'
				}).insertBefore('[name=btnFind]');
			}
	}
	$(function (){ 
		if(b_afficheBoutton){
			creance.addButtonRepartirCreance();
		}
	})
</script>
<style>
	.span span {
		padding-right:25px;
		padding-left:5px;
	}
	label{
		padding:5px 15px 0 0;
	}
</style>

<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
				
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
	<tr>
		<td>			
			<input type="hidden" name="idDemandePc" value="<%= idDemande%>" />
			<input type="hidden" name="creancierSearch.forIdDemande" value="<%= idDemande%>" />
			<label><ct:FWLabel key="JSP_PC_CREANCIER_R_BENEFICIARE" /></label>
		 </td>
		 <td> <%=PCCreancierHandler.getRequerantDetailByIdDemande(idDemande,objSession)%></td>
	 </tr>
	 
	 <tr>
	 	<td>
	 		<label><ct:FWLabel key="JSP_PC_CREANCIER_R_ID_DEMANDE" /></label>
	 	</td>
	 	<td>
			<span><strong><%= idDemande%></strong></span>
			
		<!-- <label><ct:FWLabel key="JSP_PC_CREANCIER_R_MONTANT_FAVEUR_ASSURE" />:</label>
			<span><strong>0</strong></span>
			<label><ct:FWLabel key="JSP_PC_CREANCIER_R_MONTANT_RETROACTIF" />: </label>
			<span><strong>0</strong></span> -->
		</td>
	</tr>
	 		<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
