package telran.net.games.model;

import java.time.LocalDate;

import org.json.JSONObject;


public record GamerDto(String username, LocalDate birthday) {
	private static final String BIRTHDAY_FIELD = "birthday";
	private static final String USERNAME_FIELD = "username";
	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(USERNAME_FIELD, username);
		jsonObject.put(BIRTHDAY_FIELD, birthday);
		return jsonObject.toString();
	}
	public GamerDto(JSONObject jsonObject) {
		this(jsonObject.getString(USERNAME_FIELD), LocalDate.parse(jsonObject.getString(BIRTHDAY_FIELD)));
	}
}
