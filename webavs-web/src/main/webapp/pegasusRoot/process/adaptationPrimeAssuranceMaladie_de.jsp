<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%@ page import="globaz.pegasus.vb.process.PCAdaptationPrimeAssuranceMaladieViewBean" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	String servletContext = request.getContextPath();
	PCAdaptationPrimeAssuranceMaladieViewBean viewBean = (PCAdaptationPrimeAssuranceMaladieViewBean) session.getAttribute("viewBean");
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
		<input id="IS_ADAPTATION_PRIME_LAMAL" value="true" style="display: none"/>
	</div>
	<span class="stepParamsTitle"><ct:FWLabel key="PROCESS_ADAPTATION_PC_ETAPE_IMPRESSION"/> </span>
	<hr>
	<div class="date_doc_impression">
		<label><ct:FWLabel key="PROCESS_ADAPTATION_PC_DATE_DOC_IMPRESSION"/></label>
		<input id="DATE_DOCUMENT_IMPRESSION" data-g-calendar="mandatory:true" value="" />
	</div>
	<hr>
</div>
