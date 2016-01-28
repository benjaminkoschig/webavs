<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%> 
<%
	idEcran ="GTI0041";

	String idTiers = "";
	if(!JadeStringUtil.isNull(request.getParameter("idTiers"))){
		idTiers = request.getParameter("idTiers");
	}
	
	actionNew  += "&idTiers=" + idTiers ;
%>


<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%><SCRIPT>
usrAction = "pyxis.tiers.histoNumAvs.lister";
bFind = true;
</SCRIPT>
<%
globaz.pyxis.db.tiers.TITiersViewBean viewBean = (globaz.pyxis.db.tiers.TITiersViewBean  )session.getAttribute ("viewBean");							
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>AHV Verlauf
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>

	<TR>
        <TD nowrap width="60"><LABEL for="nom"><ct:FWLabel key="HISTORIQUE_AVS_TIER"/></LABEL></TD>
		<TD><INPUT type="text" name="nom" value="<%=viewBean.getNom()%>" class="libelleLongDisabled" readonly> </TD>
	</TR>
	<TR>
		<TD colspan="2">&nbsp;</TD>
	<TR>
        <TD><LABEL for="nss"><ct:FWLabel key="HISTORIQUE_AVS_NSS"/></LABEL></TD>
		<TD><INPUT type="text" name="NSS" value="<%=viewBean.getNumAvsActuel()%>" class="libelleLongDisabled" readonly> </TD>
	</TR>
	<TR>
		<TD>
			<INPUT type="hidden" name="forIdTiers" value='<%=viewBean.getIdTiers()%>' >
			<INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>' >
			<INPUT type="hidden" name="selectedId" value='<%=request.getParameter("selectedId")%>' >
	    </TD>
     </TR>

	 <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons"  --%>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-35");
</script>

		<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>