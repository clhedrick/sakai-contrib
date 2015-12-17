function navtoedit(navto, navid)
{
	try
	{
		var ele = document.getElementById("navid");
			
		if (ele == undefined)
		{
			defaultnav(navto);
			return true;
		}
		
		ele.value = navid;	
		return savechanges(navto);
	}
	catch(err)
	{
		//alert(err.description);
		defaultnav(navto);
		return true;
	}
	finally
	{	
	}
}

function savechanges(navto)
{
	try
	{
		var ele = document.getElementById("autosavenav");
		
		if (ele == undefined)
		{
			defaultnav(navto);
			return true;
		}
		
		// for browser back button fix
		if (ele.value == "frmedit" || ele.value == "frmreorder")
		{
			ele.value = "forumlist";
		}
		
		// for browser back button fix
		if (ele.value == "catedit" || ele.value == "catreorder")
		{
			ele.value = "categorylist";
		}
		
		if (ele.value == "forumaddedit")
		{
			if (document.form.forum_name.value.replace(/^\s*|\s*$/g, "").length == 0)
			{
				defaultnav(navto);
				return true;
			}
			else
			{
				return validatecatgforumforms(ele, navto);
			}
		}
		else if (ele.value == "catgaddedit")
		{
			if (document.form.category_name.value.replace(/^\s*|\s*$/g, "").length == 0)
			{
				defaultnav(navto);
				return true;
			}
			else
			{
				return validatecatgforumforms(ele, navto);
			}
		}
		else if (ele.value == "forumlist")
		{
			if (validateDates())
			{
				var ele1 = document.getElementById("saveForums");
				
				ele.value = navto;
				
				if (ele1 != undefined)
				{
					ele1.click();
				}
				else
				{
					defaultnav(navto);
				}
	
				return true;
			}
			return false;
		}
		else if (ele.value == "categorylist")
		{
			if (validateDates())
			{
				var ele1 = document.getElementById("saveCategories");
				
				ele.value = navto;
				
				if (ele1 != undefined)
				{
					ele1.click();
				}
				else
				{
					defaultnav(navto);
				}
				
				return true;
			}
			return false;
		}
		else
		{
			defaultnav(navto);
			return true;
		}
	
		return false;
	}
	catch(err)
	{
		//alert(err.description);
		defaultnav(navto);
		return true;
	}
	finally
	{	
	}
}

function defaultnav(navto)
{
	if (navto == 'forums')
	{
		document.location = "${contextPath}/adminForums/list${extension}";
	}
	else if (navto == 'foruminsert')
	{
		document.location = "${contextPath}/${moduleName}/insert${extension}";
	}
	else if (navto == 'categories')
	{
		document.location = "${contextPath}/adminCategories/list${extension}";
	}
	else if (navto == 'discussionlist')
	{
		document.location = "${contextPath}/forums/list${extension}";
	}
	else if (navto == 'importexport')
	{
		document.location = "${contextPath}/adminImportExport/list${extension}";
	}
	else
	{
		document.location = "${contextPath}/adminForums/list${extension}";
	}
}

function validatecatgforumforms(ele, navto)
{
	if (checkInput())
	{
		ele.value = navto;
		var ele1 = document.getElementById("submit");
		
		if (ele1 != undefined)
		{
			ele1.click();
		}
		else
		{
			defaultnav(navto);
		}
		
		return true;
	}
	return false;
}

function forumListSave()
{
	if (validateDates())
	{
		document.form.actionMode.value = "saveForums";
		document.form.submit();
	}
}

function forumDelete()
{
	if (validateDates())
	{
		document.form.actionMode.value = "deleteForums";
		document.form.submit();
	}
}

function categoryListSave()
{
	if (validateDates())
	{
		document.form.actionMode.value = "saveCategories";
		document.form.submit();
	}
}

function categoryDelete()
{
	if (validateDates())
	{
		document.form.actionMode.value = "deleteCategories";
		document.form.submit();
	}
}

function validateDates()
{
	var form = document.form;
	
	var eleTxt = document.getElementsByTagName("input");

 	for (i=0; i<eleTxt.length; i++) {
 		if (eleTxt[i].type == "text" && eleTxt[i].value.replace(/^\s*|\s*$/g, "").length > 0) 
 	 	{
 	 	 	var sdate = eleTxt[i];
 	 	 	if ((sdate.id).indexOf("startdate_") != -1)
 	 	 	{
				if (!compareDates(sdate))
				{
	 				eleTxt[i].focus();
					return false;
				}
 	 	 	}	
 		}
 	}
 	return true;
}


function compareDates(sdate)
{
	var sdatearr = (sdate.id).split("_");

	if (sdatearr.length != 2)
	{
		return false;
	}
	
	var edate = document.getElementById("enddate_"+ sdatearr[1]);

	var audate = document.getElementById("allowuntildate_"+ sdatearr[1]);
		
	var blnStartDate = true, blnEndDate = true, blnAllowUntilDate = true;
	
	if (sdate.length == 0) 
 	{
		blnStartDate = false;
 	}
	
 	if (edate.length == 0) 
 	{
 		blnEndDate = false;
 	}
 	
 	if (audate.length == 0) 
 	{
 		blnAllowUntilDate = false;
 	}
 	
 	if (blnStartDate && blnEndDate && blnAllowUntilDate)
 	{
	 	if ((Date.parse(sdate.value) > Date.parse(edate.value)) || (Date.parse(sdate.value) > Date.parse(audate.value))
	 				|| (Date.parse(edate.value) > Date.parse(audate.value))) 
	 	{
	   		alert("${I18n.getMessage("Forums.Form.DateError")}");
	   		return false;
	 	}
 	}
 	else if (blnStartDate && blnEndDate && !blnAllowUntilDate)
	{
 		if ((Date.parse(sdate.value) > Date.parse(edate.value))) 
	 	{
	   		alert("${I18n.getMessage("Forums.Form.DateError")}");
	   		return false;
	 	}
	}
 	else if (!blnStartDate && blnEndDate && blnAllowUntilDate)
	{
 		if (Date.parse(edate.value) > Date.parse(audate.value)) 
	 	{
	   		alert("${I18n.getMessage("Forums.Form.DateError")}");
	   		return false;
	 	}
	}
 	else if (blnStartDate && !blnEndDate && blnAllowUntilDate)
 	{
	 	if (Date.parse(sdate.value) > Date.parse(audate.value)) 
	 	{
	   		alert("${I18n.getMessage("Forums.Form.DateError")}");
	   		return false;
	 	}
 	}

 	return true;
}