/**
 * 
 */
package games.Catan;

import java.util.Random;

import userManagement.User;

/**
 * @author
 *
 */
public class Player extends User {
	// dieb spieler zum klauen auswählen
	// handel
	// methoden für die ersten beiden züge
	// in buildRoad auf freeRoads prüfen
	// strings an clients schicken
	
	//manchmal geht zug beenden button nicht
	//würfel zeigen nichts an!

	/**
	 * 
	 */
	private static final long serialVersionUID = -4578299629223494233L;

	public Player(AI ai, Board board){
		super(ai.getName(), null);
		this.board = board;
		settlements = 5;
		roads = 15;
		cities = 4;
		free_roads = 1;
		free_settlement = 1;
	}
	
	public Player(String name, String pw, Board board) {
		super(name, pw);
		this.board = board;
		settlements = 5;
		roads = 15;
		cities = 4;
		free_roads = 1;
		free_settlement = 1;
	}
	
	public Player(User user, Board board){
		super(user.getName(), "none");
		this.board = board;
		settlements = 5;
		roads = 15;
		cities = 4;
		free_roads = 1;
		free_settlement = 1;
	}

	/**
	 * 
	 */
	private Board board;
	private int sheep = 0;
	private int clay = 0;
	private int ore = 0;
	private int wheat = 0;
	private int wood = 0;
	private int settlements = 0;// not built yet
	private int roads = 0;// not built yet
	private int cities = 0;// not built yet
	private int knightcards = 0;
	private int open_knightcards = 0;
	private int monopolcards = 0;// not played yet
	private int roadcards = 0;// not played yet
	private int researchcards = 0;// not played yet
	private int victorypointcards = 0;// not played yet
	private int victorypoints = 0;
	private int free_roads = 0;
	private int free_settlement = 0;
	private int longestRoad = 0;
	//private String trade;
	
	//0=3:1, 1=sheep, 2=clay, 3=ore, 4=wheat, 5=wood
	private boolean[] harbor = new boolean[6];

	public int getFree_settlement() {
		return free_settlement;
	}

	public void setFree_settlement(int free_settlement) {
		this.free_settlement = free_settlement;
	}

	public int getSettlements() {
		return settlements;
	}

	public void setSettlements(int settlements) {
		this.settlements = settlements;
	}

	public int getRoads() {
		return roads;
	}

	public void setRoads(int roads) {
		this.roads = roads;
	}

	public int getCities() {
		return cities;
	}

	public void setCities(int cities) {
		this.cities = cities;
	}

	public int getKnightcards() {
		return knightcards;
	}
	
	public Board getBoard() {
		return this.board;
	}

	public void setKnightcards(int knightcards) {
		this.knightcards = knightcards;
	}

	public int getOpen_knightcards() {
		return open_knightcards;
	}

	public void setOpen_knightcards(int open_knightcards) {
		this.open_knightcards = open_knightcards;
	}

	public int getMonopolcards() {
		return monopolcards;
	}

	public void setMonopolcards(int monopolcards) {
		this.monopolcards = monopolcards;
	}

	public int getRoadcards() {
		return roadcards;
	}

	public void setRoadcards(int roadcard) {
		this.roadcards = roadcard;
	}

	public int getResearchcards() {
		return researchcards;
	}

	public void setResearchcards(int researchcard) {
		this.researchcards = researchcard;
	}

	public int getVictorypointcards() {
		return victorypointcards;
	}

	public void setVictorypointcards(int victorypointcard) {
		this.victorypointcards = victorypointcard;
	}

	public int getVictorypoints() {
		return victorypoints;
	}

	public void setVictorypoints(int victorypoints) {
		this.victorypoints = victorypoints;
	}

	public int getSheep() {
		return sheep;
	}

	public void setSheep(int sheep) {
		this.sheep = sheep;
	}

	public int getClay() {
		return clay;
	}

	public void setClay(int clay) {
		this.clay = clay;
	}

	public int getOre() {
		return ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public int getWheat() {
		return wheat;
	}

	public void setWheat(int straw) {
		this.wheat = straw;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getFree_roads() {
		return free_roads;
	}

	public void setFree_roads(int free_roads) {
		this.free_roads = free_roads;
	}

	public void decreaseVictorypoints() {
		victorypoints--;
	}

	public int drawCard() throws RessourceException, NoMoreCardsException {
		if (ore < 1 || sheep < 1 || wheat < 1) {
			throw new RessourceException();
		}
		ore--;
		sheep--;
		wheat--;
		int drew = board.drawcard();
		switch (drew) {
		case 0:
			knightcards++;
			return 3;
		case 1:
			monopolcards++;
			return 1;
		case 2:
			roadcards++;
			return 0;
		case 3:
			researchcards++;
			return 2;
		case 4:
			victorypointcards++;
			return 4;
		}
		return drew;

	}
	
	public void increaseVictoryPoints() throws WinnerException {
		if (victorypoints < 9) {
			victorypoints++;
		} else {
			victorypoints++;
			throw new WinnerException();
		}
	}

	public void playKnightCard() throws MustOwnCardException,
	PlayerNotOnTileException, WinnerException{
		if (knightcards < 1) {
			throw new MustOwnCardException();
		}
		open_knightcards++;
		knightcards--;
		if (board.getKnightforce() == null && open_knightcards > 2) {
			board.setKnightforce(this);
			this.increaseVictoryPoints();
			this.increaseVictoryPoints();
		} else if(board.getKnightforce() == null){
			
		} else if (board.getKnightforce().getOpen_knightcards() < open_knightcards) {
			board.getKnightforce().decreaseVictorypoints();
			board.getKnightforce().decreaseVictorypoints();
			board.setKnightforce(this);
			this.increaseVictoryPoints();
			this.increaseVictoryPoints();
		}
	}

	public void playVictoryPointCard() throws MustOwnCardException, WinnerException {
		if (victorypointcards < 1) {
			throw new MustOwnCardException();
		}
		victorypointcards--;
		this.increaseVictoryPoints();
	}

	public String playMonopolcard(String ressource) throws MustOwnCardException {
		if (monopolcards < 1) {
			throw new MustOwnCardException();
		}
		String resourceGotten = "";
		
		monopolcards--;
		int amount = 0;
		switch (ressource) {
		case "3":
			resourceGotten = "Schafe";
			for (Player p : board.getCatan().getPl()) {
				if (!p.equals(this)) {
					amount += p.getSheep();
					sheep = sheep + p.getSheep();
					p.setSheep(0);
				}
			}
			break;
		case "4":
			resourceGotten = "Lehm" ; 
			for (Player p : board.getCatan().getPl()) {
				if (!p.equals(this)) {
					amount += p.getClay();
					clay = clay + p.getClay();
					p.setClay(0);
				}
			}
			break;
		case "2":
			resourceGotten = "Erz";
			for (Player p : board.getCatan().getPl()) {
				if (!p.equals(this)) {
					amount += p.getOre();
					ore = ore + p.getOre();
					p.setOre(0);
				}
			}
			break;
		case "0":
			resourceGotten = "Getreide";
			for (Player p : board.getCatan().getPl()) {
				if (!p.equals(this)) {
					amount += p.getWheat();
					wheat = wheat + p.getWheat();
					p.setWheat(0);
				}
			}
			break;
		case "1":
			resourceGotten = "Holz";
			for (Player p : board.getCatan().getPl()) {
				if (!p.equals(this)) {
					amount += p.getWood();
					wood = wood + p.getWood();
					p.setWood(0);
				}
			}
			break;
		}
		String res = getName() + " hat eine Monopolkarte gespielt und " + amount + " " + resourceGotten + " bekommen";
		return res;
		
	}

	public String playResearchCard(String ressource) throws MustOwnCardException {
		if (researchcards < 1) {
			throw new MustOwnCardException();
		}
		researchcards--;
		switch (ressource) {
		case "3":
			sheep++;
			sheep++;
			return getName()+" Hat eine Erfinderkarte gespielt und 2x Schafe bekommen.";
		case "4":
			clay++;
			clay++;
			return getName()+" Hat eine Erfinderkarte gespielt und 2x Lehm bekommen.";
		case "2":
			ore++;
			ore++;
			return getName()+" Hat eine Erfinderkarte gespielt und 2x Erz bekommen.";
		case "0":
			wheat++;
			wheat++;
			return getName()+" Hat eine Erfinderkarte gespielt und 2x Getreide bekommen.";
		case "1":
			wood++;
			wood++;
			return getName()+" Hat eine Erfinderkarte gespielt und 2x Holz bekommen.";
		}
		return null;
	}

	public void playRoadCard() throws MustOwnCardException {
		if (roadcards < 1) {
			throw new MustOwnCardException();
		}
		free_roads += 2;
		roadcards--;
	}

	public String buildRoad(int road)
			throws PlayersPropertyException, NotConnectedException, RessourceException, NoFigureException {
		if (board.getRoads()[road].getOwner() != null) {
			throw new PlayersPropertyException();
		}

		if (roads <= 0) {
			throw new NoFigureException();
		}

		if ((clay < 1 || wood < 1) && free_roads < 1) {
			throw new RessourceException();
		}

		boolean nocon = true;
		for (int i = 0; i < board.getRoads()[road].getCrossroads().length; i++) {
			if (board.getRoads()[road].getCrossroads()[i].getOwner() == this) {
				nocon = false;
			}
			for (int j = 0; j < board.getRoads()[road].getCrossroads()[i].getRoads().length; j++) {
				if (board.getRoads()[road].getCrossroads()[i].getRoads()[j].getOwner() == this) {
					nocon = false;
				}
			}
		}
		if (nocon) {
			throw new NotConnectedException();
		}

		// Second road, must be connected to the second city
		if (roads == 14) {
			Road r = board.getRoads()[road];
			boolean settle2 = true;
			boolean conSettle = false;
			for (int i = 0; i < 2; i++) {

				// check if the Crossroads belong to the player
				// one of the two belonging to the player (if both)
				if (r.getCrossroads()[i].getOwner() != null && r.getCrossroads()[i].getOwner().equals(this)) {
					conSettle = true;
					for (int j = 0; j < 3; j++) {
						if (r.getCrossroads()[i].getRoads()[j] != null) {
							if (r.getCrossroads()[i].getRoads()[j].getOwner() != null) {
								settle2 = false;
							}
						}

					}
				}
			}
			// Not the second city
			if ((!settle2) || !conSettle) {
				throw new NotConnectedException();
			}
		}

		board.getRoads()[road].setOwner(this);
		roads--;
		// resource Costs
		if (free_roads > 0) {
			free_roads--;
		} else {
			clay--;
			wood--;
		}
		// TODO check if Player equals TraderroutePlayer
		return "buildRoad,accept," + road;
	}
	
	public String buildSettlement(int crossroad) throws PlayersPropertyException, NotConnectedException,
			RessourceException, CityTooCloseException, WinnerException, NoFigureException {

		if (board.getCrossroads()[crossroad].getOwner() != null) {
			throw new PlayersPropertyException();
		}

		if (settlements <= 0) {
			throw new NoFigureException();
		}

		Crossroad cross = board.getCrossroads()[crossroad];
		// Schaut ob bereits eine Siedlung/Stadt auf einem direkten Nachbarn
		// steht
		for (int i = 0; i < 3; i++) {
			if (cross.getRoads()[i] != null) {
				for (int j = 0; j < 2; j++) {
					Road road = board.getCrossroads()[crossroad].getRoads()[i];
					// ist die gesuchte kreuzung selbst
					if (road.getCrossroads()[j].equals(cross)) {
					}
					// keine Siedlung/stadt an der anderen seite der Strasse
					else if (road.getCrossroads()[j].getOwner() == null) {
					}
					// es ist eine Siedlung oder stadt auf einer benachbarten
					// kreuzung
					else {
						throw new CityTooCloseException();
					}
				}
			}
		}

		// Teil für die ersten beidein Siedlungen
		if (free_settlement > 0) {
			// Place the settlement and add the victorypoints, subtract number
			// left to place
			board.getCrossroads()[crossroad].setBuild(1);
			board.getCrossroads()[crossroad].setOwner(this);
			free_settlement--;
			settlements--;
			this.increaseVictoryPoints();

			// Resourcen bekommen für die zweite stadt
			if (5 - settlements == 2) {
				for (int i = 0; i < 3; i++) {
					String ress = cross.getTiles()[i].getResource();
					switch (ress) {
					case "sheep":
						sheep++;
						break;
					case "clay":
						clay++;
						break;
					case "ore":
						ore++;
						break;
					case "wood":
						wood++;
						break;
					case "wheat":
						wheat++;
						break;
					}
				}
			}
			// if the crossroad's position is next to a harbor write the harbor in
			// this.harbor
			if (cross.isHarbor()) {
				harbor[0] = true;
			} else if (cross.isSheepHarbor()) {
				harbor[1] = true;
			} else if (cross.isClayHarbor()) {
				harbor[2] = true;
			} else if (cross.isOreHarbor()) {
				harbor[3] = true;
			} else if (cross.isWheatHarbor()) {
				harbor[4] = true;
			} else if (cross.isWoodHarbor()) {
				harbor[5] = true;
			}
			return "buildSettlement,accept," + crossroad;
		}

		if (clay < 1 || sheep < 1 || wood < 1 || wheat < 1) {
			throw new RessourceException();
		}

		boolean nocon = true;
		for (int i = 0; i < board.getCrossroads()[crossroad].getRoads().length; i++) {
			if (board.getCrossroads()[crossroad].getRoads()[i].getOwner() == this) {
				nocon = false;
			}
		}
		if (nocon) {
			throw new NotConnectedException();
		}
		clay--;
		wood--;
		sheep--;
		wheat--;
		board.getCrossroads()[crossroad].setBuild(1);
		board.getCrossroads()[crossroad].setOwner(this);
		settlements--;
		this.increaseVictoryPoints();

		// if the crossroad's position is next to a harbor write the harbor in
		// this.harbor
		if (cross.isHarbor()) {
			harbor[0] = true;
		} else if (cross.isSheepHarbor()) {
			harbor[1] = true;
		} else if (cross.isClayHarbor()) {
			harbor[2] = true;
		} else if (cross.isOreHarbor()) {
			harbor[3] = true;
		} else if (cross.isWheatHarbor()) {
			harbor[4] = true;
		} else if (cross.isWoodHarbor()) {
			harbor[5] = true;
		}

		return "buildSettlement,accept," + crossroad;
	}

	public String buildCity(int city)
			throws NoSettlementException, RessourceException, WinnerException, NoFigureException {
		if (board.getCrossroads()[city].getOwner() != this || board.getCrossroads()[city].getBuild() != 1) {
			throw new NoSettlementException();
		}
		if (ore < 3 || wheat < 2) {
			throw new RessourceException();
		}
		if (cities <= 0) {
			throw new NoFigureException();
		}

		board.getCrossroads()[city].setBuild(2);
		ore -= 3;
		wheat -= 2;
		cities--;
		settlements++;
		increaseVictoryPoints();
		return "buildCity,accept," + city;

	}
	
	public String steal(int tile, Player victim) throws PlayerNotOnTileException{
		boolean nocon = true;
		for (int i = 0; i < board.getTiles()[tile].getCrossroads().length; i++){
			if(board.getTiles()[tile].getCrossroads()[i].getOwner().equals(victim)){
				nocon = false;
			}
		}
		if(nocon){
			throw new PlayerNotOnTileException();
		}
		board.setThief(board.getTiles()[tile]);
		int temp = victim.sheep + victim.wheat + victim.wood+ victim.clay + victim.ore;
		Random rand = new Random();
		int foo = rand.nextInt(temp);
		if(foo < sheep){
			victim.sheep--;
			sheep++;
			return "chat,Du hast Schafe erhalten!";
		}
		else if(foo < sheep + wheat){
			victim.wheat--;
			wheat++;
			return "chat,Du hast Getreide erhalten!";
		}
		else if(foo < sheep + wheat + wood){
			victim.wood--;
			wood++;
			return "chat,Du hast Holz erhalten!";
		}
		else if(foo < sheep + wheat+ wood + clay){
			victim.clay--;
			clay++;
			return "chat,Du hast Lehm erhalten!";
		}
		else{
			victim.ore--;
			ore++;
			return "chat,Du hast Erz erhalten!";
		}
	}
	
	public String getSettlementPlaces(){
		return null;
	}
	
	public String getCityPlaces(){
		return null;
	}
	
	public String getRoadPlaces(){
		return null;
	}
	
	public void UserToPlayer(Board board){
		this.board = board;
		roads = 15;
		settlements = 5;
		cities = 4;
		free_roads = 2;
		free_settlement = 2;
	}

	public int getLongestRoad() {
		return longestRoad;
	}

	public void setLongestRoad(int longestRoad) {
		this.longestRoad = longestRoad;
	}
	
	public void trade(String resourceGet, int get, String resourceGive, int give) throws RessourceException, OfferToPlayerException{
		switch(resourceGive){
		case "sheep": 
			if(sheep < give*(-1)){
				throw new RessourceException();
			}
			//for(int i = 0; i < harbor.length; i++){
			//	System.out.println(i + " " + harbor[i]);
			//}
			if(harbor[1]){
				if(give*(-1) < 2*get || sheep < 2*get){
					throw new OfferToPlayerException();
				}
				sheep -= 2*get;
				switch(resourceGet){
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else if(harbor[0]){
				if(give*(-1) < 3*get || sheep < 3*get){
					throw new OfferToPlayerException();
				}
				sheep -= 3*get;
				switch(resourceGet){
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else{
				if(give *(-1)< 4*get || sheep < 4*get){
					throw new OfferToPlayerException();
				}
				sheep -= 4*get;
				switch(resourceGet){
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}
			break;
		case "clay": 
			if(clay < give*(-1)){
				throw new RessourceException();
			}
			if(harbor[2]){
				if(give*(-1) < 2*get || clay < 2*get){
					throw new OfferToPlayerException();
				}
				clay -= 2*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else if(harbor[0]){
				if(give*(-1) < 3*get || clay < 3*get){
					throw new OfferToPlayerException();
				}
				clay -= 3*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else{
				if(give*(-1) < 4*get || clay < 4*get){
					throw new OfferToPlayerException();
				}
				clay -= 4*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}
			break;
		case "ore": 
			if(ore < give*(-1)){
				throw new RessourceException();
			}
			if(harbor[3]){
				if(give*(-1) < 2*get || ore < 2*get){
					throw new OfferToPlayerException();
				}
				ore -= 2*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else if(harbor[0]){
				if(give*(-1) < 3*get || ore < 3*get){
					throw new OfferToPlayerException();
				}
				ore -= 3*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else{
				if(give*(-1) < 4*get || ore < 4*get){
					throw new OfferToPlayerException();
				}
				ore -= 4*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "wheat":
					wheat += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}
			break;
		case "wheat": 
			if(wheat < give*(-1)){
				throw new RessourceException();
			}
			if(harbor[4]){
				if(give*(-1) < 2*get || wheat < 2*get){
					throw new OfferToPlayerException();
				}
				wheat -= 2*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else if(harbor[0]){
				if(give*(-1) < 3*get || wheat < 3*get){
					throw new OfferToPlayerException();
				}
				wheat -= 3*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}else{
				if(give*(-1) < 4*get || wheat < 4*get){
					throw new OfferToPlayerException();
				}
				wheat -= 4*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wood":
					wood += get;
					break;
				}
			}
			break;
		case "wood": 
			if(wood < give*(-1)){
				throw new RessourceException();
			}
			if(harbor[5]){
				if(give*(-1) < 2*get || wood < 2*get){
					throw new OfferToPlayerException();
				}
				wood -= 2*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				}
			}else if(harbor[0]){
				if(give*(-1) < 3*get || wood < 3*get){
					throw new OfferToPlayerException();
				}
				wood -= 3*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				}
			}else{
				if(give*(-1) < 4*get || wood < 4*get){
					throw new OfferToPlayerException();
				}
				wood -= 4*get;
				switch(resourceGet){
				case "sheep":
					sheep += get;
					break;
				case "clay":
					clay += get;
					break;
				case "ore":
					ore += get;
					break;
				case "wheat":
					wheat += get;
					break;
				}
			}
			break;
		}
		
	}
	
	public boolean tradePossible(int[] res){
		if(wheat < res[0]){
			return false;
		}
		if(wood < res[1]){
			return false;
		}
		if(ore < res[2]){
			return false;
		}
		if(sheep < res[3]){
			return false;
		}
		if(clay < res[4]){
			return false;
		}
		return true;
	}//"wheat", "wood", "ore", "sheep", "clay"
	
	public void playerTrade(int wheat, int wood, int ore, int sheep, int clay, Player offer){
		this.wheat -= wheat;
		offer.wheat += wheat;
		this.wood -= wood;
		offer.wood += wood;
		this.ore -= ore;
		offer.ore += ore;
		this.sheep -= sheep;
		offer.sheep += sheep;
		this.clay -= clay;
		offer.clay += clay;
	}

}
