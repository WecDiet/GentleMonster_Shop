#### Không Thấy Dữ Liệu Trong DB Khi Chạy Docker

- pg_dump -U postgres -d gentlemonsterDB -f gentlemonsterDB.sql
- docker cp gentlemonsterDB.sql gentle_monster_db:/gentlemonsterDB.sql
- docker exec -it gentle_monster_db psql -U postgres -d gentlemonsterDB -f /gentlemonsterDB.sql

Câu lệnh docker

- docker build -t wecdiet/gentle_monster:v1.0.0 . : dùng để build image và tagname
- docker-compose up -d --build : sẽ build lại toàn bộ service trong docker compose
- docker-compose up -d --build + "service chỉ định": build lại riêng service chỉ định
- docker-compose down -v : tắt tất cả các container đang chạy và xóa tất cả container
- docker volume ls : liệt kê các volume

#### VÍ DỤ

- docker run --name some-postgres -e POSTGRES_PASSWORD=123456 -p 5431:5432 -d postgres:16

  --> -e (environment): Dùng để truyền biến môi trường (environment variable) vào container.
  --> -d (detached mode): Container sẽ chạy ngầm trong nền (background), không chiếm terminal.
  --> postgres: tên image mà Docker sẽ chạy container.
  --> :16 : sẽ là version của database postgres.
  --> some-postgres: tên container mà bạn đặt cho PostgreSQL.
  --> -P (port): là cổng ứng dụng chạy bên trong container.
  --> 5431:5432
  => 5431: là cổng để dùng kết nối từ bên ngoài (có thể thay đổi tùy ý).
  => 5432: là cổng mặc định của PosgreSQL (Ngoài ra: MySQL = 3306, Nginx = 80,...)

#### CÂU LỆNH ĐỂ KIỂM TRA CÓ KẾT NỐI VỚI REDIS

- Nhập câu lệnh: docker exec -it gentle_monster_redis redis-cli
  --> gentle_monster_redis: là tên của container redis đang chạy trong docker
  --> redis-cli: kết nối tới Redis server qua giao thức RESP trên cổng mặc định 6379 (hoặc cổng tùy chỉnh).
  ===> Sau khi nhập lên cmd: docker exec -it gentle_monster_redis redis-cli
  > > > > > Gõ từ: PING ==> PONG thì REDIS KẾT NỐI THÀNH CÔNG
