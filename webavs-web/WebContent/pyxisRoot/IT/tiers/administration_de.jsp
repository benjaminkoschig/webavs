<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0006";
	globaz.pyxis.db.tiers.TIAdministrationViewBean viewBean = (globaz.pyxis.db.tiers.TIAdministrationViewBean )session.getAttribute ("viewBean");
	selectedIdValue = viewBean.getIdTiers();

%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">

<!--hide this script from non-javascript-enabled browsers
top.document.title = "Tiers - Administration Détail"

function checkModification() {

    var result = true;

    // Designation1
    if (result) {
    	var cspNom = new CodeSystemPopup();
	cspNom.setMotifElement("motifModifDesignation1");
    	cspNom.setDateElement("dateModifDesignation1");
    	cspNom.setElementToCheck("oldDesignation1","designation1");
    	cspNom.setLibelle("Scegliete il motivo della modifica del campo 'Cognome' e la data di entrata in vigore");
    	if (cspNom.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspNom ,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // Designation2
    if (result) {
    	var cspDesi2 = new CodeSystemPopup();
	cspDesi2.setMotifElement("motifModifDesignation2");
    	cspDesi2.setDateElement("dateModifDesignation2");
    	cspDesi2.setElementToCheck("oldDesignation2","designation2");
    	cspDesi2.setLibelle("Scegliete il motivo della modifica del campo 'Cognome seguito 1' e la data di entrata in vigore");
    	if (cspDesi2.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspDesi2,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }

    // Designation3
    if (result) {
    	var cspDesi3= new CodeSystemPopup();
	cspDesi3.setMotifElement("motifModifDesignation3");
    	cspDesi3.setDateElement("dateModifDesignation3");
    	cspDesi3.setElementToCheck("oldDesignation3","designation3");
    	cspDesi3.setLibelle("Scegliete il motivo della modifica del campo 'Cognome seguito 2' e la data di entrata invigore");
    	if (cspDesi3.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspDesi3,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }
    
    
    // Nationalite
    if (result) {
    	var cspNationalite= new CodeSystemPopup();
		cspNationalite.setMotifElement("motifModifPays");
    	cspNationalite.setDateElement("dateModifPays");
    	cspNationalite.setElementToCheck("oldPays","idPays");
    	cspNationalite.setLibelle("Scegliete il motivo della modifica del paese e la data di entrata in vigore");
    	if (cspNationalite.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspNationalite,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }
        

    return result;
}



function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.administration.ajouter"
}
function upd() {
}
function validate() {
	var exit = true;
	var message = "ERREUR tous les champs obligatoire ne sont pas rempli !";

	if (document.forms[0].elements('genreAdministration').value == "")
	{
		message = message + "\nVous n'avez pas saisi de genre d'administration!";
		exit = false;
	}
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.administration.ajouter";
	else {
		document.forms[0].elements('userAction').value="pyxis.tiers.administration.modifier";
		exite = checkModification();

	}
	return (exit);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.administration.afficher";
}
function del() {
	if (window.confirm("Siete in fase di sopprimere l'oggetto selezionato! Volete continuare?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.administration.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Amministrazione - dettaglio<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 

          
          <TR> 
            <TD nowrap width="100">Genere</TD>
            <TD nowrap> 
             <ct:FWCodeSelectTag name="genreAdministration"
            		defaut="<%=viewBean.getGenreAdministration()%>"
            		codeType="PYGENREADM"/>
            </TD>
            <TD width="50" nowrap></TD>
            <TD nowrap width="100">Paese</TD>
            <TD nowrap width="125"> 
               <ct:FWListSelectTag name="idPays" 
            		defaut="<%=viewBean.getIdPays()%>"
            		data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>
            		
			<!-- champs caché pour la creation de l'historique -->
			<input type ="hidden" name="motifModifPays" value="">
			<input type ="hidden" name="dateModifPays" value="">
			<input type ="hidden" name="oldPays" value="<%=viewBean.getOldPays()%>">

            </TD>
          </TR>
	   	   <TR> 
            <TD nowrap width="125">&nbsp;</TD>
          </TR>
		<TR> 
            <TD nowrap width="100">Codice</TD>
            <TD nowrap><INPUT type="text" name="codeAdministration"  style="text-transform : capitalize;" class="numero" value="<%=viewBean.getCodeAdministration()%>"> *</TD>
            <TD width="50"></TD>
            <TD nowrap width="100">Cantone</TD>
            <TD nowrap width="125">
		       <ct:FWCodeSelectTag name="canton"
            		defaut="<%=viewBean.getCanton()%>"
            		codeType="PYCANTON"
            		wantBlank="true"
            		/>
   	   </TD>
          </TR>
          
          <tr>
          	<td>Codice istituto</td>
            <TD nowrap><INPUT type="text" name="codeInstitution"   value="<%=viewBean.getCodeInstitution()%>"></TD>
          </tr>
          
          
          

          <TR> 
            <TD nowrap width="125">&nbsp;</TD>
          </TR>
          <TR> 
            <TD nowrap width="100">Cognome</TD>
            <TD width="100" nowrap><INPUT type="text" maxlength="40" name="designation1"  class="libelleLong" value="<%=viewBean.getDesignation1()%>">
		<input type ="hidden" name="motifModifDesignation1" value="">
		<input type ="hidden" name="dateModifDesignation1" value="">
		<input type ="hidden" name="oldDesignation1" value="<%=viewBean.getOldDesignation1()%>">


		</TD>
	      <TD width="50"></TD>
          
          </TR>
          <TR> 
            <TD nowrap width="100">Cognome seguito 1</TD>
            <TD nowrap> 
              <INPUT type="text" name="designation2" class="libelleLong" maxlength="40" value="<%=viewBean.getDesignation2()%>">
		<input type ="hidden" name="motifModifDesignation2" value="">
		<input type ="hidden" name="dateModifDesignation2" value="">
		<input type ="hidden" name="oldDesignation2" value="<%=viewBean.getOldDesignation2()%>">


            </TD>
             <TD width="50"></TD>
           
          </TR>
          <TR> 
            <TD nowrap width="100">Cognome seguito 2</TD>
            <TD nowrap> <INPUT type="text" name="designation3" maxlength="40" class="libelleLong" value="<%=viewBean.getDesignation3()%>">
		<input type ="hidden" name="motifModifDesignation3" value="">
		<input type ="hidden" name="dateModifDesignation3" value="">
		<input type ="hidden" name="oldDesignation3" value="<%=viewBean.getOldDesignation3()%>">


		</TD>
            <TD width="50"><input type="hidden" name="_creation" style="text-transform : capitalize;" class="numero" size="3" maxlength="3" value="test"></TD>
           
          </TR>
          <TR> 
            <TD nowrap width="100"></TD>
            <TD nowrap></TD>
             <TD width="50"></TD>
                     </TR>
          <TR> 
            <TD nowrap width="100">Lingua</TD>
            <TD nowrap><ct:FWCodeSelectTag name="langue"
            		defaut="<%=viewBean.getLangue()%>"
            		codeType="PYLANGUE"/>
		</TD>
             <TD width="50"></TD>
                     </TR>
	
	<tr>
	<td>
		Senza attività
	</td>
	<td>
		<input tabindex ="20" type="checkbox" name="inactif" <%=(viewBean.getInactif().booleanValue())?"CHECKED":""%>>
	</td>
	</tr>
	
	
	<TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap></TD>
             <TD width="50">
      			<INPUT type="hidden" name="colonneSelection" value="<%=viewBean.getColonneSelection()%>">
	     </TD>
          </TR>

	<tr>
            <TD nowrap width="130">Indirizzo principale della corrispondenza</TD>
            <TD width="260">
        <TEXTAREA rows="5" width="250" align="left" readonly class="libelleLongDisabled" readonly><%=viewBean.getAdressePrincipaleCourrier()%>
	 </TEXTAREA>
    	</tr>

          <TR> 
            <TD nowrap width="100">&nbsp;</TD>
            <TD nowrap></TD>
             <TD width="50"></TD>
            <TD nowrap width="100"></TD>
            <TD nowrap width="125">
            <input type="hidden" name="personneMorale" value ="<%=(viewBean.getPersonneMorale().booleanValue())?"on":""%>">
			
		</TD>
          </TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <SCRIPT>
		</SCRIPT> <%	} 
%>
		<ct:menuChange displayId="options" menuId="tiers-administration" showTab="options">
			<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
		</ct:menuChange>

 <%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>