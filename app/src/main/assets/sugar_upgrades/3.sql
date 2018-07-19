-- Version 3: Adding number of workouts column to the week table

alter table week add numberOfWorkouts integer default 0;