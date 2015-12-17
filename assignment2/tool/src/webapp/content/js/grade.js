/**
 * This file includes javascript for the "View Student's Submission" screen where grading takes place
 */

var asnn2gradeview = asnn2gradeview || {};

/**
 * Handles the rendering of the "Override Assignment-Level Settings" options
 */
asnn2gradeview.override_submission_settings = function() {
    override_checkbox = jQuery("input:checkbox[name='page-replace\:\:override_settings']").get(0);

    if (override_checkbox) {
        if (override_checkbox.checked) {
            // change text back to normal
            jQuery("#override_settings_container").removeClass("inactive");

            // enable all of the form elements
            jQuery("select[name='page-replace\:\:resubmission_additional-selection']").removeAttr("disabled");
            jQuery("input:checkbox[name='page-replace\:\:require_accept_until']").removeAttr("disabled");
            
            // display the editable date/time entry
            jQuery('#resubmission_due_section').show();
            jQuery('#resubmission_due_read_only').hide();
        } else {
            // gray out the text
            jQuery("#override_settings_container").addClass("inactive");

            // disable all form elements
            jQuery("select[name='page-replace\:\:resubmission_additional-selection']").attr("disabled", "disabled");
            jQuery("input:checkbox[name='page-replace\:\:require_accept_until']").attr("disabled", "disabled");
            
            // if due date was extended, we replace the editable date inputs with read-only form. The date widget
            // gets cranky if we try to disable it and throws validation errors upon form submission
            var dueDateExtended = jQuery("input:checkbox[name='page-replace\:\:require_accept_until']");
            if (dueDateExtended.get(0) && dueDateExtended.get(0).checked) {
                jQuery('#resubmission_due_section').hide();
                jQuery('#resubmission_due_read_only').show();
                var readOnlyText = jQuery("#resubmission_due_read_only");
                dateText = jQuery("input[name='page-replace\:\:accept_until\:1\:date-field']");
                timeText = jQuery("input[name='page-replace\:\:accept_until\:1\:time-field']");
                readOnlyText.text(dateText.val() + " " + timeText.val());
            }
        }
    }
}

asnn2gradeview.set_accept_until_on_submission_level = function() {
    el = jQuery("input:checkbox[name='page-replace\:\:require_accept_until']").get(0);
    if (el) { 
        if (el.checked) {
            jQuery("#accept_until_container").show();
        } else {
            jQuery("#accept_until_container").hide();
        }
    }
}

/**
 * Save feedback when user hits Next/Previous/Return to List when grading
 */
asnn2gradeview.saveFeedback = function(direction, contUrl) {
	var formSubmit = document.getElementById("page-replace::submit");
    var option = document.getElementById("page-replace::submitOption");
    option.value="INSTRUCTOR_FEEDBACK_SUBMIT_" + direction;
    formSubmit.click();
}

/**
 * Save/Release/Clear grading information for submission and navigate
 *
 * This function uses the asnn2 dialog utility.
 */
asnn2gradeview.saveGradingDialog = function(direction, contUrl) {
    var saveGradingDialog = jQuery('#save-grading-dialog');
    var saveButton = jQuery('#page-replace\\:\\:save-grading-save').click(function(event) {
        asnn2util.closeDialog(saveGradingDialog);
        var formSubmit = document.getElementById("page-replace::submit");
        var option = document.getElementById("page-replace::submitOption");
        option.value="INSTRUCTOR_FEEDBACK_SUBMIT_" + direction;
        formSubmit.click();
    });
    
    var saveButton = jQuery('#page-replace\\:\\:save-grading-saveAndRelease').click(function(event) {
        asnn2util.closeDialog(saveGradingDialog);
        var formSubmit = document.getElementById("page-replace::submit");
        var option = document.getElementById("page-replace::submitOption");
        option.value="INSTRUCTOR_FEEDBACK_RELEASE_" + direction;
        formSubmit.click();
    });

    var clearButton = jQuery('#page-replace\\:\\:save-grading-clear').click(function(event) {
        asnn2util.closeDialog(saveGradingDialog);
        window.location = contUrl;
    });
    
    var cancelButton = jQuery('#page-replace\\:\\:save-grading-cancel').click(function(event) {
        asnn2util.closeDialog(saveGradingDialog);
    });

    asnn2util.openDialog(saveGradingDialog);
    return false;
};

asnn2gradeview.isGradingChanged = function()
{
    var gradingChanged = false;
    
    /*
     * This for loop forces all our textareas to sync with the fck editors on 
     * the page. This was taken from fckeditorapi.js - _FormSubmit with minor
     * modifications.
     */

    //Check if there are dirty FCKeditor instances
    if (typeof FCKeditorAPI != "undefined") {
        for ( var name in FCKeditorAPI.Instances ) {
            var oEditor = FCKeditorAPI.Instances[ name ];
            if (oEditor.IsDirty()) {
                gradingChanged = true;
            }
        }
    }
    //Check if there are dirty CKEditor instances
    if (typeof CKEDITOR != "undefined") {
        for(var name in CKEDITOR.instances) {
            var oEditor=CKEDITOR.instances[name];
            if(oEditor.checkDirty()) {
                gradingChanged = true;
            }
        }
    }
    
    var attachments = {};
    jQuery("input[name$='attachments-input']").each(function() {
        var that = jQuery(this);
        var key = that.attr('name');
        if (attachments[key]) {
            attachments[key].push(that.val());
        }
        else {
            attachments[key] = [that.val()];
        }
    });
    
    for (var attachname in attachments) {
        var attachFossilName = attachname+"-fossil";
        var attachFossil = jQuery("input[name='"+attachFossilName+"']");
        var fossil = RSF.parseFossil(attachFossil.val());
        var fossilObj = JSON.parse(fossil.oldvalue);

        var inputvals = attachments[attachname];
        if (fossilObj.length !== inputvals.length) {
            gradingChanged = true;
        }
        else {
            fossilObj.sort();
            inputvals.sort();
            for (var i = 0; i < fossilObj.length; i++) {
                if (fossilObj[i] !== inputvals[i]) {
                    gradingChanged = true;
                }
            }
        }
    }

    if (!gradingChanged)
    {
        // whether the grading points changed
        var currentGradePoints = $("input[name='page-replace\:\:grade_input']").val();
        currentGradePoints=asnn2gradeview.trimHtmlInput(currentGradePoints,false);
        var previousGradePoints = $("input[name='page-replace\:\:grade_input-fossil']").val();
        previousGradePoints = asnn2gradeview.trimHtmlInput(previousGradePoints,true);
        if (currentGradePoints !== previousGradePoints)
        {
            gradingChanged = true;
        }
        if (!gradingChanged)
        {
            // whether the grading comments changed
            var currentGradeComment = $('#page-replace\\:\\:grade_comment_input').val();
            currentGradeComment=asnn2gradeview.trimHtmlInput(currentGradeComment,false);
            var previousGradeComment = $("input[name='page-replace\:\:grade_comment_input-fossil']").val();
            previousGradeComment = asnn2gradeview.trimHtmlInput(previousGradeComment,true);
            if (currentGradeComment !== previousGradeComment)
            {
                gradingChanged = true;
            }
        }
    }

    return gradingChanged;
};

asnn2gradeview.endsWith = function(orig, end)
{
    return (orig.match(end+"$")==end)
}

asnn2gradeview.trimHtmlInput = function(inputString, isPreviousValue)
{
    var rv = inputString;
    if (rv != null)
    {
        // replace all breaks
        rv = rv.replace("<br>", "<br/>");
        if (isPreviousValue && (rv.indexOf("jstring#{") != -1 || rv.indexOf("istring#{") != -1))
        {   
            // the hidden fossile text value are of format jstring#{.....}value
            rv = rv.substring(inputString.indexOf("}")+1);
        }
        // if end with 
        if (asnn2gradeview.endsWith(rv,"<br>"))
        {
            var index = rv.lastIndexOf("<br>");
            rv = rv.substring(0, index);
        }
        if (asnn2gradeview.endsWith(rv,"<br/>"))
        {
            var index = rv.lastIndexOf("<br/>");
            rv = rv.substring(0, index);
        }
        if (asnn2gradeview.endsWith(rv,"<br />"))
        {
            var index = rv.lastIndexOf("<br />");
            rv = rv.substring(0, index);
        }
    }
    return rv;
};


