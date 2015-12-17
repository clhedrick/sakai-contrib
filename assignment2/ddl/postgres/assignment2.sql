
    create table A2_ASSIGNMENT_T (
        ASSIGNMENT_ID int8 not null,
        VERSION int4 not null,
        GRADEBOOK_ITEM_ID int8,
        CONTEXT varchar(99) not null,
        TITLE varchar(255) not null,
        DRAFT bool,
        SORT_INDEX int4 not null,
        OPEN_DATE timestamp not null,
        ACCEPT_UNTIL_DATE timestamp,
        GRADED bool,
        DUE_DATE timestamp,
        HONOR_PLEDGE bool,
        INSTRUCTIONS text,
        REQUIRES_SUBMISSION bool,
        SUBMISSION_TYPE int4 not null,
        SEND_SUBMISSION_NOTIF bool,
        HAS_ANNOUNCEMENT bool,
        ANNOUNCEMENT_ID varchar(99),
        ADDED_TO_SCHEDULE bool,
        EVENT_ID varchar(99),
        NUM_SUB_ALLOWED int4 not null,
        CONTENT_REVIEW_ENABLED bool,
        CONTENT_REVIEW_REF varchar(255),
        MODEL_ANSWER_ENABLED bool,
        MODEL_ANSWER_TEXT text,
        MODEL_ANSWER_DISPLAY_RULE int4,
        CREATOR varchar(99) not null,
        CREATE_DATE timestamp not null,
        MODIFIED_BY varchar(99),
        MODIFIED_DATE timestamp,
        REMOVED bool,
        primary key (ASSIGNMENT_ID)
    );

    create table A2_ASSIGN_ATTACH_T (
        ASSIGN_ATTACH_ID int8 not null,
        ASSIGN_ATTACH_TYPE varchar(1) not null,
        VERSION int4 not null,
        ASSIGNMENT_ID int8 not null,
        ATTACHMENT_REFERENCE varchar(255) not null,
        primary key (ASSIGN_ATTACH_ID),
        unique (ASSIGNMENT_ID, ATTACHMENT_REFERENCE)
    );

    create table A2_ASSIGN_GROUP_T (
        ASSIGNMENT_GROUP_ID int8 not null,
        VERSION int4 not null,
        ASSIGNMENT_ID int8 not null,
        GROUP_REF varchar(255) not null,
        primary key (ASSIGNMENT_GROUP_ID),
        unique (ASSIGNMENT_ID, GROUP_REF)
    );

    create table A2_SUBMISSION_ATTACH_T (
        SUBMISSION_ATTACH_ID int8 not null,
        SUB_ATTACH_TYPE varchar(1) not null,
        VERSION int4 not null,
        SUBMISSION_VERSION_ID int8 not null,
        ATTACHMENT_REFERENCE varchar(255) not null,
        primary key (SUBMISSION_ATTACH_ID),
        unique (SUBMISSION_VERSION_ID, ATTACHMENT_REFERENCE)
    );

    create table A2_SUBMISSION_T (
        SUBMISSION_ID int8 not null,
        VERSION int4 not null,
        ASSIGNMENT_ID int8 not null,
        USER_ID varchar(99) not null,
        RESUBMIT_CLOSE_DATE timestamp,
        NUM_SUB_ALLOWED int4,
        COMPLETED bool,
        CREATED_BY varchar(99) not null,
        CREATED_DATE timestamp not null,
        MODIFIED_BY varchar(99),
        MODIFIED_DATE timestamp,
        primary key (SUBMISSION_ID),
        unique (ASSIGNMENT_ID, USER_ID)
    );

    create table A2_SUBMISSION_VERSION_T (
        SUBMISSION_VERSION_ID int8 not null,
        VERSION int4 not null,
        SUBMISSION_ID int8 not null,
        SUBMITTED_DATE timestamp,
        SUBMITTED_VERSION_NUMBER int4 not null,
        FEEDBACK_RELEASED_DATE timestamp,
        SUBMITTED_TEXT text,
        HONOR_PLEDGE bool,
        ANNOTATED_TEXT text,
        FEEDBACK_NOTES text,
        DRAFT bool,
        CREATED_BY varchar(99) not null,
        CREATED_DATE timestamp not null,
        MODIFIED_BY varchar(99),
        MODIFIED_DATE timestamp,
        LAST_FEEDBACK_BY varchar(99),
        LAST_FEEDBACK_DATE timestamp,
        STUDENT_SAVE_DATE timestamp,
        FEEDBACK_LAST_VIEWED timestamp,
        primary key (SUBMISSION_VERSION_ID),
        unique (SUBMISSION_ID, SUBMITTED_VERSION_NUMBER)
    );

    create index A2_ASSIGN_REMOVED_I on A2_ASSIGNMENT_T (REMOVED);

    create index A2_ASSIGN_CONTEXT_I on A2_ASSIGNMENT_T (CONTEXT);

    create index A2_ASSIGN_ATTACH_ASSIGN_I on A2_ASSIGN_ATTACH_T (ASSIGNMENT_ID);

    create index ASSIGN_ATTACH_TYPE_I on A2_ASSIGN_ATTACH_T (ASSIGN_ATTACH_TYPE);

    alter table A2_ASSIGN_ATTACH_T 
        add constraint FKFF1065FC175E3454 
        foreign key (ASSIGNMENT_ID) 
        references A2_ASSIGNMENT_T;

    create index A2_ASSIGN_GROUP_ASSIGN_I on A2_ASSIGN_GROUP_T (ASSIGNMENT_ID);

    alter table A2_ASSIGN_GROUP_T 
        add constraint FK39B6CD12175E3454 
        foreign key (ASSIGNMENT_ID) 
        references A2_ASSIGNMENT_T;

    create index A2_SUB_ATTACH_VERSION_I on A2_SUBMISSION_ATTACH_T (SUBMISSION_VERSION_ID);

    create index SUB_ATTACH_TYPE_I on A2_SUBMISSION_ATTACH_T (SUB_ATTACH_TYPE);

    alter table A2_SUBMISSION_ATTACH_T 
        add constraint FK3FF3D33F49CF92D6 
        foreign key (SUBMISSION_VERSION_ID) 
        references A2_SUBMISSION_VERSION_T;

    create index A2_SUBMISSION_USER_ID_I on A2_SUBMISSION_T (USER_ID);

    create index A2_SUBMISSION_ASSIGN_I on A2_SUBMISSION_T (ASSIGNMENT_ID);

    alter table A2_SUBMISSION_T 
        add constraint FK4A5A558F175E3454 
        foreign key (ASSIGNMENT_ID) 
        references A2_ASSIGNMENT_T;

    create index A2_SUB_VERSION_SUBMITTED_NUM on A2_SUBMISSION_VERSION_T (SUBMITTED_VERSION_NUMBER);

    create index A2_SUB_VERSION_SUB_I on A2_SUBMISSION_VERSION_T (SUBMISSION_ID);

    create index A2_SUB_VERSION_SUB_DATE_I on A2_SUBMISSION_VERSION_T (SUBMITTED_DATE);

    alter table A2_SUBMISSION_VERSION_T 
        add constraint FK873450C88ABCB1A5 
        foreign key (SUBMISSION_ID) 
        references A2_SUBMISSION_T;

    create sequence A2_ASSIGNMENT_S;

    create sequence A2_ASSIGN_ATTACH_S;

    create sequence A2_ASSIGN_GROUP_S;

    create sequence A2_SUBMISSION_ATTACH_S;

    create sequence A2_SUBMISSION_S;

    create sequence A2_SUBMISSION_VERSION_S;
