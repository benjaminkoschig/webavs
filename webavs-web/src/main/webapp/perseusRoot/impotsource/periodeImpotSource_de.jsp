<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.vb.impotsource.PFPeriodeImpotSourceViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
idEcran="PPF2121";
PFPeriodeImpotSourceViewBean viewBean = (PFPeriodeImpotSourceViewBean) session.getAttribute("viewBean");

boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
boolean viewBeanIsValid = "fail".equals(request.getParameter("_valid"));

selectedIdValue = viewBean.getId();

autoShowErrorPopup = true;

if(null != viewBean.getPeriode().getSimplePeriodeImpotSource().getPeriodeGeneree()
	&& Boolean.TRUE.equals(viewBean.getPeriode().getSimplePeriodeImpotSource().getPeriodeGeneree())){
	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonCancel = false;
	bButtonValidate = false;
}

if(!objSession.hasRight("perseus", FWSecureConstants.ADD)){
	bButtonUpdate = false;
	bButtonDelete = false;
	bButtonCancel = false;
	bButtonValidate = false;
}



String srcVm = servletContext + (mainServletPath+"Root")+"/impotsource/";


%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>
<script type="text/javascript">
  var url = "<%=IPFActions.ACTION_PARAMETRES_PERIODE_IMPOT_SOURCE%>";
  var messageDelete = "<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>";
  var messageUpdate = "<ct:FWLabel key='JSP_UPDATE_MESSAGE_INFO'/>";
 
  
  
</script>
<script type="text/javascript" src="<%=srcVm%>periodeImpotSource_js.js"></script>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>

<link rel="stylesheet" type="text/css" media="screen" href="<%=srcVm%>periodeImpotSource_css.css">

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>

<ct:FWLabel key="JSP_PF_PARAM_PERIODE_IMPOT_SOURCE_R_TITRE"/>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td>
		<table>
		<TR>
		<TD>
			<ct:FWLabel key="JSP_PF_PARAM_PERIODE_IMPOT_SOURCE_DATE_DEBUT"/></label>
		</TD>
		<td width="30"></td>
		<TD>
			<input type="text"  name="periode.simplePeriodeImpotSource.dateDebut" value="<%=JadeStringUtil.toNotNullString(viewBean.getPeriode().getSimplePeriodeImpotSource().getDateDebut())%>" data-g-calendar="mandatory:true,type:month"/>
		</TD>
	</TR>
	<TR>
		<TD>
		  <ct:FWLabel key="JSP_PF_PARAM_PERIODE_IMPOT_SOURCE_DATE_FIN"/></label>
		</TD>
		<td width="30"></td>
		<TD>
		 <input type="text" name="periode.simplePeriodeImpotSource.dateFin" value="<%=JadeStringUtil.toNotNullString(viewBean.getPeriode().getSimplePeriodeImpotSource().getDateFin())%>" data-g-calendar="mandatory:true,type:month"/>
		</TD> 
	</TR>
	<%if(null != viewBean.getPeriode().getSimplePeriodeImpotSource().getIdPeriode()){ %>
	<TR>	
			<td><ct:FWLabel key="JSP_PF_PARAM_PERIODE_IMPOT_SOURCE_GENEREE"/></td>
			<td width="30"></td>
			<td><input type="checkbox" name="isGeneree" <%=(viewBean.getPeriode().getSimplePeriodeImpotSource().getPeriodeGeneree() != null && viewBean.getPeriode().getSimplePeriodeImpotSource().getPeriodeGeneree()) ? "checked='checked'" : ""%>/></td>
	</TR>
	<%}%>
	</table>
	</td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>

<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>