create table players (
	id int,
	name varchar(255),
	user_name varchar(255)
);


alter table players
add primary key(id)

select * from players

alter table players modify column id int not null AUTO_INCREMENT


create table game (
	id int,
	created_date TIMESTAMP
)
drop table game
select * from game
alter table game add column winner int
alter table game add column max_score long
alter table game add primary key(id)
alter table game modify column id int not null AUTO_INCREMENT





desc game_members

drop table game_members

select * from game_members

create table game_members (
	id int,
	game_id int,
	players_id int
)

alter table game_members
add primary key(id)

alter table game_members add column total_score int

alter table game_members modify column total_score int not null

alter table game_members add column is_active boolean not null default 1

alter table game_members modify column id int not null AUTO_INCREMENT

alter table game_members add foreign key(game_id) references game(id)

alter table game_members add foreign key(players_id) references players(id)


update game_members gm set gm.total_score = 40 where gm.id = 1


create table game_rounds (
	id int,
	game_id int
)

alter table game_rounds add primary key(id)

alter table game_rounds add column custom_id int not null

alter table game_rounds modify column id int not null AUTO_INCREMENT

alter table game_rounds add foreign key(game_id) references game(id)

select * from game_rounds

drop table game_rounds


create table game_scores (
	id int,
	round_id int,
	member_id int,
	score int								
)

alter table game_scores add primary key(id)

alter table game_scores modify column id int not null AUTO_INCREMENT

alter table game_scores add foreign key(round_id) references game_rounds(id)

alter table game_scores add foreign key(member_id) references game_members(id)

select * from game_scores

drop table game_scores