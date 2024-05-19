CREATE TABLE `Patient` (
                           `id`	BIGINT	NOT NULL,
                           `username`	varchar(30),
                           `password`	varchar(255),
                           `role`	varchar(50),
                           `name`	varchar(50),
                           `gender`	varchar(20),
                           `birth_date`   DATE,
                           `phone_number`	varchar(20),
                           `email`	varchar(255),
                           `address`	varchar(255),
                           `created_at`	DATETIME,
                           `modified_at`	DATETIME
);

CREATE TABLE `Doctor` (
                          `id`	BIGINT	NOT NULL,
                          `username`	varchar(30),
                          `password`	varchar(255),
                          `role`	varchar(50),
                          `name`	varchar(50),
                          `phone_number`	varchar(20),
                          `email`	varchar(255),
                          `bio`	text,
                          `created_at`	DATETIME,
                          `modified_at`	DATETIME,
                          `specialty_id` BIGINT	NOT NULL,
                          `hospital_id`	BIGINT	NOT NULL
);

CREATE TABLE `Staff` (
                         `id`	BIGINT	NOT NULL,
                         `username`	varchar(30),
                         `password`	varchar(255),
                         `role`	varchar(50),
                         `name`	varchar(50),
                         `phone_number`	varchar(20),
                         `email`	varchar(255),
                         `created_at`	DATETIME,
                         `modified_at`	DATETIME,
                         `hospital_id`	BIGINT	NOT NULL
);

CREATE TABLE `Hospital` (
                            `id`	BIGINT	NOT NULL,
                            `name`	varchar(50),
                            `address`	varchar(255),
                            `latitude`	DOUBLE,
                            `longitude`	DOUBLE,
                            `contact_number`  varchar(20),
                            `description`	text
);

CREATE TABLE `Specialty` (
                             `id`	BIGINT	NOT NULL,
                             `name`	varchar(50)
);

CREATE TABLE `Appointment` (
                               `id`	BIGINT	NOT NULL,
                               `appointment_time`	DATETIME,
                               `status`	varchar(50),
                               `created_at`	DATETIME,
                               `modified_at`	DATETIME,
                               `patient_id`	BIGINT	NOT NULL,
                               `doctor_id`	BIGINT	NOT NULL,
                               `hospital_id`	BIGINT	NOT NULL
);

CREATE TABLE `MedicalRecord` (
                                 `id`	BIGINT	NOT NULL,
                                 `date`	DATE,
                                 `diagnosis`	varchar(255),
                                 `treatment`	text,
                                 `prescription`	text,
                                 `patient_id`	BIGINT	NOT NULL,
                                 `doctor_id`	BIGINT	NOT NULL
);

ALTER TABLE `Patient` ADD CONSTRAINT `PK_PATIENT` PRIMARY KEY (
                                                               `id`
    );

ALTER TABLE `Doctor` ADD CONSTRAINT `PK_DOCTOR` PRIMARY KEY (
                                                             `id`
    );

ALTER TABLE `Staff` ADD CONSTRAINT `PK_STAFF` PRIMARY KEY (
                                                           `id`
    );

ALTER TABLE `Hospital` ADD CONSTRAINT `PK_HOSPITAL` PRIMARY KEY (
                                                                 `id`
    );

ALTER TABLE `Specialty` ADD CONSTRAINT `PK_SPECIALTY` PRIMARY KEY (
                                                                   `id`
    );

ALTER TABLE `Appointment` ADD CONSTRAINT `PK_APPOINTMENT` PRIMARY KEY (
                                                                       `id`
    );

ALTER TABLE `MedicalRecord` ADD CONSTRAINT `PK_MEDICALRECORD` PRIMARY KEY (
                                                                           `id`
    );
