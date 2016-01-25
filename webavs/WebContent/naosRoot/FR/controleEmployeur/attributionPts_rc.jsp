<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="CFA";
AFAttributionPtsListViewBean viewBean = (AFAttributionPtsListViewBean) request.getAttribute("listViewBean");
int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
%>
<%@page import="globaz.naos.db.controleEmployeur.AFAttributionPtsListViewBean"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
function updateNumAffilie(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('forNumAffilie').value =  tag.select[tag.select.selectedIndex].value;
	}	
}
	usrAction = "naos.controleEmployeur.attributionPts.listerHistorique";
	bFind = true;
</SCRIPT>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide"/>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Historique<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	  	<TR>
			<td>N° affili&eacute;</td>
			<% 
				String forNumAffilie ="";
				if (!globaz.jade.client.util.JadeStringUtil.isEmpty((String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX))){
					forNumAffilie = (String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX);
				}
			%>
			<td><ct:FWPopupList name="forNumAffilie"
				value="<%=forNumAffilie%>" className="libelle"
				jspName="<%=jspLocation%>" autoNbrDigit="<%=autoDigiAff%>" size="25"
				onChange="updateNumAffilie(tag);" minNbrDigit="6" /> <!--IMG
													src="<%=servletContext%>/images/down.gif"
													alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
													title="presser sur la touche 'flèche bas' pour effectuer une recherche"
													onclick="if (document.forms[0].elements('numAffilie').value != '') numAffiliePopupTag.validate();"-->
			</td>
		</TR>
        <TR>
        	<TD nowrap width="100">Utilisateur</TD>
            <TD nowrap><INPUT type="text" name="likeUser" class="libelleLong" value=''></TD>
        </TR>
        <TR>
        	<TD nowrap width="100">Date de modification</TD>
            <TD nowrap>
	            <ct:FWCalendarTag name="forDateModification"
					value=""
					errorMessage="la date de modification est incorrecte"
					doClientValidation="CALENDAR"
				  	/>
            </TD>
      	</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
			<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>