<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<% idEcran="IEN0260"; %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="javaScript">
	usrAction = "amal.formule.formule.lister";
	bFind=true;

function onExportHtml() {
	var exportAction;
	var oldUserAction = document.forms[0].elements.userAction.value;
	exportAction = oldUserAction.substring(0, oldUserAction.lastIndexOf('.')) + '.exporterHtml';
	setUserAction(exportAction);
	var oldSubmit = document.forms[0].onsubmit;
	document.forms[0].onsubmit = "onClickFind();"
	document.forms[0].submit();
	setUserAction(oldUserAction);
	document.forms[0].onsubmit = oldSubmit;
}

function disableButtons(toDisable){
	recursiveSearch(document.getElementsByTagName('BODY')[0], toDisable);
}

function recursiveSearch(node, toDisable){
	if(node != null){
		for(var i = 0; i < node.children.length; i++){
			recursiveSearch(node.children[i], toDisable);
		}
		disableInputButton(node, toDisable);
	}
}

function disableInputButton(node, toDisable){
	if(node.nodeName == 'INPUT'){
		if(node.type == 'button' || node.type == 'submit'){
			node.onClick= function(){};
			if(toDisable == 'true'){
				node.disabled=true;
			} else{
				node.disabled=false;
			}
		}
	}
}

function onClickNew() {
	disableButtons('true');
	<%-- A surcharger document.location.href --%>
}

function onClickFind() {
	disableButtons('true');
}

function showButtons() {
	disableButtons('false');
}

</SCRIPT>
<%

btnFindLabel = objSession.getLabel("RECHERCHER");
btnNewLabel = objSession.getLabel("NOUVEAU");
btnExportLabel = objSession.getLabel("EXPORT");
%>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu"/>
<%--<SCRIPT>
	reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
</SCRIPT>--%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
					<ct:FWLabel key="JSP_AM_PARAMETRAGE_R_TITRE"/>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<%-- Formule --%>
						<TR>
							<TD width="20%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_FORMULE"/></TD>
							<TD width="25%"><input style="text-transform: uppercase;" type="text" name="parametreModelComplexSearch.forNomWord"/></TD>
							<TD width="10%">&nbsp;</TD>
							<TD width="20%"><ct:FWLabel key="JSP_AM_PARAM_LIB"/></TD>
							<TD width="25%"><input type="text" data-g-string="firstCaseUpper:true" name="parametreModelComplexSearch.forlibelleDocument"/></TD>						
						</TR>
						
						<%-- Langue --%>
						<TR>
							<TD width="20%"><ct:FWLabel key="JSP_AM_PARAMETRAGE_R_LANGUE"/></TD>
							<TD width="25%">
								<ct:FWCodeSelectTag name="parametreModelComplexSearch.forLangue"
						    	  defaut="503001"
					              codeType="PYLANGUE" wantBlank="false"/>
							</TD>
							<TD width="55%" colspan="3">&nbsp;</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<SCRIPT language="JavaScript">
	<%-- bouton Editer liste --%>
	<%--	ancre = document.getElementsByName('mainForm')[0];
	body = ancre.children[0].children[0];
	if(body!=null){
		lastTr = body.children[0];
		for(var i = 0; i<body.children.length; i++){
			lastTr = body.children[i];
		}
		theTd = lastTr.children[0];
		inputElement = document.createElement('INPUT');
		inputElement.type='button';
		inputElement.name='btnExportHtml';
		inputElement.id='btnExportHtml';
		inputElement.value='<%=objSession.getLabel("EDITER_LISTE")%>';

        inputElement.onclick = function() { document.forms[0].target='new'; onExportHtml(); document.forms[0].target='fr_list'; } ;
		theTd.appendChild(inputElement);
	} --%>
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>