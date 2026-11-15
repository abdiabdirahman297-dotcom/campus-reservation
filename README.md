# Campus Facility & Equipment Reservation System (INT322 Group Assignment 1)

## Files
- `sql/schema.sql`  - creates the database and the reservations table
- `sql/data.sql`    - test records
- `src/main/webapp/booking_form.html` - the booking form
- `src/main/java/com/ajman/campus/ReservationServlet.java` - the servlet

## Run it in NetBeans + MySQL
1. Open MySQL (Workbench or command line) and run `schema.sql`, then `data.sql`.
2. NetBeans: File > Open Project > choose this folder (needs internet the first time to download the MySQL driver).
3. Tools > Servers > Add Server > Apache Tomcat 10 (one time).
4. In ReservationServlet.java change DB_USER and DB_PASS to your MySQL login.
5. Right-click the project > Clean and Build, then Run.
6. Browser opens http://localhost:8080/campus_reservation/

Tomcat 9 or older? Change `jakarta.servlet` to `javax.servlet` in the servlet imports and use
`javax.servlet:javax.servlet-api:4.0.1` in pom.xml.

## Test cases
| Input | Expected |
|---|---|
| Student, Laptop, 2 qty, 3 days | Subtotal 300, Net 300 |
| Faculty, Projector, 2 qty, 5 days | Subtotal 800, Net 680 |
| Name "  ali  " | Saved as "ALI" |
| Quantity "abc", 0 or -5 | Error page, nothing saved |
