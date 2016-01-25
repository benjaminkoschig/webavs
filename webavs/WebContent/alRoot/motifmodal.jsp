<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>

<script>
  function modif(){

   
   
    	if (dialogArguments.elemWindow.document.getElementsByName(dialogArguments.elemMotifCode)[0] !=null) {
	    dialogArguments.elemWindow.document.getElementsByName(dialogArguments.elemMotifCode)[0].value = document.getElementById("motif").value;
    	} else {
		 alert("Element not found : "+dialogArguments.elemMotifCode);
    	}
    	if (dialogArguments.elemWindow.document.getElementsByName(dialogArguments.elemDate)[0]!=null) {
    	    dialogArguments.elemWindow.document.getElementsByName(dialogArguments.elemDate)[0].value = document.getElementById("date").value;
    	} else {
		 alert("Element not found : "+dialogArguments.elemDate);
    	}
   }
   function validate () {

	 state = validateFields();
	 return state;
   }
</script>
</head>
<BODY bgColor=#B3C4DB>
<table cellpadding="10" width="100%">
<tr>
  <td  align="left">
    <script>
      document.open();
      document.write(dialogArguments.libelle);
      document.close();
    </script>
  </td>
</tr>
<tr>
  <td  align="center">
    <ct:FWCodeSelectTag name="motif"
                            defaut="506001"
                            codeType="PYMOTIFHIS"/>

    <ct:FWCalendarTag name="date"
                    value='<%=globaz.globall.util.JACalendar.today().toStr(".")%>'
                    doClientValidation="CALENDAR,NOT_EMPTY"/>

  </td>
</tr>
<tr>
  <td  align ="right">
    <input type='button' onclick="if (validate()){modif();returnValue=true;window.close();}" value=" Ok ">
    <input type='button' onclick="returnValue=false;window.close();" value=" Cancel ">
  </td>
</tr>
</table>
</BODY>
</HTML>
