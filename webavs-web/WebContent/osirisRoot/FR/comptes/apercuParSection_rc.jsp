<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0011";
rememberSearchCriterias = true;
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.translation.*" %>
<%@ page import="globaz.jade.client.util.*" %>
<%
bButtonNew = false;
CACompteAnnexeViewBean viewBean = (CACompteAnnexeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
String gedFolderType = "";
String gedServiceName = "";
try {
	globaz.globall.api.BIApplication osiApp = globaz.globall.api.GlobazSystem.getApplication(globaz.osiris.application.CAApplication.DEFAULT_APPLICATION_OSIRIS);
	gedFolderType = osiApp.getProperty("ged.folder.type", "");
	gedServiceName = osiApp.getProperty("ged.servicename.id", "");
} catch (Exception e){
	// Le reste de la page doit tout de même fonctionner
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuParSection.lister";
bFind = true;
<!--hide this script from non-javascript-enabled browsers
naviShortKeys[82] = 'r1_c2';//  R (paramètres)
naviShortKeys[73] = 'r2_c2';//  I (incréments)
naviShortKeys[76] = 'r3_c2';//  L (libellés)
naviShortKeys[84] = 'r4_c2';//  T (inc. types)

function removeParam(str_source, str_param) {
  var result = str_source;
  var paramPos = result.indexOf(str_param);
//   if no param, do nothing
  if (paramPos < 0)
    return result;

  nextParamPos = result.indexOf("&", paramPos + 1);
  var str_end = "";
  if (nextParamPos > -1)//   there are more parameters after this one
    str_end = result.slice(nextParamPos);

  result = result.slice(0, paramPos);
  result += str_end;

//  alert ("returning " + result);
  return result;
}


top.document.title = "Comptes - Recherche du compte annexe par section - " + top.location.href;
// stop hiding -->
function updateCompteAnnexe(data) {
	arrayOfStrings = data.split("#");
	var compteId = "";
	var numAffilie = "";
	var role = "";

	if (arrayOfStrings.length>=1) {
		compteId = arrayOfStrings[0];
	}
	if (arrayOfStrings.length>=2) {
		numAffilie = arrayOfStrings[1];
	}
	if (arrayOfStrings.length>=3) {
		role = arrayOfStrings[2];
	}

	document.forms[0].elements('forIdCompteAnnexe').value=compteId;
	document.forms[0].elements('numAffInv').value=numAffilie;
	document.forms[0].elements('role').value=role;

	document.all('btnFind').click();

	parent.fr_main.location.href = "<%=(servletContext + mainServletPath)%>?userAction=osiris.comptes.apercuParSection.chercher&idCompteAnnexe="+compteId;

}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Recherche du compte annexe par section<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
				<TR>
				<TD valign="top" width="128">Compte
				<input type="hidden" name="id" value="<%=viewBean.getIdCompteAnnexe()%>"/>
				<input type="hidden" name="forIdCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"/>
				<INPUT type="hidden" name="numAffInv" value="<%=viewBean.getIdExterneRole()%>"/>
				<INPUT type="hidden" name="role" value="<%=viewBean.getRole().getDescription()%>"/>
				</TD>
				<TD valign="top" width="280">
				<%
 		  		Object[] compteAnnexe = viewBean.getListeCompteAnnexe();
 		  		if (compteAnnexe.length>1) {%>
 		  					<select name="numAffilieList"
							onchange="updateCompteAnnexe(this.options[this.selectedIndex].value);">
							<%
	 				for(int i=0;i<compteAnnexe.length;i++) {
	 					String [] link = (String[])compteAnnexe[i];
	 					if(!"".equals(link[0])) {
	 							if(link[0].equals(viewBean.getIdCompteAnnexe())){%>
	 							<option SELECTED value="<%=link[0]+"#"+link[1]+"#"+link[2]%>">
							<%=link[1] + " " + link[2]%></option>
	 							<%}else{%>
							<option value="<%=link[0]+"#"+link[1]+"#"+link[2]%>">
							<%=link[1] + " " + link[2]%></option>
							<% }
	                  }
	                  } %>
	                  </select><BR>
	                 <% }%>
	            <TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=viewBean.getTitulaireEnteteForCompteAnnexeParSection()%></TEXTAREA>
				</TD>
				<TD valign="top" width="100">Période
				<BR/><BR/><BR/>Solde ouvert
				<BR/><BR/><BR/>Contentieux bloqué
				</TD>
				<TD valign="top">
				<input type="text" name="DateDebutDateFin" size="25" maxlength="25" value="<%=viewBean.getRole().getDateDebutDateFin(viewBean.getIdExterneRole())%>" class="libelleLongDisabled" tabindex="-1" readonly>
				<BR/><input type="text" name="solde_ouvert" size="25" maxlength="25" value="<%=viewBean.getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly>
				<BR/><BR/><input type="text" name="contentieux" size="25" maxlength="25" value="<%=viewBean.getEtatContentieux() %>" class="libelleLongDisabled" tabindex="-1" readonly>
				</TD>
				<TD valign="top" align="right" width="100">
				<%
             		String gedAffilieNumero = viewBean.getIdExterneRole();
             		String gedNumAvs = viewBean.getTiers().getNumAvsActuel();
             		String gedIdTiers = viewBean.getIdTiers();
             		String gedIdRole = viewBean.getIdRole();
             	%>
				
				<%@ include file="/theme/gedCall.jspf" %>
				
				<BR/><BR/><BR/>Sections&nbsp;
				<BR/><BR/><BR/>Tri&nbsp;
				</TD>
				<TD valign="top">
				<BR/><BR/><BR/><select name="forSelectionSections" class="libelleCourt">
				<option value="5" selected>en cours ...</option>
                <option value="1000">toutes</option>
                <option value="1">ouvertes</option>
                <option value="2">sold&eacute;es</option>
              </select>
               <%
            // HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
            java.util.HashSet set = new java.util.HashSet();
			set.add("233003");
		    set.add("233004");
			set.add("233005");
			set.add("233006");
			set.add("233007");
			%>
              	<BR/><BR/><ct:FWCodeSelectTag name="forSelectionTri" defaut="" codeType="OSITRISEC" wantBlank="false" except="<%=set%>"/>
				</TD>
				<TD>&nbsp;</TD>
				</TR>

          <TR>
            <TD colspan="6" align="right"><%if (viewBean.isMotifExistant(CACodeSystem.CS_IRRECOUVRABLE)){%> <IMG src="<%=servletContext%>/images/irrecouvrable.gif" title="Irrécouvrable"><%}%><%if (viewBean.isMotifExistant(CACodeSystem.CS_RENTIER)){%> <IMG src="<%=servletContext%>/images/rentier.gif" title="Rentier"><%}%></TD>
          </TR>
          <TR>
            <TD>Positionnement</TD>
            <TD nowrap>
              <input type="text" name="fromPositionnement" class="libelleStandard">
            </TD>
            <% if (!JadeStringUtil.isDecimalEmpty(viewBean.getInformation())) { %>

            <TD>Information</TD>
            <TD>
	          	<INPUT style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, viewBean.getInformation())%>" class="inputDisabled" tabindex="-1" readonly>
            </TD>
            <TD>&nbsp;</TD>
              <% } else { %>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
           <% } %>
           <TD>&nbsp;</TD>
          </TR>

          <TR>
          <TD colspan="6" align="right">
          <%
				String idContentieuxSrc = request.getParameter("idContentieuxSrc");
				String libSequence = request.getParameter("libSequence");
				String idAdministrateurSrc = request.getParameter("idAdministrateurSrc");

				if (globaz.jade.client.util.JadeStringUtil.isNull(idContentieuxSrc)) {
					idContentieuxSrc = "";
				}

				if (globaz.jade.client.util.JadeStringUtil.isNull(libSequence)) {
					libSequence = "";
				}

				if (globaz.jade.client.util.JadeStringUtil.isNull(idAdministrateurSrc)) {
					idAdministrateurSrc = "";
				}

				if (!globaz.jade.client.util.JadeStringUtil.isBlank(idContentieuxSrc)) {
			%>
				<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.poursuite.contentieux.afficher&refresh=true&libSequence=<%=libSequence%>&selectedId=<%=idContentieuxSrc%>" class="external_link">Contentieux</A>
			<%
				} else if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdministrateurSrc)) {
			%>
				<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=<%=idAdministrateurSrc%>" class="external_link">Administrateur</A>
			<%
				} else {
			%>
				&nbsp;
			<%
				}
			%>

			<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier">
				<ct:menuSetAllParams key="idContentieuxSrc" value="<%=idContentieuxSrc%>"/>
				<ct:menuSetAllParams key="libSequence" value="<%=libSequence%>"/>
				<ct:menuSetAllParams key="idAdministrateurSrc" value="<%=idAdministrateurSrc%>"/>
				<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
				<ct:menuSetAllParams key="forIdExterneRoleLike" value="<%=viewBean.getIdExterneRole()%>"/>
				<ct:menuSetAllParams key="forIdRole" value="<%=viewBean.getIdRole()%>"/>
			</ct:menuChange>

			</TD>
			<TD>&nbsp;</TD>
			</TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<!--            <TD bgcolor="#FFFFFF" colspan="2" align="right">
            <A href="javascript:document.forms[0].submit();">
                <IMG name="btnFind" src="/images/btnFind.gif" border="0">
            </A>
            </TD> -->

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>