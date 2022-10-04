-- 初期のユーザ作成
CREATE TABLE "users" (
	"id" SERIAL NOT NULL,
	"name" TEXT NOT NULL primary key,
	"password" TEXT NOT NULL,
	"coin" INTEGER NOT NULL
);

INSERT INTO "users" ("name", "password", "coin") VALUES ('zenn', 'zenn', '900');

-- tokenテーブル
CREATE TABLE "tokens" (
	"id" SERIAL NOT NULL primary key,
	"token" TEXT NOT NULL,
	"refresh_token" TEXT NOT NULL
);
INSERT INTO "tokens" ("token", "refresh_token") VALUES ('zenn', 'zennRefresh');
