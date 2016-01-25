<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
    <%
	    idEcran ="GTI0018";    
		globaz.pyxis.db.tiers.TISuccursaleViewBean viewBean = (globaz.pyxis.db.tiers.TISuccursaleViewBean )session.getAttribute ("viewBean");
		selectedIdValue = viewBean.getIdComposition();
	%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Filiale Détail"
function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.succursale.ajouter";
//Initialisation
	document.forms[0].debutRelation.value="<%=globaz.globall.util.JACalendar.today().toString()%>";
	fieldFormat(document.forms[0].debutRelation,"CALENDAR");
	
}
function upd() {
}
function validate() {

	//state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.succursale.ajouter";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.succursale.modifier";
	return (true);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.succursale.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.succursale.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Filiale - Detail<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 

	 <TR>
            <TD nowrap width="132">Filiale</TD>
           <TD nowrap width="269"><INPUT type="text" name="selection" value="<%=viewBean.getSelection()%>" > 
 <!--           <INPUT type="button" value="..." onclick="_act.value='pyxis.tiers.succursale.afficher';userAction.value='pyxis.tiers.gestion.chercher';submit()"></TD> -->
		  <%
			Object[] tiersMethodsName= new Object[]{
				new String[]{"setSelection","getNumAffilieActuel"},
				new String[]{"setIdSelection","getIdTiers"},
			};
			Object[] tiersParams = new Object[]{
				new String[]{"selection","_pos"},
			};
			%>
			<ct:FWSelectorTag 
				name="tiersSelector" 
				
				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.tiers.chercher"
				providerActionParams ="<%=tiersParams%>"
			/>
            <TD width="65"><INPUT type="hidden" name="idSelection" value="<%=viewBean.getIdSelection()%>"></TD>
            <TD nowrap width="69"></TD>
            <TD nowrap width="66"></TD>
          </TR>               
	<TR>
            <TD nowrap width="132"></TD>
            <TD nowrap width="269"><TEXTAREA name="descriptionAffilie" rows="5" cols="25" class="inputDisabled" readonly><%=viewBean.getDescriptionTiers(viewBean.getIdSelection())%></TEXTAREA></TD>
            <TD width="65"></TD>
            <TD nowrap width="69"></TD>
            <TD nowrap width="66"></TD>
          </TR>
	<TR>
            <TD nowrap width="132"></TD>
            <TD nowrap width="269"></TD>
            <TD width="65"></TD>
            <TD nowrap width="69"></TD>
            <TD nowrap width="66"></TD>
          </TR> 
	<TR>
            <TD nowrap width="132"></TD>
            <TD nowrap width="269"></TD>
            <TD width="65"></TD>
            <TD nowrap width="69"></TD>
            <TD nowrap width="66"></TD>
          </TR>
	<TR>
            <TD nowrap width="132">Filiale depuis le</TD>
            <TD nowrap width="269"><ct:FWCalendarTag name="debutRelation" 
		value="<%=viewBean.getDebutRelation()%>" 
		errorMessage="la date de début est incorrecte"
		doClientValidation="CALENDAR,NOT_EMPTY"
	  />

	 &nbsp;au&nbsp;
	 <ct:FWCalendarTag name="finRelation" 
		value="<%=viewBean.getFinRelation()%>"
		errorMessage="la date de fin est incorrecte"
		doClientValidation="CALENDAR,DATE_COMPARE:debutRelation:GREATER_OR_EQUALS"
	 /> </TD>
            <TD width="65"></TD>
            <TD nowrap width="69"></TD>
            <TD nowrap width="66"></TD>
          </TR>
            <TR>
            <TD nowrap width="132"></TD>
            <TD width="65"></TD>
            <TD nowrap width="69"></TD>
            <TD nowrap width="66">

		<input type="hidden" name="forIdTiersParent" value="<%=viewBean.getIdTiersParent()%>">
	     </TD>
          </TR>      
      <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	}
		

		
		 
%> 

<ct:menuChange displayId="options" menuId="TIMenuVide" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>