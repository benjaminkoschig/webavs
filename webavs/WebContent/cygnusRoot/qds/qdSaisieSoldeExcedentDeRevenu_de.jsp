<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.qds.RFQdSaisieSoldeExcedentDeRevenuViewBean"%>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_S_SOEXR_D"
	idEcran="PRF0043";

	RFQdSaisieSoldeExcedentDeRevenuViewBean viewBean = (RFQdSaisieSoldeExcedentDeRevenuViewBean) session.getAttribute("viewBean");

	//autoShowErrorPopup = true;
	
	if(viewBean.isNew()){
		viewBean.setIdQd(request.getParameter("idQd"));
	}
	
	tableHeight=200;
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

function add() {
    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU%>.ajouter";
}

function upd() {}

function cancel() {
	if (document.forms[0].elements('_method').value == "add"){
    	document.forms[0].elements('userAction').value="back";
    }else{
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU%>.rechercher";
    }
}  

function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU%>.ajouter";
    }else{
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU%>.modifier";
    }
    return state;
}    

function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_SOLDE_EXCEDENT_DE_REVENU%>.supprimer";
        document.forms[0].submit();
    }
}

function init(){
	<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	errorObj.text="<%=viewBean.getMessage()%>";
	showErrors()
	errorObj.text="";
	<%}%>
}

function postInit(){
	 if (document.forms[0].elements('_method').value == "add"){		
	 	action('add');
	 }
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_S_SOCHA_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD>
				<TABLE border="0" cellspacing="10px" cellpadding="0" align="left">
					<TR>
						<TD><ct:FWLabel key="JSP_RF_S_SOEXR_D_CONCERNE"/></TD>
						<TD>
							<INPUT type="text" name="concerne" value="<%=viewBean.getConcerne()%>" style="width: 500px"/>
							<INPUT type="hidden" name="idQd" value="<%=viewBean.getIdQd()%>" />
						</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_RF_S_SOEXR_D_REMARQUE"/></TD>
						<TD>
							<INPUT type="text" name="remarque" value="<%=viewBean.getRemarque()%>" style="width: 500px"/>					
						</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_RF_S_SOEXR_D_MONTANT"/></TD>
						<TD>
							<INPUT type="text" name="montantSoldeExcedent" value="<%=viewBean.getMontantSoldeExcedent()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"/>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>