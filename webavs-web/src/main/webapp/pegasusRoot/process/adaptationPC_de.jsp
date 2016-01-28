<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.process.PCAdaptationPCViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>

<%-- tpl:put name="zoneInit" --%>
<%
	PCAdaptationPCViewBean viewBean = (PCAdaptationPCViewBean) session.getAttribute("viewBean");
%>

<script language="JavaScript">
	$(function () {
		$(".HAS_ADAPTATION_DES_PSAL").change(function () {
			if (this.value === "true") {
				$(".montant_psal").show();
			} else {
				$(".montant_psal").hide();
			}
		});
	});
</script>
<style type="text/css">
#properties label {
	width:200px;
	display: inline;
}

#properties div{
	margin:0 0 20px 0;
	
}
#properties .montant_psal{
	margin:10px 0px 5px 0px;
	display: none;
}

.stepParamsTitle{
	font-weight:bold;
	margin-bottom:10px !important;
	margin-left:10px !important;
	color:#38709E;
}
</style>
<div id="properties" >

	<div>		
		<label for="DATE_ADAPTATION"><ct:FWLabel key="PROCESS_ADAPTATION_PC_MOIS_ADAPTATION"/></label>
		<input id="DATE_ADAPTATION" data-g-calendar="type:month,mandatory:true" value="" />
	
		<label><ct:FWLabel key="PROCESS_ADAPTATION_PC_AUGMENTATION_DES_RENTES"/></label>
		<span>
			<ct:FWLabel key="PROCESS_ADAPTATION_PC_OUI"/> <input id="HAS_ADAPTATION_DES_RENTE_true" type="radio"  class="HAS_ADAPTATION_DES_RENTE" name="HAS_ADAPTATION_DES_RENTE" value="true" />
			<ct:FWLabel key="PROCESS_ADAPTATION_PC_NON"/> <input id="HAS_ADAPTATION_DES_RENTE_false" type="radio" class="HAS_ADAPTATION_DES_RENTE" name="HAS_ADAPTATION_DES_RENTE" value="false" />
		</span>
	</div>
	<div>
		<label><ct:FWLabel key="PROCESS_ADAPTATION_PC_ADAPTATION_DES_PSAL"/>Adaptation des PSAL ?</label>
		<span>
			<ct:FWLabel key="PROCESS_ADAPTATION_PC_OUI"/> <input id="HAS_ADAPTATION_DES_PSAL_true" type="radio"  class="HAS_ADAPTATION_DES_PSAL" name="HAS_ADAPTATION_DES_PSAL" value="true" />
			<ct:FWLabel key="PROCESS_ADAPTATION_PC_NON"/> <input id="HAS_ADAPTATION_DES_PSAL_false" type="radio" class="HAS_ADAPTATION_DES_PSAL" name="HAS_ADAPTATION_DES_PSAL" value="false" />
		</span>
		<div class="montant_psal" >
			<label><ct:FWLabel key="PROCESS_ADAPTATION_PC_NOUVEAU_MONTANT"/></label>
			<input style="width:60px" id="PSAL_MONTANT_NOUVEAU" data-g-amount="mandatory:false" value="" />
			<label><ct:FWLabel key="PROCESS_ADAPTATION_PC_ANCIEN_MONTANT"/></label>
			<input style="width:60px" id="PSAL_MONTANT_ANCIEN" data-g-amount="mandatory:false" value="" />
		</div>
	</div>
	
	<!--  parametrage step impression des décisions -->
	<hr>
	<span class="stepParamsTitle"><ct:FWLabel key="PROCESS_ADAPTATION_PC_ETAPE_IMPRESSION"/> </span>
	<div class="date_doc_impression">
			<label><ct:FWLabel key="PROCESS_ADAPTATION_PC_DATE_DOC_IMPRESSION"/></label>
			<input id="DATE_DOCUMENT_IMPRESSION" data-g-calendar="mandatory:true" value="" />
	</div>
	<hr>
	
	
</div>
