/**
 * 
 */
package games.Catan;

/**
 * @author
 *
 */
public class Tile {

	private String resource;
	private int dicenum;
	private double value;
	private Crossroad[] crossroads;
	private int orientation;
	private boolean thief = false;
	private int id;
	
	/**
	 * Constructor
	 * @param resource
	 * @param dicenumber
	 * @param orientation
	 */
	public Tile(String resource, int dicenumber, int orientation){
		this.orientation = orientation;
		this.resource = resource;
		this.dicenum = dicenumber;
		
		crossroads = new Crossroad[6];
		
		//assigning a value to the dicenumber
		//more common dice numbers have a higher value
		if(dicenumber == 2 || dicenumber == 12){
			dicenumber = 1;
		}
		else if(dicenumber == 3 || dicenumber == 11){
			dicenumber = 2;
		}
		else if(dicenumber == 4 || dicenumber == 10){
			dicenumber = 3;
		}
		else if(dicenumber == 5 || dicenumber == 9){
			dicenumber = 4;
		}
		else if(dicenumber == 6 || dicenumber == 8){
			dicenumber = 5;
		}
		else{
			dicenumber = 0;
		}
		
		if(resource.equals("sheep")){
			value = 1/4 * dicenumber;
		}
		else if(resource.equals("wood")){
			value = 1/4 * dicenumber;
		}
		else if(resource.equals("clay")){
			value = 1/3 * dicenumber;
		}
		else if(resource.equals("ore")){
			value = 1/3 * dicenumber;
		}
		else if(resource.equals("desert")){
			value = 0;
		}
		else if(resource.equals("wheat")){
			value = 1/4 * dicenumber;
		}
		//water tiles
		//TODO adjust value of harbor tiles
		else if(resource.equals("harbor")){
			value = 0;
		}
		else if(resource.equals("sheepHarbor")){
			value = 0;
		}
		else if(resource.equals("clayHarbor")){
			value = 0;
		}
		else if(resource.equals("oreHarbor")){
			value = 0;
		}
		else if(resource.equals("woodHarbor")){
			value = 0;
		}
		else if(resource.equals("strawHarbor")){
			value = 0;
		}
		else if(resource.equals("water")){
			value = 0;
		}
	}

	/**
	 * Adds the Crossroad cross into
	 * the first empty space in the crossroad array
	 * @param cross
	 */
	public void addCrossroad(Crossroad cross){
		//add the crossroad to the array
		if(crossroads[0] == null){
			crossroads[0] = cross;
		}
		else if(crossroads[1] == null){
			crossroads[1] = cross;
		}
		else if(crossroads[2] == null){
			crossroads[2] = cross;
		}
		else if(crossroads[3] == null){
			crossroads[3] = cross;
		}
		else if(crossroads[4] == null){
			crossroads[4] = cross;
		}
		else if(crossroads[5] == null){
			crossroads[5] = cross;
		}
		
		//setting booleans of crossroad if there is a harbor
		if(resource.equals("harbor")){
			cross.setHarbor(true);
		}
		else if(resource.equals("sheepHarbor")){
			cross.setSheepHarbor(true);
		}		
		else if(resource.equals("woodHarbor")){
			cross.setWoodHarbor(true);
		}
		else if(resource.equals("wheatHarbor")){
			cross.setWheatHarbor(true);
		}
		else if(resource.equals("clayHarbor")){
			cross.setClayHarbor(true);
		}
		else if(resource.equals("oreHarbor")){
			cross.setOreHarbor(true);
		}
		
		
		//add the tile to the crossroads array and adjust it's value
		cross.addTile(this);
		cross.addValue(value);
	}
	
	public void calcValue() {
		if(this.resource.equals("wood")||this.resource.equals("sheep")||this.resource.equals("wheat")){
			this.value = 0.25 * diceValue();
		} else if(this.resource.equals("ore")||this.resource.equals("clay")){
			this.value = 0.33 * diceValue();
		} else{
			this.value = 0.0;
		}
	}
	
	public double diceValue(){
		if(this.dicenum==2||this.dicenum==12)
			return 1;
		else if(this.dicenum==3||this.dicenum==11)
			return 2;
		if(this.dicenum==4||this.dicenum==10)
			return 3;
		if(this.dicenum==5||this.dicenum==9)
			return 4;
		if(this.dicenum==6||this.dicenum==8)
			return 5;
		else
			return 0.0;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public int getDicenum() {
		return dicenum;
	}

	public void setDicenum(int dicenum) {
		this.dicenum = dicenum;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public Crossroad[] getCrossroads() {
		return crossroads;
	}

	public void setCrossroads(Crossroad[] crossroads) {
		this.crossroads = crossroads;
	}
		
	public int getOrientation() {
		return orientation;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public boolean isThief() {
		return thief;
	}

	public void setThief(boolean thief) {
		this.thief = thief;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
