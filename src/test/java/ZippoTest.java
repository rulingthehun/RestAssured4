import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

public class ZippoTest {
    @Test
    public void Test(){
        given()
                // hazırlık işlemlerini yapacağız (token,send body, parametreler)
                .when()
                // link i ve metodu veriyoruz
                .then()
                //  assertion ve verileri ele alma extract
        ;
    }
    @Test
    public void statusCodeTest(){
        given()//isteğin özellikler
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

}
