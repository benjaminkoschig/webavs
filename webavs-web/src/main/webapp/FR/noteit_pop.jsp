<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="globaz.framework.util.FWTextFormatter"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.framework.bean.FWViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%><html>
<head>
<%@ page language="java" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="GENERATOR" content="IBM WebSphere Studio" />
<title>NOTE</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css"/>


<%String servletContext = request.getContextPath();
String mainServletPath = (String) request.getAttribute("mainServletPath");
//String selectedIdValue = "";
//String userActionValue = "";
//int tableHeight = 243;
//String subTableWidth = "100%";
//String theMethod = request.getParameter("_method");
//boolean isMethodAdd = (new String("ADD")).equalsIgnoreCase(theMethod);
if (mainServletPath == null) {
	mainServletPath = "";
}
//String formAction = servletContext + mainServletPath;
//System.out.println("Form action de " + request.getServletPath() + formAction);
%>

<%globaz.framework.db.postit.FWNotePManager viewBeanList =
	(globaz.framework.db.postit.FWNotePManager) request.getAttribute(globaz.framework.servlets.FWActionNoteIt.VB_NAME);
globaz.framework.db.postit.FWNoteP bean;
int size = viewBeanList.size();
%>
<%if(!viewBeanList.hasErrors() && !FWViewBeanInterface.ERROR.equals(viewBeanList.getMsgType())){%>
<!-- Ajax JavaScript -->
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/JDate.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/interface/Note.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=request.getContextPath()%>/dwr/util.js'></script>
<script type="text/javascript" language="JavaScript">
var sourceId = <%= viewBeanList.getForSourceId() %>;
var tableSource = '<%= viewBeanList.getForTableSource() %>';
var noteBean = {dateCreation:null,user:null,description:null,memo:null,noteid:null,sourceId:'<%= viewBeanList.getForSourceId() %>',tableSource:'<%= viewBeanList.getForTableSource() %>'};
var emptyBean = {dateCreation:null,user:null,description:null,memo:null,noteid:null,sourceId:'<%= viewBeanList.getForSourceId() %>',tableSource:'<%= viewBeanList.getForTableSource() %>'};
<% 
java.util.Iterator it = viewBeanList.iterator(); 
StringBuffer sb = new StringBuffer();
while(it.hasNext()) {
	bean = (globaz.framework.db.postit.FWNoteP) it.next();	
	if(sb.length() > 0) {
		sb.append(",");
	}
	sb.append(bean.getNoteid());
}
%>
var listId = new Array(<%= sb.toString() %>);
var errorObj = new Object();
errorObj.text = "";

// Set any Ajax Java Error to the showErrors function
DWREngine.setErrorHandler(showErrors);
if (window.addEventListener) {
  window.addEventListener("load", init, false);
}
else if (window.attachEvent) {
  window.attachEvent("onload", init);
}
else {
  window.onload = init;
}

/**
* Init()
**/
function init() {
  DWRUtil.useLoadingMessage();
  updateUsers();  
  updateDates();
}
/**
* Update de list of user in the combo box cbUser
* do a CallBack 
**/
function updateUsers() {
	Note.getList(emptyBean, 'User', _createUserList);
}
/**
* Update de list of date in the combo box cbUser
* do a CallBack 
**/
function updateDates() {
	Note.getList(emptyBean, 'Date', _createDateList);
}
/**
* Private function : Ajax Callback function to complete the list of User
* @param data - an array of users
**/
function _createUserList(data) {
	DWRUtil.removeAllOptions("cbUser");
	data = data.sort();
	DWRUtil.addOptions("cbUser", data);
}
/**
* Private function : Ajax Callback function to complete the list of date
* @param data - an array of date
**/
function _createDateList(data) {
	DWRUtil.removeAllOptions("cbDate");
	data = data.sort();	
	DWRUtil.addOptions("cbDate", data);
}

/**
* Show or hide the Memo of a specific Note
* do a callback if the memo was not already downloaded
* @param aId - NoteId from where the call came
**/
function showMemo(aId) {
	var memoTag = $("memo_" + aId);

	if($("tblMem_"+aId).style.display == '') {
		$("tblMem_"+aId).style.display = 'none';
		return;
	}
	if($("memo_"+aId).innerHTML != '') {
		$("tblMem_"+aId).style.display = '';
		return;
	}else {
		_fillMemo(aId);
	}
}
/**
* Ajax Callback function to start a Edit Note process
* @param aId - noteId
**/
function doEditNote(aId) {
	Note.getNote(aId, _editingNote);	
}
/**
* Private function : Ajax Callback function to set the values in the edit view
* @param note - a FWNoteBean object
**/
function _editingNote(note) {
	DWRUtil.setValues(emptyBean);
	DWRUtil.setValues(note);
	$("editNote").style.display = '';
}
/**
* Display the editBox to create a new Note
**/
function doNewNote() {
	DWRUtil.setValues(emptyBean);
	$("editNote").style.display = '';
}
/**
* Ask to delete a Note from a nodeId
* and do a Ajax callBack
* @param aId - the noteId
**/
function doDeletingNote(aId, name) {
	if (window.confirm("Veuillez confirmer la suppression de la note.\n" + name))
	{
		Note.remove(aId, _deletedNote);
	}		
}
/**
* Private function : Ajax Callback function to hidde the removed Memo received from the server
* @param aId - NoteId from where the call came
**/
function _deletedNote(aId) {
	if(aId != null){ //It worked -> Then hide the note
		var tblMemo = $("tbl_" + aId);
		tblMemo.style.display = 'none';
		tblMemo.id = "removed_"+aId; // Change name so he cannot be found again by mistake
	}
}
/**
* Save the current note edited and send it back by Ajax CallBack
**/
function saving() {
	//var object = objectEval(noteBean);	
	DWRUtil.getValues(noteBean);
	Note.save(noteBean,_saved);
}
/**
* Private function : The current note have been suceffuly saved
* We need now update the Note values or add a new Notes on the list of notes
* @param note - a Java FWNoteBean
**/
function _saved(note) {
	//First check if the note already exist
	var aId = note.noteid;
	var tblMemo = $("memo_" + aId);
	
	if(tblMemo == null) { //It's a new Note
		//First row
		var cellFuncs = [
			function(data) {
				return "<img border=\"0\" src=\"images/edit.gif\" alt=\"edit\" width=\"12\" height=\"12\" onclick=\"doEditNote(" + data.noteid + ");\" style=\"cursor:pointer\"/>"
				 + " <img border=\"0\" src=\"images/delete.gif\" alt=\"effacer\" width=\"12\" height=\"12\" onclick=\"doDeletingNote(" + data.noteid + ", '"+ data.description + "');\" style=\"cursor:pointer\"/>";			
			},
			function(data) {
				return "<span id=\"date_" + data.noteid + "\">"+ data.dateCreation+"</span>";
			},
			function(data) {
				return "<span id=\"user_" + data.noteid + "\">"+ data.user+"</span>";
			},
			function(data) {
				return "<span id=\"descr_" + data.noteid + "\" onclick=\"showMemo(" + data.noteid + ");\" style=\"cursor:pointer;font-weight:bold\">"+ data.description+"</span>";					
			}];
		
		DWRUtil.addRows("tblBody",[note], cellFuncs, {
			rowCreator:function(options) {
		    	var row = document.createElement("tr");
				row.id = "tbl_" + aId;
				row.style.verticalAlign = "top";
		    	return row;
		  	},
		  	cellCreator:function(options) {
    			var td = document.createElement("td");
    			return td;
  			}
		});
		
		//Second Row
		cellFuncs = [
			function(data) {
				return "<div id=\"memo_" + data.noteid + "\" style=\"background-color: white;\">"+data.memo+"</div>";
			}];
		DWRUtil.addRows("tblBody",[note], cellFuncs, {
			rowCreator:function(options) {
		    	var row = document.createElement("tr");
				row.id = "tblMem_" + aId;
				row.style.display = "none";
		    	return row;
		  	},
		  	cellCreator:function(options) {
    			var td = document.createElement("td");
//    			td.setAttribute('colspan','4');
    			td.colSpan = 4;
    			return td;
  			}
		});
		var newList = new Array(listId.length + 1);
		newList[0] = aId;
		for(i=1, len = newList.length;i < len; i++) {
			newList[i] = listId[i-1];
		}
			
		listId = newList;
					
		updateDates();
		updateUsers();			
	}else {
		DWRUtil.setValue("descr_" + aId, note.description);
		DWRUtil.setValue("date_" + aId, note.dateCreation);
		DWRUtil.setValue("user_" + aId, note.user);
		//DWRUtil.setValue("memo_" + aId, note.memo);
		// force le rechargement du memo (détail du message), afin de remplacer correctement les \n
		DWRUtil.setValue("memo_" + aId, ""); 
	}
	ele = $("noValue");
	if(ele != null) {
		ele.style.display = 'none';
	}
	$("editNote").style.display = 'none';
	showMemo(aId);
}
/**
* Cancel an edit or new Note
**/
function cancel() {
	$("editNote").style.display = 'none'; 
	$("mainForm").reset();
}
/**
* Private function : Do the Ajax callback to get the Memo from a particular NoteId
* @param aId - NoteId from where the call came
**/
function _fillMemo(aId) {	
	var dataFromBrowser = aId;
	Note.getText(aId, {
	  	callback:function(dataFromServer) {
   			_loadInfo(dataFromServer, dataFromBrowser);}
   			, timeout:5000   			 
	});
}
/**
* Private function : Ajax Callback function to display the Memo received from the server
* @param data - Memo text got from the server
* @param aId - NoteId from where the call came
**/
function _loadInfo(data, aId) {
	var tagId = "memo_" + aId;
	DWRUtil.setValue(tagId, data);
	showMemo(aId);
	return;
}
/**
* Call the filter of a particular columns with the specified data
* @param thisObj - the ComboBox instance who call this event
* @param thisEvent - the refer event generated
**/
function filtre(thisObj, thisEvent) {
//use 'thisObj' to refer directly to this component instead of keyword 'this'
//use 'thisEvent' to refer to the event generated instead of keyword 'event'
	if($('cbDate').value == '' && $('cbUser').value == '') { //Selection tous
		_showHideAll('');
		return;
	}else {
		note = emptyBean;
		note.user = $('cbUser').value;
		note.dateCreation = $('cbDate').value;
		Note.filtre(note, _doFilter);
	}
}

/**
* Private function : Show or Hide all the Note
* @param value - '' or 'none' -> display value
**/
function _showHideAll(value) {
		for(i=0;i < listId.length; i++) {
			ele1 = $("tbl_"+listId[i]);
			if(ele1 != null) {			
				ele1.style.display = value;
			}
			ele2 = $("tblMem_"+listId[i]);
			if(ele2 != null) {
				ele2.style.display = value;
			}
		}
}

/**
* Private function : Ajax Callback function to filter the noteId to show
* @param data - Array of int -> NoteId
**/
function _doFilter(data) {
	_showHideAll('none');
	
	for(i=0;i < data.length;i++) {
		$("tbl_"+data[i]).style.display = '';
	}
}

function errorAlert(msg) {
	alert('Error : ' + msg);
}
/**
* Private function : that handle any Ajax Error and get back the message to the user
* @param msg - Java Exception mesage
**/
function showErrors(msg) {
	if (msg != "") {
		errorAlert(msg);
	}
}
function objectEval(text)
{
    // eval() breaks when we use it to get an object using the { a:42, b:'x' }
    // syntax because it thinks that { and } surround a block and not an object
    // So we wrap it in an array and extract the first element to get around
    // this.
    // The regex = [start of line][whitespace]{[stuff]}[whitespace][end of line]
    text = text.replace(/\n/g, " ");
    text = text.replace(/\r/g, " ");
    if (text.match(/^\s*\{.*\}\s*$/))
    {
        text = "[" + text + "]";
    }

    return eval(text)[0];
}
</script>
</head>
<body>
<div>
<%if(viewBeanList.getSession().hasRight("framework.noteit", FWSecureConstants.ADD)){%>
	<button class="btnCtrl" name="btnNew" id="btnNew" onclick="doNewNote()">Nouveau</button>
<%} else  {%>&nbsp;<%} %>		
</div>
<div id="editNote" style="display:none;background-color:#B3C4DB;" align="left">
	<form id="mainForm">
		Description : <input size="40" maxlength="255" type="text" id="description"/><br/>
		Note : <br/>
		<textarea rows="14" cols="40" id="memo"></textarea>
		<br/>
		<button name="btnCancel" id="btnCancel" onclick="cancel();" type="button">Annuler</button>
		<%if(viewBeanList.getSession().hasRight("framework.noteit", FWSecureConstants.UPDATE)){%>	
		<button name="btnSave" id="btnSave" onclick="saving()" type="button">Enregistrer</button>
		<%}%>
		<input type="hidden" name="noteid" value="" />
		<input type="hidden" name="sourceId" value="<%= viewBeanList.getForSourceId() %>" />
		<input type="hidden" name="tableSource" value="<%= viewBeanList.getForTableSource() %>" />
	</form>
</div>
<table id="tblNote" cellpadding="1" cellspacing="1" border="0" width="100%" style="background-color: #B3C4DB">
	<thead>
		<tr id="trTest" class="title">
			<th></th>
			<th>Date<br/><select id="cbDate" style="vertical-align:top;" name="cbDate" onchange="return filtre(this, event);"></select> </th>
			<th>Utilisateur<br/><select id="cbUser" style="vertical-align:top;" name="cbUser" onchange="return filtre(this, event);"></select></th>
			<th>Description</th>
		</tr>
	</thead>
	<tbody id="tblBody">
<%for (int i = 0; i < size; i++) {
	bean = (globaz.framework.db.postit.FWNoteP) viewBeanList.getEntity(i);%>
	<tr id="tbl_<%=bean.getNoteid()%>" valign="top" style="vertical-align:top">
			<td nowrap="nowrap">
				<%if(viewBeanList.getSession().hasRight("framework.noteit", FWSecureConstants.UPDATE)){%>		
					<img border="0" src="<%=servletContext%>/images/edit.gif" alt="edit" width="12" height="12" onclick="doEditNote(<%=bean.getNoteid() %>);" style="cursor:pointer"/>
				<%} %>
				<%if(viewBeanList.getSession().hasRight("framework.noteit", FWSecureConstants.REMOVE)){%>		
					<img border="0" src="<%=servletContext%>/images/delete.gif" width="12" height="12" onclick="doDeletingNote(<%=bean.getNoteid() %>,'<%=FWTextFormatter.slash(bean.getDescription(), '\'') %>');" style="cursor:pointer"/>
				<%}%>
				<%if(!viewBeanList.getSession().hasRight("framework.noteit", FWSecureConstants.UPDATE) && ! viewBeanList.getSession().hasRight("framework.noteit", FWSecureConstants.REMOVE)){%>
				&nbsp;
				<%} %>
			</td>
			<td><span id="date_<%=bean.getNoteid()%>"><%=bean.getDateCreation()%></span></td>
			<td><span id="user_<%=bean.getNoteid()%>"><%=bean.getUser()%></span></td>
			<td>
				<span id="descr_<%=bean.getNoteid()%>" onclick="showMemo(<%=bean.getNoteid()%>);" style="cursor:pointer;font-weight:bold"><%=bean.getDescription()%></span>				
			</td>			
	</tr>
	<tr id="tblMem_<%=bean.getNoteid()%>" style="display:none;">
		<td colspan="4"><div id="memo_<%=bean.getNoteid()%>" style="background-color:white; "></div></td>
	</tr>		
<% } //Fin For %>
<% if(size == 0) { %>
	<tr id="noValue" style="font-style:italic;font-weight:bold;"><td colspan="4">Aucune note disponible</td></tr>
<%} %>
	</tbody>		
</table>
<%} else { %>
</head>
<body>
	<div>Une erreur est survenue : <%=viewBeanList.getSession().hasErrors()?viewBeanList.getSession().getErrors().toString():viewBeanList.getMessage()%></div>
<%} %>
</body>
</html>
