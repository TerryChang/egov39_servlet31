create table board
(
    idx bigserial not null
        constraint pk_board
            primary key,
    title varchar(100),
    contents text,
    regdate timestamp
);

alter table board owner to postgres;

create table upload_file
(
    idx bigserial not null
        constraint pk_upload_file
            primary key,
    board_idx bigint not null
        constraint fk_board_idx
            references board,
    orgfilename varchar(100),
    realfilename varchar(100),
    ext varchar(10),
    filesize bigint,
    regdate timestamp
);

alter table upload_file owner to postgres;

