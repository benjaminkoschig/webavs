
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0006";
rememberSearchCriterias = true;
%>
<% bButtonNew = false; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CACompteAnnexeViewBean element = (CACompteAnnexeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
bButtonNew = false;
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
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuCompteur.lister";
bFind = true;
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
top.document.title = "Comptes - Aperçu des compteurs - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Aper&ccedil;u des compteurs<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR>
            <TD width="10%" valign="top" rowspan="3">Compte
            <input type="hidden" name="id" value="<%=element.getIdCompteAnnexe()%>"/>
			<input type="hidden" name="idCompteAnnexe" value="<%=element.getIdCompteAnnexe()%>"/>
            </TD>
            <TD valign="top" rowspan="3">
              <TEXTAREA cols="40" rows="3" class="libelleLongDisabled" readonly><%=element.getTitulaireEntete()%></TEXTAREA>
            </TD>
            <TD width="80%" align="right" valign="top">S&eacute;lection des compteurs
              <select name="forSelectionCompteur" class="rubrique">
               	<option value="<%=CACompteurManager.SELECTION_STANDARD%>" selected>standard</option>
                <option value="<%=CACompteurManager.SELECTION_TOUS%>">tous</option>
                <option value="<%=CACompteurManager.SELECTION_IRRECOUVRABLE%>">irr&eacute;couvrable</option>
                <option value="<%=CACompteurManager.SELECTION_AMORTISSEMENT%>">amortissement</option>
                <option value="<%=CACompteurManager.SELECTION_RECOUVREMENT%>">recouvrement</option>
                <option value="<%=CACompteurManager.SELECTION_COT_AVEC_MASSE%>">cotisations avec masse</option>
                <option value="<%=CACompteurManager.SELECTION_COT_SANS_MASSE%>">cotisations sans masse</option>
                <%			CACompteur tempCompteur;
					 		CACompteurManager manCompteur = new CACompteurManager();
							manCompteur.setSession(objSession);
							manCompteur.setForIdCompteAnnexe(element.getIdCompteAnnexe());
							manCompteur.setForSelectionTri(CACompteurManager.ORDER_COMPTEUR);
							manCompteur.setForSelectDistinct(true);
							manCompteur.find();
							for(int i = 0; i < manCompteur.size(); i++){
								tempCompteur = (CACompteur)manCompteur.getEntity(i);%>
                <option value="<%=tempCompteur.getRubrique().getIdExterne()%>"><%=tempCompteur.getRubrique().getIdExterne()%></option>
                <% } %>
              </select>
            </TD>
          </TR>

          <TR>
            <TD align="right">Tri&nbsp;
              <select name="forSelectionTri" class="rubrique">
                <option selected value="1">par compteur</option>
                <option value="2">par année</option>
              </select>
            </TD>
          </TR>

          <TR>
           <TD align="right">Ann&eacute;e&nbsp;
              <input type="text" name="forAnnee" maxlength="4" size="4"/>
            </TD>
          </TR>

          <TR>
            	<td colspan="2">&nbsp;</td>

            	<td align="right" valign="top">
	             <%
					String idAdministrateurSrc = request.getParameter("idAdministrateurSrc");
					String idContentieuxSrc = request.getParameter("idContentieuxSrc");
					String libSequence = request.getParameter("libSequence");

					if (globaz.jade.client.util.JadeStringUtil.isNull(idContentieuxSrc)) {
						idContentieuxSrc = "";
					}

					if (globaz.jade.client.util.JadeStringUtil.isNull(libSequence)) {
						libSequence = "";
					}

					if (globaz.jade.client.util.JadeStringUtil.isNull(idAdministrateurSrc)) {
						idAdministrateurSrc = "";
					}

					if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdministrateurSrc)) {
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
					<ct:menuSetAllParams key="idTiers" value="<%=element.getIdTiers()%>"/>
					<ct:menuSetAllParams key="forIdExterneRoleLike" value="<%=element.getIdExterneRole()%>"/>
					<ct:menuSetAllParams key="forIdRole" value="<%=element.getIdRole()%>"/>
				</ct:menuChange>
	             </td>
            </TR>
            <tr>
          <TD valign="top"  width="100">
				<%
             		String gedAffilieNumero = element.getIdExterneRole();
             		String gedNumAvs = element.getTiers().getNumAvsActuel();
             		String gedIdTiers = element.getIdTiers();
             		String gedIdRole = element.getIdRole();
             	%>
				
				<%@ include file="/theme/gedCall.jspf" %>
			</TD>
			</tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>