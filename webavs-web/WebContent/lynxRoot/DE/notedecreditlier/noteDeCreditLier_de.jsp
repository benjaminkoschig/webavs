<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.lynx.db.notedecreditlier.*" %>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
<%
	idEcran = "GLX0023"; 
	LXNoteDeCreditLierViewBean viewBean = (LXNoteDeCreditLierViewBean) session.getAttribute("viewBean");
	
	boolean isMethodAdd = false;
	String montant = viewBean.getMontant();
	if("add".equals(request.getParameter("_method")))  {
		montant = request.getParameter("montantNote");
		isMethodAdd = true;
	}
	
	FWCurrency currency = new FWCurrency();
	currency.add(montant);
	if(currency.isNegative()) {
		currency.negate();
	}
	
	LXOperation operationLiee = null;
	if (!globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(viewBean.getIdOperationLiee())) {
		try {
			operationLiee = globaz.lynx.utils.LXNoteDeCreditUtil.getOperationLiee(objSession, null, viewBean.getIdOperationLiee());
	
			if (LXOperation.CS_ETAT_PREPARE.equals(operationLiee.getCsEtatOperation()) || LXOperation.CS_ETAT_SOLDE.equals(operationLiee.getCsEtatOperation())) {
				bButtonDelete = false;
				bButtonUpdate = false;
			}
		} catch (Exception eOp) {
			bButtonDelete = false;
			bButtonUpdate = false;
		}
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.framework.util.FWCurrency"%><SCRIPT language="javascript">

function add() {
    document.forms[0].elements('userAction').value="lynx.notedecreditlier.noteDeCreditLier.ajouter"
}

function upd() {
  document.forms[0].elements('userAction').value="lynx.notedecreditlier.noteDeCreditLier.modifier";
  <% if(!isMethodAdd)  { %>
 	 document.getElementById("idOperationLiee").disabled = true;
  <% } %>
 
}

function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="lynx.notedecreditlier.noteDeCreditLier.ajouter";
    else
        document.forms[0].elements('userAction').value="lynx.notedecreditlier.noteDeCreditLier.modifier";

    return state;

}

function cancel() {
		document.forms[0].elements('userAction').value="back";
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte verbundene Gutschrift zu löschen! Wollen Sie fortfahren?")) {
        document.forms[0].elements('userAction').value="lynx.notedecreditlier.noteDeCreditLier.supprimer";
        document.forms[0].submit();
    }
}

function init() {}

top.document.title = "Detail einer verbundenen Gutschrift - " + top.location.href;
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Detail einer verbundenen Gutschrift<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
  
		<TR>
			<TD height="11" colspan="2">
				&nbsp;
			</TD>
		</TR>
		<TR>
		    <TD>Rechnung </TD>
		    <TD>
			<%
				String idOperationSrc = viewBean.getIdOperationSrc();
				if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(idOperationSrc)) {
					idOperationSrc = request.getParameter("idOperationSrc");
				}
				
				String idOperationLiee = viewBean.getIdOperationLiee();
				if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(idOperationLiee)) {
					idOperationLiee = request.getParameter("idOperationLiee");
				}
			
				String selectListeFacture = globaz.lynx.parser.LXSelectBlockParser.getListeFacture(objSession, request.getParameter("idFournisseur"), request.getParameter("idSociete"), idOperationLiee, idOperationSrc, isMethodAdd);

				if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectListeFacture)) {
					out.print(selectListeFacture);
				}
			%>
			</TD>
        </TR>                        
         <TR>
            <TD>Betrag</TD>
            <TD>

				 <input type="text" name="montant"  style="width:7cm;text-align : right" value="<%=currency.toStringFormat()%>" class="libelle" onchange="validateFloatNumber(this);" tabindex="1"/>
				 
            </TD>
        </TR>      
        
		<TR>
			<TD height="11" colspan="2">
			<hr size="3" width="100%">
			</TD>
		</TR>
		<TR>
			<TD width="255">Status verbundene Rechnung</TD>
			<%
				String etatAdd = "";
				if (operationLiee != null && !operationLiee.isNew()) {
					etatAdd = viewBean.getUcEtat(operationLiee.getCsEtatOperation()).getLibelle();
				}
			%>
			
			<TD><INPUT type="text" name="csEtatOperationLiee" value="<%=etatAdd%>" class="libelleLongDisabled"></TD>
		</TR>            
        
        
<!------------------------------------------------------------------------------
  		Champs cachés
-------------------------------------------------------------------------------->      

		<INPUT type="hidden" name="libelleNote" value="<%= request.getParameter("libelleNote") %>"/>
		<INPUT type="hidden" name="idSociete" value="<%= request.getParameter("idSociete") %>"/>
        <INPUT type="hidden" name="idFournisseur" value="<%= request.getParameter("idFournisseur") %>"/>
        
     <% if(isMethodAdd)  { %>    

        <INPUT type="hidden" name="idSection" value="<%= request.getParameter("idSection")%>"/>
        <INPUT type="hidden" name="idJournal" value="<%= request.getParameter("idJournal") %>"/>
        
        <INPUT type="hidden" name="idOperation" value="<%= request.getParameter("idOperation") %>"/> 
 		<INPUT type="hidden" name="idOperationSrc" value="<%= request.getParameter("idOperationSrc") %>"/>
 		
     <% } else {  %>
     
    	<INPUT type="hidden" name="idJournal" value="<%= viewBean.getIdJournal()%>"/>
    	<INPUT type="hidden" name="idSection" value="<%= viewBean.getIdSection() %>"/>
    	
        <INPUT type="hidden" name="idOperation" value="<%= viewBean.getIdOperation() %>"/>
 		<INPUT type="hidden" name="idOperationSrc" value="<%= viewBean.getIdOperationSrc()%>"/>
 		
 		
 	 <% } %>	

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>