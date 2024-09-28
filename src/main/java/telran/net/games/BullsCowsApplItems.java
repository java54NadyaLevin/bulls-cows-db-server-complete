package telran.net.games;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import telran.net.games.model.GamerDto;
import telran.net.games.service.BullsCowsService;
import telran.view.*;

public class BullsCowsApplItems {
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
		username  = getName;
		io.writeLine("User was registered");
		enterGame(io);
		
	}
	static void enterGame(InputOutput io) {
		Menu menu = new Menu("Guess sequence of digits", new Item[] {
				Item.of("Start game" ,
						BullsCowsApplItems::startGame),
				Item.of("Continue game" ,
						BullsCowsApplItems::continueGame),
				Item.of("Join game" ,
						BullsCowsApplItems::joinGame),
				Item.ofExit()
		});
		menu.perform(io);
	}
	static void startGame(InputOutput io) {

	}
	static void continueGame(InputOutput io) {

	}
	static void joinGame(InputOutput io) {

	}
	
	static void playGame(InputOutput io) {
		Menu menu = new Menu("Guess sequence of digits", new Item[] {
				Item.of("Make a move" ,
						BullsCowsApplItems::makeMove),
				Item.ofExit()
		});
		menu.perform(io);
	}
	
	static void makeMove(InputOutput io) {

	}
//	static void guessItem(InputOutput io) {
//		String guess = io.readStringPredicate("enter non-repeated digits",
//				"Wrong input", str -> str.chars()
//				.distinct().filter(c -> c >= '0' && c <= '9')
//				.count() == 4);
//		List<MoveResult> history = bullsCows.moveProcessing(new SequenceGameGamer(gameId, guess));
//		history.forEach(io::writeLine);
//		if (bullsCows.gameOver(gameId)) {
//			io.writeLine("Congratulations: you are winner");
//		}
//	}
}
