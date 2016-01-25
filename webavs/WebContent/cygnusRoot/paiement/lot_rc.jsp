<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%@page import="globaz.cygnus.servlet.IRFActions" %>
<%@page import="globaz.cygnus.vb.paiement.RFLotViewBean" %>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper" %>
<%@page import="globaz.cygnus.application.RFApplication" %>
<%@page import="globaz.corvus.api.lots.IRELot" %>
<%@page import="globaz.cygnus.api.paiement.IRFLot" %>
<%@page import="globaz.corvus.db.lots.RELot" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PRF0052";
rememberSearchCriterias = true;
bButtonNew = false;

RFLotViewBean viewBean = (RFLotViewBean) session.getAttribute("viewBean");
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal" showTab="menu"/>

<SCRIPT language="javascript">
usrAction = "<%=IRFActions.ACTION_LOTS%>.lister";

actionNew = "<%=IRFActions.ACTION_LOTS%>.afficher";	

function clearFields () {
	document.getElementsByName("forDateDebut")[0].value="";
	document.getElementsByName("forTypePrestation")[0].value="";
	document.getElementsByName("forEtatPrestation")[0].value="";
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_LOT_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<TR>							
	<TD><LABEL for="fromDateCreation"><ct:FWLabel key="JSP_RF_LOT_DATE_DEBUT"/></LABEL></TD>			
	<TD><input data-g-calendar=" "  name="fromDateCreation" value=""/></TD>
	<%--<TD><LABEL for="forCsType"><ct:FWLabel key="JSP_RF_LOT_TYPE_PRESTATION"/></LABEL></TD>			
	<TD><ct:FWCodeSelectTag name="forCsType" codeType="RETYPLOT" defaut="" wantBlank="true"/></TD>--%>  
	<TD><LABEL for="forCsEtat"><ct:FWLabel key="JSP_RF_LOT_ETAT_PRESTATION"/></LABEL></TD>			
	<TD><ct:FWCodeSelectTag name="forCsEtat" codeType="REETALOT" defaut="" wantBlank="true"/></TD>
	<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
	<TD><input type="hidden" name="forOrderBy" value="<%=RELot.FIELDNAME_ID_LOT + " DESC"%>" /></TD>
	<TD><input type="hidden" name="forCsType" value="<%=IRELot.CS_TYP_LOT_DECISION+","+IRFLot.CS_TYP_LOT_AVASAD%>" /></TD>
	
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