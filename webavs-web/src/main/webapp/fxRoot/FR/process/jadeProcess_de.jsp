<%@ include file="/theme/detail_ajax/header.jspf" %>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.fx.vb.process.FXJadeProcessViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="ch.globaz.jade.process.business.conf.JadeProcessStepBean"%>
<%@page import="ch.globaz.jade.process.business.enumProcess.JadeProcessStepStateEnum"%>
<%@page import="ch.globaz.jade.process.business.enumProcess.JadeProcessStateEnum"%>
<%@page import="ch.globaz.jade.process.business.enumProcess.JadeProcessLogInfoTypeEnum"%>
<%@page import="ch.globaz.jade.process.business.handler.JadeProcessStepHandler"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>


<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="??";
	String rootPath = servletContext+(mainServletPath+"Root");
	FXJadeProcessViewBean viewBean = (FXJadeProcessViewBean) session.getAttribute("viewBean");
	boolean isComputingPopulation = JadeProcessStateEnum.COMPUTING_POPULATION.equals(viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat());
	boolean isInit = JadeProcessStateEnum.INIT.equals(viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat());
	boolean displayCurrentTrTime = true;
	
	boolean hasRightNew = objSession.hasRight(userActionNew, FWSecureConstants.ADD);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/boxError.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/utilsTable.js"></script>

<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/jadeProcess.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/jadeTabs.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/jadeProperties.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/jadeProcessParameter.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jsnotation/technical/globazUpload.js"></script>

<link rel="stylesheet" type="text/css" href="<%=rootPath %>/css/porcess.css"/>

<%-- tpl:put name="zoneScripts" --%>
 
<style>
 #progressStep {
 	margin:10px;
 }
 
 #steps {
	margin-bottom:15px;
 	border: 1px solid #226194;
 	border-collapse:collapse;
 }
 
 #steps caption {
 	text-align: left;
 	font-weight: bold;
 }
  #steps td{
	padding: 1px 3px;
	height: 45px;
 }
 #steps .buton{
 	margin: 0 5px;
 }
 
 
 #tabs-1 .mainContainerAjax {
    background-color: transparent;
    border: 0px;
    margin-bottom:5px;
 }
 
 #process td{
	padding: 1px 3px;
	height: 27px;
 }
 #tabs .mainContainerAjax {
 	padding:0;
 }

#tabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
#steps tr .progressStep {cursor: pointer;}
#add_tab { cursor: pointer; } 
 .ui-tabs-panel.ui-widget-content{
 	background-image:  url('./theme/ajax/fonds.png')
 }
 li.ui-state-active.ui-tabs-selected{
  	background-image:  url('./theme/ajax/fonds.png')
 }
</style>
<script language="JavaScript">
	var ID_EXECUTE_PROCEESS = <%=viewBean.getProcessStep().getSimpleExecutionProcess().getIdExecutionProcess()%>;
	var S_KEY_PROCESS = "<%=viewBean.getKeyProcess()%>";
	var S_ACTION = "fx.process.jadeStepAjax" ;
	var B_PROCESS_ON_ERROR = <%=viewBean.getProcessStep().getSimpleExecutionProcess().getHasError().toString()%>;
	var S_RUNNING_STEP = '<%=JadeProcessStateEnum.RUNNING_STEP.toString()%>';
	//var S_PROCESS_ETAT_WAITING = '<%=JadeProcessStateEnum.WAITING_TO_RUN.toString()%>'
	var S_WAITING_TO_RUN = '<%=JadeProcessStateEnum.WAITING_TO_RUN.toString()%>';
	var S_POPULATION_CREATED = '<%=JadeProcessStateEnum.POPULATION_CREATED.toString()%>';

	var S_WAITING_FOR_VALIDATE = '<%=JadeProcessStateEnum.WAITING_FOR_VALIDATE.toString()%>';
	var S_WAITING_TO_EXECUTE_STEP  = '<%=JadeProcessStateEnum.WAITING_TO_EXECUTE_STEP.toString()%>';
	var S_INIT = '<%=JadeProcessStateEnum.INIT.toString()%>';
	var B_PROCESS_CURRENT_STEP_IS_EXECUTABLE = <%=viewBean.isCurrentStepExcutable()%>;
	var S_USER_ACTION_FOR_PARMETERS ="2";
	var S_ACTION_SPECIFIED ="<%=viewBean.getJadeProcessDefinitionBean().getUrlProperties()%>";
	var S_STATE_PROCESS = "<%=viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat()%>";
	var B_HAS_BUTTONS = "<%=viewBean.getStepDefinition().getHasButton()%>";
	
	var S_LOG_PROCESS_ERROR = "<%= JadeProcessLogInfoTypeEnum.PROCESS_ERROR%>";
	var S_LOG_PROCESS_WARNING= "<%= JadeProcessLogInfoTypeEnum.PROCESS_WARNING%>";

	function callBackUpload(data) {
		$("#PATH_CSV_TO_IMPORT").prop("disabled", false);
		$("#PATH_CSV_TO_IMPORT").val(data.path+"/"+data.fileName);
		$("#protocol").addClass("notSend");
	}
	function validate() {
		//initValidate();
		if($("#PATH_CSV_TO_IMPORT").val()== ""){
			//alert("<=viewBean.getSession().getLabel("JSP_RF_IMPORT_AVASAD_ERREUR_IMPORT")%>");

			alert("<ct:FWLabel key="JSP_RF_IMPORT_AVASAD_ERREUR_IMPORT"/>");

			return false;
		}else{
			return true;
		}
	}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<tr id="step" >		
			<td colspan="3">
				<div id="tabs">
					<ul>
						<li><a href="#tabs-1">Processus</a></li>
						<li><a href="fx?userAction=fx.process.jadeStep.afficher&byIdProcess=true&id=<%=viewBean.getProcessStep().getSimpleExecutionProcess().getIdExecutionProcess()%>" >Population</a></li>
					</ul>
					<div  id="tabs-1" style="background-image:  url('<%=servletContext%>/theme/ajax/fonds.png')">
						<div class="formTableLess">
						<%if(!JadeStringUtil.isEmpty(viewBean.getProcessStep().getSimpleExecutionProcess().getDescription())){%>
							<fieldset id="processDescription" class="ui-widget">
								<legend class="ui-widget-header ">Description</legend>
								<div class="content ui-widget-content">
									<span style="padding-left: 10px"><%=viewBean.getProcessStep().getSimpleExecutionProcess().getDescription()%></span>
								</div>
							</fieldset>
							<%}%>
						
							<fieldset id="process" class="ui-widget">
								<legend class="ui-widget-header ">Infos processus</legend>
								<div class="content ui-widget-content">
									<label>Nom:</label>
									<span><%=viewBean.getProcessStep().getSimpleExecutionProcess().getName()%></span>
									<label>Utilisateur:</label>
									<span><%=  viewBean.getProcessStep().getSimpleExecutionProcess().getIdUser()%></span>
									<label>Etat:</label>
									<span class="csEtatTranslate"><%=viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat().toLabel()%></span>
									<label>Etape:</label>
									<span><span class="numberStep" ><%= viewBean.getProcessStep().getSimpleStep().getOrdre()%></span> / <%=viewBean.getJadeProcessStepDefs().size()%></span>
								</div>
							</fieldset>
							
					
	
						    <%if(viewBean.hasProperties()){%>
						    <fieldset id="paramettreDonnesPourLeProcess" class="param ui-widget">  
								<legend class="ui-widget-header">Paramètres globaux du processuss</legend>
								<div class="content ui-widget-content">
									<div id="detailProperties" class="zoneAjaxWithoutBackground zoneAjaxWithoutBorder"> </div>
									<% if (JadeProcessStateEnum.INIT.equals(viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat())) {%>
									<div class="btnAjax">
										<input class="btnAjaxUpdate" type="button" value="<%=btnUpdLabel%>">
										<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
										<input class="btnAjaxCancel" type="button" value="<%=btnCanLabel%>">
									</div>
									<%}%>
								</div>
						    </fieldset>
						    <%}%>	
						</div>
						
						<div id="forError"> </div>
	
						<div id="forWarning"></div>
						
						<div class="area">
							<fieldset>
							<legend class="ui-widget-header">Etapes</legend>
							<table id="steps" class="ui-widget areaTabel" width="100%">
								<colgroup>
								    <col style="width: 20px">
									<col>
									<col style="width: 150px;">
								    <col >
							    </colgroup>
							
								<tbody class="ui-widget-content content">
									<tr id="creationDeLaPopulation" idEntity="0" style="border-bottom: 2px grey dashed">
								 		<td>0</td>
								 		<td class="description"> Création de la population</td>
								 		<td >	
								 		<% if(isComputingPopulation || JadeProcessStateEnum.INIT.equals(viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat())){%>
								 			<div id="current" style="height:15px"  class="progressBar progressEntity" data-g-progressbar="timer:500,parametres:<%=viewBean.getProcessStep().getSimpleExecutionProcess().getIdExecutionProcess()%>" ></div>
								 		<%}else{ %>
								 			<div style="height:15px"   data-g-progressbar="valide:true" class="progressBar progressEntity" valid="true"></div>
								 		<%} %>
								 		</td> 

								 		<td  style="text-align: left; margin:4px">
								 		<span class="boutton"></span>
								 		<span class="customHtml"></span>
								 		 
								 		<span class="infosTimeTrForProcess">
								 		 <label>Nb entité: </label>
								 		 <span class="nbEntityCreated" ><%= viewBean.getProcessInfos().getNbEntite()%></span>
								 		</span>
								 		<% if(JadeProcessStateEnum.INIT.equals(viewBean.getProcessStep().getSimpleExecutionProcess().getCsEtat()) && hasRightNew){%>
											<span class="buton createEntitys" >Créer les entités</span> 
										<%} %>
										</td> 
									
								 	</tr>
								 	
								 <%  
								 	int j = 0;
								 
								 	for (Entry<String, JadeProcessStepHandler> entry :viewBean.getJadeProcessStepDefs().entrySet()) {
								
								 		String current = "";
										Boolean isStepEnabled = false;
										Boolean error = false;
										boolean validate = false;
										String notExecuted = "";
										boolean isCurrentStep = false;
										String s_date="";
										String s_dateEnd = "";
										Date dateDebut = null;
										Date dateEnd = null;
										
										JadeProcessStepHandler step = entry.getValue();
										if(step != null && viewBean.isStepEnabled(step)) {
										 	validate = (step.getSimpleStep().getCsState().equals(JadeProcessStepStateEnum.VALIDATE) || viewBean.getProcessStep().getSimpleExecutionProcess().equals(JadeProcessStateEnum.FINISHED))?true:false;
										 	isCurrentStep = step.getSimpleStep().getId().equals(viewBean.getProcessStep().getSimpleExecutionProcess().getIdCurrentStep());
										 	//String color = step.getSimpleStep().getCsState().equals(IJadeProcess.STEP_STATE_DONE) || step.getSimpleStep().getCsState().equals(IJadeProcess.STEP_STATE_DONE_WITH_MESSAGE);
											
										 	if( !isComputingPopulation && !validate && !isInit&& isCurrentStep){
										 		current = "current";
										 		error = JadeProcessStepStateEnum.DONE_WITH_ENTITY_LOG.equals( step.getSimpleStep().getCsState());
										 	}else if(JadeProcessStepStateEnum.INIT.equals(step.getSimpleStep().getCsState())){
										 		notExecuted = "stepNotExecuted";
										 	}
							
										
											if(!JadeStringUtil.isBlankOrZero(step.getSimpleStep().getDateExecution())){
												dateDebut = new Date(Long.valueOf(step.getSimpleStep().getDateExecution()));
												s_date = JadeDateUtil.getFormattedDateTime(dateDebut);
											}
											if(!JadeStringUtil.isBlankOrZero(step.getSimpleStep().getDateExecution())){
												dateEnd = new Date(Long.valueOf(step.getSimpleStep().getDateEndExecution()));
												s_dateEnd = JadeDateUtil.getFormattedDateTime(dateEnd);
											}
											isStepEnabled = true;
								 		} 
								 %>
									<% if(isStepEnabled) { 	 
										j = j + 1;
										
										String inject = (step.getJadeProcessStepBean().getIsEntityInjectable())?"hasInject":"";
										%>
									 	<tr idEntity="<%= step.getSimpleStep().getId()%>" class="stpesDuProcessGestion <%=notExecuted +' '+inject%> ">
									 		<td><%=j%><span class ="ordreStep">(<%= step.getSimpleStep().getOrdre()%>)</span></td>
									 		<td class="description"><%= step.getJadeProcessStepBean().getDescription().get(objSession.getIdLangueISO().toUpperCase())%></td>
									 		<td>
									 			<div style="height:15px" class="progressBar progressStep" error="<%=error %>"  
									 			     data-g-progressbar="valide:<%=validate%>,
									 			     					 timer:5000,
									 			     					 parametres:<%=viewBean.getProcessStep().getSimpleExecutionProcess().getIdExecutionProcess()%>"  
									 			     id="<%=current%>">
									 			</div>
									 		</td>
									 		<td style="text-align: left; margin:4px">
									 		 
			     					 		<span  class="buttonProcess">	
				     					 		 <%if(!validate && isCurrentStep && viewBean.getStepDefinition().getHasButton()){ %>
													<%if (hasRightNew) { %>
														<span class="button executeStep" >Exécuter l'étape</span> 
														<span class="button validerStep" >Valider l'étape</span> 
														<span class="button abortStep" >Interrompre</span> 
													<%}%>
												<% }%>
											</span>
									 	   <%-- <% if(!JadeProcessStepStateEnum.INIT.equals(step.getSimpleStep().getCsState())){%>--%>
									 		<span class="customHtml">
									 			<%= viewBean.customHtml(step)%>
									 		</span>
			     					 		<%--<% }%>--%>
											</td>  
									 	</tr>
									 	<%if(current == "current" || (displayCurrentTrTime && (isComputingPopulation  || isInit))) {
									 		displayCurrentTrTime = false;
									 	%>
										 	<tr class="infosTimeTrForProcess  currentInfosTimeTrForProcess">
										 		<td><input type="hidden" class="uidJob" value="" /> </td>
										 		<td colspan="3">									 		
												 	<label>Temps écoulé:</label>
													<span class="time timeCurrentStep2" ><%= viewBean.getProcessInfos().getTimeCurrentStep()%></span>
													<label>Temps restant:</label>
													<span class="time expectedTimeCurrentStep"> <%= viewBean.getProcessInfos().getTimeCurrentStep()%></span>
													<label>Temps moyen(ms):</label>
													<span class="averageTimeEntite"> <%= viewBean.getProcessInfos().getAverageTimeEntite()%></span> 
													<label>Nb à traiter : </label>
													<span class="nbEntiteSelected"> <%= viewBean.getProcessInfos().getNbEntiteSelected()%></span>
													<label>Nb restant : </label>
													<span class="nbEntiteNotTreat"> <%= viewBean.getProcessInfos().getNbEntiteNotTreat()%></span>
													<label>Nb en erreurs:</label>
													<span class="nbEntiteOnError"> <%= viewBean.getProcessInfos().getNbEntiteOnError()%></span>
												</td>
												<div id="currentName" style="display:none"><%=entry.getKey()%></div>
										 	</tr>
									 	<% }%>
									 		<%if(JadeProcessStepStateEnum.VALIDATE.equals(step.getSimpleStep().getCsState())) {%>
										 	<tr class="infosTimeTrForProcess">
										 		<td><input type="hidden" class="uidJob" value="" /> </td>
										 		<td colspan="3">									 		
												 	<label>Debut:</label>
													<span><%= s_date %></span>
													<label>Fin:</label>
													<span> <%= s_dateEnd%></span>
													<label>Nb entités : </label>
													<span> <%=step.getSimpleStep().getNbEntite()%></span>
												</td>
										 	</tr>
									 	<% }%>
								 	<% }%>
								 <%}%>
								</tbody>
							</table>
							</fieldset>
						</div>
					</div>
				</div>
				<div id="confirm" style="display:none">Voulez-vous éxecuter l'étape ?</div>
				<div id="dialogConfirm" style="display:none">
					<input type ="hidden" name="PATH_CSV_TO_IMPORT" id="PATH_CSV_TO_IMPORT" />
					<input name="PATH_CSV_TO_IMPORT_FRONT" id='PATH_CSV_TO_IMPORT_FRONT' type='file' data-g-upload="callBack: callBackUpload, protocole:jdbc">
						<div id='messageErreurCsv'></div>
				</div>
			</td>
			<td>&nbsp;</td>
		</tr>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
</BODY>
</HTML>
