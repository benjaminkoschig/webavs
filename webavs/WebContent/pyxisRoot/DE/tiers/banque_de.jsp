<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
	idEcran ="GTI0004";
	globaz.pyxis.db.tiers.TIBanqueViewBean viewBean = (globaz.pyxis.db.tiers.TIBanqueViewBean )session.getAttribute ("viewBean");
	selectedIdValue = request.getParameter("selectedId");

%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<SCRIPT language="JavaScript">
</SCRIPT> <%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/CodeSystemPopup.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%><SCRIPT language="JavaScript">


<!--hide this script from non-javascript-enabled browsers -->
top.document.title = "Tiers - Bank Détail"

function checkModification() {

    var result = true;

    // Designation1
    if (result) {
    	var cspNom = new CodeSystemPopup();
	cspNom.setMotifElement("motifModifDesignation1");
    	cspNom.setDateElement("dateModifDesignation1");
    	cspNom.setElementToCheck("oldDesignation1","designation1");
    	cspNom.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Bank' und das Inkrafttreten");
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
    	cspDesi2.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Bank weiter' und das Inkrafttreten");
    	if (cspDesi2.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspDesi2,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }
    
        // Nationalite
    if (result) {
    	var cspNationalite= new CodeSystemPopup();
		cspNationalite.setMotifElement("motifModifPays");
    	cspNationalite.setDateElement("dateModifPays");
    	cspNationalite.setElementToCheck("oldPays","idPays");
    	cspNationalite.setLibelle("Wählen Sie den Grund der Änderung des Felds 'Land' und das Inkrafttreten");
    	if (cspNationalite.hasChanged()) {
      		result = showModalDialog('<%=request.getContextPath()%><%=(mainServletPath+"Root")%>/<%=languePage%>/motifmodal.jsp',cspNationalite,'dialogHeight:13;dialogWidth:20;status:no;resizable:no');
	}	
    }
    
    return result;
}



function add() {
	document.forms[0].elements('userAction').value="pyxis.tiers.banque.ajouter"
}
function upd() {
}
function validate() {


	state = validateFields(); 

	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="pyxis.tiers.banque.ajouter";
	else {
		document.forms[0].elements('userAction').value="pyxis.tiers.banque.modifier";
		state = checkModification();

	}
	return (state);
}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pyxis.tiers.banque.afficher";
}
function del() {
	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="pyxis.tiers.banque.supprimer";
		document.forms[0].submit();
	}
}
function init(){}
/*
*/
// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Bank - Detail<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%> 

          
          <TR>
            <TD width="119">Clearing-Nr.</TD>
            <TD nowrap width="211"><INPUT type="text" name="clearing" class="numero" value="<%=viewBean.getClearing()%>" maxlength="5"> <%=(JadeStringUtil.isIntegerEmpty(viewBean.getNewClearing()))?"":"<b> -> "+viewBean.getNewClearing()+"</b>"%></TD>
            <TD width="50" nowrap></TD>
            <TD width="180"></TD>
            <TD nowrap width="125"></TD>
          </TR>
           <TR>
            <TD width="119">Bank</TD>
            <TD nowrap width="211"> 
              <INPUT type="text" name="designation1" maxlength="40" class="libelleLong" value="<%=viewBean.getDesignation1()%>">
		<input type ="hidden" name="motifModifDesignation1" value="">
		<input type ="hidden" name="dateModifDesignation1" value="">
		<input type ="hidden" name="oldDesignation1" value="<%=viewBean.getOldDesignation1()%>">


		</TD>
            <TD width="50"></TD>
          
          </TR>
          <TR>
            <TD width="119">Bank weiter</TD>
            <TD nowrap width="211"> 
              <INPUT type="text" name="designation2" maxlength="40" class="libelleLong" value="<%=viewBean.getDesignation2()%>">
		<input type ="hidden" name="motifModifDesignation2" value="">
		<input type ="hidden" name="dateModifDesignation2" value="">
		<input type ="hidden" name="oldDesignation2" value="<%=viewBean.getOldDesignation2()%>">


		</TD>
            <TD width="50"></TD>
            
          </TR>
  	 <TR>
            <TD width="119"></TD>
            <TD nowrap width="211"></TD>
            <TD width="50"></TD>
	<!-- 
           <TD width="180">Email</TD>
            <TD nowrap width="125"><INPUT type="text" name="numEmailBean" class="libelleLong" value="<%=viewBean.getNumEmail()%>"></TD>
	-->
          </TR>
		
	   	  <TR>
            <TD width="119"></TD>
            <TD nowrap width="211">&nbsp;</TD>
            <TD width="50"></TD>
            <TD width="180"></TD>
            <TD nowrap width="125"></TD>
          </TR>

          <TR>
            <TD width="119">SWIFT Code</TD>
            <TD nowrap width="211"> 
              <INPUT type="text" name="codeSwift" style="text-transform : capitalize;" value="<%=viewBean.getCodeSwift()%>" size="20" maxlength="20"></TD>
            <TD width="50"></TD>
            <TD width="180">IBAN</TD>
            <TD nowrap width="125" align="left"><INPUT type="text" name="iban" class="libelleLong" value="<%=viewBean.getIban()%>"></TD>
          </TR>
          
          
          <TR>
            <TD width="119">Code</TD>
            <TD nowrap width="211"> 
              <INPUT type="text" name="code" class="libelleLong" value="<%=viewBean.getCode()%>" maxlength="30"></TD>
            <TD width="50"></TD>
            <TD width="180"></TD>
            <TD nowrap width="125" align="left"></TD>
          </TR>
          

          <TR>
            <TD width="119"></TD>
            <TD nowrap width="211">&nbsp;</TD>
            <TD width="50"></TD>
            <TD width="180"></TD>
            <TD nowrap width="125"></TD>
          </TR>
          <TR>
            <TD width="119">Postkonto-Nr. </TD>
            <TD nowrap width="211">  
                   <input type="hidden" name="_creation" style="text-transform : capitalize;" class="numero" size="3" maxlength="3" value="test">
                   <INPUT type="text" name="numCcpBanque" class="libelleLong" value="<%=viewBean.afficheNumCcpBanque()%>"></TD>
            <TD width="50"></TD>
            <TD width="180">Land</TD>
            <TD nowrap width="125" align="left">
		  <ct:FWListSelectTag name="idPays" 
            		defaut="<%=viewBean.getIdPays()%>"
            		data="<%=globaz.pyxis.db.adressecourrier.TIPays.getPaysList(session)%>"/>		</TD>

			<!-- champs caché pour la creation de l'historique -->
			<input type ="hidden" name="motifModifPays" value="">
			<input type ="hidden" name="dateModifPays" value="">
			<input type ="hidden" name="oldPays" value="<%=viewBean.getOldPays()%>">


          </TR>
          <TR>
            <TD width="119">Konto-Nr.</TD>
            <TD nowrap width="211"><INPUT type="text" name="numCompteBancaire" class="libelleLong" value="<%=viewBean.getNumCompteBancaire()%>"></TD>
            <TD width="50"></TD>
            <TD width="180">Sprache</TD>
            <TD nowrap width="125" align="left">
		<ct:FWCodeSelectTag name="langue"
            		defaut="<%=viewBean.getLangue()%>"
            		codeType="PYLANGUE"/>
		</TD>
          
          
          </TR>
          
 
 

          <tr>
	<td>
		Filiale-Nr.
	</td>
	<td>
		<input tabindex ="20" type="text" class="libelleLong" name="numMiseAjour" value="<%=viewBean.getNumMiseAjour()%>">
	</td>
</tr>         
          <tr>
	<td>
		Inaktiv
	</td>
	<td>
		<input tabindex ="20" type="checkbox" name="inactif" <%=(viewBean.getInactif().booleanValue())?"CHECKED":""%>>
	</td>
</tr>
          
	<TR>
            <TD width="119"></TD>
            <TD nowrap width="211"></TD>
            <TD width="50">&nbsp;</TD>
            <TD width="180"></TD>
            <TD nowrap width="125">
			<INPUT type="hidden" name="colonneSelection" value="<%=viewBean.getColonneSelection()%>">
      	    <input type="hidden" name="personneMorale" value ="<%=(viewBean.getPersonneMorale().booleanValue())?"on":""%>">
      	    
      	     </TD>
          </TR>

	<tr>
            <TD width="119"><span style="font-family:wingdings;font-size:12 pt">+</span>Hauptversandadresse</TD>
            <TD width="211">
        <TEXTAREA rows="5" width="250" align="left" readonly class="libelleLongDisabled"><%=viewBean.getAdressePrincipaleCourrier()%>
	 </TEXTAREA>
    	</TD>
          </tr>

          <TR>
            <TD width="119">&nbsp;</TD>
            <TD nowrap width="211"></TD>
            <TD width="50"></TD>
            <TD width="180"></TD>
            <TD nowrap width="125"></TD>
          </TR>          
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> 
<SCRIPT>
</SCRIPT> 
<%}%>


<ct:menuChange displayId="options" menuId="tiers-banque" showTab="options">
	<ct:menuSetAllParams key="idTiers" value="<%=viewBean.getIdTiers()%>"/>
</ct:menuChange>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>