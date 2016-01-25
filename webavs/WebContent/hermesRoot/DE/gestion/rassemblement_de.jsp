<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
globaz.hermes.db.gestion.HERassemblementViewBean viewBean = (globaz.hermes.db.gestion.HERassemblementViewBean)session.getAttribute("viewBean");

bButtonValidate = false;
bButtonUpdate = false;
if(!globaz.pavo.util.CIUtil.isSpecialist(session)){ 
	bButtonDelete = false; 
}
idEcran="GAZ0021";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript"> 
function add() {}

function init(){}

function upd() {}

function validate() {
    var state = true;
    return state;
} 

function cancel() {
  	document.forms[0].elements('userAction').value="back";
}

function del() {
	if (window.confirm("Voulez-vous vraiment supprimer cet objet ?")){
	     document.forms[0].elements('userAction').value="hermes.gestion.rassemblement.supprimer";
       	 document.forms[0].submit();
	 }
}

// fontion permettant d'afficher le bouton annuler
function showCancelBtn() {
	//document.getElementById("btnCan").active = "true";
	activateButton(document.getElementById("btnCan"));
}

// "surcharge" de la méthode doInitThings 
// ajouter l'affichage du bouton annuler
var oldDoInit = doInitThings;
doInitThings = function () {
	oldDoInit();
	showCancelBtn();
}

</SCRIPT>
<ct:menuChange displayId="options" menuId="HE-RassemblementOptions" showTab="options">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdAttenteRetour()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des erwartenden IK-Auzugs<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="100px">SVN : &nbsp;</TD>
							<TD><input class="disabled" readonly type="text" name="numAvs" size="16" tabindex="-1" value="<%=(viewBean.getNumAVS().equals(""))?"&nbsp;": globaz.commons.nss.NSUtil.formatAVSNew(viewBean.getNumAVS(), viewBean.getNumeroAvsNNSS().equals("true"))%>"></td>
							<TD>&nbsp;</TD>
						</TR>
						<TR><TD>Kasse :&nbsp;</TD>
						<TD><input class="disabled" readonly type="text" name="numCaisse" size="8" tabindex="-1" value="<%=viewBean.getNumCaisse()%>"></TD>
						<TD>&nbsp;</TD>
						</TR>
						<TR><TD>SZ :&nbsp;</TD>
						<TD><input class="disabled" readonly type="text" name="Motif" size="4" tabindex="-1" value="<%=viewBean.getMotif()%>"></TD>
						<TD>&nbsp;</TD>
						</TR>	
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>