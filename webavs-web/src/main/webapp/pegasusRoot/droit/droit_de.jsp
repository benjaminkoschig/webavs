	<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		

<%@page import="globaz.pegasus.vb.droit.PCDroitViewBean"%>

<%@page import="ch.globaz.pegasus.business.models.droit.Droit"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>

<%@page import="globaz.pegasus.utils.PCDroitHandler"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%><script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/droit/droit_de.js"/></script>

<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DRO_D"
	idEcran="PPC0005";

	PCDroitViewBean viewBean = (PCDroitViewBean) session.getAttribute("viewBean");
	Droit droitCourant=viewBean.getDroit();
	PersonneEtendueComplexModel personne=droitCourant.getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
	
	autoShowErrorPopup = true;
	
	bButtonNew      = false;
	bButtonUpdate   = viewBean.isUpdatable();
	bButtonValidate = true;
	bButtonCancel 	= true;	
	bButtonDelete   = viewBean.isUpdatable();
	userActionDel="pegasus.droit.droit.supprimer";
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">
var ACTION_DROIT="<%=IPCActions.ACTION_DROIT%>";
var motif = "<%=droitCourant.getSimpleVersionDroit().getCsMotif()%>"
function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
    	var form=document.forms[0];
        form.elements('userAction').value="<%=IPCActions.ACTION_DROIT%>.supprimer";
        var champIdVersionDroit=$('<input type="hidden"/>');
        champIdVersionDroit.attr({
        	name:"idVersionDroit",
        	value:"<%=droitCourant.getSimpleVersionDroit().getId() %>"
        });
        champIdVersionDroit.appendTo(form);
        form.submit();
    }
}

function validate() {
    state = true;
    var form=document.forms[0];
    form.elements('userAction').value=ACTION_DROIT+".modifierDateAnnonce";
    var champIdVersionDroit=$('<input type="hidden"/>');
    champIdVersionDroit.attr({
    	name:"idVersionDroit",
    	value:"<%=droitCourant.getSimpleVersionDroit().getId() %>"
    });
    champIdVersionDroit.appendTo(form);
    
    return state;
}    
$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});

	//if(motif == '0' ){$('[name=csMotif]').val('64063002')}
	
});

</script>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdroitsdetail">
     	<ct:menuSetAllParams key="selectedId" value="<%=droitCourant.getSimpleDroit().getId()%>"/>
    	<ct:menuSetAllParams key="idDemandePc" value="<%=droitCourant.getSimpleDroit().getIdDemandePC()%>"/>
    	<ct:menuSetAllParams key="idDroit" value="<%=droitCourant.getSimpleDroit().getIdDroit()%>"/>
    	<ct:menuSetAllParams key="noVersion" value="<%=droitCourant.getSimpleVersionDroit().getNoVersion()%>"/>
    	<ct:menuSetAllParams key="idVersionDroit" value="<%=droitCourant.getSimpleVersionDroit().getIdVersionDroit()%>"/>
    	<!--  Uniquement pour la version du droit validee si il nexiste pas une autre version "exploitable" -->
    	<% if(!PCDroitHandler.isVersionDroitCorrigeable(droitCourant)) {%>
    		<ct:menuActivateNode nodeId="pcDroitCorriger" active="no"/>
    	<% }else{ %>
    		<ct:menuActivateNode nodeId="pcDroitCorriger" active="yes"/>
    	<% } %>
		<!-- les version de droit qui sont dans les etats enregistre, au caucul ou calcule peuvent etre supprimes -->
    	<% if(!PCDroitHandler.isVersionDroitSupprimable(droitCourant.getSimpleVersionDroit().getCsEtatDroit())) {%>
    		<ct:menuActivateNode nodeId="pcDroitSupprimer" active="no"/>
    	<% }else{ %>
    		<ct:menuActivateNode nodeId="pcDroitSupprimer" active="yes"/>
    	<% }%>
    	<!--  Si l'état pas octroi ou revision on cache le menu -->
 		<% if(!viewBean.getReadyForDecisionSuppression()){ %>
 			<ct:menuActivateNode nodeId="DECSUPP" active="no"/>
 		<% }else{ %>
 			<ct:menuActivateNode nodeId="DECSUPP" active="yes"/>
 			<ct:menuSetAllParams key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_SUPPRESSION_SC %>"/>
 		<% } %>
 		<!--  Si l'état pas calcule  le noeud decision apres calcul -->
 		<% if(!viewBean.getReadyForDecisionApresCalcul()){ %>
 			<ct:menuActivateNode nodeId="DECAPCA" active="no"/>
 		<% } else{ %>	
 			<ct:menuActivateNode nodeId="DECAPCA" active="yes"/>
 			<ct:menuSetAllParams key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_OCTROI_AC %>"/>
 		<% } %>
		<!--  Si le droit n'est pas en état calculé, au calcul ou enregistré, cacher l'option calculer 
			  On ne donne pas la possibilité de calculer un droit d'adaptation car le calcule doit être executer depuis l'écran de l'adaptation
		-->
		<% if(viewBean.getReadyForCalcul() && !IPCDroits.CS_MOTIF_DROIT_ADAPTATION.equals(viewBean.getDroit().getSimpleVersionDroit().getCsMotif())){%>
		 	<ct:menuActivateNode nodeId="pcDroitCalcul" active="yes"/>
		<%}else{%>
 			<ct:menuActivateNode nodeId="pcDroitCalcul" active="no"/>
 		<% } %>
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon" data-g-note="idExterne:<%=droitCourant.getSimpleVersionDroit().getIdVersionDroit()%>, tableSource:PCVERDRO"></span>
			
			<ct:FWLabel key="JSP_PC_DRO_D_TITRE"/>
			
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colspan="6" align="center">
			<table width="90%">
				<TR>
					<TD><ct:FWLabel key="JSP_PC_DRO_D_NO_DROIT"/></TD>
					<TD>
					<input name="idDemandePc" type="hidden" value="<%=droitCourant.getSimpleDroit().getIdDemandePC()%>"/>
					<%=droitCourant.getId()%>
					</TD>
					</tr><tr>
					<TD><ct:FWLabel key="JSP_PC_DRO_D_NO_VERSION_DROIT"/></TD>
					<TD>
					<%=droitCourant.getSimpleVersionDroit().getNoVersion()%>
					</TD>
					</tr><tr>
					<TD><ct:FWLabel key="JSP_PC_DRO_D_DETAIL_ASSURE"/></TD>
					<TD>
					<%=PCUserHelper.getDetailAssure(objSession,personne)%>
					</TD>
					</tr><tr>
					<TD><ct:FWLabel key="JSP_PC_DRO_D_ETAT"/></TD>
					<TD>
					<%=objSession.getCodeLibelle(droitCourant.getSimpleVersionDroit().getCsEtatDroit())%>
					</TD>
					</tr><tr>
					<TD><ct:FWLabel key="JSP_PC_DRO_D_DATE_ANNONCE"/></TD>
					<TD>
					<input type="text" data-g-calendar=" " value="<%=droitCourant.getSimpleVersionDroit().getDateAnnonce()%>" name="dateAnnonce" />
					</TD>
					</tr><tr>
					<TD><ct:FWLabel key="JSP_PC_DRO_D_MOTIF"/></TD>
					<TD>
					<ct:FWCodeSelectTag codeType="<%=ch.globaz.pegasus.business.constantes.IPCDroits.CS_TYPE_MOTIF%>" name="csMotif" wantBlank="true" defaut="<%=droitCourant.getSimpleVersionDroit().getCsMotif()%>"/>
					</TD>
				</TR>
			</TABLE>
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