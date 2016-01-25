<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.musca.util.FAUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran="CFA0002";%>
<%
	//contr�le des droits
	bButtonNew = objSession.hasRight(userActionNew, "ADD");

    globaz.musca.db.facturation.FAPassageViewBean viewBean = (globaz.musca.db.facturation.FAPassageViewBean)session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdPassage();
	userActionValue = "musca.facturation.passage.modifier";
	if ( globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(viewBean.getStatus())
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(viewBean.getStatus())
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_VALIDE.equalsIgnoreCase(viewBean.getStatus())){
			bButtonValidate = false;
			bButtonDelete = false;
			bButtonUpdate = false;
			bButtonNew = false;
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
<%-- tpl:put name="zoneScripts" --%><SCRIPT language="JavaScript">
top.document.title = "Facturation - D�tail d'un journal"
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="musca.facturation.passage.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="musca.facturation.passage.ajouter";
    else
        document.forms[0].elements('userAction').value="musca.facturation.passage.modifier";

    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
    document.forms[0].elements('userAction').value="back";
 else
    document.forms[0].elements('userAction').value="musca.facturation.passage.afficher"
}
function del() {
    if (window.confirm("Vous �tes sur le point de supprimer l'objet s�lectionn�! Voulez-vous continuer?")){
        document.forms[0].elements('userAction').value="musca.facturation.passage.supprimer";
        document.forms[0].submit();
    }
}
// Variable globale 
var tailleTA = 90; 

function limite(champ,taille) 
{ 
	if(champ.value.length >= taille) 
	{ 
		alert ("Vous avez d�pass� le nombre maximum de caract�res : " + tailleTA); 
		// ici on bloque la taille en cas de copier/coller 
		champ.value = champ.value.substr(0, taille); 
		// et on retourne faux sinon il ajoute le caractere quand meme onKeyPress. 
		return false; 
	} 
} 

function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>D�tail d'un journal de facturation<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
          <TR>
            <TD nowrap width="140">Num�ro</TD>
            <TD nowrap width="300"><INPUT name="idPassage" type="text" value="<%=viewBean.getIdPassage()%>" class="numeroCourtDisabled" readonly></TD>
            <TD width="50"><IMG src="<%=request.getContextPath()%><%=(viewBean.isEstVerrouille().booleanValue())?"/images/cadenas.gif" : "/images/cadenas_ouvert.gif"%>">
		</TD>
          </TR>
		<TR>
            <TD nowrap width="140">Date de facturation</TD>
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
            <TD nowrap width="140">Libell�</TD>
           	<TD nowrap width="547">
           		<INPUT name="libelle" type="text" value="<%=viewBean.getLibelle()%>" class="libelleLong" maxlength="40" doClientValidation="NOT_EMPTY">
           	</TD>
            <TD width="50"></TD>

          </TR>
	<TR>
            <TD nowrap width="140">Type de facturation</TD>
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
            <TD nowrap width="140">P�riode de facturation</TD>
            <TD nowrap width="547">
            <% if( viewBean.isEtatOuvert()) { %>
            	 <ct:FWCalendarTag name="datePeriode"
					displayType ="month"
					value="<%=viewBean.getDatePeriode()%>" /> mois.ann�e
			<% } else { %>
				<INPUT name="datePeriode" type="text" value="<%=viewBean.getDatePeriode() %>" class="libelleLongDisabled" readonly>
			<% } %>

            </TD><TD width="65" height="20"></TD>

          </TR>
	<TR>
            <TD nowrap width="140">Plan de facturation</TD>
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
            <TD nowrap width="140">Gestion automatique</TD>
            <TD nowrap width="547"> <input type="checkbox" name="isAuto" <%=(viewBean.getIsAuto().booleanValue())? "checked" : "unchecked"%>></TD>
	</TR>
	
	<TR>
            <TD nowrap width="140">D�lai en jours</TD>
            <TD nowrap width="547"><INPUT  type="text" name="delaiJour" onkeypress="return filterCharForPositivInteger(window.event);"  maxlength="3" size="3" value="<%=viewBean.getDelaiJour()%>"></TD> 
	</TR>
	
	<TR>
            <TD nowrap width="140">Verrouill�</TD>
            <TD nowrap width="547"> <input type="checkbox" name="estVerrouille" <%=(viewBean.isEstVerrouille().booleanValue())? "checked" : "unchecked"%>></TD>
            <TD width="65" height="20"></TD>

    </TR>
	<TR>
	  	<TD>Personne de r�f�rence</TD>
	  	<td>
	      <input type="text" name="personneRef" maxlength="20" size="20" value="<%=(viewBean.getPersonneRef()== null)?"":viewBean.getPersonneRef()%>">
	    </td>
    </TR>
    <TR>
	  	<TD>Signatures</TD>
	  	<td>
	      <input type="text" name="personneSign1" maxlength="20" size="20" value="<%=(viewBean.getPersonneSign1()== null)?"":viewBean.getPersonneSign1()%>">
	      <input type="text" name="personneSign2" maxlength="20" size="20" value="<%=(viewBean.getPersonneSign2()== null)?"":viewBean.getPersonneSign2()%>">
	    </td>
    </TR>
	<TR>
            <TD nowrap width="140">Date d'�ch�ance pour remboursement</TD>
             <TD nowrap width="547"><ct:FWCalendarTag name="dateEcheance"
			value="<%=viewBean.getDateEcheance()%>"
			doClientValidation="CALENDAR"/>
	     </TD>
            <TD width="50"></TD>
   	 </TR>
	<TR>
            <TD nowrap width="140">Remarque<BR>(appara&icirc;t sur tous les d&eacute;comptes)</TD>
            <TD nowrap width="547">
		<TEXTAREA rows="3" align="left" class="libelleLong" name="remarque" onkeypress="return limite(this,tailleTA);" onchange="limite(this,tailleTA);"><%=viewBean.getRemarque()%></TEXTAREA>
		</TD>
            <TD width="65"></TD>
        </TR>
		<%if(FAUtil.isSpecialisteFacturation(session)){ %>
         <TR>
            <TD width="140">
            <%
	              if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal())) {
	              	%>
	              <A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getIdJournal()%>" class="external_link">Journal associ�</A>
	              	<%
	              } else {
	              %>
					Journal associ�
	              <%
	              }
	          %>
            </TD>
            <TD colspan="2">
            <INPUT name="idJournal" type="text" value="<%=(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal()))?viewBean.getIdJournal():""%>" class="numeroCourt">
            </TD>
		 </TR>
		 <%}else{%>
		 <TR>
            <TD width="140">
            <%
	              if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal())) {
	              	%>
	              <A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuJournal.afficher&selectedId=<%=viewBean.getIdJournal()%>" class="external_link">Journal associ�</A>
	              	<%
	              } else {
	              %>
					Journal associ�
	              <%
	              }
	          %>
            </TD>
            <TD colspan="2">
            <input type="hidden" name="idJournal" value="<%=viewBean.getIdJournal()%>"/>
            <INPUT name="idJournalAffichee" type="text" value="<%=(!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdJournal()))?viewBean.getIdJournal():""%>" class="numeroCourtDisabled" readonly>
            </TD>
		 </TR>
		 <%}%>

	<TR>
            <TD nowrap width="140">Etat</TD>
            <TD nowrap width="547"><INPUT name="libelleStatus" type="text" value="<%=globaz.musca.translation.CodeSystem.getLibelle(session, viewBean.getStatus())%>" class="libelleLongDisabled" readonly></TD>
            <TD width="50"><INPUT name="status" type="hidden" value="<%=viewBean.getStatus()%>"></TD>
   </TR>
   
   <TR>
            <TD nowrap width="140">Propri�taire</TD>
            <TD nowrap width="547"><INPUT name="libelleUserCreateur" type="text" value="<%=viewBean.giveUserCreateur()%>" class="libelleLongDisabled" readonly></TD>
            <TD width="50"><INPUT name="userCreateur" type="hidden" value="<%=viewBean.getUserCreateur()%>"></TD>
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

//Pour agrandir le taglib concern� et l'aligner
document.getElementById("idTypeFacturation").style.width=120;

//Affiche information
<%
	if(globaz.musca.db.facturation.FAPassage.CS_ETAT_COMPTABILISE.equalsIgnoreCase(viewBean.getStatus())
		|| globaz.musca.db.facturation.FAPassage.CS_ETAT_ANNULE.equalsIgnoreCase(viewBean.getStatus()) ){
		%> window.alert("Ce passage est soit annul� soit comptabilis�. Impossible de le modifier.");<%
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