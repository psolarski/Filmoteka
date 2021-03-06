/* Users */
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('admin@admin.admin', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'admin');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user2@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user2');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user3@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user3');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user4@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user4');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user5@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user5');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user6@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user6');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user7@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user7');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user8@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user8');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user9@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user9');
INSERT INTO PUBLIC.USER (EMAIL, PASSWORD, USERNAME) VALUES ('user10@user.user', '$2a$11$Msstp8c.6ktm4x7Y63QI3ezgnPqFPiuTuMOh.ysCVIbZBJMJL6Aqi', 'user10');

/* Roles */
INSERT INTO PUBLIC.ROLE (NAME) VALUES ('ADMIN');
INSERT INTO PUBLIC.ROLE (NAME) VALUES ('USER');

/* Users to roles */
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (1, 'ADMIN');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (1, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (2, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (3, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (4, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (5, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (6, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (7, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (8, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (9, 'USER');
INSERT INTO PUBLIC.USERS_ROLES (USER_ID, ROLE_NAME) VALUES (10, 'USER');

/* Directors */
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Brian', 'United Kingdom', 'Fee');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Francis Ford', 'United States of America', 'Coppola');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('John', 'United States of America', 'Lasseter');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Olaf', 'Polish', 'Lubaszenko');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Christopher', 'United States of America', 'Nolan');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('James', 'United States of America', 'Cameron');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Roger', 'United States of America', 'Allers');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Ridley', 'United Kingdom', 'Scott');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Stanley', 'United Kingdom', 'Kubrick');
INSERT INTO PUBLIC.DIRECTOR (NAME, NATIONALITY, SURNAME) VALUES ('Tim', 'United Kingdom', 'Miller');

/* Movies */
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (102, 'Animation', 'English', 'Cars 3', '2017-07-16', 1);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (175, 'Crime', 'English', 'The Godfather', '1972-03-24', 2);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (81, 'Animation', 'English', 'Toy Story', '1995-11-22', 3);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (90, 'Comedy', 'Polish', 'Chlopaki nie placza', '2000-02-25', 4);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (152, 'Crime', 'English', 'The Dark Knight', '2008-07-18', 5);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (194, 'Drama', 'English', 'Titanic ', '1997-12-19', 6);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (88, 'Drama', 'English', 'The Lion King', '1994-06-24', 7);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (117, 'Horror', 'English', 'Alien', '1979-06-22', 8);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (146, 'Horror', 'English', 'The Shining', '1980-06-13', 9);
INSERT INTO PUBLIC.MOVIE (DURATION, GENRE, LANGUAGE, NAME, RELEASE_DATE, DIRECTOR_ID) VALUES (108, 'Comedy', 'English', 'Deadpool', '2016-02-12', 10);

/* Actors */
/* Cars 3 - IDs from 1 to 3 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Owen', 'United States of America', 'Wilson');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Cristela', 'United States of America', 'Alonzo');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Chris', 'United States of America', 'Cooper');

/* The Godfather - IDs from 4 to 6 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Marlon', 'United States of America', 'Brando');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Al', 'United States of America', 'Pacino');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('James', 'United States of America', 'Caan');

/* Toy Story - IDs from 7 to 9 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Tom', 'United States of America', 'Hanks');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Tim', 'United States of America', 'Allen');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Don', 'United States of America', 'Rickles');

/* Chłopaki nie płaczą - IDs from 10 to 12 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Maciej', 'Polish', 'Stuhr');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Cezary', 'Polish', 'Pazura');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Michal', 'Polish', 'Milowicz');

/* The Dark Knight - IDs from 13 to 15 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Christian', 'United Kingdom', 'Bale');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Heath', 'United States of America', 'Ledger');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Aaron', 'United States of America', 'Eckhart');

/* Titanic - IDs from 16 to 18 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Leonardo', 'United States of America', 'DiCaprio');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Kate', 'United Kingdom', 'Winslet');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Billy', 'United States of America', 'Billy');

/* The Lion King - IDs from 19 to 21 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Matthew', 'United States of America', 'Broderick');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Jeremy', 'United Kingdom', 'Irons');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('James Earl', 'United States of America', 'Jones');

/* Alien - IDs from 22 to 24 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Sigourney', 'United States of America', 'Weaver');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Tom', 'United States of America', 'Skerritt');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('John ', 'United Kingdom', 'Hurt ');

/* The Shining - IDs from 25 to 27 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Jack', 'United States of America', 'Nicholson');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Shelley', 'United States of America', 'Duvall');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Danny', 'United States of America', 'Lloyd');

/* Deadpool - IDs from 28 to 30 */
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Ryan', 'Canada', 'Reynolds');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('Morena', 'Brazil', 'Baccarin');
INSERT INTO PUBLIC.ACTOR (NAME, NATIONALITY, SURNAME) VALUES ('T.J.', 'United States of America', 'Miller');

/* Actors to movies */
/* Cars 3 */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (1, 1);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (1, 2);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (1, 3);

/* The Godfather */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (2, 4);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (2, 5);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (2, 6);

/* Toy Story */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (3, 7);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (3, 8);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (3, 9);

/* Chłopaki nie płaczą */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (4, 10);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (4, 11);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (4, 12);

/* The Dark Knight */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (5, 13);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (5, 14);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (5, 15);

/* Titanic */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (6, 16);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (6, 17);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (6, 18);

/* The Lion King */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (7, 19);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (7, 20);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (7, 21);

/* Alien */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (8, 22);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (8, 23);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (8, 24);

/* The Shining */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (9, 25);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (9, 26);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (9, 27);

/* Deadpool */
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (10, 28);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (10, 29);
INSERT INTO PUBLIC.MOVIE_ACTOR (MOVIE_ID, ACTOR_ID) VALUES (10, 30);

/* Ratings */
/* Cars 3 */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 1);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(6, 1);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(5, 1);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 1);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(7, 1);

/* The Godfather */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 2);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 2);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 2);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 2);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(7, 2);

/* Toy Story */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(6, 3);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(6, 3);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 3);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 3);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 3);

/* Chłopaki nie płaczą */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(4, 4);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(6, 4);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 4);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(7, 4);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(7, 4);

/* The Dark Knight */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 5);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 5);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 5);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(2, 5);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(6, 5);

/* Titanic */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(7, 6);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 6);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 6);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 6);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 6);

/* The Lion King */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(7, 7);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(7, 7);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 7);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 7);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 7);

/* Alien */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 8);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 8);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(6, 8);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 8);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 8);

/* The Shining */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 9);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 9);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 9);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(8, 9);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(9, 9);

/* Deadpool */
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 10);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 10);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 10);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(10, 10);
INSERT INTO PUBLIC.RATING (EVALUATION, MOVIE_ID) VALUES(5, 10);

/* Watched movies */
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(2, 1);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(2, 4);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(2, 7);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(3, 9);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(3, 10);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(4, 2);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(4, 5);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(5, 1);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(6, 10);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(6, 4);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(7, 3);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(7, 4);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(8, 1);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(8, 2);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(8, 3);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(8, 4);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(8, 5);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(8, 6);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(9, 5);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(9, 7);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(9, 10);
INSERT INTO PUBLIC.WATCHED_MOVIES (USER_ID, MOVIE_ID) VALUES(10, 6);