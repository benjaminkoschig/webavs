<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.envoi.business.services.models.parametrageEnvoi.SimpleDefinitionFormuleService"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- @ taglib uri="/WEB-INF/amtaglib.tld" prefix="ai" --%>

<%
	AMImportformuleViewBean viewBean = (AMImportformuleViewBean) session.getAttribute("viewBean");
	userActionValue = "amal.formule.formule.exporter";
	selectedIdValue = request.getParameter("selectedId");
	idEcran = "AM0015";
	String btnImpEnLabel = objSession.getLabel("MENU_OPTION_FORMULES_IMPORTER");
	String isBatch = viewBean.isBatch();
	// Importer fichier xml
	formAction= request.getContextPath()+ mainServletPath+"Root/formule/importation_upload_de.jsp?selectedId="+selectedIdValue+"&isBatch="+isBatch+"";
	
	boolean myError = false;
	
	boolean isNew = false;
	
	boolean hasRightNew = objSession.hasRight(userActionNew, FWSecureConstants.ADD);
	
	int tabIndex=1;

    lastModification = "";
	bButtonUpdate   = false;
	bButtonDelete   = false;	
	bButtonValidate = hasRightNew;
    btnUpdLabel = objSession.getLabel("MODIFIER");
    btnDelLabel = objSession.getLabel("SUPPRIMER");
    btnValLabel = objSession.getLabel("VALIDER");
    btnCanLabel = objSession.getLabel("ANNULER");
    btnNewLabel = objSession.getLabel("NOUVEAU");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%--<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %> --%>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<SCRIPT type="text/javascript" src="<%=servletContext%>/scripts/onglet.js"></SCRIPT>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->

function onClickImporter(){
	alert("Importer:" );
    //state = validateFields();selectedId
    //document.getElementById('selectedId').value="importer";
    document.getElementById('action').value="importer";
    document.getElementById('fileInput').value= document.getElementById('fileName').value;
    document.forms[0].elements('userAction').value="amal.formule.formule.exporter";
	//chargeUrl(state);
	alert("action:" + document.forms[0].elements('userAction').value );
	action(COMMIT);
}
function init(){
}
function cancel(){}
function upd() {
}
$(function(){
	$('.lastModification').hide();
});

/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put  viewBean.getDefinitionFormule().getCsDocument()--%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ai:AIProperty key="DETAIL_FORMULE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%@page import="globaz.amal.vb.formule.AMImportformuleViewBean"%>
						<%-- tpl:put name="zoneMain" --%>

<INPUT type="hidden" name="selectorName" value="">
<ct:inputHidden name="selectedId"/>
<ct:inputHidden name="modeGeneration"/>
<ct:inputHidden name="fileInput"/>
<ct:inputHidden name="action"/>					
<%-- Définiton --%>
	<%-- Définition Formule --%>
	<TR>
		<TD width="20%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_FORMULE"/></TD>
		<TD width="25%">
			<INPUT type="text" name="libelleDocument" value="<%=viewBean.getFormuleList().getFormule().getLibelleDocument()%>" readonly="readonly" class="libelleLongDisabled">
		</TD>
		<TD width="55%" colspan="3">&nbsp;</TD>
	</TR>
	<TR>
		<TD width="20%"><ct:FWLabel key="JSP_AM_PARAM_LIB"/></TD>
		<TD width="25%">
			<INPUT type="text" name="csDocument" value="<%=objSession.getCodeLibelle(viewBean.getFormuleList().getDefinitionformule().getCsDocument())%>" readonly="readonly" class="libelleLongDisabled">
		</TD>
		<TD width="55%" colspan="3">&nbsp;</TD>
	</TR>
		<TD>&nbsp;</TD>
	</TR>


	<tr>
		<TD width="20%">Type</TD>
		<td width="80%"><%=objSession.getCodeLibelle(viewBean.getFormuleList().getFormule().getCsSequenceImpression())%></td>
	</tr>
	
	<tr><td>&nbsp;</td></tr>
	
	<TR>
		<TD width="10%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_SOURCE"/></TD>
		
		<TD width="70%">
			<%--<ct:inputText name="source" styleClass="libelleLong" style="text-align: left;" tabindex="1" id="source"/>--%>
			<input align="left" type="file" name="fileName" id="fileName"  style="width : 450px;"  class="libelleLong" tabindex="1">
		</TD>
		
		<TD width="20%"><INPUT type="button" style="" class="btnCtrl" id="btnImpEn" value="<%=btnImpEnLabel%>" onclick="onClickImporter();" tabindex="3"></TD>

	</TR>
	<tr><td>&nbsp;</td></tr>

								
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%--<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
	<ct:menuChange displayId="options" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="idFormule" value="<%=viewBean.getId()%>" menuId="optionsAIAdminFormule"/>
	<ct:menuSetAllParams key="csProvenance" value="13" menuId="optionsAIAdminFormule"/>
<%}%>--%>
<%if(!("add".equalsIgnoreCase(request.getParameter("_method")))){%>
	<ct:menuChange displayId="options" menuId="amal-optionsformules"/>
	<ct:menuSetAllParams key="idFormule" value="viewBean.getIdFormule()" menuId="amal-optionsformules"/>
	<ct:menuSetAllParams key="csProvenance" value="globaz.ai.constantes.IConstantes.CS_PRO_ENVOI_FORMULE" menuId="amal-optionsformules"/>
<%}%>
<SCRIPT language="javascript">
if (document.forms[0].elements('_method').value != "add") {
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
}
</SCRIPT>
<script language="javascript">
	document.forms[0].encoding = "multipart/form-data";
	document.forms[0].method = "post";
	reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
	document.getElementById('btnVal').tabIndex='<%=++tabIndex%>';
	document.getElementById('btnCan').tabIndex='<%=++tabIndex%>';
	document.getElementById("fileName").disabled=false;
	document.getElementById("fileName").readOnly=false;
	action(UPDATE);

</SCRIPT>
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
