<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran = "CDS0002";
	rememberSearchCriterias = true;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
bFind = false;
usrAction = "draco.declaration.declaration.lister";

top.document.title = "Web@AVS - <ct:FWLabel key='CDS0002_TITRE_ECRAN'/>";
</SCRIPT>
<ct:menuChange displayId="menu" menuId="DS-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="DS-OnlyDetail"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="CDS0002_TITRE_ECRAN"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<TR>
    <TD width="100"><ct:FWLabel key="CDS0002_AFFILIE"/>&nbsp;&nbsp;</TD>
    <% 
		String forNumAffilie ="";
		if (!globaz.jade.client.util.JadeStringUtil.isEmpty((String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX))){
		forNumAffilie = (String)session.getAttribute(globaz.pyxis.summary.TIActionSummary.PYXIS_VG_NUMAFF_CTX);
	%>
		<script>bFind = true</script>
	<%}%>
	<TD width="200"><input type="text" name="likeNumeroAffilie" value="<%=forNumAffilie%>" size="20" maxlength="20" tabindex="2"/></TD>
	<TD width="50">&nbsp;</TD>
	<TD width="100"><ct:FWLabel key="CDS0002_ETAT"/>&nbsp;&nbsp;</TD>
	<TD width="200"><ct:FWCodeSelectTag name="forEtat" defaut="121001" codeType="DRADECETAT" wantBlank="true"/></TD>

</TR>
<TR>
	<TD><ct:FWLabel key="CDS0002_ANNEE"/>&nbsp;&nbsp;</TD>
	<TD><input type="text" name="forAnnee" size="4" maxlength="4" tabindex="1"/></TD>
	<TD width="50">&nbsp;</TD>
	<TD><ct:FWLabel key="CDS0002_TRI"/>&nbsp;&nbsp;</TD>
	<TD>
   		<SELECT name="forSelectionTri" tabindex="3">
	        <OPTION value="1"><ct:FWLabel key="CDS0002_PAR_ANNEE_ET_NUM"/></OPTION>
	        <OPTION value="2"><ct:FWLabel key="CDS0002_PAR_ANNEE_ET_NOM"/></OPTION>
	        <OPTION value="3" selected="selected"><ct:FWLabel key="CDS0002_PAR_NUM"/></OPTION>
	        <OPTION value="4"><ct:FWLabel key="CDS0002_PAR_NON"/></OPTION>
    	</SELECT>
    </TD>
</TR> 
<TR>
    <TD width="100"><ct:FWLabel key="CDS0002_TYPE"/>&nbsp;&nbsp;</TD>
	<TD width="200"><ct:FWCodeSelectTag name="forTypeDeclaration" defaut="" codeType="DRATYPDECL" wantBlank="true"/></TD>  
	<TD width="50">&nbsp;</TD>
	<TD><ct:FWLabel key="CDS0002_PROVENANCE"/>&nbsp;&nbsp;</TD>
	<TD>
   		<SELECT name="forProvenance" tabindex="3">
   			<OPTION value="">&nbsp;</OPTION>
	        <OPTION value="1"><ct:FWLabel key="PROVENANCE_PUCS"/></OPTION>
	        <OPTION value="2"><ct:FWLabel key="PROVENANCE_DAN"/></OPTION>
	        <OPTION value="3"><ct:FWLabel key="PROVENANCE_PUCS_CCJU"/></OPTION>
	        <OPTION value="4"><ct:FWLabel key="PROVENANCE_SWISSDEC"/></OPTION>
    	</SELECT>
    </TD>  
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>