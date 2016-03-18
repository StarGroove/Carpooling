package covoiturage;

public class Step {


	private String name;
	private int nbPassenger;
	private int price;
	private Person[] passenger;
	private float time;
	
	
	public Step(String name, int nbPassenger, int price, Person[] passenger) {
		super();
		this.name = name;
		this.nbPassenger = nbPassenger;
		this.price = price;
		this.passenger = passenger;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNbPassenger() {
		return nbPassenger;
	}

	public void setNbPassenger(int nbPassenger) {
		this.nbPassenger = nbPassenger;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Person[] getPassenger() {
		return passenger;
	}

	public void setPassenger(Person[] passenger) {
		this.passenger = passenger;
	}


	public float getTime() {
		return time;
	}


	public void setTime(float time) {
		this.time = time;
	}	
	
}
