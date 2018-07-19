-- Version 2: Adding average steps column to the week table

alter table week add avgSteps integer default 0;