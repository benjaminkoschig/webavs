<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/aitaglib.tld" prefix="ai" %>
<%
		globaz.ai.db.envoi.AIGedDigitechViewBean viewBean = (globaz.ai.db.envoi.AIGedDigitechViewBean) session.getAttribute("viewBean");
		selectedIdValue = viewBean.getIdGedDigitech();
		userActionValue = "ai.envoi.gedDigitech.modifier";
		if(viewBean.isNew()){
			bButtonDelete = false;
		}
				
		int tabIndex=1;
		
	    btnUpdLabel = objSession.getLabel("MODIFIER");
	    btnDelLabel = objSession.getLabel("SUPPRIMER");
	    btnValLabel = objSession.getLabel("VALIDER");
	    btnCanLabel = objSession.getLabel("ANNUER");
	    btnNewLabel = objSession.getLabel("NOUVEAU");
	    
	    idEcran = "IEN0360";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
    document.forms[0].elements('userAction').value="ai.envoi.gedDigitech.ajouter";
}
function upd() {
}
function validate() {

    state = validateFields();
    <%if (viewBean.isNew()){%>
        document.forms[0].elements('userAction').value="ai.envoi.gedDigitech.ajouter";
    <%} else {%>
        document.forms[0].elements('userAction').value="ai.envoi.gedDigitech.modifier";
    <%}%>
    
    return state;

}
function cancel() {
  document.forms[0].elements('userAction').value="ai.envoi.gedDigitech.afficher";
}
function del() {
	var msgDelete = '<%=globaz.ai.util.AIUtil.javascriptEncode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="ai.envoi.gedDigitech.supprimer";
        document.forms[0].submit();
    }
}


function init(){}

function onChangeNumGed() {
	var isNumer = true;
	var nodeNumGed = document.getElementById('numeroGed');
	isNumer = IsInteger(nodeNumGed.value);
	if (isNumer == false) {
		alert('<%=objSession.getLabel("ERREUR_NUMERIQUE")%>');	
		document.getElementById('numeroGed').value = '';
	}
}

function onChangeNumRetourGed() {
	var isNumer = true;
	var nodeNumRetourGed = document.getElementById('noRetourGed');
	isNumer = IsInteger(nodeNumRetourGed.value);
	if (isNumer == false) {
		alert('<%=objSession.getLabel("ERREUR_NUMERIQUE")%>');	
		document.getElementById('noRetourGed').value = '';
	}
}

function IsInteger(sText){
   var ValidChars = "0123456789'";
   var IsNumber=true;
   var Char;
 
   for (i = 0; i < sText.length && IsNumber == true; i++) 
      { 
      Char = sText.charAt(i); 
      if (ValidChars.indexOf(Char) == -1) 
         {
         IsNumber = false;
         }
      }
   return IsNumber;
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ai:AIProperty key="GED"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Définition --%>
						<TR>
							<TD width="20%"><ai:AIProperty key="LIBELLE"/></TD>
							<TD width="25%">
								<INPUT type="text" name="csDocument" value="<%=objSession.getCodeLibelle(viewBean.getFormule().getDefinitionFormule().getCsDocument())%>" 
									readonly="readonly" class="libelleLongDisabled" tabindex="-1">
								<INPUT type="hidden" name="idFormule" value="<%=request.getParameter("idFormule")%>" tabindex="-1">
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>

						<%-- Séparateur --%>
						<TR><TD width="100%" colspan="5"><HR></TD></TR>

						<%-- Numéro Ged --%>
						<TR>
							<TD width="20%"><ai:AIProperty key="NUMERO_GED"/></TD>
							<TD width="25%">
								<INPUT type="text" class="numero" name="numeroGed" id="numeroGed" 
									value="<%=viewBean.getNumeroGed()%>" tabindex="1" onblur="onChangeNumGed();">
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>
						
						<%-- Numéro retour Ged --%>
						<TR>
							<TD width="20%"><ai:AIProperty key="NUMERO_RETOUR_GED"/></TD>
							<TD width="25%">
								<INPUT type="text" class="numero" name="noRetourGed" id="noRetourGed" 
									value="<%=viewBean.getNoRetourGed()%>" tabindex="<%=++tabIndex%>" onblur="onChangeNumRetourGed();">
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
	<ct:menuChange displayId="options" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="idFormule" value="<%=viewBean.getIdFormule()%>" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="csProvenance" value="<%=globaz.ai.constantes.IConstantes.CS_PRO_ENVOI_FORMULE%>" menuId="optionsAIAdminFormule"/>
<%}%>
<SCRIPT language="javascript">
if (document.forms[0].elements('_method').value != "add") {
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
}
</SCRIPT>
<script language="javascript">
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>