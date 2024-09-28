package telran.net.games;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import telran.net.Request;
import telran.net.TcpClient;
import telran.net.games.model.*;
import telran.net.games.service.BullsCowsService;
public class BullsCowsProxy implements BullsCowsService {
	TcpClient tcpClient;

	public BullsCowsProxy(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}

	@Override
	public long createGame() {
		String strRes = tcpClient.sendAndReceive(new Request("createGame", ""));
		return Long.parseLong(strRes);
	}

	@Override
	public List<String> startGame(long gameId) {
		String strRes = tcpClient.sendAndReceive(new Request("startGame", "" + gameId));
		return Arrays.asList(strRes.split(";"));
	}

	@Override
	public void registerGamer(String username, LocalDate birthDate) {
		GamerDto gamerDto = new GamerDto(username, birthDate);
		tcpClient.sendAndReceive(new Request("registerGamer", gamerDto.toString()));
	}

	@Override
	public void gamerJoinGame(long gameId, String username) {
		GameGamerDto gameGamerDto = new GameGamerDto(gameId, username);
		tcpClient.sendAndReceive(new Request("gamerJoinGame", gameGamerDto.toString()));
	}

	@Override
	public List<Long> getNotStartedGames() {
		String strRes = tcpClient.sendAndReceive(new Request("getNotStartedGames", ""));
		return Arrays.stream(strRes.split(";")).map(s -> Long.valueOf(s)).toList();
	}

	@Override
	public List<MoveData> moveProcessing(String sequence, long gameId, String username) {
		SequenceGameGamerDto SequenceGameGamerDto = new SequenceGameGamerDto(sequence, gameId, username);
		String strRes =tcpClient.sendAndReceive(new Request("moveProcessing", SequenceGameGamerDto.toString()));
		return Arrays.stream(strRes.split(";")).map(s -> new MoveData(new JSONObject(s))).toList();
	}

	@Override
	public boolean gameOver(long gameId) {
		String strRes = tcpClient.sendAndReceive(new Request("gameOver", "" + gameId));
		return Boolean.parseBoolean(strRes);
	}

	@Override
	public List<String> getGameGamers(long gameId) {
		String strRes = tcpClient.sendAndReceive(new Request("getGameGamers", "" + gameId));
		return Arrays.asList(strRes.split(";"));
	}

	@Override
	public String loginGamer(String username) {
		return tcpClient.sendAndReceive(new Request("loginGamer", username));
	
	}

	@Override
	public List<Long> getNotStartedGamesWithGamer(String username) {
		String strRes = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithGamer", username));
		return Arrays.stream(strRes.split(";")).map(s -> Long.valueOf(s)).toList();
	}

	@Override
	public List<Long> getNotStartedGamesWithNoGamer(String username) {
		String strRes = tcpClient.sendAndReceive(new Request("getNotStartedGamesWithNoGamer", username));
		return Arrays.stream(strRes.split(";")).map(s -> Long.valueOf(s)).toList();
	}

	@Override
	public List<Long> getStartedGamesWithGamer(String username) {
		String strRes = tcpClient.sendAndReceive(new Request("getStartedGamesWithGamer", username));
		return Arrays.stream(strRes.split(";")).map(s -> Long.valueOf(s)).toList();
	}

}
