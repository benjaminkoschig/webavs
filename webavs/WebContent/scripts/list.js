<!--hide this script from non-javascript-enabled browsers

function exist(listSource, listDest) {
  for(i=0; i < listDest.length; i++)
    if (listDest.options[i].value == listSource.options[listSource.selectedIndex].value)
      return true;

  return false;    
}

function addElement(listSource, listDest) {
  indexDest = listDest.length - 1;

  if (listSource.selectedIndex != -1 && !exist(listSource, listDest)) {
    listDest.options[indexDest + 1] = new Option(listDest.options[indexDest].text, listDest.options[indexDest].value);
    listDest.options[indexDest] = new Option(listSource.options[listSource.selectedIndex].text, listSource.options[listSource.selectedIndex].value);
  }
}

function removeSelectedElements(list) {
  for(i=list.length - 2; i >= 0; i--)
    if (list.options[i].selected)
      list.options[i] = null;
}

function removeElements(list) {
  for(i=list.length - 2; i >= 0; i--)
      list.options[i] = null;
}

// stop hiding -->
