package command;

import java.awt.Color;
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
		this.setFont(new Font("Gotham Medium", Font.PLAIN, 20));
		this.setForeground(new Color(0, 132, 180));
		this.subjectRef = subject;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
			if (!(((TwitterDataSubject) this.subjectRef).getMongoDataStore().isEmpty())){
		System.out.println("Update button pressed");

		System.out.println("Mongo data store size ="+((TwitterDataSubject)this.subjectRef).getMongoDataStore().size());

		ArrayList<JSONObject> mongoResults = ((TwitterDataSubject)this.subjectRef).getMongoDataStore();

		((TwitterDataSubject)this.subjectRef).hasChanged("elavView");
		((TwitterDataSubject)this.subjectRef).hasChanged("chartView");
		((TwitterDataSubject)this.subjectRef).hasChanged("cloudView");
		((TwitterDataSubject)this.subjectRef).setMongoDataStore(mongoResults);
		for(JSONObject obj :mongoResults ){
			System.out.println(obj.toString());
		}
			}else{
			
			JOptionPane.showMessageDialog(null,"Error", "Evaluation.",JOptionPane.ERROR_MESSAGE);
		}
	}

}
