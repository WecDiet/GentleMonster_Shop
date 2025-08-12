#### Không Thấy Dữ Liệu Trong DB Khi Chạy Docker

- pg_dump -U postgres -d gentlemonsterDB -f gentlemonsterDB.sql
- docker cp gentlemonsterDB.sql gentle_monster_db:/gentlemonsterDB.sql
- docker exec -it gentle_monster_db psql -U postgres -d gentlemonsterDB -f /gentlemonsterDB.sql
