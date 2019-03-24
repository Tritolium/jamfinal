package games.Catan;

import java.util.ArrayList;
import java.util.Random;

import userManagement.User;

/**
 * @author Florian Grensing
 *
 */
public class Board {


	int id;
	private int knights = 14;
	private int monopol = 2;
	private int roadcards = 2;
	private int research = 2;
	private int victorypoints = 5;

	// Cards awarded by amount of knight cards or longest road
	private Player traderoute = null;;
	private Player knightforce = null;
	private int tradeRouteLength = 4;
	private ArrayList<Integer> allRoads = new ArrayList<>();

	private Tile thief;

	// saves the board setup
	private Tile[] tiles;
	private Crossroad[] crossroads;
	private Road[] roads;

	// for field generation
	// Land tiles
	private int desertTile = 1;
	private int woodTile = 4;
	private int sheepTile = 4;
	private int wheatTile = 4;
	private int oreTile = 3;
	private int clayTile = 3;
	// Sea tiles
	private int harbor = 4;
	private int sheepHarbor = 1;
	private int woodHarbor = 1;
	private int wheatHarbor = 1;
	private int clayHarbor = 1;
	private int oreHarbor = 1;
	private int[] diceNumbers = { 5, 2, 6, 3, 8, 10, 9, 12, 11, 4, 8, 10, 9, 4, 5, 6, 3, 11 };
	private int diceNumCounter = 0;

	private Catan catan;
	private boolean initialized = false;

	// Field generation Methods

	public Board(Catan catan) {
		tiles = new Tile[37];
		crossroads = new Crossroad[54];
		roads = new Road[72];
		this.catan = catan;
	}

	/**
	 * Initializes the Board, places the resource Tiles places the numbers on
	 * the tiles calculates the Value for the Crossroads (for AI)
	 * 
	 * @return
	 */
	public boolean init() {
		if (initialized) {
			return false;
		}
		initialized = true;
		// fill the arrays tiles, crossroads and roads with values,
		// fill the types of fields and their dice numbers

		Tile[] temp = new Tile[37];
		String resource = "";
		int dice = 0;

		// Generate the Tiles (in a wrong order)
		for (int i = 0; i < 19; i++) {
			resource = getResource();
			dice = getDiceNumber(resource);
			temp[i] = new Tile(resource, dice, 0);
		}

		// sorting the tiles into the right order
		tiles[0] = temp[2];
		tiles[1] = temp[1];
		tiles[2] = temp[0];
		tiles[3] = temp[3];
		tiles[4] = temp[13];
		tiles[5] = temp[12];
		tiles[6] = temp[11];
		tiles[7] = temp[4];
		tiles[8] = temp[14];
		tiles[9] = temp[18];
		tiles[10] = temp[17];
		tiles[11] = temp[10];
		tiles[12] = temp[5];
		tiles[13] = temp[15];
		tiles[14] = temp[16];
		tiles[15] = temp[9];
		tiles[16] = temp[6];
		tiles[17] = temp[7];
		tiles[18] = temp[8];

		for (int i = 0; i < 18; i++) {
			if (tiles[i].getResource().equals("desert")) {
				thief = tiles[i];
			}
		}

		Random rand = new Random();
		int startHarbor = rand.nextInt(2);

		// Generating water and Harbor tiles
		for (int i = 19; i < 37; i++) {
			int orientation1 = 0;
			int orientation2 = 0;

			resource = "water";
			// first possible orientation
			if (i < 22) {
				orientation1 = 0;
			} else if (i < 25) {
				orientation1 = 1;
			} else if (i < 28) {
				orientation1 = 2;
			} else if (i < 31) {
				orientation1 = 3;
			} else if (i < 34) {
				orientation1 = 4;
			} else {
				orientation1 = 5;
			}
			// second possible orientation
			if (i > 34 || i == 19) {
				orientation2 = 0;
			} else if (i > 31) {
				orientation2 = 5;
			} else if (i > 28) {
				orientation2 = 4;
			} else if (i > 25) {
				orientation2 = 3;
			} else if (i > 22) {
				orientation2 = 2;
			} else if (i > 19) {
				orientation2 = 1;
			}

			// every second tile gets a harbor
			// startHarbor used to check where the first harbor will we placed
			// if even or odd tile numbers get harbors
			if ((i + startHarbor) % 2 == 0) {
				resource = getSeaResource();
			}

			int orientation = rand.nextInt(2);
			if (orientation == 0) {
				orientation = orientation1;
			} else {
				orientation = orientation2;
			}

			tiles[i] = new Tile(resource, 0, orientation);

		}
		

		// generating all 72 roads
		for (int i = 0; i < 72; i++) {
			roads[i] = new Road(i);
		}
		// generating all 54 crossroads
		for (int i = 0; i < 54; i++) {
			crossroads[i] = new Crossroad();
		}

		// assigning roads to the crossroads
		// top and bottow row
		for (int i = 0; i < 3; i++) {
			crossroads[i].addRoad(roads[i + i]);
			crossroads[i].addRoad(roads[i + i + 1]);
			crossroads[i].addRoad(roads[i + i + 1]);

			crossroads[i + 51].addRoad(roads[i + i + 66]);
			crossroads[i + 51].addRoad(roads[i + i + 1 + 66]);
			crossroads[i + 51].addRoad(roads[i + i + 1 + 66]);
		}

		// second and second last row
		crossroads[3].addRoad(roads[0]);
		crossroads[3].addRoad(roads[6]);
		crossroads[3].addRoad(roads[6]);

		crossroads[47].addRoad(roads[62]);
		crossroads[47].addRoad(roads[66]);
		crossroads[47].addRoad(roads[66]);

		for (int i = 0; i < 2; i++) {
			crossroads[i + 4].addRoad(roads[i + i + 1]);
			crossroads[i + 4].addRoad(roads[i + i + 2]);
			crossroads[i + 4].addRoad(roads[i + 7]);

			crossroads[i + 48].addRoad(roads[i + i + 67]);
			crossroads[i + 48].addRoad(roads[i + i + 68]);
			crossroads[i + 48].addRoad(roads[i + 63]);
		}

		crossroads[6].addRoad(roads[5]);
		crossroads[6].addRoad(roads[9]);
		crossroads[6].addRoad(roads[9]);

		crossroads[50].addRoad(roads[65]);
		crossroads[50].addRoad(roads[71]);
		crossroads[50].addRoad(roads[71]);

		// third and third last row
		for (int i = 0; i < 4; i++) {
			crossroads[i + 7].addRoad(roads[i + 6]);
			crossroads[i + 7].addRoad(roads[i + i + 10]);
			crossroads[i + 7].addRoad(roads[i + i + 11]);

			crossroads[i + 43].addRoad(roads[i + i + 54]);
			crossroads[i + 43].addRoad(roads[i + i + 55]);
			crossroads[i + 43].addRoad(roads[i + 62]);
		}

		// fourth and fourth last row
		crossroads[11].addRoad(roads[10]);
		crossroads[11].addRoad(roads[18]);
		crossroads[11].addRoad(roads[18]);

		crossroads[38].addRoad(roads[49]);
		crossroads[38].addRoad(roads[54]);
		crossroads[38].addRoad(roads[54]);

		for (int i = 0; i < 3; i++) {
			crossroads[i + 12].addRoad(roads[i + i + 11]);
			crossroads[i + 12].addRoad(roads[i + i + 12]);
			crossroads[i + 12].addRoad(roads[i + 19]);

			crossroads[i + 39].addRoad(roads[i + 50]);
			crossroads[i + 39].addRoad(roads[i + i + 55]);
			crossroads[i + 39].addRoad(roads[i + i + 56]);
		}

		crossroads[15].addRoad(roads[17]);
		crossroads[15].addRoad(roads[22]);
		crossroads[15].addRoad(roads[22]);

		crossroads[42].addRoad(roads[53]);
		crossroads[42].addRoad(roads[61]);
		crossroads[42].addRoad(roads[61]);

		// row 5
		for (int i = 0; i < 5; i++) {
			crossroads[i + 16].addRoad(roads[i + 18]);
			crossroads[i + 16].addRoad(roads[i + i + 23]);
			crossroads[i + 16].addRoad(roads[i + i + 24]);

			crossroads[i + 33].addRoad(roads[i + i + 39]);
			crossroads[i + 33].addRoad(roads[i + i + 40]);
			crossroads[i + 33].addRoad(roads[i + 49]);
		}

		// middle 2 rows
		crossroads[21].addRoad(roads[23]);
		crossroads[21].addRoad(roads[33]);
		crossroads[21].addRoad(roads[33]);

		crossroads[27].addRoad(roads[33]);
		crossroads[27].addRoad(roads[39]);
		crossroads[27].addRoad(roads[39]);

		for (int i = 0; i < 4; i++) {
			crossroads[i + 22].addRoad(roads[i + i + 24]);
			crossroads[i + 22].addRoad(roads[i + i + 25]);
			crossroads[i + 22].addRoad(roads[i + 34]);

			crossroads[i + 28].addRoad(roads[i + 34]);
			crossroads[i + 28].addRoad(roads[i + i + 40]);
			crossroads[i + 28].addRoad(roads[i + i + 41]);

		}

		crossroads[26].addRoad(roads[32]);
		crossroads[26].addRoad(roads[38]);
		crossroads[26].addRoad(roads[38]);

		crossroads[32].addRoad(roads[38]);
		crossroads[32].addRoad(roads[48]);
		crossroads[32].addRoad(roads[48]);

		// Assigning the Crossroads to the tiles
		// tiles[].addCrossroad(crossroads[]);
		for (int i = 0; i < 3; i++) {
			tiles[i].addCrossroad(crossroads[i]);
			tiles[i].addCrossroad(crossroads[i + 3]);
			tiles[i].addCrossroad(crossroads[i + 4]);
			tiles[i].addCrossroad(crossroads[i + 7]);
			tiles[i].addCrossroad(crossroads[i + 8]);
			tiles[i].addCrossroad(crossroads[i + 12]);

			tiles[i + 16].addCrossroad(crossroads[i + 39]);
			tiles[i + 16].addCrossroad(crossroads[i + 43]);
			tiles[i + 16].addCrossroad(crossroads[i + 44]);
			tiles[i + 16].addCrossroad(crossroads[i + 47]);
			tiles[i + 16].addCrossroad(crossroads[i + 48]);
			tiles[i + 16].addCrossroad(crossroads[i + 51]);

		}
		for (int i = 0; i < 4; i++) {
			tiles[i + 3].addCrossroad(crossroads[i + 7]);
			tiles[i + 3].addCrossroad(crossroads[i + 11]);
			tiles[i + 3].addCrossroad(crossroads[i + 12]);
			tiles[i + 3].addCrossroad(crossroads[i + 16]);
			tiles[i + 3].addCrossroad(crossroads[i + 17]);
			tiles[i + 3].addCrossroad(crossroads[i + 22]);

			tiles[i + 12].addCrossroad(crossroads[i + 28]);
			tiles[i + 12].addCrossroad(crossroads[i + 33]);
			tiles[i + 12].addCrossroad(crossroads[i + 34]);
			tiles[i + 12].addCrossroad(crossroads[i + 38]);
			tiles[i + 12].addCrossroad(crossroads[i + 39]);
			tiles[i + 12].addCrossroad(crossroads[i + 43]);

		}
		for (int i = 0; i < 5; i++) {
			tiles[i + 7].addCrossroad(crossroads[i + 16]);
			tiles[i + 7].addCrossroad(crossroads[i + 21]);
			tiles[i + 7].addCrossroad(crossroads[i + 22]);
			tiles[i + 7].addCrossroad(crossroads[i + 27]);
			tiles[i + 7].addCrossroad(crossroads[i + 28]);
			tiles[i + 7].addCrossroad(crossroads[i + 33]);
		}

		// sea tiles watertile if harbor isnt turned toward a crossroad
		// corner Tiles
		tiles[19].addCrossroad(crossroads[0]);
		tiles[19].addCrossroad(crossroads[3]);
		tiles[19].addCrossroad(crossroads[3]);

		tiles[22].addCrossroad(crossroads[2]);
		tiles[22].addCrossroad(crossroads[6]);
		tiles[22].addCrossroad(crossroads[6]);

		tiles[25].addCrossroad(crossroads[26]);
		tiles[25].addCrossroad(crossroads[32]);
		tiles[25].addCrossroad(crossroads[32]);

		tiles[28].addCrossroad(crossroads[50]);
		tiles[28].addCrossroad(crossroads[53]);
		tiles[28].addCrossroad(crossroads[53]);

		tiles[31].addCrossroad(crossroads[47]);
		tiles[31].addCrossroad(crossroads[51]);
		tiles[31].addCrossroad(crossroads[51]);

		tiles[34].addCrossroad(crossroads[21]);
		tiles[34].addCrossroad(crossroads[27]);
		tiles[34].addCrossroad(crossroads[27]);

		// non-corner tiles, depending on orientation
		Tile waterTile = new Tile("water", 0, 0);

		if (tiles[20].getOrientation() == 0) {
			tiles[20].addCrossroad(crossroads[1]);
			tiles[20].addCrossroad(crossroads[4]);
			crossroads[0].addTile(waterTile);
		} else {
			tiles[20].addCrossroad(crossroads[0]);
			tiles[20].addCrossroad(crossroads[4]);
			crossroads[1].addTile(waterTile);
		}

		if (tiles[21].getOrientation() == 0) {
			tiles[21].addCrossroad(crossroads[2]);
			tiles[21].addCrossroad(crossroads[5]);
			crossroads[1].addTile(waterTile);
		} else {
			tiles[21].addCrossroad(crossroads[1]);
			tiles[21].addCrossroad(crossroads[5]);
			crossroads[2].addTile(waterTile);
		}

		if (tiles[23].getOrientation() == 1) {
			tiles[23].addCrossroad(crossroads[10]);
			tiles[23].addCrossroad(crossroads[15]);
			crossroads[6].addTile(waterTile);
		} else {
			tiles[23].addCrossroad(crossroads[10]);
			tiles[23].addCrossroad(crossroads[6]);
			crossroads[15].addTile(waterTile);
		}

		if (tiles[24].getOrientation() == 1) {
			tiles[24].addCrossroad(crossroads[20]);
			tiles[24].addCrossroad(crossroads[26]);
			crossroads[15].addTile(waterTile);
		} else {
			tiles[24].addCrossroad(crossroads[20]);
			tiles[24].addCrossroad(crossroads[15]);
			crossroads[26].addTile(waterTile);
		}

		if (tiles[26].getOrientation() == 2) {
			tiles[26].addCrossroad(crossroads[37]);
			tiles[26].addCrossroad(crossroads[42]);
			crossroads[32].addTile(waterTile);
		} else {
			tiles[26].addCrossroad(crossroads[37]);
			tiles[26].addCrossroad(crossroads[32]);
			crossroads[42].addTile(waterTile);
		}

		if (tiles[27].getOrientation() == 2) {
			tiles[27].addCrossroad(crossroads[46]);
			tiles[27].addCrossroad(crossroads[50]);
			crossroads[42].addTile(waterTile);
		} else {
			tiles[27].addCrossroad(crossroads[46]);
			tiles[27].addCrossroad(crossroads[42]);
			crossroads[50].addTile(waterTile);
		}

		if (tiles[29].getOrientation() == 3) {
			tiles[29].addCrossroad(crossroads[49]);
			tiles[29].addCrossroad(crossroads[52]);
			crossroads[53].addTile(waterTile);
		} else {
			tiles[29].addCrossroad(crossroads[49]);
			tiles[29].addCrossroad(crossroads[53]);
			crossroads[52].addTile(waterTile);
		}

		if (tiles[30].getOrientation() == 2) {
			tiles[30].addCrossroad(crossroads[48]);
			tiles[30].addCrossroad(crossroads[51]);
			crossroads[52].addTile(waterTile);
		} else {
			tiles[30].addCrossroad(crossroads[48]);
			tiles[30].addCrossroad(crossroads[52]);
			crossroads[51].addTile(waterTile);
		}

		if (tiles[32].getOrientation() == 4) {
			tiles[32].addCrossroad(crossroads[43]);
			tiles[32].addCrossroad(crossroads[38]);
			crossroads[47].addTile(waterTile);
		} else {
			tiles[32].addCrossroad(crossroads[43]);
			tiles[32].addCrossroad(crossroads[47]);
			crossroads[38].addTile(waterTile);
		}

		if (tiles[33].getOrientation() == 4) {
			tiles[33].addCrossroad(crossroads[33]);
			tiles[33].addCrossroad(crossroads[27]);
			crossroads[38].addTile(waterTile);
		} else {
			tiles[33].addCrossroad(crossroads[33]);
			tiles[33].addCrossroad(crossroads[38]);
			crossroads[27].addTile(waterTile);
		}

		if (tiles[35].getOrientation() == 5) {
			tiles[35].addCrossroad(crossroads[16]);
			tiles[35].addCrossroad(crossroads[11]);
			crossroads[21].addTile(waterTile);
		} else {
			tiles[35].addCrossroad(crossroads[16]);
			tiles[35].addCrossroad(crossroads[21]);
			crossroads[11].addTile(waterTile);
		}
		if (tiles[36].getOrientation() == 5) {
			tiles[36].addCrossroad(crossroads[7]);
			tiles[36].addCrossroad(crossroads[3]);
			crossroads[11].addTile(waterTile);
		} else {
			tiles[36].addCrossroad(crossroads[7]);
			tiles[36].addCrossroad(crossroads[11]);
			crossroads[3].addTile(waterTile);
		}

		for (int i = 0; i < 37; i++) {
			tiles[i].calcValue();
			tiles[i].setId(i);
		}

		for (int i = 0; i < 54; i++) {
			for (int j = 0; j < 3; j++) {
				crossroads[i].addValue(crossroads[i].getTiles()[j].getValue());
			}
		}

		for (int i = 0; i < 54; i++) {
			crossroads[i].setId(i);
		}

		for(int i = 19; i < 37; i++){
			temp[i] = tiles[i];
		}
		
		tiles[19] = temp[19];
		tiles[20] = temp[20];
		tiles[21] = temp[21];
		tiles[22] = temp[22];
		tiles[23] = temp[36];
		tiles[24] = temp[23];
		tiles[25] = temp[35];
		tiles[26] = temp[24];
		tiles[27] = temp[34];
		tiles[28] = temp[25];
		tiles[29] = temp[33];
		tiles[30] = temp[26];
		tiles[31] = temp[32];
		tiles[32] = temp[27];
		tiles[33] = temp[31];
		tiles[34] = temp[30];
		tiles[35] = temp[29];
		tiles[36] = temp[28];
		
		return true;
	}

	/**
	 * used for init()
	 * 
	 * @return one of the remaining tile cards
	 */
	private String getResource() {
		int tilenumber = desertTile + woodTile + sheepTile + wheatTile + oreTile + clayTile;
		Random rand = new Random();
		int tileType = rand.nextInt(tilenumber);

		if (tileType < desertTile) {
			desertTile--;
			return "desert";
		} else if (tileType < desertTile + woodTile) {
			woodTile--;
			return "wood";
		} else if (tileType < desertTile + woodTile + sheepTile) {
			sheepTile--;
			return "sheep";
		} else if (tileType < desertTile + woodTile + sheepTile + wheatTile) {
			wheatTile--;
			return "wheat";
		} else if (tileType < desertTile + woodTile + sheepTile + wheatTile + oreTile) {
			oreTile--;
			return "ore";
		} else {
			clayTile--;
			return "clay";
		}
	}

	/**
	 * used for init()
	 * 
	 * @return one of the remaining harbors
	 */
	private String getSeaResource() {
		int tilenumber = harbor + sheepHarbor + woodHarbor + wheatHarbor + clayHarbor + oreHarbor;

		Random rand = new Random();
		int tileType = rand.nextInt(tilenumber);

		if (tileType < harbor) {
			harbor--;
			return "harbor";
		} else if (tileType < harbor + sheepHarbor) {
			sheepHarbor--;
			return "sheepHarbor";
		} else if (tileType < harbor + sheepHarbor + woodHarbor) {
			woodHarbor--;
			return "woodHarbor";
		} else if (tileType < harbor + sheepHarbor + woodHarbor + wheatHarbor) {
			wheatHarbor--;
			return "wheatHarbor";
		} else if (tileType < harbor + sheepHarbor + woodHarbor + wheatHarbor + clayHarbor) {
			clayHarbor--;
			return "clayHarbor";
		} else {
			oreHarbor--;
			return "oreHarbor";
		}
	}

	/**
	 * used for init()
	 * 
	 * @param resource
	 *            checks if desert or not
	 * @return one of the remaining numbers
	 */
	private int getDiceNumber(String resource) {
		if (resource.equals("desert")) {
			return 0;
		} else {
			diceNumCounter++;
			return diceNumbers[diceNumCounter - 1];
		}
	}

	/**
	 *
	 * @return the current board status
	 */
	public String getStatus() {
		String[] res = new String[74];
		/*
		 * for (int i = 0; i < 72; i++) { res[i + 111] =
		 * roads[i].getOwner().getName(); } for (int i = 0; i < 54; i++) { res[i
		 * + 183] = crossroads[i].getOwner().getName(); } for (int i = 0; i <
		 * 54; i++) { res[i + 237] = "" + crossroads[i].getBuild(); }
		 */
		for (int i = 0; i < 19; i++) {
			String ressource = tiles[i].getResource();
			switch (ressource) {
			case "desert":
				res[i] = "0";
				break;
			case "sheep":
				res[i] = "1";
				break;
			case "clay":
				res[i] = "2";
				break;
			case "ore":
				res[i] = "3";
				break;
			case "wheat":
				res[i] = "4";
				break;
			case "wood":
				res[i] = "5";
				break;
			case "harbor":
				res[i] = "1";
				break;
			case "sheepHarbor":
				res[i] = "2";
				break;
			case "woodHarbor":
				res[i] = "3";
				break;
			case "wheatHarbor":
				res[i] = "4";
				break;
			case "clayHarbor":
				res[i] = "5";
				break;
			case "oreHarbor":
				res[i] = "6";
				break;
			case "water":
				res[i] = "0";
				break;
			}
			if (tiles[i].getDicenum() == 0) {
				res[i + 19] = "" + 0;
			} else if (tiles[i].getDicenum() < 7) {
				res[i + 19] = "" + (tiles[i].getDicenum() - 2);
			} else {
				res[i + 19] = "" + (tiles[i].getDicenum() - 3);
			}
		}

		for (int i = 19; i < 37; i++) {
			String ressource = tiles[i].getResource();
			switch (ressource) {
			case "desert":
				res[i + 19] = "0";
				break;
			case "sheep":
				res[i + 19] = "1";
				break;
			case "clay":
				res[i + 19] = "2";
				break;
			case "ore":
				res[i + 19] = "3";
				break;
			case "wheat":
				res[i + 19] = "4";
				break;
			case "wood":
				res[i + 19] = "5";
				break;
			case "harbor":
				res[i + 19] = "1";
				break;
			case "sheepHarbor":
				res[i + 19] = "2";
				break;
			case "woodHarbor":
				res[i + 19] = "3";
				break;
			case "wheatHarbor":
				res[i + 19] = "4";
				break;
			case "clayHarbor":
				res[i + 19] = "5";
				break;
			case "oreHarbor":
				res[i + 19] = "6";
				break;
			case "water":
				res[i + 19] = "0";
				break;
			}
			res[i + 19 + 18] = "" + tiles[i].getOrientation();
		}

		String str = "";
		for (int i = 0; i < 74; i++) {
			str += res[i];
		}

		return str;
	}

	/**
	 * Draws a Card 0 = knight 1 = monopol 2 = roadcard 3 = research 4 =
	 * victorypoints
	 * 
	 * @return the int containing the type of card
	 * @throws NoMoreCardsException 
	 */
	public int drawcard() throws NoMoreCardsException {
		int allcards = knights + monopol + roadcards + research + victorypoints;

		if(allcards <= 0){
			throw new NoMoreCardsException();
		}
		// generates the random number
		Random rand = new Random();
		int randomnum = rand.nextInt(allcards);

		if (randomnum < knights) {
			knights -= 1;
			return 0;
		} else if (randomnum < knights + monopol) {
			monopol -= 1;
			return 1;
		} else if (randomnum < knights + monopol + roadcards) {
			roadcards -= 1;
			return 2;
		} else if (randomnum < knights + monopol + roadcards + research) {
			research -= 1;
			return 3;
		}
		victorypoints -= 1;
		return 4;
	}

	/**
	 * Calculates the longest trade route if there is a longer trade route than
	 * there was previously, the owner of this new traderoute (if it is a
	 * different person) will get 2 victorypoints, and the previous owner loses
	 * 2
	 * 
	 * @throws WinnerException
	 */
	public void getTradeRoutePlayer() throws WinnerException {

		// for each road, test how many roads are in that line
		// if there are more than 5 roads from the same player in a row, check
		// if there is a player with more
		allRoads.clear();
		int curRoads = 0;
		tradeRouteLength = 4;
		for (Player p : catan.getPl()) {
			p.setLongestRoad(0);
		}
		Player owner = null;
		for (int i = 0; i < 72; i++) {
			if (roads[i].getOwner() != null) {
				if (true) {
					curRoads = tradeRoute(100, i, new ArrayList<Integer>());
					if (curRoads > roads[i].getOwner().getLongestRoad()) {
						roads[i].getOwner().setLongestRoad(curRoads);
					}
					if (curRoads > tradeRouteLength) {
						tradeRouteLength = curRoads;
						owner = roads[i].getOwner();
					}
				}
			}
		}
		
		if(owner != null){
			if(owner != traderoute){
				if(traderoute != null){
					if(owner.getLongestRoad() > traderoute.getLongestRoad()){
						traderoute.setVictorypoints(traderoute.getVictorypoints() -2);
						owner.increaseVictoryPoints();
						owner.increaseVictoryPoints();
						traderoute = owner;
					}
				}
				else{
					owner.increaseVictoryPoints();
					owner.increaseVictoryPoints();
					traderoute = owner;
				}
				
			}
		}
		else if(traderoute != null){
			traderoute.setVictorypoints(traderoute.getVictorypoints() -2);
			traderoute = null;
		}
	}

	private int tradeRoute(int lastRoad, int current, ArrayList<Integer> allroads) {
		// adds the current road to the list of roads already in the street
		// and to the list of all roads checked this turn
		allRoads.add(current);
		boolean cross1 = false;
		boolean cross2 = false;
		Player owner = roads[current].getOwner();
		// Save the return values of the 4 connected Roads
		// that has the most entries
		int longest = 0;
		int longest2 = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		allroads.add(current);
		list.addAll(allroads);
		list.add(current);
		// check from which crossroad i came, cannot go to any roads on that
		// side
		Crossroad curr = roads[current].getCrossroads()[0];
		for (int i = 0; i < 3; i++) {
			if (curr.getRoads()[i].getId() == lastRoad) {
				cross1 = true;
			}
		}
		curr = roads[current].getCrossroads()[1];
		for (int i = 0; i < 3; i++) {
			if (curr.getRoads()[i].getId() == lastRoad) {
				cross2 = true;
			}
		}
		// check if neighboring roads have the same owner
		// first of the crossroads
		curr = roads[current].getCrossroads()[0];
		// Check that no cities from other owners interrupt the trade route
		if ((curr.getOwner() != owner && curr.getOwner() != null) || cross1) {
			longest = 0;
		} else {
			for (int j = 0; j < 3; j++) {
				// check the roads connected to the crossroads if they are of
				// the same owner
				Road curRoad = curr.getRoads()[j];
				if (curRoad.getOwner() == owner) {
					if (!allroads.contains(curRoad.getId())) {
						int temp = tradeRoute(current, curRoad.getId(), list);
						if (longest < temp) {
							longest = temp;
						}
					} else {
					}
				}
			}
		}
		// second crossroad
		curr = roads[current].getCrossroads()[1];
		// Check that no cities from other owners interrupt the trade route
		if ((curr.getOwner() != owner && curr.getOwner() != null) || cross2) {
			longest2 = 0;
		} else {
			for (int j = 0; j < 3; j++) {
				// check the roads connected to the crossroads if they are of
				// the same owner
				Road curRoad = curr.getRoads()[j];
				if (curRoad.getOwner() == owner) {
					if (!allroads.contains(curRoad.getId())) {
						int temp = tradeRoute(current, curRoad.getId(), list);
						if (longest2 < temp) {
							longest2 = temp;
						}
					} else {
					}
				}
			}
		}

		if (longest > 0 || longest2 > 0) {
			if (longest > longest2) {
				return 1 + longest;
			} else {
				return 1 + longest2;
			}

		}
		return 1;
	}

	/**
	 * rolls the dice and rewards players with the resources calls thief() if 7
	 * is rolled
	 * 
	 * @return the integer value belonging to the two die
	 */
	public int[] rollDice() {
		int[] res = { 0, 0 };
		Random rand = new Random();
		res[0] = rand.nextInt(6) + 1;
		res[1] = rand.nextInt(6) + 1;

		// rewarding the resources to the players
		int dice = res[0] + res[1];

		if (dice != 7) {
			for (int i = 0; i < 19; i++) {
				if (tiles[i].getDicenum() == dice && tiles[i] != thief) {
					for (int j = 0; j < 6; j++) {
						if (tiles[i].getCrossroads()[j].getOwner() != null) {
							int amount = tiles[i].getCrossroads()[j].getBuild();
							Player owner = tiles[i].getCrossroads()[j].getOwner();
							if (tiles[i].getResource().equals("sheep")) {
								owner.setSheep(owner.getSheep() + amount);
								catan.execute(null, "CHAT, " + owner.getName() + " hat " + amount + " Schaaaf(e) bekommen");
							} else if (tiles[i].getResource().equals("wood")) {
								owner.setWood(owner.getWood() + amount);
								catan.execute(null, "CHAT, " + owner.getName() + " hat " + amount + " Holz bekommen");
							} else if (tiles[i].getResource().equals("wheat")) {
								owner.setWheat(owner.getWheat() + amount);
								catan.execute(null, "CHAT, " + owner.getName() + " hat " + amount + " Weizen bekommen");
							} else if (tiles[i].getResource().equals("clay")) {
								owner.setClay(owner.getClay() + amount);
								catan.execute(null, "CHAT, " + owner.getName() + " hat " + amount + " Lehm bekommen");
							} else if (tiles[i].getResource().equals("ore")) {
								owner.setOre(owner.getOre() + amount);
								catan.execute(null, "CHAT, " + owner.getName() + " hat " + amount + " Erz bekommen");
							}
						}
					}
				}
			}
		} else {
			thief();
		}
		return res;
	}

	/**
	 * Called when a Seven is Rolled: All Players with more than 7 resources
	 * lose half their resources rounded down resources picked at random
	 */
	public void thief() {
		// resource reduction for all players
		for (Player p : catan.getPl()) {
			int sheep = p.getSheep();
			int wheat = p.getWheat();
			int wood = p.getWood();
			int clay = p.getClay();
			int ore = p.getOre();
			int temp = sheep + wheat + wood + clay + ore;
			
			if (temp > 7) {
				int tosteal = temp / 2;
				Random rand = new Random();
				for (int i = 0; i < tosteal; i++) {
					temp = sheep + wheat + wood + clay + ore;
					int foo = rand.nextInt(temp);
					if (foo < sheep) {
						p.setSheep(p.getSheep() - 1);
						sheep--;
					} else if (foo < sheep + wheat) {
						p.setWheat(p.getWheat() - 1);
						wheat--;
					} else if (foo < sheep + wheat + wood) {
						p.setWood(p.getWood() - 1);
						wood--;
					} else if (foo < sheep + wheat + wood + clay) {
						p.setClay(p.getClay() - 1);
						clay--;
					} else {
						p.setOre(p.getOre() - 1);
						ore--;
					}
					temp--;
				}
			}
		}

	}

	/**
	 * places the Thief on the new tile, Steals one random resource from Player
	 * b and gives it to PLayer a
	 * 
	 * @param tileInt
	 * @param b
	 * @param a
	 * @throws PlayerNotOnTileException
	 */
	public String placeThief(int tileInt, Player bC, Player a) throws PlayerNotOnTileException {
		Tile tile;
		if(tileInt != -1){
			tile = tiles[tileInt];
		}
		else{
			tile = thief;
		}
		// creating 3 other Players
		Player b = null;
		Player c = null;
		Player d = null;
		String ret = "";
		// checking how many different players are built around this crossroad
		// save them in players b,c,d
		if (bC == null) {
			for (int i = 0; i < 6; i++) {
				if (tile.getCrossroads()[i].getOwner() != null) {
					if (tile.getCrossroads()[i].getOwner() != a) {
						if (tile.getCrossroads()[i].getOwner() != b) {
							if (b == null) {
								b = tile.getCrossroads()[i].getOwner();
							} else if (c == null) {
								c = tile.getCrossroads()[i].getOwner();
							} else {
								d = tile.getCrossroads()[i].getOwner();
							}

						}
					}
				}
			}
			// if noone is build at the tile, noone will be robbed
			if (b == null) {
				thief = tile;
				return "niemanden";
			}
			// if there are more than 2 players that are not the one who moves
			// the thief he has to choose one
			else if (c != null) {
				thief = tile;
				if (d == null) {
					return "" + tileInt + "," + catan.getUserIndex(b) + "," + catan.getUserIndex(c);
				} else {
					return "" + tileInt + "," + catan.getUserIndex(b) + "," + catan.getUserIndex(c) + "," + catan.getUserIndex(d);
				}
			}
			//will return ret, the index of the player being robbed
			else{
				ret = "" + catan.getUserIndex(b);
			}
		}else{
			b = bC;
			ret = "" + catan.getUserIndex(bC);
		}
		
		boolean hasCity = false;
		for (int i = 0; i < 6; i++) {
			if (tile.getCrossroads()[i].getOwner() == b) {
				hasCity = true;
			}
		}

		if (!hasCity) {
			throw new PlayerNotOnTileException();
		}

		thief = tile;
		// Check if player b has a settlement or city on next to the tile

		// Pick a random resource from Player b and give it to Player a
		int sheep = b.getSheep();
		int wheat = b.getWheat();
		int wood = b.getWood();
		int clay = b.getClay();
		int ore = b.getOre();
		int temp = sheep + wheat + wood + clay + ore;
		if(temp > 0){
			Random rand = new Random();
			int foo = rand.nextInt(temp);

			if (foo < sheep) {
				b.setSheep(b.getSheep() - 1);
				a.setSheep(a.getSheep() + 1);
				catan.setChat("4" + a.getName() + " hat ein Schaaaf von " + b.getName() + " geklaut!");
			} else if (foo < sheep + wheat) {
				b.setWheat(b.getWheat() - 1);
				a.setWheat(a.getWheat() + 1);
				catan.setChat("4" + a.getName() + " hat ein Weizen von " + b.getName() + " geklaut!");
			} else if (foo < sheep + wheat + wood) {
				b.setWood(b.getWood() - 1);
				a.setWood(a.getWood() + 1);
				catan.setChat("4" + a.getName() + " hat ein Holz von " + b.getName() + " geklaut!");
			} else if (foo < sheep + wheat + wood + clay) {
				b.setClay(b.getClay() - 1);
				a.setClay(a.getClay() + 1);
				catan.setChat("4" + a.getName() + " hat ein Lehm von " + b.getName() + " geklaut!");
			} else {
				b.setOre(b.getOre() - 1);
				a.setOre(a.getOre() + 1);
				catan.setChat("4" + a.getName() + " hat ein Erz von " + b.getName() + " geklaut!");
			}
			catan.sendDataToUser(catan.getPlayerList().get(catan.getUserIndex(a)), "CHAT");
			catan.sendDataToUser(catan.getPlayerList().get(catan.getUserIndex(b)), "CHAT");
		}
		
		return ret;
	}

	/**
	 * checks if a player won
	 * 
	 * @throws WinnerException
	 */
	public void checkVictoryPoints() throws WinnerException {

		for (User u : catan.getPlayerList()) {
			Player p = (Player) u;
			if (p.getVictorypoints() >= 10) {
				throw new WinnerException();
			}
		}
	}

	/**
	 * called by Player class if a player wins
	 * 
	 * @param winner
	 */
	public void endOfGame(Player winner) {

	}

	/**
	 * sends a game status, without the Basic field types
	 * 
	 * @return
	 */
	public String updateField() {
		String str = "";

		// Build level of all crossroads
		for (int i = 0; i < 54; i++) {
			str += "" + crossroads[i].getBuild();
		}
		// Owner of all crossroads, 0 if no owner
		for (int i = 0; i < 54; i++) {
			if (crossroads[i].getOwner() == null) {
				str += "4";
			} else {
				str += "" + catan.getUserIndex(crossroads[i].getOwner());
			}
		}

		// owner of all streets, 0 if none
		for (int i = 0; i < 72; i++) {
			if (roads[i].getOwner() == null) {
				str += "4";
			} else {
				str += "" + catan.getUserIndex(roads[i].getOwner());
			}
		}
		return str;
	}

	// Getters and Setters
	public int getKnights() {
		return knights;
	}

	public void setKnights(int knights) {
		this.knights = knights;
	}

	public int getMonopol() {
		return monopol;
	}

	public void setMonopol(int monopol) {
		this.monopol = monopol;
	}

	public int getRoadcards() {
		return roadcards;
	}

	public void setRoadcards(int roadcards) {
		this.roadcards = roadcards;
	}

	public int getResearch() {
		return research;
	}

	public void setResearch(int research) {
		this.research = research;
	}

	public int getVictorypoints() {
		return victorypoints;
	}

	public void setVictorypoints(int victorypoints) {
		this.victorypoints = victorypoints;
	}

	public Player getTraderoute() {
		return traderoute;
	}

	public void setTraderoute(Player traderoute) {
		this.traderoute = traderoute;
	}

	public Player getKnightforce() {
		return knightforce;
	}

	public void setKnightforce(Player knightforce) {
		this.knightforce = knightforce;
	}

	public Tile getThief() {
		return thief;
	}

	public void setThief(Tile thief) {
		this.thief = thief;
	}

	public Tile[] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[] tiles) {
		this.tiles = tiles;
	}

	public Crossroad[] getCrossroads() {
		return crossroads;
	}

	public void setCrossroads(Crossroad[] crossroads) {
		this.crossroads = crossroads;
	}

	public Road[] getRoads() {
		return roads;
	}

	public void setRoads(Road[] roads) {
		this.roads = roads;
	}

	public Catan getCatan() {
		return catan;
	}

	public void setCatan(Catan catan) {
		this.catan = catan;
	}

}
