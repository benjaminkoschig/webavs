<%@page import="globaz.auriga.vb.renouvellementdecisionmasse.AURenouvellementDecisionMasseViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.naos.business.service.AffiliationService"%>
<%@page language="java" contentType="text/html;charset=ISO-8859-1"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>

<%@include file="/theme/detail_ajax/header.jspf"%>

<%-- tpl:insert attribute="zoneInit" --%>
<%
	AURenouvellementDecisionMasseViewBean viewBean = (AURenouvellementDecisionMasseViewBean) session.getAttribute(FWServlet.VIEWBEAN);
	String userAction = "auriga.renouvellementdecisionmasse.renouvellementDecisionMasse.executer";
	
	//Le code ci-dessous sert uniquement à ne pas afficher null dans le champ numeroPassage lorsqu'il n'y a pas de passage sélectionné
	String theInfoPassage =  "";
	if(!JadeStringUtil.isBlankOrZero(viewBean.getNumeroPassage())){
		theInfoPassage = theInfoPassage + viewBean.getNumeroPassage() + " "; 	
	}
	
	if(!JadeStringUtil.isBlankOrZero(viewBean.getLibellePassage())){
		theInfoPassage = theInfoPassage + viewBean.getLibellePassage() ;
	}
			
	String processStarted = request.getParameter("process");
	boolean processLaunched = "launched".equalsIgnoreCase(processStarted);
%>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/javascripts.jspf"%>

<%-- tpl:insert attribute="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>

<script type="text/javascript" src="<%=servletContext%>/aurigaRoot/scripts/renouvellementdecisionmasse/renouvellementDecisionMasse_de.js"></script>
<script type="text/javascript">
	var processLauched = <%=processLaunched%>;
</script>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_AU_RENOUVELLEMENT_MASSE_DECISION_TITRE" />
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyStart2.jspf"%>

<%-- tpl:insert attribute="zoneMain" --%>
<input type="hidden" name="userAction" value="<%=userAction%>">
<tr>
	<td>
		<table border="0" cellspacing="0" cellpadding="5" width="100%">
			<tr>
				<td style="width: 175px" />
				<td style="width: 175px" />
				<td style="width: 20px" />
				<td style="width: 175px" />
				<td />
			</tr>
			
			<TR>
		      	<TD><ct:FWLabel key="JSP_AU_RENOUVELLEMENT_MASSE_DECISION_PASSAGE"/></TD>
		        <TD colspan="3"><INPUT type="text" name="infoPassage" class="libelleLongDisabled" readonly="readonly" value="<%=theInfoPassage%>"  >
		        
		        <%
				Object[] psgMethodsName = new Object[]{
		        				new String[]{"setNumeroPassage","getIdPassage"},
		        				new String[]{"setLibellePassage","getLibelle"}
		        };  
				Object[] psgParams= new Object[]{};	
				String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+ "/renouvellementdecisionmasse/renouvellementDecisionMasse_de.jsp";	
				%>
				
				<ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
				    <ct:FWSelectorTag 
						name="passageSelector" 
						methods="<%=psgMethodsName%>"
						providerPrefix="FA"			
						providerApplication ="musca"			
						providerAction ="musca.facturation.passage.chercher"			
						providerActionParams ="<%=psgParams%>"
						redirectUrl="<%=redirectUrl%>"
					/> 
				</ct:ifhasright>
				<input type="hidden" name="selectorName" value="">
		        </TD>
		        <td />	
			</TR>
			
			<tr>
				<td><label for="numeroAffilieDebut"><ct:FWLabel key="JSP_AU_RENOUVELLEMENT_MASSE_DECISION_NUMERO_AFFILIE_DEBUT" /></label></td>
				<td colspan="3">
					<ct:widget tabindex="3" name="numeroAffilieDebut" id="numeroAffilieDebut" styleClass="normal" >
						<ct:widgetService methodName="widgetFind" className="<%=AffiliationService.class.getName()%>" defaultSearchSize="20">
							<ct:widgetCriteria criteria="likeNumeroAffilie" label="CRITERIA_NUM_AFFILIE"/>
							<ct:widgetCriteria criteria="inTypeAffiliationString" label="CRITERIA_NUM_AFFILIE" fixedValue="<%=viewBean.getTypesAffForWidgetString()%>"/>
							<ct:widgetLineFormatter format="#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt} "/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										this.value=$(element).attr('affiliation.affilieNumero');
									}
								</script>										
							</ct:widgetJSReturnFunction>
						</ct:widgetService>			
					</ct:widget>
				</td>
				<td />
			</tr>
			
			<tr>
				<td><label for="numeroAffilieFin"><ct:FWLabel key="JSP_AU_RENOUVELLEMENT_MASSE_DECISION_NUMERO_AFFILIE_FIN" /></label></td>
				<td colspan="3">
					<ct:widget tabindex="3" name="numeroAffilieFin" id="numeroAffilieFin" styleClass="normal" >
						<ct:widgetService methodName="widgetFind" className="<%=AffiliationService.class.getName()%>" defaultSearchSize="20">
							<ct:widgetCriteria criteria="likeNumeroAffilie" label="CRITERIA_NUM_AFFILIE"/>
							<ct:widgetCriteria criteria="inTypeAffiliationString" label="CRITERIA_NUM_AFFILIE" fixedValue="<%=viewBean.getTypesAffForWidgetString()%>"/>
							<ct:widgetLineFormatter format="#{affiliation.affilieNumero} #{affiliation.raisonSocialeCourt} "/>
							<ct:widgetJSReturnFunction>
								<script type="text/javascript">
									function(element){
										this.value=$(element).attr('affiliation.affilieNumero');
									}
								</script>										
							</ct:widgetJSReturnFunction>
						</ct:widgetService>			
					</ct:widget>
				</td>
				<td />
			</tr>
			
			<tr>
				<td><label for="annee"><ct:FWLabel key="JSP_AU_RENOUVELLEMENT_MASSE_DECISION_ANNEE" /></label></td>
				<td colspan="3"><ct:inputText  name="annee" id="annee" notation="data-g-integer='mandatory:true,sizeMax:4'" /> </td>
				<td />
			</tr>
			
			<tr>
				<td><label for="email"><ct:FWLabel key="EMAIL" /></label></td>
				<td colspan="3"><ct:inputText name="email" id="email" notation="data-g-email='mandatory:true'" style="width: 200px" /></td>
				<td />
			</tr>
	
			<tr>
				<td colspan="5"><B><ct:FWLabel key="JSP_AU_RENOUVELLEMENT_MASSE_DECISION_CATEGORIE_A_IMPRIMER"/>&nbsp;<%=viewBean.getLibelleCategorieDecisionPrinted()%></B></td>
			</tr>
			<tr>
				<td />
				<td />
				<td />
				<td />
				<td align="right">
					<span id="boutonValider"><ct:FWLabel key="BOUTON_VALIDER" /></span>
				</td>
			</tr>
		</table>
	</td>
</tr>
<% if (processLaunched) { %>
<tr>
	<td style="height: 2em; color: white; font-weight: bold; text-align: center; background-color: green"><ct:FWLabel key="FW_PROCESS_STARTED" /></td>
</tr>
<% } %>
<%-- /tpl:insert --%>

<%@include file="/theme/detail_ajax/bodyButtons.jspf"%>
<%@include file="/theme/detail_ajax/bodyErrors.jspf"%>
<%@include file="/theme/detail_ajax/footer.jspf"%>
