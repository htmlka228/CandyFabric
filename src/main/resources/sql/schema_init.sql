create table achievement (
    id  bigserial not null,
    name varchar(255),
    tournament_id int8,
    primary key (id)
);

create table game (
    id  bigserial not null,
    name varchar(255),
    creator varchar(255),
    primary key (id)
);

create table player (
    id  bigserial not null,
    name varchar(255),
    primary_country varchar(255),
    active boolean not null,
    game_id int8,
    team_id int8,
    primary key (id)
);

create table player_achievements (
    player_id int8 not null,
    achievements_id int8 not null
);

create table player_previous_teams (
    player_id int8 not null,
    previous_teams_id int8 not null
);

create table player_previous_tournaments (
    player_id int8 not null,
    previous_tournaments_id int8 not null
);

create table team (
    id  bigserial not null,
    name varchar(255),
    country varchar(255),
    active boolean not null,
    primary key (id)
);

create table team_achievements (
    team_id int8 not null,
    achievements_id int8 not null
);

create table tournament (
    id  bigserial not null,
    name varchar(255),
    game_id int8,
    primary key (id)
);

alter table if exists achievement add constraint fk_achievement_tournament foreign key (tournament_id) references tournament;
alter table if exists tournament add constraint fk_tournament_game foreign key (game_id) references game;
alter table if exists player add constraint fk_player_game foreign key (game_id) references game;
alter table if exists player add constraint fk_player_team foreign key (team_id) references team;
alter table if exists player_achievements add constraint fk_player_achievements_achievement foreign key (achievements_id) references achievement;
alter table if exists player_achievements add constraint fk_player_achievements_player foreign key (player_id) references player;
alter table if exists player_previous_teams add constraint fk_player_previous_teams_team foreign key (previous_teams_id) references team;
alter table if exists player_previous_teams add constraint fk_player_previous_teams_player foreign key (player_id) references player;
alter table if exists player_previous_tournaments add constraint fk_player_previous_tournaments_tournament foreign key (previous_tournaments_id) references tournament;
alter table if exists player_previous_tournaments add constraint fk_player_previous_tournaments_player foreign key (player_id) references player;
alter table if exists team_achievements add constraint fk_team_achievements_achievement foreign key (achievements_id) references achievement;
alter table if exists team_achievements add constraint fk_team_achievements_team foreign key (team_id) references team;