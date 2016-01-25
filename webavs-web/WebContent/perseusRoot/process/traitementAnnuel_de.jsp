<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.perseus.process.traitementAnnuel.PFProcessTraitementAnnuelEnum"%>
<%@page import="globaz.perseus.vb.process.PFTraitementAnnuelViewBean"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="ch.globaz.perseus.process.traitementAdaptation.PFProcessTraitementAdaptationEnum"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>

<%-- tpl:put name="zoneInit" --%>
<% PFTraitementAnnuelViewBean viewBean = (PFTraitementAnnuelViewBean) session.getAttribute("viewBean"); %>

<script language="JavaScript">
$(function() {
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

.stepParamsTitle{
	font-weight:bold;
	margin-bottom:10px !important;
	margin-left:10px !important;
	color:#38709E;
}
</style>

<div id="properties" >
	<div>
		<label> 
			<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_MAIL"/> CCVD
		</label>
		<input type="text"  id="<%=PFProcessTraitementAnnuelEnum.ADRESSE_MAIL_CCVD.toString() %>" style="width: 400px" value="<%=JadeThread.currentUserEmail() %>" data-g-string="mandatory:true"/>
	</div>
	
	<div>
		<label>
			<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_MAIL"/> AGLau
		</label>
		<input type="text" id="<%=PFProcessTraitementAnnuelEnum.ADRESSE_MAIL_AGLAU.toString() %>" style="width: 400px" value="<%=JadeThread.currentUserEmail() %>" data-g-string="mandatory:true"/>
	</div>
	<div>
		<label>
			<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_MOIS"/>
		</label>		
		<input type="text" id="<%=PFProcessTraitementAnnuelEnum.MOIS.toString() %>" value="<%=PerseusServiceLocator.getPmtMensuelService().getDateProchainPmt() %>" style="width: 400px" readonly="true" disabled="true"/>
	</div>
	<div>
		<label>
			<ct:FWLabel key="JSP_PF_TRAITEMENT_ANNUEL_TEXTE"/>
		</label>
		<textarea id="<%=PFProcessTraitementAnnuelEnum.TEXTE_DECISION.toString() %>" style="width: 400px" data-g-string="mandatory:true"/>
	</div>
</div>
