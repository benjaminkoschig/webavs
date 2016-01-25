<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0047";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.globall.util.*" %>
<%
CAApercuInteretMoratoireListViewBean viewBean = (CAApercuInteretMoratoireListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
CAApercuInteretMoratoireViewBean element = (CAApercuInteretMoratoireViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
viewBean.setForIdCompteAnnexe(globaz.jade.client.util.JadeStringUtil.isBlank(element.getIdCompteAnnexe())?viewBean.getForIdCompteAnnexe():element.getIdCompteAnnexe());

String idCompteAnnexe;
if(!JAUtil.isStringNull(request.getParameter("idCompteAnnexe"))) {
	idCompteAnnexe = request.getParameter("idCompteAnnexe");
}
else if(globaz.jade.client.util.JadeStringUtil.isBlank(element.getIdCompteAnnexe())) {
	idCompteAnnexe = viewBean.getForIdCompteAnnexe();
}
else {
	idCompteAnnexe = element.getIdCompteAnnexe();
}

viewBean.setForIdCompteAnnexe(idCompteAnnexe);
bButtonNew = false;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<script language="JavaScript">
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.interets.interetMoratoire.lister";
bFind = true;

function jsInitForIdCompteAnnexe(){
	document.forms[0].forIdCompteAnnexe.value = "";
}

top.document.title = "Comptes - Recherche des écritures du journal - " + top.location.href;

function checkSuspens() {
	if(document.forms['mainForm'].elements['forIdJournalFacturation'].checked) {
		document.forms['mainForm'].elements['forIdMotifCalcul'].selectedIndex = 0;
		document.forms['mainForm'].elements['forIdMotifCalcul'].disabled = true;
	}
	else {
		document.forms['mainForm'].elements['forIdMotifCalcul'].disabled = false;
	}
}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
        Gestion des intérêts moratoires
        <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD width="10%">N&deg; de journal</TD>
            <TD>
              <INPUT type="text" name="forIdJournalCalcul" style="width:7cm" value="">
              <input type="hidden" name="idJournalCalcul" value="">
			</TD>
            <TD>Date</TD>
            <TD width="40%">
              <ct:FWCalendarTag name="forDateCalcul" value="" />
            </TD>
          </TR>

          <TR>
            <TD colspan="4">
              <HR>
            </TD>
          </TR>

          <tr>
            <td>N&deg; compte</td>
            <td>
            	<input type="hidden" name="forIdCompteAnnexe" value="<%=viewBean.getForIdCompteAnnexe()%>">
            	<%
              	CACompteAnnexe tempCompteAnnexe = new CACompteAnnexe();
				tempCompteAnnexe.setIdCompteAnnexe(viewBean.getForIdCompteAnnexe());
				tempCompteAnnexe.setSession(objSession);
				tempCompteAnnexe.retrieve();
				%>
				<input type="text" name="forIdExterneRole" value="<%if (globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getForIdCompteAnnexe()) && request.getParameter("forIdExterneRole") != null){%><%=request.getParameter("forIdExterneRole")%><%}else{%><%=tempCompteAnnexe.getIdExterneRole()%><%}%>" onChange="jsInitForIdCompteAnnexe()">
				<select name="forIdRole" class="libelleStandard">
                <% String selectionRole = request.getParameter("forSelectionRole");
					if (selectionRole == null && tempCompteAnnexe.getIdRole() != null)
						selectionRole = tempCompteAnnexe.getIdRole();
					else if (selectionRole == null)
						selectionRole = "";%>
                <option selected value="">Tous</option>
                <%CARole tempRole;
					 		CARoleManager manRole = new CARoleManager();
							manRole.setSession(objSession);
							manRole.find();
							for(int i = 0; i < manRole.size(); i++){
								tempRole = (CARole)manRole.getEntity(i);
								if(selectionRole.equalsIgnoreCase(tempRole.getIdRole())){ %>
               	 <option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                <%}else{%>
               		<option value="<%=tempRole.getIdRole()%>"><%=tempRole.getDescription()%></option>
                	<%}%>
                <%}%>
              	</select>
				<%
				Object[] compteAnnexeMethodsName = new Object[]{
					new String[]{"setIdCompteAnnexe","getIdCompteAnnexe"}
					};

				String redirectUrl = "/osirisRoot/FR/interets/gestionInterets_rc.jsp";
				%>
				<ct:FWSelectorTag
					name="CompteAnnexeSelector"

					methods="<%=compteAnnexeMethodsName%>"
					providerApplication ="osiris"
					providerPrefix="CA"
					providerAction ="osiris.comptes.apercuComptes.chercher"
					redirectUrl="<%=redirectUrl%>" target="fr_main"
				/>
            </td>
            <td>Tri</td>
            <td>
              <select name="forSelectionTri" style="width:5cm;">
              	<option selected="selected" value="nom">nom</option>
                <option value="datenumero">date, numéro</option>
                <option value="datenom">date, nom</option>
                <option value="numero">numéro</option>
              </select>
            </td>
          </tr>
          <tr>
            <td>Genre</td>
            <td>
           		<ct:FWSystemCodeSelectTag
           			name="forIdGenreInteret"
           			defaut=""
           			codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsGenreInteret(objSession)%>"
           		/>
            </td>
            <td>Montant</td>
            <td>
              <input type="text" class="montant" name="fromTotalMontantInteret" style="width:5cm;">
              <INPUT type="hidden" name="selectorName" value="">
              <script>
           		document.getElementById("forIdGenreInteret").style.width=260;
           		</script>
            </td>
          </tr>
           <tr>
           	<td>Suspens uniquement</td>
			<td><input name="forIdJournalFacturation" type="checkbox" value="0" <% if(JAUtil.isStringNull(request.getParameter("idCompteAnnexe"))) { %>checked="checked"<% } %> onclick="checkSuspens();"></td>
			<td>Motif</td>
			<td>
			<%
			   	java.util.HashSet except = new java.util.HashSet();
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_MANUEL);
			   	except.add(globaz.osiris.db.interets.CAInteretMoratoire.CS_AUTOMATIQUE);
			%>

			<ct:FWSystemCodeSelectTag name="forIdMotifCalcul"
				defaut=""
				codeSystemManager="<%=globaz.osiris.translation.CACodeSystem.getLcsMotifDecisionInteret(objSession)%>"
				except="<%=except%>"
			/>
			<script language="JavaScript">
			if(document.forms['mainForm'].elements['forIdJournalFacturation'].checked) {
				document.forms['mainForm'].elements['forIdMotifCalcul'].selectedIndex = 0;
				document.forms['mainForm'].elements['forIdMotifCalcul'].disabled = true;
			}
			</script>
			</td>
			</tr>

			<%if(!globaz.jade.client.util.JadeStringUtil.isEmpty((String)request.getAttribute("message"))){%>
				<tr>
					<TD colspan="4">
						<font color="#FF0000"><b><%=request.getAttribute("message")%></b></font>
					</TD>
				</tr>
			<%}%>

			<tr>
			<td align="right" valign="top" colspan="4">
			<%
				String idAdministrateurSrc = request.getParameter("idAdministrateurSrc");

				if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(idAdministrateurSrc)) {
			%>
				<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=<%=idAdministrateurSrc%>" class="external_link">Administrateur</A>
			<%
				} else {
			%>
				&nbsp;
			<%
				}
			%>
            </td>
            </tr>

          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>