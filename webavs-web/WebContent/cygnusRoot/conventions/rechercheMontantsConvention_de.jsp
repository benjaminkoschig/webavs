<%-- tpl:insert page="/theme/capage.jtpl" --%>
<%@ page language="java"
	errorPage="/errorPage.jsp" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.conventions.RFConventionViewBean"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
	idEcran="PRF0023";
	RFConventionViewBean viewBean = (RFConventionViewBean) session.getAttribute("viewBean");
	autoShowErrorPopup = true;
	bButtonCancel = false;
%>
<%@ include file="/theme/detail/javascripts.jspf"%>
<%-- tpl:put name="zoneScripts" --%>
<script language="JavaScript">

  servlet = "<%=(servletContext + mainServletPath)%>";

  function init(){	  	 
	  	  
	  <%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>	 	  		  	
	  	// mise a jour de la liste du parent
		 if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		 }
	  <%}%>
  }
  
  function add() {	 
	  
	    document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.ajouter";  

  }

  function upd() {	  
  }

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.ajouter";
    else
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.afficher";
  }

  function del() {
    if (window.confirm("<ct:FWLabel key='WARNING_RF_CONV_SUPPRESSION_OBJET'/>")){
        document.forms[0].elements('userAction').value="<%=IRFActions.ACTION_RECHERCHE_MONTANTS_CONVENTION%>.supprimer";
        document.forms[0].target = "fr_main";
        document.forms[0].submit();
    }
  }
  	
  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly && 
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }


   // Attention il faut arriver vers l'écran suivant en mode ajout
   function versEcranSuivant() {		

	  	if((document.forms[0].elements('dateDebut').value.length>0 ||
	  		document.forms[0].elements('dateFin').value.length>0 ||
	  		(document.forms[0].elements('mntMaxDefaut').value.length>0 && document.forms[0].elements('mntMaxDefaut').value !="0.00")||
	  		(document.forms[0].elements('mntMaxAvecApiGrave').value.length>0 && document.forms[0].elements('mntMaxDefaut').value !="0.00") ||
	  		(document.forms[0].elements('mntMaxAvecApiMoyen').value.length>0 && document.forms[0].elements('mntMaxDefaut').value !="0.00") ||
	  		(document.forms[0].elements('csTypeBeneficiaire').value.length>0 && document.forms[0].elements('mntMaxDefaut').value !="0.00") ||
	  		(document.forms[0].elements('mntMaxAvecApiFaible').value.length>0 && document.forms[0].elements('mntMaxDefaut').value !="0.00")) 
	  	){
			warningObj.text="<ct:FWLabel key='JSP_MODIF_PERDUES_WARN'/>";			
			showWarnings();
		}
	  	
		var params = $.param({
			"userAction":"<%=IRFActions.ACTION_SAISIE_SOIN_FOURNISSEUR_CONVENTION%>.afficher",
			"_method":"add",
			"libelle":"<%=viewBean.getLibelle()%>"			
		});
	
		top.fr_main.location.href = servlet + "?"+params;
  }

  function arret() {
	  	var url = servlet + "?userAction=";
	  	url += "<%=IRFActions.ACTION_CONVENTION%>.chercher";
		top.fr_main.location.href = url;
  }


	var warningObj = new Object();
	warningObj.text = "";

	function showWarnings() {		
		if (warningObj.text != "") {
			showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
		}
	}
	
	function clearFields(){
	    $('[name=dateDebut]').val("");
	    $('[name=dateFin]').val("");
	    $('[name=csGenrePCAccordee]').val(""); 
	    $('[name=csTypePCAccordee]').val("");
	    $('[name=csPeriodicite]').val("");
	    $('[name=csTypeBeneficiaire]').val("");
	    $('[name=mntMaxDefaut]').val("0.00");
	    $('[name=mntMaxAvecApiGrave]').val("0.00");
	    $('[name=mntMaxAvecApiMoyen]').val("0.00");
	    $('[name=mntMaxAvecApiFaible]').val("0.00");
	    $('[name=mntMaxSansApi]').val("0.00");
	}
  
  function postInit(){	  
	  
	  <%if(!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
	  	if("add" == document.forms[0].elements('_method').value)
		  clearFields();
	  <%}%>	
  }

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_PERIODE" /></TD>
	<TD><span><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_DU" /></span><input data-g-calendar=" "  name="dateDebut" value="<%=viewBean.getDateDebut()%>"/></TD>
	<TD><span><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_AU" /></span><input data-g-calendar=" "  name="dateFin" value="<%=viewBean.getDateFin()%>"/></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_BENEFICIAIRE" /></TD>
	<TD colspan="5"><ct:FWListSelectTag data="<%=viewBean.getCsTypeBeneficiairePCData()%>" name="csTypeBeneficiaire" defaut="<%=viewBean.getCsTypeBeneficiaire()%>" /></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_PAR_DEFAUT" /></TD>
	<TD><INPUT style="text-align:right;width:100px;" type="text" name="mntMaxDefaut" value="<%=new FWCurrency(viewBean.getMntMaxDefaut()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"/></TD>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_PLAFONNEE" /></TD>
	<TD><INPUT type="checkbox" name="plafonne" <%=viewBean.getPlafonne().booleanValue()?"CHECKED":""%>/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_SANS_API" /></TD>
	<TD><INPUT style="text-align:right;width:100px;" type="text" name="mntMaxSansApi" value="<%=new FWCurrency(viewBean.getMntMaxSansApi()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"/></TD>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_AVEC_API_GRAVE" /></TD>
	<TD><INPUT style="text-align:right;width:100px;" type="text" name="mntMaxAvecApiGrave" value="<%=new FWCurrency(viewBean.getMntMaxAvecApiGrave()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"/></TD>
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_AVEC_API_MOYENNE" /></TD>
	<TD><INPUT style="text-align:right;width:100px;" type="text" name="mntMaxAvecApiMoyen" value="<%=new FWCurrency(viewBean.getMntMaxAvecApiMoyen()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);"/></TD>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_MNT_AVEC_API_LEGERE" /></TD>
	<TD><INPUT style="text-align:right;width:100px;" type="text" name="mntMaxAvecApiFaible" value="<%=new FWCurrency(viewBean.getMntMaxAvecApiFaible()).toStringFormat()%>" class="montant" onchange="validateFloatNumber(this);" onkeypress="return filterCharForFloat(window.event);" /></TD>
</TR>
<TR><TD colspan="6">&nbsp;</TD></TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_PERIODICITE" /></TD>
	<TD>
		<ct:FWListSelectTag data="<%=viewBean.getCsPeriodiciteData()%>" name="csPeriodicite" defaut="<%=\"add\".equals(request.getParameter(\"_method\"))?\"\":viewBean.getCsPeriodicite()%>" />
	</TD>
	<TD><ct:FWLabel key="JSP_RF_QD_S_GENRE_PC_ACCORDEE"/></TD>
	<TD>
		<ct:FWListSelectTag name="csGenrePCAccordee" data="<%=viewBean.getGenresPCAccordeeData()%>" 
			defaut="<%=viewBean.getCsGenrePCAccordee()%>"/>
	</TD>
	<TD><ct:FWLabel key="JSP_RF_QD_S_TYPE_PC_ACCORDEE"/></TD>
	<TD>
		<ct:FWListSelectTag name="csTypePCAccordee" data="<%=viewBean.getTypesPcData()%>" 
			defaut="<%=viewBean.getCsTypePCAccordee()%>"/>
	</TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<INPUT type="button" value="<ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_ARRET"/> (alt+<ct:FWLabel key="AK_REQ_ARRET"/>)" onclick="arret()" accesskey="<ct:FWLabel key="AK_REQ_ARRET"/>">
<INPUT type="button" value="<ct:FWLabel key="JSP_RF_SAISIE_MNT_CONV_SUIVANT"/> (alt+<ct:FWLabel key="AK_REQ_SUIVANT"/>)" onclick="versEcranSuivant()" accesskey="<ct:FWLabel key="AK_REQ_SUIVANT"/>">
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>
<%-- /tpl:insert --%>