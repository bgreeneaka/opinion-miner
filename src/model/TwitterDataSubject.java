package model;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import controller.Controller.CommandListner;
import controller.ICommandListner;
import controller.SimpleChangeManager;
import strategy.DatumBoxAnalysis;
import strategy.ProcessStrategy;
import strategy.ProcessTweetsStrategy;
import strategy.RapidMinerSentimentAnalysis;
import strategy.SentimentStrategy;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import view.Observer;

public class TwitterDataSubject extends SubjectDecorator implements ICommandListner {

	private ArrayList<Observer> observers;
	private Map<String, Observer> observerMap;
	private FileWriter fileWriter;
	private String topic;
	private  File fetchedTweetsCSV;// = "fetchedTweets.csv";
	private Twitter twitterAcc;
	private  ArrayList<SentimentStrategy> analysisStrategyList;
	private ProcessStrategy processStrategy;
	private ArrayList<JSONObject> mongoDataStore;
	private int datumBoxProgressCount;
	private int rapidminerProgressCount;
	private SimpleChangeManager changeManager;
	private String observerToUpdate;

	public TwitterDataSubject(Subject subjectReference,SimpleChangeManager changeManager) {
		// Constructor
		super(subjectReference);
		this.observerMap = new HashMap<String, Observer>();
		this.observers = new ArrayList<Observer>();
		this.mongoDataStore = new ArrayList<JSONObject>();
		this.datumBoxProgressCount=0;
		this.fileWriter = null;
		this.analysisStrategyList = new ArrayList<SentimentStrategy>();
		this.changeManager = changeManager;
		this.fetchedTweetsCSV = new File("fetchedTweets.csv");
		changeManager.register(this);
		buildConfiguration(); // Create build to create a twitter access account
	}

	
	public int getDatumBoxProgressCount() {
		return datumBoxProgressCount;
	}
	
	public int getRapidminerProgressCount() {
		return rapidminerProgressCount;
	}
	
	public void clearProgressCount() {
		this.datumBoxProgressCount=0;
		this.rapidminerProgressCount=0;
	}
	
	public void setProgressCount(boolean countRef){
		if(countRef){
			this.datumBoxProgressCount++;
		}else{
			this.rapidminerProgressCount++;
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic+" exclude:retweets";
	}
	
	public void clearTopic() {
		this.topic = null;
	}

	public void buildConfiguration() {
		// Process for OAuth configuration
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setIncludeMyRetweetEnabled(false);
		cb.setOAuthConsumerKey("lAUjsLcVsEYDfyoyVz8ZLGJEn");
		cb.setOAuthConsumerSecret("1yMPEgEduQnTOR9Vhic8K4DDIr0e4jGDAgHV1vfRNZrVy7wuOJ");
		cb.setOAuthAccessToken("1132014068-3por1LAq9kljhAgotaxwEpPJu6xRYaRRFfKXD3O");
		cb.setOAuthAccessTokenSecret("bohSoky6eklbPsdjpYMDotqYSI5oZB4bpoHeeYpUf8jmM");
		this.twitterAcc = new TwitterFactory(cb.build()).getInstance();
	}

	// This returns the twitter account created might not be needed
	public Twitter getTwitterAcc() {
		return this.twitterAcc;
	}

	public int getTweetCount(){
		return this.mongoDataStore.size();
	}

	public void setTweetStore() {
		try {
			fileWriter = new FileWriter(fetchedTweetsCSV);
			for(JSONObject obj : this.mongoDataStore){
				String tweet = obj.get("processedTweet").toString();
				tweet = tweet.replace(",", " ");
				fileWriter.append(tweet);
				fileWriter.append(",");
				fileWriter.append("\n");
			}
			System.out.println("Fetched Tweet CSV file was created successfully !!!");
		} catch (Exception e) {
			System.out.println("Error in Fetched Tweet CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out
						.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}
		}
	}

	
	public ArrayList<SentimentStrategy> getAnalysisStrategys() {
		return this.analysisStrategyList;
	}

	public void addSentimentAnalysisStrategy(SentimentStrategy analysisStrategy) {	
		this.analysisStrategyList.add(analysisStrategy);
	}
	
	// CAN BE REMOVED
	@Override
	public void registerObserver(Observer observer) {
		// Add observers to subject
		observers.add(observer);
	}

	@Override
	public void registerObserver(Observer o, String ObserverRef) {
		this.observerMap.put(ObserverRef, o);
//		System.out.println("Datum view added to observer Map");
	}
	
	public Map<String, Observer> getObservers(){
		return this.observerMap;
	}


	@Override
	public void removeObserver(Observer observer) {
		// remove observer
		int i = observers.indexOf(observer);
		if (i >= 0) {
			observers.remove(i);
		}
	}


	public String description() {
		// For testing to check if subjects are been decorated
		return "twitter data state added to " + super.description();

	}

	public File getFetchedTweetsCSV() {
		return this.fetchedTweetsCSV;
	}
	

	public ProcessStrategy getProcessStrategy() {
		return processStrategy;
	}

	public void setProcessStrategy(ProcessStrategy processStrategy) {
		this.processStrategy = processStrategy;//move this to main
	}

	public ArrayList<JSONObject> getMongoDataStore() {
		return mongoDataStore;
	}

	public void setMongoDataStore(ArrayList<JSONObject> mongoDataStore) {
		this.mongoDataStore = mongoDataStore;
		notifyObservers();
		///////TEST???????????
//		System.out.println("Set mongo DB activiated");
//		for (JSONObject tweet : mongoDataStore) {
//			System.out.println("in subject");
//			System.out.println(tweet.toString());
//		}
	}
	
	public void clearSubjectDataStore(){
		this.mongoDataStore.clear();
	}
	
	@Override
	public void addCommandListner(CommandListner commandListner) {
		// TODO Auto-generated method stub
		for (Observer o : observers) {
			o.addActionListener(commandListner);
		}
	}
	
	public void notifyObservers() {
//		System.out.println("Changemanager called");
		changeManager.notifyChange(this.observerToUpdate);
	}
	public void hasChanged(String observerRef){
		this.observerToUpdate = observerRef;
		notifyObservers();
//		System.out.println("has changed called = "+this.observerToUpdate);
	}
}
