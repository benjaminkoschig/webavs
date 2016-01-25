<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
		
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.vb.qds.RFQdSaisiePeriodeValiditeQdPrincipaleViewBean"%>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_SAISIE_AUG_D"
	idEcran="PRF0056";

	RFQdSaisiePeriodeValiditeQdPrincipaleViewBean viewBean = (RFQdSaisiePeriodeValiditeQdPrincipaleViewBean) session.getAttribute("viewBean");

	autoShowErrorPopup = true;
	
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

<%@page import="globaz.cygnus.vb.qds.RFQdSaisiePeriodeValiditeQdPrincipaleViewBean"%><script language="JavaScript">

function add() {
    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE%>.ajouter";
  }

function upd() {}

function cancel() {
	if (document.forms[0].elements('_method').value == "add"){
    	document.forms[0].elements('userAction').value="back";
    }else{
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE%>.rechercher";
    }
}  

function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add"){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE%>.ajouter";
    }else{
    	document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE%>.modifier";
    }
    return state;
}    

function del() {
    if (window.confirm("<ct:FWLabel key='WARNING_RF_QD_S_JSP_DELETE_PERIODE_MESSAGE_INFO'/>")){
    	document.forms[0].elements('userAction').value = "<%=IRFActions.ACTION_SAISIE_QD_PERIODE_VALIDITE_QD_PRINCIPALE%>.supprimer";
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

}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_R_QD_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<TR>
			<TD>
				<TABLE border="0" cellspacing="10px" cellpadding="0" align="left">
					<TR>
						<TD><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_QD_CONCERNE"/></TD>
						<TD colspan="5">
							<INPUT type="text" name="concerne" value="<%=viewBean.getConcerne()%>" style="width: 500px"/>
							<INPUT type="hidden" name="idQd" value="<%=viewBean.getIdQd()%>" />
							<INPUT type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>" />
							<INPUT type="hidden" name="idPeriodeValidite" value="<%=viewBean.getIdPeriodeValidite()%>" />
							<INPUT type="hidden" name="anneeQd" value="<%=viewBean.getAnneeQd()%>"/>
						</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_QD_REMARQUE"/></TD>
						<TD colspan="5">
							<INPUT type="text" name="remarque" value="<%=viewBean.getRemarque()%>" style="width: 500px"/>			
						</TD>
					</TR>
					<TR>
						<TD><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_QD_DATE_DEBUT"/></TD>
						<TD><input data-g-calendar=" "  name="dateDebut" value="<%=viewBean.getDateDebut()%>"/></TD>
						<TD><ct:FWLabel key="JSP_RF_S_PERIODE_VALIDITE_QD_DATE_FIN"/></TD>
						<TD colspan="3"><input data-g-calendar=" "  name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
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