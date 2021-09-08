<%@page import="ch.globaz.pegasus.business.models.pcaccordee.PCAccordee"%>
<%@page import="ch.globaz.pegasus.business.models.creancier.SimpleCreancier"%>
<%@page import="ch.globaz.pegasus.business.vo.decompte.PCAccordeeDecompteVO"%>
<%@page import="ch.globaz.pegasus.business.vo.decompte.DetteEnComptaVO"%>
<%@page import="ch.globaz.pegasus.business.vo.decompte.CreancierVO"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense"%>
<%@page import="ch.globaz.pegasus.business.domaine.ListTotal"%>
<%@page import="globaz.pegasus.vb.decision.PCDecomptViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.dettecomptatcompense.PCDetteComptatCompenseViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	idEcran="PPC0088";
	PCDecomptViewBean viewBean = (PCDecomptViewBean) session.getAttribute("viewBean"); 
	
//	bButtonCancel = false;
//	bButtonValidate = bButtonValidate &&  controller.getSession().hasRight(IPCActions.ACTION_PRIX_CHAMBRE, FWSecureConstants.UPDATE);
	
	String idDroit=JadeStringUtil.toNotNullString(request.getParameter("idDroit"));
	String idVersionDroit=JadeStringUtil.toNotNullString(request.getParameter("idVersionDroit"));
	viewBean.setIdVersionDroit(idVersionDroit);

	String labelSold = "";
	if(viewBean.getDecompte().getTotal().signum() != -1) {
		labelSold = objSession.getLabel("JSP_PC_DECOMPTE_L_SOLDE");
	} else {
		labelSold = objSession.getLabel("JSP_PC_DECOMPTE_L_SOLDE_NEGATIF");
	}
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdecision">
	<%if (!viewBean.isDecisionReadyForValidation()) {%>
		<ct:menuActivateNode active="no" nodeId="VALIDATIONS"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="VALIDATIONS"/>
	<%}%> 
		<%if (viewBean.hasDecompte()) {%>
		<ct:menuActivateNode active="yes" nodeId="DECOMPTE"/>  
		<ct:menuActivateNode active="yes" nodeId="DETTE_EN_COMPTAT"/>
		<ct:menuActivateNode active="yes" nodeId="CREANCE_ACCORDEE"/>
	<%} else {%>
		<ct:menuActivateNode active="no" nodeId="DECOMPTE"/>  
		<ct:menuActivateNode active="no" nodeId="DETTE_EN_COMPTAT"/>
		<ct:menuActivateNode active="no" nodeId="CREANCE_ACCORDEE"/>
	<%}%> 
	<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
    <ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="noVersion" value="<%=viewBean.getNoVersion()%>"/>
	<ct:menuSetAllParams key="idDemandePc" value="<%=viewBean.getIdDemande()%>"/> 
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision() %>"/>
	<ct:menuSetAllParams key="idDecisionDac" value="<%=viewBean.getIdDecisionDac() %>"/>
</ct:menuChange>

<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>


<script type="text/javascript">
    var ID_VERSIONDROIT = '<%=viewBean.getIdVersionDroit()%>';
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var JSP_REMARQUE_SIZE_LIMIT_MESSAGE_INFO="<ct:FWLabel key='JSP_REMARQUE_SIZE_LIMIT_MESSAGE_INFO'/>";
	var globazGlobal = {};
	globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_DETTE_COMPTAT_COMPENSE_AJAX%>";
	globazGlobal.idDroit = "<%=idDroit%>"; 
	globazGlobal.idVersionDroit = "<%=idVersionDroit%>";
	$(function (){
		ajaxUtils.triggerStartNotation();
	});

</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/decision/decomptPart.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/droit.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/bootstrap.css"/>

<style>
input{
	margin : 0;
	padding: 0;
}

label, input, select {
	display: inline-block;
	margin: 2px;
}
label
{
	padding-right: 0.5%;
	margin-left :30px;
	color: gray;
    font-weight: bold;
}

.solde {
	padding-bottom: 10px;
}

.doubleLigne{
	border-top: 3px double  #9E9EC9; 

}
.bold {
	font-weight: bold;
}

.infosMontantADisposition {
margin-bottom: 10px;
}
.requerant {
	    margin: 0px 0px 10px 0px; 
 	 	background-color: #FEFCFF;
    border: 1px solid #9E9EC9;

    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
  	border-radius: 5px;
  box-shadow 
  	-webkit-box-shadow: rgba(49, 85, 244, 0.2) 0px 1px 3px;
    -moz-box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;
    box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;
}
h1 {
	margin:0px;
	padding:0px;
	
	font-size: 1.2em;
}
h2 {
	font-size: 1em;
}

.gl-area {
	padding:0 !important;
}

.gl-header {
	font-weight: bold;
	background-color: #FBF9FF;
	border-bottom: 2px dotted #E6E6E6;
	color: #445F85;
	margin: 12px 0px 8px 0px;
	font-size: 14px;
	padding:3px;
}

.gl-pre-content {
	margin:5px 5px 10px 5px;
	padding: 0;
	
}

.gl-content {
	margin:  0;
	padding-left: 20px;
	background-color: #FFFFFF;
	color: #222222;
}

.gl-content .ligne:FIRST-CHILD {
	border-top: none;
}


.ligne {
	border-top: 1px solid #FBF9FF;
	vertical-align: middle;
}



.decription{
	padding-left: 0px;
}
.mnt{
	text-align: right;
	padding-right: 15px;
}

.sub-total {
	border-top: #445F85 solid 1px;
	font-style: italic;
	font-weight: bold;
	color: #445F85;
	padding-right: 15px;
}

.sub-totalDesc {
	border-top: #CCD0D5 solid 1px;
	font-style: italic;
	font-weight: bold;
	color: #445F85;
}

.total{
	border-top: #CCD0D5 double 3px;
	font-weight: bold;
	color: #445F85;
	padding-right: 15px;
	font-size: 14px;
}

.totalGlobal {
	border-top: #CCD0D5 double 3px;
	font-weight: bold;
	color: #445F85;
	font-size: 14px;
}

.row-fluid [class*="span"] {
    min-height: 20px !important;
}

.totalDesc{
	border-top: #CCD0D5 double 3px;
	font-weight: bold;
	color: #445F85;
}

.descTiers {
	font-size: 0.9 em;
	padding-left: 1em;
	color: #696060;
}

.descLeft {
	padding-left: 1em;
}


.bottom {
	margin-top: 1.4em;
}

</style>

<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<ct:FWLabel key="JSP_PC_DECOMPTE_L_TITRE"/>

<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %> 
	<tr>
		<td colspan="2" class="">
			
			<div class="requerant">
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_INFOS_REQUERANT"/></h1>
				<%=viewBean.getRequerantDetail() %>
			</div>
			<div class="likeMainContainerAjax gl-area" >
				<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_DECOMPTE_L_DECOMPTE"/></h1>
				<div class="gl-pre-content" style="background-color: #FFFFFF;" >
					<div class="gl-header"><ct:FWLabel key="JSP_PC_DECOMPTE_L_PCA"/></div>
					<div class="gl-content">
						<%for(PCAccordeeDecompteVO pca: viewBean.getDecompte().getPeriodesPca().getList() ) {%>
							<div class="ligne">
								<div class="row-fluid">
									<div class="span5 decription"> <%=pca.getDateDebutPeriode() +" - "+ pca.getDateFinPeriode() %> 
										<br />  <span class="descTiers"><%= pca.getDescTiers() %></span> 
										<%if(pca.hasJourAppoint()){%>
											<br />
											<span class="descLeft">
												<ct:FWLabel key="JSP_PC_DECOMPTE_L_COMPLEMENT"/></span> 
												<span><%=pca.getSimpleJoursAppoint().getDateEntreHome().substring(3)%></span>
											</span>
										<%}%>
									</div>
																		
									<div class="span3 mnt bottom">
										<span><%=pca.getNbreMois()%> <ct:FWLabel key="JSP_PC_DECOMPTE_L_MOIS_A"/></span> 	
										<span><%=new FWCurrency(pca.getMontantPcaMensuel().toString()).toStringFormat()%></span>
										<%if(pca.hasJourAppoint()){%>
											<br />
											<span><%=pca.getSimpleJoursAppoint().getNbrJoursAppoint()%> 
											<span class=""><ct:FWLabel key="JSP_PC_DECOMPTE_L_JOUR" /> </span>	
											<span><%=new FWCurrency(pca.getSimpleJoursAppoint().getMontantJournalier())%></span>
										<%}%>
									</div>
									<div class="mnt span2 bottom">
										<%=new FWCurrency(pca.getMontantForPeriod().toString()).toStringFormat()%>
										<%if(pca.hasJourAppoint()){%>
											<br />											
											<span><%=pca.getMontantTotalJourAppoint()%></span>
										<%}%>
									</div>
									
									<div class="span2"></div>
								</div>
							</div>
							
						<%}%>
						<div class="ligneTotal">
							<div class="row-fluid">
								<div class="span8 sub-totalDesc"> <ct:FWLabel key="JSP_PC_DECOMPTE_L_TOTAL"/></div>
								<div class="sub-total mnt span4"><%= new FWCurrency(viewBean.getDecompte().getPeriodesPca().getTotal().toString()).toStringFormat()%></div>
							</div>
						</div>
					</div>
					
					<%if(viewBean.getDecompte().getPrestationsVerses().getList().size()>0){ %>
						<div class="gl-header"><ct:FWLabel key="JSP_PC_DECOMPTE_L_PCA_VERSEES"/></div>
						<div class="gl-content">
						<%for(PCAccordeeDecompteVO pca: viewBean.getDecompte().getPrestationsVerses().getList() ) {%>
								<div class="ligne">
									<div class="row-fluid">
										<div class="span5 decription"><%=pca.getDateDebutPeriode() +" - "+ pca.getDateFinPeriode() %>
										<br />  <span class="descTiers"><%= pca.getDescTiers() %></span>
										<%if(pca.hasJourAppoint()){%>
											<br />
											<span class="descLeft">
												<ct:FWLabel key="JSP_PC_DECOMPTE_L_COMPLEMENT"/></span> 
												<span><%=pca.getSimpleJoursAppoint().getDateEntreHome().substring(3)%></span>
											</span>
										<%}%>
										</div>
										<div class="span3 mnt bottom" >
											<span><%=pca.getNbreMois()%> <ct:FWLabel key="JSP_PC_DECOMPTE_L_MOIS_A"/></span> 	
											<span><%=new FWCurrency(pca.getMontantPcaMensuel().toString()).toStringFormat()%></span>
										<%if(pca.hasJourAppoint()){%>
											<br />
											<span><%=pca.getSimpleJoursAppoint().getNbrJoursAppoint()%> 
											<span class=""><ct:FWLabel key="JSP_PC_DECOMPTE_L_JOUR" /> </span>	
											<span><%=new FWCurrency(pca.getSimpleJoursAppoint().getMontantJournalier())%></span>
										<%}%>
										</div>
										<div class="mnt span2 bottom">
											<%=new FWCurrency(pca.getMontantForPeriod().toString()).toStringFormat()%>
										<%if(pca.hasJourAppoint()){%>
											<br />											
											<span><%=pca.getMontantTotalJourAppoint()%></span>
										<%}%>
										</div>
										<div class="span2"></div>
									</div>
								</div>
							<%}%>
							<div class="ligne">
								<div class="row-fluid"> 
									<div class="span8 sub-totalDesc decription"><ct:FWLabel key="JSP_PC_DECOMPTE_L_TOTAL"/></div>
									<div class="mnt sub-totalDesc span4"> -<%=new FWCurrency(viewBean.getDecompte().getPrestationsVerses().getTotal().toString()).toStringFormat()%> </div>
								</div>
							</div>
						</div>
					<%}%>
					
					<%if(viewBean.getDecompte().getCreanciers().getList().size()>0){ %>
						<div class="gl-header"><ct:FWLabel key="JSP_PC_DECOMPTE_L_CREANCIER"/></div>
						<div class="gl-content">
						<%for(CreancierVO creancier: viewBean.getDecompte().getCreanciers().getList()) {%>
							<div class="ligne">
								<div class="row-fluid">
										<div class="span8 decription"> <%=creancier.getDescription()%></div>
										<div class="mnt span2"><%=new FWCurrency(creancier.getMontantVerse().toString()).toStringFormat()%></div>
										<div class="span2"></div>
								</div>
							</div>
						<%}%>
						<div class="ligneTotal">
							<div class="row-fluid "> 
								<div class="span8 sub-totalDesc" ><ct:FWLabel key="JSP_PC_DECOMPTE_L_TOTAL"/></div>
								<div class="sub-total mnt span4"><%=viewBean.formatTotal(new FWCurrency(viewBean.getDecompte().getCreanciers().getTotal().toString()))%></div>
							</div>
						</div>
						</div>
					<%}%>
					
					<%if(viewBean.getDette().getList().size()>0){ %>
						<div class="gl-header"><ct:FWLabel key="JSP_PC_DECOMPTE_L_DETTE"/></div>
						<div class="gl-content">
						<%for(DetteEnComptaVO dette:viewBean.getDecompte().getDettesCompta().getList() ) {%>
						
							<div class="ligne">
								<div class="row-fluid">
									<div class="span8 decription"> <%=dette.getDescription()%></div>
									<div class="mnt span2"><%=new FWCurrency((JadeStringUtil.isBlankOrZero(dette.getDette().getMontantModifie()))?dette.getDette().getMontant():dette.getDette().getMontantModifie()).toStringFormat()%></div>
									<div class="span2"></div>
								</div>
							</div>
						<%}%>
						<div class="ligneTotal">
							<div class="row-fluid">
								<div class="span8 sub-totalDesc" ><ct:FWLabel key="JSP_PC_DECOMPTE_L_TOTAL"/></div>
								<div class="sub-total mnt span4">-<%= new FWCurrency(viewBean.getDecompte().getDettesCompta().getTotal().toString()).toStringFormat()%></div>
							</div>
						</div>
						</div>
					<%}%>
					
					<%if(viewBean.hasAllocationNoel()){ %>
						<div class="gl-header"><ct:FWLabel key="JSP_PC_DECOMPTE_L_ALLOCATION_NOEL"/></div>
						<div class="gl-content">
							<div class="ligne">
								<div class="row-fluid">
									
									<div class="span8 decription">
										<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_MONTANT_TOTAL"/></label><span class="value"><%=viewBean.getMontantAllocationNoel()%></span>
							    		<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_NB_PERSONNE"/></label><span class="value"><%=viewBean.getNbPersonneAllocationNoel()%></span>
									</div>
									<div class="mnt span2"><%=viewBean.getMontantTotalAllocationNoel()%></div>
									<div class="span2"></div>
								</div>
							</div>
				
						<div class="ligneTotal">
							<div class="row-fluid">
								<div class="span8 sub-totalDesc" ><ct:FWLabel key="JSP_PC_DECOMPTE_L_TOTAL"/></div>
								<div class="sub-total mnt span4"><%=viewBean.getMontantTotalAllocationNoel()%></div>
							</div>
						</div>
						</div>
					<%}%>
					
					<div style="margin: 20px 0px 5px 5px; color: #445F85; font-size: 16px; font-weight: bold; background-color: #FBF9FF;">
						<%=labelSold%>
					</div>
					<div class="gl-content">
						<div class="row-fluid">
							<div class="totalGlobal span8 decription"  style="padding-left: 5px;"><ct:FWLabel key="JSP_PC_DECOMPTE_L_TOTAL"/></div>
							<div class="total mnt span4"><%= new FWCurrency(viewBean.getDecompte().getTotal().toString()).toStringFormat()%></div>
						</div>
					</div>
					
					<div class="gl-header"><ct:FWLabel key="JSP_PC_DECOMPTE_L_REMARQUE"/></div>
					<div class="gl-content">
						<div class="row-fluid">
							<table style="width: 100%;">
							<tr>
							<td style="width: 95%;">
							<textarea name="remarqueDecompte" id="remarqueDecompte"  style="width: 100%;" rows="6" onkeyup="return imposeMaxLength(this);" ><%=viewBean.getRemarqueDecompte()%></textarea>
							</td>
							<td>
							<button  name="remarqueButton" id="remarqueButton" type="button" class="ui-incon orange ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only" style="width: 2.4em; height: 2em; margin-left: 2em;vertical-align: middle;">
							<span name="remarqueButtonSpan" id="remarqueButtonSpan"   class="ui-icon ui-icon-disk ui-button-icon-primary"></span> </button>	
							
							</td>
							</tr>
							</table>
							
						</div>
					</div>
					<div class="gl-content">
						<div class="row-fluid">
							<span name="remarqueSizeLimit" id="remarqueSizeLimit" style="color: orange; display: none;">*<ct:FWLabel key="JSP_REMARQUE_SIZE_LIMIT_MESSAGE_INFO"/></span>
						</div>
					</div>
				</div>
			</div>
		</td>
	</tr>


						<%-- tpl:insert attribute="zoneMain" --%>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
