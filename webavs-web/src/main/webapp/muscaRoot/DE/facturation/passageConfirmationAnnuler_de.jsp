<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%	
	idEcran="CFA0013";

	//Redéfinition des boutons et des labels des boutons
	btnValLabel = "Journal\nannullieren";
	btnCanLabel = "Zurück";
	
	bButtonUpdate = false;
	bButtonDelete = false;
	
    globaz.musca.db.facturation.FAPassageViewBean viewBean = (globaz.musca.db.facturation.FAPassageViewBean)session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPassage();

	
	//Regarde si le passage n'est pas: verrouillé, comptabilisé ou déjà annulé
	String passageStatus = globaz.musca.util.FAUtil.getPassageStatus(viewBean.getIdPassage(),session);
	boolean passageLocked =globaz.musca.util.FAUtil.getPassageLock(viewBean.getIdPassage(),session).booleanValue();
 
	if( globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus)
		|| passageLocked
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus) ){
			bButtonValidate = false;
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

	top.document.title = "Fakturierung - Journal annullieren"
	<!--hide this script from non-javascript-enabled browsers

	function add() {}
	function upd() {}
	function init(){}
	
	function validate() {
    	state = validateFields();
	    if (window.confirm("Sie sind dabei, das ausgewählte Journal zu annullieren ! Es wird unmöglich sein, ihn zu reaktivieren. Wollen Sie fortfahren?")){
			document.forms[0].elements('userAction').value="musca.facturation.passage.annuler";
			return state;
	    }
	    return false;
	}

	function cancel() {
    	document.forms[0].elements('userAction').value="musca.facturation.passage.chercher"
	}
		
	function postInit(){
		readOnly(true);
	}
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Journal annullieren<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<TR>
            <TD nowrap width="140">Job.-Nr.</TD>
            <TD nowrap width="300"><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly></TD>
            <TD width="50"><IMG src="<%=request.getContextPath()%><%=(viewBean.isEstVerrouille().booleanValue())?"/images/cadenas.gif" : "/images/cadenas_ouvert.gif"%>">
		</TD>
          </TR>
		<TR>
            <TD nowrap width="140">Fakturierungsdatum</TD>
            <% if( viewBean.isEtatOuvert()) { %>
            	<TD nowrap width="547">
            		<ct:FWCalendarTag name="dateFacturation" value="<%=viewBean.getDateFacturation()%>" /></TD>
            	<TD width="50"></TD>
			<% } else { %>
				<TD nowrap width="547">
					<INPUT name="dateFacturation" type="text" value="<%=viewBean.getDateFacturation()%>" class="numeroDisabled" readonly>
				<TD width="50"></TD>
			<% } %>
            
            <%-- %><TD nowrap width="547">
            <ct:FWCalendarTag name="dateFacturation" value="<%=viewBean.getDateFacturation()%>" /></TD>
            <TD width="50"></TD>--%>
   	    </TR>
	   <TR>
            <TD nowrap width="140">Bezeichnung</TD>
           	<TD nowrap width="547">
           		<INPUT name="libelle" type="text" value="<%=viewBean.getLibelle()%>" class="libelleLong" maxlength="40" doClientValidation="NOT_EMPTY">
           	</TD>
            <TD width="50"></TD>

          </TR>
	<TR>
            <TD nowrap width="140">Fakturierungstyp</TD>
            <TD nowrap width="547">
            <% if( viewBean.isEtatOuvert()) { %>
            	 <ct:FWSystemCodeSelectTag name="idTypeFacturation"
            		defaut="<%=viewBean.getIdTypeFacturation()%>"
         			codeSystemManager="<%=globaz.musca.translation.CodeSystem.getLcsTypeFacturationWithoutBlank(session)%>"
					except="<%=viewBean.getExceptTypeFacturation()%>"/>
			<% } else { %>
				<INPUT name="idTypeFacturation" type="hidden" value="<%=viewBean.getIdTypeFacturation() %>">
				<INPUT name="typeFacturationLibelle" type="text" value="<%=objSession.getCodeLibelle(viewBean.getIdTypeFacturation()) %>" class="libelleLongDisabled" readonly>
			<% } %>
           </TD>
           </TR>
	<TR>
            <TD nowrap width="140">Fakturierungsperiode</TD>
            <TD nowrap width="547">
            <% if( viewBean.isEtatOuvert()) { %>
            	 <ct:FWCalendarTag name="datePeriode"
					displayType ="month"
					value="<%=viewBean.getDatePeriode()%>" /> Monat.Jahr
			<% } else { %>
				<INPUT name="datePeriode" type="text" value="<%=viewBean.getDatePeriode() %>" class="libelleLongDisabled" readonly>
			<% } %>

            </TD><TD width="65" height="20"></TD>

          </TR>
	<TR>
            <TD nowrap width="140">Fakturierungsplan</TD>
           <TD nowrap width="547">
            <% if( viewBean.isEtatOuvert()) { %>
             	<ct:FWListSelectTag name="idPlanFacturation"
            		defaut="<%=viewBean.getIdPlanFacturation()%>"
            		data="<%=globaz.musca.util.FAUtil.getPlanList(session)%>"/>
            <% } else { %>
				<INPUT name="LibellePlanFacturation" type="text" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getLibellePlanFacturation(),"\"","&quot;")%>" class="libelleLongDisabled" readonly>
			<% } %>
             </TD>
             <TD width="50"></TD>

          </TR>
	<TR>
            <TD nowrap width="140">Gesperrt</TD>
            <TD nowrap width="547"> <input type="checkbox" name="estVerrouille" <%=(viewBean.isEstVerrouille().booleanValue())? "checked" : "unchecked"%>>

       	</TD>
            <TD width="65" height="20"></TD>

    </TR>
	<TR>
	  	<TD>Referenzperson</TD>
	  	<td> 
	      <input type="text" name="personneRef" maxlength="20" size="20" value="<%=(viewBean.getPersonneRef()== null)?"":viewBean.getPersonneRef()%>">
	    </td>
    </TR>
    <TR>
	  	<TD>Unterschriften</TD>
	  	<td> 
	      <input type="text" name="personneSign1" maxlength="20" size="20" value="<%=(viewBean.getPersonneSign1()== null)?"":viewBean.getPersonneSign1()%>">
	      <input type="text" name="personneSign2" maxlength="20" size="20" value="<%=(viewBean.getPersonneSign2()== null)?"":viewBean.getPersonneSign2()%>">
	    </td>
    </TR>
	<TR>
            <TD nowrap width="140">Fälligkeitsdatum für Erstattung</TD>
             <TD nowrap width="547"><ct:FWCalendarTag name="dateEcheance"
			value="<%=viewBean.getDateEcheance()%>"
			doClientValidation="CALENDAR"/>
	     </TD>
            <TD width="50"></TD>
   	 </TR>
	<TR>
            <TD nowrap width="140">Bemerkung<BR>(erscheint auf alle Abrechnungen)</TD>
            <TD nowrap width="547">
		<TEXTAREA rows="7" width="250" align="left" class="libelleLong" maxlength="90" name="remarque"><%=viewBean.getRemarque()%></TEXTAREA>
		</TD>
            <TD width="65"></TD>
        </TR>

         <TR>
            <TD width="140">Journal-Nr. (Hilfsbuchhaltung)</TD>
            <TD colspan="2">
            <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>"/>
            <INPUT name="idJournalAffichee" type="text" value="<%=(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal()))?viewBean.getIdJournal():""%>" class="numeroCourtDisabled" readonly>
            </TD>
		 </TR>

	<TR>
            <TD nowrap width="140">Status</TD>
            <TD nowrap width="547"><INPUT name="libelleStatus" type="text" value="<%=globaz.musca.translation.CodeSystem.getLibelle(session, viewBean.getStatus())%>" class="libelleLongDisabled" readonly></TD>
            <TD width="50"><INPUT name="status" type="hidden" value="<%=viewBean.getStatus()%>"></TD>
       </TR>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>

</SCRIPT> 
<%  }  %>
<script>
//Pour agrandir le taglib concerné et l'aligner
document.getElementById("idTypeFacturation").style.width=120;

//Affiche information
<%
	if(globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(passageStatus)
		|| passageLocked
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(passageStatus) ){
		%> window.alert("Dieser Job ist bereits annulliert, verbucht oder verriegelt. Unmöglich ihn zu devalidieren.");<%
	}
%>
</script>
<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="FA-PassageFacturation" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="forIdJournalCalcul" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="idPassage" value="<%=viewBean.getIdPassage()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPassage()%>"/>
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>