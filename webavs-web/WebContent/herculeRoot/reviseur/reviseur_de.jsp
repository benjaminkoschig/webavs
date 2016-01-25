<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran ="CCE0010";
	globaz.hercule.db.reviseur.CEReviseurViewBean viewBean = (globaz.hercule.db.reviseur.CEReviseurViewBean)session.getAttribute ("viewBean");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="hercule.reviseur.reviseur.ajouter"
}

function upd() {
}

function validate() {
	 state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="hercule.reviseur.reviseur.ajouter";
    else
        document.forms[0].elements('userAction').value="hercule.reviseur.reviseur.modifier";
    
    return state;
}

function cancel() {
	document.forms[0].elements('userAction').value="back";
}

function del() {
	if (window.confirm("<%=objSession.getLabel("CONFIRM_SUPPRESSION_OBJECT")%>")) {
		document.forms[0].elements('userAction').value="hercule.reviseur.reviseur.supprimer";
		document.forms[0].submit();
	}
}
function init() {
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_REVISEUR_DETAIL"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

		<TR>
        	<TD nowrap width="140"><ct:FWLabel key="NUMERO"/></TD>
        	<TD nowrap width="300"><INPUT name="idReviseur" type="text" value="<%=viewBean.getIdReviseur()%>" class="numeroCourtDisabled" readonly="readonly"></TD>
        </TR>
        <TR>
			<TD nowrap width="140"><ct:FWLabel key="VISA"/></TD>
        	<TD nowrap width="300"><INPUT name="visa" type="text" value="<%=viewBean.getVisa()%>" size="7" maxlength="5"></TD>
		</TR>
		<TR>
			<TD nowrap width="140"><ct:FWLabel key="DESCRIPTION"/></TD>
        	<TD nowrap width="300"><INPUT name="nomReviseur" type="text" value="<%=viewBean.getNomReviseur()%>" class="libelleLong" maxlength="40"></TD>
		</TR>
		<TR> 
			<TD nowrap><ct:FWLabel key="TYPE_REVISEUR"/></TD>
			<TD nowrap>
				<ct:FWCodeSelectTag 
              			name="typeReviseur" 
					defaut="<%=viewBean.getTypeReviseur()%>"
					wantBlank="false"
					codeType="VETYPEREVI"/> 
				<SCRIPT>
					document.getElementById("typeReviseur").tabIndex="-1";
				</SCRIPT>									
			</TD>
		</TR>
		<TR> 
       		<TD height="20" width="150"><ct:FWLabel key="ACTIF"/></TD>
       		<TD nowrap height="31" width="259"><input type="checkbox" name="reviseurActif" <%=(viewBean.isReviseurActif().booleanValue())? "checked = \"checked\"" : ""%>></TD>
   		</TR>
					
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>