
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCO3008"; %>
<%@ page import="globaz.aquila.db.journaux.*" %>
<%
COElementJournalBatchViewBean viewBean = (COElementJournalBatchViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdJournal();

userActionValue = "aquila.journaux.elementJournalBatch.modifier";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="javascript">

function add() {
}

function upd() {
	document.forms[0].elements('userAction').value="aquila.journaux.elementJournalBatch.modifier";
}

function validate() {
    state = validateFields();

    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="back";
    } else {
        document.forms[0].elements('userAction').value="aquila.journaux.elementJournalBatch.modifier";
	}

    return state;

}

function cancel() {
  document.forms[0].elements('userAction').value="aquila.journaux.elementJournalBatch.chercher";
}

function del() {
  if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="aquila.journaux.elementJournalBatch.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Bestandteil des Journals - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
      <%-- tpl:put name="zoneTitle" --%>Bestandteil des Journal<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
            <%-- tpl:put name="zoneMain" --%>
         <TR>
           <TD width="125">Journal</TD>
           <TD width="30">&nbsp;</TD>
           <TD>
           <INPUT type="text" name="idJournalLibelle" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getIdJournal()%> - <%=viewBean.getJournalLibelle()%>" class="libelleDisabled" tabindex="-1" readonly/>
           <input type="hidden" name="idElementJournal" value="<%=viewBean.getIdElementJournal()%>"/>
           <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>"/>
           </TD>
           </TR>

           <TR>
           <TD width="125" valign="top">Konto</TD>
           <TD width="30">&nbsp;</TD>
           <TD>
           <TEXTAREA cols="40" rows="6" class="libelleLongDisabled" readonly><%=viewBean.getContentieux().getCompteAnnexe().getTitulaireEntete()%></TEXTAREA>
           </TD>
           </TR>

           <TR>
           <TD width="125" valign="top">Sektion</TD>
           <TD width="30">&nbsp;</TD>
           <TD>
           <%
             String sectionText = viewBean.getContentieux().getSection().getIdExterne() + " - " + viewBean.getContentieux().getSection().getDescription();
           %>

           <INPUT type="text" value="<%=sectionText%>" class="disabled" style="width:13cm" readonly>
           </TD>
           </TR>

           <%
               if (viewBean.getTraitementSpecifique().getCsType().equals(globaz.aquila.api.ICOTraitementSpecifique.CS_RDP_FICHIER_OP_GE)) {
             %>
               <tr><td colspan="3"><br/></td></tr>

               <TR>
        <TD width="125">Betrag  Schuldforderung</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
            <%
            	String[] soldeInitial = globaz.aquila.db.rdp.CORequisitionPoursuiteUtil.getSoldeSectionInitial(objSession, viewBean.getContentieux().getIdSection());
            	String tmpMontant = soldeInitial[0];
            	String forIdJournalNotIn = soldeInitial[1];
            	String ecrituresFromDate = soldeInitial[2];
            %>
            
              <input type="text" class="montantShortDisabled" name="montantCreance" value="<%=globaz.globall.util.JANumberFormatter.fmt(tmpMontant,true,true,false,2)%>" readonly="true" disabled="true"/>
            </TD>
            </TR>

               <TR>
        <TD width="125">Zinsensanfangsdatum</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
            <%
            	String dateDebut = "";
            	try {
            		dateDebut = globaz.aquila.db.rdp.CORequisitionPoursuiteUtil.getDateDebutInteretsTardifs(objSession, null, viewBean.getContentieux());
            	} catch (Exception e) {
	            	// Print nothing.
	            }
            %>
            <input type="text" class="montantShortDisabled" name="dateInteret" value="<%=dateDebut%>" readonly="true" disabled="true"/>
            </TD>
            </TR>

               <TR>
        <TD width="125">Gebührbetrag</TD>
            <TD width="30">&nbsp;</TD>
            <TD>
             <%
             	String montantTaxe = "";
            	try {
            		montantTaxe = globaz.globall.util.JANumberFormatter.fmt(globaz.aquila.ts.opge.utils.COTSOPGEUtils.getMontantCumuleTaxe(objSession, null, viewBean.getContentieux(), forIdJournalNotIn, ecrituresFromDate),true,true,false,2);
            	} catch (Exception e) {
	            	// Print nothing.
	            }
            %>
              <input type="text" class="montantShortDisabled" name="montantTaxe" value="<%=montantTaxe%>" readonly="true" disabled="true"/>
             </TD>
            </TR>
             <%
              }
             %>

              <tr><td colspan="3"><br/></td></tr>

		   <TR>
           <TD width="125">Bemerkungen</TD>
           <TD width="30">&nbsp;</TD>
           <TD>
           <textarea name="texteRemarque" cols="73" rows="5" wrap="hard" class="input"><%=viewBean.getRemarque().getTexte()%></textarea>
           </TD>
           </TR>

           <tr><td colspan="3"><br/></td></tr>

           <TR>
           <TD width="125">Status</TD>
           <TD width="30">&nbsp;</TD>
           <TD>
           <INPUT type="text" name="etatLibelle" style="width:7cm" maxlength="30" value="<%=viewBean.getEtatLibelle()%>" class="libelleDisabled" tabindex="-1" readonly/>
           </TD>
           </TR>

           <TR>
           <TD width="125" valign="top">Fehler</TD>
           <TD width="30">&nbsp;</TD>
           <TD>
           <textarea name="errorsMessages" cols="80" rows="5" class="textareaShowingErrors"  readonly="true" disabled="true"><%=viewBean.getErrorMessages()%></textarea>
           </TD>
           </TR>


           <tr><td colspan="3"><br/><br/><br/><br/><br/></td></tr>

           <TR>
           <TD width="125">&nbsp;</TD>
           <TD width="30">&nbsp;</TD>
           <TD>
       	   <A href="<%=request.getContextPath()%>\aquila?userAction=aquila.poursuite.contentieuxAVS.afficher&refresh=true&selectedId=<%=viewBean.getContentieux().getIdContentieux()%>&libSequence=<%=viewBean.getContentieux().getLibSequence()%>">Dossier</A>
           &nbsp;
           <A href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getContentieux().getCompteAnnexe().getTiers().getIdTiers()%>" class="external_link">Partner</A>
           &nbsp;
           <A href="<%=request.getContextPath()%>\osiris?userAction=osiris.comptes.apercuComptes.afficher&selectedId=<%=viewBean.getContentieux().getCompteAnnexe().getIdCompteAnnexe()%>" class="external_link">Konto</A>
           &nbsp;
           <A href="<%=request.getContextPath()%>\osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=viewBean.getContentieux().getSection().getIdSection()%>" class="external_link">Sektion</A>
           </TD>
           </TR>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
        <%-- tpl:put name="zoneButtons" --%>
        <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%
  if ("add".equalsIgnoreCase(request.getParameter("_method")) && request.getParameter("_valid") == null) {
  } else {
%>

<ct:menuChange displayId="options" menuId="CO-JournalElements" showTab="options" checkAdd="no">
  <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournal()%>" checkAdd="no"/>

</ct:menuChange>
<%
  }
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>