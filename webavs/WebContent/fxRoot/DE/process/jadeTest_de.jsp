<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>
<%-- tpl:put name="zoneInit" --%>

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
		<label for="DATE">Mois adaptation </label>
		<input id="DATE" data-g-calendar="type:month,mandatory:true" value="" />
	</div>

</div>
