package command;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;

import model.Subject;
import model.TwitterDataSubject;

public class BtnEvaluateMongoResults extends JButton implements Command {
	
	private Subject subjectRef;
	
	public BtnEvaluateMongoResults(String caption, Subject subject) {
		// TODO Auto-generated constructor stub
		super(caption);
		this.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		this.subjectRef = subject;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		//((TwitterDataSubject)this.subjectRef).setMongoDataStore();
			if (!(((TwitterDataSubject) this.subjectRef).getMongoDataStore().isEmpty())){
		System.out.println("Update button pressed");
		System.out.println("Rapidminer results size ="+((TwitterDataSubject)this.subjectRef).getDatumResultsJSON().size());
		System.out.println("Mongo data store size ="+((TwitterDataSubject)this.subjectRef).getMongoDataStore().size());
		System.out.println("Datumresults size ="+((TwitterDataSubject)this.subjectRef).getDatumResultsJSON().size());
		ArrayList<JSONObject> mongoResults = ((TwitterDataSubject)this.subjectRef).getMongoDataStore();
		//JSONObject mongoData = new JSONObject();
		int mongoSize=((TwitterDataSubject)this.subjectRef).getMongoDataStore().size();

		for(int i = 0; i<mongoSize; i++){	
			JSONObject mongoData = new JSONObject();
			mongoResults.get(i).put("rapidMinerResults",((TwitterDataSubject)this.subjectRef).getRapidResultsJSON().get(i).get("RapidResult"));
//			mongoResults.get(i).put("datumResults",((TwitterDataSubject)this.subjectRef).getDatumResultsJSON().get(i).get("result"));
			//mongoResults.get(i).put("tweet",((TwitterDataSubject)this.subjectRef).getDatumResultsJSON().get(i).get("tweet"));
			//mongoResults.add(mongoData);		
		}
		//((TwitterDataSubject)this.subjectRef).setMongoDataStore(mongoResults);
		((TwitterDataSubject)this.subjectRef).notifyEvaluation();
		for(JSONObject obj :mongoResults ){
			System.out.println(obj.toString());
		}
			}else{
			
			JOptionPane.showMessageDialog(null,"Error", "Evaluation.",JOptionPane.ERROR_MESSAGE);
		}
	}

}
