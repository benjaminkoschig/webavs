
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.helios.db.comptes.*,globaz.helios.db.interfaces.*" %>

<style type="text/css">
<!--
.txtLink {font-family: Verdana, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; text-decoration: none; margin-left: 0px}
-->
</style>

<!-- Creer l'enregitrement s'il n'existe pas -->
<%	
	idEcran="GCF0021";
	userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficherAnalyseBudgetaire";
	userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifierAnalyseBudgetaire";
	userActionDel = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".supprimerAnalyseBudgetaire";	
	bButtonUpdate = objSession.hasRight(userActionUpd, "UPDATE");
	bButtonDelete = objSession.hasRight(userActionDel, "REMOVE");

    CGAnalyseBudgetaireViewBean viewBean = (CGAnalyseBudgetaireViewBean)session.getAttribute ("viewBean");
    CGExerciceComptableViewBean exerciceComptable = (CGExerciceComptableViewBean)session.getAttribute (CGNeedExerciceComptable.SESSION_EXERCICECOMPTABLE);
	selectedIdValue = viewBean.getIdCompte();
    userActionValue = "helios.comptes.analyseBudgetaire.modifierAnalyseBudgetaire";
           	
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers


function init(){}

function upd() {
}
function validate() {
	
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="helios.comptes.analyseBudgetaire.modifierAnalyseBudgetaire";
    else
        document.forms[0].elements('userAction').value="helios.comptes.analyseBudgetaire.modifierAnalyseBudgetaire";
    
    return state;

}
function cancel() {
		document.forms[0].elements('userAction').value="back";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="helios.comptes.analyseBudgetaire.supprimerAnalyseBudgetaire";
        document.forms[0].submit();
    }    
}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail der fehlerhaften Analyse<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>


	<tr>
	            <TD width="204">Kontonummer</TD>
	            <TD width="488">
		            <input type='text' name='idExterne' class='disabled' readonly value="<%=viewBean.getIdExterne()%>">
	            </TD>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>	

	<tr>
	            <TD width="204">Jährliches Budget</TD>
	            <TD width="488">
		            <input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="libelle" style="text-align : right" name="montantAnalyseBudgetaireAnnuelle" value="<%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getMontantAnalyseBudgetaireAnnuelle().toString(),true,true,false,2)%>"/>
	            </TD>
	</tr>
	<tr><td colspan="2">&nbsp;</td></tr>
						
<%
	for (int i=0;i<viewBean.getPeriodeSize(); i++) {
	
	globaz.globall.parameters.FWParametersSystemCode cs = new globaz.globall.parameters.FWParametersSystemCode();
	cs.setIdCode(viewBean.getIdTypePeriode(i));
	cs.setIdLangue(objSession.getIdLangueISO());
	cs.setSession(objSession);
	cs.retrieve();				
	globaz.globall.parameters.FWParametersUserCode  userCode = cs.getCodeUtilisateur(objSession.getIdLangue());
	
%>		
	<tr>
	            <TD width="204">Budget <%=userCode.getLibelle()%>, code periode : <%=viewBean.getCodePeriode(i)%></TD>
	            <TD width="488">
		            <input onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" class="libelle" style="text-align : right" name="idSolde_<%=viewBean.geIdSoldePeriode(i)%>" value="<%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getMontantBudgetePeriode(i).toString(),true,true,false,2)%>"/>		            
	            </TD>
	</tr>		
<%}%>
             
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