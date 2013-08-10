package com.sparkfighters.shard.loader;

import java.util.ArrayList;

public class JSONBattleDTO {
	public ArrayList<JSONUserDTO> users = new ArrayList<>();	
	public int map;
	
	public String content_fs_path;
	
	public String netifc;
	public int netport;
	
	
	/**
	 * Either returns a JSONUserDTO with specified login, or null
	 * @param login Login to find a JSONUserDTO
	 * @return null if not found; DTO object is found
	 */
	public JSONUserDTO find_by_login(String login) {
		for (JSONUserDTO user : this.users)
			if (user.username.equals(login))
				return user;
		return null;
	}
}
