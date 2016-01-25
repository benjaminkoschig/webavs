<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA0010"; %>
<%@ taglib uri="/WEB-INF/dyntable.tld" prefix="ta" %>
<%@ page import="globaz.osiris.application.*" %>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%
	CASectionViewBean viewBean = (CASectionViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
	userActionValue = "osiris.comptes.apercuParSection.modifier";
	selectedIdValue = viewBean.getIdSection();
	globaz.osiris.api.APICompteAnnexe compteAnnexe = viewBean.getCompteAnnexe();
	//TODO: faire quelque chose si compteAnnexe est null: warning, message pour user, etc.
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.osiris.api.APISection"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.osiris.translation.CACodeSystem"%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
function add() {
    document.forms[0].elements('userAction').value="osiris.comptes.apercuParSection.ajouter";
}
function upd() {
   document.forms[0].elements('userAction').value="osiris.comptes.apercuParSection.modifier";
	document.forms[0].elements('texteRemarque').focus();
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="osiris.comptes.apercuParSection.ajouter";
    else
        document.forms[0].elements('userAction').value="osiris.comptes.apercuParSection.modifier";

    return state;

}
function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="osiris.comptes.apercuParSection.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, die ausgeählte Sektion zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="osiris.comptes.apercuParSection.supprimer";
        document.forms[0].submit();
    }
}
function init(){}


top.document.title = "Konti - Detail einer Sektion - " + top.location.href;

//<!-- AUTO COMPLETE SECTION -->

<%
	String jspCaissesProfSelectLocation = servletContext + mainServletPath + "Root/caissesprof_select.jsp";
%>

function updateIdCaissesProf(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('idCaisseProfessionnelle').value = tag.select[tag.select.selectedIndex].selectedIdCaissesProf;
		document.getElementById('caisseProfDescription').value = tag.select[tag.select.selectedIndex].selectedCaissesProfLibelle;
	} else {
		document.getElementById('idCaisseProfessionnelle').value = "";
		document.getElementById('caisseProfDescription').value = "";
	}
}

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail einer Sektion<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<input type="hidden" name="idSection" value="<%=viewBean.getIdSection()%>"/>
		<input type="hidden" name="idTypeSection" value="<%=viewBean.getIdTypeSection()%>"/>
		<input type="hidden" name="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"/>
		<input type="hidden" name="base" value="<%=viewBean.getBase()%>"/>
		<input type="hidden" name="categorieSection" value="<%=viewBean.getCategorieSection()%>"/>
		<input type="hidden" name="dateDebutPeriode" value="<%=viewBean.getDateDebutPeriode()%>"/>
		<input type="hidden" name="dateFinPeriode" value="<%=viewBean.getDateFinPeriode()%>"/>
		<input type="hidden" name="frais" value="<%=viewBean.getFrais()%>"/>
		<input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>"/>
		<input type="hidden" name="idLastEtapeCtx" value="<%=viewBean.getIdLastEtapeCtx()%>"/>
		<input type="hidden" name="idPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
		<input type="hidden" name="idPosteJournalisation" value="<%=viewBean.getIdPosteJournalisation()%>"/>
		<input type="hidden" name="idRemarque" value="<%=viewBean.getIdRemarque()%>"/>
		<input type="hidden" name="interets" value="<%=viewBean.getInterets()%>"/>
		<input type="hidden" name="pmtCmp" value="<%=viewBean.getPmtCmp()%>"/>
		<input type="hidden" name="taxes" value="<%=viewBean.getTaxes()%>"/>
          <TR>
            <TD nowrap width="91">
              <P>Sektion</P>
            </TD>
            <TD width="1"></TD>
            <TD width="270">
              <input type="text" name="idExterne" value="<%=viewBean.getIdExterne()%>" class="inputDisabled" tabindex="-1" readonly size="15" maxlength="15">
            </TD>
            <TD width="164"></TD>
            <TD width="74">
          	<% if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdCompteAnnexe())) { %>
				<A href="osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getIdCompteAnnexe()%>">Konto</A>
			<% } else { %>
				Konto
			<% } %>
			</TD>
            <TD width="21" height="32"></TD>
            <TD rowspan="2" colspan="2">
              <textarea cols="65" rows="3" class="inputDisabled" readonly><%=viewBean.getCompteAnnexe().getTitulaireEntete()%></textarea>
            </TD>
          </TR>
          <TR>
            <TD nowrap width="91">&nbsp;</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="270">
              <input type="text" name="description" value="<%=viewBean.getDescription()%>" class="inputDisabled" tabindex="-1" readonly size="31" maxlength="30">
            </TD>
            <TD width="164">&nbsp;</TD>
            <TD width="74">&nbsp;</TD>
            <TD width="21" height="32"></TD>
          </TR>
          <TR>
            <TD nowrap width="91">Sektionsart</TD>
            <TD width="1"></TD>
            <TD width="270">
              <input type="text" name="typeSection" value="<%=viewBean.getTypeSection().getDescription()%>" class="inputDisabled" tabindex="-1" readonly size="31" maxlength="30">
            </TD>
            <TD width="164"></TD>
            <TD width="74">Ausgleichsmodus&nbsp;</TD>
            <TD width="21"></TD>
            <TD colspan="2"> <%
            if (viewBean.getIdModeCompensation().equals(APISection.MODE_REPORT) && !JadeStringUtil.isBlankOrZero(viewBean.getIdPassageComp())) { %>
            	<input type="text" name="" value="<%=CACodeSystem.getLibelle(session, viewBean.getIdModeCompensation()) %>" class="libelleLongDisabled" tabindex="-1" readonly >
            <%
            } else {
              		String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdModeCompensationSelectBlock(objSession, viewBean.getIdModeCompensation());

              		if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
              			out.print(selectBlock);
              		}
            }
              %></TD>
          </TR>
          <TR>
            <TD nowrap width="91">Datum</TD>
            <TD width="1"></TD>
            <TD width="270">
              <INPUT type="text" name="dateSection" value="<%=viewBean.getDateSection()%>" class="dateDisabled" tabindex="-1" readonly size="11" maxlength="10">
            </TD>
            <TD width="164"></TD>
            <TD nowrap width="74">Bemerkungen</TD>
            <TD width="21" height="32"></TD>
            <TD rowspan="2" colspan="2">
              <textarea name="texteRemarque" cols="65" rows="3" class="input"><%=viewBean.getRemarqueEcran()%></textarea>
            </TD>
          </TR>
          <TR>
            <TD nowrap width="91">Saldo</TD>
            <TD width="1">&nbsp;</TD>
            <TD width="270">
              <INPUT type="text" name="soldeFormate" value="<%=viewBean.getSoldeFormate()%>" class="montantDisabled" tabindex="-1" readonly>
              <input type="hidden" name="solde" value="<%=viewBean.getSolde()%>"/>
            </TD>
            <TD width="164">&nbsp;</TD>
            <TD width="74">&nbsp;</TD>
            <TD width="21" height="32"></TD>
          </TR>
<TR>
            <TD nowrap>Separat ausdrucken</TD>
            <TD width="1">&nbsp;</TD>
            <TD>
              <input type="checkbox" name="nonImprimable" id="nonImprimable" <%=(viewBean.getNonImprimable().booleanValue())? "checked" : "unchecked"%> >
              <label for="nonImprimable"></label>
            </TD>
            <TD>&nbsp;</TD>
            <TD>Domäne</TD>
            <TD width="164">&nbsp;</TD>
			<TD nowrap height="31">
				<ct:FWCodeSelectTag
					name="domaine"
					defaut="<%=viewBean.getDomaine()%>"
					codeType="PYAPPLICAT"
					wantBlank="true"/>
			</TD>

            <TD height="32"></TD>
          </TR>
          <TR>
            <TD width="129">Verbandskasse</TD>
            <TD width="1">&nbsp;</TD>
            <TD nowrap width="500" colspan="5">
            <input type="hidden" id="idCaisseProfessionnelle" name="idCaisseProfessionnelle" value="<%=viewBean.getIdCaisseProfessionnelle()%>" >

            <%
            	String tmp = "tempIdExterneRole=" + viewBean.getCompteAnnexe().getIdExterneRole() + "&tempIdRole=" + viewBean.getCompteAnnexe().getIdRole();
            %>

            <ct:FWPopupList
	           name="idCaisseProfessionnelleEcran"
	           value="<%=viewBean.getCaisseProfessionnelleNumero()%>"
	           className="libelle"
	           jspName="<%=jspCaissesProfSelectLocation%>"
	           onChange="updateIdCaissesProf(tag);"
	           minNbrDigit="1"
	           params="<%=tmp%>"
	          />
	          <input type="text" name="caisseProfDescription" size="30" value="<%=viewBean.getCaisseProfessionnelleLibelle()%>" class="libelleLongDisabled"  readonly tabindex="-1">
            </TD>
          </TR>

          <TR>
            <TD nowrap>Referenz alten ESR</TD>
            <TD width="1">&nbsp;</TD>
            <TD>
              <input type="text" name="referenceBvr" value="<%=viewBean.getReferenceBvr()%>" class="inputDisabled" tabindex="-1" readonly size="31" maxlength="30">
            </TD>
            <TD>&nbsp;</TD>
            <% if (viewBean.getAttenteLSVDD().booleanValue()) { %>
            <TD>Anhängig LSV/DD</TD>
            <TD height="32"><input type="checkbox" name="attenteLSVDD" id="attenteLSVDD" <%=(viewBean.getAttenteLSVDD().booleanValue())? "checked" : "unchecked"%> >
              <label for="attenteLSVDD"></label>
            </TD>
            <% } else { %>
            <TD>&nbsp;</TD>
            <TD>&nbsp;</TD>
            <% } %>
          </TR>

          <%if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdSectionPrincipal())) {%>
          <TR>
          	<TD nowrap width="91" valign="top">Verbindende Hauptsektion</TD>
          	<TD width="1">&nbsp;</TD>
            <TD width="270">
            <textarea cols="65" rows="3" class="inputDisabled" readonly><%=viewBean.getSectionPrincipalInfos()%></textarea>
            </TD>
            <TD colspan="3">&nbsp;</TD>
          </TR>
          <%}%>

          <%
          	String sectionsAuxInfos = viewBean.getSectionsAuxInfos();
          	if (!globaz.jade.client.util.JadeStringUtil.isBlank(sectionsAuxInfos)) {
          %>
          <TR>
          	<TD nowrap width="91" valign="top">Verbindende Hilfssektion</TD>
          	<TD width="1">&nbsp;</TD>
            <TD width="270">
            <textarea cols="65" rows="3" class="inputDisabled" readonly><%=sectionsAuxInfos%></textarea>
            </TD>
            <TD colspan="3">&nbsp;</TD>
          </TR>
          <%}%>

          <TR>
            <TD width="91">&nbsp;</TD>
            <TD width="1"></TD>
            <TD nowrap width="270"> </TD>
            <TD width="164"></TD>
            <TD width="74"></TD>
            <TD width="21"></TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>

          <TR>
            <TD colspan="8" height="10" class="title" nowrap>Rechtspflege </TD>
          </TR>
          <TR>
            <TD nowrap width="91">&nbsp;</TD>
            <TD width="1"></TD>
            <TD width="270"> </TD>
            <TD width="164"></TD>
            <TD width="74"></TD>
            <TD width="21"></TD>
            <TD colspan="2" align="right">
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
				<A href="<%=request.getContextPath()%>/aquila?userAction=aquila.administrateurs.administrateur.afficher&selectedId=<%=idAdministrateurSrc%>" class="external_link">Administrateur</A>
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
            <TD nowrap width="91">Fälligkeit der Beiträge</TD>
            <TD width="1"></TD>
            <TD width="270">
              <INPUT type="text" name="dateEcheance" value="<%=viewBean.getDateEcheance()%>" class="dateDisabled" tabindex="-1" readonly size="11" maxlength="10">
            </TD>
            <TD width="164"></TD>
            <% if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()){ %>
            <TD width="74">
              <p>Rechtspflege aufheben</p>
            </TD>
            <TD width="21"></TD>
            <TD>
            	<span disabled>
            		<INPUT type="checkbox" name="contentieuxEstSuspendu" <%=(viewBean.getContentieuxEstSuspendu().booleanValue())? "checked" : "unchecked"%> >
            	</span>
            	Bis am
			</TD>
            <TD>
              <ct:FWCalendarTag name="dateSuspendu" doClientValidation="CALENDAR" value="<%=viewBean.getDateSuspendu()%>"/>
            </TD>
            <% } %>
          </TR>
          <TR>
            <TD nowrap width="91"></TD>
            <TD width="1"></TD>
            <TD nowrap width="270"></TD>
            <TD width="164"></TD>
            <TD width="74" nowrap></TD>
            <TD width="21"></TD>
            <TD nowrap colspan="2"></TD>
          </TR>
          <tr>
            <td nowrap width="91">Letzte Nachfrist</td>
            <td width="1"></td>
            <td width="270">
              <INPUT type="text" name="derniereEtape" value="<%=viewBean.getResumeContentieux()%>" class="inputDisabled" tabindex="-1" readonly size="31" maxlength="30">
            </td>
            <td width="164"></td>
            <% if (!CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()){ %>
            <td width="74">Grund</td>
            <td width="21"></td>
            <td colspan="2">
              <div align="left">
                <%	viewBean.getCsMotifContentieuxSuspendus();
							globaz.globall.parameters.FWParametersSystemCode _motifContentieuxSus = null; %>
                <select name="idMotifContentieuxSuspendu" style="width : 100%">
                  <%	for (int i=0; i < viewBean.getCsMotifContentieuxSuspendus().size(); i++) {
								_motifContentieuxSus = (globaz.globall.parameters.FWParametersSystemCode) viewBean.getCsMotifContentieuxSuspendus().getEntity(i);
								if (_motifContentieuxSus.getIdCode().equalsIgnoreCase(viewBean.getIdMotifContentieuxSuspendu())) { %>
                  <option selected value="<%=_motifContentieuxSus.getIdCode()%>"><%=_motifContentieuxSus.getCurrentCodeUtilisateur().getLibelle()%></option>
                  <%	} else { %>
                  <option value="<%=_motifContentieuxSus.getIdCode()%>"><%=_motifContentieuxSus.getCurrentCodeUtilisateur().getLibelle()%></option>
                  <%	}
							} %>
                </select>
              </div>
            <% } %>
          </tr>
          <TR>
            <TD nowrap width="91">Betreibungsnummer</TD>
            <TD width="1"></TD>
            <TD width="270">
              <input type="text" name="numeroPoursuite" value="<%=viewBean.getNumeroPoursuite()%>" size="11" maxlength="10">
            </TD>
            <TD width="164"></TD>
            <TD width="74">Folge / Ablauf</TD>
            <TD width="21"></TD>
            <TD colspan="2">
              <select name="idSequenceContentieux" style="width : 7cm">
<!--            <option selected value="0"></option> MKA: 20.04.10 MIS EN COMMENTAIRE POUR EVITER SEQUENCE 0-->
                <%globaz.osiris.db.contentieux.CASequenceContentieux tempSeq;
				  globaz.osiris.db.contentieux.CASequenceContentieuxManager manSeq = new globaz.osiris.db.contentieux.CASequenceContentieuxManager();
				  manSeq.setSession(objSession);
				  manSeq.find();
				  for(int i = 0; i < manSeq.size(); i++){
				    	tempSeq = (globaz.osiris.db.contentieux.CASequenceContentieux)manSeq.getEntity(i);
						if  (viewBean.getIdSequenceContentieux().equalsIgnoreCase(tempSeq.getIdSequenceContentieux())) { %>
                <option selected value="<%=tempSeq.getIdSequenceContentieux()%>"><%=tempSeq.getIdSequenceContentieux()%>
                - <%=tempSeq.getDescription()%></option>
                <% } else { %>
                <option value="<%=tempSeq.getIdSequenceContentieux()%>"><%=tempSeq.getIdSequenceContentieux()%>
                - <%=tempSeq.getDescription()%></option>
                <% } %>
                <% } %>
              </select>
            </TD>
          </TR>


	<% if (APISection.ID_TYPE_SECTION_BULLETIN_NEUTRE.equals(viewBean.getIdTypeSection()) && !APISection.STATUTBN_DECOMPTE_FINAL.equals(viewBean.getStatutBN())) { %>
		<tr title="Suspendierungsstatus der Inkassoprozedur NE">
			<td nowrap width="91">Status NE</td>
			<td width="1"></td>
			<td width="270">
            	<%
            		boolean wantBlank = false;
		            // HashSet pour définir les codes systèmes qui ne doivent pas venir dans la liste
		            java.util.HashSet set = new java.util.HashSet();
					set.add(APISection.STATUTBN_DECOMPTE_FINAL);
            		if (JadeStringUtil.isBlankOrZero(viewBean.getStatutBN())) {
            			wantBlank = true;
            		}
				%>

				<ct:FWCodeSelectTag
					name="statutBN"
					defaut="<%=viewBean.getStatutBN()%>"
					codeType="OSISTATBN"
					wantBlank="<%=wantBlank%>"
					except="<%=set%>"/>
			</td>
			<td width="164"></td>
			<td width="74"></td>
			<td width="21"></td>
			<td colspan="2">&nbsp;</td>
		</tr>
	<%} %>

          <% if (CAApplication.getApplicationOsiris().getCAParametres().isContentieuxAquila()){ %>
          <tr>
            <td nowrap width="91">&nbsp;</td>
            <td width="1"></td>
            <td width="270"> </td>
            <td width="164"></td>
            <td width="74"></td>
            <td width="21"></td>
            <td colspan="2">&nbsp;<INPUT type="hidden" name="contentieuxEstSuspendu" value="<%=viewBean.getContentieuxEstSuspendu()%>" ></td>
          </tr>
          <tr>
			<td colspan="8"><ta:table title="table" id="validTable" height="100"
				readOnly="false">
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
			<TR>
            <TD nowrap width="91">&nbsp;</TD>
            <TD width="1"></TD>
            <TD width="270"> </TD>
            <TD width="164"></TD>
            <TD width="74"></TD>
            <TD width="21"></TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <% } %>
          <TR>
            <TD colspan="8" height="10" class="title" nowrap>Abzahlungsplan / Amortisationsplan</TD>
          </TR>
          <TR>
            <TD width="91">&nbsp;</TD>
            <TD width="1"></TD>
            <TD width="270"> </TD>
            <TD width="164"></TD>
            <TD width="74"></TD>
            <TD width="21"></TD>
            <TD colspan="2">&nbsp;</TD>
          </TR>
          <TR>
            <TD nowrap width="91">Zahlungsvereinbarung</TD>
            <TD width="1"></TD>
            <TD colspan="6" nowrap>
              <input type="text" name="plan" value="<%=viewBean.getPlan()%>" maxlength="30" size="31" class="inputDisabled" tabindex="-1" readonly>
            </TD>
          </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		//mettreTauxBase();

		</SCRIPT> <%	}
%>		<ct:menuChange displayId="options" menuId="CA-DetailSectionGauche" showTab="options">
			<ct:menuSetAllParams key="id" value="<%=viewBean.getIdSection()%>"/>
			<ct:menuSetAllParams key="idSection" value="<%=viewBean.getIdSection()%>"/>
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdSection()%>"/>
			<ct:menuSetAllParams key="noAffiliationId" value="<%=compteAnnexe.getIdExterneRole()%>"/>
			<ct:menuSetAllParams key="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"/>
			<ct:menuSetAllParams key="idPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
			<ct:menuSetAllParams key="forIdSection" value="<%=viewBean.getIdSection()%>"/>

			<% if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdPlanRecouvrement())) {%>
			<ct:menuActivateNode active="no" nodeId="echeances_plan"/>
			<% } else { %>
			<ct:menuActivateNode active="yes" nodeId="echeances_plan"/>
			<% } %>
		</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>