create database QuestionGame;
use QuestionGame;
create table users
(
	username varchar(100),
    password varchar(100),
    primary key(username)
);

insert into users(username,password) values ('nhom5','5'),('quan','5');

create table category
(
	id int auto_increment,
    catename varchar(100),
    
    primary key (id)
);
insert into category(catename) values ('Math') ,('English'),('History');

create table quesandans
(
	id int auto_increment,
    ques varchar(500),
    ans varchar(500),
	idcate int,
    
	primary key (id),
    foreign key (idcate) references category (id)
);
insert into quesandans (ques,ans,idcate) values 
('1+3=?','4',1) ,('Next of number 3 is:?','4',2),('Bản tuyên ngôn độc lập được Hồ Chủ Tịch đọc vào năm nào?','1945',3),
('9*3=?','27',1) ,('Next of number 8 is:?','9',2),('Trận Điện Biên Phủ trên không diễn ra bao nhiêu ngày đêm?','12',3),
('6/3=?','2',1) ,('The number before number 3 is:?','2',2),('Ngay sinh cua chu tich Ho Chi Minh la ngay nao?','19',3);

create table userhistory
(
	username_id int,
    name nvarchar(100),
    score int default 0,
    
    primary key (username_id),
    foreign key (name) references users(username)
);

update userhistory set score = 0 where name = 'quan';
insert into userhistory(username_id,name) values (1,'nhom5'),(2,'quan');
insert into userhistory(username_id,name) values (3,'vanquan'),(4,'quyen');
delete from category where id = 6;
select *from users;


