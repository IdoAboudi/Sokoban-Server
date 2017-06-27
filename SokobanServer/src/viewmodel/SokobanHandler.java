package viewmodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import model.Model;
import model.data.database.DataBaseManager;
import model.data.database.User;
import model.data.level.Level;
import solver.SokobanSolver;

public class SokobanHandler implements ClientHandler {

	SokobanSolver solver;
	BufferedReader reader;
	PrintWriter writer;
	ObjectInputStream objectReader;
	ObjectOutputStream objectWriter;
	Model model;

	//remove solution and dbm and leave model alone
	//complete more functions if it works ! 
	public SokobanHandler(Model model){
		this.model = model;
	}

	@Override
	public void handle(InputStream in, OutputStream out,String cAddr) throws IOException, ClassNotFoundException{
		
		writer = new PrintWriter(out,true);
		reader = new BufferedReader(new InputStreamReader(in));
		String line;
		List table;
		User user;
		Level level;
			objectWriter = new ObjectOutputStream(out);
			objectReader = new ObjectInputStream(in);

			while((line=reader.readLine())!=null){
				if(!line.equals("close")){
					String[] words = line.split(" ");
					switch(words[0]){
					case "LevelSessionTable":
						table = (List) model.getScoreByLevel(words[1]);
						objectWriter.writeObject(table);
						objectWriter.flush();
						break;
					case "UserSessionTable":
						table =(List) model.getScoreByUser(words[1]);
						objectWriter.writeObject(table);
						objectWriter.flush();
						break;
					case "SaveSession":
						user = (User) objectReader.readObject();
						level = (Level) objectReader.readObject();
						if(user != null && level != null)
							model.saveSession(level, user);
						break;
//					case "addUser":
//						user = (User) objectReader.readObject();
//						if(user != null)
//							dbm.addUser(user);
//						break;
//					case "addLevel":
//						level = (Level) objectReader.readObject();
//						if(level != null)
//							dbm.addLevel(level);
//						break;
//					case "deleteUser":
//						int userId = Integer.parseInt(words[1]);
//						dbm.deleteUser(userId);
//						break;
//					case "deleteLevel":
//						String levelName = words[1];
//						dbm.deleteLevel(levelName);
//						break;
					case "getSolution":
						level = (Level) objectReader.readObject();
						//should run this in a new thread
						String solution = model.getSolution(level);
						writer.println(solution);
					default:break;
					}

				}
				//"close" was received
				else{
					model.deleteClient(cAddr);
					closeConnections();
					break;
				}
			}
	}

	private void closeConnections(){
		try {

			if(writer != null)
				writer.close();
			if(reader != null)
				reader.close();
			if(objectReader!=null)
				objectReader.close();
			if(objectWriter!=null)
				objectWriter.close();
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
