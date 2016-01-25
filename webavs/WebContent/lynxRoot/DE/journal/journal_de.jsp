<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GLX0009"; %>
<%@ page import="globaz.lynx.db.journal.*" %>
<%@ page import="globaz.globall.util.*" %>
<%@page import="globaz.lynx.utils.LXJournalUtil"%>
<%
LXJournalViewBean viewBean = (LXJournalViewBean) session.getAttribute("viewBean");
selectedIdValue = viewBean.getIdJournal();
bButtonDelete = false;
if(!LXJournalUtil.isOuvert(objSession, viewBean.getIdJournal())){
	bButtonUpdate = false;
}

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="lynx.journal.journal.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="lynx.journal.journal.modifier";
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.journal.journal.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.journal.journal.modifier";

    return state;

}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="lynx.journal.journal.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Journal zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="lynx.journal.journal.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.libelleSociete;
	}
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Die Schuldnerfirma existiert nicht.");
	}
}

top.document.title = "Detail des Journals - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Journals<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
            <%  // Initialisation des variables suivant si on est en ajout ou en modif
            	String dateAdd = viewBean.getDateCreation();
            	String etatAdd = viewBean.getUcEtat().getLibelle();
            	String codeEtatAdd = viewBean.getCsEtat();
            	String proprietaireAdd = viewBean.getProprietaire();
            	
				if ("add".equalsIgnoreCase(request.getParameter("_method")) && (request.getParameter("_valid") == null || request.getParameter("_valid").equals("fail"))) {
					dateAdd = JACalendar.format(JACalendar.today(), JACalendar.FORMAT_DDsMMsYYYY);
					etatAdd = viewBean.getUcEtat(LXJournal.CS_ETAT_OUVERT).getLibelle();
					codeEtatAdd = LXJournal.CS_ETAT_OUVERT;
					proprietaireAdd = objSession.getUserName();
				}
	        %>

	       <tr>
            <td>Journal</td>
            <td colspan="2">
              <input type="text" name="idJournal" style="width:7cm" size="16" maxlength="15" value="<%=viewBean.getIdJournal()%>" class="libelleDisabled" readonly="readonly"/>
            </td>
          </tr>
		<tr>
        	<td>Buchungsdatum</td>
            <td colspan="2">
            	<ct:FWCalendarTag name="dateValeurCG" doClientValidation="CALENDAR" value="<%=viewBean.getDateValeurCG()%>" tabindex="1"/>
			</td>
		</tr>         
		<tr>
        	<td>Erstellungsdatum</td>
            <td colspan="2">
				<INPUT type="text" name="" style="width:2.5cm" size="30" maxlength="30" value="<%=dateAdd%>" class="libelleLongDisabled" readonly="readonly">    
 			</td>
		</tr>   
         <TR>
         	<TD>Schuldnerfirma</TD>
         	<TD colspan="2">
				<%@ include file="/lynxRoot/include/societe.jsp" %>
				
				<%
					if (!"add".equals(request.getParameter("_method"))) {
				%>
					<script language="JavaScript">document.getElementById("idExterneSociete").className = "libelleLongDisabled";</script>				
				<%
					}
				%>
				
			</TD>
        </TR>        
         <TR>
            <TD>Bezeichnung</TD>
            <TD colspan="2">
				 <input type="text" name="libelle" style="width:7cm" size="41" maxlength="40" value="<%=viewBean.getLibelle()%>" class="libelle" tabindex="1"/>
            </TD>
        </TR>                        
         <TR>
            <TD>Benutzer</TD>
            <TD colspan="2">
				 <input type="text" name="proprietaire" style="width:7cm" size="121" maxlength="20" value="<%=proprietaireAdd%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR> 
          <TR>
            <TD>Status</TD>
            <TD colspan="2">
            	<input type="hidden" name="csEtat" value="<%=codeEtatAdd%>"/>
				<input type="text" name="" style="width:7cm" value="<%=etatAdd%>" class="libelleLongDisabled" readonly="readonly"/> 
			</TD>
        </TR>     
		<TR> 
			<TD height="11" colspan="4"> 
				<hr size="3" width="100%">
			</TD>
		</TR>
        <TR>
            <TD>Total Rechnungen</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getTotalFactures()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR>        
        <TR>
            <TD>Total Gutschriften</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getTotalNoteDeCredit()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR> 
        <TR>
            <TD>Total Zahlung</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getTotalPaiement()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR>        
        <TR>
            <TD>Total Diskont</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getTotalEscompte()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR>   
        <TR>
            <TD>Total Stornierung</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getTotalExtourne()%>" class="libelleDisabled" readonly="readonly"/>
            </TD>
        </TR>   
		<TR> 
			<TD height="11" colspan="4"> 
				<hr size="3" width="100%">
			</TD>
		</TR>
          <TR>
            <TD>
			<%
	              if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournalCG())) {
	              	%>
	              <A href="<%=request.getContextPath()%>/helios?userAction=helios.special.journal.afficher&idJournal=<%=viewBean.getIdJournalCG()%>" class="external_link">Verknüpftes Buchungsjournal</A>
	              	<%
	              } else {
	              %>
					Verknüpftes Buchungsjournal
	              <%
	              }
	          %>
			</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getIdJournalCG()%>" class="libelleLongDisabled"/>
            </TD>
        </TR>    
        <TR>
            <TD>
			<%
				if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdOrdreGroupeSrc())) {
			%>
	              <A href="<%=request.getContextPath()%>/lynx?userAction=lynx.ordregroupe.ordreGroupe.afficher&selectedId=<%=viewBean.getIdOrdreGroupeSrc()%>" class="link">Verknüpfter Sammelauftrag</A>
	              	<%
	              		} else {
	              	%>
					Verknüpfter Sammelauftrag
	              <%
	              		}
	              	%>
			</TD>
            <TD colspan="2">
				 <input type="text" name="" style="width:7cm" size="10" maxlength="9" value="<%=viewBean.getIdOrdreGroupeSrc()%>" class="libelleLongDisabled"/>
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
<ct:menuChange displayId="options" menuId="LX-Journal" showTab="options" checkAdd="no">
	<ct:menuSetAllParams key='selectedId' value='<%=viewBean.getIdJournal()%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idSociete' value='<%=viewBean.getIdSociete()%>' checkAdd='no'/>
	<ct:menuSetAllParams key='idJournal' value='<%=viewBean.getIdJournal()%>' checkAdd='no'/>
</ct:menuChange>
<%
	}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>