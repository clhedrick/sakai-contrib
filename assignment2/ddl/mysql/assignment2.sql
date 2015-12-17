
    create table A2_ASSIGNMENT_T (
        ASSIGNMENT_ID bigint not null auto_increment,
        VERSION integer not null,
        GRADEBOOK_ITEM_ID bigint,
        CONTEXT varchar(99) not null,
        TITLE varchar(255) not null,
        DRAFT bit,
        SORT_INDEX integer not null,
        OPEN_DATE datetime not null,
        ACCEPT_UNTIL_DATE datetime,
        GRADED bit,
        DUE_DATE datetime,
        HONOR_PLEDGE bit,
        INSTRUCTIONS longtext,
        REQUIRES_SUBMISSION bit,
        SUBMISSION_TYPE integer not null,
        SEND_SUBMISSION_NOTIF bit,
        HAS_ANNOUNCEMENT bit,
        ANNOUNCEMENT_ID varchar(99),
        ADDED_TO_SCHEDULE bit,
        EVENT_ID varchar(99),
        NUM_SUB_ALLOWED integer not null,
        CONTENT_REVIEW_ENABLED bit,
        CONTENT_REVIEW_REF varchar(255),
        MODEL_ANSWER_ENABLED bit,
        MODEL_ANSWER_TEXT longtext,
        MODEL_ANSWER_DISPLAY_RULE integer,
        CREATOR varchar(99) not null,
        CREATE_DATE datetime not null,
        MODIFIED_BY varchar(99),
        MODIFIED_DATE datetime,
        REMOVED bit,
        primary key (ASSIGNMENT_ID)
    );

    create table A2_ASSIGN_ATTACH_T (
        ASSIGN_ATTACH_ID bigint not null auto_increment,
        ASSIGN_ATTACH_TYPE varchar(1) not null,
        VERSION integer not null,
        ASSIGNMENT_ID bigint not null,
        ATTACHMENT_REFERENCE varchar(255) not null,
        primary key (ASSIGN_ATTACH_ID),
        unique (ASSIGNMENT_ID, ATTACHMENT_REFERENCE)
    );

    create table A2_ASSIGN_GROUP_T (
        ASSIGNMENT_GROUP_ID bigint not null auto_increment,
        VERSION integer not null,
        ASSIGNMENT_ID bigint not null,
        GROUP_REF varchar(255) not null,
        primary key (ASSIGNMENT_GROUP_ID),
        unique (ASSIGNMENT_ID, GROUP_REF)
    );

    create table A2_SUBMISSION_ATTACH_T (
        SUBMISSION_ATTACH_ID bigint not null auto_increment,
        SUB_ATTACH_TYPE varchar(1) not null,
        VERSION integer not null,
        SUBMISSION_VERSION_ID bigint not null,
        ATTACHMENT_REFERENCE varchar(255) not null,
        primary key (SUBMISSION_ATTACH_ID),
        unique (SUBMISSION_VERSION_ID, ATTACHMENT_REFERENCE)
    );

    create table A2_SUBMISSION_T (
        SUBMISSION_ID bigint not null auto_increment,
        VERSION integer not null,
        ASSIGNMENT_ID bigint not null,
        USER_ID varchar(99) not null,
        RESUBMIT_CLOSE_DATE datetime,
        NUM_SUB_ALLOWED integer,
        COMPLETED bit,
        CREATED_BY varchar(99) not null,
        CREATED_DATE datetime not null,
        MODIFIED_BY varchar(99),
        MODIFIED_DATE datetime,
        primary key (SUBMISSION_ID),
        unique (ASSIGNMENT_ID, USER_ID)
    );

    create table A2_SUBMISSION_VERSION_T (
        SUBMISSION_VERSION_ID bigint not null auto_increment,
        VERSION integer not null,
        SUBMISSION_ID bigint not null,
        SUBMITTED_DATE datetime,
        SUBMITTED_VERSION_NUMBER integer not null,
        FEEDBACK_RELEASED_DATE datetime,
        SUBMITTED_TEXT longtext,
        HONOR_PLEDGE bit,
        ANNOTATED_TEXT longtext,
        FEEDBACK_NOTES longtext,
        DRAFT bit,
        CREATED_BY varchar(99) not null,
        CREATED_DATE datetime not null,
        MODIFIED_BY varchar(99),
        MODIFIED_DATE datetime,
        LAST_FEEDBACK_BY varchar(99),
        LAST_FEEDBACK_DATE datetime,
        STUDENT_SAVE_DATE datetime,
        FEEDBACK_LAST_VIEWED datetime,
        primary key (SUBMISSION_VERSION_ID),
        unique (SUBMISSION_ID, SUBMITTED_VERSION_NUMBER)
    );

    create index A2_ASSIGN_REMOVED_I on A2_ASSIGNMENT_T (REMOVED);

    create index A2_ASSIGN_CONTEXT_I on A2_ASSIGNMENT_T (CONTEXT);

    create index A2_ASSIGN_ATTACH_ASSIGN_I on A2_ASSIGN_ATTACH_T (ASSIGNMENT_ID);

    create index ASSIGN_ATTACH_TYPE_I on A2_ASSIGN_ATTACH_T (ASSIGN_ATTACH_TYPE);

    alter table A2_ASSIGN_ATTACH_T 
        add index FKFF1065FC175E3454 (ASSIGNMENT_ID), 
        add constraint FKFF1065FC175E3454 
        foreign key (ASSIGNMENT_ID) 
        references A2_ASSIGNMENT_T (ASSIGNMENT_ID);

    create index A2_ASSIGN_GROUP_ASSIGN_I on A2_ASSIGN_GROUP_T (ASSIGNMENT_ID);

    alter table A2_ASSIGN_GROUP_T 
        add index FK39B6CD12175E3454 (ASSIGNMENT_ID), 
        add constraint FK39B6CD12175E3454 
        foreign key (ASSIGNMENT_ID) 
        references A2_ASSIGNMENT_T (ASSIGNMENT_ID);

    create index A2_SUB_ATTACH_VERSION_I on A2_SUBMISSION_ATTACH_T (SUBMISSION_VERSION_ID);

    create index SUB_ATTACH_TYPE_I on A2_SUBMISSION_ATTACH_T (SUB_ATTACH_TYPE);

    alter table A2_SUBMISSION_ATTACH_T 
        add index FK3FF3D33F49CF92D6 (SUBMISSION_VERSION_ID), 
        add constraint FK3FF3D33F49CF92D6 
        foreign key (SUBMISSION_VERSION_ID) 
        references A2_SUBMISSION_VERSION_T (SUBMISSION_VERSION_ID);

    create index A2_SUBMISSION_USER_ID_I on A2_SUBMISSION_T (USER_ID);

    create index A2_SUBMISSION_ASSIGN_I on A2_SUBMISSION_T (ASSIGNMENT_ID);

    alter table A2_SUBMISSION_T 
        add index FK4A5A558F175E3454 (ASSIGNMENT_ID), 
        add constraint FK4A5A558F175E3454 
        foreign key (ASSIGNMENT_ID) 
        references A2_ASSIGNMENT_T (ASSIGNMENT_ID);

    create index A2_SUB_VERSION_SUBMITTED_NUM on A2_SUBMISSION_VERSION_T (SUBMITTED_VERSION_NUMBER);

    create index A2_SUB_VERSION_SUB_I on A2_SUBMISSION_VERSION_T (SUBMISSION_ID);

    create index A2_SUB_VERSION_SUB_DATE_I on A2_SUBMISSION_VERSION_T (SUBMITTED_DATE);

    alter table A2_SUBMISSION_VERSION_T 
        add index FK873450C88ABCB1A5 (SUBMISSION_ID), 
        add constraint FK873450C88ABCB1A5 
        foreign key (SUBMISSION_ID) 
        references A2_SUBMISSION_T (SUBMISSION_ID);
