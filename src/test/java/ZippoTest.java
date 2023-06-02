import POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ZippoTest {
    @Test
    public void Test() {
        given()
                // hazırlık işlemlerini yapacağız (token,send body, parametreler)
                .when()
                // link i ve metodu veriyoruz
                .then()
        //  assertion ve verileri ele alma extract
        ;
    }

    @Test
    public void statusCodeTest() {
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün response u gösterir
                .statusCode(200)
        ;
    }

    @Test
    public void contentTypeTest() {
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün response u gösterir
                .statusCode(200)
                .contentType(ContentType.JSON) //dönen sonuç JSON tipinde mi
        ;
    }

    @Test
    public void checkCountryInResponseBody() {
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün response u gösterir
                .statusCode(200)
                .body("country", equalTo("United States"))
        ;
    }

    @Test
    public void checkStateInResponseBody() {
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün response u gösterir
                .statusCode(200)
                .body("places[0].state", equalTo("California"))
        ;
    }

    @Test
    public void bodyJsonPathTest3() {
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/tr/01000")
                .then()
                .log().body() // log().all() bütün response u gösterir
                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü")) // verile path de ki liste bu item e sahip mi, contains
        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün response u gösterir
                .statusCode(200)
                .body("places", hasSize(1)) //place in size 1 e eşit mi
        ;
    }

    @Test
    public void combiningTest() {
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body() // log().all() bütün response u gösterir
                .statusCode(200)
                .body("places", hasSize(1)) //place in size 1 e eşit mi
                .body("places.state", hasItem("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
        ;
    }

    @Test
    public void pathParamTest() {
        given()//isteğin özellikler
                .pathParam("Country", "us")
                .pathParam("ZipKode", 90210)
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{Country}/{ZipKode}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test
    public void pathParamTest2() {
        for (int i = 90210; i < 90214; i++) {
            // 90210 dan 90213 kadar test sonuçlarında places in size nın hepsinde 1 gediğini test ediniz.
            given()//isteğin özellikler
                    .pathParam("Country", "us")
                    .pathParam("ZipKode", i)
                    .log().uri()
                    .when()
                    .get("http://api.zippopotam.us/{Country}/{ZipKode}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("places", hasSize(1))
            ;
        }
    }

    @Test
    public void queryParamTest() {
        // https://gorest.co.in/public/v1/users?page=3
        given()//isteğin özellikler
                .param("page", 1)// ?page=1 şeklinde linke ekleniyor
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .statusCode(200)
                .body("meta.pagination.page", equalTo(1))
        ;
    }

    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response da ki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
        for (int pageNo = 1; pageNo < 11; pageNo++) {
            given()//isteğin özellikler
                    .param("page", pageNo)// ?page=1 şeklinde linke ekleniyor
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("meta.pagination.page", equalTo(pageNo))
            ;
        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    @BeforeClass
    void Setup(){

        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .log(LogDetail.URI)
                .setContentType(ContentType.JSON)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    public void requestResponseSpecification() {
        // https://gorest.co.in/public/v1/users?page=3
        given()//isteğin özellikler
                .param("page", 1)// ?page=1 şeklinde linke ekleniyor
                .spec(requestSpec)
                .when()
                .get("/users")
                .then()
                .body("meta.pagination.page", equalTo(1))
                .spec(responseSpec)
        ;
    }

    //Json extract
    @Test
    public void extractingJsonPath(){
        // String placeName=given().when().get("http://api.zippopotam.us/us/90210").then().statusCode(200).extract().path("places[0].'place name'");
        String placeName=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
                .log().body()
                .extract().path("places[0].'place name'")
                // extract methodu ile given ile başlayan satır,
                // bir değer döndürür hale geldi, en sonda extract olmalı
                ;
        System.out.println("placeName = " + placeName);
    }

    @Test
    public void extractingJsonPathInt() {
        // alınacak tipin değerine en uygun olan karşılıktaki tip yazılır
        int limit=
        given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("meta.pagination.limit")
                ;
        System.out.println("limit = " + limit);
        Assert.assertEquals(limit, 10, "Test result wrong");

    }

    @Test
    public void extractingJsonPathList() {
        // alınacak tipin değerine en uygun olan karşılıktaki tip yazılır
        List<Integer> ids=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.id")
                ;
        System.out.println("ids = " + ids);
        Assert.assertTrue(ids.contains(2368369));

    }

    @Test
    public void extractingJsonPathStringList() {
        // alınacak tipin değerine en uygun olan karşılıktaki tip yazılır
        List<String> names=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().path("data.name")
                ;
        System.out.println("names = " + names);
        Assert.assertTrue(names.contains("Nawal Khanna"));
    }


    @Test
    public void extractingJsonPathResponseAll() {
        // alınacak tipin değerine en uygun olan karşılıktaki tip yazılır
        Response response=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().response() // bütün body alındı  //.log().body() gözüken body
                ;
        List<Integer> ids = response.path("data.id");
        List<String> names = response.path("data.name");
        int limit = response.path("meta.pagination.limit");

        System.out.println("response = " + response.prettyPrint());
        System.out.println("ids = " + ids);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(names.contains("Nawal Khanna"));
        Assert.assertTrue(ids.contains(2368369));
        Assert.assertEquals(limit, 10, "Test result wrong");
    }

    @Test
    public void extractingJsonPOJO(){
        // POJO (Plain Old Java Object)
        // Json Object

//        Ogrenci ogr=new Ogrenci();
//        ogr.id=1;
//        ogr.isim="ismet temur";
//        ogr.tel="3434343";
//
//        System.out.println("ogr = " + ogr.tel);
        
        Location place=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .extract().as(Location.class) // Location şablonuna çıkart
                ;
        System.out.println("place.getPostCode() = " + place.getPostCode());
        System.out.println("place.getPlaces().get(0).getPlaceName() = " +
                place.getPlaces().get(0).getPlaceName());
    }

}
