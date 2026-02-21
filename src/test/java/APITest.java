import java.time.LocalDate;

import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class APITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:9998/tasks-backend";
    }
    

    @Test
    public void deveRetornarTarefas() {
        System.out.println("deveRetornarTarefas");

        RestAssured.given()
        .when()
            .get("/todo")
        .then()
            .log().ifValidationFails()
            .statusCode(200);
    }


    @Test
    public void conexaoComAPI() {
        System.out.println("conexaoComAPI");
        RestAssured.given()
        .when()
            .get("/todo")
        .then()
            .statusCode(200);
    }



    @Test
    public void deveAdicionarTarefaComSucesso() {
    	
    	System.out.println("deveAdicionarTarefaComSucesso");
        String future = LocalDate.now().plusYears(15).toString();

        RestAssured.given()
            .body("{\"task\":\"Teste via API\",\"dueDate\":\"" + future + "\"}")
            .contentType(ContentType.JSON)
        .when()
            .post("/todo")
        .then()
            .log().all()
            .statusCode(201);
    }

    @Test
    public void naoDeveAdicionarTarefaInvalida() {
    	
    	System.out.println("naoDeveAdicionarTarefaInvalida");
    	
        String past = LocalDate.now().minusDays(1).toString();

        RestAssured.given()
            .body("{\"task\":\"Teste via API\",\"dueDate\":\"" + past + "\"}")
            .contentType(ContentType.JSON)
        .when()
            .post("/todo")
        .then()
            .log().all()
            .statusCode(400)
            .body("message", CoreMatchers.is("Due date must not be in past"));
    }
    
    @Test
    public void deveRemoverTarefaComSucesso() {
    	
    	String future = LocalDate.now().plusYears(15).toString();
    	
    	 Integer id = RestAssured.given()
         .body("{\"task\":\"Teste via API Diego\",\"dueDate\":\"" + future + "\"}")
         .contentType(ContentType.JSON)
     .when()
         .post("/todo")
     .then()
         .log().all()
         .statusCode(201)
         .extract().path("id");
    	 
    	 
    	 RestAssured.given()
    	 	.when()
    	 		.delete("/todo/" + id)
    	 	.then()
    	 		.log().all()
    	 	.statusCode(204);
    }
    
}
