<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.phenix.toolbox.CPToolBox"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
	idEcran="CCP1014";
    globaz.phenix.db.communications.CPReglePlausibiliteViewBean viewBean = (globaz.phenix.db.communications.CPReglePlausibiliteViewBean) session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdPlausibilite();
	userActionValue = "phenix.communications.reglePlausibilite.modifier";
	subTableWidth = "75%";
	bButtonValidate = objSession.hasRight(userActionUpd, globaz.framework.secure.FWSecureConstants.UPDATE);
%>
<SCRIPT language="JavaScript">
top.document.title = "Detail einer Plausibilität"
</SCRIPT>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
function add() {
    document.forms[0].elements('userAction').value="phenix.communications.reglePlausibilite.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="phenix.communications.reglePlausibilite.ajouter";
    else
        document.forms[0].elements('userAction').value="phenix.communications.reglePlausibilite.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="phenix.communications.reglePlausibilite.afficher"
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="phenix.communications.reglePlausibilite.supprimer";
        document.forms[0].submit();
    }
}

function init(){}

function postInit(){
	if (document.forms[0].elements('description_fr').value == "") {
		document.forms[0].elements('description_fr').focus();
	} else if (document.forms[0].elements('description_de').value == "") {
		document.forms[0].elements('description_de').focus();
	} else if (document.forms[0].elements('description_it').value == "") {
		document.forms[0].elements('description_it').focus();
	} else if (document.forms[0].elements('classpath').value == "") {
		document.forms[0].elements('classpath').focus();
	} else if (document.forms[0].elements('priorite').value == "") {
		document.forms[0].elements('priorite').focus();
	}
}

</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Plausibilitätsregel<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
        <TR>
            <TD nowrap width="140">Nummer</TD>
            <TD nowrap width="300"><INPUT name="idPlausibilite" type="text" value="<%=viewBean.getIdPlausibilite()%>" class="numeroCourtDisabled" readonly></TD>
        </TR>
		<TR>
            <TD nowrap width="140">Beschreibung auf französisch</TD>
            <TD nowrap width="300"><INPUT name="description_fr" type="text" data-g-string="mandatory:true" size="80" value="<%=viewBean.getDescription_fr()%>" maxlength="255"> </TD>
   	    </TR>
		<TR>
            <TD nowrap width="140">Beschreibung auf deutsch</TD>
            <TD nowrap width="300"><INPUT name="description_de" type="text" data-g-string="mandatory:true" size="80" value="<%=viewBean.getDescription_de()%>" maxlength="255"> </TD>
   	    </TR>
		<TR>
            <TD nowrap width="140">Beschreibung auf italienisch</TD>
            <TD nowrap width="300"><INPUT name="description_it" type="text"  data-g-string="mandatory:true" size="80" value="<%=viewBean.getDescription_it()%>" maxlength="255"> </TD>
   	    </TR>
   	     <TR>
        	<TD width="140" height="20">Kanton</TD>
           <TD nowrap>
					<select name="canton">
					<%=CPToolBox.getListCantonEtSedex(session,viewBean.getCanton(), true)%>
				</select>
      	</TD>
      	</TR>
	     
	     
		<TR>
            <TD nowrap width="140">Installationsklasse</TD>
            <TD nowrap width="300"><INPUT name="classpath" type="text" data-g-string="mandatory:true" size="80" value="<%=viewBean.getClasspath()%>"  > </TD>
   	    </TR>
		<TR>
            <TD nowrap width="140">Prioritäts-Nr.</TD>
            <TD nowrap width="300"><INPUT name="priorite" type="text" data-g-string="mandatory:true" value="<%=viewBean.getPriorite()%>" class="numeroCourt" > </TD>
   	    </TR>
		<TR>	
            <TD nowrap width="140">Aktivierte Regel</TD>
          <TD nowrap width="300"> <input type="checkbox" name="actif" <%=(viewBean.getActif().booleanValue())? "checked" : "unchecked"%>>
        	&nbsp;&nbsp;Auslösung&nbsp;
	                    	<ct:FWCodeSelectTag name="declenchement"
				      		defaut="<%=viewBean.getDeclenchement()%>"
				      		wantBlank="<%=false%>"
				    	    codeType="CPDECLENCH"/>
              			</TD>
        </TR>

						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>