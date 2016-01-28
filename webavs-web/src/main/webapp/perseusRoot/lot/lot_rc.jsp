<%-- tpl:insert page="/theme/find.jtpl" --%>
<%--<%@page import="globaz.perseus.utils.parametres.PFParametresHandler"--%>
<%--<%@page import="globaz.jade.persistence.model.JadeAbstractModel"--%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<%
// Les labels de cette page commence par le préfix "JSP_PF_PARAM_LOT_R"
	idEcran="PPF0712";
	rememberSearchCriterias = true;
	bButtonNew=false;
	String idLot = request.getParameter ("idLot");
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>

<%-- tpl:insert attribute="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="menu"/>

<script language="JavaScript" >
	var bFind = true;

	var usrAction = "perseus.lot.lot.lister";
	
	function clearFields () {
		$('.clearable,[name="lotSearch\\.forTypeLot"],[name="lotSearch\\.forEtatCs"],#forDateValable').val('');
	}
</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_LOT_D_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
		<TABLE border="0" cellspacing="0" cellpadding="5" width="100%">
				<td>
						<label for="lotSearch.forMoisValable"><ct:FWLabel key="JSP_PF_PARAM_LOT_DATE"/></label>
				</td>
				<td>
						<ct:inputText notation="data-g-calendar='type:month'" name="lotSearch.forMoisValable" id="forDateValable"/>
				</td>
				<td>
						<label for="lotSearch.forTypeLot"><ct:FWLabel key="JSP_PF_PARAM_LOT_TYPE" /></label>   
				</td>
				<td>
						<ct:select name="lotSearch.forTypeLot" wantBlank="true">
							<ct:optionsCodesSystems csFamille="PFTYPELOT"/>
						</ct:select>
				</td>
				<td>
						<label for="lotSearch.forEtatCs"><ct:FWLabel key="JSP_PF_PARAM_LOT_ETAT"/></label>
				</td>
				<td>
						<ct:select name="lotSearch.forEtatCs" wantBlank="true">
							<ct:optionsCodesSystems csFamille="PFETATLOT"/>
						</ct:select>
				</td>
			</tr>
			<TR>
				<TD colspan="6">&nbsp;</TD>
			</TR>
			<TR>
				<TD><input type="button" onclick="clearFields()" accesskey="C" value="Effacer">[ALT+C]</TD>
				<TD colspan="5">&nbsp;</TD>
			</TR>
		</table>
	</td>
</tr>
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>



