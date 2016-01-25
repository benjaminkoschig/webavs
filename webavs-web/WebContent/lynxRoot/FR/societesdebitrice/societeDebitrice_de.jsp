<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GLX0003"; %>
<%@ page import="globaz.lynx.db.societesdebitrice.*" %>
<%
LXSocieteDebitriceViewBean viewBean = (LXSocieteDebitriceViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdSociete();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%><SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="lynx.societesdebitrice.societeDebitrice.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="lynx.societesdebitrice.societeDebitrice.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.societesdebitrice.societeDebitrice.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.societesdebitrice.societeDebitrice.modifier";

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.societesdebitrice.societeDebitrice.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer la société débitrice sélectionnée! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="lynx.societesdebitrice.societeDebitrice.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

function updateMandat(tag) {
	if (tag.select && tag.select.selectedIndex != -1) {
		document.getElementById("libelleMandat").value = tag.select[tag.select.selectedIndex].libelleMandat;
	}
}

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}
}


top.document.title = "Détail de la société débitrice - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D&eacute;tail de la soci&eacute;t&eacute; d&eacute;bitrice<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	     <TR>
            <TD>Num&eacute;ro</TD>
            <TD>
            	<input type="hidden" name="idSociete" value="<%=viewBean.getIdSociete()%>"/>
            	<input type="text" name="idExterne" style="width:7cm" size="21" maxlength="20" value="<%=viewBean.getIdExterne()%>" class="libelle" tabindex="1"/>
            </TD>
          </TR>
		<TR> 
				<TD nowrap  height="11" colspan="2"> 
					<hr size="3" width="100%">
				</TD>
		</TR>
		
		<%
			String jspLocation = servletContext + "/lynxRoot/autocomplete/mandat_select.jsp";
			String params = "";
		%>
		
		<TR>
            <TD>Plan comptable</TD>
            <TD>         
             <ct:FWPopupList name="idMandat" onFailure="onCompteFailure(window.event);" onChange="updateMandat(tag);" validateOnChange="true" params="" value="<%=viewBean.getIdMandat()%>" className="libelle" jspName="<%=jspLocation%>" minNbrDigit="1" autoNbrDigit="2" forceSelection="true" tabindex="1"/> 
             <input id="libelleMandat" type="text" style="width:7cm" size="21" maxlength="20" value="<%=viewBean.getLibelleMandat()%>" class="libelleDisabled" readonly="readonly"/>
             </TD>
        </TR>				
		<TR> 
				<TD nowrap  height="11" colspan="2"> 
					<hr size="3" width="100%">
				</TD>
		</TR>
		<TR>
            <TD>Nom</TD>
            <TD>
              <input type="text" name="nom" style="width:14cm" size="81" maxlength="80" value="<%=viewBean.getNom()%>" class="libelleDisabled" readonly="readonly"/>
           	&nbsp;
            <input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"/>
            <ct:FWSelectorTag
				name="selectTiers"
				methods="<%=globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean.METHODES_SELECT_TIERS%>"
				providerApplication="pyxis"
				providerPrefix="TI"
				providerAction="pyxis.tiers.tiers.chercher"
				target="fr_main"
				redirectUrl="<%=mainServletPath%>"/>
			</TD>
			<TD align="left">
				<%
					if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdTiers())) {
				%>			
					<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link" >Tiers</A>
				<% } else { %>
				&nbsp;
				<% } %>
            </TD>	            
	
		</TR>
		<TR>
            <TD>Adresse postale</TD>
        </TR>
        <TR>
            <TD>
            <TEXTAREA rows="9" cols="30" class="libelleLongDisabled" readonly="readonly"><%=viewBean.getAdresse()%></TEXTAREA>
            </TD>
        </TR>
		<TR> 
				<TD nowrap  height="11" colspan="2"> 
					<hr size="3" width="100%">
				</TD>
		</TR>
		<TR>
            <TD>Lecture optique</TD>
            <TD>
            	<input type="checkbox" id="lectureOptique" name="lectureOptique" <%=(viewBean.isLectureOptique().booleanValue())? "checked = \"checked\"" : ""%>  tabindex="1" />
            </TD>
        </TR>		
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%
	if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
	} else {
%>
<ct:menuChange displayId="options" menuId="LX-Societe" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=viewBean.getIdSociete()%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idSociete' value='<%=viewBean.getIdSociete()%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idExterneSociete' value='<%=viewBean.getIdExterne()%>' checkAdd='no'/>
</ct:menuChange>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>