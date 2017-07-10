package boa.datagen;


import java.io.File;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import boa.datagen.forges.github.RepoMetadata;
import boa.datagen.util.FileIO;
import boa.datagen.util.Properties;
import boa.types.Toplevel.Project;

public class CacheGithubJSON {
	final static String jsonPath = Properties.getProperty("gh.json.path", DefaultProperties.GH_JSON_PATH);
	final static String jsonCachePath = Properties.getProperty("gh.json.cache.path", DefaultProperties.GH_JSON_CACHE_PATH);
	
	public static void main(String[] args) {
		final long startTime = System.currentTimeMillis();
		HashMap<String, byte[]> repos = new HashMap<String, byte[]>();
		File dir = new File(jsonPath + "/repos");
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith(".json")) {
				String content = FileIO.readFileContents(file);
				Gson parser = new Gson();
				JsonArray repoArray = parser.fromJson(content, JsonElement.class).getAsJsonArray();
				for (int i = 0; i < repoArray.size(); i++) {
					RepoMetadata repo = new RepoMetadata(repoArray.get(i).getAsJsonObject());
					if (repo.id != null && repo.name != null) {
						Project protobufRepo = repo.toBoaMetaDataProtobuf();
						// System.out.println(jRepo.toString());
						repos.put(repo.id, protobufRepo.toByteArray());
						System.out.println(repos.size() + ": " + repo.id + " " + repo.name);
					}
				}
			}
		}
		File output = new File(jsonCachePath);
		output.mkdirs();
		FileIO.writeObjectToFile(repos, jsonCachePath + "/buf-map", false);

		System.out.println("Time: " + (System.currentTimeMillis() - startTime) / 1000);
	}
}
	
