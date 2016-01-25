 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.pavo.db.compte.CIAnnonceCentraleViewBean"%>
<%@ page import="globaz.globall.util.*"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran = "CCI0041";
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<SCRIPT language="JavaScript">
	top.document.title = "<ct:FWLabel key='JSP_CI_ANNONCE_CENTRALE_TITLE_DETAIL' />";
</SCRIPT>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>


<%
    CIAnnonceCentraleViewBean viewBean = (CIAnnonceCentraleViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getAnnonceCentraleId();
	userActionValue = "pavo.compte.annonceCentrale.modifier";
	subTableWidth = "650";
	
	bButtonUpdate = false;
	bButtonDelete = false;
	if(!viewBean.isLectureSeule()){
		bButtonUpdate = true;
		bButtonDelete = true;
	}
		
%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">

<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.compte.annonceCentrale.ajouter"
}

function upd() {
	 document.forms[0].elements('userAction').value="pavo.compte.annonceCentrale.modifier"
}

function validate() {
    state = validateFields();
    
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.compte.annonceCentrale.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.compte.annonceCentrale.modifier";
    
    return state;

}

function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.compte.annonceCentrale.afficher";
}

function del() {
    if (window.confirm("<ct:FWLabel key='JSP_CI_ANNONCE_CENTRALE_DELETE_CONFIRM' />")){
        document.forms[0].elements('userAction').value="pavo.compte.annonceCentrale.supprimer";
        document.forms[0].submit();
    }
}

function init(){}
-->

</SCRIPT>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key='JSP_CI_ANNONCE_CENTRALE_TITLE_DETAIL' /><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

<TR>
    <TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_PERIODE"/></TD>
	<TD><ct:inputText name="moisAnneeCreation" id="moisAnneeCreation" notation="data-g-calendar='mandatory:true,type:month'"/></TD>
</TR>

<TR>
	<TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_TYPE_LOT"/></TD>
	<TD><ct:FWCodeSelectTag name="idTypeAnnonce" codeType="CITYPENAC" wantBlank="false" defaut="<%=viewBean.getIdTypeAnnonce()%>" /></TD>
</TR>


<TR>
	<TD><ct:FWLabel key="JSP_CI_ANNONCE_CENTRALE_QUITTANCER_ANNONCE_HORS_PERIODE"/></TD>
	<TD><input name="annonceHorsPeriodeQuittancee" id="annonceHorsPeriodeQuittancee" type="checkbox"  /> </TD>
</TR>





		  
		  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	}%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>