<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ0032";
	
	globaz.ij.vb.prononces.IJSaisirEcheanceViewBean viewBean = (globaz.ij.vb.prononces.IJSaisirEcheanceViewBean)(session.getAttribute("viewBean"));
	
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete =  true;
	bButtonUpdate = true;
	bButtonNew = false;
	
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>

<SCRIPT language="javaScript">

function add() {}

function upd() {
		document.forms[0].elements('userAction').value="ij.prononces.saisirEcheance.modiferEcheance";
		document.forms[0].elements('modifie').value="true";
}

function validate() {
	state = true;
	    
    document.forms[0].elements('userAction').value="ij.prononces.saisirEcheance.modiferEcheance";

    return state;
}

function cancel() {
   document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE_JOINT_DEMANDE%>.chercher";
}

function init(){
}

function typeInfoComplChange(){
}

function del() {
	if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=globaz.ij.servlet.IIJActions.ACTION_INFO_COMPL%>.supprimer";
        document.forms[0].submit();
	}
}



 </SCRIPT>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="SAISIE_ECHEANCE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></TD>
							<TD colspan="3">
								<INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_BASE_IND_NO_PRONONCE"/></TD>
							<TD><INPUT type="text" name="idPrononce" class="disabled" readonly value="<%=viewBean.getIdPrononce()%>"></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>

						<TR>
							<TD><ct:FWLabel key="JSP_DATE_ECHEANCE"/></TD>
							<TD><ct:FWCalendarTag name="dateEcheance" value="<%=viewBean.getDateEcheance()%>"/></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MOTIF_ECHEANCE"/></TD>
							<TD><ct:select name="csMotifEcheance" defaultValue="<%=viewBean.getCsMotifEcheance()%>">
							<ct:optionsCodesSystems csFamille="IJTYPECHEA"/>
							</ct:select></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>