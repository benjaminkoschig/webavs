<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_LOT_R"

idEcran="PPC0090";
rememberSearchCriterias = true;
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>


<%@page import="globaz.corvus.api.lots.IRELot"%>
<%@page import="globaz.corvus.db.lots.RELotManager"%>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<SCRIPT language="javascript">
	bFind = true;
	usrAction = "pegasus.lot.lot.lister";
	$(document).ready(function(){ 
		$('#lotSearch\\.forCsType').change(function() {
		    if($(this).val() == <%=IRELot.CS_TYP_LOT_DECISION_RESTITUTION%>) {
		    	$('#lotSearch\\.forNotDecisionRestitution').prop('checked', false);
		    } else {
		    	$('#lotSearch\\.forNotDecisionRestitution').prop('checked', true);
		    }
		});
		$('#lotSearch\\.forNotDecisionRestitution').click(function(){
		    if ($('#checkbox').attr('checked')) {

		    	 $("#lotSearch\\.forCsType").val('<%=IRELot.CS_TYP_LOT_DECISION_RESTITUTION%>');
		    } else {
		    	 $("#lotSearch\\.forCsType").val('');
		    }
		});
	});
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_LOT_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_PC_LOT_R_A_PARTIR_DE"/>&nbsp; <input type="text" name="lotSearch.fromDateCreation" value="" data-g-calendar="mandatory:false" /></TD>
							<TD><ct:FWLabel key="JSP_PC_LOT_R_TYPE"/>&nbsp;
								<ct:select name="lotSearch.forCsType" id="lotSearch.forCsType"  wantBlank="true">
									<OPTION value="<%=IRELot.CS_TYP_LOT_MENSUEL%>"><%=objSession.getCodeLibelle(IRELot.CS_TYP_LOT_MENSUEL)%></OPTION>
									<OPTION value="<%=IRELot.CS_TYP_LOT_DECISION%>"><%=objSession.getCodeLibelle(IRELot.CS_TYP_LOT_DECISION)%></OPTION>
									<OPTION value="<%=IRELot.CS_TYP_LOT_DECISION_RESTITUTION%>"><%=objSession.getCodeLibelle(IRELot.CS_TYP_LOT_DECISION_RESTITUTION)%></OPTION>
								</ct:select>								
							</TD>
							<TD><ct:FWLabel key="JSP_PC_LOT_R_ETAT"/>&nbsp;
								<ct:select name="lotSearch.forCsEtat" wantBlank="true">
									<ct:optionsCodesSystems csFamille="<%=IRELot.CS_GROUPE_ETAT_LOT%>"/>
								</ct:select>
							</TD>
						</TR>
						<tr>
							<td><ct:FWLabel key="JSP_PC_LOT_R_DECISION_RESTITUTION"/>&nbsp;<INPUT type="checkbox" name="mustSearchRestitution" id="lotSearch.forNotDecisionRestitution" checked/>
							</td>
						</tr>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>