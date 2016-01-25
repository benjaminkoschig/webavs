<%-- tpl:insert page="/theme/detail_ajax.jtpl" --%>
<%@page import="globaz.fx.vb.process.FXJadeProcessListViewBean"%>
<%@page import="ch.globaz.jade.process.business.enumProcess.JadeProcessStateEnum"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail_ajax/header.jspf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored ="false" %>

<%-- tpl:put name="zoneInit" --%> 
<%
	//Les labels de cette page commencent par le préfix "JSP_PC_PRET_TIERS_D"
	idEcran="??";
	String rootPath = servletContext+(mainServletPath+"Root");
	FXJadeProcessListViewBean viewBean = (FXJadeProcessListViewBean) session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/boxError.js"/></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/jadeProcessList.js"/></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/jadeProcess/jadeProperties.js"/></script>
<link rel="stylesheet" type="text/css" href="<%=rootPath %>/css/porcess.css"/>

<%-- tpl:put name="zoneScripts" --%>
 
<style>
 #progressStep {
 	margin:10px;
 }
 
 #steps,#process {
 	margin-top:15px;
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
 
 #process {

 }
 .formTableLess .content {
 background-image :none;
 background-color:  #F4F4F4 ;
 
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
 
 #tabs { margin-top: 1em; }
#tabs li .ui-icon-close { float: left; margin: 0.4em 0.2em 0 0; cursor: pointer; }
#add_tab { cursor: pointer; }
</style>
<script language="JavaScript">
	var S_KEY_PROCESS = "${viewBean.keyProcess}";
	var S_ACTION = "fx.process.currentStepAjax" ;
	var S_PROCESS_ETAT_WAITING = "${viewBean.stateWaitingToRun}";
	var S_ACTION_SPECIFIED ="${viewBean.urlProperties}";
	var JSP_DELETE_MESSAGE_INFO="<ct:FWLabel key='JSP_PROCESS_DELETE_MESSAGE_INFO'/>";
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyStart.jspf" %>
<%@ include file="/theme/detail_ajax/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<tr id="step" >		
			<td colspan="3">

				<div class="area">
					<div class="formTableLess">
						<fieldset class="ui-widget"">
							<legend class="ui-widget-header ">Description</legend>
							<div class="ui-widget-content content">
								<div>
								${viewBean.description}
								</div>
							</div>
						</fieldset>							
					</div>
					<ct:ifhasright element="<%=userActionNew %>" crud="c">
						<span id="newProcess" class="btnAjaxAdd"><ct:FWLabel key="JADE_PROCESS_NEW"/></span>
					</ct:ifhasright>
					<table class="areaTabel areaDataTable" width="100%">
						<thead>
							<tr class="ui-widget-header search">
							<c:forEach var="column" items="${viewBean.columns}" >
								<c:if test ="${column.display}">
									<td>
										<c:choose>
											<c:when test="${column.keyEnum == viewBean.keyEnumState}">
												<ct:select id="forCsEtat" styleClass="noFocus" name="forCsEtat" notation="data-g-selectautocomplete='addIcon:false'" wantBlank="true">
							
												<% for(JadeProcessStateEnum e: JadeProcessStateEnum.values()){%>
													<ct:option label="<%=e.toLabel()%>" value="<%=e.toString()%>" />
												<%}%>
			
												</ct:select>
											</c:when>
											<c:when test="${column.search}">
													<input type="text" styleClass="noFocus" id="${column.nameSearch}" name="${column.nameSearch}" styleClass="noFocus">
											</c:when>
											<c:otherwise>
											</c:otherwise>
										</c:choose>
									</td>	
								</c:if>	
							</c:forEach>
							</tr>
							<tr>
							<c:forEach var="column" items="${viewBean.columns}" >
								<c:if test ="${column.display}">
								<th>
									<ct:FWLabel key="${column.libelle}" />
								</th>	
								</c:if>	
							</c:forEach>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
					<div class="areaDetail"> 			
						<div class="formTableLess">
						<c:if test ="${viewBean.hasProperties}">
							<fieldset class="ui-widget"">
								<legend class="ui-widget-header "> <ct:FWLabel key="JADE_PROCESS_PARAMETRES"/></legend>
								<div class="ui-widget-content content">
									<div id="detailProperties" ></div>
									<div class="btnAjax">
										<input class="btnAjaxValidate" type="button" value="<%=btnValLabel%>">
									</div>
								</div>
							</fieldset>					
						</c:if>	
						</div>
					</div>
				</div>
			</td>
			<td>&nbsp;</td>
		</tr>
<%@ include file="/theme/detail_ajax/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail_ajax/footer.jspf" %>
<%-- /tpl:insert --%>