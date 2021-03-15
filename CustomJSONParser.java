// example with 2 types of read shown
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Reader;
import org.json.simple.*;
import org.json.simple.parser.*;

// note: to compile and run this code, you will need to tell java how to find the org.json.simple package. There is a jar file in the local directory which has the package. So we will update the CLASSPATH variable through the terminal to show Java where to look for imported classes.
// in bash, this is done with the following line
// export CLASSPATH=$CLASSPATH:./org-json-simple.jar
// after running that line, you will be able to compile and run.
public class CustomJSONParser {
	
	public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = rd.read()) != -1) {
			sb.append((char) cp);
		} // end of while
		
		return sb.toString();
	}	// end of method
	

	public static JSONArray readJsonFromURL(String urlString) throws IOException, Exception {
		JSONObject json = null;
		URL url = new URL(urlString);
		InputStream is = url.openStream();
		JSONArray jsonArr = null;
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader rd = new BufferedReader(isr);
			String jsonText = readAll(rd); 
			
			Object obj = new JSONParser().parse(jsonText);
			jsonArr = (JSONArray) obj;

			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			is.close();
		}
		return jsonArr;
	}	
	

	
	public static void main(String[] args) throws Exception {
		String urlString = "http://hp-api.herokuapp.com/api/characters/";
		JSONArray jsa = readJsonFromURL(urlString);
		//Print out all the information in the API
		for(int i = 0;i<jsa.size();i++){
			JSONObject json = (JSONObject)jsa.get(i);
			System.out.println(json.toString());
		}
		//Search for the information of a specific character
		String info = null;
		for(int i = 0;i<jsa.size();i++){
			JSONObject json = (JSONObject)jsa.get(i);
			
			if(json.get("name").equals("Harry Potter")){
				info="name: " + json.get("name")+" house: " + 
				json.get("house") + " hairColour: " + json.get("hairColour") + " date of birth: " +  json.get("dateOfBirth") + "\n";
				System.out.print(info);
			}
		}
		
	}	
}
