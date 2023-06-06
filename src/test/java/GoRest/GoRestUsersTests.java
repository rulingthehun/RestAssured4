package GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTests {
    int userID;
    User newUser;
    @BeforeClass
    void Setup() {
        baseURI = "https://gorest.co.in/public/v2/users"; // PROD
        //baseURI = "https://test.gorest.co.in/public/v2/users"; // TEST
    }

    public String getRandomName(){
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String getRandomEmail(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@gmail.com";
    }

    @Test (enabled = false)
    public void createUserObject(){
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", getRandomName());
        newUser.put("gender", "male");
        newUser.put("email", getRandomEmail());
        newUser.put("status", "active");

        //başlangıç işlemleri
        //token aldım
        //users JSON ı hazırladım
        int userID =
        given()
                .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                .contentType(ContentType.JSON)
                //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                .body(newUser)
                // üst taraf request özellikleridir : hazırlık işlemleri POSTMAN deki Authorization ve request BODY kısmı
                .log().uri()
                .log().body()
                .when() // request in olduğu nokta    POSTMAN deki SEND butonu
                .post("")//baseURI+parantez içi(http yoksa) respons un oluştuğu nokta
                // CREATE işlemi POST methodu ile çağırıyoruz POSTMAN deki gibi

                // alt taraf response sonrası  POSTMAN deki test penceresi
                .then()
                .log().body()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract().path("id")
                ;
        System.out.println("userID = " + userID);
    }

    @Test
    public void createUserObjectWithObject(){
        newUser = new User();
        newUser.setName(getRandomName());
        newUser.setGender("male");
        newUser.setEmail(getRandomEmail());
        newUser.setStatus("active");
        userID =
                given()
                        .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                        .contentType(ContentType.JSON)
                        //.body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                        .body(newUser)
                        // üst taraf request özellikleridir : hazırlık işlemleri POSTMAN deki Authorization ve request BODY kısmı
                        .log().uri()
                        .log().body()
                        .when() // request in olduğu nokta    POSTMAN deki SEND butonu
                        .post("")//baseURI+parantez içi(http yoksa) respons un oluştuğu nokta
                        // CREATE işlemi POST methodu ile çağırıyoruz POSTMAN deki gibi

                        // alt taraf response sonrası  POSTMAN deki test penceresi
                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.extract().path("id")
                        .extract().jsonPath().getInt("id")
                ;
        System.out.println("userID = " + userID);

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi classa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direkt veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }

    @Test(dependsOnMethods = "createUserObjectWithObject", priority = 1)
    public void getUserByID()
    {
        given()
                .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                .pathParam("userId",userID)
                .log().uri()
                .when()
                .get("/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "createUserObjectWithObject", priority = 2)
    public void updateUserObject()
    {
        //newUser.setName("burak gaznep");

        Map<String,String> updateUser = new HashMap<>();
        updateUser.put("name","burak gaznep");
        given()
                .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                .pathParam("userId",userID)
                .contentType(ContentType.JSON)
                .body(updateUser)
                .log().body()
                .log().uri()
                .when()
                .put("/{userId}")
                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id",equalTo(userID))
        ;
    }

    @Test(dependsOnMethods = "updateUserObject", priority = 3)
    public void deleteUserByID()
    {

        given()
                .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                .pathParam("userId",userID)
                .log().uri()

                .when()
                .delete("/{userId}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deleteUserByID")
    public void deleteUserByIDNegative()
    {
        given()
                .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                .pathParam("userId",userID)
                .log().uri()
                .when()
                .delete("/{userId}")
                .then()
                .log().body()
                .statusCode(404)
        ;
    }

    @Test
    public void getUsers(){
        Response body =
        given()
                .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                .when()
                .get()
                .then()
                //.log().body()
                .statusCode(200)
                .extract().response()
                ;
        // path ve jsonpath farkları
        int idUser3Path = body.path("[2].id"); // tip dönüşümü ototmatik uygun tip verilmeli
        int idUser3JsonPath = body.jsonPath().getInt("[2].id"); // tip dönüşümü kendi içinde yapılabiliyor
        System.out.println("idUser3Path = " + idUser3Path);
        System.out.println("idUser3JsonPath = " + idUser3JsonPath);
        User[] users = body.as(User[].class); // extract.as
        System.out.println("Arrays.toString(users) = " + Arrays.toString(users));
        List<User> userList = body.jsonPath().getList("", User.class); // jsonpath ile list e dönüştürerek alabiliyorum
        System.out.println("userList = " + userList);
    }

    @Test
    public void getUsersV1() {
        Response body =
                given()
                        .header("Authorization", "Bearer 5fc36a3168d6db80f76f15fb65b865206c16ccc7211981f783ca4adad18249b7")
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().response();
        //body.as(), extract.as // tüm gelen response uygun nesneler için tüm classların yapılması gerekiyor.

        List<User> dataUsers = body.jsonPath().getList("data", User.class);
        // JSONPATH bir response içindeki bir parçayı nesneye dönüştürebiliriz.
        System.out.println("dataUsers = " + dataUsers);

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
        // Burada ise(JsonPath) aradaki bir veriyi classa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direkt veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }
}
class User{
    private int id;
    private String name;
    private String gender;
    private String email;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
