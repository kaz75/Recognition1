package recognition1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectFacesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.DetectedFaces;


public class Recognition1_lib {

	VisualRecognition service;
    IamOptions iamOptions = null;
    DetectedFaces result;
    
    public Recognition1_lib() {
 
    	service = new VisualRecognition("2018-03-19");
    	iamOptions = new IamOptions.Builder()
    			.apiKey("")
    			.build();
    	service.setIamCredentials(iamOptions);
}
    
    public void imgfile (File file) {

		DetectFacesOptions detectFacesOptions = null;
		try {
			detectFacesOptions = new DetectFacesOptions.Builder()
			  .imagesFile(new File("img/abe.jpg"))
			  .build();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 result = service.detectFaces(detectFacesOptions).execute();
		System.out.println(result);
    }
		
	public JsonNode getNode()	{
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = null;
		
		try {
			node = mapper.readTree(String.valueOf(result));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return node;
	}
	
	public void keisan (JsonNode node)	{	
		
		int age_min = node.get("images").get(0).get("faces").get(0).get("age").get("min").asInt();
		int age_max = node.get("images").get(0).get("faces").get(0).get("age").get("max").asInt();
		double age_score = node.get("images").get(0).get("faces").get(0).get("age").get("score").doubleValue();
		int gender;
		if(node.get("images").get(0).get("faces").get(0).get("gender").get("gender").toString() == "MALE") {
			gender = 0;
		}else{
			gender = 1;
		}
		double gender_score = node.get("images").get(0).get("faces").get(0).get("gender").get("score").doubleValue();
		MySQL mysql = new MySQL();
		mysql.updateImage(age_min, age_max, age_score, gender, gender_score);
		System.out.println("age_min : " + age_min);
		System.out.println(result);

}
}
