import TasksPOJO.User;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Tasks {
    /** Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */
    @Test
    public void taskPOJO(){
        User user =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().as(User.class)
                ;
        System.out.println("user = " + user);
    }

    /**
     * Task 2
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */

    @Test
    public void task2(){
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                .statusCode(203)
                // Postman code:
                //        pm.test("Status code is 200", function () {
                //        pm.response.to.have.status(200);
                //    });
                .contentType(ContentType.TEXT)
                ;
    }

    /**
     * Task 3
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     * expect BODY to be equal to "203 Non-Authoritative Information"
     */

    @Test
    public void task3(){
        String output =
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                // output: 203 Non-Authoritative Information
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .extract().asString()
                ;
        System.out.println("output = " + output);
    }
}
