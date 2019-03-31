package games.Catan;

import java.io.IOException;
import java.util.ArrayList;

import gameClasses.Game;
import gameClasses.GameState;
import global.FileHelper;
import userManagement.User;

@SuppressWarnings("unused")
public class Catan extends Game {



	// phase: 0 = rolldice
	// 1 = thief
	// 2 = playCard / drawCard / trade / build
	//

	private int phase = -1;
	private boolean working = false;
	private User playerTurn = null;
	private ArrayList<User> playerList = new ArrayList<User>();
	private ArrayList<Player> pl = new ArrayList<Player>();
	private ArrayList<User> spectatorList = new ArrayList<User>();
	private int turnCounter = 1;
	private String playerLeft = null;
	private int[] res;
	private Board board = new Board(this);
	private String chat = "";
	private String users = "";
	private String send;
	private boolean turn3 = false;
	private int lastDrawn = 0;
	private int thiefTile = -1;
	private String trade = "";
	private ArrayList<User> traders = new ArrayList<User>();
	private int knight = 0;
	private String end;
	private String status;
	private boolean thief;
	private boolean built = false;

	
	@Override
	public int getMaxPlayerAmount() {
		return 4;
	}

	@Override
	public int getCurrentPlayerAmount() {
		return playerList.size();
	}

	@Override
	public String getSite() {
		try {
			return FileHelper.getFile("Catan/Catan.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void execute(User user, String gsonString) {
		
		if(gsonString.equals("stop")){
			working = false;
		}
		
		
		if (gsonString.equals("GETME")) {
			send = "" + getUserIndex(user);
			sendDataToUser(user, "ME");
		}
		if (gsonString.equals("abcd")) {
			System.out.println("String vom Client angekommen");
			sendDataToClients("abcd");
		}
		if (gsonString.equals("GETUSER")) {
			sendDataToClients("USERJOINED");
		}

		if (gsonString.equals("ADDAI")) {
			AI ai = new AI(this.board);
			addAi(ai);
			return;
		}

		if (gsonString.equals("SETUP")) { // at this point the game is being
											// started
			nextPlayer();
			this.gState = GameState.RUNNING;
			sendDataToClients("SETUP");
			return;
		}

		if (this.gState == GameState.CLOSED)
			return;

		if (gsonString.equals("CLOSE")) {
			sendDataToClients("CLOSE");
			closeGame();
			return;
		}

		if (gState != GameState.RUNNING)
			return;
		if (working || user !=playerTurn){
			if(gsonString.contains("CHAT")){
				
			}else if(traders.isEmpty()){
				return;
			}else{
				if(!(gsonString.contains("ACCEPTTRADE")|| gsonString.equals("REJECTTRADE"))){
					return;
				}
			}
		}

		if(built && !gsonString.contains("CHAT") && !gsonString.contains("finish")){
			return;
		}

		if(user != null && !playerList.contains(user)){

			return;
		}
		String[] command = gsonString.split(",");

		if (command[0].equals("buildSettlement")) {

		//	sendDataToClients("END");

			if (phase != 2) {
				return;
			}
			try {
				working = true;
				pl.get(getUserIndex(user)).buildSettlement(Integer.parseInt(command[1]));
				if (pl.get(getUserIndex(user)).getFree_settlement() < 2) {
					chat = 4 + "Du musst eine Siedlung und eine daran anliegende Strasse bauen.";
					if(!user.getClass().getSimpleName().equals("AI")){
						sendDataToUser(user, "CHAT");
					}
				}
				// updateVictoryPoints();
				updateField();
				board.getTradeRoutePlayer();
				updateResources();
				working = false;
			} catch (NumberFormatException e) {
				System.err.println("Crossroad did not contain a number");
			} catch (PlayersPropertyException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (NotConnectedException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (RessourceException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (WinnerException e) {
				sendDataToClients("END");
				gState = GameState.FINISHED;
			} catch (CityTooCloseException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (NoFigureException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
			working = false;
		} else if (command[0].equals("buildCity")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				String send = pl.get(getUserIndex(user)).buildCity(Integer.parseInt(command[1]));
				// sendDataToClients(send+","+getUserIndex(user));
				updateField();
				board.getTradeRoutePlayer();
				updateResources();
				working = false;
			} catch (NumberFormatException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (NoSettlementException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (RessourceException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (WinnerException e) {
				sendDataToClients("END");
				gState = GameState.FINISHED;
			} catch (NoFigureException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
			working = false;
		} else if (command[0].equals("buildRoad")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				pl.get(getUserIndex(user)).buildRoad(Integer.parseInt(command[1]));
				// updateVictoryPoints();
				if(!turn3){
					built = true;
				}
				updateField();
				board.getTradeRoutePlayer();
				updateResources();
				working = false;
			} catch (NumberFormatException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (PlayersPropertyException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (NotConnectedException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (RessourceException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (WinnerException e) {
				sendDataToClients("END");
				gState = GameState.FINISHED;
			} catch (NoFigureException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
			working = false;
		} else if (command[0].equals("BUYEVENT")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				lastDrawn = pl.get(getUserIndex(user)).drawCard();
				chat = 4 + user.getName() + " hat eine Ereigniskarte gezogen.";
				sendDataToClients("CHAT");
				updateCards();
				working = false;
			} catch (RessourceException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (NoMoreCardsException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
			updateResources();
			working = false;
		} else if (command[0].equals("PLAYEVENT4")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				pl.get(getUserIndex(user)).playVictoryPointCard();
				chat = 4 + user.getName() + " hat Siegpunktkarte gespielt.";
				sendDataToClients("CHAT");
				updateCards();
				working = false;
			} catch (MustOwnCardException e) {
				chat = 4+ e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (WinnerException e) {
				sendDataToClients("END");
				gState = GameState.FINISHED;
			}
			working = false;
		} 
		//Monopol card
		else if (command[0].equals("PLAYEVENT1")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				chat = 4 + pl.get(getUserIndex(user)).playMonopolcard(command[1]);
				sendDataToClients("CHAT");
				updateResources();
				updateCards();
				working = false;
			} catch (MustOwnCardException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
			working = false;
		} 
		//Ritterkarte
		else if (command[0].equals("PLAYEVENT3")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				pl.get(getUserIndex(user)).playKnightCard();
				chat = 4 + user.getName() + " hat eine Ritterkarte gespielt";
				sendDataToClients("CHAT");
				phase = 1;
				knight = 1;
				sendDataToUser(playerTurn, "MOVETHIEF");
				updateResources();
				updateCards();
				working = false;
			} catch (NumberFormatException e) {
				System.err.println("thiefTile didn't contain a number");
			} catch (MustOwnCardException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			} catch (WinnerException e) {
				sendDataToClients("END");
				gState = GameState.FINISHED;
			} catch (PlayerNotOnTileException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
		} 
		// Research card
		else if (command[0].equals("PLAYEVENT2")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				String res = pl.get(getUserIndex(user)).playResearchCard(command[1]);
				chat = 4 + res;
				sendDataToClients("CHAT");
				updateResources();
				updateCards();
				working = false;
			} catch (MustOwnCardException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
			working = false;
		} else if (command[0].equals("PLAYEVENT0")) {
			if (phase != 2) {
				return;
			}
			try {
				working = true;
				pl.get(getUserIndex(user)).playRoadCard();
				chat = 4 + user.getName() + " hat eine Strassenkarte gespielt";
				sendDataToClients("CHAT");
				updateCards();
				working = false;
			} catch (NumberFormatException e) {
				System.err.println("road1 or road2 didn't contain a number");
			} catch (MustOwnCardException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
			}
			working = false;
		} else if (command[0].equals("SETTHIEF")) {
			working  = true;
			if (phase != 1) {
				return;
			}
			try {
				thiefTile = Integer.parseInt(command[1]);
				String ress = board.placeThief(Integer.parseInt(command[1]), null, pl.get(getUserIndex(user)));
				if(ress.equals("niemanden")){
					phase = 2;
					lastDrawn = 4;
					chat = 4 + user.getName() + " hat " +
					ress + " beklaut.";
					sendDataToClients("CHAT");
					
				}
				else if(ress.length() == 1){
					phase = 2;
					lastDrawn = Integer.parseInt(ress);
					chat = 4 + user.getName() + " hat " +
					pl.get(Integer.parseInt(ress)).getName() + " beklaut.";
					for(User u : playerList){
						if(u != user && u != playerList.get(Integer.parseInt(ress))){
							sendDataToUser(u, "CHAT");
						}
					}
				}
				
				else if(ress.length() > 1){
					chat = ress;
					sendDataToUser(user, "SELECTVICTIM");
					working = false;
					return;
				}
				updateThief();
				updateResources();
			} catch (NumberFormatException e) {
				System.err.println("NFE in setthief");
			} catch (PlayerNotOnTileException e) {
				System.err.println("setthief player not on tile");
			}
			working = false;
		}
		else if(command[0].equals("STEALFROM")){
			if (phase != 1) {
				return;
			}
			try {
				lastDrawn = Integer.parseInt(command[1]);
				System.out.println("victim is: " + Integer.parseInt(command[1]));
				String ress = board.placeThief(-1, pl.get(Integer.parseInt(command[1])), pl.get(getUserIndex(user)));
				//more than 1 player on the tile
				//one needs to be chosen
				if(ress.length() > 1){
					chat = ress;
					sendDataToUser(user, "SELECTVICTIM");
					return;
				}
				else{
					phase = 2;
					chat = 4 + user.getName() + " hat " +
					pl.get(lastDrawn).getName() + " beklaut.";
					for(User u : playerList){
						if(u != user && u != playerList.get(lastDrawn)){
							sendDataToUser(u, "CHAT");
						}
					}
					sendDataToClients("CHAT");
				}
				updateThief();
				updateResources();
			} catch (NumberFormatException e) {
			} catch (PlayerNotOnTileException e) {
			}
			updateResources();
		} else if (command[0].equals("finish")) {
			
			if (phase < 2) {
				return;
			}
			if (working) {
				return;
			}
			if(pl.get(getUserIndex(user)).getFree_roads()>0){
				return;
			}
			if(pl.get(getUserIndex(user)).getFree_settlement()>0
					&& pl.get(getUserIndex(user)).getSettlements()>0){
				return;
			}
			nextPlayer();
			
		} else if (command[0].equals("CHAT")) {
			if(user == null){
				chat = 4 + command[1];
			}
			else{
				chat = getUserIndex(user) + user.getName() + ": " + command[1];
			}
			
			if(command[1].startsWith("/help")){
				helpChat(command[1], user);
				return;
			}
			sendDataToClients("CHAT");
		}else if(command[0].equals("TRADE")){
			if (phase < 2) {
				return;
			}
			
			int[] tmp = {Integer.parseInt(command[1]), Integer.parseInt(command[2]),
					Integer.parseInt(command[3]), Integer.parseInt(command[4]), Integer.parseInt(command[5])};
			try {
				working = true;
				String[] temp = trade(tmp);
				pl.get(getUserIndex(user)).trade(temp[0],Integer.parseInt(temp[1]), temp[2], Integer.parseInt(temp[3]));
				chat = 4 + "Du hast gehandelt";
				sendDataToUser(user, "CHAT");
				updateResources();
				chat = 4 + user.getName() + "hat mit der Bank gehandelt.";
				sendDataToClients("CHAT");
				working = false;
			} catch (NumberFormatException e) {
			} catch (RessourceException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
				working = false;
			} catch (OfferToPlayerException e) {
				chat = 4 + e.getMessage();
				sendDataToUser(user, "CHAT");
				for(User cur: playerList){
					boolean tr = pl.get(getUserIndex(cur)).tradePossible(tmp);
					if(tr && !cur.equals(user) && !(cur instanceof AI)){
						trade = command[1]+","+command[2]+","+command[3]+","+
								command[4]+","+command[5]+","+user.getName();
						sendDataToUser(cur,"TRADE");
						traders.add(cur);
					}
				}
				if(trade == ""){
					chat = 4 + "Alle Spieler haben deinen Handel abgelehnt.";
					sendDataToUser(user, "CHAT");
					working  = false;
				}
			}
		}
		else if(command[0].equals("ACCEPTTRADE")){
			if(!traders.contains(user)){
				return;
			}
			for(User u: traders){
				sendDataToUser(u, "TRADECANCELLED");
			}
			traders.clear();
			working = true;
			String[] t = trade.split(",");
			int[] tr = new int[5];
			for(int i = 0; i < 5; i++){
				tr[i] = Integer.parseInt(t[i]);
			}
			pl.get(getUserIndex(user)).playerTrade(tr[0], tr[1], tr[2], tr[3], tr[4], pl.get(getUserIndex(t[t.length-1])));
			updateResources();
			chat = 4 + t[t.length-1] + " hat mit "+ user.getName() + " gehandelt.";
					sendDataToClients("CHAT");
			trade = "";
			working  = false;
			
		}else if(command[0].equals("REJECTTRADE")){
			working = true;
			int index = -1;
			for(User cur: traders){
				index++;
				if (cur.getName().equals(user.getName())){
					traders.remove(index);
				}
			}
			if(traders.isEmpty()){
				String[] t = trade.split(",");
				trade = "";
				chat = 4 + "Alle Spieler haben den handel von "+ t[t.length-1] + " abgelehnt";
				System.out.println("hier kam alle spieler haben den handel von abgelehnt");
				sendDataToClients("CHAT");
			}
			working = false;
		}

	}

	@Override
	public String getGameData(String eventName, User user) {
		
		String gameData = "";
		if(eventName.equals("STATUS")){
			return status;
		}
		if(eventName.equals("SELECTVICTIM")){
			return chat;
		}
		if(eventName.equals("THIEFMOVED")){
			for(int i = 0; i < 54; i++){
				if(board.getThief() == board.getTiles()[i]){
					if(lastDrawn == 4){
						return "" + i + "," +  pl.get(getUserIndex(playerTurn)).getName() + "," + pl.get(getUserIndex(playerTurn)).getName();
					}
					return "" + i + "," +  pl.get(getUserIndex(playerTurn)).getName() + "," + pl.get(lastDrawn).getName();
				}
			}
		}
		if(eventName.equals("MOVETHIEF")){
			return "" + knight;
		}
		if (eventName.equals("WHOSETURN")) {
			return "" + getUserIndex(playerTurn);
		}
		if (eventName.equals("START")) {
			return "Spielinstanz gestartet";
		}

		if (eventName.equals("SETUP")) {
			board.init();
			gameData = this.board.getStatus();
			return gameData;
		}
		if (eventName.equals("UPDATEFIELD")) {
			gameData = this.board.updateField();
			return gameData;
		}
		if (eventName.equals("UPDATERESOURCE")) {
			Player p = pl.get(getUserIndex(user));
			// values 0-4, resources of the player it is sent to
			String str = "" + p.getWheat() + "," + p.getWood() + "," + p.getOre() + "," + p.getSheep() + ","
					+ p.getClay();
			// values 5-8, total resources of all players
			for (Player u : pl) {
				str += "," + (u.getWheat() + u.getWood() + u.getOre() + u.getClay() + u.getSheep());
			}
			if (pl.size() < 4) {
				for (int i = pl.size(); i < 4; i++) {
					str += ",0";
				}
			}
			// digits 9-12, total number of hidden "entwicklungskarten"
			for (Player u : pl) {
				str += "," + (u.getKnightcards() + u.getMonopolcards() + u.getResearchcards() + u.getRoadcards()
						+ u.getVictorypointcards());
			}
			if (pl.size() < 4) {
				for (int i = pl.size(); i < 4; i++) {
					str += ",1";
				}
			}
			// digits 13-16, number of current victorypoints
			for (Player u : pl) {
				str += "," + u.getVictorypoints();
			}
			if (pl.size() < 4) {
				for (int i = pl.size(); i < 4; i++) {
					str += ",2";
				}
			}
			// digits 17-20, amount of open knight cards
			for (Player u : pl) {
				str += "," + u.getOpen_knightcards();
			}
			if (pl.size() < 4) {
				for (int i = pl.size(); i < 4; i++) {
					str += ",3";
				}
			}
			// digits 21-24, longest road the player currently has
			for (Player u : pl) {
				str += "," + u.getLongestRoad();
			}
			if (pl.size() < 4) {
				for (int i = pl.size(); i < 4; i++) {
					str += ",4";
				}
			}
			//longest traderouteplayers id
			if(board.getTraderoute() == null){
				str += ",4";
			}
			else{
				str += "," + getUserIndex(board.getTraderoute());
			}
			//knightforceplayer
			if(board.getKnightforce() == null){
				str += ",4";
			}
			else{
				str += "," + getUserIndex(board.getKnightforce());
			}
			
			
			// for safety reasons
			str += ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";

			gameData = str;
			return gameData;
		}
		if (eventName.equals("UPDATEDICE")) {
			gameData = "" + res[0] + res[1];
			return gameData;
		}
		if (eventName.equals("UPDATEEVENT")) {
			Player p = pl.get(getUserIndex(user));
			gameData = p.getRoadcards() +"," + p.getMonopolcards() +
					"," + p.getResearchcards() + "," + p.getKnightcards() +
					"," + p.getVictorypointcards() + "," + lastDrawn;
			return gameData;
		}
		if (eventName.equals("CHAT")) {
			gameData = chat;
			if (chat.contains("/")) {
				if (chat.contains("/tableflip")) {
					return "4(â•¯Â°â–¡Â°ï¼‰â•¯ï¸µ â”»â”�â”»";
				} else if (chat.contains("/lenny")) {
					return "4( Í¡Â° ÍœÊ– Í¡Â°)";
				}
			}

			return gameData;
		}
		if(eventName.equals("TRADE")){
			return trade;
		}
		if (eventName.equals("abcd")) {
			return "String vom Server an Client";
		}
		if (eventName.equals("USERJOINED")) {
			gameData = users;
			return gameData;
		}
		if (eventName.equals("ME")) {
			gameData = send;
		}
		if(eventName.equals("END")){
			for(Player cur: pl){
				if(cur.getVictorypoints()>=10){
					return cur.getName();
				}
			}
			return null;
		}
		if(eventName.equals("TRADECANCELLED")){
			return "";
		}
		return gameData;
	}

	public void setChat(String chat){
		this.chat = chat;
	}
	
	@Override
	public void addUser(User user) {
		if(gState.equals(GameState.RUNNING)){
			spectatorList.add(user);
			updateField();
			return;
		}
		if (playerList.size() < 4 && !playerList.contains(user)) {// vllt eher
			// ...size<4??
			playerList.add(user);
			Player p = new Player(user, board);
			pl.add(p);

			if (users.equals("")) {
				users = user.getName();
			} else {
				users = users + "," + user.getName();
			}
			sendDataToClients("USERJOINED");

			if (playerTurn == null) {
				playerTurn = user;
			}
			sendDataToClients("START");
		}
		/*if (playerList.size() == 4) {
			this.gState = GameState.RUNNING;
			sendDataToClients("START");
			updateField();
		} */
	}

	public void addAi(AI ai) {
		if (playerList.size() < 4 && !playerList.contains(ai)) {
			playerList.add(ai);
			Player p = new Player(ai, board);
			pl.add(p);

			if (users.equals("")) {
				users = ai.getName();
			} else {
				users = users + "," + ai.getName();
			}
			sendDataToClients("USERJOINED");

			if (playerTurn == null) {
				playerTurn = ai;
			}
			sendDataToClients("START");
		}
//		if (playerList.size() == 4) {
//			this.gState = GameState.RUNNING;
//			sendDataToClients("START");
//			board = new Board(this);
//			board.init();
//			updateField();
//		}
	}

	@Override
	public void addSpectator(User user) {
		this.spectatorList.add(user);
	}

	@Override
	public void playerLeft(User user) {
		playerList.remove(user);
		playerLeft = user.getName();
		sendDataToClients("PLAYERLEFT");
	}

	@Override
	public boolean isJoinable() {
		if (playerList.size() < 4) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GameState getGameState() {
		return this.gState;
	}

	@Override
	public String getCSS() {
		try {
			return global.FileHelper.getFile("Catan/css/Catan.css");
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	public String getJavaScript() {
		return "<script src=\"javascript/Catan.js\"></script>";
	}
	
	@Override
	public ArrayList<User> getPlayerList() {
		return this.playerList;
	}

	public ArrayList<Player> getPl() {
		return pl;
	}

	@Override
	public ArrayList<User> getSpectatorList() {
		return this.spectatorList;
	}

	
	private void updateResources() {
		for (int i = 0; i < playerList.size(); i++) {
			User u = playerList.get(i);
			sendDataToUser(u, "UPDATERESOURCE");
		}
	}

	private void updateCards() {
		sendDataToClients("UPDATEEVENT");
	}

	private void nextPlayer() {
		built = false;
		if (phase == -1) {// first turn, first player
			playerTurn = playerList.get(0);
			phase = 2;
			turnCounter = 1;
		} else {
			if (turnCounter != 2) {
				//System.out.println("not second turn");
				if (turnCounter == 3 && !turn3) {
					turn3 = true;
				}
				else{
					playerTurn = playerList.get((getUserIndex(playerTurn)+1)%playerList.size());
					if (playerTurn.getClass().getSimpleName().equals("AI") && turnCounter == 1){
						((AI) playerTurn).startBotTurn();
					}
					if (getUserIndex(playerTurn) == 0) {
						turnCounter++;
						System.out.println("__________________________________________________________");
						if (turnCounter == 2) {
							for (Player p : pl) {
								p.setFree_roads(1);
								p.setFree_settlement(1);
							}
						}
					}
				}
			}

			if (turnCounter == 2) {// second turn
				if (getUserIndex(playerTurn) == 0) {
					playerTurn = playerList.get(playerList.size() - 1);
					if (playerTurn.getClass().getSimpleName().equals("AI") && turnCounter == 2) {
						((AI) playerTurn).startBotTurn();
					}
				} else {
					
					playerTurn = playerList.get(getUserIndex(playerTurn) - 1);
					if (playerTurn.getClass().getSimpleName().equals("AI") && turnCounter == 2) {
						((AI) playerTurn).startBotTurn();
//						int settlementid = AI.placeStartSettlement(this.board.getCrossroads());
//						execute(playerTurn, "buildSettlement," + settlementid);
//						execute(playerTurn, "buildRoad," + this.board.getCrossroads()[settlementid].getRoads()[1].getId());
					}
					if (getUserIndex(playerTurn) == 0) {
						turnCounter = 3;
					}
				}
			}

		}

		if (turnCounter > 2 && turn3) {

			phase = 0;
			res = board.rollDice();
			if (res[0] + res[1] == 7) {
				 phase = 1;
				// updateField();
				 updateResources();
				 knight = 0;

				 if(playerTurn.getClass().getSimpleName().equals("AI")){
					 AI ai = (AI) playerTurn;
					 execute(playerTurn, "SETTHIEF," + ai.getBestThiefTile(this.board.getTiles()));
					 phase = 2;
				 }
				 
				 sendDataToUser(playerTurn, "MOVETHIEF");
				
				 for(User u : playerList){
					 if(u == playerTurn){
						 
					 }
					 else{
						 sendDataToUser(u , "WHOSETURN");
					 }
				 }
			} else {
				phase = 2;
				sendDataToClients("WHOSETURN");
				
				if(playerTurn.getClass().getSimpleName().equals("AI")){
					((AI) playerTurn).botturn();
//					execute(playerTurn,"BUYEVENT");
					((AI) playerTurn).message(getUserIndex(playerTurn)); 
				}
			}
			sendDataToClients("UPDATEDICE");

			updateResources();
//			if (getClass().getSimpleName().equals("AI")) {
//				execute(playerTurn, "finish");
//			}
		}
		else{
			sendDataToClients("WHOSETURN");
		}
		
		//Ausgeben wer dran ist in STATUS
		for(User u : playerList){
			if(u == playerTurn){
				status = "Du bist dran";
			}
			else{
				status = playerTurn.getName() + " ist dran";
			}
			sendDataToUser(u, "STATUS");
		}
		
		if(playerTurn.getClass().getSimpleName().equals("AI"))
			execute(playerTurn, "finish");	
		
		
	}

	
	private void updateField() {
		sendDataToClients("UPDATEFIELD");
	}
	private void updateThief(){
		sendDataToClients("THIEFMOVED");
	}

	public int getUserIndex(User user) {
		if(user == null){
			return 4;
		}
		for (int i = 0; i < playerList.size(); i++) {
			if (user.getName().equals(playerList.get(i).getName())) {
				return i;
			}
		}
		System.err.println("user given to execute was not found in playerList");
		return 4;
	}
	public int getUserIndex(String user) {
		for (int i = 0; i < playerList.size(); i++) {
			if (user.equals(playerList.get(i).getName())) {
				return i;
			}
		}
		System.err.println("user given to execute was not found in playerList");
		return 4;
	}

	public String toGerman(String str) {
		switch (str) {
		case "ore":
			return "Erz";
		case "wheat":
			return "Getreide";
		case "clay":
			return "Lehm";
		case "sheep":
			return "Schaaafe";
		case "wood":
			return "Holz";
		}
		return null;
	}

	public void sendDataToUser (User u, String eventName){
		if(!u.getClass().getSimpleName().equals("AI")){
			sendGameDataToUser(u, eventName);
		}
	}
	
	public void sendDataToClients (String eventName){
		for(User u : playerList){
			sendDataToUser(u,eventName);
		}
		for(User u: spectatorList){
			sendDataToUser(u,eventName);
		}
	}
	
	private String[] trade(int[] res) throws OfferToPlayerException{
		for(int i=0; i<res.length; i++){
		}
		int getCount = 0;
		int giveCount = 0;
		String[] resnames = {"wheat", "wood", "ore", "sheep", "clay"};
		String [] result = new String[4];
		for(int i = 0; i < res.length; i++){
			if(res[i]>0){
				getCount++;
			}else if(res[i] < 0){
				giveCount++;
			}
		}
		if(giveCount!= 1|| getCount!=1){
			throw new OfferToPlayerException();
		}
		for(int i = 0; i < res.length; i++){
			if(res[i]>0){
				result[0] = resnames[i];
				result[1] = ""+res[i];
			}else if(res[i] < 0){
				result[2] = resnames[i];
				result[3] = ""+res[i];
			}
		}
		return result;
	}
	
	private void helpChat(String str, User user){
		if(str.equals("/help")){
			chat = "4 <p>/help</p>";
			chat += "<p>\"/help kosten\" um die Kosten anzuzeigen</p>";
		}
		else if(str.equals("/help kosten")){
			chat = "4BILDKOSTEN";
		}
		sendDataToUser(user, "CHAT");
	}
	

	public String getChat() {
		return chat;
	}


}
