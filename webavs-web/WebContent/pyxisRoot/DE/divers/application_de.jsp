<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	globaz.pyxis.db.divers.TIApplicationViewBean viewBean = (globaz.pyxis.db.divers.TIApplicationViewBean)session.getAttribute ("viewBean");
%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
top.document.title = "Tiers - Application détail"
<!--hide this script from non-javascript-enabled browsers
function add() {
	document.forms[0].elements('userAction').value="pyxis.divers.application.ajouter"
}
function upd() {
}
function validate() {
	state = validateFields();
 
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.divers.application.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.divers.application.modifier";
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.divers.application.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="pyxis.divers.application.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>


          <TR>
            <TD nowrap width="125">&nbsp;</TD>
            <TD nowrap></TD>
          </TR>
           <TR>
            <TD nowrap width="150">Benutzer</TD>
            <TD nowrap><INPUT type="text" name="idApplication" tabindex="-1" disabled readonly value="<%=viewBean.getIdApplication()%>" class="numeroCourtDisabled"></TD>

          </TR>
	   <TR>
            <TD nowrap width="150">&nbsp;</TD>
            <TD width="30"></TD>
          </TR>
	   <TR>
            <TD nowrap width="150">Libelle</TD>
            <TD nowrap><INPUT type="text" name="libelleApplication" tabindex="-1" class="libelleLongDisabled" value="<%=viewBean.getSession().getCodeLibelle(viewBean.getIdApplication())%>" disabled readonly><input type="hidden" name="_creation" tabindex="-1" style="text-transform : capitalize;" class="numero" size="3" maxlength="3" value="test">
            </TD>
          </TR>
         <TR>
            <TD nowrap width="150">&nbsp;</TD>
          </TR>
	   <TR>
            <TD nowrap width="150">Fakturierungsklasse</TD>
            <TD nowrap><INPUT type="text" name="classeFacture" tabindex="-1" class="libelleLong" value="<%=viewBean.getClasseFacture()%>">
            </TD>
          </TR>
	<TR>
            <TD nowrap width="150">&nbsp;</TD>
            <TD width="30"></TD>
          </TR>
	   <TR>
            <TD nowrap width="150">Intern</TD>
	     <TD ><input type="checkbox" name="interne" <%=(viewBean.isInterne().booleanValue())? "checked" : "unchecked"%>></TD>
          </TR>
	<TR>
            <TD nowrap width="150">&nbsp;</TD>
          </TR>
	   <TR>
            <TD nowrap width="150">Extern</TD>
	     <TD ><input type="checkbox" name="externe" <%=(viewBean.isExterne().booleanValue())? "checked" : "unchecked"%>></TD>
          </TR>

  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%> <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>