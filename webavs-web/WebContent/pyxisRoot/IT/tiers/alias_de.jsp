<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%

	idEcran ="GTI0010";


    globaz.pyxis.db.tiers.TIAliasViewBean viewBean = (globaz.pyxis.db.tiers.TIAliasViewBean)session.getAttribute ("viewBean");
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
top.document.title = "Tiers - Detto.. Détail"

function add() {
    document.forms[0].elements('userAction').value="pyxis.tiers.alias.ajouter"
//Initialisation
       document.forms[0].debutRole.value="<%=globaz.globall.util.JACalendar.today().toString()%>";
	fieldFormat(document.forms[0].debutRole,"CALENDAR");
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.tiers.alias.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.tiers.alias.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.alias.afficher";
}
function del() {
    if (window.confirm("Siete in fase di sopprimere l'oggetto selezionato! Volete continuare?")){
        document.forms[0].elements('userAction').value="pyxis.tiers.alias.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detto..<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 

	
		  <TR>
	            <TD nowrap width="132"> <INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiers()%>'>
		    </TD>
	            <TD nowrap width="269"></TD>
	            <TD width="65" height="20"></TD>
	            <TD nowrap width="69"></TD>
	            <TD nowrap width="66"></TD>
	          </TR>	
	          <TR> 
	            <TD nowrap width="100">Detto..</TD>
	            <TD nowrap> 
	             <INPUT name="libelleAlias" type="text" value="<%=viewBean.getLibelleAlias()%>" size="30" maxlength="30">
	            </TD>
	            <TD width="50"></TD>
	            <TD nowrap width="100"></TD>
	            <TD nowrap width="125"></TD>
	          </TR>
		  <TR>
	            <TD nowrap width="132"></TD>
	            <TD nowrap width="269"></TD>
	            <TD width="65" height="20"></TD>
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