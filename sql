-- 初期のユーザ作成
DROP TABLE IF EXISTS users;
CREATE TABLE "users" (
	"id" SERIAL NOT NULL primary key,
	"name" VARCHAR(255) NOT NULL,
	"password" VARCHAR(255) NOT NULL,
	"email" VARCHAR(255) NOT NULL,
	"role" VARCHAR(255) NOT NULL
);

INSERT INTO "users" ("name", "password","email","role") VALUES ('zenn', 'zenn','email','role');
INSERT INTO "users" ("name", "password","email","role") VALUES ('zenn2', 'zenn2','email2','role');
INSERT INTO "users" ("name", "password","email","role") VALUES ('zenn3', 'zenn3','email3','role');

-- tokenテーブル
DROP TABLE IF EXISTS tokens;
CREATE TABLE "tokens" (
	"id" SERIAL NOT NULL primary key,
	"token" TEXT NOT NULL,
	"refresh_token" TEXT NOT NULL
);


--勤怠テーブル
DROP TABLE IF EXISTS "attendances";
CREATE TABLE "attendances" (
	"id" SERIAL NOT NULL primary key,
	"user_id" INTEGER NOT NULL,
	"start_at" DATE default CURRENT_TIMESTAMP,
	"finish_at" DATE default CURRENT_TIMESTAMP,
	"comment" TEXT DEFAULT NULL
);

INSERT INTO "attendances" ("user_id", "comment") VALUES ('1', 'testComment');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('1', 'testComment2');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('1', 'testComment2');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('1', 'testComment3');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('1', 'testComment4');

INSERT INTO "attendances" ("user_id", "comment") VALUES ('2', 'testComment');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('2', 'testComment2');


INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment2');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment2');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment3');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment4');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment5');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment6');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment7');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment8');
INSERT INTO "attendances" ("user_id", "comment") VALUES ('3', 'testComment9');

--タグテーブル
DROP TABLE IF EXISTS "tags";
CREATE TABLE "tags" (
	"id" SERIAL NOT NULL primary key,
	"tag_name" VARCHAR(255) NOT NULL
);

INSERT INTO "tags" ("tag_name") VALUES ('first tag');
INSERT INTO "tags" ("tag_name") VALUES ('second tag');
INSERT INTO "tags" ("tag_name") VALUES ('third tag');
INSERT INTO "tags" ("tag_name") VALUES ('forth tag');

--タグ・勤怠中間テーブル
DROP TABLE IF EXISTS "tag_attendances";
CREATE TABLE "tag_attendances" (
	"id" SERIAL NOT NULL primary key,
	"attendance_id" INTEGER NOT NULL,
	"tag_id" INTEGER NOT NULL
);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (1,1);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (2,1);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (3,1);

INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (1,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (2,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (3,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (4,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (5,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (6,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (7,2);
INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (8,2);


INSERT INTO "tag_attendances" ("attendance_id","tag_id") VALUES (1,3);

