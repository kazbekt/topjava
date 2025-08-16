### Get meals by ID (GET)

curl "http://localhost:8080/topjava/rest/profile/meals"

### Get with meals (GET)

curl "http://localhost:8080/topjava/rest/profile/with-meals"

### Get meals date filter (GET)

curl "http://localhost:8080/topjava/rest/profile/meals/filter?startDate=2020-01-30&startTime=10:00:00&endDate=2020-01-31&endTime=13:00:00"

### Delete meal by ID (DELETE)

curl -s -X DELETE "http://localhost:8080/topjava/rest/profile/meals/100005"

### Update meal by ID (PUT)

curl -s -X PUT "http://localhost:8080/topjava/rest/profile/meals/100005" \
-H "Content-Type: application/json" \
-d '{"dateTime":"2020-01-30T07:00","description":"Updated breakfast","calories":200}'

### Create meal (POST)

curl -s -X POST "http://localhost:8080/topjava/rest/profile/meals" \
-H "Content-Type: application/json" \
-d '{"dateTime":"2023-01-30T07:00","description":"New breakfast","calories":200}'