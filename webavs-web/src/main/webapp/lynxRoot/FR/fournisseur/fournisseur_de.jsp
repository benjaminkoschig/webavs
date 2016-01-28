<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GLX0002"; %>
<%@ page import="globaz.lynx.db.fournisseur.*" %>
<%
LXFournisseurViewBean viewBean = (LXFournisseurViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdFournisseur();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>
<%@page import="globaz.lynx.service.tiers.LXTiersService"%><SCRIPT language="javascript">

function add() {
	updateBlocage();
    document.forms[0].elements('userAction').value="lynx.fournisseur.fournisseur.ajouter"
}

function upd() {
	updateBlocage();
  	document.forms[0].elements('userAction').value="lynx.fournisseur.fournisseur.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.fournisseur.fournisseur.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.fournisseur.fournisseur.modifier";

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.fournisseur.fournisseur.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer le fournisseur sélectionné! Voulez-vous continuer?")) {
        document.forms[0].elements('userAction').value="lynx.fournisseur.fournisseur.supprimer";
        document.forms[0].submit();
    }
}

function updateBlocage() {

	for (i = 0; i < Number(document.getElementById("csMotifBlocage").options.length-1); i++){
		if(document.getElementById("csMotifBlocage").options[ Number(i+1) ].value == "0") {
			document.getElementById("csMotifBlocage").options[ Number(i+1) ] = null;
		}
	}

	if(document.getElementById("estBloque").checked) {
		document.getElementById("csMotifBlocage").disabled = false;

	}else {
		var length = document.getElementById("csMotifBlocage").options.length ;
		document.getElementById("csMotifBlocage").options[length]= new Option('', '0');
		document.getElementById("csMotifBlocage").options.selectedIndex = length;
		document.getElementById("csMotifBlocage").disabled = true;
	}
}

function init() {

	updateBlocage();
}

top.document.title = "Détail du fournisseur - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D&eacute;tail du fournisseur<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	     <TR>
            <TD>Num&eacute;ro</TD>
            <TD>
            	<input type="hidden" name="idFournisseur" value="<%=viewBean.getIdFournisseur()%>"/>
                <input type="text" name="idExterne" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getIdExterne()%>" class="libelle" tabindex="1"/>
            </TD>
            <TD align="left">
            	<%
					String GEDText = "&nbsp;";
            		String gedFolderType = "";
            		String gedServiceName = "";

            		try {
	            		globaz.globall.api.BIApplication lynxApplication = globaz.globall.api.GlobazSystem.getApplication(globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX);
	            		gedFolderType = lynxApplication.getProperty("ged.folder.type", "");
	            		gedServiceName = lynxApplication.getProperty("ged.servicename.id", "");
	            	} catch (Exception e){
	            		// Le reste de la page doit tout de même fonctionner
	            	}

					if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {
						GEDText = "<a href=\"#\" onclick=\"window.open('" + servletContext +
							"/naos?userAction=naos.affiliation.affiliation.gedafficherdossier&amp;lynx.fournisseur.idExterne=" + viewBean.getIdExterne() +
							"&amp;serviceNameId=" + gedServiceName +
							"&amp;gedFolderType=" + gedFolderType +
							"&amp;idRole=" + LXTiersService.ROLE_FOURNISSEUR +
							"')\"  class=\"external_link\">GED</a>";
					}
				%>
			<ct:ifhasright element="naos.affiliation.affiliation.gedafficherdossier" crud="r">
			<%=GEDText%>
			</ct:ifhasright>

            </TD>
          </TR>
         <TR>
            <TD>Cat&eacute;gorie&nbsp;</TD>
            <TD colspan="2">
				<ct:FWCodeSelectTag name="csCategorie" defaut="<%=viewBean.getCsCategorie()%>" codeType="LXCATEG" wantBlank="true" tabindex="1"/>
            </TD>
        </TR>
		<TR>
			<TD height="11" colspan="3">
				<hr size="3" width="100%">
			</TD>
		</TR>
		<TR>
            <TD>Nom</TD>
            <TD>
              <input type="text" name="nom" style="width:7cm" size="41" maxlength="40" value="<%=viewBean.getNom()%>" class="libelleDisabled" readonly="readonly" />
            &nbsp;
            <input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>"/>
            <ct:FWSelectorTag
				name="selectTiers"
				methods="<%=globaz.lynx.db.fournisseur.LXFournisseurViewBean.METHODES_SELECT_TIERS%>"
				providerApplication="pyxis"
				providerPrefix="TI"
				providerAction="pyxis.tiers.tiers.chercher"
				target="fr_main"
				redirectUrl="<%=mainServletPath%>" tabindex="1"/>
			</TD>
			<TD align="left">
				<%
					if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdTiers())) {
				%>
					<A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdTiers()%>" class="external_link">Tiers</A>
				<% } else { %>
				&nbsp;
				<% } %>
            </TD>
		</TR>
		<TR>
            <TD>Compl&eacute;ment</TD>
            <TD colspan="2">
              <input type="text" name="complement" style="width:7cm" size="41" maxlength="40" value="<%=viewBean.getComplement()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR>
        <TR>
            <TD>Adresse</TD>
            <TD colspan="2">Adresse de paiement</TD>
        </TR>
        <TR>
            <TD>
            <TEXTAREA rows="9" cols="30" class="libelleLongDisabled" readonly="readonly"><%=viewBean.getAdresse()%></TEXTAREA>
            </TD>
            <TD colspan="2">
            <TEXTAREA rows="9" cols="30" class="libelleLongDisabled" readonly="readonly"><%=viewBean.getAdressePaiement()%></TEXTAREA>
            </TD>
        </TR>
		<TR>
			<TD height="11" colspan="3">
				<hr size="3" width="100%">
			</TD>
		</TR>
		<TR>
            <TD title="numéro d'identification des entreprises (IDE)">No de contribuable TVA / IDE</TD>
            <TD colspan="2" title="numéro d'identification des entreprises (IDE)">
              <input type="text" name="noTva" style="width:7cm" size="7" maxlength="20" value="<%=viewBean.getNoTva()%>" class="libelle" tabindex="1"/>
            </TD>
        </TR>
		<TR>
            <TD>Bloquer paiement(s)</TD>
            <TD colspan="2">
				<input type="checkbox" id="estBloque" onclick="javascript:updateBlocage();" tabindex="1" name="estBloque" <%=(viewBean.isEstBloque().booleanValue())? "checked = \"checked\"" : ""%> />
			</TD>
        </TR>
		<TR>
            <TD>Motif du blocage</TD>
            <TD colspan="2">
				<ct:FWCodeSelectTag name="csMotifBlocage" defaut="<%=viewBean.getCsMotifBlocage()%>" codeType="LXMOTIFBL" wantBlank="true" tabindex="1"/>
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
<ct:menuChange displayId="options" menuId="LX-Fournisseur" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=viewBean.getIdFournisseur()%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idFournisseur' value='<%=viewBean.getIdFournisseur()%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idExterne' value='<%=viewBean.getIdExterne()%>' checkAdd='no'/>
	<ct:menuSetAllParams key='lynx.fournisseur.idExterne' value='<%=viewBean.getIdExterne()%>' checkAdd='no'/>
</ct:menuChange>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>