<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.typeDeSoins.RFTypeDeSoinViewBean"%>
<%@page import="globaz.cygnus.utils.RFGestionnaireHelper"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Vector"%>
<%
	idEcran="PRF0026";

	RFTypeDeSoinViewBean viewBean = (RFTypeDeSoinViewBean) session.getAttribute("viewBean");

	autoShowErrorPopup = true;
	
	tableHeight=200;
	
	bButtonDelete = false;
	bButtonValidate = false;
	bButtonCancel = false;
	bButtonNew = false;
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="cygnus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="cygnus-optionsempty" showTab="options"/>
<script language="JavaScript">

function cancel() {
    document.forms[0].elements('userAction').value="back";
}  

function upd(){
}

function init(){

	// le bouton modifier est utile : on simulera un clique sur modifier pour dégriser les champs au moment du postInit
	$('#btnUpd').click();
	$('#btnUpd').hide();
	
	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors()
	errorObj.text="";
	<%}%>

}

function postInit(){
	document.forms[0].method = "get";
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_S_PARAM_SOINS_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

					<TR><TD>&nbsp;</TD></TR>					
                	<%@ include file="../utils/typeSousTypeDeSoinsListes.jspf"%>
                	<TR>
                		<TD colspan="6">
		                	<INPUT type="hidden" name="isSaisieDemande" value="false"/>
		                	<INPUT type="hidden" name="isEditSoins" value="true"/>
		                	<INPUT type="hidden" name="isSaisieQd" value="false"/>	
                		</TD>
                	</TR>
					<TR><TD>&nbsp;</TD></TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>				
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>