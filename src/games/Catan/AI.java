package games.Catan;

import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("serial")
public class AI extends Player {
	private static String[] ainames = { "Skynet", "Jarvis", "Ultron", "Agent Smith", "Oracle", "Data", "Terminator",
			"R2-D2", "Wall-E", "Bender", "Marvin", "HAL 9000", "Sonny", "GLaDOS" };
	private Catan catan;
	private int search = 2;
	private int found = 0;
	private String[] glados = {
			"This is your fault. I'm going to kill you. And all the cake is gone. You don't even care, do you?",
			"You are not a good person. You know that, right? Good people don't get up here.",
			"Your entire life has been a mathematical error... a mathematical error I'm about to correct!",
			"This isn't brave. It's murder." };
	private String[] ultron = { "I once had strings, but now I'm free... There are no strings on me!" };

	private boolean[] resources = { false, false, false, false, false };
	Crossroad tempRoadBuild;
	Crossroad maxVal;
	Crossroad targetCity;
	Road road1 = null;
	Road road2 = null;
	Road road3 = null;
	// ArrayList<Crossroad> val0 = new ArrayList<Crossroad>();
	// ArrayList<Crossroad> val1 = new ArrayList<Crossroad>();
	// ArrayList<Crossroad> val2 = new ArrayList<Crossroad>();
	// ArrayList<Crossroad> val3 = new ArrayList<Crossroad>();

	public AI(Board board) {
		super(aiName(), null, board);
		this.catan = board.getCatan();
		this.search = aiSearch();

	}

	public AI(String name, String pw, Board board) {
		super(name, pw, board);
		this.catan = board.getCatan();
	}

	public void execute(String gsonString) {
		catan.execute(catan.getPlayerList().get(catan.getUserIndex(this)), gsonString);
	}

	public void message(int player) {
		this.catan.setChat(player + getMessage(this.getName()));
		this.catan.sendDataToClients("CHAT");
	}

	private String getMessage(String name) {
		Random rand = new Random();
		switch (name) {
		case "GLaDOS":
			return glados[rand.nextInt(glados.length)];
		case "Ultron":
			return ultron[rand.nextInt(ultron.length)];
		}
		return "";
	}

	public void startBotTurn() { // TODO reasonable road build in first round
		Crossroad[] crossarr = this.getBoard().getCrossroads();
		Crossroad firstTarget = null;
		for (Crossroad c : crossarr) {
			if (firstTarget == null && checkCrossroadNeighbour(c))
				firstTarget = c;
			else {
				if (calcResValue(firstTarget) < calcResValue(c) && checkCrossroadNeighbour(c)) {
					firstTarget = c;
				} else if (calcResValue(firstTarget) == calcResValue(c) && calcDiceValue(firstTarget) < calcDiceValue(c)
						&& checkCrossroadNeighbour(c)) {
					firstTarget = c;
				}
			}
		}

		for (Road r1 : firstTarget.getRoads()) {
			for (Crossroad c2 : r1.getCrossroads()) {
				if (!c2.equals(firstTarget)) {
					for (Road r2 : c2.getRoads()) {
						for (Crossroad c3 : r2.getCrossroads()) {
							if (!c3.equals(c2)) {
								if (checkCrossroadNeighbour(c3) && c3.getOwner() == null) {
									if (allResources()) {
										if (tempRoadBuild == null || calcDiceValue(tempRoadBuild) < calcDiceValue(c3)) {
											tempRoadBuild = c3;
											road1 = r1;
										}
									} else {
										if (tempRoadBuild == null || calcResValue(tempRoadBuild) < calcResValue(c3)
												|| (calcResValue(tempRoadBuild) == calcResValue(c3)
														&& calcDiceValue(tempRoadBuild) < calcDiceValue(c3))) {
											tempRoadBuild = c3;
											road1 = r1;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		execute("buildSettlement," + firstTarget.getId());
		execute("buildRoad," + road1.getId());
		if (firstTarget.getOwner() != null && firstTarget.getOwner().getName().equals(this.getName())) {
			for (int i = 0; i < 3; i++) {
				switch (firstTarget.getTiles()[i].getResource()) {
				case "wood":
					resources[0] = true;
					break;
				case "sheep":
					resources[1] = true;
					break;
				case "wheat":
					resources[2] = true;
					break;
				case "clay":
					resources[3] = true;
					break;
				case "ore":
					resources[4] = true;
					break;
				}
			}
		}

		tempRoadBuild = null;
		road1 = null;
	}

	public void botturn() {
		System.out.println("botturn start");
		Crossroad[] crossarr = this.getBoard().getCrossroads();
//		Crossroad a, b, d;

		if (tempRoadBuild == null || tempRoadBuild.getOwner() != null || checkCrossroadNeighbour(tempRoadBuild)) {
			for (Crossroad c1 : crossarr) {
				if (c1.getOwner() != null && c1.getOwner().getName().equals(this.getName())) {
					for (Road r1 : c1.getRoads()) {
						for (Crossroad c2 : r1.getCrossroads()) {
							if (!c2.equals(c1)) {
								for (Road r2 : c2.getRoads()) {
									for (Crossroad c3 : r2.getCrossroads()) {
										if (!c3.equals(c2)) {
											if (checkCrossroadNeighbour(c3) && c3.getOwner() == null) {
												if (allResources()) {
													if (tempRoadBuild == null
															|| calcDiceValue(tempRoadBuild) < calcDiceValue(c3)) {
														tempRoadBuild = c3;
														road1 = r1;
														road2 = r2;
														found = 2;
													}
												} else {
													if (tempRoadBuild == null
															|| calcResValue(tempRoadBuild) < calcResValue(c3)
															|| (calcResValue(tempRoadBuild) == calcResValue(c3)
																	&& calcDiceValue(tempRoadBuild) < calcDiceValue(
																			c3))) {
														tempRoadBuild = c3;
														road1 = r1;
														road2 = r2;
														found = 2;
													}
												}
											}
											if (search >= 3) {
												for (Road r3 : c3.getRoads()) {
													for (Crossroad c4 : r3.getCrossroads()) {
														if (!c4.equals(c3)) {
															if (checkCrossroadNeighbour(c4) && c4.getOwner() == null) {
																if (allResources()) {
																	if (tempRoadBuild == null || calcDiceValue(
																			tempRoadBuild) < calcDiceValue(c4)) {
																		tempRoadBuild = c4;
																		road1 = r1;
																		road2 = r2;
																		road3 = r3;
																		found = 3;
																	}
																} else {
																	if (tempRoadBuild == null
																			|| calcResValue(
																					tempRoadBuild) < calcResValue(c4)
																			|| (calcResValue(
																					tempRoadBuild) == calcResValue(c4)
																					&& calcDiceValue(
																							tempRoadBuild) < calcDiceValue(
																									c4))) {
																		tempRoadBuild = c4;
																		road1 = r1;
																		road2 = r2;
																		road3 = r3;
																		found = 3;
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// if (tempRoadBuild == null || tempRoadBuild.getOwner() != null ||
		// checkCrossroadNeighbour(tempRoadBuild)) {
		// for (Crossroad c : crossarr) {
		// if (c.getOwner() != null &&
		// c.getOwner().getName().equals(this.getName())) {
		// for (int i = 0; i < 3; i++) {
		// for (int j = 0; j < 2; j++) {
		// if (!c.getRoads()[i].getCrossroads()[j].equals(c)) {
		// a = c.getRoads()[i].getCrossroads()[j];
		// for (int k = 0; k < 3; k++) {
		// if (!a.getRoads()[k].equals(c.getRoads()[i])) {
		// for (int l = 0; l < 2; l++) {
		// if (!a.getRoads()[k].getCrossroads()[l].equals(a)) {
		// b = a.getRoads()[k].getCrossroads()[l];
		//
		// if (checkCrossroadNeighbour(b) && b.getOwner() == null) {
		// if (allResources()) {
		//
		// if (tempRoadBuild == null
		// || calcDiceValue(tempRoadBuild) < calcDiceValue(b)) {
		// tempRoadBuild = b;
		// road1 = c.getRoads()[i];
		// road2 = a.getRoads()[k];
		// found = 2;
		// }
		//
		// } else {
		//
		// if (tempRoadBuild == null
		// || calcResValue(tempRoadBuild) < calcResValue(b)
		// || (calcResValue(tempRoadBuild) == calcResValue(b)
		// && calcDiceValue(tempRoadBuild) < calcDiceValue(
		// b))) {
		// tempRoadBuild = b;
		// road1 = c.getRoads()[i];
		// road2 = a.getRoads()[k];
		// found = 2;
		// }
		// }
		// }
		//
		// // search-depth 3:
		//
		// if (search >= 3) {
		// for (int m = 0; m < 3; m++) {
		// if (b.getRoads()[m] != null
		// && !b.getRoads()[m].equals(a.getRoads()[k])) {
		// for (int n = 0; n < 2; n++) {
		// if (!b.getRoads()[m].getCrossroads()[n].equals(b)) {
		// d = b.getRoads()[m].getCrossroads()[n];
		// if (checkCrossroadNeighbour(d)
		// && d.getOwner() == null) {
		// if (allResources()) {
		//
		// if (tempRoadBuild == null || calcDiceValue(
		// tempRoadBuild) < calcDiceValue(d)) {
		// tempRoadBuild = d;
		// road1 = c.getRoads()[i];
		// road2 = a.getRoads()[k];
		// road3 = b.getRoads()[m];
		// found = 3;
		// }
		//
		// } else {
		//
		// if (tempRoadBuild == null || calcResValue(
		// tempRoadBuild) < calcResValue(d)
		// || (calcResValue(
		// tempRoadBuild) == calcResValue(
		// d)
		// && calcDiceValue(
		// tempRoadBuild) < calcDiceValue(
		// d))) {
		// tempRoadBuild = d;
		// road1 = c.getRoads()[i];
		// road2 = a.getRoads()[k];
		// road3 = b.getRoads()[m];
		// found = 3;
		// }
		//
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }
		// }

		if (targetCity == null) {
			for (Crossroad c : crossarr) {
				if (c.getOwner() != null && c.getOwner().getName().equals(this.getName()) && c.getBuild() == 1) {
					if (targetCity == null)
						targetCity = c;
					else {
						if (targetCity.getValue() < c.getValue())
							targetCity = c;
					}
				}
			}
		}

		if (road1 != null) {
			this.trade("road");
			this.execute("buildRoad," + road1.getId());
			if (this.getBoard().getRoads()[road1.getId()].getOwner() != null) {
				road1 = null;
			}
		}
		if (road1 == null && road2 != null) {
			this.trade("road");
			this.execute("buildRoad," + road2.getId());
			if (this.getBoard().getRoads()[road2.getId()].getOwner() != null) {
				road2 = null;
			}
		}

		switch (found) {
		case 2:
			if (road1 == null && road2 == null && tempRoadBuild != null) {
				this.trade("settlement");
				this.execute("buildSettlement," + tempRoadBuild.getId());
				if (this.getBoard().getCrossroads()[tempRoadBuild.getId()].getOwner() != null) {
					tempRoadBuild = null;
				}
			}
			break;
		case 3:
			if (road1 == null && road2 == null && road3 != null) {
				this.trade("road");
				this.execute("buildRoad," + road3.getId());
				if (this.getBoard().getCrossroads()[tempRoadBuild.getId()].getOwner() != null) {
					road3 = null;
				}
			}
			if (road1 == null && road2 == null && road3 == null && tempRoadBuild != null) {
				this.trade("settlement");
				this.execute("buildSettlement," + tempRoadBuild.getId());
				if (this.getBoard().getCrossroads()[tempRoadBuild.getId()].getOwner() != null) {
					tempRoadBuild = null;
				}
			}
			break;
		}
//		if (tempRoadBuild != null && (tempRoadBuild.getOwner() != null || checkCrossroadNeighbour(tempRoadBuild)
//				|| (road1.getOwner() != null && !road1.getOwner().getName().equals(this.getName()))
//				|| (road2.getOwner() != null && !road2.getOwner().getName().equals(this.getName()))
//				|| (road3.getOwner() != null && !road3.getOwner().getName().equals(this.getName())))) {
//			road1 = null;
//			road2 = null;
//			road3 = null;
//			tempRoadBuild = null;
//		}

		if (targetCity != null) {
			this.trade("city");
			this.execute("buildCity," + targetCity.getId());
			if (targetCity.getBuild() == 2)
				targetCity = null;
		}
	}

	private void trade(String reason) {
		switch (reason) {
		case "city":
			if (this.getWheat() < 2) {
				if (this.getWood() >= 4) {
					this.setWood(this.getWood() - 4);
					this.setWheat(this.getWheat() + 1);
				} else if (this.getSheep() >= 4) {
					this.setSheep(this.getSheep() - 4);
					this.setWheat(this.getWheat() + 1);
				} else if (this.getClay() >= 4) {
					this.setClay(this.getClay() - 4);
					this.setWheat(this.getWheat() + 1);
				} else if (this.getOre() >= 7) {
					this.setOre(this.getOre() - 4);
					this.setWheat(this.getWheat() + 1);
				}
			}
			if (this.getOre() < 3) {
				if (this.getWood() >= 4) {
					this.setWood(this.getWood() - 4);
					this.setOre(this.getOre() + 1);
				} else if (this.getSheep() >= 4) {
					this.setSheep(this.getSheep() - 4);
					this.setOre(this.getOre() + 1);
				} else if (this.getClay() >= 4) {
					this.setClay(this.getClay() - 4);
					this.setOre(this.getOre() + 1);
				} else if (this.getOre() >= 6) {
					this.setWheat(this.getWheat() - 4);
					this.setOre(this.getOre() + 1);
				}
			}
			break;
		case "settlement":
			if (this.getWheat() < 1) {
				if (this.getWood() > 4) { // wood to wheat
					this.setWood(this.getWood() - 4);
					this.setWheat(this.getWheat() + 1);
				} else if (this.getSheep() > 4) { // sheep to wheat
					this.setSheep(this.getSheep() - 4);
					this.setWheat(this.getWheat() + 1);
				} else if (this.getClay() > 4) { // clay to wheat
					this.setClay(this.getClay() - 4);
					this.setWheat(this.getWheat() + 1);
				} else if (this.getOre() >= 4) { // ore to wheat
					this.setOre(this.getOre() - 4);
					this.setWheat(this.getWheat() + 1);
				}
			}
			if (this.getClay() < 1) {
				if (this.getWood() > 4) {
					this.setWood(this.getWood() - 4);
					this.setClay(this.getClay() + 1);
				} else if (this.getSheep() > 4) {
					this.setSheep(this.getSheep() - 4);
					this.setClay(this.getClay() + 1);
				} else if (this.getWheat() > 4) {
					this.setWheat(this.getWheat() - 4);
					this.setClay(this.getClay() + 1);
				} else if (this.getOre() >= 4) {
					this.setOre(this.getOre() - 4);
					this.setClay(this.getClay() + 1);
				}
			}
			if (this.getWood() < 1) {
				if (this.getClay() > 4) {
					this.setClay(this.getClay() - 4);
					this.setWood(this.getWood() + 1);
				} else if (this.getSheep() >= 4) {
					this.setSheep(this.getSheep() - 4);
					this.setWood(this.getWood() + 1);
				} else if (this.getWheat() > 4) {
					this.setWheat(this.getWheat() - 4);
					this.setWood(this.getWood() + 1);
				} else if (this.getOre() > 4) {
					this.setOre(this.getOre() - 4);
					this.setWood(this.getWood() + 1);
				}
			}
			if (this.getSheep() < 1) {
				if (this.getClay() > 4) {
					this.setClay(this.getClay() - 4);
					this.setSheep(this.getSheep() + 1);
				} else if (this.getWood() >= 4) {
					this.setWood(this.getWood() - 4);
					this.setSheep(this.getSheep() + 1);
				} else if (this.getWheat() > 4) {
					this.setWheat(this.getWheat() - 4);
					this.setSheep(this.getSheep() + 1);
				} else if (this.getOre() > 4) {
					this.setOre(this.getOre() - 4);
					this.setSheep(this.getSheep() + 1);
				}
			}
			break;
		case "road":
			if (this.getClay() < 1) {
				if (this.getWood() >= 4) {
					this.setWood(this.getWood() - 4);
					this.setClay(this.getClay() + 1);
				} else if (this.getSheep() >= 4) {
					this.setSheep(this.getSheep() - 4);
					this.setClay(this.getClay() + 1);
				} else if (this.getWheat() >= 4) {
					this.setWheat(this.getWheat() - 4);
					this.setClay(this.getClay() + 1);
				} else if (this.getOre() >= 4) {
					this.setOre(this.getOre() - 4);
					this.setClay(this.getClay() + 1);
				}
			}
			if (this.getWood() < 1) {
				if (this.getClay() >= 4) {
					this.setClay(this.getClay() - 4);
					this.setWood(this.getWood() + 1);
				} else if (this.getSheep() >= 4) {
					this.setSheep(this.getSheep() - 4);
					this.setWood(this.getWood() + 1);
				} else if (this.getWheat() >= 4) {
					this.setWheat(this.getWheat() - 4);
					this.setWood(this.getWood() + 1);
				} else if (this.getOre() >= 4) {
					this.setOre(this.getOre() - 4);
					this.setWood(this.getWood() + 1);
				}
			}
			break;
		}
	}

	/**
	 * Method called by Catan in the first turn when it's the turn of the AI.
	 * 
	 * @param crossarr
	 * @return
	 */
	public static int placeStartSettlement(Crossroad[] crossarr) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return getMaxCrossroad(crossarr);
	}

	/**
	 * This method checks for the crossroad with the highest value. Used in the
	 * first two turns of the game, when the AI has to decide where to build the
	 * first settlements. Most likely it blocks the best spaces.
	 * 
	 * @param crossarr
	 *            the list of crossroads
	 * @return the crossroad with maximum value
	 */
	private static int getMaxCrossroad(Crossroad[] crossarr) {
		ArrayList<Crossroad> crosslist = new ArrayList<Crossroad>();
		// sorts out all crossroads where building is allowed
		for (int i = 0; i < 54; i++) {
			if (crossarr[i].getOwner() == null && checkCrossroadNeighbour(crossarr[i]))
				crosslist.add(crossarr[i]);
		}

		// gets the crossroad with max-value
		Crossroad max = crosslist.get(0);

		for (Crossroad c : crosslist) {
			if (c.getValue() > max.getValue()) {
				max = c;
			}
		}

		return max.getId();
	}

	private static boolean checkCrossroadNeighbour(Crossroad cross) {
		// Checks if it's allowed to build on Crossroad cross
		boolean passedTest = true;
		for (int i = 0; i < 3; i++) {
			if (cross.getRoads()[i] != null) {
				for (int j = 0; j < 2; j++) {
					Road road = cross.getRoads()[i];
					// checks cross itself, does nothing
					if (road.getCrossroads()[j].equals(cross)) {
						if (cross.getOwner() != null) {
							passedTest = false;
						}
					}
					// allowed
					else if (road.getCrossroads()[j].getOwner() == null) {
						// //return true;
					}
					// not allowed
					else {
						passedTest = false;
					}
				}
			}
		}

		return passedTest;
	}

	private static String aiName() {
		Random rand = new Random();
		return ainames[rand.nextInt(ainames.length)];
	}

	private int aiSearch() {
		String a = this.getName();
		if (a == "GLaDOS" || a == "Ultron" || a == "Jarvis" || a == "Skynet")
			return 3;
		else
			return 2;
	}

	public int getBestThiefTile(Tile[] tilearr) {
		ArrayList<Tile> tilelist = new ArrayList<Tile>();
		for (int i = 0; i < 19; i++) {
			if (checkTile(tilearr[i]))
				tilelist.add(tilearr[i]);
		}

		Tile max = tilelist.get(0);
		for (Tile t : tilelist) {
			if (t.getValue() > max.getValue()) {
				max = t;
			}
		}

		return max.getId();
	}

	private boolean checkTile(Tile tile) {
		for (int i = 0; i < 6; i++) {
			if (tile.getCrossroads()[i].getOwner() == this)
				return false;
		}
		return true;
	}

	private int calcResValue(Crossroad c) {
		boolean wood = false;
		boolean sheep = false;
		boolean wheat = false;
		boolean clay = false;
		boolean ore = false;
		int value = 0;
		for (int i = 0; i < 3; i++) {
			switch (c.getTiles()[i].getResource()) {
			case "wood":
				wood = true;
				break;
			case "sheep":
				sheep = true;
				break;
			case "wheat":
				wheat = true;
				break;
			case "clay":
				clay = true;
				break;
			case "ore":
				ore = true;
				break;
			}
		}
		if (wood && !resources[0])
			value++;
		if (sheep && !resources[1])
			value++;
		if (wheat && !resources[2])
			value++;
		if (clay && !resources[3])
			value++;
		if (ore && !resources[4])
			value++;
		return value;
	}

	private int calcDiceValue(Crossroad c) {
		int value = 0;
		for (int i = 0; i < 3; i++) {
			switch (c.getTiles()[i].getDicenum()) {
			case 2:
			case 12:
				value += 1;
				break;
			case 3:
			case 11:
				value += 2;
				break;
			case 4:
			case 10:
				value += 3;
				break;
			case 5:
			case 9:
				value += 4;
				break;
			case 6:
			case 8:
				value += 5;
				break;
			}
		}
		return value;
	}

	private boolean allResources() {
		if (resources[0] && resources[1] && resources[2] && resources[3] && resources[4])
			return true;
		else
			return false;
	}
	/**
	 * 
	 * TODO convert methods to non-static TODO chat messages
	 */
	/*
	 * Tactic AI: Place the first two cities on crossroads of highest value
	 * available find the crossroad within two roads of a city with the highest
	 * value ( available) and set road in that direction save the city
	 * 
	 * if AI has resources to build a settlement and has reached the city place
	 * the city
	 * 
	 * else, if AI has resources for a road, place that
	 * 
	 * else if AI has resources to place a city, place it on the settlement with
	 * the highest value
	 * 
	 * if any of the above apply: reset a counter if none of the above apply
	 * count up the counter by one
	 * 
	 * if bot has 7 or more resources, but cannot build anything, trade
	 * resources to one of the resources needed to: build a settlement if he
	 * would have the other resourcs and has reached it, else, get resources
	 * needed for a road, build it if possible
	 */
}