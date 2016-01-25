<%@page import="globaz.framework.util.FWCurrency"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.pegasus.vb.dettecomptatcompense.PCDetteComptatCompenseViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	idEcran="PPC0089";
	PCDetteComptatCompenseViewBean viewBean = (PCDetteComptatCompenseViewBean) session.getAttribute("viewBean"); 
	
//	bButtonCancel = false;
//	bButtonValidate = bButtonValidate &&  controller.getSession().hasRight(IPCActions.ACTION_PRIX_CHAMBRE, FWSecureConstants.UPDATE);
	
	String idDroit=  viewBean.getIdDroit();
	String idVersionDroit=  viewBean.getIdVersionDroit();
	viewBean.setIdVersionDroit(idVersionDroit);

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
	<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
    <ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="noVersion" value="<%=viewBean.getNoVersion()%>"/>
	<ct:menuSetAllParams key="idDemandePc" value="<%=viewBean.getIdDemande()%>"/> 
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader() %>"/>
	<ct:menuSetAllParams key="idDecisionDac" value="<%=viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getIdDecisionApresCalcul() %>"/>
</ct:menuChange>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/DefaultTableAjax.js"/></script>

<script type="text/javascript" src="<%=rootPath %>/scripts/dettecomptatcompense/dettecomptatcompensePart.js"></script> 
<script type="text/javascript">
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
	var globazGlobal = {};
	globazGlobal.ACTION_AJAX = "<%=IPCActions.ACTION_DETTE_COMPTAT_COMPENSE_AJAX%>";
	globazGlobal.idDroit = "<%=idDroit%>"; 
	globazGlobal.idVersionDroit = "<%=idVersionDroit%>";
</script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/droit/droit.css"/>

<!-- <link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/home/typeChambre_de.css"/>-->

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
.mnt {
	width: 150px;
	text-align: right;
}

.infosMontantADisposition {
margin-bottom: 10px;
}
.requerant {
	padding: 5px 3px;
	margin: 0px 0px 10px 0px; 
 	 	/*background-color: #FEFCFF;
    border: 1px solid #9E9EC9;

    -webkit-border-radius: 5px;
    -moz-border-radius: 5px;
  	border-radius: 5px;
  box-shadow 
  	-webkit-box-shadow: rgba(49, 85, 244, 0.2) 0px 1px 3px;
    -moz-box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;
    box-shadow:rgba(49, 85, 244, 0.2) 0px 1px 3px;*/
}
</style>

<%-- tpl:insert attribute="zoneScripts" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_TITRE"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %> 
	<tr>
		<td colspan="2">
			<div class="area">
				<div class="requerant">
					<%=viewBean.getRequerantDetail() %>
				</div>
				<div class="ui-widget infosMontantADisposition">
					<h1 class="ui-widget-header "><ct:FWLabel key="JSP_PC_DETTE_CALCUL_MONTANT_A_DISPOSITION"/></h1>
					<div class="ui-widget-content">
						<table id="decompte">
							<tr>
								<td class="lable">
									<ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANT_CREANCIER"/>
								</td>
								<td class="mnt">
									<span id="montantCreance" class="data"><%= new FWCurrency(viewBean.getDecompte().getCreanciers().getTotal().toString()).toStringFormat() %></span>
								</td>
							</tr>
							<tr>
								<td class="lable">
									<ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANT_DETTE"/>
								</td>
								<td class="mnt">
									<span  id="montantDette"  class="data"><%= new FWCurrency(viewBean.getDecompte().getDettesCompta().getTotal().toString()).toStringFormat() %></span>
								</td>
							</tr>
							<tr>
								<td class="lable solde">
									<ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_TOTAL_DETTE"/>
								</td>
								<td class="solde mnt" >
									<span  id="montantDeduction"  class="data doubleLigne"><%= new FWCurrency(viewBean.getDecompte().getSousTotalDeduction().toString()).toStringFormat() %></span>
								</td>
							</tr>
							<tr>
								<td class="lable">
									<ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANT_RETRO"/>
								</td>
								<td class="mnt">
									<span  id="montantRetro"  class="data"><%= new FWCurrency(viewBean.getDecompte().getSousTotalPCA().toString()).toStringFormat() %></span>
								</td>
							</tr>
							<tr>
								<td class="lable">
									<ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANT_A_DISPOSITION"/>
								</td>
								<td class="mnt">
									<span  id="montantSold"  class="data doubleLigne bold"><%= new FWCurrency(viewBean.getDecompte().getTotal().toString()).toStringFormat() %></span>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<table class="areaTable" width="100%">
					<thead>
						<tr>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_ISCOMPENSE"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_DESCRIPTION"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANT"/></th>
						    <th class="notSortable"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANT_COMPENSE"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table> 
				
				<div class="areaDetail">
					<table id="formTableLess" > 
						<tr>
							<td>
								<label for="isCompense"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_ISCOMPENSE"/></label>
							</td>
							<td> 
								<input type="checkbox" id="isCompense"/></input>
								<input type="hidden" id="currentEntity.idDetteComptatCompense" value=""/></input>
							</td> 
							<td>
								<label for="currentEntity.montant"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_DESCRIPTION"/></label>
							</td>
							<td> 
								<span type="text" id="descriptionSection"/></span>
							</td> 
						</tr>
						<tr>
							<td>
								<label for="currentEntity.montant"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANT"/></label>
							</td>
							<td> 
								<span type="text" id="currentEntity.montant" data-g-amount=" "/>  </span>
							</td> 
							<td>
								<label for="currentEntity.montantModifie"><ct:FWLabel key="JSP_PC_DETTE_COMPENSE_L_MONTANTMODIFIE"/></label>
							</td>
							<td>
								<input type="text" id="currentEntity.montantModifie"  data-g-amount="mandatory:false" />
							</td>
						</tr>
					</table>
					
					<table>
						<tr> 
							<td colspan="6">
			
							<%if(viewBean.isDetteEditable()){%>
								<%@ include file="/theme/detail_ajax/capageButtons.jspf" %>
							<%}%>
							
							</td>
						</tr>
					</table>
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
