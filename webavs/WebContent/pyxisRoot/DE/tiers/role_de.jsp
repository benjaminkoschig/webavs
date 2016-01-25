<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0008";
    globaz.pyxis.db.tiers.TIRoleViewBean viewBean = (globaz.pyxis.db.tiers.TIRoleViewBean)session.getAttribute ("viewBean");
    selectedIdValue = request.getParameter("selectedId");

%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%
tableHeight = 300;
lastModification ="";
%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Rolle Détail"

function add() {
    document.forms[0].elements('userAction').value="pyxis.tiers.role.ajouter"
//Initialisation
       document.forms[0].debutRole.value="<%=globaz.globall.util.JACalendar.today().toString()%>";
	fieldFormat(document.forms[0].debutRole,"CALENDAR");
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.tiers.role.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.tiers.role.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.role.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pyxis.tiers.role.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail der Rolle eines Partners<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 

	
          <TR> 
            <TD nowrap >Rolle</TD>
            <TD nowrap> 
             <ct:FWCodeSelectTag name="role"
            		defaut="<%=viewBean.getRole()%>"
            		codeType="PYROLE"/>
            </TD>
            <TD ></TD>
            <TD nowrap ></TD>
            <TD nowrap></TD>
          </TR>
	<TR>
            <TD nowrap><INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>'></TD>
            <TD nowrap ></TD>
            <TD  ></TD>
            <TD nowrap ></TD>
            <TD nowrap ></TD>
          </TR>
	<TR>
            <TD nowrap >Gültig von</TD>
            <TD nowrap><ct:FWCalendarTag name="debutRole" 
		value="<%=viewBean.getDebutRole()%>" 
		errorMessage="la date de début est incorrecte"
		doClientValidation=""
	  />

	 &nbsp;bis&nbsp;
	 <ct:FWCalendarTag name="finRole" 
		value="<%=viewBean.getFinRole()%>"
		errorMessage="la date de fin est incorrecte"
		doClientValidation=""
	 /> </TD>
            <TD width="65"></TD>
            <TD nowrap width="69"></TD>
            <TD nowrap width="66"></TD>
          </TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT> 
<%  }  %>

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">

</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>