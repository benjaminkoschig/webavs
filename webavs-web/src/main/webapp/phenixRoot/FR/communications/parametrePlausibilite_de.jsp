<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CCP1008";
    globaz.phenix.db.communications.CPParametrePlausibiliteViewBean viewBean = (globaz.phenix.db.communications.CPParametrePlausibiliteViewBean ) session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdParametre();
	userActionValue = "phenix.communications.parametrePlausibilite.modifier";
	subTableWidth = "75%";
%>
<SCRIPT language="JavaScript">
top.document.title = "Détail d'un paramètre d'une plausibilité"
</SCRIPT>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
function add() {
    document.forms[0].elements('userAction').value="phenix.communications.parametrePlausibilite.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="phenix.communications.parametrePlausibilite.ajouter";
    else
        document.forms[0].elements('userAction').value="phenix.communications.parametrePlausibilite.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="phenix.communications.parametrePlausibilite.afficher"
}
function del() {
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="phenix.communications.parametrePlausibilite.supprimer";
        document.forms[0].submit();
    }
}

function init(){}

</SCRIPT>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail d'un paramètre d'une plausibilité<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
            <TD nowrap width="170">Numéro</TD>
            <TD nowrap width="300"><INPUT name="idParametre" type="text" value="<%=viewBean.getIdParametre()%>" class="numeroCourtDisabled" readonly></TD>
        </TR>
		<TR>
            <TD nowrap width="170">Description en Français</TD>
            <TD nowrap width="547"><INPUT name="description_fr" type="text" size="80" value="<%=viewBean.getDescription_fr()%>" maxlength="255"> </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
            <TD nowrap width="170">Description en Allemand</TD>
            <TD nowrap width="547"><INPUT name="description_de" type="text" size="80" value="<%=viewBean.getDescription_de()%>" maxlength="255"> </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
            <TD nowrap width="170">Description en Italien</TD>
            <TD nowrap width="547"><INPUT name="description_it" type="text"  size="80" value="<%=viewBean.getDescription_it()%>" maxlength="255"> </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
            <TD nowrap width="170">Nom de la methode</TD>
            <TD nowrap width="547"><INPUT name="nomCle" type="text" value="<%=viewBean.getNomCle()%>" class="libelleLong" > </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>
			<TD nowrap width="170">Type message</TD>
	        <TD width="547">
			            	<ct:FWCodeSelectTag name="typeMessage"
				      		defaut="<%=viewBean.getTypeMessage()%>"
				      		wantBlank="<%=false%>"
				    	    codeType="CPNIVERROR"/>
              			</TD>
	        <TD width="50"></TD>
   	    </TR>
 <%-- 		<TR>
            <TD nowrap width="170">Parametre 2</TD>
            <TD nowrap width="547"><INPUT name="valeur" type="text" value="<%=viewBean.getValeur()%>" class="libelleLong" > </TD>
            <TD width="50"></TD>
   	    </TR> --%>
		<TR>
            <TD nowrap width="170">Numéro de priorité</TD>
            <TD nowrap width="547"><INPUT name="priorite" type="text"  value="<%=viewBean.getPriorite()%>" class="numeroCourt" > </TD>
            <TD width="50"></TD>
   	    </TR>
		<TR>	
            <TD nowrap width="170">Règle activée</TD>
            <TD nowrap width="547"><input type="checkbox" name="actif" <%=(viewBean.getActif().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD width="65" height="20"></TD>
        </TR>
		<TR>
			<%
				String idPlausibilite = request.getParameter("idPlausibilite");
				if (idPlausibilite == null) {
					idPlausibilite = viewBean.getIdPlausibilite();
				}
			%>
            <TD nowrap width="300"><INPUT name="idPlausibilite" type="hidden" value="<%=idPlausibilite%>" ></TD>
        </TR>
						
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>