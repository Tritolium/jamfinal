/**
 * 
 */
package games.Catan;

/**
 * @author
 *
 */
public class Crossroad implements Comparable<Crossroad>{

	private Player owner;
	private int id;
	private int build;
	private double value;
	private Road[] roads;
	private Tile[] tiles;
	
	//boolean to show if there is a harbor for a city here
	private boolean harbor = false;
	private boolean sheepHarbor = false;
	private boolean woodHarbor = false;
	private boolean wheatHarbor = false;
	private boolean clayHarbor = false;
	private boolean oreHarbor = false;
	
	/**
	 * Constructor
	 * creates an empty crossroad
	 */
	public Crossroad(){
		build = 0;
		owner = null;
		roads = new Road[3];

		roads[0] = null;
		roads[1] = null;
		roads[2] = null;
		

		value = 0;
		
		tiles = new Tile[3];
	}
	
	/**
	 * Add the road into the first empty spot in the array
	 * @param road
	 */
	public void addRoad(Road road){
		if(roads[0] == null){
			roads[0] = road;
		}
		else if(roads[1] == null){
			roads[1] = road;
		}
		else{
			roads[2] = road;
		}
		
		road.addCrossroad(this);
	}
	
	/**
	 * adds the tile into the first
	 * empty space int the tile array
	 * @param tile
	 */
	public void addTile(Tile tile){
		if(tiles[0] == null){
			tiles[0] = tile;
		}
		else if(tiles[1] == null){
			tiles[1] = tile;
		}
		else if(tiles[2] == null){
			tiles[2] = tile;
		}
	}
	
	/**
	 * adds a value to the tile
	 * used for AI
	 * @param val
	 */
	public void addValue(double val){
		value += val;
	}
	

	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	public int getBuild() {
		return build;
	}
	public void setBuild(int build) {
		this.build = build;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Road[] getRoads() {
		return roads;
	}
	public void setRoads(Road[] roads) {
		this.roads = roads;
	}
	public Tile[] getTiles() {
		return tiles;
	}
	public void setTiles(Tile[] tiles) {
		this.tiles = tiles;
	}
	public boolean isHarbor() {
		return harbor;
	}
	public void setHarbor(boolean harbor) {
		this.harbor = harbor;
	}
	public boolean isSheepHarbor() {
		return sheepHarbor;
	}
	public void setSheepHarbor(boolean sheepHarbor) {
		this.sheepHarbor = sheepHarbor;
	}
	public boolean isWoodHarbor() {
		return woodHarbor;
	}
	public void setWoodHarbor(boolean woodHarbor) {
		this.woodHarbor = woodHarbor;
	}
	public boolean isWheatHarbor() {
		return wheatHarbor;
	}
	public void setWheatHarbor(boolean wheatHarbor) {
		this.wheatHarbor = wheatHarbor;
	}
	public boolean isClayHarbor() {
		return clayHarbor;
	}
	public void setClayHarbor(boolean clayHarbor) {
		this.clayHarbor = clayHarbor;
	}
	public boolean isOreHarbor() {
		return oreHarbor;
	}
	public void setOreHarbor(boolean oreHarbor) {
		this.oreHarbor = oreHarbor;
	}

	@Override
	public int compareTo(Crossroad arg0) {
		// TODO Auto-generated method stub
		return Double.compare(this.getValue(), arg0.getValue());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
