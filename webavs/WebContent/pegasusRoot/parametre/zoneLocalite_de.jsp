<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%@page import="globaz.pegasus.vb.parametre.PCZoneLocaliteViewBean"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%-- tpl:insert attribute="zoneInit" --%>
 
<%// Les labels de cette page commence par la préfix "JSP_PC_PARAM_ZONE_LOCALITE_D"
	idEcran="PPC0108";
	PCZoneLocaliteViewBean viewBean = (PCZoneLocaliteViewBean) session.getAttribute("viewBean");
	
	//boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));
	boolean viewBeanIsNew= true;
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete = true;
	//bButtonNew = "add".equals(request.getParameter("_method"));
	String dateDebut = null;
	String idZone = null;
	String useValiderContinuer = PCCommonHandler.getStringDefault(request.getParameter("useValiderContinuer"));
	
	if (Boolean.valueOf(useValiderContinuer)){
	 	dateDebut = PCCommonHandler.getStringDefault(request.getParameter("zoneLocalite.simpleLienZoneLocalite.anneeDebut"));
		idZone = PCCommonHandler.getStringDefault(request.getParameter("zoneLocalite.simpleLienZoneLocalite.idZoneForfait"));
	}else{
	    dateDebut = viewBean.getZoneLocalite().getSimpleLienZoneLocalite().getDateDebut();
	    //idZone = viewBean.getZoneLocalite().getSimpleLienZoneLocalite().getIdZoneForfait();
	}
	
	%>
<%@ include file="/theme/detail/javascripts.jspf" %>


<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%=rootPath%>/css/formTableLess.css">
<%-- tpl:insert attribute="zoneScripts" --%>
<script type="text/javascript">
  var url = "<%=IPCActions.ACTION_PARAMETRES_ZONE_LOCALITE%>";
 // var MAIN_URL="<%=formAction%>";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
  var messageButtonValidContinue = "<ct:FWLabel key='JSP_PC_PARAM_CONVERSION_RENTE_D_VALIDCONTINUE'/>" ;
</script>

<%-- /tpl:insert --%>
<script type="text/javascript" src="<%=rootPath %>/scripts/parametre/zoneLocalite_js.js"></script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
<td>
	<input type="hidden" id="useValiderContinuer" value="" name="useValiderContinuer"/>
	<div id="main" class="formTableLess"> 
		<div>
			<label for="simpleZoneForfaits.csCanton">
				<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_D_ZONE_FOFAITS" />
			</label>   
			<ct:select name="zoneLocalite.simpleLienZoneLocalite.idZoneForfait" 
					   wantBlank="true" 
				 	   defaultValue="<%=idZone%>"
			           notation="data-g-select='mandatory:true'"
			           id="idZoneForfait">
			    <%for( JadeAbstractModel model:viewBean.getListZoneFofaits()){ 
			     	SimpleZoneForfaits zone = (SimpleZoneForfaits)model;
			     %>
					<ct:option id="<%=zone.getCsCanton()%>" value="<%=zone.getId()%>" label="<%=viewBean.getDescZone(zone,objSession) %>"/>
				<%} %>
			</ct:select>
			<label for="zoneLocalite.simpleLienZoneLocalite.idLocalite">
				<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_D_LOCALITE" />
			</label>
				<input type="hidden" 
				       id="idLocalite" 
				       name = "zoneLocalite.simpleLienZoneLocalite.idLocalite"
				       value="<%=viewBean.getZoneLocalite().getSimpleLienZoneLocalite().getIdLocalite()%>" />
		 		<ct:widget id='localite' 
				           name='localite' 
				           notation='data-g-string="mandatory:true"'
				           defaultValue="<%=viewBean.getZoneLocalite().getLocaliteSimpleModel().getLocalite()%>"
				           styleClass='libelleLong'>
					<ct:widgetService methodName="findLocalite" className="<%=AdresseService.class.getName()%>">										
						<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_PARAM_HOMES_W_TIERS_NPA"/>
						<ct:widgetCriteria criteria="forLocaliteUpperLike" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>
						<ct:widgetLineFormatter format="#{numPostal}, #{localite}"/>
						<ct:widgetDynamicCriteria>
							<script type="text/javascript">
								function(){
									 $zoneLocalite = $('#idZoneForfait').find('option:selected');
									 return 'forIdCanton='+$zoneLocalite.attr('id');
								}
							</script>
						</ct:widgetDynamicCriteria>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									$(this).val($(element).attr('numPostal')+', '+$(element).attr('localite'));
									$('#idLocalite').val($(element).attr('idLocalite'));
								}
							</script>											
						</ct:widgetJSReturnFunction>
					</ct:widgetService>
				</ct:widget>
		</div>
		<div>  
			<label for="zoneLocalite.simpleLienZoneLocalite.anneeDebut">
				<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_D_DATE_DEBUT" />
			</label>
			<span class='td'>
				<ct:inputText name="zoneLocalite.simpleLienZoneLocalite.dateDebut" 
			              defaultValue="<%=dateDebut%>" 
			              id="zoneLocalite.simpleLienZoneLocalite.dateDebut" 
			              notation="data-g-calendar='mandatory:true'" /> 
			</span>
			<label for="zoneLocalite.simpleLienZoneLocalite.dateFin">
				<ct:FWLabel key="JSP_PC_PARAM_ZONE_LOCALITE_D_DATE_FIN" />
			</label>
			<span class='td'>
				<ct:inputText name="zoneLocalite.simpleLienZoneLocalite.dateFin" 
			              defaultValue="<%=viewBean.getZoneLocalite().getSimpleLienZoneLocalite().getDateFin()%>" 
			              id="zoneLocalite.simpleLienZoneLocalite.dateFin" 
			              notation="data-g-calendar='mandatory:false'" />
			</span>
		</div>
		
	</div>
</td>
</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
