

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_ID'
where PROPERTY_NAME='ID';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_NM'
where PROPERTY_NAME='NAME';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='D_WGHT'
where PROPERTY_NAME='WEIGHT';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_EQL_WGHT'
where PROPERTY_NAME='EQUAL_WEIGHT';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_X_CRDT'
where PROPERTY_NAME='EXTRA_CREDIT';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_INCLD'
where PROPERTY_NAME='INCLUDED';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_RMVD'
where PROPERTY_NAME='REMOVED';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_GB_NAME'
where PROPERTY_NAME='GRADEBOOK';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='I_DRP_LWST'
where PROPERTY_NAME='DROP_LOWEST';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_CTGRY_NAME'
where PROPERTY_NAME='CATEGORY_NAME';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='L_CTGRY_ID'
where PROPERTY_NAME='CATEGORY_ID';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='W_DUE'
where PROPERTY_NAME='DUE_DATE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='D_PNTS'
where PROPERTY_NAME='POINTS';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_PNTS'
where PROPERTY_NAME='POINTS_STRING';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_RLSD'
where PROPERTY_NAME='RELEASED';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_NLLS_ZEROS'
where PROPERTY_NAME='NULLSASZEROS';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_SOURCE'
where PROPERTY_NAME='SOURCE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_ITM_TYPE'
where PROPERTY_NAME='ITEM_TYPE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='D_PCT_GRD'
where PROPERTY_NAME='PERCENT_COURSE_GRADE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_PCT_GRD'
where PROPERTY_NAME='PERCENT_COURSE_GRADE_STRING';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='D_PCT_CTGRY'
where PROPERTY_NAME='PERCENT_CATEGORY';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_PCT_CTGRY'
where PROPERTY_NAME='PERCENT_CATEGORY_STRING';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_IS_PCT'
where PROPERTY_NAME='IS_PERCENTAGE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='O_LRNR_KEY'
where PROPERTY_NAME='STUDENT_MODEL_KEY';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='L_ITM_ID'
where PROPERTY_NAME='ASSIGNMENT_ID';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_NM'
where PROPERTY_NAME='NAME';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_DATA_TYPE'
where PROPERTY_NAME='DATA_TYPE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='C_CTGRY_TYPE'
where PROPERTY_NAME='CATEGORYTYPE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='G_GRD_TYPE'
where PROPERTY_NAME='GRADETYPE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_REL_GRDS'
where PROPERTY_NAME='RELEASEGRADES';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_REL_ITMS'
where PROPERTY_NAME='RELEASEITEMS';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='I_SRT_ORDR'
where PROPERTY_NAME='ITEM_ORDER';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='L_GRD_SCL_ID'
where PROPERTY_NAME='GRADESCALEID';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_RECALC_PTS'
where PROPERTY_NAME='DO_RECALCULATE_POINTS';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_WT_BY_PTS'
where PROPERTY_NAME='ENFORCE_POINT_WEIGHTING';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_SHW_MEAN'
where PROPERTY_NAME='SHOWMEAN';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_SHW_MEDIAN'
where PROPERTY_NAME='SHOWMEDIAN';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_SHW_MODE'
where PROPERTY_NAME='SHOWMODE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_SHW_RANK'
where PROPERTY_NAME='SHOWRANK';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_SHW_ITM_STATS'
where PROPERTY_NAME='SHOWITEMSTATS';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='A_CHILDREN'
where PROPERTY_NAME='CHILDREN';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_ACTIVE'
where PROPERTY_NAME='IS_ACTIVE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_EDITABLE'
where PROPERTY_NAME='IS_EDITABLE';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_CHKD'
where PROPERTY_NAME='IS_CHECKED';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='S_PARENT'
where PROPERTY_NAME='PARENT_NAME';

update GB_ACTION_RECORD_PROPERTY_T
set PROPERTY_NAME='B_ALW_SCL_X_CRDT'
where PROPERTY_NAME='ALLOW_SCALED_EXTRA_CREDIT';

commit;
