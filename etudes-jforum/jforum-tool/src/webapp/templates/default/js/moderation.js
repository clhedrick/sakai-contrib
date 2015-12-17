function verifyModerationCheckedTopics()
{
	var f = document.form1.topic_id;
	
	if (f == undefined)	 {
		return false;
	}
	
	if (f.length == undefined)	 {
		if (f.checked) {
			document.form1.markTopics.value = "false";
			return true;
		}
	}

	for (var i = 0; i < f.length; i++) {
		if (f[i].checked) {
			document.form1.markTopics.value = "false";
			return true;
		}
	}
	
	alert("${I18n.getMessage("Moderation.SelectTopics")}");
	return false;
}

function verifyCheckedTopicsAndSubmit()
{
	var f = document.form1.topic_id;
	
	if (f == undefined)	 {
		return;
	}
	
	if (f.length == undefined)	 {
		if (f.checked) {
			document.form1.submit();
			return;
		}
	}

	for (var i = 0; i < f.length; i++) {
		if (f[i].checked) {
			document.form1.submit();
			return;
		}
	}
	
	alert("${I18n.getMessage("Moderation.SelectTopics")}");
}

function validateModerationDelete()
{
	if (verifyModerationCheckedTopics() && confirm("${I18n.getMessage("Moderation.ConfirmDelete")}")) 
	{
		return true;
	}
	
	return false;
}

function topicDelete()
{
	if (validateModerationDelete())
	{
		document.form1.actionMode.value = "topicRemove";
		document.form1.submit();
	}
}

function topicModeration(modAction)
{
	if (verifyModerationCheckedTopics())
	{
		document.form1.actionMode.value = modAction;
		document.form1.submit();
	}
}