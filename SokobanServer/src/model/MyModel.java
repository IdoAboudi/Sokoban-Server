package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javafx.application.Platform;
import model.data.database.DBSolutionAgent;
import model.data.database.DataBaseManager;
import model.data.database.SolutionEncoder;
import model.data.database.User;
import model.data.level.Level;
import solver.SokobanSolver;
import strips.Action;

public class MyModel extends Observable implements Model {

	DBSolutionAgent agent;
	SokobanSolver solver;
	DataBaseManager dbm;
	List<String> clients;
	int numOfClients = 0;


	public MyModel(DBSolutionAgent agent,SokobanSolver solver,DataBaseManager dbm){
		
		this.agent = agent;
		this.solver = solver;
		this.dbm = dbm;
		this.clients = new ArrayList<String>();
	}


	@Override
	public List getScoreByLevel(String levelName) {
		return dbm.getGameSessionTableForLevel(levelName);
	}


	@Override
	public List getScoreByUser(String username) {
		return dbm.getGameSessionTableForUser(username);
	}


	@Override
	public void saveSession(Level lvl, User user) {
		dbm.saveUserAndLevel(user, lvl);
	}


	@Override
	public String getSolution(Level lvl) {

		String solution = agent.fetchSolution(lvl.hashCode());
		String compressed = "";
		if(solution.isEmpty()){//no solution in the database
			List<Action> actions = solver.solve(lvl);
			if(actions != null){//successfully solved the level
				List<String> actionNames = new ArrayList<>();
				for(Action a : actions)
					actionNames.add(a.getType());
				compressed = SolutionEncoder.compress(actionNames);
				agent.saveOrUpdateSolution(lvl.getLevelName(), compressed,lvl.hashCode());
				return compressed;
			}//failed solving the level
			else return null;
		}
		//the saved solution from the database
		else return solution;
	}


	@Override
	public void addClient(String cAddr) {
		if(clients == null)
			clients = new ArrayList<String>();
		clients.add(cAddr);
		numOfClients = clients.size();
		setChanged();
		notifyObservers();
	}


	@Override
	public void deleteClient(String cAddr) {
		if(clients == null)
			return;
		clients.remove(cAddr);
		numOfClients = clients.size();
		setChanged();
		notifyObservers();
	}


	@Override
	public int getNumOfClients() {
		return numOfClients;
	}
	
	@Override
	public List<String> getConnectedClients(){
		return clients;
	}
	
	@Override
	public void close(){
		dbm.closeDB();
	}


	@Override
	public void deleteAllClients() {
		Platform.runLater(()->{
			clients.clear();
			numOfClients = clients.size();
		});
		setChanged();
		notifyObservers();
	}
}