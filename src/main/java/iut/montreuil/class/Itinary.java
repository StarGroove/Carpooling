package covoiturage;

import java.util.*;

public class Itinary {


	private String locationStart;
	private String locationEnd;
	private Driver driver;
	private Car car;
	private List<Step> listStep;
	private int nbStep;
	private Date datedep;
	
	public Itinary(String locationDep, String locationArriv, Driver driver, Car car, List<Step> step, int nbStep,
			Date datedep) {
		this.locationStart = locationDep;
		this.locationEnd = locationArriv;
		this.driver = driver;
		this.car = car;
		this.listStep = step;
		this.nbStep = nbStep;
		this.datedep = datedep;
	}
	
	
	public String getLocationDep() {
		return locationStart;
	}

	public void setLocationDep(String locationDep) {
		this.locationStart = locationDep;
	}

	public String getLocationArriv() {
		return locationEnd;
	}

	public void setLocationArriv(String locationArriv) {
		this.locationEnd = locationArriv;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public List<Step>  getStep() {
		return listStep;
	}

	public void setStep(List<Step> step) {
		this.listStep = step;
	}

	public int getNbStep() {
		return nbStep;
	}

	public void setNbStep(int nbStep) {
		this.nbStep = nbStep;
	}

	
	public float priceStep(Person person ,Step step)
	{
		Person[] passenger = step.getPassenger();
		for (int i = 0; i < step.getNbPassenger() ; i++)
		{
			if(passenger[i] == person)
			{
				return step.getPrice();
			}
		}
		return 0;
	}
	
	public float priceItinary(Person person, Step[] step)
	{
		float totalPrice = 0;
		for (int i = 0; i < this.getNbStep() ; i++)
		{
			totalPrice += this.priceStep(person, step[i]);
		}
		return totalPrice;
		
	}

	public int calculTime(Step[] step)
	{
		int timeTotal = 0;
		for (int i = 0; i < this.getNbStep(); i++)
		{
			timeTotal +=step[i].getTime();
		}
		return timeTotal; 
	}
	
	public Date dateArriv(Step[] step)
	{
		Calendar cal = Calendar.getInstance();
		Date dateArriv = this.datedep;		
		cal.setTime(dateArriv);
		
		cal.add(Calendar.MINUTE,calculTime(step));
			
		return dateArriv;
	}
	
	
	public List<Step> lSteps(List<Step> lSteps, Step step)
	{	
		lSteps.add(step);
		return lSteps;
	}
	
	
}
