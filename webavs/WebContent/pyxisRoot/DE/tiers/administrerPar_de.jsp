<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran ="GTI0014";
    globaz.pyxis.db.tiers.TIAdministrerParViewBean viewBean = (globaz.pyxis.db.tiers.TIAdministrerParViewBean)session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");

%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Tiers - Administrer par Détail"
function add() {
    document.forms[0].elements('userAction').value="pyxis.tiers.administrerPar.ajouter";

}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pyxis.tiers.administrerPar.ajouter";
    else
        document.forms[0].elements('userAction').value="pyxis.tiers.administrerPar.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.administrerPar.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pyxis.tiers.administrerPar.supprimer";
        document.forms[0].submit();
    }
}


function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Verwaltung eines Partners<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
       
					<TR> 
						<TD nowrap width="100">Verwaltung</TD>
						<TD nowrap> 
							<INPUT name="codeAdministration" type="text" value="<%=viewBean.getCodeAdministration()%>" size="30">
							<INPUT type="hidden" name="idAdministration" value="<%=viewBean.getIdAdministration()%>">
														<%
								Object[] administrationMethodsName= new Object[]{
									new String[]{"setIdAdministration","getIdTiersAdministration"},
									new String[]{"setDescriptionAdministration","getDescriptionAdministration"},
									new String[]{"setCodeAdministration","getCodeAdministration"}
								};
								Object[]  administrationParams = new Object[]{
									new String[]{"codeAdministration","_pos"},
								};
							%>
							<ct:FWSelectorTag 
								name="administrationSelector" 
								
								methods="<%=administrationMethodsName%>"
								providerApplication ="pyxis"
								providerPrefix="TI"
								providerAction ="pyxis.tiers.administration.chercher"
								providerActionParams ="<%=administrationParams %>"
							/>
						</TD>
					</TR>
					<TR>
						<TD nowrap width="132">
							<INPUT type="hidden" name="idTiers" value='<%=viewBean.getIdTiersPersonne()%>'>
						</TD>
						<TD nowrap width="269">
							<TEXTAREA rows="5" width="250" align="left" readonly class="libelleLongDisabled"><%=viewBean.getDescriptionAdministration()%>
							</TEXTAREA>
						</TD>
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

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>