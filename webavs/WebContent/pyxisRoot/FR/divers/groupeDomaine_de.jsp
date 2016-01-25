<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	globaz.pyxis.db.divers.TIGroupeDomaineViewBean viewBean = (globaz.pyxis.db.divers.TIGroupeDomaineViewBean)session.getAttribute ("viewBean");
	idEcran ="GTI6005";
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<SCRIPT language="JavaScript">
top.document.title = "Tiers - Groupe Domaine détail"
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="pyxis.divers.groupeDomaine.ajouter";
}

function upd() {
}

function validate() {
	state = validateFields();
 
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.divers.groupeDomaine.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.divers.groupeDomaine.modifier";
	return (state);
}

function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.divers.groupeDomaine.afficher";
}

function del() {
	var msgDelete = '<%=globaz.pyxis.util.TIUtil.encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
    if (window.confirm(msgDelete)){
		document.forms[0].elements('userAction').value="pyxis.divers.groupeDomaine.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

/*
*/
// stop hiding -->
</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
		<%-- tpl:put name="zoneTitle" --%>
		<ct:FWLabel key='GROUPE_DOMAINE_DETAIL' />
		<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain"  --%>
          <TR>
            <TD nowrap="nowrap" width="125">&nbsp;</TD>
            <TD nowrap="nowrap"></TD>
          </TR>
          <TR>
            <TD nowrap="nowrap" width="150"><ct:FWLabel key='GROUPE'/></TD>
            <TD nowrap="nowrap">
            	<select class="libelleLong" name="idGroupe">
					<%for(int i=0;i<viewBean.getIdGroupes().length;i++) {%>
						<option value="<%=viewBean.getIdGroupes()[i]%>" 
							<%=((viewBean.getIdGroupes()[i].equals(viewBean.getIdGroupe()))? "selected ":"")%> >
								<%=viewBean.getIdGroupes()[i]%>
						</option>
					<%}%>
				</select>
			</TD>
          </TR>
	   	  <TR>
            <TD nowrap="nowrap" width="150">&nbsp;</TD>
            <TD width="30"></TD>
          </TR>
	   	  <TR>
            <TD nowrap="nowrap" width="150"><ct:FWLabel key='DOMAINE' /></TD>
            <TD nowrap="nowrap">
            	<ct:FWCodeSelectTag name="csDomaine"
					defaut="<%=viewBean.getCsDomaine()%>" 
					codeType="PYAPPLICAT"/>
            </TD>
          </TR>
          <TR>
            <TD nowrap="nowrap" width="150">&nbsp;</TD>
          </TR>
  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
	<%-- tpl:put name="zoneButtons" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
	<%-- tpl:put name="zoneEndPage"  --%>
	 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>