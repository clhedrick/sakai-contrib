
    create table A2_ASSIGNMENT_T (
        ASSIGNMENT_ID numeric(19,0) identity not null,
        VERSION int not null,
        GRADEBOOK_ITEM_ID numeric(19,0) null,
        CONTEXT varchar(99) not null,
        TITLE varchar(255) not null,
        DRAFT tinyint null,
        SORT_INDEX int not null,
        OPEN_DATE datetime not null,
        ACCEPT_UNTIL_DATE datetime null,
        GRADED tinyint null,
        DUE_DATE datetime null,
        HONOR_PLEDGE tinyint null,
        INSTRUCTIONS text null,
        REQUIRES_SUBMISSION tinyint null,
        SUBMISSION_TYPE int not null,
        SEND_SUBMISSION_NOTIF tinyint null,
        HAS_ANNOUNCEMENT tinyint null,
        ANNOUNCEMENT_ID varchar(99) null,
        ADDED_TO_SCHEDULE tinyint null,
        EVENT_ID varchar(99) null,
        NUM_SUB_ALLOWED int not null,
        CONTENT_REVIEW_ENABLED tinyint null,
        CONTENT_REVIEW_REF varchar(255) null,
        MODEL_ANSWER_ENABLED tinyint null,
        MODEL_ANSWER_TEXT text null,
        MODEL_ANSWER_DISPLAY_RULE int null,
        CREATOR varchar(99) not null,
        CREATE_DATE datetime not null,
        MODIFIED_BY varchar(99) null,
        MODIFIED_DATE datetime null,
        REMOVED tinyint null,
        primary key (ASSIGNMENT_ID)
    );

    create table A2_ASSIGN_ATTACH_T (
        ASSIGN_ATTACH_ID numeric(19,0) identity not null,
        ASSIGN_ATTACH_TYPE varchar(1) not null,
        VERSION int not null,
        ASSIGNMENT_ID numeric(19,0) not null,
        ATTACHMENT_REFERENCE varchar(255) not null,
        primary key (ASSIGN_ATTACH_ID),
        unique (ASSIGNMENT_ID, ATTACHMENT_REFERENCE)
    );

    create table A2_ASSIGN_GROUP_T (
        ASSIGNMENT_GROUP_ID numeric(19,0) identity not null,
        VERSION int not null,
        ASSIGNMENT_ID numeric(19,0) not null,
        GROUP_REF varchar(255) not null,
        primary key (ASSIGNMENT_GROUP_ID),
        unique (ASSIGNMENT_ID, GROUP_REF)
    );

    create table A2_SUBMISSION_ATTACH_T (
        SUBMISSION_ATTACH_ID numeric(19,0) identity not null,
        SUB_ATTACH_TYPE varchar(1) not null,
        VERSION int not null,
        SUBMISSION_VERSION_ID numeric(19,0) not null,
        ATTACHMENT_REFERENCE varchar(255) not null,
        primary key (SUBMISSION_ATTACH_ID),
        unique (SUBMISSION_VERSION_ID, ATTACHMENT_REFERENCE)
    );

    create table A2_SUBMISSION_T (
        SUBMISSION_ID numeric(19,0) identity not null,
        VERSION int not null,
        ASSIGNMENT_ID numeric(19,0) not null,
        USER_ID varchar(99) not null,
        RESUBMIT_CLOSE_DATE datetime null,
        NUM_SUB_ALLOWED int null,
        COMPLETED tinyint null,
        CREATED_BY varchar(99) not null,
        CREATED_DATE datetime not null,
        MODIFIED_BY varchar(99) null,
        MODIFIED_DATE datetime null,
        primary key (SUBMISSION_ID),
        unique (ASSIGNMENT_ID, USER_ID)
    );

    create table A2_SUBMISSION_VERSION_T (
        SUBMISSION_VERSION_ID numeric(19,0) identity not null,
        VERSION int not null,
        SUBMISSION_ID numeric(19,0) not null,
        SUBMITTED_DATE datetime null,
        SUBMITTED_VERSION_NUMBER int not null,
        FEEDBACK_RELEASED_DATE datetime null,
        SUBMITTED_TEXT text null,
        HONOR_PLEDGE tinyint null,
        ANNOTATED_TEXT text null,
        FEEDBACK_NOTES text null,
        DRAFT tinyint null,
        CREATED_BY varchar(99) not null,
        CREATED_DATE datetime not null,
        MODIFIED_BY varchar(99) null,
        MODIFIED_DATE datetime null,
        LAST_FEEDBACK_BY varchar(99) null,
        LAST_FEEDBACK_DATE datetime null,
        STUDENT_SAVE_DATE datetime null,
        FEEDBACK_LAST_VIEWED datetime null,
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
