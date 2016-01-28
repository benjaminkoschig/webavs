<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0001";
rememberSearchCriterias = true;
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
CACompteAnnexeViewBean _compteAnnexe = (CACompteAnnexeViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.comptes.apercuCompteResume.lister";
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

top.document.title = "Kontoauszug " + top.location.href;
// stop hiding -->

function initPos(){
	if (document.forms[0].forIdSection.value == "") {
		<%
          	String year = "01.01.";
          	year += "" + (globaz.globall.util.JACalendarGregorian.today().getYear() - 1);
          	%>
		document.forms[0].fromPositionnement.value = "<%=year%>";
	} else {
		document.forms[0].fromPositionnement.value = "";
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Kontoauszug<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

          <input type="hidden" name="id" value="<%=_compteAnnexe.getIdCompteAnnexe()%>"/>
          <TR>
            <TD width="128" valign="top">Konto<input type="hidden" name="id" value="<%=request.getParameter("id")%>"></TD>
            <TD rowspan="3" valign="top">
              <TEXTAREA cols="40" rows="4" class="libelleLongDisabled" readonly><%=_compteAnnexe.getTitulaireEntete()%></TEXTAREA>
            </TD>
            <TD width="13">&nbsp;</TD>
            <TD width="89">Periode</TD>
            <TD><input type="text" name="DateDebutDateFin" size="25" maxlength="25" value="<%=_compteAnnexe.getRole().getDateDebutDateFin(_compteAnnexe.getIdExterneRole())%>" class="libelleLongDisabled" tabindex="-1" readonly></TD>
            <TD align="right">
            Rechnungsart&nbsp;
              <select name="forSelectionSections" class="libelleCourt">
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.SOLDE_ALL%>" selected>alle</option>
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.SOLDE_OPEN%>">offene</option>
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.SOLDE_CLOSED%>">saldierte</option>
              </select>
            </TD>
          </TR>

          <TR>
          	<TD colspan="5">&nbsp;</TD>
			<TD align="right" valign="bottom">Auswahl&nbsp;
              <select name="forSelectionTri"  class="libelleCourt">
                <option selected value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_DATE_COMPTABLE%>">nach Buchungsdatum</option>
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_DATE_VALEUR%>">Valutadatum</option>
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.ORDER_BY_IDSECTION%>">nach Sektion</option>
              </select>
            </TD>
          </TR>

         <TR>
          	<TD colspan="3">&nbsp;</TD>
          	<TD width="89">Offener Saldo</TD>
          	<TD><input type="text" name="solde" size="25" maxlength="25" value="<%=_compteAnnexe.getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly></TD>
        	<TD align="right" valign="bottom">Rechnungs-Nr.&nbsp;
              <select name="forIdSection" class="libelleCourt" onclick="initPos()">
            <%
            	String forIdSection = "";
            	if (request.getParameter("forIdSection") != null) {
            		forIdSection = request.getParameter("forIdSection");
            	}
            %>

              	<option value="" <%if (forIdSection.equals("")) {%>selected<%}%>>Alle</option>
            <%
            	CASectionManager sectionManager = new CASectionManager();
            	sectionManager.setSession(objSession);

            	sectionManager.setForIdCompteAnnexe(_compteAnnexe.getIdCompteAnnexe());

            	sectionManager.find();

            	for (int i=0; i<sectionManager.size(); i++) {
            		CASection section = (CASection) sectionManager.get(i);
            %>
				<option value="<%=section.getIdSection()%>" <%if (forIdSection.equals(section.getIdSection())) {%>selected<%}%>><%=section.getIdExterne()%></option>
			<%
				}
			%>
              </select>
            </TD>
          </TR>

          <TR>
            <TD width="128">Ab</TD>
            <TD>
              <input type="text" name="fromPositionnement" class="libelleStandard" value=" ">
            </TD>
            <TD colspan="3">&nbsp;</TD>
            <TD align="right">Vorgang
              <select name="forIdTypeOperation" >
                <option value="<%=globaz.osiris.db.comptes.extrait.CAExtraitCompteManager.FOR_ALL_IDTYPEOPERATION%>">Alle</option>
                <%CATypeOperation tempTypeOperation;
					CATypeOperationManager manTypeOperation = new CATypeOperationManager();
					manTypeOperation.setSession(objSession);
					manTypeOperation.find();
						for(int i = 0; i < manTypeOperation.size(); i++){
							tempTypeOperation = (CATypeOperation)manTypeOperation.getEntity(i);
							if (!tempTypeOperation.getIdTypeOperation().equalsIgnoreCase("PEND"))%>
                <option value="<%=tempTypeOperation.getIdTypeOperation()%>"> <%=tempTypeOperation.getDescription()%>
                </option>
                <%}%>
              </select>
            </TD>
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

			<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier">
				<ct:menuSetAllParams key="idContentieuxSrc" value="<%=idContentieuxSrc%>"/>
				<ct:menuSetAllParams key="libSequence" value="<%=libSequence%>"/>
				<ct:menuSetAllParams key="idAdministrateurSrc" value="<%=idAdministrateurSrc%>"/>
				<ct:menuSetAllParams key="idTiers" value="<%=_compteAnnexe.getIdTiers()%>"/>
				<ct:menuSetAllParams key="forIdExterneRoleLike"  value="<%=_compteAnnexe.getIdExterneRole()%>"/>
				<ct:menuSetAllParams key="forIdRole"  value="<%=_compteAnnexe.getIdRole()%>"/>
			</ct:menuChange>

			</TD>
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