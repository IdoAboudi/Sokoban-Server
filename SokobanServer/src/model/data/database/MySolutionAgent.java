package model.data.database;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class MySolutionAgent implements DBSolutionAgent{

	@Override
	public String fetchSolution(int lvlHash) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/SokobanService/resources/get/" + lvlHash);
		Invocation.Builder builder = target.request();
		Response response = builder.get();
		String solution = response.readEntity(String.class);
		return solution;

	}

	@Override
	public int saveOrUpdateSolution(String lvlName, String solution, int levelHash) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target("http://localhost:8080/SokobanService/resources/save/" + lvlName + "/" + solution);
		Invocation.Builder builder = target.request();
		Response response = builder.get();
		return response.getStatus();
	}


}
