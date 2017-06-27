package model.data.database;


public interface DBSolutionAgent {

	String fetchSolution(int lvlHash);
	int saveOrUpdateSolution(String lvlName,String Solution, int lvlHash);
}
