<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.corvus.api.lots.IRELot"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%

	idEcran="PRF0051";

	RFLotViewBean viewBean = (RFLotViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdLot();	
	bButtonDelete = false;
	bButtonUpdate = true;

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.cygnus.vb.paiement.RFLotViewBean"%>	    

<SCRIPT language="javascript">

	function add(){
	    document.forms[0].elements('userAction').value="cygnus.paiement.lot.ajouter";
	}

	function upd(){
		
		 document.getElementsByName("idLot")[0].disabled=true;	   
		 document.getElementsByName("forCsEtat")[0].disabled=true;
		 document.getElementsByName("dateCreationLot")[0].disabled=true;
		 document.getElementsByName("dateEnvoiLot")[0].disabled=true;
		 document.getElementsByName("forCsType")[0].disabled=true;
	}

	function validate() {
	    state = validateFields();
	    if (document.forms[0].elements('_method').value == "add"){
	        document.forms[0].elements('userAction').value="cygnus.paiement.lot.ajouter";
	    }else{
	        document.forms[0].elements('userAction').value="cygnus.paiement.lot.modifier";
	    }
	    return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add"){
		  document.forms[0].elements('userAction').value="back";
		}else{
		  document.forms[0].elements('userAction').value="cygnus.paiement.lot.chercher";
		}
	}

	function del() {
	    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	        document.forms[0].elements('userAction').value="cygnus.paiement.lot.supprimer";
	        document.forms[0].submit();
	    }
	}

	function init(){
		

	}
	
	function postInit(){
	    if (document.forms[0].elements('_method').value == "add"){		
			action('add');
	    }
	}	
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RFM_LOT_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD><ct:FWLabel key="JSP_RF_S_ID_LOT" /></TD>
	<TD><INPUT type="text" name="idLot" value="<%=viewBean.getIdLot()%>" disabled="disabled" /></TD>
	<TD><ct:FWLabel key="JSP_RF_S_ETAT" /></TD>
	<TD><ct:FWCodeSelectTag name="forCsEtat" codeType="REETALOT" defaut="<%=viewBean.getCsEtatLot() %>" wantBlank="true"/></TD>  		
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_S_DATE_CREATION" /></TD>
	<TD><input data-g-calendar=" "  name="dateCreationLot" value="<%=viewBean.getDateCreationLot()%>"/></TD>
	<TD><ct:FWLabel key="JSP_RF_S_DATE_ENVOI" /></TD>
	<TD><input data-g-calendar=" "  name="dateEnvoiLot" value="<%=viewBean.getDateEnvoiLot()%>"/></TD>	
</TR>
<TR>
	<TD><ct:FWLabel key="JSP_RF_S_DESCRIPTION" /></TD>
	<TD><INPUT type="text" name="description" value="<%=viewBean.getDescription()%>"/></TD>
	<TD><ct:FWLabel key="JSP_RF_S_TYPE" /></TD>
	<TD><ct:FWCodeSelectTag name="forCsType" codeType="RETYPLOT" defaut="<%=viewBean.getCsTypeLot() %>" wantBlank="true"/></TD>	
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<ct:menuChange displayId="options" menuId="cygnus-optionslots">
<%if(IRELot.CS_TYP_LOT_MENSUEL.equals(viewBean.getCsTypeLot())){%>
	<ct:menuActivateNode active="no" nodeId="prestation"/>
<%} %>
<%if(IRELot.CS_TYP_LOT_DECISION.equals(viewBean.getCsTypeLot())){%>
	<ct:menuActivateNode active="no" nodeId="prestationAccordee"/>
<%} %>	
<%if(!IRELot.CS_ETAT_LOT_OUVERT.equals(viewBean.getCsEtatLot()) || IRELot.CS_TYP_LOT_MENSUEL.equals(viewBean.getCsTypeLot()) ){%>
	<ct:menuActivateNode active="no" nodeId="comptabiliser"/>
<%} %>	
</ct:menuChange>
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdLot()%>" menuId="cygnus-optionslots"/>
<ct:menuSetAllParams key="idLot" value="<%=viewBean.getIdLot()%>" menuId="cygnus-optionslots"/>
<ct:menuSetAllParams key="csTypeLot" value="<%=viewBean.getCsTypeLot()%>" menuId="cygnus-optionslots"/>
<ct:menuSetAllParams key="csEtatLot" value="<%=viewBean.getCsEtatLot()%>" menuId="cygnus-optionslots"/>
<ct:menuSetAllParams key="dateCreationLot" value="<%=viewBean.getDateCreationLot()%>" menuId="cygnus-optionslots"/>
<ct:menuSetAllParams key="dateEnvoiLot" value="<%=viewBean.getDateEnvoiLot()%>" menuId="cygnus-optionslots"/>
<ct:menuSetAllParams key="description" value="<%=viewBean.getDescription()%>" menuId="cygnus-optionslots"/>

<SCRIPT language="javascript">
reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
</SCRIPT>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>