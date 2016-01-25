<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%@page import="globaz.pegasus.vb.process.PCAdaptationHomePCViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	String servletContext = request.getContextPath();
	PCAdaptationHomePCViewBean viewBean = (PCAdaptationHomePCViewBean) session.getAttribute("viewBean");
%>

<style type="text/css">

#properties label {
}

#properties div{
	margin:0 0 20px 0;
	
}
#properties .montant_psal{
	margin:10px 0px 5px 0px;
	display: none;
}
</style>

<div id="properties" >

	<div>
		<label for="DATE_ADAPTATION">Mois adaptation </label>
		<input id="DATE_ADAPTATION" data-g-calendar="type:month,mandatory:true" value="" />

		<label>Home</label>
		<input name="ID_HOME" id="ID_HOME" type="hidden"></input>
		<ct:widget	id='DESC_HOME' name='DESC_HOME' 
					styleClass="selecteurHome libelleHome"
					notation="data-g-string='mandatory:true'">
			<ct:widgetService methodName="search" className="<%=HomeService.class.getName()%>">
				<ct:widgetCriteria criteria="likeNumeroIdentification" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NO_IDENTIFICATION"/>
				<ct:widgetCriteria criteria="likeDesignation" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_DESIGNATION"/>
				<ct:widgetCriteria criteria="likeNpa" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_NPA"/>
				<ct:widgetCriteria criteria="likeLocalite" label="JSP_PC_TAXE_JOURNALIERE_HOME_W_LOCALITE"/>
				<ct:widgetCriteria criteria="forTypeAdresse" fixedValue="<%=ch.globaz.pyxis.business.service.AdresseService.CS_TYPE_DOMICILE%>" label="JSP_PC_PARAM_HOMES_W_TIERS_LOCALITE"/>

				<ct:widgetLineFormatter format="#{simpleHome.numeroIdentification} #{simpleHome.nomBatiment}  #{adresse.tiers.designation1} #{adresse.tiers.designation2} - (#{adresse.localite.numPostal} #{adresse.localite.localite})"/> 
				<ct:widgetJSReturnFunction>
					<script type="text/javascript">
						function(element){
							$('#ID_HOME').val($(element).attr('simpleHome.id'));
							this.value=$.trim($(element).attr('simpleHome.numeroIdentification'))+' '+$.trim($(element).attr('simpleHome.nomBatiment'))+' '+$.trim($(element).attr('adresse.tiers.designation1'))+' '+$.trim($(element).attr('adresse.tiers.designation2'));
						}
					</script>
				</ct:widgetJSReturnFunction>
			</ct:widgetService>
		</ct:widget>
	</div>
</div>
