
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%idEcran = "GCA4003"; %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CARubriqueViewBean viewBean = (CARubriqueViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
  document.forms[0].elements('userAction').value="osiris.comptes.rubrique.ajouter";

  document.forms[0].elements['description'].value = '';
  document.forms[0].elements['idRubrique'].value = '';
  document.forms[0].elements['idTraduction'].value = '';
  document.forms[0].elements['fromNumero'].value = '';
  document.forms[0].elements['idExterne'].value = '';
  document.forms[0].elements['saisieEcran'].selectedIndex = 'on';
  document.forms[0].elements['descriptionFr'].value = '';
  document.forms[0].elements['descriptionDe'].value = '';
  document.forms[0].elements['descriptionIt'].value = '';
  document.forms[0].elements['alias'].value = '';
  document.forms[0].elements['natureRubrique'].value = '';
  document.forms[0].elements['idSecteur'].selectedIndex = 0;
  document.forms[0].elements['estVentilee'].checked = false;
  document.forms[0].elements['tenirCompteur'].checked = false;
  document.forms[0].elements['AnneeCotisation'].value = '';
  document.forms[0].elements['idContrepartie'].value = '';
  document.forms[0].elements['idExterneCompteCourantEcran'].value = '';
  document.forms[0].elements['textfield'].value = '';
  document.forms[0].elements['numCompteCG'].value = '';
}

function upd() {
  document.forms[0].elements('userAction').value="osiris.comptes.rubrique.modifier"
	document.forms[0].idExterne.disabled = true;
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Rubrik zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.rubrique.supprimer";
        document.forms[0].submit();
    }

}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.rubrique.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.rubrique.modifier";
    
    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.rubrique.afficher";
}
function init(){}

top.document.title = "Konti- Detail einer Rubrik " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Rubrik <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <TR> 
            <TD nowrap width="174"> 
              <input type="hidden" name="description" value="<%=viewBean.getDescription()%>"/>
              <input type="hidden" name="idRubrique" value="<%=viewBean.getIdRubrique()%>"/>
              <input type="hidden" name="idTraduction" value="<%=viewBean.getIdTraduction()%>"/>
              <input type="hidden" name="fromNumero" value="<%=viewBean.getIdExterne()%>"/>
              <p>Nummer</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393"> 
              <INPUT type="text" name="idExterne" size="20" maxlength="15" value="<%=viewBean.getIdExterne()%>" tabindex="1" >
              <input type="hidden" name="saisieEcran" value="on">
            </TD>
            <TD width="168">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174" height="43">Beschreibung</TD>
            <TD width="10" height="43">&nbsp;</TD>
            <TD nowrap width="393" height="43"> 
              <input type="text" name="descriptionFr" size="40" maxlength="100" value="<%=viewBean.getDescription("FR")%>"  tabindex="2" >
              Französisch 
              <input type="text" name="descriptionDe" size="40" maxlength="100" value="<%=viewBean.getDescription("DE")%>"  tabindex="3" >
              Deutsch 
              <input type="text" name="descriptionIt" size="40" maxlength="100" value="<%=viewBean.getDescription("IT")%>"  tabindex="4" >
              Italienisch </TD>
            <TD width="168" height="43">&nbsp;</TD>
          </TR>
          <tr> 
            <td nowrap width="174"> 
              <p>Alias</p>
            </td>
            <td width="10">&nbsp;</td>
            <td nowrap width="393"> 
              <input type="text" size="40" maxlength="15"  value="<%=viewBean.getAlias()%>"  tabindex="5" name="alias">
            </td>
            <td width="168">&nbsp;</td>
          </tr>
          <TR> 
            <TD nowrap width="174" height="38"> 
              <p>Kontoart</p>
            </TD>
            <TD width="10" height="38">&nbsp;</TD>
            <TD width="393"> 
              <%	viewBean.getCsNatureRubrique();
							globaz.globall.parameters.FWParametersSystemCode _natureRubrique = null; %>
              <select name="natureRubrique"style="width : 8.5cm;" tabindex="6">
                <%	for (int i=0; i < viewBean.getCsNatureRubriques().size(); i++) { 
								_natureRubrique = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsNatureRubriques().getEntity(i);
								if (_natureRubrique.getIdCode().equalsIgnoreCase(viewBean.getNatureRubrique())) { %>
                <option selected value="<%=_natureRubrique.getIdCode()%>"><%=_natureRubrique.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	} else { %>
                <option value="<%=_natureRubrique.getIdCode()%>"><%=_natureRubrique.getCurrentCodeUtilisateur().getLibelle()%></option>
                <%	}
							} %>
              </select>
            </TD>
            <TD width="168" height="38">&nbsp;</TD>
          </TR>
          <tr> 
            <td nowrap width="174">Bereich</td>
            <td width="10">&nbsp;</td>
            <td width="393"> 
              <select name="idSecteur"style="width : 8.5cm;" tabindex="7">
                <%CASecteur tempSecteur;
					 		CASecteurManager manSecteur = new CASecteurManager();
							manSecteur.setSession(objSession);
							manSecteur.find();
							for(int i = 0; i < manSecteur.size(); i++){
								tempSecteur = (CASecteur)manSecteur.getEntity(i);
                			if  (viewBean.getIdSecteur().equalsIgnoreCase(tempSecteur.getIdSecteur())) { %>
                <option selected value="<%=tempSecteur.getIdSecteur()%>"><%=tempSecteur.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempSecteur.getIdSecteur()%>"><%=tempSecteur.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
            </td>
            <td width="168">&nbsp;</td>
          </tr>
          <TR> 
            <TD nowrap width="174"> 
              <p>Verteilung</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393"> 
              <input type="checkbox" value="on" name="estVentilee" tabindex="8" <%=(viewBean.getEstVentilee().booleanValue())? "checked" : ""%> >
            </TD>
            <TD width="168">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174"> 
              <p>Zähler</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <td nowrap width="393"> 
              <input type="checkbox" name="tenirCompteur" tabindex="9" <%=(viewBean.getTenirCompteur().booleanValue())? "checked" : ""%> value="on" >
            </td>
            <TD width="168">&nbsp;</TD>
          </TR>
          
          <TR>
          	<TD>Verbandskasse verwalten</TD>
          	<TD width="10">&nbsp;</TD>
          	<TD>
          	<input type="checkbox" name="useCaissesProf" tabindex="10" <%=(viewBean.isUseCaissesProf())? "checked" : ""%> value="on" >
          	</TD>
          </TR>
          
          <TR> 
            <TD nowrap width="174"> 
              <p>Beitragsjahr</p>
            </TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap width="393"> 
              <input type="text" size="15" maxlength="4"  value="<%=viewBean.getAnneeCotisation()%>"  tabindex="12" name="AnneeCotisation">
            </TD>
            <TD width="168">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="174">Kontokorrent</TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap colspan="2"> 
              <input type="hidden" name="idContrepartie" value="<%=viewBean.getIdContrepartie()%>" tabindex="-1">
              <input type="text" name="idExterneCompteCourantEcran" size="15" maxlength="15" tabindex="15" value="<%=viewBean.getIdExterneCompteCourantEcran()%>">
              <input type="text" readonly disabled name="textfield" size="40" maxlength="40" value="<% if(viewBean.getCompteCourant() != null) {%><%=viewBean.getCompteCourant().getRubrique().getDescription()%><%}%>">
            </TD>
          </TR>
          <TR> 
            <TD nowrap width="174">Kontonummer Hauptbuchhaltung</TD>
            <TD width="10">&nbsp;</TD>
            <TD nowrap colspan="2"> 
              <input type="text" size="15" maxlength="15"  value="<%=viewBean.getNumCompteCG()%>"  tabindex="16" name="numCompteCG">
            </TD>
          </TR>
          <%if (!"".equals(viewBean.getReferencesRubriqueDescription(3,2)) && !"add".equalsIgnoreCase(request.getParameter("_method"))){%>
          <TR> 
            <TD nowrap width="174">Referenzen der Rubrik</TD>
            <TD width="10">&nbsp;</TD>
            <TD>
            	<!-- Les attributs de format et retourLigne de la méthode getReferencesRubriqueDescription(format,retourLigne) sont les suivants 
            	format : 1 = description du code système, 2 = numéro et description du code système, 3 = code et description du code système, 4 = numéro, code et description du code système 
	 			retourLigne : 1 toutes les informations à la suite séparées par des espaces, 2 = retour à la ligne entre chaque code 
            	-->
            	<TEXTAREA cols="70"  tabindex="-1" readonly rows="4"><%=viewBean.getReferencesRubriqueDescription(2,1) %></TEXTAREA>
            </TD>
          </TR>
          <%}%>
		<TR> 
        	<TD nowrap width="174" height="38">Bezeichnung für den Kontoauszug</TD>
            <TD width="10" height="38">&nbsp;</TD>
            <TD width="393"> 
			<%	
            	viewBean.getCsLibelleExtraitCompte();
				globaz.globall.parameters.FWParametersSystemCode libelleExtrait = null; 
			%>
            <select name="libelleExtraitCompte" style="width : 8.5cm;" tabindex="6">
            <%	
            	for (int i=0; i < viewBean.getCsLibelleExtraitCompteManager().size(); i++) { 
					libelleExtrait = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsLibelleExtraitCompteManager().getEntity(i);
					if (libelleExtrait.getIdCode().equalsIgnoreCase(viewBean.getLibelleExtraitCompte())) { 
			%>
            	    <option selected value="<%=libelleExtrait.getIdCode()%>"><%=libelleExtrait.getCurrentCodeUtilisateur().getLibelle()%></option>
            <%		
            		} else { 
            %>
            		<option value="<%=libelleExtrait.getIdCode()%>"><%=libelleExtrait.getCurrentCodeUtilisateur().getLibelle()%></option>
            <%	
            		}
				} 
			%>
            </select>
           	</TD>
			<TD width="168" height="38">&nbsp;</TD>
		</TR>            
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>

<ct:menuChange displayId="options" menuId="CA-Reference-Rubrique" showTab="options">
	<ct:menuSetAllParams key="idRubrique" value="<%=viewBean.getIdRubrique()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>