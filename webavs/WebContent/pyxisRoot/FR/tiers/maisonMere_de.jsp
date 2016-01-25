<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0016";
	globaz.pyxis.db.tiers.TISuccursaleViewBean viewBean = (globaz.pyxis.db.tiers.TISuccursaleViewBean )session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("forIdTiersParent");
%>

<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Maison mère Détail"
function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.succursale.maisonAjouter";
//Initialisation
       document.forms[0].nom.value="<%=viewBean.getNom(viewBean.getIdTiersParent())%>";
	document.forms[0].localite.value="<%=viewBean.getLocalite(viewBean.getIdTiersParent())%>";
	document.forms[0].affilie.value="<%=viewBean.getNumAffilie(viewBean.getIdTiersParent())%>";
	document.forms[0].debutRelation.value="<%=globaz.globall.util.JACalendar.today().toString()%>";
	fieldFormat(document.forms[0].debutRelation,"CALENDAR");

}
function upd() {
}
function validate() {

	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.succursale.maisonAjouter";
	else
		document.forms[0].elements('userAction').value="pyxis.tiers.maisonMere.modifier";
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add") {
  document.forms[0].elements('userAction').value="";
  top.fr_appicons.icon_back.click();
  }

 else
  document.forms[0].elements('userAction').value="pyxis.tiers.maisonMere.afficher";
}
function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
//		document.forms[0].elements('userAction').value="pyxis.tiers.succursale.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Définition maison mère/succursale - Détail<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 
          <TR>
            <TD nowrap width="117">Tiers sélectionné</TD>
            <TD nowrap width="274" > 
              <INPUT type="text" name="nom" tabindex="-1" value="<%=viewBean.getNom(viewBean.getIdTiersParent())%>" readonly class="libelleLongDisabled">
	     </TD>
             <TD width="55"></TD>
	        <TD nowrap width="43" align="right">Affilié</TD>
            <TD nowrap colspan="2">
		<INPUT type="text" name="affilie" tabindex="-1" value="<%=viewBean.getNumAffilie(viewBean.getIdTiersParent())%>" readonly  class="libelleLongDisabled">
	     </TD>
	</TR>

	<TR>
            <TD nowrap width="117"></TD>
            <TD nowrap width="274"> 
              <input type="text" name="localite" tabindex="-1" readonly   class="libelleLongDisabled" value="<%=viewBean.getLocalite(viewBean.getIdTiersParent())%>">
            </TD>
            <TD width="55"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
       </TR>
	<TR>
            <TD nowrap width="117"></TD>
            <TD nowrap width="274" height="20"></TD>
            <TD width="55"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
          </TR> 
	<TR>
            <TD nowrap width="117">Est maison mère</TD>
            <TD nowrap colspan="3">
		<input type="checkbox" name="maisonMere" <%=(viewBean.isMaisonMere().booleanValue())? "checked" : "unchecked"%>>
	      <I>(cocher cette case si le tiers sélectionné est la maison mère)</I></TD>
            <TD width="134"></TD>
           
            <TD nowrap width="1"></TD>
       </TR> 
	  <TR>
            <TD nowrap width="117"></TD>
            <TD nowrap width="274" height="20"></TD>
            <TD width="55"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
          </TR> 
	<TR>
            <TD nowrap colspan="2">Choix d'une maison mère ou d'une succursale:</TD>
            <TD width="55"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
          </TR>

	 <TR>
            <TD nowrap width="117"></TD>
            <TD nowrap width="274">
		<INPUT type="text" name="selection" doClientValidation="NOT_EMPTY" value="<%=viewBean.getSelection()%>"> 
	<!--	<INPUT type="button" value="..." onclick="_act.value='pyxis.tiers.maisonMere.afficher';userAction.value='pyxis.tiers.gestion.chercher';submit()"> -->
		<%
			Object[] tiersMethodsName= new Object[]{
				new String[]{"setSelection","getNumAffilieActuel"},
				new String[]{"setIdSelection","getIdTiers"},
			};
			Object[] tiersParams = new Object[]{
				new String[]{"selection","_pos"},
			};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/tiers/maisonMere_de.jsp";	

		%>
			<ct:FWSelectorTag 
				name="tiersSelector" 
				
				methods="<%=tiersMethodsName%>"
				providerApplication ="pyxis"
				providerPrefix="TI"
				providerAction ="pyxis.tiers.tiers.chercher"
				providerActionParams ="<%=tiersParams%>"
				redirectUrl="<%=redirectUrl%>"
			/>
		</TD>
            <TD width="55"><INPUT type="hidden" name="idSelection" value="<%=viewBean.getIdSelection()%>"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
          </TR>               
	<TR>
            <TD nowrap width="117"></TD>
            <TD nowrap width="274">
<TEXTAREA name="descriptionAffilie" rows="5" cols="25" class="inputDisabled" disabled readonly><%=viewBean.getDescriptionTiers(viewBean.getIdSelection())%></TEXTAREA></TD>
            <TD width="55"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
          </TR>
	 <TR>
            <TD nowrap width="117"></TD>
            <TD nowrap width="274" height="20"></TD>
            <TD width="55"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
          </TR> 
	<TR>
            <TD nowrap width="117">Succursale depuis le</TD>
            <TD nowrap width="274"><ct:FWCalendarTag name="debutRelation" 
		value="<%=viewBean.getDebutRelation()%>" 
		errorMessage="la date de début est incorrecte"
		doClientValidation="CALENDAR,NOT_EMPTY"
	  /> &nbsp;au&nbsp; <ct:FWCalendarTag name="finRelation" 
		value="<%=viewBean.getFinRelation()%>"
		errorMessage="la date de fin est incorrecte"
		doClientValidation="CALENDAR"
	 /> </TD>
            <TD width="55"></TD>
            <TD nowrap width="43"></TD>
            <TD nowrap width="134"></TD>
          </TR>
            <TR>
            <TD nowrap width="117"></TD>
            <TD width="274"></TD>
            <TD nowrap width="55"></TD>
            <TD nowrap width="43"><input type="hidden" name="forIdTiersParent" value="<%=viewBean.getIdTiersParent()%>">
		
	     </TD>
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