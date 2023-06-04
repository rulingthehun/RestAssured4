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
    public void Task2(){
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
    public void Task3(){
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                // output: 203 Non-Authoritative Information
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .body(equalTo("203 Non-Authoritative Information"))
                ;
        //2. Yöntem
        String bodyText =
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .log().body()
                // output: 203 Non-Authoritative Information
                .statusCode(203)
                .contentType(ContentType.TEXT)
                .extract().body().asString()
        ;
        Assert.assertTrue(bodyText.equalsIgnoreCase("203 Non-Authoritative Information"));
    }

    /**
     * Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */
    @Test
    public void Task4(){
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
                ;
    }

    /**
     * Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect response completed status to be false
     */

    @Test
    public void Task5(){
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
                ;
        //2. Yöntem
        Boolean response =
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().path("completed")
        ;
        Assert.assertFalse(response);
    }
}
