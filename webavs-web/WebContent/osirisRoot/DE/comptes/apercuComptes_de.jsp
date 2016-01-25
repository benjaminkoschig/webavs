<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0004"; %>
<%@ taglib uri="/WEB-INF/dyntable.tld" prefix="ta" %>
<%@ page import="globaz.osiris.application.*" %>
<%@ page import="globaz.osiris.translation.*" %>
<%@ page import="globaz.jade.client.util.*" %>
<%
	globaz.osiris.db.comptes.CACompteAnnexeViewBean viewBean = (globaz.osiris.db.comptes.CACompteAnnexeViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.comptes.apercuComptes.modifier";
	selectedIdValue = viewBean.getIdCompteAnnexe();
	String modeBulletinNeutreParDefaut = CAApplication.getApplicationOsiris().getCAParametres().getModeParDefautBulletinNeutre();
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
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
    document.forms[0].elements('userAction').value="osiris.comptes.apercuParSection.ajouter"
}
function upd() {
  document.forms[0].elements('userAction').value="osiris.comptes.apercuComptes.modifier";
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.apercuComptes.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.apercuComptes.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.apercuComptes.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Abrechnungskonto zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.apercuComptes.supprimer";
        document.forms[0].submit();
    }
}
function init(){

}

top.document.title = "Konti - Detail eines Abrechnungskontos - " + top.location.href;
// stop hiding -->
</SCRIPT>

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
%>

	<%
		String tmpDescription = JadeStringUtil.change(viewBean.getDescription(), "\"", "&quot;");
	%>
	
	<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=viewBean.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idContentieuxSrc" value="<%=idContentieuxSrc%>"/>
		<ct:menuSetAllParams key="libSequence" value="<%=libSequence%>"/>
		<ct:menuSetAllParams key="idAdministrateurSrc" value="<%=idAdministrateurSrc%>"/>
		<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
		<ct:menuSetAllParams key="forIdExterneRoleLike" value="<%=viewBean.getIdExterneRole()%>"/>
		<ct:menuSetAllParams key="forIdRole" value="<%=viewBean.getIdRole()%>"/>
		<ct:menuSetAllParams key="idExterneRole" value="<%=viewBean.getIdExterneRole()%>"/>
		<ct:menuSetAllParams key="description" value="<%=tmpDescription%>"/>
	</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<span class="postItIcon">
				<ct:FWNote sourceId="<%=viewBean.getIdCompteAnnexe()%>" tableSource="<%=globaz.osiris.db.comptes.CACompteAnnexeViewBean.TABLE_CACPTAP%>"/>
			</span>
			Detail eines Abrechnungskontos<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="125" valign="top">Konto</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
              <TEXTAREA cols="40" rows="6" class="libelleLongDisabled" readonly><%=viewBean.getTitulaireEntete()%></TEXTAREA>
            </TD>
            <TD colspan="2" align="right" valign="top">&nbsp;</TD>
          </TR>
          <TR>
            <TD width="125">Datum von - bis </TD>
            <TD width="30">&nbsp;</TD>
            <TD>
              <INPUT type="text" name="" size="30" maxlength="30" value="<%=viewBean.getRole().getDateDebutDateFin(viewBean.getIdExterneRole())%>" class="libelleLongDisabled" tabindex="-1" readonly>
            </TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <TR>
            <TD width="125">Gesperrt / verriegelt</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
              <input type="checkbox" name="estVerrouille" <%=(viewBean.getEstVerrouille().booleanValue())? "checked" : "unchecked"%> >
            </TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <TR>
            <TD width="125">Unter &Uuml;berwachung</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
              <input type="checkbox" name="aSurveiller" <%=(viewBean.isASurveiller().booleanValue())? "checked" : "unchecked"%> >
            </TD>
            <TD width="30">&nbsp;</TD>
            <TD width="10" align="left">Bemerkung</TD>
          </TR>
          <TR>
            <TD width="125">Offene Posten</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
              <INPUT type="text" name="" size="30" maxlength="30" value="<%=viewBean.getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly>
            </TD>
            <TD width="30">&nbsp;</TD>
            <TD width="10" rowspan="3"><TEXTAREA name="remarque" cols="50" rows="5"><%=viewBean.getRemarque()%></TEXTAREA></TD>
          </TR>

          <TR>
            <TD width="125">Art</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
              <INPUT type="text" name="" size="30" maxlength="30" value="<%if ("0".equals(viewBean.getIdGenreCompte())){ %><%=viewBean.getSession().getLabel("COMPTE_AUXILIAIRE_STANDARD")%><%} else {%><%=CACodeSystem.getLibelle(session, viewBean.getIdGenreCompte())%><%} %>" class="libelleLongDisabled" tabindex="-1" readonly>
              
            </TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <tr>
          <TD valign="top"  width="100">
				<%
             		String gedAffilieNumero = viewBean.getIdExterneRole();
             		String gedNumAvs = viewBean.getTiers().getNumAvsActuel();
             		String gedIdTiers = viewBean.getIdTiers();
             		String gedIdRole = viewBean.getIdRole();
             	%>
				
				<%@ include file="/theme/gedCall.jspf" %>
			</TD>
			</tr>
        <%-- D0009 --%> 
               <%if (!modeBulletinNeutreParDefaut.equalsIgnoreCase(viewBean.CS_BN_INACTIF)) {%>
	    <TR>
            <TD nowrap width="140">Neutraler Einzahlungsschein</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
              <%
						java.util.HashSet except = new java.util.HashSet();
						except.add(viewBean.CS_BN_INACTIF);
					%>
         	<ct:FWCodeSelectTag name="modeBulletinNeutre"
			defaut="<%=viewBean.getModeBulletinNeutre() %>"
			codeType="OSIMBNEU"
			except="<%=except%>"
			/>
			<%-- Affichage du mode par défaut défini dans la propriété osiris.modeBulletinNeutre si elle est active et que le mode du compte annexe et mode par défaut--%>
			 <% if (!modeBulletinNeutreParDefaut.equalsIgnoreCase(viewBean.CS_BN_INACTIF) && viewBean.getModeBulletinNeutre().equalsIgnoreCase(viewBean.CS_BN_DEFAUT)) { %>
			 <font size="3"><b><%= "(" + CACodeSystem.getLibelle(session, modeBulletinNeutreParDefaut) + ")"%></b></font>
		    <% } %>
		    
		    </TD>
		    <TD colspan="2">&nbsp;</TD>
          </TR>
         <%} %>  
          
          <% if (!JadeStringUtil.isDecimalEmpty(viewBean.getInformation())) { %>
			<TR>
            	<TD width="125">Information</TD>
            	<TD width="30">&nbsp;</TD>
            	<TD>
	            	<INPUT style="color:#FF0000" type="text" name="" value="<%=CACodeSystem.getLibelle(session, viewBean.getInformation())%>" class="inputDisabled" tabindex="-1" readonly>
            	</TD>
            	<TD colspan="2">&nbsp;</TD>
			</TR>
          <% } else { %>
          	<TR>
            	<TD width="125"></TD>
            	<TD width="30">&nbsp;</TD>
            	<TD>&nbsp;</TD>
            	<TD colspan="2">&nbsp;</TD>
			</TR>
          <% } %>

          <TR><TD colspan="5"><br/></TD></TR>
          <TR><TD colspan="5" class="title">Ausstände</TD></TR>

          <% if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()){ %>
          <% if (!JadeStringUtil.isDecimalEmpty(viewBean.getQualiteDebiteur())) { %>
			<TR>
            	<TD>Code Geldstrafe</TD>
            	<TD>&nbsp;</TD>
            	<TD>
	            	<INPUT type="text" name="" value="<%=CACodeSystem.getLibelle(session, viewBean.getQualiteDebiteur())%>" class="montantDisabled" tabindex="-1" readonly>
            	</TD>
            	<TD colspan="2">&nbsp;</TD>
			</TR>
          <% } else { %>
          	<TR>
            	<TD></TD>
            	<TD>&nbsp;</TD>
            	<TD>&nbsp;</TD>
            	<TD colspan="2">&nbsp;</TD>
			</TR>
          <% } %>
          <TR>
          	<td>Busse sperren</td>
            <td>&nbsp;</td>
            <td>
              <input type="checkbox" name="bloquerAmendeStatutaire" <%=(viewBean.getBloquerAmendeStatutaire().booleanValue())? "checked" : "unchecked"%> tabindex="2">
            </td>
            <TD colspan="2" align="right">
			<%
				if (!JadeStringUtil.isBlank(idContentieuxSrc)) {
			%>
				<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.poursuite.contentieux.afficher&refresh=true&libSequence=<%=libSequence%>&selectedId=<%=idContentieuxSrc%>" class="external_link">Rechtspflege</A>
			<%
				} else if (!globaz.jade.client.util.JadeStringUtil.isBlank(idAdministrateurSrc)) {
			%>
				<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=<%=idAdministrateurSrc%>" class="external_link">Verwalter</A>
			<%
				} else {
			%>
				&nbsp;
			<%
				}
			%>
            </TD>
          </TR>
          <TR>
          	<td>Mahnspesen sperren</td>
            <td>&nbsp;</td>
            <td>
              <input type="checkbox" name="bloquerTaxeSommation" <%=(viewBean.getBloquerTaxeSommation().booleanValue())? "checked" : "unchecked"%> tabindex="2">
            </td>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <TR>
          	<td>Betreibungsspesen sperren</td>
            <td>&nbsp;</td>
            <td>
              <input type="checkbox" name="bloquerFraisPoursuite" <%=(viewBean.getBloquerFraisPoursuite().booleanValue())? "checked" : "unchecked"%> tabindex="2">
            </td>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <% } %>

          <TR>
            <TD>Ausstände sperren</TD>
            <TD>&nbsp;</TD>
            <TD>
				<div id="divCheckbox" disabled>
	            	<input type="checkbox" name="contEstBloque" <%=(viewBean.getContEstBloque().booleanValue())? "checked" : "unchecked"%> tabindex="-1">
				</div>
            </TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>

          <% if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()){ %>
          <TR>
            <TD>Sperrungsanfangsdatum </TD>
            <TD>&nbsp;</TD>
            <TD>
            <ct:FWCalendarTag name="contDateDebBloque" doClientValidation="CALENDAR" value="<%=viewBean.getContDateDebBloque()%>"/>
            </TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <TR>
            <TD>Sperrungsenddatum</TD>
            <TD>&nbsp;</TD>
            <TD>
            	<ct:FWCalendarTag name="contDateFinBloque" doClientValidation="CALENDAR" value="<%=viewBean.getContDateFinBloque()%>"/>
            </TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <TR>
          	<td>Grund</td>
            <td>&nbsp;</td>
            <td colspan="2">
              <div align="left">
                <%	viewBean.getCsMotifContentieuxSuspendus();
							globaz.globall.parameters.FWParametersSystemCode _motifContentieuxSus = null; %>
                <select name="idContMotifBloque"style="width : 100%">
                  <%	for (int i=0; i < viewBean.getCsMotifContentieuxSuspendus().size(); i++) {
								_motifContentieuxSus = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsMotifContentieuxSuspendus().getEntity(i);
								if (_motifContentieuxSus.getIdCode().equalsIgnoreCase(viewBean.getIdContMotifBloque())) { %>
                  <option selected value="<%=_motifContentieuxSus.getIdCode()%>"><%=_motifContentieuxSus.getCurrentCodeUtilisateur().getLibelle()%></option>
                  <%	} else { %>
                  <option value="<%=_motifContentieuxSus.getIdCode()%>"><%=_motifContentieuxSus.getCurrentCodeUtilisateur().getLibelle()%></option>
                  <%	}
							} %>
                </select>
              </div>
            </td>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <% } else { %>
          <TR>
           	<td colspan="5">&nbsp;</td>
          </TR>
          <TR>
          <% String ro = objSession.hasRight(userActionValue, globaz.framework.secure.FWSecureConstants.ADD)?"false":"true"; %>
			<TD colspan="5"><ta:table title="table" id="validTable" height="100"
				readOnly="<%=ro%>">
				<ta:column width="1px">
					<ta:input style="display : none" name="idMotifContentieux"
						type="hidden" />
				</ta:column>
				<ta:column title="Anfangsdatum">
					<ta:input name="dateDebutMotif" type="text"></ta:input>
				</ta:column>
				<ta:column title="Enddatum">
					<ta:input name="dateFinMotif" type="text"></ta:input>
				</ta:column>
				<ta:column title="Grund" width="400px">
					<ta:select name="idMotifBlocage" wantBlank="true">
						<ta:optioncollection csFamille="OSIMOTCTX" />
					</ta:select>
				</ta:column>
				<ta:column title="Kommentar" width="300px">
					<ta:input name="commentaire" type="text"></ta:input>
				</ta:column>
			</ta:table></TD>
		</TR>
		<% } %>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>