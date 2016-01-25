<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.globall.util.*, globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*, globaz.helios.translation.*" %>
<%
	idEcran="GCF4008";
	CGCentreChargeViewBean viewBean = (CGCentreChargeViewBean)session.getAttribute ("viewBean");
	CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean)session.getAttribute (CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
	String mandat = viewBean.getIdMandat();
	if (globaz.jade.client.util.JadeStringUtil.isBlank(mandat))
		mandat = exerciceComptable.getIdMandat();
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="helios.comptes.centreCharge.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.centreCharge.ajouter";
    else
        document.forms[0].elements('userAction').value="helios.comptes.centreCharge.modifier";
    
    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="helios.comptes.centreCharge.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.comptes.centreCharge.supprimer";
        document.forms[0].submit();
    }
}

function init(){
}

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Kostenstellendetail<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<tr>
<td>Nummer</td>
<td> <input name='idCentreCharge' <%=((request.getParameter("_method")!=null)&&(request.getParameter("_method").equals("add")))?"class='libelle'":"readonly class='disabled'"%> value='<%=viewBean.getIdCentreCharge()%>'> 
	 <input name='idMandat' type="hidden" value='<%=mandat%>'></td>
</tr>
<tr>
<td>Bezeichnung DE</td>
<td> <input name='libelleDe' class='libelleLong' value="<%=viewBean.getLibelleDe()%>"> </td>
</tr>
<tr>
<td>Bezeichnung FR</td>
<td> <input name='libelleFr' class='libelleLong' value="<%=viewBean.getLibelleFr()%>"> </td>
</tr>
<tr>
<td>Bezeichnung IT</td>
<td> <input name='libelleIt' class='libelleLong' value="<%=viewBean.getLibelleIt()%>"> </td>
</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>