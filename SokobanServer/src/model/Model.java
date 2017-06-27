package model;

import java.util.List;

import model.data.database.User;
import model.data.level.Level;

public interface Model {

	public List getScoreByLevel(String levelName);
	public List getScoreByUser(String username);
	public void saveSession(Level lvl,User user);
	public void addClient(String cAddr);
	public void deleteClient(String cAddr);
	public void deleteAllClients();
	public int getNumOfClients();
	public List<String> getConnectedClients();
	public String getSolution(Level lvl);
	public void close();
	
}
