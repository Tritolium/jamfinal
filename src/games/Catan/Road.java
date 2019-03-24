/**
 * 
 */
package games.Catan;

/**
 * @author
 *
 */
public class Road {
	private int id;
	private Player owner;
	private Crossroad[] crossroads;
	
	/**
	 * Constructor
	 * creates an empty road with an id
	 * @param id
	 */
	public Road(int id){
		this.id = id;
		owner = null;
		crossroads = new Crossroad[2];
		crossroads[0] = null;
		crossroads[1] = null;
	}
	
	/**
	 * adds a Crossroad into the first open
	 * space in the crossroad array
	 * @param cross
	 */
	public void addCrossroad(Crossroad cross){
		if(crossroads[0] == null){
			crossroads[0] = cross;
		}
		else{
			crossroads[1] = cross;
		}
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	public Crossroad[] getCrossroads() {
		return crossroads;
	}
	public void setCrossroads(Crossroad[] crossroads) {
		this.crossroads = crossroads;
	}
	
}
