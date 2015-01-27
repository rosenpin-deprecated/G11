package bots;

import java.util.List;

import pirates.game.Direction;
import pirates.game.Island;
import pirates.game.Location;
import pirates.game.Pirate;
import pirates.game.PirateBot;
import pirates.game.PirateGame;

public class MyBot implements PirateBot {
	boolean isFirstTurn = true;
	List<Island> MyIslands;
	List<Pirate> pirate;
	List<Island> NotMyIslands;
	List<Direction> dir;
	PirateGame game;
	private void constructor(PirateGame game){
		this.game = game;
		MyIslands = game.myIslands();
		NotMyIslands = game.notMyIslands();
		pirate = game.myPirates();
	}
	@Override
	public void doTurn(PirateGame game) {
		if(isFirstTurn){
			constructor(game);
		}

		refresh();
		game.debug("You dont own "+NotMyIslands.size()+" islands");
	}


	private void refresh(){
		if(pirate.size()==0){
			game.debug("YO DEAD");
		}
		else if(pirate.size()<2){
			for (Pirate aPirate : pirate) {
				List<Direction> direction = game.getDirections(aPirate, aPirate.getInitialLocation());
				MovePlayerToLocation(aPirate, direction);
			}
		}
		else {
			MyIslands = game.myIslands();
			NotMyIslands = game.notMyIslands();
			if (NotMyIslands.size() == 1) {
				game.debug("WIN");
			} else {
				try {
					for (int i = 0; i < pirate.size(); i++) {
						if (i < pirate.size() / 2) {
							dir = game.getDirections(pirate.get(i), NotMyIslands.get(0));
						} else {
							dir = game.getDirections(pirate.get(i), NotMyIslands.get(1));
						}
						for (Direction aDir : dir) {
							game.setSail(pirate.get(i), aDir);
						}
					}
				} catch (IndexOutOfBoundsException e) {
					for (Pirate i : pirate) {
						if (!isOnIsland(i)) {
							for (int k = 0; k < closestIslandDir(MyIslands, i).size(); k++) {
								game.setSail(i, closestIslandDir(MyIslands, i).get(k));
							}
						}
					}
					e.printStackTrace();
				}
			}
		}
	}
	private boolean isOnIsland(Pirate pirate1){
		for (Island MyIsland : MyIslands) {
			if (pirate1.getLocation() == MyIsland.getLocation()) {
				return true;
			}
		}
		return false;
	}
	private List<Direction> closestIslandDir(List<Island> islands,Pirate pirateToCheck){
		List<Direction> closest = game.getDirections(pirateToCheck,islands.get(0));
		for (Island island : islands) {
			List<Direction> dir = game.getDirections(pirateToCheck, island);
			if (dir.size() < closest.size()) {
				closest = dir;
			}
		}
		return closest;
	}

	private void MovePlayerToLocation(Pirate pirate1,List<Direction> directions){
		for (Direction direction : directions) {
			game.setSail(pirate1, direction);
		}
	}
}
