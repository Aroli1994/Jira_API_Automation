package demopackage;

import static io.restassured.RestAssured.given;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class BugTest {

	public static void main(String[] args) {

		RestAssured.baseURI = "https://vk1062986.atlassian.net/";	
		
		//CreateIssue - POST
		String createIssueResponse = given()
		.header("Content-Type", "Application/json")
		.header("Authorization", "Basic dmsxMDYyOTg2QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBydllUZFlIeXVId2l6QTh3V3VISFVaTHFvWnEtSWhkWjM5WHc2Z1VGUVBuTTZTUjRYbnRVOEtMTHVBbHZRLUY5bnNFblpJNFlaVDJwWUNTOTJHYTBkU2YxRnlVNUV1RmhQdllDaEZwMlFJRk14REJ5S1ZIeFFvUXh5aUZ4WElsQ2twRGRoa0dxSFVtWnZTMWtFT2d6dlNqZklIMHNTakFlZmd3NWYzWWhjYm89MzYwMjcxNEU=")
		.body("{\n"
				+ "    \"fields\": {\n"
				+ "       \"project\":\n"
				+ "       {\n"
				+ "          \"key\": \"SCRUM\"\n"
				+ "       },\n"
				+ "       \"summary\": \"Radio buttons are not working - automation\",\n"
				+ "       \"issuetype\": {\n"
				+ "          \"name\": \"Bug\"\n"
				+ "       }\n"
				+ "   }\n"
				+ "}\n"
				+ "").log().all()
		.post("rest/api/3/issue")
		.then().log().all().assertThat().statusCode(201)
		.extract().response().asString();
		
		JsonPath js = new JsonPath(createIssueResponse);
		String issueId = js.getString("id");
		System.out.println("issueId = " + issueId);
		
		//GetIssue - GET
		String getIssueResponse = given().log().all()
		.header("Authorization", "Basic dmsxMDYyOTg2QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBydllUZFlIeXVId2l6QTh3V3VISFVaTHFvWnEtSWhkWjM5WHc2Z1VGUVBuTTZTUjRYbnRVOEtMTHVBbHZRLUY5bnNFblpJNFlaVDJwWUNTOTJHYTBkU2YxRnlVNUV1RmhQdllDaEZwMlFJRk14REJ5S1ZIeFFvUXh5aUZ4WElsQ2twRGRoa0dxSFVtWnZTMWtFT2d6dlNqZklIMHNTakFlZmd3NWYzWWhjYm89MzYwMjcxNEU=")
		.when().get("rest/api/3/issue/" + issueId + "")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath js1 = new JsonPath(getIssueResponse);
		String issueSummary = js1.getString("fields.summary");
		System.out.println("Issue Summary = " + issueSummary);
		
		//AddScreenshot/ AddAttachment - POST
		given()
		.pathParam("key", issueId)
		.header("Authorization", "Basic dmsxMDYyOTg2QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBydllUZFlIeXVId2l6QTh3V3VISFVaTHFvWnEtSWhkWjM5WHc2Z1VGUVBuTTZTUjRYbnRVOEtMTHVBbHZRLUY5bnNFblpJNFlaVDJwWUNTOTJHYTBkU2YxRnlVNUV1RmhQdllDaEZwMlFJRk14REJ5S1ZIeFFvUXh5aUZ4WElsQ2twRGRoa0dxSFVtWnZTMWtFT2d6dlNqZklIMHNTakFlZmd3NWYzWWhjYm89MzYwMjcxNEU=")
		.header("X-Atlassian-Token", "no-check")
		.multiPart("file", new File(System.getProperty("user.dir" ) + "//Actual result.png"))
		.when().post("rest/api/3/issue/{key}/attachments")
		.then().log().all().assertThat().statusCode(200);
		
	}
}
