<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.pots.RFParametrageGrandeQDViewBean"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
	idEcran="PRF0030";
	RFParametrageGrandeQDViewBean viewBean = (RFParametrageGrandeQDViewBean) session.getAttribute("viewBean");
	autoShowErrorPopup = true;	
	bButtonCancel = false;
%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  servlet = "<%=(servletContext + mainServletPath)%>";
  
  function add() {	           
	  
	  document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PARAMETRAGE_GRANDE_QD%>.ajouter";   
		      		      	    
  }

  function upd() {	  	  
	}

  function validate() {
    state = true;

    if (document.forms[0].elements('_method').value == "add"){    	
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PARAMETRAGE_GRANDE_QD%>.ajouter";
    }
    else{    	
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PARAMETRAGE_GRANDE_QD%>.modifier";
    }
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PARAMETRAGE_GRANDE_QD%>.afficher";
  }

  function del() {
	    if (window.confirm("<ct:FWLabel key='WARNING_RF_PARAMETRAGE_QD_SUPPRESSION_OBJET'/>")){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_PARAMETRAGE_GRANDE_QD%>.supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){	    	    

		// recharger la page rcListe du parent si une modification a ete effectuee
		<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
		  	// mise a jour de la liste du parent
			if (parent.document.forms[0]) {
				parent.document.forms[0].submit();
			}						
		<%}%>	


  }

  function postInit(){
	  	//selon si l'on est en read ou en ajout il faut le forcer	  	  
		<%if(FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>		
			errorObj.text="<%=viewBean.getMessage()%>";
			showErrors();
			errorObj.text="";			
			//il faut se mettre en add ssi erreur en add!
			<%if(viewBean.getIsUpdate()){%>
				action('read');
			<%}else{%>
				action('add');
			<%}%>			
		<%}%>		

  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_SAISIE_PERIODE_SOIN_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD colspan="6">
			<table>	
				<tr>		
					<TD>
						<ct:FWLabel key="JSP_RF_PARAM_SOINS_DU" />&nbsp;<input data-g-calendar=" "  name="forDateDebut"  value="<%=viewBean.getForDateDebut() %>" />&nbsp;&nbsp;&nbsp;<ct:FWLabel key="JSP_RF_PARAM_SOINS_AU" />&nbsp;<input data-g-calendar=" "  name="forDateFin" value="<%=viewBean.getForDateFin()%>" />&nbsp;					
					</TD>
				</tr>
			</table>
		</TD>																		
	</TR>	
	<TR><TD>
	<TABLE cellspacing="3px">	
		<TR><TD>&nbsp;</TD></TR>
		<TR><TD colspan="2"><b><ct:FWLabel key="JSP_RF_PARAM_SOINS_DOMICILE" /></b></TD><TD><b><ct:FWLabel key="JSP_RF_PARAM_SOINS_PENSIONNAIRE" /></b></TD></TR>	
	<TR><TD>&nbsp;</TD></TR>	
	<TR>
			<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_POUR_TOUS" /></TD>
			<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPourTous" value="<%=new FWCurrency(viewBean.getMontantPlafondPourTous()).toStringFormat() %>"/></TD>
			<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PENSIONNAIRE_POUR_TOUS" /></TD>
			<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPensionnairePourTous" value="<%=new FWCurrency(viewBean.getMontantPlafondPensionnairePourTous()).toStringFormat() %>"/></TD>
	</TR>	
	<TR><TD>&nbsp;</TD></TR>                          
	<TR>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_PERSONNE_SEULE" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPersonneSeule" value="<%=new FWCurrency(viewBean.getMontantPlafondPersonneSeule()).toStringFormat() %>"/></TD>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PENSIONNAIRE_PLAFONNE_PERSONNE_SEULE" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPensionnairePersonneSeule" value="<%=new FWCurrency(viewBean.getMontantPlafondPensionnairePersonneSeule()).toStringFormat() %>"/></TD>
	</TR>	
	<TR><TD>&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_COUPLE_DOMICILE" /></TD>
		<TD><input style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondCoupleDomicile" value="<%=new FWCurrency(viewBean.getMontantPlafondCoupleDomicile()).toStringFormat() %>"/></TD>
			<TD colspan="2">&nbsp;</TD>
	</TR>		
	<TR><TD>&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_COUPLE_AVEC_ENFANTS" /></TD>
		<TD><input style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondCoupleEnfants" value="<%=new FWCurrency(viewBean.getMontantPlafondCoupleEnfants()).toStringFormat() %>"/></TD>
			<TD colspan="2">&nbsp;</TD>
	</TR>		
	<TR><TD>&nbsp;</TD></TR>
	<TR>	
		<TD colspan="2">&nbsp;</TD>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PENSIONNAIRE_PLAFONNE_SEPARE_HOME" /></TD>
		<TD><input style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPensionnaireSepareHome" value="<%=new FWCurrency(viewBean.getMontantPlafondPensionnaireSepareHome()).toStringFormat() %>"/></TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_SEPARE_DOMICILE" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondSepareDomicile" value="<%=new FWCurrency(viewBean.getMontantPlafondSepareDomicile()).toStringFormat() %>"/></TD>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PENSIONNAIRE_PLAFONNE_SEPARE_DOMICILE" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPensionnaireSepareDomicile" value="<%=new FWCurrency(viewBean.getMontantPlafondPensionnaireSepareDomicile()).toStringFormat() %>"/></TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>	
	<TR>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_ENFANTS_SEPARES" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondEnfantsSepares" value="<%=new FWCurrency(viewBean.getMontantPlafondEnfantsSepares()).toStringFormat() %>"/></TD>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PENSIONNAIRE_PLAFONNE_ENFANTS_SEPARES" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPensionnaireEnfantsSepares" value="<%=new FWCurrency(viewBean.getMontantPlafondPensionnaireEnfantsSepares()).toStringFormat() %>"/></TD>
	</TR>	
	<TR><TD>&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_ADULTE_ENFANTS" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondAdulteEnfants" value="<%=new FWCurrency(viewBean.getMontantPlafondAdulteEnfants()).toStringFormat() %>"/></TD>
			<TD colspan="2">&nbsp;</TD>
	</TR>
	<TR><TD>&nbsp;</TD></TR>
	<TR>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PLAFONNE_ENFANTS_ENFANTS" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondEnfantsEnfants" value="<%=new FWCurrency(viewBean.getMontantPlafondEnfantsEnfants()).toStringFormat() %>"/></TD>
		<TD><ct:FWLabel key="JSP_RF_PARAM_SOINS_MNT_PENSIONNAIRE_PLAFONNE_ENFANTS_ENFANTS" /></TD>
		<TD><input  style="text-align:right;width:100px;" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" type="text" name="montantPlafondPensionnaireEnfantsEnfants" value="<%=new FWCurrency(viewBean.getMontantPlafondPensionnaireEnfantsEnfants()).toStringFormat() %>"/></TD>
	</TR>	
	<TR><TD>&nbsp;</TD></TR>
	</TABLE>
	</TD></TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>