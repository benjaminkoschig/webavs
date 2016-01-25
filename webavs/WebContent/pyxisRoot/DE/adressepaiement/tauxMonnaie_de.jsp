<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	globaz.pyxis.db.adressepaiement.TITauxMonnaieViewBean viewBean = (globaz.pyxis.db.adressepaiement.TITauxMonnaieViewBean )session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");

%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Währung Détail"
function add() {
	document.forms[0].elements('userAction').value="pyxis.adressepaiement.tauxMonnaie.ajouter"
}
function upd() {
	document.forms[0].elements('idMonnaie').disabled = true;
}
function validate() {
	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.adressepaiement.tauxMonnaie.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.adressepaiement.tauxMonnaie.modifier";
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.adressepaiement.tauxMonnaie.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="pyxis.adressepaiement.tauxMonnaie.supprimer";
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
            <TD nowrap width="125"></TD>
           
            <TD nowrap></TD>
          </TR>
           <TR>
            <TD nowrap width="125">Währung</TD>
            
            <TD nowrap><ct:FWCodeSelectTag name="idMonnaie"
            		defaut="<%=viewBean.getIdMonnaie()%>"
            		codeType="PYMONNAIE"/>
		</TD>
          </TR>
	   <TR>
            <TD nowrap width="125">&nbsp;</TD>
          
          </TR>
	   <TR>
            <TD nowrap width="125">Wechselkurs</TD>
            <TD nowrap><INPUT type="text" name="tauxChange"  doClientValidation="NOT_EMPTY" class="numero" value="<%=viewBean.getTauxChange()%>"> * </TD>
            </TR>
         <TR>
            <TD nowrap width="125">&nbsp;</TD>
          
          </TR>
	   <TR>
            <TD nowrap width="125">Datum des Kurses</TD>
           
             <TD nowrap><ct:FWCalendarTag name="dateValeurTaux" 
			value="<%=viewBean.getDateValeurTaux()%>" 
			doClientValidation="CALENDAR"/>

              * 
              <input type="hidden" name="_creation" tabindex="-1" style="text-transform : capitalize;" class="numero" size="3" maxlength="3" value="test">
            </TD>
          </TR>

  <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>