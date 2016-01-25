<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.aries.business.constantes.ARDetailDecisionType"%>
<%@page import="globaz.aries.vb.decisioncgas.ARDecisionCgasViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page language="java" contentType="text/html;charset=ISO-8859-1"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
ARDecisionCgasViewBean viewBean = (ARDecisionCgasViewBean) request.getAttribute(FWServlet.VIEWBEAN);

String attributsHtmlTypeDecision = "";
if(viewBean.isTypeDecisionEnLectureSeule()){
	attributsHtmlTypeDecision = " class='alwaysDisabled' readonly='readonly' disabled='disabled' "; 
}

%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/ariesAurigaCommon/scripts/decision/defaultDetailAjaxDecision.js"></script>
<script type="text/javascript" src="<%=servletContext%>/ariesRoot/scripts/decisionCgas_part.js"></script>

<SCRIPT language="JavaScript">

 globazGlobal.selectedID = <%=viewBean.getId()%>  ;
 
 

 $(function () {
	<%if(!JadeStringUtil.isBlankOrZero(viewBean.getWarningAucunPassageDecisionCGASOuvert())){%>
		globazNotation.utils.consoleWarn("<%=viewBean.getWarningAucunPassageDecisionCGASOuvert()%>",'Avertissement',true);
	<%}%>
});

</SCRIPT>

<style type="text/css">
	
	.montant{
    	width : 200px;
	}
	
	
	
</style>


<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>

<span class="postItIcon" data-g-note="idExterne:<%=viewBean.getId()%>, tableSource:ARDECI"></span>

<ct:FWLabel key="JSP_AR_DECISION_TITRE" />

<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>

<%-- tpl:insert attribute="zoneMain" --%>

<tr>
	<td clospan="6">
		<div class="area">
			<table>
				<tr>
					<td width="100px">
						<label for="forNumAffilie"><ct:FWLabel key="JSP_AR_DECISION_AFFILIE" /></label>
					</td>
					<td>
						<ct:inputText name="numAffilie" id="numAffilie" readonly="readonly" disabled="disabled"/>
						<ct:inputText name="nomAffilie" id="nomAffilie" style="width: 350px;" readonly="readonly" disabled="disabled"/>
						<ct:inputText name="typeAffilieLibelle" id="typeAffilieLibelle" readonly="readonly" disabled="disabled"/>
					</td>
				</tr>
			</table>
			<div  class="areaDetail">
		
					<div  class="inputsNotCleared">
						<ct:inputHidden id="decisionCGASBean.decisionCGAS.idAffiliation" name="decisionCGASBean.decisionCGAS.idAffiliation" defaultValue="<%=viewBean.getIdAffiliation()%>"/>
						<ct:inputHidden name="decisionCGASBean.culturePlaine.type" id="decisionCGASBean.culturePlaine.type" defaultValue="<%=ARDetailDecisionType.CULTURE_PLAINE.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.cultureArboricole.type" id="decisionCGASBean.cultureArboricole.type" defaultValue="<%=ARDetailDecisionType.CULTURE_ARBORICOLE.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.cultureMaraichere.type" id="decisionCGASBean.cultureMaraichere.type" defaultValue="<%=ARDetailDecisionType.CULTURE_MARAICHERE.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.vigneNordCanton.type" id="decisionCGASBean.vigneNordCanton.type" defaultValue="<%=ARDetailDecisionType.VIGNE_NORD_CANTON.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.vigneEstCanton.type" id="decisionCGASBean.vigneEstCanton.type" defaultValue="<%=ARDetailDecisionType.VIGNE_EST_CANTON.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.vigneLaCote.type" id="decisionCGASBean.vigneLaCote.type" defaultValue="<%=ARDetailDecisionType.VIGNE_LA_COTE.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.ugbPlaine.type" id="decisionCGASBean.ugbPlaine.type" defaultValue="<%=ARDetailDecisionType.UGB_PLAINE.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.ugbMontagne.type" id="decisionCGASBean.ugbMontagne.type" defaultValue="<%=ARDetailDecisionType.UGB_MONTAGNE.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.ugbSpecial.type" id="decisionCGASBean.ugbSpecial.type" defaultValue="<%=ARDetailDecisionType.UGB_SPECIAL.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.alpage.type" id="decisionCGASBean.alpage.type" defaultValue="<%=ARDetailDecisionType.ALPAGE.getCodeSystem()%>" />
						<ct:inputHidden name="decisionCGASBean.decisionCGAS.idDecision" id="decisionCGASBean.decisionCGAS.idDecision" defaultValue="<%=viewBean.getId()%>"/>
						<ct:inputHidden name="decisionCGASBean.decisionCGAS.idDecisionRectifiee" id="decisionCGASBean.decisionCGAS.idDecisionRectifiee" defaultValue="<%=viewBean.getIdDecisionCgasRectifiee()%>"/>
					</div>				
					
					<table border="0" cellspacing="0" cellpadding="5" width="100%">
									
						<tr >
							<td><label for="decisionCGASBean.decisionCGAS.exempte"><ct:FWLabel key="JSP_AR_DECISION_EXEMPTE" /></label></td>
							<td><input name="decisionCGASBean.decisionCGAS.exempte" id="decisionCGASBean.decisionCGAS.exempte" type="checkbox" /></td>
						</tr>
						
						<tr >
							<td><label for="decisionCGASBean.decisionCGAS.type"><ct:FWLabel key="JSP_AR_DECISION_TYPE" /></label></td>
							<td><ct:FWCodeSelectTag  name="decisionCGASBean.decisionCGAS.type" defaut="<%=viewBean.getTypeDecision() %>" except="<%=viewBean.getHashSetTypeDecisionExcept()%>" codeType="ARTYPEDECI" wantBlank="false" notation="<%=attributsHtmlTypeDecision%>"  /></td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.decisionCGAS.annee"><ct:FWLabel key="JSP_AR_DECISION_ANNEE" /></label></td>
							<td><ct:inputText  name="decisionCGASBean.decisionCGAS.annee" id="decisionCGASBean.decisionCGAS.annee" notation="data-g-integer='mandatory:true,sizeMax:4'" style="width: 45px;" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.decisionCGAS.dateDebut"><ct:FWLabel key="JSP_AR_DECISION_PERIODE" /></label></td>
							<td>
								<ct:inputText  name="decisionCGASBean.decisionCGAS.dateDebut" id="decisionCGASBean.decisionCGAS.dateDebut" notation="data-g-calendar='mandatory:true'" /> &nbsp; &nbsp;
								<ct:inputText  name="decisionCGASBean.decisionCGAS.dateFin" id="decisionCGASBean.decisionCGAS.dateFin" notation="data-g-calendar='mandatory:true'" /> 
							</td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.decisionCGAS.dateDonnees"><ct:FWLabel key="JSP_AR_DECISION_DATE_DECISION" /></label></td>
							<td><ct:inputText  name="decisionCGASBean.decisionCGAS.dateDonnees" id="decisionCGASBean.decisionCGAS.dateDonnees" notation="data-g-calendar='mandatory:true'"  defaultValue="<%=viewBean.getDateFacturationPassage()%>" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.decisionCGAS.idPassageFacturation"><ct:FWLabel key="JSP_AR_DECISION_JOURNAL_FACTURATION" /></label></td>
							<td><ct:inputText  styleClass="alwaysDisabled"  name="decisionCGASBean.decisionCGAS.idPassageFacturation" id="decisionCGASBean.decisionCGAS.idPassageFacturation"  disabled="disabled" readonly="readonly" defaultValue="<%=viewBean.getIdPassageFacturation()%>" style="width: 100px"/> </td>
						</tr>
						
														
				</table>
				<br>		
				<table border="0" cellspacing="0" cellpadding="2" width="100%">
										
					<thead>
						<tr>
							<th><ct:FWLabel key="JSP_AR_DECISION_LIBELLE" /></th>
							<th><ct:FWLabel key="JSP_AR_DECISION_NOMBRE" /></th>
							<th><ct:FWLabel key="JSP_AR_DECISION_MONTANT_UNITAIRE" /></th>
							<th><ct:FWLabel key="JSP_AR_DECISION_MONTANT_TOTAL" /></th>	
						</tr>
					</thead>
							
						
						<tr>
							<td><b><ct:FWLabel key="JSP_AR_DECISION_CULTURE" /></b></td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.culturePlaine.nombre"><ct:FWLabel key="JSP_AR_DECISION_CULTURE_PLAINE" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.culturePlaine.nombre" id="decisionCGASBean.culturePlaine.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.culturePlaine.montant" id="decisionCGASBean.culturePlaine.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled" name="decisionCGASBean.culturePlaine.total" id="decisionCGASBean.culturePlaine.total"   disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.cultureArboricole.nombre"><ct:FWLabel key="JSP_AR_DECISION_CULTURE_ARBORICOLE" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.cultureArboricole.nombre" id="decisionCGASBean.cultureArboricole.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.cultureArboricole.montant" id="decisionCGASBean.cultureArboricole.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.cultureArboricole.total" id="decisionCGASBean.cultureArboricole.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.cultureMaraichere.nombre"><ct:FWLabel key="JSP_AR_DECISION_CULTURE_MARAICHERE" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.cultureMaraichere.nombre" id="decisionCGASBean.cultureMaraichere.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.cultureMaraichere.montant" id="decisionCGASBean.cultureMaraichere.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.cultureMaraichere.total" id="decisionCGASBean.cultureMaraichere.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						
						<tr>
							<td><b><ct:FWLabel key="JSP_AR_DECISION_VIGNE" /></b></td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.vigneNordCanton.nombre"><ct:FWLabel key="JSP_AR_DECISION_VIGNE_NORD_CANTON" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.vigneNordCanton.nombre" id="decisionCGASBean.vigneNordCanton.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.vigneNordCanton.montant" id="decisionCGASBean.vigneNordCanton.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.vigneNordCanton.total" id="decisionCGASBean.vigneNordCanton.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.vigneEstCanton.nombre"><ct:FWLabel key="JSP_AR_DECISION_VIGNE_EST_CANTON" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.vigneEstCanton.nombre" id="decisionCGASBean.vigneEstCanton.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.vigneEstCanton.montant" id="decisionCGASBean.vigneEstCanton.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.vigneEstCanton.total" id="decisionCGASBean.vigneEstCanton.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.vigneLaCote.nombre"><ct:FWLabel key="JSP_AR_DECISION_VIGNE_LA_COTE" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.vigneLaCote.nombre" id="decisionCGASBean.vigneLaCote.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.vigneLaCote.montant" id="decisionCGASBean.vigneLaCote.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.vigneLaCote.total" id="decisionCGASBean.vigneLaCote.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						
						<tr>
							<td><b><ct:FWLabel key="JSP_AR_DECISION_UGB" /></b></td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.ugbPlaine.nombre"><ct:FWLabel key="JSP_AR_DECISION_UGB_PLAINE" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.ugbPlaine.nombre" id="decisionCGASBean.ugbPlaine.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.ugbPlaine.montant" id="decisionCGASBean.ugbPlaine.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.ugbPlaine.total" id="decisionCGASBean.ugbPlaine.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.ugbMontagne.nombre"><ct:FWLabel key="JSP_AR_DECISION_UGB_MONTAGNE" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.ugbMontagne.nombre" id="decisionCGASBean.ugbMontagne.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.ugbMontagne.montant" id="decisionCGASBean.ugbMontagne.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.ugbMontagne.total" id="decisionCGASBean.ugbMontagne.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td><label for="decisionCGASBean.ugbSpecial.nombre"><ct:FWLabel key="JSP_AR_DECISION_UGB_ELVAGE_SPECIAL" /></label></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.ugbSpecial.nombre" id="decisionCGASBean.ugbSpecial.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.ugbSpecial.montant" id="decisionCGASBean.ugbSpecial.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.ugbSpecial.total" id="decisionCGASBean.ugbSpecial.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
						
						<tr>
							<td><b><label for="decisionCGASBean.alpage.nombre"><ct:FWLabel key="JSP_AR_DECISION_ALPAGE" /></label></b></td>
							<td align="center" ><ct:inputText  name="decisionCGASBean.alpage.nombre" id="decisionCGASBean.alpage.nombre"    notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.alpage.montant" id="decisionCGASBean.alpage.montant"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
							<td align="center"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.alpage.total" id="decisionCGASBean.alpage.total"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" /> </td>
						</tr>
						
						<tr>
							<td colspan="4">&nbsp;</td>
						</tr>
				</table>
				<br>
				<table width="100%">
					<tr>
	                	<td colspan="2">
	                       <hr size="0.1">
	                    </td>
		            </tr>
					<tr>
						<td width="150px"><b><label for="decisionCGASBean.decisionCGAS.cotisationPeriode"><ct:FWLabel key="JSP_AR_DECISION_COTISATION_PERIODE" /></label></b></td>
						<td align="left"><ct:inputText styleClass="alwaysDisabled"  name="decisionCGASBean.decisionCGAS.cotisationPeriode" id="decisionCGASBean.decisionCGAS.cotisationPeriode"  disabled="disabled" readonly="readonly" notation="data-g-amount='blankAsZero:false'" />
							<!--impression document -->
							<a id="printDoc" data-g-download="docType:pdf,
							serviceClassName:ch.globaz.aries.business.services.DecisionCGASService,
							dynParametres:getParamDynamique,
							serviceMethodName:printDecision, 
							docName:decisioncgas_,
							displayOnlyImage:false"><ct:FWLabel key="JSP_AR_DECISION_PRINT" /></a>
						</td>
					</tr>
				</table>
				<br>
				<%if(!viewBean.isLectureSeule()){%>
					<%@include file="/ariesAurigaCommon/decision/decisionButtons.jspf"%>
				<%}%>
		</div>
	</td>
</tr>
<%-- /tpl:insert --%>
<%@include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%@include file="/theme/detail_ajax/footer.jspf"%>