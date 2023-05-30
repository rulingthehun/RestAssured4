import io.restassured.http.ContentType;
import org.testng.annotations.Test;

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
    }    @Test
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
}
