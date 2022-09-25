- springboot
- springsecurity
- myBatis
- gradle
- docker, docker-compose
- vue3
- postgreSQL

- 参考サイト
  https://zenn.dev/misaka/books/9734700544990d/viewer/dad770

##### ローカル起動手順

①DB 立ち上げ
docker-compose -f docker-compose.local.yaml up
②springboot 起動
vscode 　 → 　 gradle → bootrun
③vuejs 起動
cd vuejs
npm run serve

##### 本番起動手順

①springboot をビルド
./gradlew build

②docker の起動
cd docker
docker-compose up -d

##### 参考サイト

###### springboot

###### vue3

###### postgreSQL
