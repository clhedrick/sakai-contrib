var asnn2subview = asnn2subview || {};

/*
asnn2subview.selectorMap = [
  { selector: ".row", id: "row:" },
  { selector: ".sub-table-header", id: "sub-table-header:" },
  { selector: ".student-name", id: "student-name"},
  { selector: ".submitted-time", id: "submitted-time"},
  { selector: ".submission-status", id: "submission-status"},
  { selector: ".grade", id: "grade"},
  { selector: ".review-score", id: "review-score"},
  { selector: ".review-error", id: "review-error"},
  { selector: ".review-multiple", id: "review-multiple"},
  { selector: ".review-pending", id: "review-pending"},
  { selector: ".feedback-released", id: "feedback-released"},
  { selector: ".student-grade-link", id: "student-grade-link"},
  { selector: ".student-name-sort", id: "student-name-sort" },
  { selector: ".student-name-sort-img", id: "student-name-sort-img" },
  { selector: ".submitted-time-sort", id: "submitted-time-sort"},
  { selector: ".submitted-time-sort-img", id: "submitted-time-sort-img"},
  { selector: ".submission-status-sort", id: "submission-status-sort"},
  { selector: ".submission-status-sort-img", id: "submission-status-sort-img"},
  { selector: ".submission-report-sort", id: "submission-report-sort" },
  { selector: ".submission-report-sort-img", id: "submission-report-sort-img" },
  { selector: ".feedback-released-sort", id: "feedback-released-sort" },
  { selector: ".feedback-released-sort-img", id: "feedback-released-sort-img" },
  { selector: ".grade-sort", id: "grade-sort" },
  { selector: ".grade-sort-img", id: "grade-sort-img" },
  { selector: ".grade-col-header", id: "grade-col-header" },
  { selector: ".grade-td", id: "grade-td" },
  { selector: ".report-col-header", id: "report-col-header" }
];
*/

asnn2subview.classSelectors = ["row:", "sub-table-header:", "student-name",
  "submitted-time", "submission-status", "grade", "review-score", "review-error",
  "review-multiple", "review-pending", "feedback-released", "student-grade-link",
  "student-name-sort", "student-name-sort-img", "submitted-time-sort", 
  "submitted-time-sort-img", "submission-status-sort", "submission-status-sort-img",
  "submission-report-sort", "submission-report-sort-img", "feedback-released-sort",
  "feedback-released-sort-img", "grade-sort", "grade-sort-img", "grade-col-header",
  "grade-td", "report-col-header" ];

asnn2subview.selectorMap = fluid.transform(asnn2subview.classSelectors, function (obj, idx) {
  var cssClass = "."+obj;
  if (cssClass.slice(cssClass.length-1)===":") {
    cssClass = cssClass.slice(0,cssClass.length-1);
  }
  return { selector: cssClass, id: obj };
});


asnn2subview.getSortHeaderComptree = function(newModel) {
  var onSortClick = function(sortBy) {
    return function() {
      var newModel = fluid.copy(asnn2subview.pager.model);
      newModel.pageIndex = 0;
      newModel.sortKey = sortBy;
      if (!newModel.sortDir) {
        newModel.sortDir = 1; 
      } 
      else {
        newModel.sortDir = -1 * newModel.sortDir;
      }        
      jQuery(".sortimg").remove();

      asnn2subview.pager.model = newModel;
      asnn2subview.pager.events.onModelChange.fire(newModel);
    }
  };

  var tree = {
      children: [ 
        { ID: "student-name-sort",
          value: true,
          decorators: [
            {"jQuery": ["click", onSortClick('studentName')]}
          ]
        }
      ]
  };
  
  if (asnn2subview.nonElectronicSubmission === false) {
      tree.children.push({
          ID: "submitted-time-sort", value: true,
          decorators: [
                        {"jQuery": ["click", onSortClick('submittedDate')]}
          ]
      });
      tree.children.push({ 
          ID: "submission-status-sort", value: true,
          decorators: [
                        {"jQuery": ["click", onSortClick('submissionStatus')]}
          ]
      });
  }
  
  tree.children.push({ 
          ID: "feedback-released-sort", value: true,
          decorators: [
                    {"jQuery": ["click", onSortClick('feedbackReleased')]}
      ]
    });


  if (asnn2subview.graded === true) {
    tree.children.push({
      ID: "grade-col-header"
    });
    tree.children.push({
      ID: "grade-sort", 
      value: true,
      decorators: [
        {"jQuery": ["click", onSortClick('grade')]}
      ]
    });
  }
  
  if (asnn2subview.reviewEnabled === true) {
      tree.children.push({
        ID: "report-col-header", value: true
      });
      /*tree.children.push({
        ID: "submission-report-sort", 
        value: true,
        decorators: [
          {"jQuery": ["click", onSortClick('report')]}
        ]
      });*/
    } else {
      // hide the associated tds
      jQuery('td.report_info').hide();
    }

  if (newModel.sortDir > 0) {
    var imgsrc = "/library/image/sakai/sortascending.gif";
  }  
  else {
    imgsrc = "/library/image/sakai/sortdescending.gif";
  }

  if (newModel.sortKey === "studentName") {
    tree.children.push({ ID: "student-name-sort-img", target: imgsrc });
  } else if (newModel.sortKey === "submittedDate") {
    tree.children.push({ ID: "submitted-time-sort-img", target: imgsrc });
  } else if (newModel.sortKey === "submissionStatus") {
    tree.children.push({ ID: "submission-status-sort-img", target: imgsrc });
  } else if (newModel.sortKey === "feedbackReleased") {
    tree.children.push({ ID: "feedback-released-sort-img", target: imgsrc });
  } else if (newModel.sortKey === "grade") {
    tree.children.push({ ID: "grade-sort-img", target: imgsrc });
  }

  return tree;
};

/**
 * Turns the model waiting spinner on and off. 
 *
 * @param (boolean) On or Off
 */
asnn2subview.spinner = function(ison) {
  if (!asnn2.pageLoaded || asnn2.pageLoaded === false) {
    return; // IE blows up if this runs during page load
  }

  if (ison === true) {
    jQuery("#spinnerDiv").dialog({
      modal: true,
      bgiframe: true,
      dialogClass: 'spinnerDialog'
    }).show();
  }
  else {
    jQuery("#spinnerDiv").dialog('close'); 
  }
}

asnn2subview.subTableRenderer = function (overallThat, inOptions) {
  var that = fluid.initView("asnn2subview.subTableRenderer", overallThat.container, inOptions);
  return {
    returnedOptions: {
      listeners: {
        onModelChange: function (newModel, oldModel) {
          if (!newModel.sortKey) {
            newModel.sortKey = "studentName";
          }
          if (!newModel.sortDir) {
            newModel.sortDir = 1; 
          }
          
          var order = "&_order=" + newModel.sortKey;
          if (newModel.sortDir && newModel.sortDir < 0) {
            order = order + "_desc";
          }

          if (newModel.groupId && newModel.groupId !== "") {
            var groupfilter = "&groupId="+newModel.groupId;
          }
          else {
            groupfilter = "";
          }
          asnn2subview.spinner(true);
          jQuery.ajax({
            type: "GET",
            url: "/direct/assignment2submission.json?asnnid="+asnn2subview.asnnid+"&placementId="+sakai.curPlacement+"&_start="+(newModel.pageIndex*newModel.pageSize)+"&_limit="+newModel.pageSize+order+groupfilter,
            cache: false,
            success: function (payload) {
              var data = JSON.parse(payload);
              togo = fluid.transform(data.assignment2submission_collection, asnn2util.dataFromEntity, asnn2subview.filteredRowTransform);

              if (togo.length === 0) {
                jQuery("#submissions-table-area").hide();
                jQuery("#no-students-area").show();
              }
              else {
                jQuery("#submissions-table-area").show();
                jQuery("#no-students-area").hide();
              }
               
              var treedata = { 
                "sub-table-header:": asnn2subview.getSortHeaderComptree(newModel),
                "row:": togo  
              };
              // Keep this around to test on Fluid Trunk and create a Jira to have more debug information if it's still the same.
              //var treedata = { children: [{ ID: "row:", children: togo }] };
              asnn2subview.renderSubmissions(treedata);
              asnn2subview.spinner(false);
            },
            failure: function() {
              // TODO We need to handle this
            } 
          });
        }
      }
    }
  };

};

asnn2subview.renderSubmissions = function(treedata) {
  if (asnn2subview.subListTemplate) {
    fluid.reRender(asnn2subview.subListTemplate, jQuery("#asnn-submissions-table"), treedata, {cutpoints: asnn2subview.selectorMap});
  }
  else {
    asnn2subview.subListTemplate = fluid.selfRender(jQuery("#asnn-submissions-table"), treedata, {cutpoints: asnn2subview.selectorMap});
  }
  RSF.getDOMModifyFirer().fireEvent();
  
  
  asnn2subview.alignGrading();
}

asnn2subview.initPager = function(numSubmissions, curPageSize, curOrderBy, curAscending, pageIndex) {
  var graded = true;

  var columnDefs = [
    {
      key: "student-name-sort",
      valuebinding: "*.studentName",
      sortable: true
    }
  ];
  
  if (asnn2subview.nonElectronicSubmission === false) {
    columnDefs.push ({
            key: "submitted-time-sort",
            valuebinding: "*.submittedDateFormat",
            sortable: true
      });
    columnDefs.push({
        key: "submission-status-sort",
        valuebinding: "*.submissionStatus",
        sortable: true
      });	
  }
  
  columnDefs.push({
      key: "feedback-released-sort",
      valuebinding: "*.feedbackReleased",
      sortable: true
    });


  if (asnn2subview.graded === true) {
    columnDefs.push({
      key: "grade-sort",
      valuebinding: "*.grade",
      sortable: true
    });
  }

  var pagerBarOptions = {
          type: "fluid.pager.pagerBar",
          options: {
            pageList: {
               type: "fluid.pager.renderedPageList",
               options: {
                 linkBody: "a",
                 pageStrategy: fluid.pager.gappedPageStrategy(3, 1)
                 }
               }
            }
      };

  var fakedata = [];
  for (var i = 0; i < numSubmissions; i++) {
    fakedata.push(i);
  }
  
  if (!curPageSize) {
    curPageSize = 200;
  }
  
  asnn2subview.pager = fluid.pager("#submissions-table-area", {
    model: {
      pageIndex: pageIndex,
      pageSize: curPageSize,
      totalRange: undefined,
      sortKey: curOrderBy,
      sortDir: curAscending
    },
    dataModel: fakedata,
    columnDefs: columnDefs,
    pagerBar: pagerBarOptions,
    bodyRenderer: {
      type: "asnn2subview.subTableRenderer",
      options: {
//        filteredRowTransform : filteredRowTransform,
        selectors: {
          root: ".fl-pager-data"
        },
        renderOptions: {debugMode: false, cutpoints: asnn2subview.selectorMap}
      }
    }

  });
  
  // Init Group Filter
  jQuery('#page-replace\\:\\:group_filter-selection').change(function() {
    var newModel = fluid.copy(asnn2subview.pager.model);
    newModel.pageIndex = 0;
    newModel.groupId = jQuery(this).val();
    asnn2subview.pager.model = newModel;
    asnn2subview.pager.events.onModelChange.fire(newModel);
    
    var downloadAllLink = jQuery("#download_all_span > a");
    
    if (downloadAllLink.length  == 1) {
        var linkHref = downloadAllLink.attr('href');
    	
        var questionMarkIndex = linkHref.indexOf("?");
    	
        var newHref = "";
    	
        if (questionMarkIndex == -1) {
            newHref = linkHref;
        }
        else {
            newHref = linkHref.substring(0, questionMarkIndex);
    	}
    		
        if (jQuery.trim(newModel.groupId) == "") {
             downloadAllLink.attr('href', newHref);
        }
        else {
            downloadAllLink.attr('href', newHref + "?filterGroupId=" + newModel.groupId);    			
    	}
    }

  });

  if (jQuery('#page-replace\\:\\:group_filter-selection').length === 0) {
    jQuery('#page-list').show();
  }

};

/**
 * Aligns the "Apply to Unassigned" box with the grading column
 */
asnn2subview.alignGrading = function() {
    var p = jQuery("td.grade:first");
    var position = p.position();
    if (position) {
        var applyToUnassigned = jQuery('div.unassigned-apply');
        applyToUnassigned.attr("style", "margin-left:" + position.left + "px");
    }
};

var pathprefix = '/portal/tool/'+sakai.curPlacement;
if (window.location.pathname.indexOf('/portal/site/') == 0) 
  pathprefix = '/portal/site/'+sakai.curContext+'/tool/'+sakai.curPlacement;

asnn2subview.filteredRowTransform = function(obj, idx) {
    var row = obj;
    var togo = [
      { ID: "student-grade-link",
        target: pathprefix+'/grade/'+asnn2.curAsnnId+'/'+row.studentId+'?viewSubPageIndex='+asnn2subview.pager.model.pageIndex,
        linktext: row.studentName
      }
    ];

    if (asnn2subview.nonElectronicSubmission === false) {

   	    togo.push({ ID: "submission-status",
            value: row.submissionStatus
          });

        if (row.submittedDateFormat) {
            togo.push({ ID: "submitted-time",
              value: row.submittedDateFormat
            });
          }
   
    }

    if (row.feedbackReleased === true) {
      togo.push({ ID: "feedback-released", value: true});
    }

    if (asnn2subview.graded === true) {
      togo.push({ ID: "grade", value: row.grade});
    }
    
    if (asnn2subview.reviewEnabled === true) {
      if (row.reviewScore && !row.reviewPending) {
        togo.push({ ID: "review-score", 
                    linktext: row.reviewScore,
                    target: row.reviewScoreLink,
                    decorators: [
                     {
                        attrs: {title: row.reviewScoreLinkInfo}
                      },
                      {
                        type: "addClass",
                        classes: row.reviewScoreClass
                      }
                    ]
          });
      } else if (row.reviewScore && row.reviewPending) {
        togo.push({ ID: "review-pending", 
                    value: row.reviewScore,
                    
                    decorators: [
                     {
                        attrs: {title: row.reviewPendingText}
                      },
                      {
                        type: "addClass",
                        classes: "reportStatusPending"
                      }
                    ]
          });
      }
      
      if (row.reviewMultiple) {
        togo.push({ ID: "review-multiple", 
                    value: row.reviewMultiple,
                    decorators: [
                       {
                         attrs: {title: row.reviewMultipleInfo, alt: row.reviewMultipleInfo}
                       }
                    ]
        });
      }
      
      if (row.reviewError) {
        togo.push({ ID: "review-error", 
                    value: row.reviewError,
                    decorators: [
                      {
                        attrs: {title: row.reviewErrorMsg, alt: row.reviewErrorMsg}
                      }
                    ]
        });
      }
    }

    return togo;
  };

asnn2subview.init = function(asnnid, contextId, placementId, numSubmissions, graded, reviewEnabled, nonElectronicSubmission, curPageSize, curOrderBy, curAscending, gradesReleased, pageIndex) {
  sakai.curPlacement = placementId;
  sakai.curContext = contextId;

  asnn2.curAsnnId = asnnid;

  asnn2subview.asnnid = asnnid;
  // TODO Graded is being encoded as a String param for some reason :|
  if (graded === "true") {
    asnn2subview.graded = true;
    
    // this is a bit of a hack to change the heading on the grade column depending
    // on the released status of the gradebook item
    if (gradesReleased == "true") {
        jQuery("a.grade-sort").html("Grade (Released)");
    } else {
        jQuery("a.grade-sort").html("Grade (Not Released)");
    }
  }
  else { 
    asnn2subview.graded = false;
  }

  if (reviewEnabled === "true") {
    asnn2subview.reviewEnabled = true;
  } else {
    asnn2subview.reviewEnabled = false;
  }
  
  if (nonElectronicSubmission === "true") {
        asnn2subview.nonElectronicSubmission = true;
  } else {
        asnn2subview.nonElectronicSubmission = false;
  }

  if (curAscending === "false" || curAscending === false) {
    curAscending = -1;
  }
  else { // Default to ascending
    curAscending = 1;
  }
  
  asnn2subview.initPager(numSubmissions, curPageSize, curOrderBy, curAscending, pageIndex);

};
