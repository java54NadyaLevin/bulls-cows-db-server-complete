package telran.net.games;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import telran.net.games.model.MoveData;
import telran.net.games.service.BullsCowsService;
import telran.view.*;

public class BullsCowsApplItems {
	private static final int N_DIGITS = 4;
	static BullsCowsService bullsCows;
	static long gameId;
	static String username;
	static LocalDate birthday;

	public static List<Item> getItems(BullsCowsService bullsCows) {
		BullsCowsApplItems.bullsCows = bullsCows;
		Item[] items = { Item.of("Login", BullsCowsApplItems::loginGamer),
				Item.of("Register", BullsCowsApplItems::registerGamer) };
		return new ArrayList<>(List.of(items));
	}

	static void loginGamer(InputOutput io) {
		String getName = io.readString("Enter your name");
		username = bullsCows.loginGamer(getName);
		enterGame(io);
	}

	static void registerGamer(InputOutput io) {
		String getName = io.readString("Enter your name");
		LocalDate birthdate = io.readIsoDateRange("Enter you birthdate", "Wrong date", LocalDate.MIN,
				LocalDate.now().plusDays(1));
		bullsCows.registerGamer(getName, birthdate);
		username = getName;
		io.writeLine("User was registered");
		enterGame(io);

	}

	static void enterGame(InputOutput io) {
		Menu menu = new Menu("What do you want to do?",
				new Item[] { Item.of("Start and play existing game", BullsCowsApplItems::startExistingGame),
						Item.of("Continue game", BullsCowsApplItems::continueGame),
						Item.of("Join game", BullsCowsApplItems::joinGame),
						Item.of("Create new game", BullsCowsApplItems::createNewGame), Item.ofExit() });
		menu.perform(io);
	}

	static void startExistingGame(InputOutput io) {
		try {
			List<Long> games = bullsCows.getNotStartedGamesWithGamer(username);
			games.forEach(io::writeLine);
			Long getGameId = io.readLong("Enter gameId", "incorrect id"); 
			if(checkGameAvailable(io, games, getGameId)) {
				bullsCows.startGame(getGameId);
				playGame(io);
			};
		} catch (Exception e) {
			io.writeLine("No games available");
		}
		
	}

	private static boolean checkGameAvailable(InputOutput io, List<Long> games, Long getGameId) {
		
		if(games.contains(getGameId)) {
			gameId = getGameId;
		}else {
			io.writeLine("Incorrect id: this game is not available for you");
		}
		return games.contains(getGameId);
	}

	static void createNewGame(InputOutput io) {
		Long createdGameId = bullsCows.createGame();
		bullsCows.gamerJoinGame(createdGameId, username);
		io.writeLine("New game has been created");
	}

	static void continueGame(InputOutput io) {
		List<Long> games = new ArrayList();
		try {
			games = bullsCows.getStartedGamesWithGamer(username);
			games.forEach(io::writeLine);
			Long getGameId = io.readLong("Enter id of the game you want to continue", "incorrect id");
			if(checkGameAvailable(io, games, getGameId)) {
				playGame(io);
			};	
		} catch (Exception e) {
			io.writeLine("No games available");
		}

	}

	static void joinGame(InputOutput io) {
		try {
			List<Long> games = bullsCows.getNotStartedGamesWithNoGamer(username);
			games.forEach(io::writeLine);
			Long getGameId = io.readLong("Enter id of the game you want to join", "incorrect id");
			if(checkGameAvailable(io, games, getGameId)) {
				bullsCows.gamerJoinGame(gameId, username);
				io.writeLine("You've joined to game with id " + gameId);
			};
		} catch (Exception e) {
			io.writeLine("No games available");
		}
		
	}

	static void playGame(InputOutput io) {
		Menu menu = new Menu("Guess sequence of digits",
				new Item[] { Item.of("Make a move", BullsCowsApplItems::makeMove), Item.ofExit() });
		menu.perform(io);
	}

	static void makeMove(InputOutput io) {
		String guess = io.readStringPredicate("Enter "+ N_DIGITS + " non-repeated digits", "Wrong input",
				str -> str.chars().distinct().filter(c -> c >= '0' && c <= '9').count() == N_DIGITS);
		List<MoveData> moves = bullsCows.moveProcessing(guess, gameId, username);
		moves.forEach(io::writeLine);
		if (bullsCows.gameOver(gameId)) {
			io.writeLine("Congratulations: you are winner!!!");
			enterGame(io);
		}

	}
}
